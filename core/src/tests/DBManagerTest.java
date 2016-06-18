package tests;


import server.DBManager;

/**
 * Created by benja_000 on 18.06.2016.
 */
public class DBManagerTest {

    public void RegisterAndCheckAccountTest(){
        DBManager dbManager = new DBManager();
       dbManager.registerAccount("test","1234");
        dbManager.dropTables();
    }
}
