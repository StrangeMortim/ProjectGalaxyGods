package server;

import GameObject.GameSession;
import GameObject.IGameSession;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Just a remote Interface to enable access for the Screens therefore no comments->check Server
 * @author Fabi
 */
public interface ServerInterface extends Remote{

    public IGameSession loadSession(String sessionName) throws RemoteException;

    public boolean saveSession(IGameSession session) throws RemoteException;

    public String createSession(String name) throws RemoteException;

    public boolean registerAccount(String name, String password) throws RemoteException;

    public boolean checkAccount(String name, String password) throws RemoteException;

    public String getSessionList() throws RemoteException;
}
