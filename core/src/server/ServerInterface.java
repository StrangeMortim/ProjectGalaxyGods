package server;

import GameObject.GameSession;
import GameObject.IGameSession;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 07.05.2016.
 */
public interface ServerInterface extends Remote{

    public String sayHello() throws RemoteException;

    public void createChat() throws RemoteException;

    /**
     * Startet den Server.
     * @param args
     */
   // public void main(String args);

    /**
     * Gibt die Session mit jeweiligen Namen zurueck.
     * @param sessionName Name der GameSession
     * @return
     */
    public IGameSession loadSession(String sessionName) throws RemoteException;

    /**
     * Speichert eine GameSession.
     * @param session GameSession die gespeichert werdenn soll
     * @return true ,wenn das Speichern funktioniert hat, sonst false.
     */
    public boolean saveSession(IGameSession session) throws RemoteException;

    public String createSession(String name) throws RemoteException;


    /**
     * Diese Methode realisiert die Registration eines neuen Accounts.
     * @param name Name vom Account
     * @param password Passwort vom Account
     * @return true, wenn Registration geklappt hat, sonst false
     */
    public boolean registerAccount(String name, String password) throws RemoteException;

    /**
     * Prueft, ob Eingaben zum Account korrekt sind.
     * @param name Name des Accounts
     * @param password Passwort des Accounts
     * @return true, wenn Pruefung erfolgreich, sonst false.
     */
    public boolean checkAccount(String name, String password) throws RemoteException;

    /**
     * Beendet die Laufzeit des Servers.
     */
   // public void shutdown();

    /**
     * Gibt alle GameSession-Namen zurueck, die auf dem Server gespeichert sind.
     * @return Namen der GameSession-Objekte
     */
    public String getSessionList() throws RemoteException;
}
