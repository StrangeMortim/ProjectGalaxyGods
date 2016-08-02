package chat;

import Player.Player;
import projectgg.gag.GoldAndGreed;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 11.05.2016. (mod. 12.06)
 * Die Chat-Klasse ermoeglicht den Nachrichtenaustausch zwischen den Spielern.
 */
public class Chat implements ChatInterface,Serializable {

   // private static final long serialVersionUID = 1987963162254788571L;
    /**
     * Nachrichtenverlauf dieses Chats.
     */
    private List<Message> backLog = new ArrayList<Message>();
    /**
     * Spieler die in diesem Chat schreiben können.
     */
    private List<Player> participants = new ArrayList<Player>();
    /**
     * Spieler die in diesem Chat nur lesen dürfen.
     */
    private List<Player> readOnly = new ArrayList<Player>();

    public Chat(){  }

    public void addMessage(String player, String msg) throws RemoteException{
        if(player == null)
            throw new IllegalArgumentException("addMessage: Player is null");

        Message m = new Message();
        m.SetContent(player +": "+ msg);
        backLog.add(m);
    }



    /**
     * Diese Methode ermoeglicht das Loeschen einer Nachricht.
     *
     * @param m zu loeschende Message
     */
    @Override
    public void deleteMessage(Message m) {
        backLog.remove(m);
    }

    /**
     * Diese Methode fuegt einen Spieler dem Chat hinzu.
     *
     * @param p Player (Spieler) der hinzugefuegt werden soll.
     */
    @Override
    public void addParticipant(Player p) {
     if(p == null)
         throw new IllegalArgumentException("addParticipant: player is null");

     if(readOnly.contains(p))
         throw  new IllegalArgumentException("The Player is blocked from writing!");

        this.getParticipants().add(p);
    }

    /**
     * Loescht den Chat.
     */
    @Override
    public void clear() {
     backLog.clear();
    }

    /**
     * Diese Methode blockiert einen Spieler, wodurch er keine Nachrichten
     * im Chat schreiben kann.
     *
     * @param p zu blockierender Spieler
     */
    @Override
    public void blockPlayer(Player p) {
     if(p == null)
         throw new IllegalArgumentException("blockPlayer: player is null");

        readOnly.add(p);
    }

    /**
     * Diese Methode entfernt einen Spieler aus dem Chat.
     *
     * @param p Spieler der entfernt werden soll
     */
    @Override
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

    @Override
    public List<Player> getParticipants() {
        return participants;
    }

    @Override
    public List<Player> getReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(List<Player> p) {
     readOnly=p;
    }

    @Override
    public void setParticipants(List<Player> p) {
     participants=p;
    }
}
