package GameObject;

import Action.Buff;
import Action.BuffInfo;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Unit implements Serializable {

   // private static final long serialVersionUID = -8623318700146833448L;
    protected UnitType type;
    protected int maxHp;
    protected int currentHp;
    protected int atk;
    protected int def;
    protected int movePoints;
    protected int movePointsLeft;
    protected int range;
    protected int spriteIndex = -1;
    protected Player owner;
    protected Field currentField = null;
    protected int[] ressources = new int[4];
    protected int direction=0;
    protected int iD;
    protected GameSession session;
    protected BuffInfo significantBuff = BuffInfo.NONE;

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
        try {
           iD = session.registerObject(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        /*TODO check*/
    }


    /**
     * Aktualisiert alle Werte der Einheit die nicht direkt bearbeitet werden
     */

    public List<Buff> update() {
        movePointsLeft = movePoints;

        return new ArrayList<Buff>();
    }
//TODO: Check ob Spieler dranne is und Einheit vom Spieler, jo und swag
    /**
     * getter und setter
     *
     * @param type
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

    public void setSignificantBuff(BuffInfo buff){
        this.significantBuff = buff;
    }

    public BuffInfo getSignificantBuff(){
        return significantBuff;
    }
}
