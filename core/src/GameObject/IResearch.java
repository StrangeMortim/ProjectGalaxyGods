package GameObject;


import java.util.List;

public interface IResearch {

    /**
     * Ermoeglicht Zugriffe ueber einen String
     * @param name der Name der gesuchten Forschung
     * @return die zugehoerige Forschung
     */
    public static Research getResearchByName(String name){
        /*TODO*/
        return null;
    };

    public int[] getRessourceCost();

    public int[] getValues();

    /**
     * @return  Entweder alle Unit für die die Werte gelten oder die Unit die durch diese Forschung freigeschaltet wird
     * letzteres gekennzeichnet durch nur 0 in getValues
     */
    public List<UnitType> getTargets();

    public int getResearchTime();

    public boolean isPermanet();
}
