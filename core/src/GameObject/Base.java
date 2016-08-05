package GameObject;


import Action.Buff;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Map;

/**
 * represents the Base of a Player
 * @author Benjamin, Fabi
 */
public class Base extends Unit implements Serializable {

    /**
     * How many turns remain until the lab is finished
     */
    private int labRoundsRemaining=Constants.NONE_OR_NOT_SET;

    /**
     * How many turns remain until the caserne is finished
     */
    private int caserneRoundsRemaining=Constants.NONE_OR_NOT_SET;

    /**
     * Contains the units that are currently being recruited
     */
    private HashMap<Unit, Integer> recruiting = new HashMap<Unit, Integer>();

    /**
     * which units can be created in this base
     */
    private List<UnitType> avaibleUnits = new ArrayList<UnitType>();

    /**
     * Contains the researches that are currently being researched
     */
    private HashMap<Research, Integer> researching = new HashMap<Research,Integer>();

    /**
     * finished researches
     */
    private List<Research> researched = new ArrayList<Research>();

    public Base(UnitType type, Player owner, GameSession session) {
        super(type, owner, session);
        avaibleUnits.add(UnitType.SWORDFIGHTER);
        avaibleUnits.add(UnitType.WORKER);
    }

    /**
     * updates the building, recruiting and research processes
     * also sets the correct sprite-Indeces on the other fields
     * @return a list of buffs if the researches generated some
     */
    @Override
    public List<Buff> update(){
        List<Buff> result = new ArrayList<Buff>();

        //receive ressources from near units
        for(Unit u: currentField.getNearUnits()){
            if(u.getOwner() == this.owner){
                owner.getRessources()[Constants.WOOD] += u.getRessources()[Constants.WOOD];
                u.getRessources()[Constants.WOOD] = Constants.FINISHED;
                owner.getRessources()[Constants.IRON] += u.getRessources()[Constants.IRON];
                u.getRessources()[Constants.IRON] = Constants.FINISHED;
            }
        }

        //receive resources from units on the other side, since a base takes up 4 fields
        //with this only up right and down left corner aren't covered, but those are just to far away from a
        //gate into the town...
        for(Unit u: currentField.getMap().getField(currentField.getXPos()+1,currentField.getYPos()-1).getNearUnits()){
            if(u.getOwner() == this.owner){
                owner.getRessources()[Constants.WOOD] += u.getRessources()[Constants.WOOD];
                u.getRessources()[Constants.WOOD] = Constants.FINISHED;
                owner.getRessources()[Constants.IRON] += u.getRessources()[Constants.IRON];
                u.getRessources()[Constants.IRON] = Constants.FINISHED;
            }
        }


        //count down if lab is in building state
        if(labRoundsRemaining > Constants.FINISHED)
            labRoundsRemaining--;

        //count down if caserne is in building state
        if(caserneRoundsRemaining > Constants.FINISHED)
            caserneRoundsRemaining--;

        //set the sprite index
        if(caserneRoundsRemaining == Constants.FINISHED){
            currentField.getMap().getField(currentField.getXPos(),currentField.getYPos()-1).setSpriteIndex(SpriteNames.BASE_DOWN_LEFT_CASERNE.getSpriteIndex());

            if(labRoundsRemaining == Constants.FINISHED)
                currentField.getMap().getField(currentField.getXPos()+1,currentField.getYPos()-1).setSpriteIndex(SpriteNames.BASE_DOWN_RIGHT_FULL.getSpriteIndex());
            else
                currentField.getMap().getField(currentField.getXPos()+1,currentField.getYPos()-1).setSpriteIndex(SpriteNames.BASE_DOWN_RIGHT_CASERNE.getSpriteIndex());

        } else if(labRoundsRemaining == Constants.FINISHED){
            currentField.getMap().getField(currentField.getXPos()+1,currentField.getYPos()-1).setSpriteIndex(SpriteNames.BASE_DOWN_RIGHT_LAB.getSpriteIndex());
        }

        //count all recruiting states down and if finished, spawn them
        Iterator it = recruiting.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Unit,Integer> entry = (Map.Entry)it.next();
            entry.setValue(entry.getValue()-1);
            if(entry.getValue() <= Constants.FINISHED){
                if(this.spawnUnit(entry.getKey()))
                    it.remove(); //remove if spawn was successfull
            }
        }

        //count all researches down and if finished add them to the return list
        it = researching.entrySet().iterator();
        Buff current = null;
        while (it.hasNext()){
            Map.Entry<Research,Integer> entry = (Map.Entry)it.next();
            entry.setValue(entry.getValue()-1);
            if(entry.getValue() <= Constants.FINISHED){
                current = new Buff(null,owner,entry.getKey().getInfo(), session);
                if(current.isPermanent()) {
                    owner.getPermaBuffs().add(current);
                } else {
                    owner.getTemporaryBuffs().add(current);
                }
                try {
                    currentField.getMap().getSession().registerBuff(current.getOrigin().getId(),current.getPlayer().getId(), current.getBuffInfo());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                //result.add(entry.getKey());
                it.remove();  //remove if finished
            }
        }

        return result;
    }

    /**
     * Helpermethod for spawning units, iterates through the near fields starting with the fields directly
     * around, increasing the radius, for everytime there was no free field. if the radius reached 7 and
     * still no field is found, the method stops an returns false
     *
     * @param u the unit to spawn
     * @return true if a field was found, else false
     */
    private boolean spawnUnit(Unit u){
        int xPos = currentField.getXPos();
        int yPos = currentField.getYPos();

        int yRange = 1;

        Field current = null;

        //iterate as long as the range does not exceed the map bounds in every way
        // Y-Range is a bit short than x-range so currently some fields aren't covered
        while (yRange < Constants.FIELDYLENGTH/2) {
            //iterate on column level
            for (int i = yPos - yRange; i <= yPos + yRange; ++i) {

                //iterate on row level
                for (int k = xPos - yRange; k <= xPos + yRange; ++k)
                    try {
                        current = currentField.getMap().getField(k, i);
                        //if field found and free, place unit
                        if (current.getCurrent() == null) {
                            current.setCurrent(u);
                            currentField.getMap().getSession().registerUnit(u);
                            return true;
                        }
                    }catch (Exception e){//case there is no field with these coordinates
                        continue;
                    }

            }
            yRange++;
        }

        return false;
    }

    /**
     * Creates a new unit from the given type
     *
     * @param type the type of the wanted unit
     * @return if the action was a success or not
     */

    public boolean createUnit(UnitType type) {
        if(avaibleUnits.contains(type))
        {
            int[] cost = null;
                if(owner.hasReducedUnitCosts())
                    cost = type.getReducedCost();
                else
                 cost = type.getRessourceCost();


            int[] ressourcesAvailable = owner.getRessources();

            //Check if enough ressources, if not return result(null)
            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for (int j = Constants.WOOD; j<=Constants.MANA; ++j)
                if(cost[j] > ressourcesAvailable[j])
                   return  false;


            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                for(int i = Constants.WOOD; i <= Constants.MANA; ++i)
                owner.getRessources()[i] -= cost[i];

                if(type.getRecruitingTime() != 0)
                recruiting.put(new Unit(type, owner, session), type.getRecruitingTime());
            else
                    spawnUnit(new Unit(type, owner, session));

        }
        return true;
    }

    /**
     * cancels the creation of a unit, currently unused, therefore no further comment
     *
     * ////////////////////////////////////////////////////////////////////////////////////////////
     * Wird direkt ausgefuehrt nicht erst am Ende der Runde
     *
     * Bricht das Erstellen der uebergebenen Einheit ab,
     * wenn diese Einheit bereits fertig erstellt ist oder nicht existiert(null) passiert nichts)
     * ////////////////////////////////////////////////////////////////////////////////////////////
     *
     * @param which the one to cancel
     */

    public void abortCreation(Unit which) {
        if(recruiting.containsKey(which))
        {
            UnitType whichType = which.getType();
            int[] originalCost = whichType.getRessourceCost();
            float ressourcesRemaining = recruiting.get(which) / whichType.getRecruitingTime();

            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for(int i = Constants.WOOD; i<=Constants.MANA; ++i)
            owner.getRessources()[i] += (ressourcesRemaining* originalCost[i]);

            recruiting.remove(which);
        }
    }

    /**
     * starts building the labor
     *
     * @return true if starting was a success, else false
     */

    public boolean buildLab() {
        if(labRoundsRemaining == Constants.NONE_OR_NOT_SET){
            int[] ressourceCost = Building.LABOR.getRessourceCost();
            int[] avaibleRessources = owner.getRessources();

            //check if enough ressources, if not return false
            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
                if (ressourceCost[i] > avaibleRessources[i])
                    return false;

            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for(int j=Constants.WOOD; j<=Constants.MANA; ++j)
                owner.getRessources()[j] -= ressourceCost[j];

            labRoundsRemaining = Building.LABOR.getBuildTime();
            return true;
        }
        return false;
    }

    /**
     * cancels the building-process if it's active, otherwise does nothing
     * The player gets back some of the starting costs, depending on how much time has passed
     */
    public void abortLab() {
        if(labRoundsRemaining > Constants.FINISHED){
            float ressourcesLeft = labRoundsRemaining / Building.LABOR.getBuildTime();
            labRoundsRemaining = Constants.NONE_OR_NOT_SET;

            int[] originalCost = Building.LABOR.getRessourceCost();

            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for(int i=Constants.WOOD; i<=Constants.MANA;++i)
                owner.getRessources()[i] += (ressourcesLeft*originalCost[i]);
        }
    }

    /**
     * same as with buildingLab, but for caserne
     */
    public boolean buildCaserne() {
        if(caserneRoundsRemaining == Constants.NONE_OR_NOT_SET){
            int[] ressourceCost = Building.CASERNE.getRessourceCost();
            int[] avaibleRessources = owner.getRessources();

            //check if enough ressources, if not return false
            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
                if (ressourceCost[i] > avaibleRessources[i])
                    return false;

            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for(int j=Constants.WOOD; j<=Constants.MANA; ++j)
                owner.getRessources()[j] -= ressourceCost[j];

            caserneRoundsRemaining = Building.CASERNE.getBuildTime();
            return true;
        }
        return false;
    }

    /**
     * same as with abortLab, but for caserne
     */
    public void abortCaserne() {
        if(caserneRoundsRemaining > Constants.FINISHED){
            float ressourcesLeft = caserneRoundsRemaining / Building.CASERNE.getBuildTime();
            caserneRoundsRemaining = Constants.NONE_OR_NOT_SET;

            int[] originalCost = Building.CASERNE.getRessourceCost();

            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for(int i=Constants.WOOD; i<=Constants.MANA;++i)
                owner.getRessources()[i] += (ressourcesLeft*originalCost[i]);
        }
    }

    /**
     * builds the marketplace
     */
    public boolean buildMarket() {
        if(owner.getMarket())
            return true;

        if(owner.getRessources()[Constants.WOOD] >= Building.MARKET.getRessourceCost()[Constants.WOOD]
                && owner.getRessources()[Constants.GOLD] >= Building.MARKET.getRessourceCost()[Constants.GOLD]){
            owner.getRessources()[Constants.WOOD] -= Building.MARKET.getRessourceCost()[Constants.WOOD];
            owner.getRessources()[Constants.GOLD] -= Building.MARKET.getRessourceCost()[Constants.GOLD];
            owner.setMarket(true);
            owner.getRessourceBoni()[Constants.GOLD] += Constants.GOLD_BONI_VALUE; //The Goldbonus you get for having a market
            return true;
        }
        return false;
    }

    /**
     * researches stuff but deprecated and currently unused therefore no further comment
     * ///////////////////////////////////////////////////////////////
     * Startet das Erforschen des uebergebenen Forschungsobjektes
     *
     * @param research
     * @return true wenn der Start erfolgreich war(alle bedingungen erfuellt und noch nicht erforscht), sonst false
     */

    public boolean research(Research research) {
        if(!researched.contains(research)) {
            int[] ressourceCost = research.getRessourceCost();
            int[] avaibleRessources = owner.getRessources();

            //check if enough ressources, if not return false
            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for (int i = Constants.WOOD; i <= Constants.MANA; ++i)
                if (ressourceCost[i] > avaibleRessources[i])
                    return false;

            //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
            for (int j = Constants.WOOD; j <=Constants.MANA; ++j)
                owner.getRessources()[j] -= ressourceCost[j];

            researching.put(research, research.getResearchTime());
            researched.add(research);
            return true;
        }

        return false;
    }

    /**
     *  cancels research stuff but deprecated and currently unused therefore no further comment
     * ///////////////////////////////////////////////////////////////
     * Bricht die Erforschung der uebergebenen Forschung ab
     * wird die Forschung nicht erforscht passiert nichts
     *
     * @param research
     */

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

                    //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                    for(int i=Constants.WOOD; i<=Constants.MANA;++i)
                        owner.getRessources()[i] += (ressourcesLeft * originalCost[i]);

                    //remove buff and research
                    it.remove();
                    researched.remove(research);
                    break;      //finish method
                }
            }


        }
    }

    /**
     * Spawns a hero for the owner of the base
     * @return true if a hero was spawned, else false because owner has already a hero
     */
    public boolean spawnHero(){
        if(owner.getHero() == null){
            spawnUnit(new Hero(UnitType.HERO,owner,owner.getAccount().getName(),session));
            return true;
        }

        return false;
    }

    /**
     * Getter/Setter
     */
    public void setLabRoundsRemaining(int remaining) {
        if(remaining < Constants.NONE_OR_NOT_SET)
            this.labRoundsRemaining = Constants.NONE_OR_NOT_SET;
        else
        this.labRoundsRemaining = remaining;
    }
    public int getLabRoundsRemaining() {
        return labRoundsRemaining;
    }

    public void setCaserneRoundsRemaining(int remaining) {
        if(remaining < Constants.NONE_OR_NOT_SET)
            this.caserneRoundsRemaining = Constants.NONE_OR_NOT_SET;
        else
            this.caserneRoundsRemaining = remaining;
    }
    public int getCaserneRoundsRemaining() {
        return caserneRoundsRemaining;
    }

    public void setAvaibleUnits(List<UnitType> avaibleUnits) {
        if(avaibleUnits != null)
            this.avaibleUnits = avaibleUnits;
    }
    public List<UnitType> getAvaibleUnits() {
        return avaibleUnits;
    }

    public void setResearched(List<Research> researched) {
        if(researched != null)
            this.researched = researched;
    }
    public List<Research> getResearched() {
        return researched;
    }

    public HashMap<Unit, Integer> getRecruiting(){
        return recruiting;
    }

}
