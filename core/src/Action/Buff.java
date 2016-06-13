package Action;

import GameObject.Unit;
import Player.Player;

public class Buff extends Action implements IBuff{

    private boolean permanent;
    private int roundsLeft;
    private int atk;
    private int def;
    private int hp;
    private int range;
    private int movePoints;


    public Buff(Unit origin, Unit target, Player player, ActionProcessor processor) {
        super(origin, target, player, processor);
    }

    @Override
    public void execute(){
        /*TODO*/
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
    public Boolean getPermanent() {
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
}
