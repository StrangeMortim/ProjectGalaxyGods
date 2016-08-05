package Action;

import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Unit;
import Player.Player;

import java.util.List;

/**
 * Realises one of two hero skills,
 * as the name suggest it Heals stuff
 *
 * @author Benjamin
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
     * Specialcase: Friendly units are nearby, then heal them too(is visible ingame)
     *
     * @return whether execution was a success or not(e.g. not enough Mana or something)
     */
    @Override
    public boolean execute() {
        //HEAL costs only mana
  if(target.getCurrentHp()==target.getMaxHp()||target.getOwner()!=player
          ||player.getRessources()[Constants.MANA]< (BuffInfo.HEAL.getBuffCost()[Constants.MANA]-player.getRessourceBoni()[Constants.MANA]))
  {return false;}


         target.setCurrentHp(target.getCurrentHp()+ BuffInfo.HEAL.getPower());
        player.getRessources()[Constants.MANA]-= (BuffInfo.HEAL.getBuffCost()[Constants.MANA]-player.getRessourceBoni()[Constants.MANA]);

        //check special case
        List<Unit> additionalTargets = target.getField().getNearUnits();
        for(Unit u :additionalTargets){
            if(u.getOwner()==player) {
                u.setCurrentHp(u.getCurrentHp() + BuffInfo.HEAL.getPower());
            }
        }
        return true;
    }
}
