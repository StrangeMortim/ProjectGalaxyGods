package Player;


import Action.Buff;
import GameObject.Hero;
import GameObject.Research;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IPlayer extends Remote {

    public boolean advanceOnTechTree(TreeElement element);

    /**
    Getter und Setter fuer die Attribute
     */
    public void setRessources(int ressources[])throws RemoteException;

    public int[] getRessources()throws RemoteException;

    public int[] getRessourceBoni()throws RemoteException;

    public void setTechTree(TechnologyTree tree)throws RemoteException;

    public TechnologyTree getTechTree()throws RemoteException;

    public void setTurn(boolean b)throws RemoteException;

    public Boolean getTurn()throws RemoteException;

    public void setAccount(Account acc)throws RemoteException;

    public Account getAccount()throws RemoteException;

    public void setMarket(boolean access)throws RemoteException;

    public Boolean getMarket()throws RemoteException;

    public void setPermaBuffs(List<Buff> permaBuffs)throws RemoteException;

    public List<Buff> getPermaBuffs()throws RemoteException;

    public void setTemporaryBuffs(List<Buff> temporaryBuffs)throws RemoteException;

    public List<Buff> getTemporaryBuffs()throws RemoteException;

    public Team getTeam()throws RemoteException;

    public void setTeam(Team t)throws RemoteException;

    public void setReducedUnitCost(boolean reducedUnitCost) throws RemoteException;

    public boolean hasReducedUnitCosts() throws RemoteException;

    public void setHero(Hero hero) throws RemoteException;

    public Hero getHero() throws RemoteException;

    public void addPermaBuff(Buff b) throws RemoteException;

    public void addTemporaryBuff(Buff b) throws RemoteException;
}
