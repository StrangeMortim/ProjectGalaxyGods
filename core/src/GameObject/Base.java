package GameObject;


import Action.Buff;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Map;

public class Base extends Unit implements IBase,Serializable {

    private int labRoundsRemaining=-1;
    private int caserneRoundsRemaining=-1;
    private HashMap<Unit, Integer> recruiting = new HashMap<Unit, Integer>();
    private List<UnitType> avaibleUnits = new ArrayList<UnitType>();
    private HashMap<Research, Integer> researching = new HashMap<Research,Integer>();
    private List<Research> researched = new ArrayList<Research>();


    public Base(UnitType type, Player owner) {
        super(type, owner);
        /*TODO implement*/
    }

    @Override
    public List<Buff> update(){
        List<Buff> result = new ArrayList<Buff>();

        //receive ressources from near units
        currentField.getNearUnits().stream().filter(u -> u.getOwner() == this.owner).forEach(u -> {
            owner.getRessources()[0] += u.getRessources()[0];
            u.getRessources()[0] = 0;
            owner.getRessources()[1] += u.getRessources()[1];
            u.getRessources()[1] = 0;
        });

        //count down if lab is in building state
        if(labRoundsRemaining > 0)
            labRoundsRemaining--;

        //count down if caserne is in building state
        if(caserneRoundsRemaining > 0)
            caserneRoundsRemaining--;

        //count all recruiting states down and if finished, spawn them
        Iterator it = recruiting.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Unit,Integer> entry = (Map.Entry)it.next();
            entry.setValue(entry.getValue()-1);
            if(entry.getValue() <= 0){
                if(this.spawnUnit(entry.getKey()))
                    it.remove(); //remove if spawn was successfull
            }
        }

        /*TODO add research*/
        //count all researches down and if finished add them to the return list
        it = researching.entrySet().iterator();
        Research current = null;
        while (it.hasNext()){
            Map.Entry<Research,Integer> entry = (Map.Entry)it.next();
            entry.setValue(entry.getValue()-1);
            if(entry.getValue() <= 0){
                current = entry.getKey();
                if(current.isPermanet()) {
                    owner.getPermaBuffs().add(current);
                } else {
                    owner.getTemporaryBuffs().add(current);
                }
                result.addAll(this.registerNewBuff(current));
                //result.add(entry.getKey());
                it.remove();  //remove if finished
            }
        }

        return result;
    }

    private List<Buff> registerNewBuff(Research toRegister){
        Field tmp[][] = currentField.getMap().getFields();
        List<Buff> result = new ArrayList<>();

        Buff current = null;
        Unit currentUnit = null;
                if(toRegister.isPermanet()){
                    current = new Buff(null, null, owner);
                    current.setSource(toRegister);
                    for(Field[] fArray: tmp){
                        for(Field f: fArray){
                            currentUnit = f.getCurrent();
                            if(currentUnit != null){
                                current.setOrigin(currentUnit);
                                current.execute();
                                current.setFirstTime(false);
                            }
                        }
                    }
                } else {
                    for(Field[] fArray: tmp){
                        for(Field f: fArray){
                            currentUnit = f.getCurrent();
                            if(currentUnit != null){
                                current = new Buff(currentUnit,null,owner);
                                current.setSource(toRegister);
                                result.add(current);
                            }
                        }
                    }
                }
        return result;

    }

    /**
     * Hilfsmethode zum spawnen von einheiten, iteriert solange durch bis ein freies feld gefunden wurde
     * und platziert dann die einheit, wird kein feld gefunden und der such bereich( startpunkt +- 7) überschritte
     * beendet die methode ohne die einheit zu platzieren und gibt false zurück
     *
     * @param u die zu platzierende Einheit
     * @return gibt an ob ein feld gefunden wurde
     */
    private boolean spawnUnit(Unit u){
        int xPos = currentField.getXPos();
        int yPos = currentField.getYPos();

        int range = 1;

        Field current = null;

        //iterate as long as the range does not exceed the map bounds in every way TODO from hardcoded to dynamic bounds
        while (range < 7) {
            //iterate on column level
            for (int i = yPos - range; i <= yPos + range; ++i) {

                //iterate on row level
                for (int k = xPos - range; k <= xPos + range; ++k)
                    current = currentField.getMap().getField(k, i);
                //if field found and free, place unit
                if (current != null && current.getCurrent() == null) {
                    current.setCurrent(u);
                    currentField.getMap().getSession().registerUnit(u);
                    return true;
                }
            }
            range++;
        }

        return false;
    }

    /**
     * Erstellt eine neue Unit vom uebergebenen Typ
     *
     * @param type der Typ der gewuenschten Unit
     * @return ob der Vorgang moeglich ist(und dementsprechend ausgefuehrt wird)
     */
    @Override
    public boolean createUnit(UnitType type) {
        if(avaibleUnits.contains(type))
        {

            int[] cost = type.getRessourceCost();
            int[] ressourcesAvailable = owner.getRessources();

            //Check if enough ressources, if not return result(null)
            for (int j = 0; j<4; ++j)
                if(cost[j] > ressourcesAvailable[j])
                   return  false;


                for(int i = 0; i < 4; ++i)
                owner.getRessources()[i] -= cost[i];

                recruiting.put(new Unit(type, owner), type.getRecruitingTime());

        }
        /*TODO check*/
        return true;
    }

    /**
     * Wird direkt ausgefuehrt nicht erst am Ende der Runde
     *
     * Bricht das Erstellen der uebergebenen Einheit ab,
     * wenn diese Einheit bereits fertig erstellt ist oder nicht existiert(null) passiert nichts)
     *
     * @param which die Einheit deren Erstellung abgebrochen werden soll
     */
    @Override
    public void abortCreation(Unit which) {
        if(recruiting.containsKey(which))
        {
            UnitType whichType = which.getType();
            int[] originalCost = whichType.getRessourceCost();
            float ressourcesRemaining = recruiting.get(which) / whichType.getRecruitingTime();

            for(int i = 0; i<4; ++i)
            owner.getRessources()[i] += (ressourcesRemaining* originalCost[i]);

            recruiting.remove(which);
        }
                    /*TODO check*/
    }

    /**
     * Startet den Bau des Labors
     *
     * @return gibt an ob der Bau gestartet ist, false wenn nicht alle bedingungen erfuellt sind
     */
    @Override
    public boolean buildLab() {
        if(labRoundsRemaining == -1){
            int[] ressourceCost = Building.LABOR.getRessourceCost();
            int[] avaibleRessources = owner.getRessources();

            //check if enough ressources, if not return false
            for(int i=0; i<4; ++i)
                if (ressourceCost[i] > avaibleRessources[i])
                    return false;

            for(int j=0; j<4; ++j)
                owner.getRessources()[j] -= ressourceCost[j];

            labRoundsRemaining = Building.LABOR.getBuildTime();
        }
                /*TODO check*/
        return false;
    }

    /**
     * Bricht den Bau des Labors ab, wenn er statt findet sonst passiert nichts
     * Der Spieler erhaelt einen Teil der Einheiten zurueck, abhaengig vom Fortschritt
     */
    @Override
    public void abortLab() {
        if(labRoundsRemaining > 0){
            float ressourcesLeft = labRoundsRemaining / Building.LABOR.getBuildTime();
            labRoundsRemaining = -1;

            int[] originalCost = Building.LABOR.getRessourceCost();

            for(int i=0; i<4;++i)
                owner.getRessources()[i] += (ressourcesLeft*originalCost[i]);
        }
        /*TODO check*/
    }

    /**
     * Analog zu buildLab fuer die Kaserne
     */
    @Override
    public boolean buildCaserne() {
        if(caserneRoundsRemaining == -1){
            int[] ressourceCost = Building.CASERNE.getRessourceCost();
            int[] avaibleRessources = owner.getRessources();

            //check if enough ressources, if not return false
            for(int i=0; i<4; ++i)
                if (ressourceCost[i] > avaibleRessources[i])
                    return false;

            for(int j=0; j<4; ++j)
                owner.getRessources()[j] -= ressourceCost[j];

            caserneRoundsRemaining = Building.CASERNE.getBuildTime();
        }
                /*TODO check*/
        return false;
    }

    /**
     * Analog zu abortLab fuer die Kaserne
     */
    @Override
    public void abortCaserne() {
        if(caserneRoundsRemaining > 0){
            float ressourcesLeft = caserneRoundsRemaining / Building.CASERNE.getBuildTime();
            caserneRoundsRemaining = -1;

            int[] originalCost = Building.CASERNE.getRessourceCost();

            for(int i=0; i<4;++i)
                owner.getRessources()[i] += (ressourcesLeft*originalCost[i]);
        }
        /*TODO check*/
    }

    /**
     * Baut den Marktplatz
     */
    @Override
    public boolean buildMarket() throws RemoteException {
        if(owner.getMarket())
            return true;

        if(owner.getRessources()[0] >= 100 && owner.getRessources()[2] >= 100){
            owner.getRessources()[0] -= 100;
            owner.getRessources()[2] -= 100;
            owner.setMarket(true);
            return true;
        }
        return false;
    }

    /**
     * Startet das Erforschen des uebergebenen Forschungsobjektes
     *
     * @param research
     * @return true wenn der Start erfolgreich war(alle bedingungen erfuellt und noch nicht erforscht), sonst false
     */
    @Override
    public boolean research(Research research) {
        if(!researched.contains(research)) {
            int[] ressourceCost = research.getRessourceCost();
            int[] avaibleRessources = owner.getRessources();

            //check if enough ressources, if not return false
            for (int i = 0; i < 4; ++i)
                if (ressourceCost[i] > avaibleRessources[i])
                    return false;

            for (int j = 0; j < 4; ++j)
                owner.getRessources()[j] -= ressourceCost[j];

            researching.put(research, research.getResearchTime());
            researched.add(research);
            return true;
        }
                /*TODO check*/
        return false;
    }

    /**
     * Bricht die Erforschung der uebergebenen Forschung ab
     * wird die Forschung nicht erforscht passiert nichts
     *
     * @param research
     */
    @Override
    public void abortResearch(Research research) {
        //if never researched, nothing to abort
        if(researched.contains(research)){
            //iterate to find the buff
            for(Iterator<java.util.Map.Entry<Research,Integer>> it = researching.entrySet().iterator(); it.hasNext();){
                java.util.Map.Entry<Research,Integer> entry = it.next();

                //case buff found
                if(entry.getKey() == research)
                {
                    //calc returning ressources and give them back
                    int[] originalCost = research.getRessourceCost();
                    float ressourcesLeft = entry.getValue() / research.getResearchTime();

                    for(int i=0; i<4;++i)
                        owner.getRessources()[i] += (ressourcesLeft * originalCost[i]);

                    //remove buff and research
                    it.remove();
                    researched.remove(research);
                    break;      //finish method
                }
            }


        }
        /*TODO check*/
    }

    /**
     * Getter und Setter
     *
     * @param remaining
     */
    @Override
    public void setLabRoundsRemaining(int remaining) {
        if(remaining < -1)
            this.labRoundsRemaining = -1;
        else
        this.labRoundsRemaining = remaining;
    }

    @Override
    public int getLabRoundsRemaining() {
        return labRoundsRemaining;
    }

    @Override
    public void setCaserneRoundsRemaining(int remaining) {
        if(remaining < -1)
            this.caserneRoundsRemaining = -1;
        else
            this.caserneRoundsRemaining = remaining;
    }

    @Override
    public int getCaserneRoundsRemaining() {
        return caserneRoundsRemaining;
    }

    @Override
    public void setAvaibleUnits(List<UnitType> avaibleUnits) {
        if(avaibleUnits != null)
            this.avaibleUnits = avaibleUnits;
    }

    @Override
    public List<UnitType> getAvaibleUnits() {
        return avaibleUnits;
    }

    @Override
    public void setResearched(List<Research> researched) {
        if(researched != null)
            this.researched = researched;
    }

    @Override
    public List<Research> getResearched() {
        return researched;
    }

    public HashMap<Unit, Integer> getRecruiting(){
        return recruiting;
    }
}
