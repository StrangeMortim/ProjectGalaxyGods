package GameObject;

import Action.Action;
import Player.Player;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Hero extends Unit implements IHero {

    private Action leftHand;
    private Action rightHand;
    private String name;

    public Hero(UnitType type, Player owner, String name) {
        super(type, owner);

        if(name.equals(""))
            throw new IllegalArgumentException("You must name your hero");

        this.name = name;
    }

    /**
     * Getter und Setter
     *
     * @param leftHand
     */
    @Override
    public void setLeftHand(Action leftHand) {
        this.leftHand = leftHand;
    }

    @Override
    public Action getLeftHand() {
        return leftHand;
    }

    @Override
    public void setRightHand(Action rightHand) {
        this.rightHand = rightHand;
    }

    @Override
    public Action getRightHand() {
        return rightHand;
    }

    @Override
    public void setName(String name) {
        if (name.equals(""))
            throw new IllegalArgumentException("Name is empty");

        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }


}
