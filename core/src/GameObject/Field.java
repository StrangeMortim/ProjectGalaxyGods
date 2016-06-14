package GameObject;

import Action.Buff;
import Player.Player;

import java.io.Serializable;
import java.util.List;


public class Field implements IField,Serializable {

    private int resType = -1;
    private int resValue = 0;
    private int xPos = -1;
    private int yPos = -1;
    private IUnit current;
    private boolean walkable=true;
    private int roundsRemain = -1;
    private String spriteName = "";
    private boolean hasMine = false;
    private Map map = null;

    public Field(int resType, int resValue, int xPos, int yPos){
        if(resType < -1 || resType > 4 || xPos < 0 || xPos > 8 || yPos < 0 || yPos >8)
            throw new IllegalArgumentException("Invalid Values");

        this.resType = resType;
        this.resValue = resValue;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Aktualisiert das Objekt auf dem Feld
     */
    @Override
    public List<Buff> update() {
        return current.update();
    }

    /**
     * Faengt an eine Basis auf dem Feld zu bauen
     *
     * @param player der Spieler der die Basis baut
     * @return gibt an ob der Spieler die Basis bauen kann oder nicht
     */
    @Override
    public boolean buildBase(Player player) {
        /*TODO*/
        return false;
    }

    /**
     * Bricht den Bau der Basis ab, findet keiner statt passiert nichts
     *
     * @param player der Spieler der versucht den Bau abzubrechen
     * @return Ob der Vorgang erfolgreich war oder nicht(ob nach dem Methodenaufruf kein Bau mehr stattfindet oder nicht)
     */
    @Override
    public boolean abortBuild(Player player) {
                /*TODO*/
        return false;
    }

    /**
     * Startet den Bau einer Mine, der Bau kann nicht abgebrochen werden
     *
     * @return gibt an ob das Starten erfolgreich war
     */
    @Override
    public boolean buildMine() {
                /*TODO*/
        return false;
    }

    /**
     * Getter und setter
     *
     * @param resType
     */
    @Override
    public void setResType(int resType) {
        if(resType < -1 || resType > 4)
            throw new IllegalArgumentException("That ressource does not exist");

        this.resType = resType;
    }

    @Override
    public int getResType() {
        return resType;
    }

    @Override
    public void setResValue(int resValue){
        if(resValue < 0)
            this.resValue = 0;
        else
            this.resValue = resValue;
    }

    @Override
    public int getResValue(){
        return resValue;
    }

    @Override
    public void setXPos(int xPos) {
        if(xPos < 0 || xPos > 8)
            throw new IllegalArgumentException("Invalid coordinates");

        this.xPos = xPos;
    }

    @Override
    public int getXPos() {
        return xPos;
    }

    @Override
    public void setYPos(int yPos) {
        if(yPos < 0 || yPos > 8)
            throw new IllegalArgumentException("Invalid coordinates");

        this.yPos = yPos;
    }

    @Override
    public int getYPos() {
        return yPos;
    }

    @Override
    public void setCurrent(IUnit current) {
        this.current = current;
        current.setField(this);
    }

    @Override
    public IUnit getCurrent() {
        return current;
    }

    @Override
    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    @Override
    public boolean getWalkable() {
        return walkable;
    }

    @Override
    public void setRoundsRemain(int roundsRemain) {
        if(roundsRemain < -1)
            this.roundsRemain = 0;
        else
            this.roundsRemain = roundsRemain;
    }

    @Override
    public int getRoundsRemain() {
        return roundsRemain;
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
    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    @Override
    public boolean getHasMine() {
        return hasMine;
    }

    @Override
    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public Map getMap() {
        return map;
    }


}
