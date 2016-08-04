package tests.ActionTest;


import Action.BuffInfo;
import Action.Shield;
import GameObject.Constants;
import GameObject.GameSession;
import GameObject.Hero;
import GameObject.UnitType;
import Player.Player;
import Player.Account;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by Fabi on 02.08.2016.
 */
public class ShieldTest {
/*
    @Test
    public void testeAusfuehren() throws RemoteException {
        GameSession session = new GameSession();
        Player testPlayer = new Player(new Account("test","1234"));
        Hero testHero = new Hero(UnitType.HERO,testPlayer,"Test");
        Shield testShield = new Shield(testHero,testHero,testPlayer);
        session.getMap().getFields()[0][0].setCurrent(testHero);
        int mana = testPlayer.getRessources()[Constants.MANA];

        testPlayer.getRessourceBoni()[Constants.MANA] = 5;
        assertTrue(testHero.getDef() == UnitType.HERO.getDef());
        assertTrue(testShield.execute());
        assertTrue(testHero.getDef() == (UnitType.HERO.getDef()+ BuffInfo.SHIELD.getPower()));
        assertTrue(testPlayer.getRessources()[Constants.MANA] == mana - (BuffInfo.SHIELD.getBuffCost()[Constants.MANA] - testPlayer.getRessourceBoni()[Constants.MANA]));
        assertFalse(session.getBuffs().get(0).execute());
        assertTrue(session.getBuffs().get(0).execute());
    }*/
}
