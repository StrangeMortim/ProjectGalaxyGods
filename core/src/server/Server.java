package server;

import GameObject.GameSession;
import chat.Chat;
import chat.ChatInterface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Fabi on 07.05.2016.
 */
public class Server implements ServerInterface {

    static Registry reg;


    public  Server(){}

    public String sayHello(){
        System.out.println("Recieving Invocation");
        return "Hello World!";
    }

    //returns null if something went wrong
    public void createChat() throws RemoteException {
        try {
            Chat chat = new Chat();
            ChatInterface stub = (ChatInterface) UnicastRemoteObject.exportObject(chat, 0);
            Server.reg.rebind("Chat", stub);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Startet den Server.
     *
     * @param args
     */
    @Override
    public void main(String args) {

    }

    /**
     * Gibt die Session mit jeweiligen Namen zurueck.
     *
     * @param sessionName Name der GameSession
     * @return
     */
    @Override
    public GameSession loadSession(String sessionName) {
        return null;
    }

    /**
     * Speichert eine GameSession.
     *
     * @param session GameSession die gespeichert werdenn soll
     * @return true ,wenn das Speichern funktioniert hat, sonst false.
     */
    @Override
    public boolean saveSession(GameSession session) {
        return false;
    }

    /**
     * Diese Methode realisiert die Registration eines neuen Accounts.
     *
     * @param name     Name vom Account
     * @param password Passwort vom Account
     * @return true, wenn Registration geklappt hat, sonst false
     */
    @Override
    public boolean registerAccount(String name, String password) {
        return false;
    }

    /**
     * Prueft, ob Eingaben zum Account korrekt sind.
     *
     * @param name     Name des Accounts
     * @param password Passwort des Accounts
     * @return true, wenn Pruefung erfolgreich, sonst false.
     */
    @Override
    public boolean checkAccount(String name, String password) {
        return false;
    }

    /**
     * Beendet die Laufzeit des Servers.
     */
    @Override
    public void shutdown() {

    }

    /**
     * Gibt alle GameSession-Namen zurueck, die auf dem Server gespeichert sind.
     *
     * @return Namen der GameSession-Objekte
     */
    @Override
    public List<String> getSessionList() {
        return null;
    }

    public static void init()
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
