package tests.ChatTest;

import Player.Player;
import Player.Account;
import chat.Chat;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Fabi on 02.08.2016.
 */
public class ChatTest {
/*
    /*
    Da die Methoden hinzufuegen und entfernen sehr simpel sind werden sie zusammen gefasst

    @Test
    public void testTeilnehmerHinzufuegenUndEntfernen(){
        Chat testChat = new Chat();
        Player testPlayer = new Player(new Account("test","1234"));
        Player testPlayer2 = new Player(new Account("test2","12345"));

        assertTrue(testChat.getParticipants().isEmpty());
        testChat.addParticipant(testPlayer);
        assertTrue(testChat.getParticipants().contains(testPlayer));
        assertTrue(testChat.getParticipants().size() == 1);
        testChat.addParticipant(testPlayer2);
        assertTrue(testChat.getParticipants().contains(testPlayer2));
        assertTrue(testChat.getParticipants().size() == 2);
        testChat.removeParticipant(testPlayer);
        assertFalse(testChat.getParticipants().contains(testPlayer));
        assertTrue(testChat.getParticipants().contains(testPlayer2));
        assertTrue(testChat.getParticipants().size() == 1);
        testChat.removeParticipant(testPlayer2);
        assertTrue(testChat.getParticipants().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTeilnehmerHinzufuegenNull() {
        Chat testChat = new Chat();

        testChat.addParticipant(null);
    }

    @Test
    public void testTeilnehmerBlockieren() {
        Chat testChat = new Chat();
        Player testPlayer = new Player(new Account("test","1234"));

        testChat.addParticipant(testPlayer);
        testChat.blockPlayer(testPlayer);
        assertTrue(testChat.getReadOnly().contains(testPlayer));
    }

    @Test
    public void testTeilnehmerFreigeben() throws RemoteException {
        Chat testChat = new Chat();
        Player testPlayer = new Player(new Account("test","1234"));

        testChat.addParticipant(testPlayer);
        testChat.blockPlayer(testPlayer);
        testChat.unblockPlayer(testPlayer);
        assertFalse(testChat.getReadOnly().contains(testPlayer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockiertenTeilnehmerHinzufuegen() {
        Chat testChat = new Chat();
        Player testPlayer = new Player(new Account("test","1234"));

        testChat.blockPlayer(testPlayer);
        testChat.addParticipant(testPlayer);
    }

    @Test
    public void testNachrichtHinzufuegen() throws RemoteException {
        Chat testChat = new Chat();
        Player testPlayer = new Player(new Account("test","1234"));

        assertTrue(testChat.getBacklog().isEmpty());
        testChat.addParticipant(testPlayer);
        testChat.addMessage(testPlayer,"blabla");
        assertTrue(testChat.getBacklog().size() == 1);
        assertTrue(testChat.getBacklog().get(0).getContent().equals(testPlayer.getAccount().getName() + ": " + "blabla"));
        testChat.addMessage(testPlayer,"blabla2");
        assertTrue(testChat.getBacklog().size() == 2);
        assertTrue(testChat.getBacklog().get(1).getContent().equals(testPlayer.getAccount().getName() + ": " + "blabla2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNachrichtHinzufuegenNull() throws RemoteException {
        Chat testChat = new Chat();

        testChat.addMessage(null,"bla");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNachrichtHinzufuegenKeinTeilnehmer() throws RemoteException {
        Chat testChat = new Chat();
        Player testPlayer = new Player(new Account("test","1234"));

        testChat.addMessage(testPlayer,"blabla");
    }

    @Test
    public void testLeeren() throws RemoteException {
        Chat testChat = new Chat();
        Player testPlayer = new Player(new Account("test","1234"));

        assertTrue(testChat.getBacklog().isEmpty());
        testChat.addParticipant(testPlayer);
        testChat.addMessage(testPlayer,"blabla");
        testChat.addMessage(testPlayer,"blabla2");
        testChat.addMessage(testPlayer,"blabla3");
        testChat.clear();
        assertTrue(testChat.getBacklog().isEmpty());
    }*/
}
