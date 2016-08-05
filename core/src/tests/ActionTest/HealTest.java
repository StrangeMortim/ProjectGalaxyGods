package tests.ActionTest;

import Action.BuffInfo;
import GameObject.Constants;
import GameObject.Hero;
import GameObject.Unit;
import GameObject.UnitType;
import Player.Player;
import Player.Account;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by Fabi on 02.08.2016.
 */
public class HealTest {
/*
    @Test
    public void testeAusfuehren(){
        Player testPlayer = new Player(new Account("test","1234"));
        Hero testHero = new Hero(UnitType.HERO,testPlayer,"Test");
        int mana = testPlayer.getRessources()[Constants.MANA];

        testPlayer.getRessourceBoni()[Constants.MANA] = 5;
        testHero.setCurrentHp(1);
        assertTrue(testHero.getLeftHand().execute());
        assertTrue(testHero.getCurrentHp() == BuffInfo.HEAL.getPower()+1);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == mana - (BuffInfo.HEAL.getBuffCost()[Constants.MANA] - testPlayer.getRessourceBoni()[Constants.MANA]));
    }

    @Test
    public void testeAusfuehrenHeal2(){
        Player testPlayer = new Player(new Account("test","1234"));
        Hero testHero = new Hero(UnitType.HERO,testPlayer,"Test");
        Unit testUnit = new Unit(UnitType.SWORDFIGHTER,testPlayer);
        int mana = testPlayer.getRessources()[Constants.MANA];

        testPlayer.getRessourceBoni()[Constants.MANA] = 5;
        testHero.setCurrentHp(1);
        testUnit.setCurrentHp(1);
        assertTrue(testHeal.execute());
        assertTrue(testHero.getCurrentHp() == BuffInfo.HEAL.getPower()+1);
        assertTrue(testUnit.getCurrentHp() == testUnit.getMaxHp());
        assertTrue(testPlayer.getRessources()[Constants.MANA] == mana - (BuffInfo.HEAL.getBuffCost()[Constants.MANA] - testPlayer.getRessourceBoni()[Constants.MANA]));
    }*/
}
