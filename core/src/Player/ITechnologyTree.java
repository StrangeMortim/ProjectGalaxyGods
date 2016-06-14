package Player;


import java.rmi.Remote;

public interface ITechnologyTree extends Remote {

    /*
    Getter und Setter
     */
    public void setSteel(Boolean achieved[]);

    public Boolean[] getSteel();

    public void setMagic(Boolean achieved[]);

    public Boolean[] getMagic();

    public void setCulture(Boolean achieved[]);

    public Boolean[] getCulture();

}
