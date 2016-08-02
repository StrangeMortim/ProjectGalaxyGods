package Player;

import Action.Buff;
import GameObject.Constants;
import GameObject.Hero;
import GameObject.Research;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Player implements IPlayer,Serializable {

    //private static final long serialVersionUID = 4714946459418095704L;
    private int[] ressources = new int[]{Constants.PLAYER_START_WOOD,Constants.PLAYER_START_IRON,Constants.PLAYER_START_GOLD,Constants.PLAYER_START_MANA};
    private int[] ressourcesBoni = new int[4];
    private TechnologyTree tree = new TechnologyTree();
    private boolean turn = false;
    private Account account;
    private boolean market = false;
    private List<Buff> permaBuffs = new ArrayList<>();
    private List<Buff> avaibleTemporaryBuffs = new ArrayList<>();
    private Team team;
    private boolean hasReducedUnitCosts = false;
    private Hero hero;

    public Player(Account acc){
        if(acc == null)
            throw new IllegalArgumentException("Account must not be null");

        this.account = acc;
    }

    @Override
    public boolean advanceOnTechTree(TreeElement element) {
        switch (element.getTreePath()){
            case "steel":
                if(element.getPreRequisiteIndex() != -1 && !tree.getSteel()[element.getPreRequisiteIndex()])
                    return false;

                break;
            case "magic":
                if(element.getPreRequisiteIndex() != -1 && !tree.getMagic()[element.getPreRequisiteIndex()])
                    return false;

                break;
            case "culture":
                if(element.getPreRequisiteIndex() != -1 && !tree.getCulture()[element.getPreRequisiteIndex()])
                    return false;

                break;
        }

        for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
            if(ressources[i] < element.getRessourceCosts()[i])
                return false;

        for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
            ressources[i] -= element.getRessourceCosts()[i];

        element.activate(this);

        return true;
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
    public void setTurn(boolean isTurn) {
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
    public void setMarket(boolean access) {
        this.market = access;
    }

    @Override
    public Boolean getMarket() {
        return market;
    }

    @Override
    public void setPermaBuffs(List<Buff> permaBuffs) {
        this.permaBuffs = permaBuffs;
    }

    @Override
    public List<Buff> getPermaBuffs() {
        return permaBuffs;
    }

    @Override
    public void setTemporaryBuffs(List<Buff> temporaryBuffs) {
        this.avaibleTemporaryBuffs = temporaryBuffs;
    }

    @Override
    public List<Buff> getTemporaryBuffs() {
        return avaibleTemporaryBuffs;
    }

    @Override
    public Team getTeam() throws RemoteException {
        return team;
    }

    @Override
    public void setTeam(Team t) throws RemoteException {
        this.team=t;
    }

    @Override
    public void setReducedUnitCost(boolean reducedUnitCost) throws RemoteException {
        this.hasReducedUnitCosts = reducedUnitCost;
    }

    @Override
    public boolean hasReducedUnitCosts() throws RemoteException {
        return hasReducedUnitCosts;
    }

    @Override
    public void setHero(Hero hero) throws RemoteException {
        this.hero = hero;
    }

    @Override
    public Hero getHero() throws RemoteException {
        return hero;
    }

    @Override
    public void addPermaBuff(Buff b) throws RemoteException {
        if(b != null && !permaBuffs.contains(b))
            permaBuffs.add(b);
    }

    @Override
    public void addTemporaryBuff(Buff b) throws RemoteException {
        if(b != null)
            avaibleTemporaryBuffs.add(b);
    }


}
