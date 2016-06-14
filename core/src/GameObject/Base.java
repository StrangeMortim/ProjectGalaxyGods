package GameObject;


import Action.Action;
import Action.Buff;
import Action.UnitCreationAction;
import Player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Base extends Unit implements IBase {

    private int labRoundsRemaining=-1;
    private int caserneRoundsRemaining=-1;
    private HashMap<Unit, Integer> recruiting = new HashMap<Unit, Integer>();
    private List<UnitType> avaibleUnits = new ArrayList<UnitType>();
    private HashMap<Buff, Integer> researching = new HashMap<Buff,Integer>();
    private List<Research> researched = new ArrayList<Research>();


    public Base(UnitType type, Player owner) {
        super(type, owner);
        /*TODO implement*/
    }

    @Override
    public void update(){
        /*TODO implement*/
    }

    /**
     * Erstellt eine neue Unit vom uebergebenen Typ
     *
     * @param type der Typ der gewuenschten Unit
     * @return ob der Vorgang moeglich ist(und dementsprechend ausgefuehrt wird)
     */
    @Override
    public Action createUnit(UnitType type) {
        UnitCreationAction result = null;
        if(avaibleUnits.contains(type))
        {
            int[] cost = type.getRessourceCost();
            int[] ressourcesAvailable = owner.getRessources();

            if(cost[0] < ressourcesAvailable[0]
            && cost[1] < ressourcesAvailable[1]
            && cost[2] < ressourcesAvailable[2]
            && cost[3] < ressourcesAvailable[3])
            {
                for(int i = 0; i < 4; ++i)
                owner.getRessources()[i] -= cost[i];

                result = new UnitCreationAction(this,null,owner);
                result.setType(type);
            }
        }
        /*TODO check*/
        return result;
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
                /*TODO implement*/
        return false;
    }

    /**
     * Bricht den Bau des Labors ab, wenn er statt findet sonst passiert nichts
     * Der Spieler erhaelt einen Teil der Einheiten zurueck, abhaengig vom Fortschritt
     */
    @Override
    public void abortLab() {
        /*TODO implement*/
    }

    /**
     * Analog zu buildLab fuer die Kaserne
     */
    @Override
    public boolean buildCaserne() {
                /*TODO implement*/
        return false;
    }

    /**
     * Analog zu abortLab fuer die Kaserne
     */
    @Override
    public void abortCaserne() {
        /*TODO implement*/
    }

    /**
     * Startet das Erforschen des uebergebenen Forschungsobjektes
     *
     * @param research
     * @return true wenn der Start erfolgreich war(alle bedingungen erfuellt und noch nicht erforscht), sonst false
     */
    @Override
    public boolean research(Research research) {
                /*TODO implement*/
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
        /*TODO implement*/
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
