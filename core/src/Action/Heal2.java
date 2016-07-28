package Action;

import GameObject.Constants;
import GameObject.Unit;
import Player.Player;

/**
 * Created by benja_000 on 21.07.2016.
 */
public class Heal2 extends Action{

    Unit[]additionalTargets;

    public Heal2(Unit origin, Unit target, Player player, Unit[]additionalTargets) {
        super(origin, target, player);
        if(origin == null||player == null||target==null)return;
        this.origin = origin;
        this.target = target;
        this.player = player;
        this.additionalTargets=additionalTargets;
    }

    /**
     * Restores the HP of the target.
     * @return true if it's possible
     */
    @Override
    public boolean execute() {
  if(target.getCurrentHp()==target.getMaxHp()||target.getOwner()!=player||player.getRessources()[Constants.MANA]< BuffInfo.HEAL.getBuffCost()[Constants.MANA]){return false;}
        // target.setCurrentHp(target.getCurrentHp() + BuffInfo.HEAL.getPower()); //wird schon bei Heal1 gemacht
       if(additionalTargets!=null)
        for(Unit u :additionalTargets){
            if(u.getOwner()==player) {
                 u.setCurrentHp(u.getCurrentHp() + BuffInfo.HEAL.getPower());
            }
        }
        player.getRessources()[Constants.MANA]-= BuffInfo.HEAL.getBuffCost()[Constants.MANA];
        return true;
    }
}
