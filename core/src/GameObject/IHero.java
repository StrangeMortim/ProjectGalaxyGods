package GameObject;

import Action.Action;


public interface IHero {

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
