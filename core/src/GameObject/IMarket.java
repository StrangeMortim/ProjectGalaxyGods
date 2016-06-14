package GameObject;

import java.rmi.Remote;

/**
 * Created by benja_000 on 12.06.2016.
 * Realisiert das Marktplatz-Objekt, wo Spieler ihre Ware verkaufen oder kaufen koennen.
 */
public interface IMarket extends Remote {

    //Getter Setter
    public void setIron(int price);
    public void setWood(int price);
    public int ironPrice();
    public int woodPrice();

}
