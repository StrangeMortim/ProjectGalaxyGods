package Action;

import GameObject.Unit;
import Player.Player;

import java.rmi.Remote;

/**
 * Created by Fabi on 11.06.2016.
 */
public interface IAction extends Remote {

    /**
     * Fuehrt die Action aus, die jeweiligen Implementationen bestimmen den Inhalt dieser Methode
     */
    public boolean execute();

    /**
     * Getter/setter
     */
    public Player getPlayer();

    public void setOrigin(Unit origin);

    public Unit getOrigin();

    public void setParent(ActionProcessor processor);
}
