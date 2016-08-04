package Action;

import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Unit;
import Player.Player;

import java.rmi.RemoteException;

/**
 * Created by Fabi on 27.07.2016.
 */
public class ReduceUnitCosts extends Buff {
    public ReduceUnitCosts(Unit origin, Player player, GameSession session) {
        super(origin, player, BuffInfo.NONE, session);
        roundsLeft = BuffInfo.REDUCED_UNIT_COST.getRounds();
        System.out.println(player.getAccount().getName());
    }

    @Override
    public boolean execute(){
        for(int i = Constants.WOOD; i<=Constants.MANA; ++i)
            if(player.getRessources()[i] < (BuffInfo.REDUCED_UNIT_COST.getBuffCost()[i]-player.getRessourceBoni()[i]))
                return true;

        for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
            player.getRessources()[i] -= (BuffInfo.REDUCED_UNIT_COST.getBuffCost()[i] - player.getRessourceBoni()[i]);


        if(firstTime) {
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
