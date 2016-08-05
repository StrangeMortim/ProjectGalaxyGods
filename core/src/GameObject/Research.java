package GameObject;


import Action.BuffInfo;
import Player.Player;

import java.io.Serializable;
import java.util.List;

/**
 * Represents all researching that can be done and the corresponding values
 * @author Fabi
 */
public enum Research implements Serializable {
    RESEARCH_ARCHER, RESEARCH_SPEARFIGHTER;

    /**
     * @return The resource costs for the different enum values(for indeces check Constants enum)
     */
    public int[] getRessourceCost() {
        switch (this){
            case RESEARCH_ARCHER:
                return new int[]{30,0,30,0};
            case RESEARCH_SPEARFIGHTER:
                return new int[]{15,15,30,0};
            default:
                return new int[4];
        }
    }

    /**
     * @return how many turns it takes for the research to be finished
     */
    public int getResearchTime() {
        switch (this){
            case RESEARCH_ARCHER:
                return 0;
            case RESEARCH_SPEARFIGHTER:
                return 3;
            default:
                return 0;
        }
    }

    /**
     * starts the researching on the given base if possible
     * @param b the base where the value should be researched
     * @return whether the researched started correctly or failed
     */
    public boolean research(Base b){
        switch (this){
            case RESEARCH_ARCHER:
            case RESEARCH_SPEARFIGHTER:
                for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
                    if(b.getOwner().getRessources()[i] < this.getRessourceCost()[i])
                        return false;

                for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
                    b.getOwner().getRessources()[i] -= this.getRessourceCost()[i];

                if(this == RESEARCH_ARCHER)
                b.getAvaibleUnits().add(UnitType.ARCHER);
                else
                    b.getAvaibleUnits().add(UnitType.SPEARFIGHTER);

                return true;
            default:
                return true;
        }
    }

    /**
     * -currently useless-
     * determines which kind of buff the research gives
     * @return the BuffInfo for achieved Buff
     */
    public BuffInfo getInfo(){
        switch (this){
            default:
                return BuffInfo.NONE;
        }
    }
}
