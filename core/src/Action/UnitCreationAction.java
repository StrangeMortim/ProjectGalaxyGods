package Action;

import GameObject.Base;
import GameObject.Unit;
import GameObject.UnitType;
import Player.Player;

/**
 * Created by Fabi on 14.06.2016.
 */
public class UnitCreationAction extends Action {

    private UnitType type;

    public UnitCreationAction(Base origin, Unit target, Player player) {
        super(origin, target, player);
    }

    @Override
    public void execute(){
        ((Base) origin).getRecruiting().put(new Unit(type, origin.getOwner()), type.getRecruitingTime());
    }


    public void setType(UnitType type){
        this.type = type;
    }


}
