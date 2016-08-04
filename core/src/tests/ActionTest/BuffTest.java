package tests.ActionTest;

import Action.Buff;
import Action.BuffInfo;
import GameObject.Unit;
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
public class BuffTest {
/*

    @Test
    public void testeAusfuehren(){
        Unit testUnit = new Unit(UnitType.SWORDFIGHTER, new Player(new Account("test","1234")));
        Buff testBuff = new Buff(testUnit,testUnit,null, BuffInfo.NONE);
        testBuff.setDef(5);
        testBuff.setAtk(5);
        testBuff.setRoundsLeft(1);
        testBuff.setRange(5);
        testBuff.setHp(5);
        testBuff.setMovePoints(5);

        assertTrue(testUnit.getRange() == UnitType.SWORDFIGHTER.getRange());
        assertFalse(testBuff.execute());
        assertTrue(testUnit.getRange() == UnitType.SWORDFIGHTER.getRange()+5);
        assertTrue(testUnit.getDef() == UnitType.SWORDFIGHTER.getDef()+5);
        assertTrue(testUnit.getAtk() == UnitType.SWORDFIGHTER.getAtk()+5);
        assertTrue(testUnit.getMaxHp() == UnitType.SWORDFIGHTER.getMaxHp()+5);
        assertTrue(testUnit.getMovePoints() == UnitType.SWORDFIGHTER.getMovePoints()+5);
        assertTrue(testBuff.getRoundsLeft() == 1);
        assertTrue(testBuff.execute());
        assertTrue(testUnit.getRange() == UnitType.SWORDFIGHTER.getRange());
        assertTrue(testUnit.getDef() == UnitType.SWORDFIGHTER.getDef());
        assertTrue(testUnit.getAtk() == UnitType.SWORDFIGHTER.getAtk());
        assertTrue(testUnit.getMaxHp() == UnitType.SWORDFIGHTER.getMaxHp());
        assertTrue(testUnit.getMovePoints() == UnitType.SWORDFIGHTER.getMovePoints());
        assertTrue(testBuff.getRoundsLeft() == 0);
    }

    @Test
    public void testeGiltFuerUnit() throws RemoteException {
        Player testPlayer = new Player(new Account("test","1234"));
        Unit testUnit = new Unit(UnitType.SWORDFIGHTER, testPlayer);
        Unit testUnit2 = new Unit(UnitType.ARCHER, testPlayer);
        Unit testUnit3 = new Unit(UnitType.WORKER, testPlayer);
        Unit testUnit4 = new Unit(UnitType.SPEARFIGHTER, testPlayer);
        Buff testBuff = new Buff(testUnit,testUnit,testPlayer, BuffInfo.NONE);

        assertTrue(testBuff.appliesForUnit(testUnit));
        assertTrue(testBuff.appliesForUnit(testUnit2));
        assertTrue(testBuff.appliesForUnit(testUnit3));
        assertTrue(testBuff.appliesForUnit(testUnit4));
        testBuff.getTargets().add(UnitType.ARCHER);
        assertFalse(testBuff.appliesForUnit(testUnit));
        assertTrue(testBuff.appliesForUnit(testUnit2));
        assertFalse(testBuff.appliesForUnit(testUnit3));
        assertFalse(testBuff.appliesForUnit(testUnit4));
    }
*/
}
