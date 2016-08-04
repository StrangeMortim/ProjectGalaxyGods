package Player;

import Action.Buff;
import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Hero;
import GameObject.Research;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Player implements Serializable {

    //private static final long serialVersionUID = 4714946459418095704L;
    private int[] ressources = new int[]{Constants.PLAYER_START_WOOD,Constants.PLAYER_START_IRON,Constants.PLAYER_START_GOLD,Constants.PLAYER_START_MANA};
    private int[] ressourcesBoni = new int[4];
    private TechnologyTree tree;
    private boolean turn = false;
    private Account account;
    private boolean market = false;
    private List<Buff> permaBuffs = new ArrayList<>();
    private List<Buff> avaibleTemporaryBuffs = new ArrayList<>();
    private Team team;
    private boolean hasReducedUnitCosts = false;
    private Hero hero;
    private int iD;
    private GameSession session;

    public Player(Account acc, GameSession session){
        if(acc == null || session == null)
            throw new IllegalArgumentException("Account must not be null");

        this.account = acc;
        this.session = session;
        try {
            iD = session.registerObject(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        tree = new TechnologyTree(session);
    }


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


    public void setRessources(int[] ressources) {
        this.ressources = ressources;
    }


    public int[] getRessources() {
        return ressources;
    }


    public int[] getRessourceBoni() {
        return ressourcesBoni;
    }


    public void setTechTree(TechnologyTree tree) {
        this.tree = tree;
    }


    public TechnologyTree getTechTree() {
        return tree;
    }


    public void setTurn(boolean isTurn) {
        this.turn = isTurn;
    }


    public Boolean getTurn() {
        return turn;
    }


    public void setAccount(Account acc) {
        this.account = acc;
    }


    public Account getAccount() {
        return account;
    }


    public void setMarket(boolean access) {
        this.market = access;
    }


    public Boolean getMarket() {
        return market;
    }


    public void setPermaBuffs(List<Buff> permaBuffs) {
        this.permaBuffs = permaBuffs;
    }


    public List<Buff> getPermaBuffs() {
        return permaBuffs;
    }


    public void setTemporaryBuffs(List<Buff> temporaryBuffs) {
        this.avaibleTemporaryBuffs = temporaryBuffs;
    }


    public List<Buff> getTemporaryBuffs() {
        return avaibleTemporaryBuffs;
    }


    public Team getTeam() {
        return team;
    }


    public void setTeam(Team t){
        this.team=t;
    }


    public void setReducedUnitCost(boolean reducedUnitCost)  {
        this.hasReducedUnitCosts = reducedUnitCost;
    }


    public boolean hasReducedUnitCosts() {
        return hasReducedUnitCosts;
    }


    public void setHero(Hero hero)  {
        this.hero = hero;
    }


    public Hero getHero() {
        return hero;
    }


    public void addPermaBuff(Buff b) {
        if(b != null && !permaBuffs.contains(b))
            permaBuffs.add(b);
    }


    public void addTemporaryBuff(Buff b)  {
        if(b != null)
            avaibleTemporaryBuffs.add(b);
    }


    public int getId(){
        return iD;
    }


}
