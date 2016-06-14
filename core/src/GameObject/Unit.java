package GameObject;

import Action.Buff;
import Player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Unit implements IUnit,Serializable {

    protected UnitType type;
    protected int maxHp;
    protected int currentHp;
    protected int atk;
    protected int def;
    protected int movePoints;
    protected int range;
    protected String spriteName = "";
    protected Player owner;
    protected Field currentField = null;
    protected int[] ressources = new int[2];

    public Unit(UnitType type, Player owner){
        if(type == null || owner == null)
            throw new IllegalArgumentException("Not enough information");

        this.type = type;
        this.owner = owner;
        /*TODO speccific values*/
    }


    /**
     * Aktualisiert alle Werte der Einheit die nicht direkt bearbeitet werden
     */
    @Override
    public List<Buff> update() {
        return new ArrayList<Buff>();        /*TODO override in base*/
    }

    /**
     * getter und setter
     *
     * @param type
     */
    @Override
    public void setType(UnitType type) {
        if(type == null)
            throw new IllegalArgumentException("type must not be null");

        this.type = type;
    }

    @Override
    public UnitType getType() {
        return type;
    }

    @Override
    public void setMaxHp(int maxHp) {
        if(maxHp < 1)
            this.maxHp = 1;
        else
            this.maxHp = maxHp;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public void setAtk(int atk) {
        this.atk = atk;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public void setDef(int def) {
        this.def = def;
    }

    @Override
    public int getDef() {
        return def;
    }

    @Override
    public void setMovePoints(int movePoints) {
        if(movePoints < 0)
            this.movePoints = 0;
        else
            this.movePoints = movePoints;
    }

    @Override
    public int getMovePoints() {
        return movePoints;
    }

    @Override
    public void setRange(int range) {
        if(range < 1)
            this.range = 1;
        else
            this.range = range;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public void setSpriteName(String spriteName) {
        if(spriteName.equals(""))
            throw new IllegalArgumentException("SpriteName is empty");

        this.spriteName = spriteName;
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    @Override
    public void setOwner(Player player) {
        if(player != null)
            owner = player;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setRessources(int[] ressources) {
        if(ressources != null)
            this.ressources = ressources;
        /*TODO check array size?*/
    }

    @Override
    public int[] getRessources() {
        return ressources;
    }

    @Override
    public void setField(Field field) {
        this.currentField = field;
    }

    @Override
    public Field getField() {
        return currentField;
    }
}
