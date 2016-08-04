package Action;

import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Unit;
import Player.Player;

import java.util.List;

/**
 * Created by benja_000 on 21.07.2016.
 */
public class Heal extends Action{

    public Heal(Unit origin, Unit target, Player player, GameSession session) {
        super(origin, target, player,session);
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
        //HEAL costs only mana
  if(target.getCurrentHp()==target.getMaxHp()||target.getOwner()!=player
          ||player.getRessources()[Constants.MANA]< (BuffInfo.HEAL.getBuffCost()[Constants.MANA]-player.getRessourceBoni()[Constants.MANA]))
  {return false;}


         target.setCurrentHp(target.getCurrentHp()+ BuffInfo.HEAL.getPower());
        player.getRessources()[Constants.MANA]-= (BuffInfo.HEAL.getBuffCost()[Constants.MANA]-player.getRessourceBoni()[Constants.MANA]);

        List<Unit> additionalTargets = target.getField().getNearUnits();
        for(Unit u :additionalTargets){
            if(u.getOwner()==player) {
                u.setCurrentHp(u.getCurrentHp() + BuffInfo.HEAL.getPower());
            }
        }
        return true;
    }
}
