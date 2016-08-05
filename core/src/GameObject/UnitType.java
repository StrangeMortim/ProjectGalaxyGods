package GameObject;


import java.io.Serializable;

/**
 * An enum for all Type of Unis there are
 * with getter method for the different attributes, should be self-explaining
 * @author Fabi
 */
public enum UnitType implements Serializable {
    BASE, SPEARFIGHTER, ARCHER, SWORDFIGHTER, WORKER,HERO
    ;

    /**
     * Getter for the attributes of the different types
     */
    public int getMaxHp() {
        switch (this){
            case BASE:
                return 1000;
            case SPEARFIGHTER:
                return 30;
            case ARCHER:
                return 15;
            case SWORDFIGHTER:
                return 50;
            case HERO:
                return 200;
            case WORKER:
                return 10;
            default:
                return 0;
        }
    }

    public int getAtk() {
        switch (this){
            case BASE:
                return 0;
            case SPEARFIGHTER:
                return 15;
            case ARCHER:
                return 20;
            case SWORDFIGHTER:
                return 25;
            case HERO:
                return 40;
            case WORKER:
                return 5;
            default:
                return 0;
        }
    }

    public int getDef() {
        switch (this){
            case BASE:
                return 0;
            case SPEARFIGHTER:
                return 10;
            case ARCHER:
                return 5;
            case SWORDFIGHTER:
                return 10;
            case HERO:
                return 10;
            default:
                return 0;
        }
    }

    public int getMovePoints() {
        switch (this){
            case BASE:
                return 0;
            case SPEARFIGHTER:
                return 6;
            case ARCHER:
                return 3;
            case SWORDFIGHTER:
                return 5;
            case HERO:
                return 5;
            case WORKER:
                return 4;
            default:
                return 0;
        }
    }

    public int getRange() {
        switch (this){
            case BASE:
                return 1;
            case SPEARFIGHTER:
                return 2;
            case ARCHER:
                return 3;
            case SWORDFIGHTER:
                return 1;
            case HERO:
                return 1;
            case WORKER:
                return 1;
            default:
                return 0;
        }
    }

    public int getSpriteIndex() {
        switch (this){
            case SPEARFIGHTER:
                return SpriteNames.SPEARFIGHTER.getSpriteIndex();
            case ARCHER:
                return SpriteNames.ARCHER.getSpriteIndex();
            case SWORDFIGHTER:
                return SpriteNames.SWORDFIGHTER.getSpriteIndex();
            case WORKER:
                return SpriteNames.WORKER.getSpriteIndex();
            case HERO:
                return SpriteNames.HERO.getSpriteIndex();
            case BASE:
                return SpriteNames.BASE_UP_LEFT.getSpriteIndex();
            default:
                return 0;
        }
    }

    public int getRecruitingTime(){
        switch (this){
            case BASE:
                return 4;
            case SPEARFIGHTER:
                return 2;
            case ARCHER:
                return 0;
            case SWORDFIGHTER:
                return 0;
            default:
                return 0;
        }
    }

    public int[] getRessourceCost() {

        switch (this){
            case BASE:
                return new int[]{100,100,50,0};
            case SPEARFIGHTER:
                return new int[]{15,5,20,0};
            case ARCHER:
                return new int[]{20,0,10,0};
            case SWORDFIGHTER:
                return new int[]{5,15,30,0};
            case WORKER:
                return new int[]{5,5,0,0};
            default:
                return new int[]{0,0,0,0};
        }
    }

    public int[] getReducedCost() {

        switch (this){
            case BASE:
                return new int[]{80,80,30,0};
            case SPEARFIGHTER:
                return new int[]{10,0,15,0};
            case ARCHER:
                return new int[]{10,0,5,0};
            case SWORDFIGHTER:
                return new int[]{0,5,15,0};
            default:
                return new int[]{0,0,0,0};
        }
    }
}
