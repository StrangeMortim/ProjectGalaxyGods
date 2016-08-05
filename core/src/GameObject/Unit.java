package GameObject;

import Action.Buff;
import Action.BuffInfo;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Realises a Unit in the game
 *
 * @author Benjamin, Fabi
 */
public class Unit implements Serializable {

    /**
     * What kind of Unit it is(e.g. Archer)
     */
    protected UnitType type;

    /**
     * How much health the unit can have at max
     */
    protected int maxHp;

    /**
     * How much health the unit currently has
     */
    protected int currentHp;

    /**
     * How much damage the Unit deals(without the enemy defense)
     */
    protected int atk;

    /**
     * How much incoming damage is reduced(is directly subtracted)
     */
    protected int def;

    /**
     * How far the Unit can move at most in one turn(number of fields)
     */
    protected int movePoints;

    /**
     * How many fields that Unit can still move in that current turn
     */
    protected int movePointsLeft;

    /**
     * How many fields an enemy unit can be away and can still be attacked
     */
    protected int range;

    /**
     * Corresponds to the prepared Textures in GameScreen, value is received from SpriteNames enum
     */
    protected int spriteIndex = -1;

    /**
     * The Player to whom the Unit belongs
     */
    protected Player owner;

    /**
     * The field the Unit is currently standing on
     */
    protected Field currentField = null;

    /**
     * The resources the unit is carrying, actually only used by Worker-Type
     */
    protected int[] ressources = new int[4];

    /**
     * the direction in which the Unit looks(used in display)
     */
    protected int direction=0;

    /**
     * the global ID of the Object
     */
    protected int iD;

    /**
     * the session where the Unit belongs to an is registered
     */
    protected GameSession session;

    public Unit(UnitType type, Player owner, GameSession session){
        if(type == null || owner == null || session == null)
            throw new IllegalArgumentException("Not enough information");

        this.type = type;
        this.owner = owner;
        this.maxHp = type.getMaxHp();
        this.currentHp = this.maxHp;
        this.atk = type.getAtk();
        this.def = type.getDef();
        this.movePoints = type.getMovePoints();
        this.range = type.getRange();
        this.spriteIndex = type.getSpriteIndex();
        this.session = session;
           iD = session.registerObject(this);

    }


    /**
     * updates the unit, currently only movePoints are resetted
     *
     * @return a list of buffs, if there are some new ones create
     */
    public List<Buff> update() {
        movePointsLeft = movePoints;

        return new ArrayList<Buff>();
    }

    /**
     * getter und setter
     */
    public void setType(UnitType type) {
        if(type == null)
            throw new IllegalArgumentException("type must not be null");

        this.type = type;
    }
    public UnitType getType() {
        return type;
    }

    public void setMaxHp(int maxHp) {
        if(maxHp < 1)
            this.maxHp = 1;
        else
            this.maxHp = maxHp;
    }
    public int getMaxHp() {
        return maxHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp>maxHp ? maxHp : currentHp;
    }
    public int getCurrentHp() {
        return currentHp;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }
    public int getAtk() {
        return atk;
    }

    public void setDef(int def) {
        this.def = def;
    }
    public int getDef() {
        return def;
    }

    public void setMovePoints(int movePoints) {
        if(movePoints < Constants.FINISHED)
            this.movePoints = Constants.FINISHED;
        else
            this.movePoints = movePoints;
    }
    public int getMovePoints() {
        return movePoints;
    }

    public int getMovePointsLeft(){return movePointsLeft;}
    public void setMovePointsLeft(int i){movePointsLeft=i;}

    public void setRange(int range) {
        if(range < 1)
            this.range = 1;
        else
            this.range = range;
    }
    public int getRange() {
        return range;
    }

    public void setSpriteIndex(int spriteIndex) {
        if(spriteIndex < 0)
            throw new IllegalArgumentException("SpriteName is empty");
        this.spriteIndex = spriteIndex;
    }
    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setOwner(Player player) {
        if(player != null)
            owner = player;
    }
    public Player getOwner() {
        return owner;
    }

    public void setRessources(int[] ressources) {
        if(ressources != null)
            this.ressources = ressources;
        /*TODO check array size?*/
    }
    public int[] getRessources() {
        return ressources;
    }

    public void setField(Field field) {
        this.currentField = field;
    }
    public Field getField() {
        return currentField;
    }

    public int getDirection()  {
        return direction;
    }
    public void setDirection(int direction) {
        if(direction==0) setSpriteIndex(type.getSpriteIndex());
        if(direction==1)if(direction==1) setSpriteIndex( SpriteNames.valueOf(getType()+"BACK").getSpriteIndex());
        this.direction=direction;
    }

    public int getId() {
        return iD;
    }

    public List<String> getInfo(){
        ArrayList<String> result = new ArrayList<>();
        result.add(atk+"");
        result.add(def+"");
        result.add(currentHp+"/"+maxHp);
        result.add(movePointsLeft+"/"+movePoints);
        result.add(type.toString());
        result.add(range+"");
        result.add(owner.getAccount().getName());
        return result;
    }


}
