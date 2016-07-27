package Action;

/**
 * Created by Fabi on 27.07.2016.
 */
public enum SpecialBuff implements ISpecialBuff{
    REDUCED_UNIT_COST;

    @Override
    public int[] getBuffCost(){
        switch (this){
            case REDUCED_UNIT_COST:
                return new int[]{0,0,30,50};
            default:
                return new int[4];
        }
    }

    @Override
    public int getRounds(){
        switch (this){
            case REDUCED_UNIT_COST:
                return 20;
            default:
                return 0;
        }
    }
}
