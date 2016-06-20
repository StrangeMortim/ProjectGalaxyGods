package GameObject;

import Action.Buff;
import Player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IUnit extends Remote {

    /**
     * Aktualisiert alle Werte der Einheit die nicht direkt bearbeitet werden
     */
    public List<Buff> update()throws RemoteException;

    /**
     * getter und setter
     */
    public void setType(UnitType type)throws RemoteException;

    public UnitType getType()throws RemoteException;

    public void setMaxHp(int maxHp)throws RemoteException;

    public int getMaxHp()throws RemoteException;

    public void setCurrentHp(int currentHp)throws RemoteException;

    public int getCurrentHp()throws RemoteException;

    public void setAtk(int atk)throws RemoteException;

    public int getAtk()throws RemoteException;

    public void setDef(int def)throws RemoteException;

    public int getDef()throws RemoteException;

    public void setMovePoints(int movePoints)throws RemoteException;

    public int getMovePoints()throws RemoteException;

    public void setRange(int range)throws RemoteException;

    public int getRange()throws RemoteException;

    public void setSpriteName(String spriteName)throws RemoteException;

    public String getSpriteName()throws RemoteException;

    public void setOwner(Player player)throws RemoteException;

    public Player getOwner()throws RemoteException;

    public void setRessources(int ressources[])throws RemoteException;

    public int[] getRessources()throws RemoteException;

    public void setField(Field field)throws RemoteException;

    public Field getField()throws RemoteException;
}
