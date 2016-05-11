package ProjectGG;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 11.05.2016.
 */
public class ChatObject implements ChatInterface {

    private List backLog = new ArrayList<String>();

    public ChatObject(){  }

    public void addMessage(String player, String msg) {
        backLog.add(player+": "+msg);
    }

    public List getBacklog() {
        return backLog;
    }
}
