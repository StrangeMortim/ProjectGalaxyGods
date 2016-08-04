package Player;


import GameObject.GameSession;

import java.io.Serializable;
import java.rmi.RemoteException;

public class TechnologyTree implements Serializable {

  //  private static final long serialVersionUID = -481325218194852515L;
    private boolean[] steel = new boolean[5];
    private boolean[] magic = new boolean[5];
    private boolean[] culture = new boolean[5];
    private boolean steelFull = false;
    private boolean magicFull = false;
    private boolean cultureFull = false;
    private GameSession session;
    private int iD;

    public TechnologyTree(GameSession session){
        if(session == null)
            throw new IllegalArgumentException("Session ist null in TechTree");

        this.session = session;
        try {
            iD = session.registerObject(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
