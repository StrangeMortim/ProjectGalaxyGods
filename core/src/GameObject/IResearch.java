package GameObject;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import Action.BuffInfo;
import Player.Player;

public interface IResearch extends Remote{

    /**
     * Ermoeglicht Zugriffe ueber einen String
     * @param name der Name der gesuchten Forschung
     * @return die zugehoerige Forschung
     */
    public static Research getResearchByName(String name)throws RemoteException {
        /*TODO*/
        return null;
    };

    public int[] getRessourceCost()throws RemoteException;

    public int[] getValues()throws RemoteException;

    /**
     * @return  Entweder alle Unit für die die Werte gelten oder die Unit die durch diese Forschung freigeschaltet wird
     * letzteres gekennzeichnet durch nur 0 in getValues
     */
    public List<UnitType> getTargets()throws RemoteException;

    public int getResearchTime()throws RemoteException;

    public boolean isPermanet()throws RemoteException;

    public boolean research(Base b) throws RemoteException;

    public BuffInfo getInfo() throws RemoteException;
}
