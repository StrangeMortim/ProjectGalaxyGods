package GameObject;

import Action.Action;
import Player.Player;
import Action.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This class represents the player in the Game in Form of the special Hero-Unit
 *
 * @author Benjamin
 */
public class Hero extends Unit implements Serializable {

  //  private static final long serialVersionUID = -7787413125681105826L;
    private Action leftHand;
    private Action rightHand;
    private String name;
    private boolean calledTheDragon = false;

    public Hero(UnitType type, Player owner, String name, GameSession session) {
        super(type, owner, session);
            owner.setHero(this);


        if(name.equals(""))
            throw new IllegalArgumentException("You must name your hero");

        this.name = name;
        setMovePointsLeft(movePoints);
        setLeftHand(new Heal(this,this,getOwner(),session));
        setRightHand(new Shield(this,getOwner(),session));
    }

    /**
     * Getter und Setter
     *
     * @param leftHand
     */

    public void setLeftHand(Action leftHand) {
        this.leftHand = leftHand;
    }
    public Action getLeftHand() {
        return leftHand;
    }

    public void setRightHand(Action rightHand) {
        this.rightHand = rightHand;
    }
    public Action getRightHand() {
        return rightHand;
    }

    public void setName(String name) {
        if (name.equals(""))
            throw new IllegalArgumentException("Name is empty");

        this.name = name;
    }
    public String getName() {
        return name;
    }

    public boolean getCalledTheDragon(){
        return calledTheDragon;
    }
    public void setCalledTheDragon(boolean isCalled){
        calledTheDragon = isCalled;
    }

    /**
     * @see Unit
     * @return
     */
    @Override
    public List<String> getInfo(){
        List<String> result = super.getInfo();
        result.remove(result.size()-1);
        result.add(name);
        return result;
    }

}
