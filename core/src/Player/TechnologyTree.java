package Player;


import GameObject.GameSession;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * This class realises the TechnologyTree for a player
 * player can advance on this tree to get resource boni and other stuff(the ability to build bases)
 * the actually advance happens in TreeElement enum
 * @author Fabi
 */
public class TechnologyTree implements Serializable {

    /**
     * The steel Path
     */
    private boolean[] steel = new boolean[5];

    /**
     * The magic Path
     */
    private boolean[] magic = new boolean[5];

    /**
     * The culture Path
     */
    private boolean[] culture = new boolean[5];

    /**
     * if the steel Path has reached it's end
     */
    private boolean steelFull = false;

    /**
     * if the magic Path has reached it's end
     */
    private boolean magicFull = false;

    /**
     * if the culture Path has reached it's end
     */
    private boolean cultureFull = false;

    /**
     * The session this Object belongs to and where it is registered
     */
    private GameSession session;

    /**
     * The global ID of the Object
     */
    private int iD;

    public TechnologyTree(GameSession session){
        if(session == null)
            throw new IllegalArgumentException("Session ist null in TechTree");

        this.session = session;

            iD = session.registerObject(this);

    }


    public void setSteel(boolean[] achieved) {
        this.steel = achieved;
    }
    public boolean[] getSteel() {
        return steel;
    }

    public void setMagic(boolean[] achieved) {
        this.magic = achieved;
    }
    public boolean[] getMagic() {
        return magic;
    }

    public void setCulture(boolean[] achieved) {
        this.culture = achieved;
    }
    public boolean[] getCulture() {
        return culture;
    }

    public boolean isSteelFull()  {
        return steelFull;
    }
    public void setSteelFull(boolean full)  {
        steelFull = full;
    }

    public boolean isMagicFull()  {
        return magicFull;
    }
    public void setMagicFull(boolean full) {
        magicFull = full;
    }

    public boolean isCultureFull() {
        return cultureFull;
    }
    public void setCultureFull(boolean full) {
        cultureFull = full;
    }
}
