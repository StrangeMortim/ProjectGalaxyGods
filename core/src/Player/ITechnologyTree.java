package Player;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITechnologyTree extends Remote {

    /*
    Getter und Setter
     */
    public void setSteel(Boolean achieved[])throws RemoteException;

    public Boolean[] getSteel()throws RemoteException;

    public void setMagic(Boolean achieved[])throws RemoteException;

    public Boolean[] getMagic()throws RemoteException;

    public void setCulture(Boolean achieved[])throws RemoteException;

    public Boolean[] getCulture()throws RemoteException;

}
