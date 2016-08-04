package tests.ActionTest;

import Action.BuffInfo;
import Action.ReduceUnitCosts;
import GameObject.Constants;
import Player.Player;
import Player.Account;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Fabi on 02.08.2016.
 */
public class ReducedUnitCostTest {
/*
    @Test
    public void testeAusfuehren() throws RemoteException {
        Player testPlayer = new Player(new Account("test","1234"));
        ReduceUnitCosts testCosts = new ReduceUnitCosts(null,null,testPlayer);
        int mana = testPlayer.getRessources()[Constants.MANA];
        int gold = testPlayer.getRessources()[Constants.GOLD];

        testPlayer.getRessourceBoni()[Constants.GOLD] = 5;
        testPlayer.getRessourceBoni()[Constants.MANA] = 5;
        assertFalse(testCosts.execute());
        assertTrue(testPlayer.hasReducedUnitCosts());
        assertTrue(testPlayer.getRessources()[Constants.MANA] == mana-(BuffInfo.REDUCED_UNIT_COST.getBuffCost()[Constants.MANA] - testPlayer.getRessourceBoni()[Constants.MANA]));
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == gold-(BuffInfo.REDUCED_UNIT_COST.getBuffCost()[Constants.GOLD] - testPlayer.getRessourceBoni()[Constants.GOLD]));
        assertFalse(testCosts.execute());
        assertFalse(testCosts.execute());
        assertTrue(testCosts.execute());
    }
*/
}
