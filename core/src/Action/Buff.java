package Action;

import GameObject.*;
import Player.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Buff extends Action{

    protected GameSession buffParent = null;      //the game session where this buff belongs to
    protected Research source;                   //the research which this buff realizes
    protected List<UnitType> appliesFor = new ArrayList<>(); //contains all unit types which get a bonus, null equals 'ALL'
    protected boolean permanent;
    protected int roundsLeft;
    protected int atk;
    protected int def;
    protected int hp;
    protected int range;
    protected int movePoints;
    protected boolean firstTime = true;
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
        if(firstTime){//TODO: check Null

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
        /*TODO check*/
    }


    public Buff getPersonalCopy(Unit u){
        Buff result = new Buff(u, this.player,BuffInfo.NONE, session);
        result.setSource(this);
        result.setRoundsLeft(this.roundsLeft);
        result.setGameSession(this.buffParent);

        return result;
    }

    /**
     * Getter und Setter
     *
     * @param permanent
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


    public void setGameSession(GameSession session) {
        this.buffParent = session;
    }


    public GameSession getGameSession() {
        return buffParent;
    }

    //sets all the values according to the given research

    public void setSource(Buff source) {
            this.permanent = source.isPermanent();

            this.atk = source.getAtk();
            this.def = source.getDef();
            this.hp = source.getHp();
            this.range = source.getRange();
            this.movePoints = source.getMovePoints();
            this.roundsLeft = source.getRoundsLeft();

            this.appliesFor = source.appliesFor;
        /*TODO absolut und relativ werte verarbeiten?*/
    }


    public Research getSource(){
        return source;
    }

    //checks if this buff applies for a unit

    public boolean appliesForUnit(Unit unit){
        return (player == unit.getOwner() && (appliesFor.isEmpty() || appliesFor.contains(unit.getType())) && unit.getType() != UnitType.BASE);
    }


    public void setFirstTime(boolean firstTime){
        this.firstTime = firstTime;
    }


    public BuffInfo getBuffInfo(){
        return info;
    }


    public List<UnitType> getTargets() throws RemoteException {
        return appliesFor;
    }
}
