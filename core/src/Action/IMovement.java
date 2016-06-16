package Action;


import java.rmi.Remote;

public interface IMovement extends Remote {

    /**
     * Getter und setter
     */
    public void setXAmount(int xAmount);

    public int getXAmount();

    public void setYAmount(int yAmount);

    public int getYAmount();

}
