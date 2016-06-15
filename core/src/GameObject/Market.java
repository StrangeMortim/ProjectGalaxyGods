package GameObject;

import Player.Player;

import java.io.Serializable;

/**
 * Created by benja_000 on 12.06.2016.
 * Realisiert den Marktplatz auf dem Spieler Ressourcen kaufen und verkaufen koennen.
 */
public class Market implements IMarket,Serializable{
    /**
     *  Verfügbares Holz.
     */
    private int wood=100;

    /**
     * Preis für Holz
     */
    private int woodPrice=10;

    /**
     * Verfügbares Eisen
     */
    private int iron=100;

    /**
     * Preis fuer Eisen.
     */
    private int ironPrice = 10;


    public Market(){
        //Nothing to do because only default Values
    }


    /**
     * Ermöglicht einem Spieler eine Ressource zu kaufen
     *
     * @param p      Der Spieler der etwas kaufen will
     * @param type   Bestimmt die zu kaufende Ressource(0=Holz, 1=Eisen)
     * @param amount Gibt die Menge an die gekauft werden soll
     * @return ob der vorgang erfolgreich war oder nicht
     */
    @Override
    public boolean buy(Player p, int type, int amount) {
        switch (type){
            case 0:
                if(wood < amount || amount*woodPrice > p.getRessources()[2])
                    return false;

                p.getRessources()[0] += amount;
                p.getRessources()[2] -= amount*woodPrice;
                wood -= amount;
                woodPrice = (100/wood) * 10;
                return true;
            case 1:
                if(iron < amount || amount*ironPrice > p.getRessources()[2])
                    return false;

                p.getRessources()[1] += amount;
                p.getRessources()[2] -= amount*ironPrice;
                iron -= amount;
                ironPrice = (100/iron) * 10;
                return true;
            default:
                return false;
        }
    }

    /**
     * Ermöglicht einem Spieler eine Ressource zu verkaufen
     *
     * @param p      Der Spieler der etwas verkaufen will
     * @param type   Bestimmt die zu verkaufende Ressource(0=Holz, 1=Eisen)
     * @param amount Gibt die Menge an die verkauft werden soll
     * @return ob der vorgang erfolgreich war oder nicht
     */
    @Override
    public boolean sell(Player p, int type, int amount) {
        switch (type){
            case 0:
                if(p.getRessources()[0] < amount)
                    return false;

                p.getRessources()[0] -= amount;
                p.getRessources()[2] += amount*woodPrice;
                wood += amount;
                woodPrice = (100/wood) * 10;
                return true;
            case 1:
                if(p.getRessources()[1] < amount)
                    return false;

                p.getRessources()[1] -= amount;
                p.getRessources()[2] += amount*ironPrice;
                iron += amount;
                ironPrice = (100/iron) * 10;
                return true;
            default:
                return false;
        }
    }

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
        return ironPrice;
    }
    @Override
    public int woodPrice() {
        return woodPrice;
    }
}
