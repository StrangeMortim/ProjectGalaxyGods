package chat;

import GameObject.GameSession;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a message inside a chat and contains information about who can see the message
 * @author Benjamin
 */
public class Message implements Serializable {

    /**
     * Content of the Message
     */
    private String content;
    /**
     * Determines if the message is visible for all
     */
    private boolean visibleForAll = true;

    /**
     * If not visible for all this contains all players who can see the message
     */
    private List<Player> visibleFor=new ArrayList<Player>();

    /**
     * The session this object belongs to and where it's registered
     */
    private GameSession session;

    /**
     * the global ID of the Object
     */
    private int iD;


    public Message(GameSession session){
        if (session == null)
            throw new IllegalArgumentException("Session ist null in Message");

        this.session = session;

            iD = session.registerObject(this);

    }

    /**
     * Enables the given player to see the message
     * @param p the player who shall the see message
     */
    public void makeVisibleFor(Player p) {
        if(p == null)
            throw new IllegalArgumentException("MakeVisibleFor: the Player is null");

            if(!visibleFor.contains(p))
                visibleFor.add(p);
    }


    /**
     * Getter/Setter
     */
    public String getContent() {
        return content;
    }
    public void SetContent(String s) {
    content=s;
    }

    public boolean getVisibleForAll() {
        return visibleForAll;
    }
    public void setVisibleForAll(boolean b) {
    visibleForAll=b;
    }

    public List<Player> getVisibleFor() {
        return visibleFor;
    }
    public void setVisibleFor(List<Player> p) {
    visibleFor=p;
    }
}
