package Action;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMovement extends Remote {

    /**
     * Getter und setter
     */
    public void setXAmount(int xAmount)throws RemoteException;

    public int getXAmount()throws RemoteException;

    public void setYAmount(int yAmount)throws RemoteException;

    public int getYAmount()throws RemoteException;

}
