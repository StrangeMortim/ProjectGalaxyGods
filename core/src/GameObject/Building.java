package GameObject;

/**
 * Created by Fabi on 14.06.2016.
 */
public enum Building implements IBuilding{
    LABOR, CASERNE;


    @Override
    public int getBuildTime() {
        return 0;
    }

    @Override
    public int[] getRessourceCost() {
        return new int[4];
    }
}
