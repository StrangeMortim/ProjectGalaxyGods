package Action;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 27.07.2016.
 */
public interface IBuffInfo extends Remote, Serializable {

    public int[] getBuffCost() throws RemoteException;

    public int getRounds() throws RemoteException;

    public int getPower() throws RemoteException;

    public int getAtk() throws RemoteException;
    public int getDef() throws RemoteException;
    public int getRange() throws RemoteException;
    public int getMovepoints() throws RemoteException;
    public int getHp() throws RemoteException;
    public boolean isPermanent() throws RemoteException;
}
