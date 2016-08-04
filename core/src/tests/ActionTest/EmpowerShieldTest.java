package tests.ActionTest;

import Action.Buff;
import Action.BuffInfo;
import Action.EmpowerShield;
import GameObject.Constants;
import GameObject.Hero;
import GameObject.UnitType;
import Player.Player;
import Player.Account;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Fabi on 02.08.2016.
 */
public class EmpowerShieldTest {
/*
    @Test
    public void testeAusfuehren(){
        Player testPlayer = new Player(new Account("test","1234"));
        Hero testHero = new Hero(UnitType.HERO,testPlayer,"Test");
        EmpowerShield testBuff = new EmpowerShield(testHero,null,testPlayer);
        int mana = testPlayer.getRessources()[Constants.MANA];

        assertTrue(((Buff)testHero.getRightHand()).getDef() == BuffInfo.SHIELD.getPower());
        assertTrue(testBuff.getRoundsLeft() == BuffInfo.EMPOWER_SHIELD.getRounds());
        assertFalse(testBuff.execute());
        assertTrue(testPlayer.getRessources()[Constants.MANA] == mana - BuffInfo.HEAL.getBuffCost()[Constants.MANA]);
        assertTrue(((Buff)testHero.getRightHand()).getDef() == BuffInfo.SHIELD.getPower()+BuffInfo.EMPOWER_SHIELD.getPower());
        assertTrue(testBuff.execute());
        assertTrue(((Buff)testHero.getRightHand()).getDef() == BuffInfo.SHIELD.getPower());
        assertTrue(testBuff.getRoundsLeft() == 0);
    }
*/
}
