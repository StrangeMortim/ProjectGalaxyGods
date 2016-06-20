package Player;


import chat.Chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ITeam extends Remote {

    /*
    Getter und Setter fuer die Attribute
     */
    public void setPlayers(List<Player> players)throws RemoteException;

    public List<Player> getPlayers()throws RemoteException;

    public void setChat(Chat chat)throws RemoteException;

    public Chat getChat()throws RemoteException;

    public void setColor(String color)throws RemoteException;

    public String getColor()throws RemoteException;

    /*
    Check = Kasse
     */
    public void setCheck(int check[])throws RemoteException;

    public int[] getCheck()throws RemoteException;
}
