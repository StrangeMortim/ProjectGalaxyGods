package GameObject;

import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Realises the Marketplace for players to trade
 *
 * @author Benjamin
 */
public class Market implements Serializable{

    /**
     *  Current amount of Wood on the marketplace
     */
    private int wood=100;

    /**
     * Price for each piece of wood in gold
     */
    private int woodPrice=10;

    /**
     *  Current amount of iron on the marketplace
     */
    private int iron=100;

    /**
     * Price for each piece of iron in gold
     */
    private int ironPrice = 10;

    private GameSession session = null;
    private int iD;

    public Market(GameSession session){
        if(session == null)
            throw new IllegalArgumentException("Session ist null in Market");

        this.session = session;
        iD = session.registerObject(this);
    }


    /**
     * Lets a player buy something on the market
     *
     * @param p      The player who wants to buy
     * @param type   Determines the resources(0=Wood, 1=Iron)
     * @param amount How much the player want to buy
     * @return true if the transaction was a success, else false
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
     * Lets a player sell something on the market
     *
     * @param p      The player who wants to sell
     * @param type   Determines the resources(0=Wood, 1=Iron)
     * @param amount How much the player want to sell
     * @return true if the transaction was a success, else false
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

    /**
     * returns the current amount and price values on the market
     * @return the values(for indeces check Constants enum)
     */
    public int[] getMarketInfo(){
        return new int[]{wood,iron,woodPrice,ironPrice};
    }

    /**
     * Getter/setter
     */
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

}
