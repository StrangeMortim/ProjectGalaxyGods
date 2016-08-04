package GameObject;


import Action.BuffInfo;
import Player.Player;

import java.io.Serializable;
import java.util.List;

public enum Research implements Serializable {
    RESEARCH_ARCHER, RESEARCH_SPEARFIGHTER;
    /*TODO*/


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


    public int[] getValues() {
        return new int[6];
    }


    public List<UnitType> getTargets() {
        return null;
    }


    public int getResearchTime() {
        switch (this){
            case RESEARCH_ARCHER:
                return 0;
            case RESEARCH_SPEARFIGHTER:
                return 0;
            default:
                return 0;
        }
    }


    public boolean isPermanet() {
        return false;
    }


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


    public BuffInfo getInfo(){
        switch (this){
            default:
                return BuffInfo.NONE;
        }
    }
}
