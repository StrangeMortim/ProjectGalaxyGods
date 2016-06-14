package GameObject;

import Action.*;
import Player.*;
import chat.*;

import java.rmi.Remote;
import java.util.List;


/**
 * Created by benja_000 on 10.06.2016.
 */
public interface IGameSession extends Remote {
    /**
     *
     */
    public void update();

    /**
     * Registriert eine neue Einheit und gibt ihr alle Buffs
     */
    public void registerUnit(Unit u);

    /**
     *
     * @param t
     */
    public void addTeam(Team t);
    /**
     *
     * @param t
     */
    public void removeTeam(Team t);
    /**
     *
     * @param m
     */
    public void sendMessage(Message m);
    /**
     *
     * @param b
     */
    public void addBuffs(List<Buff> b);
    /**
     *
     * @param b
     */
    public void removeBuff(Buff b);
    /**
     *
     */
    public void startTurn();
    /**
     *
     */
    public void finishTurn();
    /**
     *
     * @param a
     * @param p
     * @param t
     * @return
     */
    public boolean playerJoin(Account a, Player p, Team t);
    /**
     *
     * @param p
     */
    public void playerLeave(Player p);
    /**
     *
     * @return
     */
    public boolean save();
    /**
     *
     * @return
     */
    public boolean finish();

}
