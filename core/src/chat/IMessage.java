package chat;

import Player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by benja_000 on 12.06.2016.
 * Dieses Interface ist fuer die Klasse: Message.
 * Es realisiert die noetigen Eigenschaften von Nachrichten im Chat.
 */
public interface IMessage extends Remote {
    /**
     * Macht die Nachricht sichtbar fuer ausgewaehlte Spieler.
     * @param p der ausgewaehlte Spieler
     */
    public void makeVisibleFor(Player p);

    //Getter Setter
    public String getContent()throws RemoteException;
    public void SetContent(String s)throws RemoteException;
    public boolean getVisibleForAll()throws RemoteException;
    public void setVisibleForAll(boolean b)throws RemoteException;
    public List<Player>getVisibleFor()throws RemoteException;
    public void setVisibleFor(List<Player> p)throws RemoteException;
}
