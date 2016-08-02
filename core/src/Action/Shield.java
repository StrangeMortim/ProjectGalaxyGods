package Action;

import GameObject.Hero;
import GameObject.Unit;
import Player.Player;

/**
 * Created by benja_000 on 22.07.2016.
 */
public class Shield extends Buff {


    public Shield(Hero origin, Unit target, Player player) {
        super(origin, target, player, BuffInfo.SHIELD);
        def = BuffInfo.SHIELD.getPower();
        roundsLeft = BuffInfo.SHIELD.getRounds();//TODO enum benutzen
    }

    @Override
    public boolean execute() {
        Buff bu= new Buff(origin,target,player,BuffInfo.SHIELD);
        bu.setDef(this.def);
        bu.setRoundsLeft(this.roundsLeft);
        try {
            origin.getField().getMap().getSession().addSingleBuff(bu);
            bu.execute();
        }catch (Exception e){return false;}
            return true;
    }

    @Override
    public boolean appliesForUnit(Unit unit){
        return unit instanceof Hero && unit.getOwner() == player;
    }
}
