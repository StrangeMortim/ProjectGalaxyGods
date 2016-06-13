package GameObject;

import java.io.Serializable;

/**
 * Created by benja_000 on 12.06.2016.
 * Realisiert den Marktplatz auf dem Spieler Ressourcen kaufen und verkaufen koennen.
 */
public class Market implements IMarket,Serializable{
    /**
     *  Preis fuer Holz.
     */
    private int wood;
    /**
     * Preis fuer Eisen.
     */
    private int iron;


    //Getter Setter
    @Override
    public void setIron(int price) {
iron=price;
    }
    @Override
    public void setWood(int price) {
wood=price;
    }
    @Override
    public int ironPrice() {
        return iron;
    }
    @Override
    public int woodPrice() {
        return wood;
    }
}
