package GameObject;

import Action.Action;

import java.rmi.Remote;


public interface IHero extends Remote {

    /**
     * Getter und Setter
     */
    public void setLeftHand(Action leftHand);

    public Action getLeftHand();

    public void setRightHand(Action rightHand);

    public Action getRightHand();

    public void setName(String name);

    public String getName();
}
