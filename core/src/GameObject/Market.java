package GameObject;

import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Created by benja_000 on 12.06.2016.
 * Realisiert den Marktplatz auf dem Spieler Ressourcen kaufen und verkaufen koennen.
 */
public class Market implements Serializable{

   // private static final long serialVersionUID = -8470908004440665355L;
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

    private GameSession session = null;
    private int iD;


    public Market(GameSession session){
        if(session == null)
            throw new IllegalArgumentException("Session ist null in Market");

        this.session = session;
        try {
           iD = session.registerObject(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

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

    public boolean buy(Player p, int type, int amount) {
        if(p == null)
            return false;

        switch (type){
            case Constants.WOOD:

                if(wood < amount || amount*woodPrice > p.getRessources()[Constants.GOLD])
                    return false;

                p.getRessources()[Constants.WOOD] += amount;
                p.getRessources()[Constants.GOLD] -= amount*woodPrice;
                wood -= amount;
                double woodValue = ((double)Constants.WOOD_MARKET_DEFAULT_AMOUNT)/wood;
                woodValue = (woodValue<1.0) ? 1.0 : woodValue;
                woodPrice = (int)(woodValue * Constants.WOOD_MARKET_DEFAULT_PRICE);
                return true;
            case Constants.IRON:
                if(iron < amount || amount*ironPrice > p.getRessources()[Constants.GOLD])
                    return false;

                p.getRessources()[Constants.IRON] += amount;
                p.getRessources()[Constants.GOLD] -= amount*ironPrice;
                iron -= amount;
                double ironValue = ((double)Constants.IRON_MARKET_DEFAULT_AMOUNT)/iron;
                ironValue = (ironValue<1.0) ? 1.0 : ironValue;
                ironPrice = (int)(ironValue*Constants.IRON_MARKET_DEFAULT_PRICE);
                return true;
            default:
                return false;
        }
    }

    /**
     * Ermöglicht einem Spieler eine Ressource zu verkaufen
     *
     * @param p     Der Spieler der etwas verkaufen will
     * @param type   Bestimmt die zu verkaufende Ressource(0=Holz, 1=Eisen)
     * @param amount Gibt die Menge an die verkauft werden soll
     * @return ob der vorgang erfolgreich war oder nicht
     */

    public boolean sell(Player p, int type, int amount) {
        if(p == null)
            return false;
        //TODO fix price
        switch (type){
            case Constants.WOOD:
                if(p.getRessources()[Constants.WOOD] < amount)
                    return false;

                wood += amount;
                double woodValue = ((double)Constants.WOOD_MARKET_DEFAULT_AMOUNT)/wood;
                woodValue = (woodValue<1.0) ? 1.0 : woodValue;
                woodPrice = (int)(woodValue * Constants.WOOD_MARKET_DEFAULT_PRICE);
                p.getRessources()[Constants.WOOD] -= amount;
                p.getRessources()[Constants.GOLD] += amount*woodPrice;
                return true;
            case Constants.IRON:
                if(p.getRessources()[1] < amount)
                    return false;

                iron += amount;
                double ironValue = ((double)Constants.IRON_MARKET_DEFAULT_AMOUNT)/iron;
                ironValue = (ironValue<1.0) ? 1.0 : ironValue;
                ironPrice = (int)(ironValue*Constants.IRON_MARKET_DEFAULT_PRICE);
                p.getRessources()[Constants.IRON] -= amount;
                p.getRessources()[Constants.GOLD] += amount*ironPrice;
                return true;
            default:
                return false;
        }
    }

    //Getter Setter

    public void setIron(int amount) {
iron=amount;
    }


    public int getIron() {
        return iron;
    }


    public void setWood(int amount) {
wood=amount;
    }


    public int getWood(){
        return wood;
    }


    public int ironPrice() {
        return ironPrice;
    }

    public int woodPrice() {
        return woodPrice;
    }


    public int getId()  {
        return iD;
    }

    public int[] getMarketInfo(){
        return new int[]{wood,iron,woodPrice,ironPrice};
    }
}
