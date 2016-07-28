package Action;

import GameObject.Hero;
import GameObject.Unit;
import Player.Player;

/**
 * Created by Fabi on 28.07.2016.
 */
public class EmpowerShield extends Buff {
    public EmpowerShield(Hero origin, Unit target, Player player) {
        super(origin, target, player, BuffInfo.NONE);
        roundsLeft = BuffInfo.EMPOWER_SHIELD.getRounds();
    }

    @Override
    public boolean execute(){
        try {
            Action tmp = ((Hero)origin).getRightHand() instanceof Shield ? ((Hero)origin).getRightHand() : ((Hero)origin).getLeftHand();


            if(firstTime){
                ((Shield)tmp).setDef(BuffInfo.SHIELD.getPower() + BuffInfo.EMPOWER_SHIELD.getPower());
            }

            roundsLeft--;

            if(roundsLeft<=0){
                ((Shield)tmp).setDef(BuffInfo.SHIELD.getPower());
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
