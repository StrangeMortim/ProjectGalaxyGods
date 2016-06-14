package GameObject;

import java.rmi.Remote;

/**
 * Created by Fabi on 14.06.2016.
 */
public interface IBuilding extends Remote {

    public int[] getRessourceCost();

    public int getBuildTime();
}
