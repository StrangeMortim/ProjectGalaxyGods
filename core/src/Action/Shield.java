package Action;

import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Hero;
import GameObject.Unit;
import Player.Player;

import java.rmi.RemoteException;
import java.util.List;

/**
 * One of the two hero skills
 * the name is deprecated, ingame it is now called "Macht des Drachen"(Might of the Dragon)
 *
 * @author Benjamin
 */
public class Shield extends Buff {


    public Shield(Hero origin, Player player, GameSession session) {
        super(origin, player, BuffInfo.SHIELD, session);
        def = BuffInfo.SHIELD.getPower();
        roundsLeft = BuffInfo.SHIELD.getRounds();//TODO enum benutzen
    }

    /**
     * Case 1: The Hero is alone or friendly units are nearby,
     *          then this skill casts a shield that just works like any other buff
     *          (for that a new Buff is create)
     * Case 2: Enemy Units are nearby, in this case instead of the Shield, the Hero casts
     *          a powerful spell, damaging all enemy Units around him
     * @return whether the Action was a success or not
     */
    @Override
    public boolean execute() {
        for(int i = Constants.WOOD; i<=Constants.MANA; ++i)
            if(player.getRessources()[i] < (BuffInfo.SHIELD.getBuffCost()[i]-player.getRessourceBoni()[i]))
                return false;

        for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
            player.getRessources()[i] -= (BuffInfo.SHIELD.getBuffCost()[i] - player.getRessourceBoni()[i]);



        List<Unit> additionalTargets = origin.getField().getNearUnits();
        if(!additionalTargets.isEmpty()) {
            for (Unit u : additionalTargets) {
                if (u.getOwner() != player && u.getOwner().getTeam() != player.getTeam()) {
                    u.setCurrentHp(u.getCurrentHp() - BuffInfo.DRAGONFIST.getPower());
                    if (u.getCurrentHp() <= 0)
                        session.removeUnit(u);
                }
            }
            //info for graphics
            ((Hero)origin).setCalledTheDragon(true);
        } else {
            //create the Buff
            Buff bu = new Buff(origin, player, BuffInfo.SHIELD, session);
            bu.setDef(this.def);
            bu.setRoundsLeft(this.roundsLeft);

            try {
                origin.getField().getMap().getSession().addSingleBuff(bu);
                bu.execute();
            } catch (Exception e) {
                return false;
            }
        }
            return true;
    }

    /**
     * @see Buff
     */
    @Override
    public boolean appliesForUnit(Unit unit){
        return unit instanceof Hero && unit.getOwner() == player;
    }
}
