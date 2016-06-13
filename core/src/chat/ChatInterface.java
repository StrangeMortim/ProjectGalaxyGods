package chat;

import Player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Fabi on 11.05.2016. (mod. by Ben 12.6)
 * Diese Interface ist für das Chat-Objekt. Es realisiert die Kommunikation
 * durch Message-Objekte.
 */
public interface ChatInterface extends Remote{
    /**
     * Fuegt eine neue Nachricht zum Chat hinzu.
     * @param player Name des Verfassers
     * @param msg Inhalt der Nachricht
     * @throws RemoteException
     */
    public void addMessage(String player, String msg) throws RemoteException;
    /**
     * Diese Methode ermoeglicht das Loeschen einer Nachricht.
     * @param m zu loeschende Message
     */
    public void deleteMessage(Message m);
    /**
     * Diese Methode fuegt einen Spieler dem Chat hinzu.
     * @param p Player (Spieler) der hinzugefuegt werden soll.
     */
    public void addParticipant(Player p);
    /**
     * Diese Methode entfernt einen Spieler aus dem Chat.
     * @param p Spieler der entfernt werden soll
     */
    public void removeParticipant(Player p);
    /**
     * Diese Methode blockiert einen Spieler, wodurch er keine Nachrichten
     * im Chat schreiben kann.
     * @param p zu blockierender Spieler
     */
    public void blockPlayer(Player p);
    /**
     * Loescht den Chat.
     */
    public void clear();


    //Getter Setter
    public List getBacklog() throws RemoteException;
    public List<Player> getParticipants();
    public void setParticipants(List<Player> p);
    public List<Player> getReadOnly();
    public void setReadOnly(List<Player>p);
}