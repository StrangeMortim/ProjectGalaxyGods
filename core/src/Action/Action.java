package Action;

import GameObject.GameSession;
import GameObject.Unit;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * This class realises a few different, but quite specific
 * Actions that can be executed during the game
 *
 * @author Benjamin
 */
public abstract class Action implements Serializable {

    /**
     * The Unit from whom the Action started(the attacker for example)
     */
    protected Unit origin;

    /**
     * The Unit to whom the Action directs(for example the defender)
     */
    protected Unit target;

    /**
     * The owner of the origin unit or any player instead of an origin,
     * depends on what the action does
     */
    protected Player player;

    /**
     * The global ID of the Object
     */
    protected int iD;

    /**
     * The GameSession to which the Action belongs and where it is registered
     */
    protected GameSession session;


    public Action(Unit origin, Unit target, Player player, GameSession session){
        if(session == null || (origin == null && player == null))
            throw new IllegalArgumentException("Invalid Values");

        this.origin = origin;
        this.target = target;
        this.player = player;
        this.session = session;
            iD = session.registerObject(this);                                   //recieve ID

    }

    /**
     * Executes the Action, content depends on specific implementation
     * @return wether the execution was a success or not
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
