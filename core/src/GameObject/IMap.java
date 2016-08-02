package GameObject;


import Action.Buff;
import Player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IMap extends Remote {

    /**
     * Initialisiert die Map, d.h. platziert alle Felder, Basen und Standardobjekte
     */
    public void init()throws RemoteException;

    /**
     * Generiert zufaellig eine Karte, nur bei genuegend Zeit zu implementieren
     */
    public void generateRandom()throws RemoteException;

    /**
     * Speichert die generierte Karte um sie spaeter laden zu koennen
     * wird ebenfalls nur bei genuegend Zeit implementiert
     */
    public void saveConfiguration()throws RemoteException;

    /**
     * Aktualisiert alle Felder der Karte ueber ihre update-Methode
     */
    public List<Buff> update()throws RemoteException;

    public boolean checkMovement(int xPos, int yPos)throws RemoteException;

    /**
     * Gibt ein spezielles Feld zurueck
     */
    public Field getField(int x, int y)throws RemoteException;

    /**
     * Getter und Setter
     */
    public void setFields(Field fields[][])throws RemoteException;

    public Field[][] getFields()throws RemoteException;

    public void setMaxPlayers(int maxPlayers)throws RemoteException;

    public int getMaxPlayers()throws RemoteException;

    public void setMinPlayers(int minPlayers)throws RemoteException;

    public int getMinPlayers()throws RemoteException;

    public void setLevelName(String levelName)throws RemoteException;

    public String getLevelName()throws RemoteException;

    public IGameSession getSession()throws RemoteException;

    public boolean addBase(Player player, int playerNumber);

}
