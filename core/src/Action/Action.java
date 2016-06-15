package Action;

import GameObject.Unit;
import Player.Player;

import java.io.Serializable;

public abstract class Action implements IAction,Serializable {

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
    public boolean execute() {
        return true;
    }

    /**
     * Getter/setter
     */
    @Override
    public Player getPlayer(){
        return player;
    }

    @Override
    public void setParent(ActionProcessor processor){
        this.parent = processor;
    }
}
