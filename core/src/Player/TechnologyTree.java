package Player;


import java.io.Serializable;
import java.rmi.RemoteException;

public class TechnologyTree implements ITechnologyTree,Serializable {

    private boolean[] steel = new boolean[5];
    private boolean[] magic = new boolean[5];
    private boolean[] culture = new boolean[5];
    private boolean steelFull = false;
    private boolean magicFull = false;
    private boolean cultureFull = false;

    public TechnologyTree(){}

    @Override
    public void setSteel(boolean[] achieved) {
        this.steel = achieved;
    }

    @Override
    public boolean[] getSteel() {
        return steel;
    }

    @Override
    public void setMagic(boolean[] achieved) {
        this.magic = achieved;
    }

    @Override
    public boolean[] getMagic() {
        return magic;
    }

    @Override
    public void setCulture(boolean[] achieved) {
        this.culture = achieved;
    }

    @Override
    public boolean[] getCulture() {
        return culture;
    }

    @Override
    public boolean isSteelFull() throws RemoteException {
        return steelFull;
    }

    @Override
    public void setSteelFull(boolean full) throws RemoteException {
        steelFull = full;
    }

    @Override
    public boolean isMagicFull() throws RemoteException {
        return magicFull;
    }

    @Override
    public void setMagicFull(boolean full) throws RemoteException {
        magicFull = full;
    }

    @Override
    public boolean isCultureFull() throws RemoteException {
        return cultureFull;
    }

    @Override
    public void setCultureFull(boolean full) throws RemoteException {
        cultureFull = full;
    }
}
