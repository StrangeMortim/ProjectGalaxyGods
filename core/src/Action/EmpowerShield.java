package Action;

import GameObject.GameSession;
import GameObject.Hero;
import GameObject.Unit;
import Player.Player;

/**
 * Improves the "Macht des Drachen" Skill of the given Hero
 *
 * @author Fabi
 */
public class EmpowerShield extends Buff {
    public EmpowerShield(Hero origin, Player player, GameSession session) {
        super(origin, player, BuffInfo.EMPOWER_SHIELD, session);
        roundsLeft = BuffInfo.EMPOWER_SHIELD.getRounds();
    }

    /**
     * Acts like any other buff, but instead of unit values, it modifies the Skill-Values
     *
     * @return  whether the Buff is still needed or not(false -> can be deleted)
     */
    @Override
    public boolean execute(){
        try {
            //get the right action
            Action tmp = ((Hero)origin).getRightHand() instanceof Shield ? ((Hero)origin).getRightHand() : ((Hero)origin).getLeftHand();


            if(firstTime){
                ((Shield)tmp).setDef(BuffInfo.SHIELD.getPower() + BuffInfo.EMPOWER_SHIELD.getPower());
                firstTime = false;
            }

            roundsLeft--;

            if(roundsLeft<=0){
                ((Shield)tmp).setDef(BuffInfo.SHIELD.getPower());
                return true;
            }
            //for safety not sure if still needed
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @see Buff
     */
    @Override
    public boolean appliesForUnit(Unit unit){
        return unit instanceof Hero && unit.getOwner() == player;
    }
}
