package Action;

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
    public void execute(){
        /* TODO*/
    }
}
