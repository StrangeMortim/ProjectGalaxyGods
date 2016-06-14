package Player;


import java.io.Serializable;

public class TechnologyTree implements ITechnologyTree,Serializable {

    private Boolean[] steel = new Boolean[5];
    private Boolean[] magic = new Boolean[5];
    private Boolean[] culture = new Boolean[5];

    public TechnologyTree(){}

    @Override
    public void setSteel(Boolean[] achieved) {
        this.steel = achieved;
    }

    @Override
    public Boolean[] getSteel() {
        return steel;
    }

    @Override
    public void setMagic(Boolean[] achieved) {
        this.magic = achieved;
    }

    @Override
    public Boolean[] getMagic() {
        return magic;
    }

    @Override
    public void setCulture(Boolean[] achieved) {
        this.culture = achieved;
    }

    @Override
    public Boolean[] getCulture() {
        return culture;
    }
}
