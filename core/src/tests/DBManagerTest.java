package tests;


import GameObject.GameSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.DBManager;

/**
 * Testklasse fuer den DBManager.
 */
public class DBManagerTest {

    DBManager dbManager;
    GameSession gameSession;

    @Before
    public void createDB(){
        dbManager=new DBManager();
        gameSession=new GameSession();
    }

    @After
    public void dropTables(){
        dbManager.dropTables();
    }


    /**
     * Testet das Registrieren und Checken eines Accounts.
     */
    @Test
    public void RegisterAndCheckAccountTest(){
        Assert.assertTrue(dbManager.registerAccount("test","1234"));
        Assert.assertTrue(dbManager.checkAccount("test","1234"));
        Assert.assertFalse(dbManager.registerAccount("test","1234"));
        Assert.assertFalse(dbManager.checkAccount("test","1235"));
    }

    /**
     * Testet das Speichern und Laden eines neuen simplen GameSession-Objektes
     */
    @Test
    public void saveAndLoadGameSessionTest(){
        gameSession.setName("test1");
        gameSession.setHasStarted(true);
        dbManager.saveSession(gameSession);
        GameSession gs =dbManager.loadSession("test1");
        Assert.assertTrue(gs.isHasStarted());
    }

    /**
     * Testet das Speichern und Updaten eines simplen GameSession-Objektes
     */
    @Test
    public void updateAndLoadGameSessionTest(){
        gameSession.setName("test2");
        gameSession.setHasStarted(true);
        dbManager.saveSession(gameSession);
        gameSession.setHasStarted(false);
        dbManager.saveSession(gameSession);
        GameSession gs =dbManager.loadSession("test2");
        Assert.assertFalse(gs.isHasStarted());
    }


}
