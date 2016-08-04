package Action;

import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Hero;
import GameObject.Unit;
import Player.Player;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by benja_000 on 22.07.2016.
 */
public class Shield extends Buff {


    public Shield(Hero origin, Player player, GameSession session) {
        super(origin, player, BuffInfo.SHIELD, session);
        def = BuffInfo.SHIELD.getPower();
        roundsLeft = BuffInfo.SHIELD.getRounds();//TODO enum benutzen
    }

    @Override
    public boolean execute() {
        for(int i = Constants.WOOD; i<=Constants.MANA; ++i)
            if(player.getRessources()[i] < (BuffInfo.SHIELD.getBuffCost()[i]-player.getRessourceBoni()[i]))
                return false;

        for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
            player.getRessources()[i] -= (BuffInfo.SHIELD.getBuffCost()[i] - player.getRessourceBoni()[i]);

        Buff bu= new Buff(origin,player,BuffInfo.SHIELD,session);
        bu.setDef(this.def);
        bu.setRoundsLeft(this.roundsLeft);

        try {
            origin.getField().getMap().getSession().addSingleBuff(bu);
            bu.execute();
        }catch (Exception e){return false;}

        List<Unit> additionalTargets = target.getField().getNearUnits();
        for(Unit u :additionalTargets){
            if(u.getOwner()==player) {
                bu= new Buff(u,player,BuffInfo.SHIELD,session);
                bu.setDef(this.def);
                bu.setRoundsLeft(this.roundsLeft);
                try {
                    origin.getField().getMap().getSession().addSingleBuff(bu);
                    bu.execute();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

            return true;
    }

    @Override
    public boolean appliesForUnit(Unit unit){
        return unit instanceof Hero && unit.getOwner() == player;
    }
}
