package Player;


import Action.Buff;
import GameObject.Research;

import java.rmi.Remote;
import java.util.List;

public interface IPlayer extends Remote {

    /**
    Getter und Setter fuer die Attribute
     */
    public void setRessources(int ressources[]);

    public int[] getRessources();

    public int[] getRessourceBoni();

    public void setTechTree(TechnologyTree tree);

    public TechnologyTree getTechTree();

    public void setTurn(Boolean b);

    public Boolean getTurn();

    public void setAccount(Account acc);

    public Account getAccount();

    public void setMarket(Boolean access);

    public Boolean getMarket();

    public void setPermaBuffs(List<Research> permaBuffs);

    public List<Research> getPermaBuffs();

    public void setTemporaryBuffs(List<Research> temporaryBuffs);

    public List<Research> getTemporaryBuffs();
}
