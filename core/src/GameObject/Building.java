package GameObject;

import java.io.Serializable;

/**
 * Created by Fabi on 14.06.2016.
 */
public enum Building implements IBuilding,Serializable {
    LABOR, CASERNE;


    @Override
    public int getBuildTime() {
        switch (this)
        {
            case LABOR:
                return 3;
            case CASERNE:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public int[] getRessourceCost() {
        switch (this)
        {
            case LABOR:
                return new int[]{20,30,0,0};
            case CASERNE:
                return new int[]{40,10,0,0};
            default:
                return new int[]{0,0,0,0};
        }
    }
}
