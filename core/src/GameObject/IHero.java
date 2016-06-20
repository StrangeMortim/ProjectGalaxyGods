package GameObject;

import Action.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IHero extends Remote {

    /**
     * Getter und Setter
     */
    public void setLeftHand(Action leftHand)throws RemoteException;

    public Action getLeftHand()throws RemoteException;

    public void setRightHand(Action rightHand)throws RemoteException;

    public Action getRightHand()throws RemoteException;

    public void setName(String name)throws RemoteException;

    public String getName()throws RemoteException;
}
