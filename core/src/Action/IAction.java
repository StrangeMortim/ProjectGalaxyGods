package Action;

import Player.Player;

/**
 * Created by Fabi on 11.06.2016.
 */
public interface IAction {

    /**
     * Fuehrt die Action aus, die jeweiligen Implementationen bestimmen den Inhalt dieser Methode
     */
    public boolean execute();

    /**
     * Getter/setter
     */
    public Player getPlayer();
}
