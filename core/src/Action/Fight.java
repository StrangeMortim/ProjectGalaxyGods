package Action;

import GameObject.Unit;
import Player.Player;

import java.util.Random;


public class Fight extends Action {

    public Fight(Unit origin, Unit target, Player player, ActionProcessor processor) {
        super(origin, target, player);
    }

    @Override
    public boolean execute(){
        try{
            if(origin.getCurrentHp() == 0 || target.getCurrentHp() == 0)
                return true;

            Random r = new Random();

            //Damage the target
            int dmg = origin.getAtk()-target.getDef();
            if(dmg < 0)
                dmg = 0;

            target.setCurrentHp(target.getCurrentHp()-dmg - (r.nextInt(origin.getAtk()/2)+1));

            //Damage the origin
            dmg = target.getAtk()-origin.getDef();
            if(dmg < 0)
                dmg = 0;

            origin.setCurrentHp(origin.getCurrentHp()-dmg - (r.nextInt(target.getAtk()/2)+1));

            if(target.getCurrentHp() <= 0) {
                target.getField().setCurrent(null);
                parent.getSession().removeUnit(target);
            }

            if(origin.getCurrentHp() <= 0){
                target.getField().setCurrent(null);
                parent.getSession().removeUnit(origin);
            }

            return true;
        /* TODO check */
        } catch (NullPointerException e){
            return true;
        }
    }
}
