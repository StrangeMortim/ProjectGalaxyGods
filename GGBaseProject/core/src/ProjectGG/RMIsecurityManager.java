package ProjectGG;

/**
 * Created by Fabi on 07.05.2016.
 */
public class RMIsecurityManager extends SecurityManager {
    public RMIsecurityManager(){
        System.setProperty("java.security.policy", "./GG.policy");
    }
}
