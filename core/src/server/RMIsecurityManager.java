package server;

/**
 * Created by Fabi on 07.05.2016.
 */
public class RMIsecurityManager extends SecurityManager {
    public RMIsecurityManager(){
        System.setProperty("java.server.policy", "./GG.policy");
    }
}
