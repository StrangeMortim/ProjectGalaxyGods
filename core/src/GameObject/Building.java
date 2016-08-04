package GameObject;

import java.io.Serializable;

/**
 * Created by Fabi on 14.06.2016.
 */
public enum Building implements Serializable {
    BASE,LABOR, CASERNE, MINE, MARKET;



    public int getBuildTime() {
        switch (this)
        {
            case LABOR:
                return 0;
            case CASERNE:
                return 0;
            case MINE:
                return 8;
            default:
                return 0;
        }
    }


    public int[] getRessourceCost() {
        switch (this)
        {
            case LABOR:
                return new int[]{20,30,0,0};
            case CASERNE:
                return new int[]{40,10,0,0};
            case MINE:
                return new int[]{5,0,0,0};
            case MARKET:
                return new int[]{100,0,100,0};
            default:
                return new int[]{0,0,0,0};
        }
    }


    public int[] getInitialCost(){
        switch (this){
            case MINE:
                return new int[]{10,0,0,0};
            default:
                return new int[]{0,0,0,0};
        }
    }
}
