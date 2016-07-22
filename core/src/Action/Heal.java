package Action;

import GameObject.Constants;
import GameObject.Unit;
import Player.Player;

/**
 * Created by benja_000 on 21.07.2016.
 */
public class Heal extends Action{

    public Heal(Unit origin, Unit target, Player player) {
        super(origin, target, player);
        if(origin == null||player == null||target==null)return;
        this.origin = origin;
        this.target = target;
        this.player = player;
    }

    /**
     * Restores the HP of the target.
     * @return true if it's possible
     */
    @Override
    public boolean execute() {
  if(target.getCurrentHp()==target.getMaxHp()||target.getOwner()!=player||player.getRessources()[Constants.MANA]<30){return false;}
         target.setCurrentHp(target.getMaxHp());
        player.getRessources()[Constants.MANA]-=30;
        return true;
    }
}
