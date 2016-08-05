package Action;

import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Unit;
import Player.Player;

import java.rmi.RemoteException;

/**
 * Special Buff that reduces Unitcosts for a specific amount of time
 *
 * @author Fabi
 */
public class ReduceUnitCosts extends Buff {
    public ReduceUnitCosts(Unit origin, Player player, GameSession session) {
        super(origin, player, BuffInfo.NONE, session);
        roundsLeft = BuffInfo.REDUCED_UNIT_COST.getRounds();
        System.out.println(player.getAccount().getName());
    }

    /**
     * If first time and enough resources, set the reducedUnitCost Value in Player true
     * else count down the rounds and if they reach 0 set the reducedUnitCost Value back to false
     * The specific Values for the reduced costs can be found in UnitType
     *
     * @return whether the Buff is still active or can be deleted(true if can be deleted)
     */
    @Override
    public boolean execute(){
        if(firstTime) {
            for(int i = Constants.WOOD; i<=Constants.MANA; ++i)
                if(player.getRessources()[i] < (BuffInfo.REDUCED_UNIT_COST.getBuffCost()[i]-player.getRessourceBoni()[i]))
                    return true;

            for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
                player.getRessources()[i] -= (BuffInfo.REDUCED_UNIT_COST.getBuffCost()[i] - player.getRessourceBoni()[i]);

            player.setReducedUnitCost(true);
            firstTime = false;

        }else
        roundsLeft--;

        if(roundsLeft<=0) {
            player.setReducedUnitCost(false);
            return true;
        }


        return false;
    }
}
