package ProjectGG;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 11.05.2016.
 */
public class ChatObject implements ChatInterface {

    private List backLog = new ArrayList<String>();

    public ChatObject(){
        try {
            GalaxyGods.reg.bind("Chat",this);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (AlreadyBoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addMessage(String player, String msg) {
        backLog.add(player+": "+msg);
    }

    @Override
    public List getBacklog() {
        return backLog;
    }
}
