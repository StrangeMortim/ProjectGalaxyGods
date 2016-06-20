package Player;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAccount extends Remote {

    /**
    Getter und Setter
     */
    public void setName(String name)throws RemoteException;

    public String getName()throws RemoteException;

    public void setPassword(String pw)throws RemoteException;

    public String getPassword()throws RemoteException;
}
