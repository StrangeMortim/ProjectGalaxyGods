package GameObject;

import java.rmi.Remote;
import Player.Player;

/**
 * Created by benja_000 on 12.06.2016.
 * Realisiert das Marktplatz-Objekt, wo Spieler ihre Ware verkaufen oder kaufen koennen.
 */
public interface IMarket extends Remote {

    /**
     * Ermöglicht einem Spieler eine Ressource zu kaufen
     * @param p         Der Spieler der etwas kaufen will
     * @param type      Bestimmt die zu kaufende Ressource(0=Holz, 1=Eisen)
     * @param amount    Gibt die Menge an die gekauft werden soll
     * @return ob der vorgang erfolgreich war oder nicht
     */
    public boolean buy(Player p, int type, int amount);

    /**
     * Ermöglicht einem Spieler eine Ressource zu verkaufen
     * @param p         Der Spieler der etwas verkaufen will
     * @param type      Bestimmt die zu verkaufende Ressource(0=Holz, 1=Eisen)
     * @param amount    Gibt die Menge an die verkauft werden soll
     * @return ob der vorgang erfolgreich war oder nicht
     */
    public boolean sell(Player p, int type, int amount);

    //Getter Setter
    public void setIron(int price);
    public void setWood(int price);
    public int ironPrice();
    public int woodPrice();

}
