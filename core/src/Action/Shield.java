package Action;

import GameObject.Hero;
import GameObject.Unit;
import GameObject.UnitType;
import Player.Player;

/**
 * Created by benja_000 on 22.07.2016.
 */
public class Shield extends Action {


    public Shield(Unit origin, Unit target, Player player) {
        super(origin, target, player);
    }

    @Override
    public boolean execute() {
        Buff bu= new Buff(origin,target,player);
        bu.setDef(5);
        bu.setRoundsLeft(2);
        try {
            origin.getField().getMap().getSession().getBuffs().add(bu);
            bu.execute();
        }catch (Exception e){return false;}
            return true;
    }
}
