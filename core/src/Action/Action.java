package Action;

import GameObject.Unit;
import Player.Player;

public abstract class Action implements IAction{

    private Unit origin;
    private Unit target;
    private String iconName = "";
    private Player player;
    private ActionProcessor parent;

    public Action(Unit origin, Unit target, Player player, ActionProcessor processor){
        if((origin == null && player == null) || processor == null)
            throw new IllegalArgumentException("Processor must not be null and either origin or player or both must be set");

        this.origin = origin;
        this.target = target;
        this.player = player;
        this.parent = processor;
    }

    /**
     * Fuehrt die Action aus, die jeweiligen Implementationen bestimmen den Inhalt dieser Methode
     */
    @Override
    public void execute() {

    }
}
