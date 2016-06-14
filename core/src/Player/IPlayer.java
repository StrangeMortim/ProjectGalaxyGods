package Player;


import java.rmi.Remote;

public interface IPlayer extends Remote {

    /**
    Getter und Setter fuer die Attribute
     */
    public void setRessources(int ressources[]);

    public int[] getRessources();

    public void setTechTree(TechnologyTree tree);

    public TechnologyTree getTechTree();

    public void setTurn(Boolean b);

    public Boolean getTurn();

    public void setAccount(Account acc);

    public Account getAccount();

    public void setMarket(Boolean access);

    public Boolean getMarket();
}
