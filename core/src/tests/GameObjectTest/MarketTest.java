package tests.GameObjectTest;


import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Market;
import Player.Player;
import Player.Account;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by Fabi on 02.08.2016.
 */
public class MarketTest {
/*
    @Test
    public void testKaufen() throws RemoteException {
        GameSession testSession = new GameSession();
        Market testMarket = new Market(testSession);
        Player testPlayer = new Player(new Account("test","1234"));
        int goldTmp = 0;


        //definitiv genug gold vorhanden
        testPlayer.getRessources()[Constants.GOLD] = 99999;

        assertTrue(testMarket.buy(testPlayer.getAccount().getName(),Constants.WOOD, Constants.WOOD_MARKET_DEFAULT_AMOUNT));
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == 99999 - (Constants.WOOD_MARKET_DEFAULT_AMOUNT*Constants.WOOD_MARKET_DEFAULT_PRICE));
        goldTmp = testPlayer.getRessources()[Constants.GOLD];
        assertTrue(testMarket.buy(testPlayer.getAccount().getName(),Constants.IRON,Constants.IRON_MARKET_DEFAULT_AMOUNT));
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == goldTmp - (Constants.IRON_MARKET_DEFAULT_AMOUNT*Constants.IRON_MARKET_DEFAULT_PRICE));
        assertTrue(testMarket.getWood() == 0);
        assertTrue(testMarket.getIron() == 0);
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == Constants.PLAYER_START_WOOD+Constants.WOOD_MARKET_DEFAULT_AMOUNT);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == Constants.PLAYER_START_IRON+Constants.IRON_MARKET_DEFAULT_AMOUNT);
    }

    @Test
    public void testKaufenNichtGenugGold() throws RemoteException {
        GameSession testSession = new GameSession();
        Market testMarket = new Market(testSession);
        Player testPlayer = new Player(new Account("test","1234"));

        //definitiv genug gold vorhanden
        testPlayer.getRessources()[Constants.GOLD] = 0;

        assertFalse(testMarket.buy(testPlayer.getAccount().getName(),Constants.WOOD, Constants.WOOD_MARKET_DEFAULT_AMOUNT));
        assertFalse(testMarket.buy(testPlayer.getAccount().getName(),Constants.IRON, Constants.IRON_MARKET_DEFAULT_AMOUNT));
    }

    @Test
    public void testKaufenNichtGenugHolzVorhanden() throws RemoteException {
        GameSession testSession = new GameSession();
        Market testMarket = new Market(testSession);
        Player testPlayer = new Player(new Account("test","1234"));

        //definitiv genug gold vorhanden
        testPlayer.getRessources()[Constants.GOLD] = 99999;

        testMarket.setWood(0);

        assertFalse(testMarket.buy(testPlayer.getAccount().getName(),Constants.WOOD, Constants.WOOD_MARKET_DEFAULT_AMOUNT));
    }

    @Test
    public void testKaufenNichtGenugEisenVorhanden() throws RemoteException {
        GameSession testSession = new GameSession();
        Market testMarket = new Market(testSession);
        Player testPlayer = new Player(new Account("test","1234"));

        //definitiv genug gold vorhanden
        testPlayer.getRessources()[Constants.GOLD] = 99999;

        testMarket.setIron(0);

        assertFalse(testMarket.buy(testPlayer.getAccount().getName(),Constants.IRON, Constants.IRON_MARKET_DEFAULT_AMOUNT));
    }

    @Test
    public void testVerkaufen() throws RemoteException {
        GameSession testSession = new GameSession();
        Market testMarket = new Market(testSession);
        Player testPlayer = new Player(new Account("test","1234"));
        int goldTmp = 0;


        //definitiv genug gold vorhanden
        testPlayer.getRessources()[Constants.GOLD] = 0;

        assertTrue(testMarket.sell(testPlayer.getAccount().getName(),Constants.WOOD, Constants.PLAYER_START_WOOD));
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == 0 + (Constants.PLAYER_START_WOOD*Constants.WOOD_MARKET_DEFAULT_PRICE));
        goldTmp = testPlayer.getRessources()[Constants.GOLD];
        assertTrue(testMarket.sell(testPlayer.getAccount().getName(),Constants.IRON,Constants.PLAYER_START_IRON));
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == goldTmp + (Constants.PLAYER_START_IRON*Constants.IRON_MARKET_DEFAULT_PRICE));
        assertTrue(testMarket.getWood() == Constants.PLAYER_START_WOOD+Constants.WOOD_MARKET_DEFAULT_AMOUNT);
        assertTrue(testMarket.getIron() == Constants.PLAYER_START_IRON+Constants.IRON_MARKET_DEFAULT_AMOUNT);
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == 0);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == 0);
    }

    @Test
    public void testVerkaufenNichtGenugHolz() throws RemoteException {
        GameSession testSession = new GameSession();
        Market testMarket = new Market(testSession);
        Player testPlayer = new Player(new Account("test","1234"));

        //definitiv genug gold vorhanden
        testPlayer.getRessources()[Constants.WOOD] = 0;

        assertFalse(testMarket.sell(testPlayer.getAccount().getName(),Constants.WOOD, Constants.WOOD_MARKET_DEFAULT_AMOUNT));
    }

    @Test
    public void testVerkaufenNichtGenugEisen() throws RemoteException {
        GameSession testSession = new GameSession();
        Market testMarket = new Market(testSession);
        Player testPlayer = new Player(new Account("test","1234"));

        //definitiv genug gold vorhanden
        testPlayer.getRessources()[Constants.IRON] = 0;

        assertFalse(testMarket.sell(testPlayer.getAccount().getName(),Constants.IRON, Constants.IRON_MARKET_DEFAULT_AMOUNT));
    }
*/

}
