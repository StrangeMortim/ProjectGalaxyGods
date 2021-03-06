package GameObject;

import Action.Buff;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a single field on the map
 *
 * @author Fabi
 */
public class Field implements Serializable {

  //  private static final long serialVersionUID = -7532038227070675590L;
    private int resType = Constants.NONE_OR_NOT_SET;
    private int resValue = 0;
    private int xPos = Constants.NONE_OR_NOT_SET;
    private int yPos = Constants.NONE_OR_NOT_SET;
    private Unit current;
    private boolean walkable=true;
    private int roundsRemain = Constants.NONE_OR_NOT_SET;
    private int spriteIndex = -1;
    private boolean baseBuilding = false;
    private Player builder = null;
    private boolean mineBuilding = false;
    private boolean hasMine = false;
    private Map map = null;
    private GameSession session;
    private int iD;

    public Field(int resType, int resValue, int xPos, int yPos, Map map, GameSession session) {
        if(session == null || resType < Constants.NONE_OR_NOT_SET || resType > Constants.MANA || xPos < 0 || xPos > Constants.FIELDXLENGTH || yPos < 0 || yPos >Constants.FIELDYLENGTH || map == null)
            throw new IllegalArgumentException("Invalid Values");

        this.resType = resType;
        this.resValue = resValue;
        this.xPos = xPos;
        this.yPos = yPos;
        this.map = map;
        this.session = session;
        iD = session.registerObject(this);


        //Choose random default Fieldsprite, increase number for new sprites,
        //sprites must be named "normal" + number(next higher int) and be png files
        //sprites must be located in sprites folder
        int type = new Random().nextInt(Constants.NUMBER_NORMAL_FIELDS);
        switch (type){
            case 0:
                this.spriteIndex = SpriteNames.NORMAL_FIELD.getSpriteIndex();
                break;
            case 1:
                this.spriteIndex = SpriteNames.NORMAL_FIELD_2.getSpriteIndex();
                break;
            case 2:
                this.spriteIndex = SpriteNames.NORMAL_FIELD_3.getSpriteIndex();
                break;
        }


        //this.texture = new Texture(Gdx.files.internal("assets/"+this.spriteIndex));
    }

    /**
     * Updates the current Unit on the field,
     * distributes resources to units and updates building processes
     */
    public List<Buff> update() {

        this.updateBuildingProcesses();
        this.distributeRessources();

        if(current != null)
        return current.update();
        else
            return new ArrayList<Buff>();
    }

    /**
     * If the field has resources, distribute them to all (Worker)units around the field
     */
    private void distributeRessources(){
        //if field has ressources
        if(resType != Constants.NONE_OR_NOT_SET) {
            List<Unit> nearUnits = null;
            nearUnits = this.getNearUnits();

            Player tmp = null;

            //add ressources to all near units depending on the fields ressource
            switch (resType) {
                case Constants.WOOD:
                    for (Unit u : nearUnits) {
                        tmp = u.getOwner();
                        if(u.type == UnitType.WORKER) {
                            u.getRessources()[Constants.WOOD] += Constants.WOOD_RES_VALUE + tmp.getRessourceBoni()[Constants.WOOD];
                            resValue -= (Constants.WOOD_RES_VALUE + tmp.getRessourceBoni()[Constants.WOOD]);
                        }
                    }
                    break;
                case Constants.IRON:
                    if(hasMine) {
                        for (Unit u : nearUnits) {
                            tmp = u.getOwner();
                            if(u.type == UnitType.WORKER) {
                                u.getRessources()[Constants.IRON] += Constants.IRON_RES_VALUE + tmp.getRessourceBoni()[Constants.IRON];
                                resValue -= (Constants.IRON_RES_VALUE + tmp.getRessourceBoni()[Constants.IRON]);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            //if no more ressources left after  dispersal, set no-ressource values
            if(resValue <= Constants.FINISHED){
                resType = Constants.NONE_OR_NOT_SET;
                resValue = 0;
            }
        }
    }

    /**
     * updates the building processes
     */
    private void updateBuildingProcesses(){
        /*TODO check*/
        if(roundsRemain > Constants.FINISHED){ //there is something to build

            List<Unit> nearUnits = this.getNearUnits();
            if (baseBuilding) {
                for(Unit u: nearUnits)
                    if(u.getOwner() == builder) {       //if it's a base and owner units are near, count down the rounds, -1 for every unit near
                        roundsRemain--;
                        if (roundsRemain == Constants.FINISHED) {        //if the rounds reach zero, build the base and stop
                            current = new Base(UnitType.BASE, builder, session);
                            builder = null;
                            baseBuilding = false;
                            break;
                        }
                    }
                    /*
                    if it's a mine check all near units and reduce the rounds for each, as long as the owner has enough wood,
                    also reduce the owners wood by 5
                     */
            } else if(mineBuilding){
                if(!nearUnits.isEmpty())
                    for(Unit u: nearUnits){
                        if(u.getOwner().getRessources()[Constants.WOOD] >= Building.MINE.getRessourceCost()[Constants.WOOD]){
                            u.getOwner().getRessources()[Constants.WOOD] -= Building.MINE.getRessourceCost()[Constants.WOOD];
                            roundsRemain--;
                        }
                    }
                if(roundsRemain <= Constants.FINISHED) {
                    hasMine = true;
                    spriteIndex = SpriteNames.MINE.getSpriteIndex();
                    mineBuilding = false;
                }
            }
        }
    }

    /**
     * Searches all Unit in the direct environment to the Field(the (max) 8 surrounding Fields
     * @return the found Units
     */
    public List<Unit> getNearUnits(){
        List<Unit> result = new ArrayList<>();
        for(int i=yPos-1; i<=yPos+1; ++i){
            for(int k=xPos-1; k<=xPos+1; ++k){
                try {
                    Unit tmp = map.getField(k,i).getCurrent();
                    if(tmp != null&&tmp!=current){
                       result.add(tmp);
                    }
                } catch (NullPointerException e){
                    continue;
                }
            }
        }

        return result;
    }

    /**
     * Starts building a base for the given player on the field
     *
     * @param player the player who wants to build the base
     * @return whether the player could start the building or not
     */

    public boolean buildBase(Player player) {
        if(player == null)
            throw new IllegalArgumentException("Player is null in Buildbase");


            if(current == null && !hasMine && player.getTechTree().isCultureFull()){
                int[] baseCost = UnitType.BASE.getRessourceCost();
                int[] availableRessources = player.getRessources();
                //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                for(int j = Constants.WOOD; j<=Constants.MANA; ++j)
                    if (baseCost[j] > availableRessources[j])
                        return false;

                List<Unit> nearUnits = this.getNearUnits();
                for(Unit u: nearUnits){
                    if(u.getOwner() == player){
                        baseBuilding = true;
                        roundsRemain = UnitType.BASE.getRecruitingTime();
                        //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                        for(int k = Constants.WOOD; k<=Constants.MANA; ++k)
                            player.getRessources()[k] -= baseCost[k];

                        builder = player;
                        walkable = false;
                        return true;
                    }
                }
            }

        return false;
    }

    /**
     * Cancels the building process of the base, if it is active
     *
     * @param player the player trying to cancel the process
     * @return if the cancelling was a success or not
     */

    public boolean abortBuild(Player player) {
        if(player != null) {
            List<Unit> nearUnits = this.getNearUnits();
            for (Unit u : nearUnits) {
                if (u.getOwner() == player) {
                    int[] originalCost = UnitType.BASE.getRessourceCost();
                    float ressourcesLeft = roundsRemain / UnitType.BASE.getRecruitingTime();

                    //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                    for (int i = Constants.WOOD; i <= Constants.MANA; ++i)
                        player.getRessources()[i] = (int) (ressourcesLeft * originalCost[i]);

                    roundsRemain = Constants.NONE_OR_NOT_SET;
                    walkable = true;
                    baseBuilding = false;
                    return true;
                }
            }
        }
                /*TODO check*/
        return false;
    }

    /**
     * starts building a mine on the field, cannot be canceled
     *
     * @param player the player who wants to build the mine
     * @return whether the player could start the building or not
     */

    public boolean buildMine(Player player) {
        if(player != null && roundsRemain == Constants.NONE_OR_NOT_SET && current == null && resType == Constants.IRON){
            List<Unit> nearUnits = this.getNearUnits();
            //check near units if one of them belongs to player, player may start building
            for(Unit u: nearUnits)
                if(u.getOwner() == player){
                    roundsRemain = Building.MINE.getBuildTime();
                    mineBuilding = true;
                    walkable = false;
                    player.getRessources()[Constants.WOOD] -= Building.MINE.getInitialCost()[Constants.WOOD];  //initial starting cost
                    return true;
                }
        }

        return false;
    }

    /**
     * @return if the field has a unit on it, return the unit, else the field itself
     */
    public Object select(){
        return (current == null) ? this : current;
    }


    /**
     * Getter und setter
     *
     */
    public int getId()  {
        return iD;
    }

    public void setResType(int resType) {
        if(resType < Constants.NONE_OR_NOT_SET || resType > Constants.MANA)
            throw new IllegalArgumentException("That ressource does not exist");



        this.resType = resType;
        walkable=false;
        if(resType==Constants.MANA) {walkable=true;}

        this.spriteIndex = (resType == Constants.WOOD) ? SpriteNames.FOREST.getSpriteIndex()
                        : (resType == Constants.IRON) ? (new Random().nextInt(2) == 0 ? SpriteNames.IRON_FIELD.getSpriteIndex() : SpriteNames.IRON_FIELD_2.getSpriteIndex())
                        : (resType == Constants.MANA) ? SpriteNames.MIRACLE.getSpriteIndex()
                        : SpriteNames.NORMAL_FIELD.getSpriteIndex();
        
    }
    public int getResType() {
        return resType;
    }

    public void setResValue(int resValue){
        if(resValue < 0) {
            this.resValue = 0;
            this.setResType(Constants.NONE_OR_NOT_SET);
        }else
            this.resValue = resValue;


    }
    public int getResValue(){
        return resValue;
    }

    public void setXPos(int xPos) {
        if(xPos < 0 || xPos > Constants.FIELDXLENGTH)
            throw new IllegalArgumentException("Invalid coordinates");

        this.xPos = xPos;
    }
    public int getXPos() {
        return xPos;
    }

    public void setYPos(int yPos) {
        if(yPos < 0 || yPos > Constants.FIELDYLENGTH)
            throw new IllegalArgumentException("Invalid coordinates");

        this.yPos = yPos;
    }
    public int getYPos() {
        return yPos;
    }

    public void setCurrent(Unit current) {
        if(current==null){
            walkable=true;
            switch (new Random().nextInt(Constants.NUMBER_NORMAL_FIELDS)){
                case 0:
                    setSpriteIndex(SpriteNames.NORMAL_FIELD.getSpriteIndex());
                    break;
                case 1:
                    setSpriteIndex(SpriteNames.NORMAL_FIELD_2.getSpriteIndex());
                    break;
                case 2:
                    setSpriteIndex(SpriteNames.NORMAL_FIELD_3.getSpriteIndex());
            }

            this.current=null;
            return;
        }
        this.current = current;
        current.setField(this);

        if(current.getSpriteIndex()>= 0 && !(current instanceof Base))
        setSpriteIndex(current.getSpriteIndex());


        if(resType == Constants.MANA){
            current.getOwner().getRessources()[Constants.MANA] += resValue;
            resValue = 0;
        }
        walkable=false;
    }
    public Unit getCurrent() {
        return current;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }
    public boolean getWalkable() {
        return walkable;
    }

    public void setRoundsRemain(int roundsRemain) {
        if(roundsRemain < Constants.NONE_OR_NOT_SET)
            this.roundsRemain = Constants.NONE_OR_NOT_SET;
        else
            this.roundsRemain = roundsRemain;
    }
    public int getRoundsRemain() {
        return roundsRemain;
    }

    public void setSpriteIndex(int spriteIndex) {
        if(spriteIndex < 0)
            throw new IllegalArgumentException("SpriteName is empty");

        this.spriteIndex = spriteIndex;
       // this.texture = new Texture(Gdx.files.internal("assets/"+this.spriteIndex));
    }
    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }
    public boolean getHasMine() {
        return hasMine;
    }

    public void setMap(Map map) {
        this.map = map;
    }
    public Map getMap() {
        return map;
    }
}
