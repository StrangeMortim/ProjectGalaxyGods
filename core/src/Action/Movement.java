package Action;

import GameObject.Field;
import GameObject.Unit;
import Player.Player;


public class Movement extends Action implements IMovement{

    private int xAmount = 0;
    private int yAmount = 0;

    public Movement(Unit origin, Unit target, Player player, ActionProcessor processor) {
        super(origin, target, player);
    }

    @Override
    public int getXAmount() {
        return xAmount;
    }

    @Override
    public void setXAmount(int xAmount) {
        this.xAmount = xAmount;
    }

    @Override
    public int getYAmount() {
        return yAmount;
    }

    @Override
    public void setYAmount(int yAcmount) {
        this.yAmount = yAcmount;
    }

    @Override
    public boolean execute(){
    /*    Field originField = origin.getField();
        Field destination = parent.getSession().getMap().getField(originField.getXPos()+xAmount, originField.getYPos()+yAmount);
        originField.setCurrent(null);
        destination.setCurrent(origin);

        return true;*/
        /* TODO validate action in screens(using map.checkMovement with destination position)*/
        return true;
    }
}
