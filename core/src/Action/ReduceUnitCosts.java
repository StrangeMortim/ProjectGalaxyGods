package Action;

import GameObject.Constants;
import GameObject.Unit;
import Player.Player;

import java.rmi.RemoteException;

/**
 * Created by Fabi on 27.07.2016.
 */
public class ReduceUnitCosts extends Buff {
    public ReduceUnitCosts(Unit origin, Unit target, Player player) {
        super(origin, target, player, BuffInfo.NONE);
        roundsLeft = BuffInfo.REDUCED_UNIT_COST.getRounds();
        System.out.println(player.getAccount().getName());
    }

    @Override
    public boolean execute(){
        for(int i = Constants.WOOD; i<=Constants.MANA; ++i)
            if(player.getRessources()[i] < BuffInfo.REDUCED_UNIT_COST.getBuffCost()[i])
                return true;

        for(int i=Constants.WOOD; i<Constants.MANA; ++i)
            player.getRessources()[i] -= BuffInfo.REDUCED_UNIT_COST.getBuffCost()[i];

        if(firstTime)
            try {
                player.setReducedUnitCost(true);
                firstTime = false;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        else
        roundsLeft--;

        if(roundsLeft<=0)
            try {
                player.setReducedUnitCost(false);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        return false;
    }
}
