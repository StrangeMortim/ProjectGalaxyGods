package GameObject;

import Action.Buff;
import Player.Player;

import java.rmi.Remote;
import java.util.List;

public interface IField extends Remote {

    /**
     * Aktualisiert das Objekt auf dem Feld
     *
     * @return gibt eine Liste mit allen beim update generierten Buffs zurück
     */
    public List<Buff> update();

    /**
     * Faengt an eine Basis auf dem Feld zu bauen
     *
     * @param player der Spieler der die Basis baut
     * @return gibt an ob der Spieler die Basis bauen kann oder nicht
     */
    public boolean buildBase(Player player);

    /**
     * Bricht den Bau der Basis ab, findet keiner statt passiert nichts
     *
     * @param player der Spieler der versucht den Bau abzubrechen
     * @return Ob der Vorgang erfolgreich war oder nicht(ob nach dem Methodenaufruf kein Bau mehr stattfindet oder nicht)
     */
    public boolean abortBuild(Player player);

    /**
     * Startet den Bau einer Mine, der Bau kann nicht abgebrochen werden
     *
     * @return gibt an ob das Starten erfolgreich war
     */
    public boolean buildMine();


    /**
     * Getter und setter
     */
    public void setResType(int resType);

    public int getResType();

    public void setResValue(int resValue);

    public int getResValue();

    public void setXPos(int xPos);

    public int getXPos();

    public void setYPos(int yPos);

    public int getYPos();

    public void setCurrent(IUnit current);

    public IUnit getCurrent();

    public void setWalkable(boolean walkable);

    public boolean getWalkable();

    public void setRoundsRemain(int roundsRemain);

    public int getRoundsRemain();

    public void setSpriteName(String spriteName);

    public String getSpriteName();

    public void setHasMine(boolean hasMine);

    public boolean getHasMine();

    public void setMap(Map map);

    public Map getMap();
}
