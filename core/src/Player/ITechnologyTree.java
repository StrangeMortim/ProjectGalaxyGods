package Player;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITechnologyTree extends Remote {

    /*
    Getter und Setter
     */
    public void setSteel(boolean achieved[])throws RemoteException;

    public boolean[] getSteel()throws RemoteException;

    public void setMagic(boolean achieved[])throws RemoteException;

    public boolean[] getMagic()throws RemoteException;

    public void setCulture(boolean achieved[])throws RemoteException;

    public boolean[] getCulture()throws RemoteException;

}
