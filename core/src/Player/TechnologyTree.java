package Player;


import java.io.Serializable;

public class TechnologyTree implements ITechnologyTree,Serializable {

    private static final long serialVersionUID = -481325218194852515L;
    private boolean[] steel = new boolean[5];
    private boolean[] magic = new boolean[5];
    private boolean[] culture = new boolean[5];

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
}
