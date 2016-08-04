package Action;

import GameObject.GameSession;
import GameObject.Unit;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;

public abstract class Action implements Serializable {

    protected Unit origin;
    protected Unit target;
    protected String iconName = "";
    protected Player player;
    protected int iD;
    protected GameSession session;


    public Action(Unit origin, Unit target, Player player, GameSession session){
        if(session == null || (origin == null && player == null))
            throw new IllegalArgumentException("Processor must not be null and either origin or player or both must be set");

        this.origin = origin;
        this.target = target;
        this.player = player;
        this.session = session;
        try {
            iD = session.registerObject(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fuehrt die Action aus, die jeweiligen Implementationen bestimmen den Inhalt dieser Methode
     */

    public boolean execute() {
        return true;
    }


    /**
     * Getter/setter
     */

    public Player getPlayer(){
        return player;
    }


    public  void setOrigin(Unit origin){
        this.origin = origin;
    }


    public Unit getOrigin(){
        return origin;
    }


    public int getId() {
        return iD;
    }
}
