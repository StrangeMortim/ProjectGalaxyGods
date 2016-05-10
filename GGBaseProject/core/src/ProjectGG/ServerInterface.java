package ProjectGG;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 07.05.2016.
 */
public interface ServerInterface extends Remote {

    public String sayHello() throws RemoteException;

    public void shutdown() throws RemoteException;
}
