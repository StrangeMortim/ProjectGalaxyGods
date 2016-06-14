package GameObject;


import java.io.Serializable;
import java.util.List;

public enum Research implements IResearch,Serializable {
    ;
    /*TODO*/

    @Override
    public int[] getRessourceCost() {
        return new int[4];
    }

    @Override
    public int[] getValues() {
        return new int[6];
    }

    @Override
    public List<UnitType> getTargets() {
        return null;
    }

    @Override
    public int getResearchTime() {
        return 0;
    }

    @Override
    public boolean isPermanet() {
        return false;
    }
}
