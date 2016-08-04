package server;

import GameObject.GameSession;
import GameObject.IGameSession;
import chat.Chat;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 07.05.2016.
 */
public class Server implements ServerInterface {

    static Registry reg;
    private List<GameSession> sessions = new ArrayList<>();


    public  Server(){}


    /**
     * Startet den Server.
     *
     * @param args
     */
    //@Override
   // public void main(String args) {

    //}

    /**
     * Gibt die Session mit jeweiligen Namen zurueck.
     *
     * @param sessionName Name der GameSession
     * @return
     */
    @Override
    public IGameSession loadSession(String sessionName){
        try{
            return (IGameSession)reg.lookup(sessionName);
        } catch (NotBoundException e) {
            GameSession tmp = new DBManager().loadSession(sessionName);
            sessions.add(tmp);
            try {
                IGameSession stub = (IGameSession) UnicastRemoteObject.exportObject(tmp, 0);
                reg.rebind(sessionName,stub);
                return (IGameSession)reg.lookup(sessionName);
            } catch (NotBoundException e1) {
                e1.printStackTrace();
                System.out.println("LoadSession: rebind of GameSession seems to not have worked");
            } catch (AccessException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Speichert eine GameSession.
     *
     * @param session GameSession die gespeichert werdenn soll
     * @return true ,wenn das Speichern funktioniert hat, sonst false.
     */
    @Override
    public boolean saveSession(IGameSession session) {
        for(GameSession s: sessions)
            try {
                if(s.getName().equals(session.getName()))
                         return new DBManager().saveSession(s);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        return false;
    }

    @Override
    public String createSession(String name){
        GameSession tmp = new GameSession();
        tmp.setName(name);
        if(new DBManager().saveSession(tmp)){
            sessions.add(tmp);
            IGameSession stub = null;
            try {
                stub = (IGameSession) UnicastRemoteObject.exportObject(tmp, 0);
                reg.rebind(name,stub);
                return name;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    /**
     * Diese Methode realisiert die Registration eines neuen Accounts.
     *
     * @param name     Name vom Account
     * @param password Passwort vom Account
     * @return true, wenn Registration geklappt hat, sonst false
     */
    @Override
    public boolean registerAccount(String name, String password) throws RemoteException {
        return new DBManager().registerAccount(name,password);
    }

    /**
     * Prueft, ob Eingaben zum Account korrekt sind.
     *
     * @param name     Name des Accounts
     * @param password Passwort des Accounts
     * @return true, wenn Pruefung erfolgreich, sonst false.
     */
    @Override
    public boolean checkAccount(String name, String password) throws RemoteException {
        return new DBManager().checkAccount(name,password);
    }

    /**
     * Beendet die Laufzeit des Servers.
     */


    /**
     * Gibt alle GameSession-Namen zurueck, die auf dem Server gespeichert sind.
     *
     * @return Namen der GameSession-Objekte
     */
    @Override
    public String getSessionList() throws RemoteException {
        return new DBManager().getSessionList();
    }

    public static void init() throws RemoteException
    {
        try{
            Server serv = new Server();
            ServerInterface stub = (ServerInterface)UnicastRemoteObject.exportObject(serv,0);

            reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            reg.rebind("ServerInterface",stub);

            System.out.println("Server: Server ready");
        } catch (RemoteException e){
            System.out.println(e.getMessage());
        }

    }


}
