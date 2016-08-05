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
 * this class realises a player in a GameSession
 * @author Benjamin
 */
public class Player implements Serializable {

    /**
     * The resources of the player
     */
    private int[] ressources = new int[]{Constants.PLAYER_START_WOOD,Constants.PLAYER_START_IRON,Constants.PLAYER_START_GOLD,Constants.PLAYER_START_MANA};

    /**
     * The Bonus values for the different resources
     */
    private int[] ressourcesBoni = new int[4];

    /**
     * The personal TechnologyTree for the player
     */
    private TechnologyTree tree;

    /**
     * if the player is on turn
     */
    private boolean turn = false;

    /**
     * the account to the player
     */
    private Account account;

    /**
     * if the player has market access
     */
    private boolean market = false;

    /**
     * References for the permanent buffs of the player
     */
    private List<Buff> permaBuffs = new ArrayList<>();

    /**
     * References for the temporary buffs of the player
     */
    private List<Buff> avaibleTemporaryBuffs = new ArrayList<>();

    /**
     * the team the player belongs to
     */
    private Team team;

    /**
     * if the reduced unit-cost buff is active for the player
     */
    private boolean hasReducedUnitCosts = false;

    /**
     * the Hero of the player
     */
    private Hero hero;

    /**
     * the global ID of the Object
     */
    private int iD;

    /**
     * the session this object belongs to and is registered at
     */
    private GameSession session;

    public Player(Account acc, GameSession session){
        if(acc == null || session == null)
            throw new IllegalArgumentException("Account must not be null");

        this.account = acc;
        this.session = session;

            iD = session.registerObject(this);

        tree = new TechnologyTree(session);
    }

    /**
     * Lets the player advance on his TechTree for the given element
     * @param element  the next element the player wants to reach
     * @return  whether the action was a success or not
     */
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

    /**
     * adds a permanent buff-reference
     * @param b the buff to add
     */
    public void addPermaBuff(Buff b) {
        if(b != null && !permaBuffs.contains(b))
            permaBuffs.add(b);
    }

    /**
     * adds a temporary buff-reference
     * @param b the buff to add
     */
    public void addTemporaryBuff(Buff b)  {
        if(b != null)
            avaibleTemporaryBuffs.add(b);
    }

    /**
     * Getter/setter
     */
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

    public int getId(){
        return iD;
    }


}
