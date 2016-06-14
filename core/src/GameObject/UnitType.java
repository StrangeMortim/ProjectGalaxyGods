package GameObject;


import java.io.Serializable;

public enum UnitType implements IUnitType,Serializable {
    /*TODO Add Types*/
    ;

    /**
     * Ermöglicht Zugriff auf die verschiedenen UnitTypes ueber einen String
     *
     * @param name der Name des gesuchten Typs
     * @return der zugehoerige Typ
     */
    public UnitType getTypeByName(String name) {
        /*TODO implement*/
        return null;
    }

    /**
     * Getter fuer die jeweiligen werte der spezifischen einheit
     */
    @Override
    public int getMaxHp() {
        return 0;
    }

    @Override
    public int getAtk() {
        return 0;
    }

    @Override
    public int getDef() {
        return 0;
    }

    @Override
    public int getMovePoints() {
        return 0;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public String getSpriteName() {
        return null;
    }

    @Override
    public int getRecruitingTime(){ return 0;}

    @Override
    public int[] getRessourceCost() {
        return new int[4];
    }
}
