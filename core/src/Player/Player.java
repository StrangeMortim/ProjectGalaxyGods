package Player;

import java.io.Serializable;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Player implements IPlayer,Serializable {

    private int[] ressources = new int[4];
    private TechnologyTree tree = new TechnologyTree();
    private Boolean turn = false;
    private Account account;
    private Boolean market = false;

    public Player(Account acc){
        if(acc == null)
            throw new IllegalArgumentException("Account must not be null");

        this.account = acc;
    }

    @Override
    public void setRessources(int[] ressources) {
        this.ressources = ressources;
    }

    @Override
    public int[] getRessources() {
        return ressources;
    }

    @Override
    public void setTechTree(TechnologyTree tree) {
        this.tree = tree;
    }

    @Override
    public TechnologyTree getTechTree() {
        return tree;
    }

    @Override
    public void setTurn(Boolean isTurn) {
        this.turn = isTurn;
    }

    @Override
    public Boolean getTurn() {
        return turn;
    }

    @Override
    public void setAccount(Account acc) {
        this.account = acc;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void setMarket(Boolean access) {
        this.market = access;
    }

    @Override
    public Boolean getMarket() {
        return market;
    }
}
