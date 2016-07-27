package Action;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 27.07.2016.
 */
public interface ISpecialBuff extends Remote, Serializable {

    public int[] getBuffCost() throws RemoteException;

    public int getRounds() throws RemoteException;
}
