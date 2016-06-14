package Action;

import GameObject.Unit;
import Player.Player;

public abstract class Action implements IAction{

    protected Unit origin;
    protected Unit target;
    protected String iconName = "";
    protected Player player;
    protected ActionProcessor parent;                 //set from the ActionProcessor-perspective


    public Action(Unit origin, Unit target, Player player){
        if(origin == null && player == null)
            throw new IllegalArgumentException("Processor must not be null and either origin or player or both must be set");

        this.origin = origin;
        this.target = target;
        this.player = player;
    }

    /**
     * Fuehrt die Action aus, die jeweiligen Implementationen bestimmen den Inhalt dieser Methode
     */
    @Override
    public void execute() {

    }
}
