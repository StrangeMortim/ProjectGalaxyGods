package Action;

import GameObject.*;
import Player.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Buff extends Action implements IBuff{

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

    public Buff(Unit origin, Unit target, Player player, BuffInfo info) {
        super(origin, target, player);

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

    @Override
    public Buff getPersonalCopy(Unit u){
        Buff result = new Buff(u, null, this.player,BuffInfo.NONE);
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
    @Override
    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    @Override
    public boolean isPermanent() {
        return permanent;
    }

    @Override
    public void setRoundsLeft(int rounds) {
        this.roundsLeft = rounds;
    }

    @Override
    public int getRoundsLeft() {
        return roundsLeft;
    }

    @Override
    public void setAtk(int atk) {
        this.atk = atk;
    }

    @Override
    public int getAtk() {
        return atk;
    }

    @Override
    public void setDef(int def) {
        this.def = def;
    }

    @Override
    public int getDef() {
        return def;
    }

    @Override
    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public void setMovePoints(int movePoints) {
        this.movePoints = movePoints;
    }

    @Override
    public int getMovePoints() {
        return movePoints;
    }

    @Override
    public void setGameSession(GameSession session) {
        this.buffParent = session;
    }

    @Override
    public GameSession getGameSession() {
        return buffParent;
    }

    //sets all the values according to the given research
    @Override
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

    @Override
    public Research getSource(){
        return source;
    }

    //checks if this buff applies for a unit
    @Override
    public boolean appliesForUnit(Unit unit){
        return (player == unit.getOwner() && (appliesFor.isEmpty() || appliesFor.contains(unit.getType())) && unit.getType() != UnitType.BASE);
    }

    @Override
    public void setFirstTime(boolean firstTime){
        this.firstTime = firstTime;
    }

    @Override
    public BuffInfo getBuffInfo(){
        return info;
    }

    @Override
    public List<UnitType> getTargets() throws RemoteException {
        return appliesFor;
    }
}
