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
 * This class provides everyhting necessary for a client to connect to the current computer
 * and the computer serving as a server
 * @author Benjamin
 */
public class Server implements ServerInterface {

    /**
     * The Server itself
     */
    static Server serv;

    /**
     * The registry, save it as attribute to avoid the Server shutting down immediately after starting
     */
    static Registry reg;

    /**
     * all current GameSessions, save them so they don't get eaten by garbage collector
     * and other stuff
     */
    private List<GameSession> sessions = new ArrayList<>();


    public  Server(){}

    /**
     * Creates a Server and Registry and binds the Server to the Registry under the Key "ServerInterface"
     * to enable the access from clients to the Server
     */
    public static void init()
    {
        try{
            if(serv == null)
                serv = new Server();


            reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            ServerInterface stub = (ServerInterface)UnicastRemoteObject.exportObject(serv,0);

            reg.rebind("ServerInterface",stub);

            System.out.println("Server: Server ready");
        } catch (Exception e){

            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    /**
     * Loads the GameSession with the given Name and binds it to the registry, with the name as key
     *
     * @param sessionName the Name of the Session to load
     * @return the Interface stub, which can also be loaded from the Registry
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
     * Saves a GameSession, the GameSession is given by it's corresponding Interface Counterpart
     * and then identified through it's name
     *
     * @param session the Interface of the Session to save
     * @return true if savin was success else false
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

    /**
     * Creates a new session, saves it in the Database and binds it to the registry, with it's name as key
     * @param name the name of the session to create
     * @return the name under which the session can be found in the registry
     */
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
     * Just calls same method for the DBManager
     *
     * @param name     Name of the Account
     * @param password Password of the Account
     * @return true if registration was a success else false
     */
    @Override
    public boolean registerAccount(String name, String password) {
        return new DBManager().registerAccount(name,password);
    }

    /**
     * Just calls same method for the DBManager
     *
     * @param name     Name of the Account
     * @param password Password of the Account
     * @return true if values are valid, else false
     */
    @Override
    public boolean checkAccount(String name, String password)  {
        return new DBManager().checkAccount(name,password);
    }

    /**
     * Just calls DBManager counterpart
     *
     * @return all GameSession names separated with an ;
     */
    @Override
    public String getSessionList()  {
        return new DBManager().getSessionList();
    }




}
