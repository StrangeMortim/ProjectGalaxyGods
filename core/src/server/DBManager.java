package server;

import GameObject.GameSession;
import GameObject.Market;

import java.io.*;
import java.sql.*;

/**
 *
 */
public class DBManager
{
    /**
     * Laedt die GameSession mit spezifischem Namen.
     * @param sessionName Name der GameSession
     * @return GameSession mit dem jeweiligen Namen, sonst null.
     */
    public GameSession loadSession(String sessionName){
        return null;
    }

    /**
     *Speichert die uebergebene GameSession.
     * @param session zu speichernde GameSession
     * @return true, falls Speichern erfolgreich, sonst false.
     */
    public boolean saveSession(GameSession session){
        return false;
    }

    /**
     * Diese Methode realisiert die Registration eines neuen Accounts.
     *
     * @param name     Name vom Account
     * @param password Passwort vom Account
     * @return true, wenn Registration geklappt hat, sonst false
     */
    public boolean registerAccount(String name, String password){
        return false;
    }

    /**
     * Prueft, ob Eingaben zum Account korrekt sind.
     *
     * @param name     Name des Accounts
     * @param password Passwort des Accounts
     * @return true, wenn Pruefung erfolgreich, sonst false.
     */
    public boolean checkAccount(String name, String password){
        return false;
    }

    /**
     * Gibt alle GameSession-Namen zurueck, die auf dem Server gespeichert sind.
     *
     * @return Namen der GameSession-Objekte
     */
    public String getSessionList(){
        return "";
    }

    public DBManager()
    {

        try
        {
            // Create a named constant for the URL.
            // NOTE: This value is specific for Java DB.
            final String DB_URL = "jdbc:derby:DerbyDB;create=true";

            // Create a connection to the database.
            Connection conn =
                    DriverManager.getConnection(DB_URL);

            // If the DB already exists, drop the tables.
            dropTables(conn);


            buildGameSessionTable(conn);


            // Close the connection.
            conn.close();
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    /**
     * The dropTables method drops any existing
     * in case the database already exists.
     */
    public static void dropTables(Connection conn)
    {
        System.out.println("Checking for existing tables.");

        try
        {
            // Get a Statement object.
            Statement stmt = conn.createStatement();

            try
            {
                stmt.execute("DROP TABLE GameSessions");
                System.out.println("GameSessions table dropped.");
            } catch (SQLException ex)
            {
                // No need to report an error.
                // The table simply did not exist.
            }



        } catch (SQLException ex)
        {
            System.out.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }






    public static void buildGameSessionTable(Connection conn)
    {
        try
        {
            // Get a Statement object.
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE GameSessions (id VARCHAR(15), session Blob)");
            conn.commit();


            try {

                GameSession gs = new GameSession();
                gs.setRound(2);
                gs.setHasStarted(false);
                gs.setMaxPlayersPerTeam(2);
                Market m = new Market();
                m.setIron(1000);
                m.setWood(2000);
                gs.setMarket(m);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(gs);
                byte[] employeeAsBytes = baos.toByteArray();
                PreparedStatement pstmt = conn
                        .prepareStatement("INSERT INTO GameSessions (session) VALUES(?)");
                ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes);
                pstmt.setBinaryStream(1, bais, employeeAsBytes.length);
                pstmt.executeUpdate();
                pstmt.close();

                System.out.println("GameSessionTable created.");

            }catch(Exception e){
                System.out.println("ERROR1: " + e.getMessage());
            }




           try{
               Statement stmt2 = conn.createStatement();
               ResultSet rs = stmt2.executeQuery("SELECT session FROM GameSessions");
               while (rs.next()) {
                   byte[] st =  rs.getBytes(1);

                   ByteArrayInputStream baip = new ByteArrayInputStream(st);

                   ObjectInputStream ois = new ObjectInputStream(baip);

                   GameSession emp = (GameSession) ois.readObject();

                   System.out.println(emp.getMarket().woodPrice());
               }
               stmt2.close();
               rs.close();
               conn.close();

            }catch(Exception e){
               System.out.println("ERROR2: " + e.getMessage());
           }

        } catch (SQLException ex)
        {
            System.out.println("ERROR3: " + ex.getMessage());
        }
    }

}
