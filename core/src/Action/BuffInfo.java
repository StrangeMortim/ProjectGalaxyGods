package Action;

/**
 * Created by Fabi on 27.07.2016.
 */
public enum BuffInfo implements IBuffInfo {
    REDUCED_UNIT_COST, SHIELD, HEAL, EMPOWER_SHIELD, HORNET_STYLE, RANGED_STYLE, NONE;

    @Override
    public int[] getBuffCost(){
        switch (this){
            case REDUCED_UNIT_COST:
                return new int[]{0,0,30,50};
            case SHIELD:
                return new int[]{0,0,0,30};
            case HEAL:
                return new int[]{0,0,0,50};
            case EMPOWER_SHIELD:
                return new int[]{0,0,30,10};
            case NONE:
            default:
                return new int[4];
        }
    }

    @Override
    public int getRounds(){
        switch (this){
            case REDUCED_UNIT_COST:
                return 20;
            case SHIELD:
                return 2;
            case EMPOWER_SHIELD:
                return 2;
            case HORNET_STYLE:
                return 1;
            case RANGED_STYLE:
                return 1;
            case NONE:
            default:
                return 0;
        }
    }

    @Override
    public int getPower(){
        switch (this){
            case SHIELD:
                return 5;
            case HEAL:
                return 50;
            case EMPOWER_SHIELD:
                return 5;
            case NONE:
            default:
                return 0;
        }
    }

    @Override
    public int getAtk(){
        switch (this){
            case HORNET_STYLE:
                return 5;
            case NONE:
            default:
                return 0;
        }

    }

    @Override
    public int getDef(){
        switch (this){
            case NONE:
            default:
                return 0;
        }
    }

    @Override
    public int getHp(){
        switch (this){
            case NONE:
            default:
                return 0;
        }
    }

    @Override
    public int getRange(){
        switch (this){
            case RANGED_STYLE:
                return 2;
            case NONE:
            default:
                return 0;
        }
    }

    @Override
    public int getMovepoints(){
        switch (this){
            case NONE:
            default:
                return 0;
        }
    }

    @Override
    public boolean isPermanent(){
        switch (this){
            case NONE:
            default:
                return false;

        }
    }
}
