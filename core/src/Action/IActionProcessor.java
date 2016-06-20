package Action;


import GameObject.GameSession;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IActionProcessor extends Remote {

    /**
     * Getter und setter
     */
    public void setSession(GameSession session)throws RemoteException;

    public GameSession getSession()throws RemoteException;

    /**
     * Fuegt eine neue zu verarbeitende Action hinzu
     *
     * @param toAdd die hinzu zu fuegenden Action
     */
    public void addAction(Action toAdd)throws RemoteException;

    /**
     * Entfernt die angegebene Action aus den zu verarbeitenden
     * wenn die action nicht vorhanden ist oder null passiert nichts
     *
     * @param toRemove die zu entfernende Action
     */
    public void removeAction(Action toRemove)throws RemoteException;

    /**
     * Verarbeitet alle Actions in toProcess ueber ihre execute-Methode,
     * nach jeder Action wird dabei ueberprueft ob sich fuer target oder origin der Action
     * neue Actions oder Buffs ergeben, diese werden dann generiert und hinzu gefuegt
     *
     * @return toReturn mit allen in diesem Zug generierten Buffs
     */
    public List<Buff> execute()throws RemoteException;

}
