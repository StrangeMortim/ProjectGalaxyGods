package Action;


import GameObject.GameSession;
import GameObject.Research;
import GameObject.Unit;
import GameObject.UnitType;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBuff extends Remote {

    /**
     * Getter und Setter
     */
    public void setPermanent(Boolean permanent)throws RemoteException;

    public Boolean getPermanent()throws RemoteException;

    public void setRoundsLeft(int rounds)throws RemoteException;

    public int getRoundsLeft()throws RemoteException;

    public void setAtk(int atk)throws RemoteException;

    public int getAtk()throws RemoteException;

    public void setDef(int def)throws RemoteException;

    public int getDef()throws RemoteException;

    public void setHp(int hp)throws RemoteException;

    public int getHp()throws RemoteException;

    public void setRange(int range)throws RemoteException;

    public int getRange()throws RemoteException;

    public void setMovePoints(int movePoints)throws RemoteException;

    public int getMovePoints()throws RemoteException;

    public void setGameSession(GameSession session)throws RemoteException;

    public GameSession getGameSession()throws RemoteException;

    public void setSource(Research source)throws RemoteException;

    public Research getSource()throws RemoteException;

    public boolean appliesForUnit(UnitType unit)throws RemoteException;

    public Buff getPersonalCopy(Unit u)throws RemoteException;

    public void setFirstTime(boolean firstTime)throws RemoteException;
}
