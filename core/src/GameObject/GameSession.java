package GameObject;

import java.util.HashMap;

/**
 * Created by benja_000 on 10.06.2016.
 */
public class GameSession implements IGameSession{
    //private List<Team> teams;
   // private Map level;
    //private Player active;
    //private List<Buff>buffs;
    //private HashMap<Account,Player> identities
    //private ActionProcessor currentTurn
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

    public void update(){};

    //public void addBuffs(List<Buff> b){}

    //public void removeBuff(Buff b){}

    public void startTurn(){};

    public void finishTurn(){};

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public boolean finish() {
        return false;
    }

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
}
