package server;

import chat.Chat;
import chat.ChatInterface;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

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
