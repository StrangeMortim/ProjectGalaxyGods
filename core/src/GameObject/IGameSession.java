package GameObject;

import Action.*;
import Player.*;
import chat.*;

import java.util.List;


/**
 * Created by benja_000 on 10.06.2016.
 */
public interface IGameSession {
    /**
     *
     */
    public void update();
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
