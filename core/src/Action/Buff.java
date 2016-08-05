package Action;

import GameObject.*;
import Player.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * A buff is a specific kind of Action which has additional values,
 * most of the additional values correspond to one value of Unit
 * more to that in execute
 *
 * special case can be that the buff adds a new Unit to the avaible Units
 *
 * @author Fabi
 */
public class Buff extends Action{

    /**
     * Used for Research in Base
     */
    protected Research source;

    /**
     * Contains all UnitType who benefit from this Buff
     * empty equals all UnitTypes
     */
    protected List<UnitType> appliesFor = new ArrayList<>();

    /**
     * if the Buff ist Permanent
     */
    protected boolean permanent;

    /**
     * How many rounds remain until the Buff loses it's effect
     */
    protected int roundsLeft;

    /**
     * Correspond to the Unit values
     */
    protected int atk;
    protected int def;
    protected int hp;
    protected int range;
    protected int movePoints;

    /**
     * If the Buff is executed for the first Time, used to determine what the Buff should do
     */
    protected boolean firstTime = true;

    /**
     * The BuffInfo that was used to determine the values, used for personal copy
     */
    protected BuffInfo info;


    public Buff(Unit origin, Player player, BuffInfo info, GameSession session) {
        super(origin, null, player, session);

        permanent = info.isPermanent();
        roundsLeft = info.getRounds();
        atk = info.getAtk();
        def = info.getDef();
        hp = info.getHp();
        range = info.getRange();
        movePoints = info.getMovepoints();
        this.info = info;
    }

    /**
     * If firstTime is true then the Buff increases origins Values,
     * by the Buffs corresponding values(e.g. Unit.atk += Buff.atk)
     *
     * If firstTime is false but the Buff has still rounds remaining
     * it will just count down the rounds by 1
     *
     * If firstTime is false and the Buff has no Rounds remaining
     * it will remove origins value increases(e.g. Unit.atk -= Buff.atk)
     *
     * @return wether the Buff is still needed or can be deleted
     */
    @Override
    public boolean execute(){

               /*
        if the buff is not permanent, count down the remaining rounds
        and if they reach 0, remove all the values an return true, so it can be removed
         */
        if(!permanent && !firstTime) {
            roundsLeft--;
            if(roundsLeft <= 0){
                if(origin != null){
                    origin.setAtk(origin.getAtk()-this.atk);
                    origin.setDef(origin.getDef()-this.def);
                    origin.setMaxHp(origin.getMaxHp()-this.hp);
                    origin.setRange(origin.getRange()-this.range);
                    origin.setMovePoints(origin.getMovePoints()-this.movePoints);
                }
                return true;
            }
        }

        //if first time executed, do stuff
        if(firstTime){

            /*
            special case, buff only adds a new avaible unit
            returns true after that because once it's done, the buff can be removed
             */
            if(atk == 0 && def == 0 && hp == 0 && range == 0 && movePoints == 0){
                if(origin != null && origin instanceof Base && appliesFor != null) {
                    ((Base) origin).getAvaibleUnits().add(appliesFor.get(0));
                    return true;
                }

            } else {
               //usual case, buff applies it's values
                if(origin != null){
                    origin.setAtk(origin.getAtk()+this.atk);
                    origin.setDef(origin.getDef()+this.def);
                    origin.setMaxHp(origin.getMaxHp()+this.hp);
                    origin.setRange(origin.getRange()+this.range);
                    origin.setMovePoints(origin.getMovePoints()+this.movePoints);
                    firstTime = false;   //set to false so it's not done again
                } else {
                    return true;
                }
            }
        }

        //permanent or rounds remain-> return false for not finished
        return false;
    }


    /**
     * creates a copy Base on this Buff, with the same rounds remaining
     *
     * @param u the Unit who receives the copy
     * @return the copy of the Buff
     */
    public Buff getPersonalCopy(Unit u){
        Buff result = new Buff(u, this.player,BuffInfo.NONE, session);
        result.setSource(this);
        result.setRoundsLeft(this.roundsLeft);
        result.setGameSession(this.session);

        return result;
    }

    /**
     * translates the Values to the ones of the given source
     *
     * @param source the Buff which values should be used
     */
    public void setSource(Buff source) {
        this.permanent = source.isPermanent();

        this.atk = source.getAtk();
        this.def = source.getDef();
        this.hp = source.getHp();
        this.range = source.getRange();
        this.movePoints = source.getMovePoints();
        this.roundsLeft = source.getRoundsLeft();

        this.appliesFor = source.appliesFor;
    }

    /**
     * @param unit  the Unit to check
     * @return whether the Buff applies for the given Unit or not
     */
    public boolean appliesForUnit(Unit unit){
        return (player == unit.getOwner() && (appliesFor.isEmpty() || appliesFor.contains(unit.getType())) && unit.getType() != UnitType.BASE);
    }

    /**
     * Getter und Setter
     */
    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }
    public boolean isPermanent() {
        return permanent;
    }

    public void setRoundsLeft(int rounds) {
        this.roundsLeft = rounds;
    }
    public int getRoundsLeft() {
        return roundsLeft;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }
    public int getAtk() {
        return atk;
    }

    public void setDef(int def) {
        this.def = def;
    }
    public int getDef() {
        return def;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getHp() {
        return hp;
    }

    public void setRange(int range) {
        this.range = range;
    }
    public int getRange() {
        return range;
    }

    public void setMovePoints(int movePoints) {
        this.movePoints = movePoints;
    }
    public int getMovePoints() {
        return movePoints;
    }

    public void setGameSession(GameSession session) { this.session = session; }
    public GameSession getGameSession() { return session; }

    public Research getSource(){
        return source;
    }

    public void setFirstTime(boolean firstTime){
        this.firstTime = firstTime;
    }

    public BuffInfo getBuffInfo(){
        return info;
    }

    public List<UnitType> getTargets() throws RemoteException {return appliesFor; }
}
