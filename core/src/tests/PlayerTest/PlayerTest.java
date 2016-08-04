package tests.PlayerTest;

import Action.Buff;
import Action.BuffInfo;
import GameObject.Constants;
import Player.Player;
import Player.Account;
import Player.TreeElement;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by Fabi on 02.08.2016.
 */
public class PlayerTest {
/*
    int currentWoodAmount;
    int currentIronAmount;
    int currentGoldAmount;
    int currentManaAmount;

    private void setAttributes(Player p){
        currentWoodAmount = p.getRessources()[Constants.WOOD];
        currentIronAmount = p.getRessources()[Constants.IRON];
        currentGoldAmount = p.getRessources()[Constants.GOLD];
        currentManaAmount = p.getRessources()[Constants.MANA];
    }

    private void resetAttributes(){
        currentWoodAmount = Constants.PLAYER_START_WOOD;
        currentIronAmount = Constants.PLAYER_START_IRON;
        currentGoldAmount = Constants.PLAYER_START_GOLD;
        currentManaAmount = Constants.PLAYER_START_MANA;
    }

    @Test
    public void testBuffHinzufuegen() throws RemoteException {
        Player testPlayer = new Player(new Account("test","1234"));
        Buff testBuff = new Buff(null,null,testPlayer, BuffInfo.NONE);
        Buff testBuff2 = new Buff(null,null,testPlayer, BuffInfo.NONE);

        testPlayer.addPermaBuff(testBuff);
        testPlayer.addTemporaryBuff(testBuff2);
        assertTrue(testPlayer.getPermaBuffs().contains(testBuff));
        assertTrue(testPlayer.getTemporaryBuffs().contains(testBuff2));
        assertFalse(testPlayer.getPermaBuffs().contains(testBuff2));
        assertFalse(testPlayer.getTemporaryBuffs().contains(testBuff));
    }

    @Test
    public void testBuffHinzufuegenNull() throws RemoteException {
        Player testPlayer = new Player(new Account("test","1234"));

        testPlayer.addTemporaryBuff(null);
        testPlayer.addPermaBuff(null);
        assertTrue(testPlayer.getPermaBuffs().isEmpty());
        assertTrue(testPlayer.getTemporaryBuffs().isEmpty());
    }


    @Test
    public void testAufStahlZweigVoranschreiten() throws RemoteException {
        Player testPlayer = new Player(new Account("test","1234"));
        setAttributes(testPlayer);

        //zu hohe Stufen
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.STEEL2));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.STEEL3));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.STEEL4));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.STEEL5));

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.STEEL1));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.STEEL1.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.STEEL1.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.STEEL1.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.STEEL1.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getSteel()[0]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.STEEL2));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.STEEL2.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.STEEL2.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.STEEL2.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.STEEL2.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getSteel()[1]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.STEEL3));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.STEEL3.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.STEEL3.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.STEEL3.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.STEEL3.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getSteel()[2]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.STEEL4));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.STEEL4.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.STEEL4.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.STEEL4.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.STEEL4.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getSteel()[3]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.STEEL5));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.STEEL5.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.STEEL5.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.STEEL5.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.STEEL5.getRessourceCosts()[Constants.MANA]);
        assertTrue(testPlayer.getTechTree().isSteelFull());

        resetAttributes();
    }

    @Test
    public void testAufMagieZweigVoranschreiten() throws RemoteException {
        Player testPlayer = new Player(new Account("test","1234"));
        setAttributes(testPlayer);

        //zu hohe Stufen
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.MAGIC2));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.MAGIC3));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.MAGIC4));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.MAGIC5));

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.MAGIC1));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.MAGIC1.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.MAGIC1.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.MAGIC1.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.MAGIC1.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getMagic()[0]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.MAGIC2));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.MAGIC2.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.MAGIC2.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.MAGIC2.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.MAGIC2.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getMagic()[1]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.MAGIC3));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.MAGIC3.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.MAGIC3.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.MAGIC3.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.MAGIC3.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getMagic()[2]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.MAGIC4));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.MAGIC4.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.MAGIC4.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.MAGIC4.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.MAGIC4.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getMagic()[3]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.MAGIC5));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.MAGIC5.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.MAGIC5.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.MAGIC5.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.MAGIC5.getRessourceCosts()[Constants.MANA]);
        assertTrue(testPlayer.getTechTree().isMagicFull());

        resetAttributes();
    }

    @Test
    public void testAufKulturZweigVoranschreiten() throws RemoteException {
        Player testPlayer = new Player(new Account("test","1234"));
        setAttributes(testPlayer);

        //zu hohe Stufen
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.CULTURE2));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.CULTURE3));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.CULTURE4));
        assertFalse(testPlayer.advanceOnTechTree(TreeElement.CULTURE5));

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.CULTURE1));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.CULTURE1.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.CULTURE1.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.CULTURE1.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.CULTURE1.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getCulture()[0]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.CULTURE2));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.CULTURE2.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.CULTURE2.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.CULTURE2.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.CULTURE2.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getCulture()[1]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.CULTURE3));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.CULTURE3.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.CULTURE3.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.CULTURE3.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.CULTURE3.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getCulture()[2]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.CULTURE4));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.CULTURE4.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.CULTURE4.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.CULTURE4.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.CULTURE4.getRessourceCosts()[Constants.MANA]);
        setAttributes(testPlayer);
        assertTrue(testPlayer.getTechTree().getCulture()[3]);

        assertTrue(testPlayer.advanceOnTechTree(TreeElement.CULTURE5));
        assertTrue(testPlayer.getRessources()[Constants.WOOD] == currentWoodAmount-TreeElement.CULTURE5.getRessourceCosts()[Constants.WOOD]);
        assertTrue(testPlayer.getRessources()[Constants.IRON] == currentIronAmount-TreeElement.CULTURE5.getRessourceCosts()[Constants.IRON]);
        assertTrue(testPlayer.getRessources()[Constants.GOLD] == currentGoldAmount-TreeElement.CULTURE5.getRessourceCosts()[Constants.GOLD]);
        assertTrue(testPlayer.getRessources()[Constants.MANA] == currentManaAmount-TreeElement.CULTURE5.getRessourceCosts()[Constants.MANA]);
        assertTrue(testPlayer.getTechTree().isCultureFull());

        resetAttributes();
    }*/
}
