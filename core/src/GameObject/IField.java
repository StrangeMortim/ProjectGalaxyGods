package GameObject;

import Action.Buff;
import Player.Player;
import com.badlogic.gdx.graphics.Texture;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IField extends Remote {

    /**
     * Aktualisiert das Objekt auf dem Feld
     *
     * @return gibt eine Liste mit allen beim update generierten Buffs zurück
     */
    public List<Buff> update()throws RemoteException;

    /**
     * Faengt an eine Basis auf dem Feld zu bauen
     *
     * @param player der Spieler der die Basis baut
     * @return gibt an ob der Spieler die Basis bauen kann oder nicht
     */
    public boolean buildBase(Player player)throws RemoteException;

    /**
     * Searches all Unit in the direct environment to the Field(the (max) 8 surrounding Fields
     * @return the found Units
     */
    public List<Unit> getNearUnits()throws RemoteException;

    /**
     * Bricht den Bau der Basis ab, findet keiner statt passiert nichts
     *
     * @param player der Spieler der versucht den Bau abzubrechen
     * @return Ob der Vorgang erfolgreich war oder nicht(ob nach dem Methodenaufruf kein Bau mehr stattfindet oder nicht)
     */
    public boolean abortBuild(Player player)throws RemoteException;

    /**
     * Startet den Bau einer Mine, der Bau kann nicht abgebrochen werden
     *
     * @return gibt an ob das Starten erfolgreich war
     */
    public boolean buildMine(Player player)throws RemoteException;


    public Object select() throws  RemoteException;

    /**
     * Getter und setter
     */
    public void setResType(int resType)throws RemoteException;

    public int getResType()throws RemoteException;

    public void setResValue(int resValue)throws RemoteException;

    public int getResValue()throws RemoteException;

    public void setXPos(int xPos);

    public int getXPos();

    public void setYPos(int yPos);

    public int getYPos();

    public void setCurrent(Unit current);

    public Unit getCurrent();

    public void setWalkable(boolean walkable);

    public boolean getWalkable();

    public void setRoundsRemain(int roundsRemain);

    public int getRoundsRemain();

    public void setSpriteName(String spriteName);

    public String getSpriteName();

    public Texture getTexture();

    public void setHasMine(boolean hasMine);

    public boolean getHasMine();

    public void setMap(Map map);

    public Map getMap();
}
