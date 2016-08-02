package GameObject;

import Action.*;
import Player.*;
import chat.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
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

    public void addSingleBuff(Buff b) throws RemoteException;
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
    public void finishTurn(Player p)throws RemoteException;

    /**
     *
     * @param a
     * @param p
     * @param t
     * @return
     */
    public Player playerJoin(Account a, Player p, Team t)throws RemoteException;
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
    public IMap getMap()throws RemoteException;

    public int getNumberOfPlayers()throws RemoteException;

    public void setNumberOfPlayers(int number)throws RemoteException;

    public boolean registerBuff(Buff b) throws RemoteException;

    public String getName() throws RemoteException;

    public void setName(String name) throws RemoteException;

    public String getPassword() throws RemoteException;

    public Market getMarket() throws RemoteException;

    public Player getActive() throws RemoteException;

    public void setActive(Player p) throws RemoteException;

    public List<Buff> getBuffs() throws RemoteException;

    public List<Team> getTeams() throws RemoteException;

    public boolean isHasStarted() throws RemoteException;

    public void setHasStarted(boolean b) throws RemoteException;

    public Chat getSessionChat() throws RemoteException;

    public void showSessionDetails() throws RemoteException;

    public void setSessionChat(Chat sessionChat)throws RemoteException;

    public int getTurn()throws RemoteException;

    public void setTurn(int turn)throws RemoteException;

    public int getMaxPlayersPerTeam()throws RemoteException;

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam)throws RemoteException;

    public void setTeams(List<Team> teams)throws RemoteException;

    public IMap getLevel()throws RemoteException;

    public void setLevel(Map level)throws RemoteException;

    public void setBuffs(List<Buff> buffs)throws RemoteException;

    public HashMap<Account, Player> getIdentities()throws RemoteException;

    public void setIdentities(HashMap<Account, Player> identities)throws RemoteException;

    public ActionProcessor getCurrentTurn()throws RemoteException;

    public void setCurrentTurn(ActionProcessor currentTurn)throws RemoteException;

    public void setMarket(Market market)throws RemoteException;

    public void setPassword(String password) throws RemoteException;

    public int getRound() throws RemoteException;

    public void setRound(int round) throws RemoteException;


}
