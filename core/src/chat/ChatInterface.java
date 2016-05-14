package chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Fabi on 11.05.2016.
 */
public interface ChatInterface extends Remote{

    public void addMessage(String player, String msg) throws RemoteException;

    public List getBacklog() throws RemoteException;
}