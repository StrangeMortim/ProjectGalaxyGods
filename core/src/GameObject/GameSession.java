package GameObject;

import Action.*;
import Player.*;
import chat.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by benja_000 on 10.06.2016.
 */
public class GameSession implements IGameSession, Serializable{
    /**
     *
     */
    private List<Team> teams;
    /**
     *
     */
    private Map level;
    /**
     *
     */
    private Player active;
    /**
     *
     */
    private List<Buff>buffs;
    /**
     *
     */
    private HashMap<Account,Player> identities;
    /**
     *
     */
    private ActionProcessor currentTurn;
    /**
     *
     */
    private int sessionChat;
    /**
     *
     */
    private int round;
    /**
     *
     */
    private int maxPlayersPerTeam;
    /**
     *
     */
    private boolean hasStarted;
    /**
     *
     */
    private Market market;

    public void update(){}

    @Override
    public void sendMessage(Message m) {

    }

    @Override
    public void removeTeam(Team t) {

    }

    @Override
    public void addTeam(Team t) {

    }

    ;

    public void addBuffs(List<Buff> b){}

    public void removeBuff(Buff b){}

    public void startTurn(){};

    public void finishTurn(){}

    @Override
    public void playerLeave(Player p) {

    }

    @Override
    public boolean playerJoin(Account a, Player p, Team t) {
        return false;
    }

    ;

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public boolean finish() {
        return false;
    }



    //Getter Setter
    public int getSessionChat() {
        return sessionChat;
    }

    public void setSessionChat(int sessionChat) {
        this.sessionChat = sessionChat;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Map getLevel() {
        return level;
    }

    public void setLevel(Map level) {
        this.level = level;
    }

    public Player getActive() {
        return active;
    }

    public void setActive(Player active) {
        this.active = active;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }

    public HashMap<Account, Player> getIdentities() {
        return identities;
    }

    public void setIdentities(HashMap<Account, Player> identities) {
        this.identities = identities;
    }

    public ActionProcessor getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(ActionProcessor currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }
}
