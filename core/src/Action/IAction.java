package Action;

import GameObject.Unit;
import Player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 11.06.2016.
 */
public interface IAction extends Remote {

    /**
     * Fuehrt die Action aus, die jeweiligen Implementationen bestimmen den Inhalt dieser Methode
     */
    public boolean execute()throws RemoteException;

    /**
     * Getter/setter
     */
    public Player getPlayer()throws RemoteException;

    public void setOrigin(Unit origin)throws RemoteException;

    public Unit getOrigin()throws RemoteException;

    public void setParent(ActionProcessor processor)throws RemoteException;
}
