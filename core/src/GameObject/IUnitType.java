package GameObject;


public interface IUnitType {

    /**
     * Ermöglicht Zugriff auf die verschiedenen UnitTypes ueber einen String
     * @param name der Name des gesuchten Typs
     * @return der zugehoerige Typ
     */
    public static UnitType getTypeByName(String name){
        /*TODO*/
        return null;
    };

    /**
     * Getter fuer die jeweiligen werte der spezifischen einheit
     */
    public int getMaxHp();
    public int getAtk();
    public int getDef();
    public int getMovePoints();
    public int getRange();
    public String getSpriteName();
}
