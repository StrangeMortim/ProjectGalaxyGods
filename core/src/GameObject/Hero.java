package GameObject;

import Action.Action;
import Player.Player;
import Action.*;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Hero extends Unit implements IHero,Serializable {

    private static final long serialVersionUID = -7787413125681105826L;
    private Action leftHand;
    private Action rightHand;
    private String name;

    public Hero(UnitType type, Player owner, String name) {
        super(type, owner);
        try {
            owner.setHero(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(name.equals(""))
            throw new IllegalArgumentException("You must name your hero");

        this.name = name;
        setLeftHand(new Heal(this,this,getOwner()));
        setRightHand(new Shield(this,this,getOwner()));
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
