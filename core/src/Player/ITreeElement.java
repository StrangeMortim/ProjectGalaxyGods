package Player;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 22.07.2016.
 */
public interface ITreeElement extends Remote{

    public String getTreePath() throws RemoteException;

    public int getPreRequisiteIndex() throws RemoteException;

    public int[] getRessourceCosts()throws RemoteException;

    public void activate(Player player) throws RemoteException;
}
