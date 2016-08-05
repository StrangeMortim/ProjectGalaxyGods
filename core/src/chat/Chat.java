package chat;

import GameObject.GameSession;
import Player.Player;
import projectgg.gag.GoldAndGreed;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class enables Chatting between Players
 */
public class Chat implements Serializable {

    /**
     * All the Messages in the Chat
     */
    private List<Message> backLog = new ArrayList<Message>();
    /**
     * All player who have the right to write in this Chat
     */
    private List<Player> participants = new ArrayList<Player>();
    /**
     * Player who may only read but not write in the Chat
     */
    private List<Player> readOnly = new ArrayList<Player>();

    /**
     * The Session this Object belongs to and where it's registered
     */
    private GameSession session;

    /**
     * The global ID of the Object
     */
    private int iD;

    public Chat(GameSession session){
        if (session == null)
            throw new IllegalArgumentException("Session ist null in Chat");

        this.session = session;
            iD = session.registerObject(this);

    }

    /**
     * Adds a new Message to the Chat
     * @param player the message sending player
     * @param msg the content of the message
     */
    public void addMessage(Player player, String msg) {
        if(player == null)
            throw new IllegalArgumentException("addMessage: Player is null");

        if(!participants.contains(player) || readOnly.contains(player))
            throw new IllegalArgumentException("Spieler ist nicht zum Schreiben berechtigt");

        Message m = new Message(session);
        m.SetContent(player.getAccount().getName() +": "+ msg);
        backLog.add(m);
    }


    /**
     * Adds a new participant in the chat
     *
     * @param p the player to add
     */

    public void addParticipant(Player p) {
     if(p == null)
         throw new IllegalArgumentException("addParticipant: player is null");

     if(readOnly.contains(p))
         throw  new IllegalArgumentException("The Player is blocked from writing!");

        this.getParticipants().add(p);
    }

    /**
     * Clears the Chat
     */

    public void clear() {
     backLog.clear();
    }

    /**
     * Disables a player from writing in the chat
     *
     * @param p the player to disable
     */

    public void blockPlayer(Player p) {
     if(p == null)
         throw new IllegalArgumentException("blockPlayer: player is null");

        readOnly.add(p);
    }

    /**
     * enables a blocked player to write again
     *
     * @param p the blocked player to be enabled
     */
    public void unblockPlayer(Player p)  {
        if(p == null)
            throw new IllegalArgumentException("blockPlayer: player is null");

        readOnly.remove(p);
    }

    /**
     * Removes a participant from the chat
     *
     * @param p the player to remove
     */

    public void removeParticipant(Player p) {
     if(p == null)
         throw new IllegalArgumentException("removeParticipant: player is null");

        readOnly.remove(p);
        participants.remove(p);
    }



    //Getter Setter
    public List<Message> getBacklog() {
        return backLog;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public List<Player> getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(List<Player> p) {
     readOnly=p;
    }

    public void setParticipants(List<Player> p) {
     participants=p;
    }

    public int getId(){
        return iD;
    }
}
