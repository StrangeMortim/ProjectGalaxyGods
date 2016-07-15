package GameObject;

import Action.*;
import Player.*;
import chat.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


/**
 * Created by benja_000 on 10.06.2016.
 */
public interface IGameSession extends Remote {
    /**
     *
     */
    public void update()throws RemoteException;

    /**
     * Registriert eine neue Einheit und gibt ihr alle Buffs
     */
    public void registerUnit(Unit u)throws RemoteException;

    /**
     * Entfernt alle Buffs für die übergebene Einheit
     */
    public void removeUnit(Unit u)throws RemoteException;

    /**
     *
     * @param t
     */
    public void addTeam(Team t)throws RemoteException;
    /**
     *
     * @param t
     */
    public void removeTeam(Team t)throws RemoteException;
    /**
     *
     * @param m
     */
    public void sendMessage(Message m)throws RemoteException;
    /**
     *
     * @param b
     */
    public void addBuffs(List<Buff> b)throws RemoteException;
    /**
     *
     * @param b
     */
    public void removeBuff(Buff b)throws RemoteException;
    /**
     *
     */
    public void startTurn()throws RemoteException;
    /**
     *
     */
    public void finishTurn()throws RemoteException;

    /**
     *
     * @param a
     * @param p
     * @param t
     * @return
     */
    public Player playerJoin(Account a, Player p, Team t, int playerPos)throws RemoteException;
    /**
     *
     * @param p
     */
    public void playerLeave(Player p)throws RemoteException;
    /**
     *
     * @return
     */
    public boolean save()throws RemoteException;
    /**
     *
     * @return
     */
    public boolean finish()throws RemoteException;

    /**
     * Getter für die Map
     */
    public Map getMap()throws RemoteException;

}
