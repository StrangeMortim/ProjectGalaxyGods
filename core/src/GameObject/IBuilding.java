package GameObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 14.06.2016.
 */
public interface IBuilding extends Remote {

    public int[] getRessourceCost()throws RemoteException;

    public int getBuildTime()throws RemoteException;
}
