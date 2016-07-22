package Player;


import Action.Buff;
import GameObject.Research;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IPlayer extends Remote {

    /**
    Getter und Setter fuer die Attribute
     */
    public void setRessources(int ressources[])throws RemoteException;

    public int[] getRessources()throws RemoteException;

    public int[] getRessourceBoni()throws RemoteException;

    public void setTechTree(TechnologyTree tree)throws RemoteException;

    public TechnologyTree getTechTree()throws RemoteException;

    public void setTurn(Boolean b)throws RemoteException;

    public Boolean getTurn()throws RemoteException;

    public void setAccount(Account acc)throws RemoteException;

    public Account getAccount()throws RemoteException;

    public void setMarket(Boolean access)throws RemoteException;

    public Boolean getMarket()throws RemoteException;

    public void setPermaBuffs(List<Research> permaBuffs)throws RemoteException;

    public List<Research> getPermaBuffs()throws RemoteException;

    public void setTemporaryBuffs(List<Research> temporaryBuffs)throws RemoteException;

    public List<Research> getTemporaryBuffs()throws RemoteException;

    public Team getTeam()throws RemoteException;

    public void setTeam(Team t)throws RemoteException;
}
