package Action;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 27.07.2016.
 */
public interface ISpecialBuff extends Remote {

    public int[] getBuffCost() throws RemoteException;

    public int getRounds() throws RemoteException;
}
