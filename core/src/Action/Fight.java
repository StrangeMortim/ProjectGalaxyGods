package Action;

import GameObject.Unit;
import Player.Player;


public class Fight extends Action {

    public Fight(Unit origin, Unit target, Player player, ActionProcessor processor) {
        super(origin, target, player, processor);
    }

    @Override
    public void execute(){
        /* TODO; */
    }
}
