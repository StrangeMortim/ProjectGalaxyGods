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

    public boolean isSteelFull() throws RemoteException;

    public void setSteelFull(boolean full) throws RemoteException;

    public boolean isMagicFull() throws RemoteException;

    public void  setMagicFull(boolean full) throws RemoteException;

    public boolean isCultureFull() throws RemoteException;

    public void setCultureFull(boolean full) throws RemoteException;

}
