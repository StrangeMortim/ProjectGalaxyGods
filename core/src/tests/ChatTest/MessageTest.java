package tests.ChatTest;

import Player.Player;
import Player.Account;
import chat.Message;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Created by Fabi on 02.08.2016.
 */
public class MessageTest {

    @Test
    public void testeSichtbarMachen(){
        Player testPlayer = new Player(new Account("test","1234"));
        Message testMsg = new Message();

        testMsg.setVisibleForAll(false);
        assertFalse(testMsg.getVisibleFor().contains(testPlayer));
        testMsg.makeVisibleFor(testPlayer);
        assertTrue(testMsg.getVisibleFor().contains(testPlayer));
    }
}
