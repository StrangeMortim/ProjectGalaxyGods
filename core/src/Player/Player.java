package Player;

import Action.Buff;
import GameObject.Constants;
import GameObject.Research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Player implements IPlayer,Serializable {

    private int[] ressources = new int[]{Constants.PLAYER_START_WOOD,Constants.PLAYER_START_IRON,Constants.PLAYER_START_GOLD,Constants.PLAYER_START_MANA};
    private int[] ressourcesBoni = new int[4];
    private TechnologyTree tree = new TechnologyTree();
    private Boolean turn = false;
    private Account account;
    private Boolean market = false;
    private List<Research> permaBuffs = new ArrayList<>();
    private List<Research> avaibleTemporaryBuffs = new ArrayList<>();

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
    public int[] getRessourceBoni() {
        return ressourcesBoni;
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

    @Override
    public void setPermaBuffs(List<Research> permaBuffs) {
        this.permaBuffs = permaBuffs;
    }

    @Override
    public List<Research> getPermaBuffs() {
        return permaBuffs;
    }

    @Override
    public void setTemporaryBuffs(List<Research> temporaryBuffs) {
        this.avaibleTemporaryBuffs = temporaryBuffs;
    }

    @Override
    public List<Research> getTemporaryBuffs() {
        return avaibleTemporaryBuffs;
    }


}
