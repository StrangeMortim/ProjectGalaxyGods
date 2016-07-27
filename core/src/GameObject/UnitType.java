package GameObject;


import java.io.Serializable;

public enum UnitType implements IUnitType,Serializable {
    BASE, SPEARFIGHTER, ARCHER, SWORDFIGHTER, WORKER,HERO/*TODO Add Types*/
    ;

    /**
     * Ermöglicht Zugriff auf die verschiedenen UnitTypes ueber einen String
     *
     * @param name der Name des gesuchten Typs
     * @return der zugehoerige Typ
     */
    public UnitType getTypeByName(String name) {
        switch (name.toLowerCase())
        {
            case "base":
                return BASE;
            case "spearfighter":
                return SPEARFIGHTER;
            case "archer":
                return ARCHER;
            case "swordfighter":
                return SWORDFIGHTER;
            case "hero":
                return HERO;
            default:
                System.out.println("getTypeByName got non existent unitName: " + name.toLowerCase());
        }
        return null;
    }

    /**
     * Getter fuer die jeweiligen werte der spezifischen einheit
     */
    @Override
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
            default:
                return 0;
        }
    }

    @Override
    public int getAtk() {
        switch (this){
            case BASE:
                return 10;
            case SPEARFIGHTER:
                return 15;
            case ARCHER:
                return 20;
            case SWORDFIGHTER:
                return 25;
            case HERO:
                return 40;
            default:
                return 0;
        }
    }

    @Override
    public int getDef() {
        switch (this){
            case BASE:
                return 15;
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

    @Override
    public int getMovePoints() {
        switch (this){
            case BASE:
                return 0;
            case SPEARFIGHTER:
                return 8;
            case ARCHER:
                return 5;
            case SWORDFIGHTER:
                return 3;
            case HERO:
                return 5;
            default:
                return 0;
        }
    }

    @Override
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
            default:
                return 0;
        }
    }

    @Override
    public String getSpriteName() {
        switch (this){
            case SPEARFIGHTER:
                return SpriteNames.SPEARFIGHTER.getSpriteName();
            case ARCHER:
                return SpriteNames.ARCHER.getSpriteName();
            case SWORDFIGHTER:
                return SpriteNames.SWORDFIGHTER.getSpriteName();
            case WORKER:
                return SpriteNames.WORKER.getSpriteName();
            case HERO:
                return SpriteNames.HERO.getSpriteName();
            default:
                return "";
        }
    }

    @Override
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

    @Override
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
            default:
                return new int[]{0,0,0,0};
        }
    }

    @Override
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
