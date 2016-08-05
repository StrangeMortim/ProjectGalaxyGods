package server;

import GameObject.GameSession;
import GameObject.Market;

import java.io.*;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;

/**
 * This class realises all communication with the Database using JDBC-Embedded
 * therefore she takes care of all saving an loading processes for GameSessions and Account-Objects
 * @author Benjamin
 */
public class DBManager {
    //Verbindungsadresse der Datenbank.
    final static String DB_URL = "jdbc:derby:DerbyDB;create=true";

    /**
     * The Constructor, Initializes missing Parts of the Database if needed
     */
    public DBManager()
    {
        try
        {
            try {
                DriverManager.getConnection("jdbc:derby:DerbyDB;shutdown=true");
            }catch(Exception e){}

            Connection conn = DriverManager.getConnection(DB_URL);
            try{buildGameSessionTable(conn);
            }catch(Exception e){System.out.println("GameSession-Table ist vorhanden.");}
            try{buildAccountTable(conn);
            }catch(Exception e){System.out.println("Account-Table ist vorhanden.");}
            conn.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }


    /**
     * Loads the GameSession with the given Name
     * @param sessionName Name of the GameSession
     * @return the GameSession if found, otherwise null
     */
    public static GameSession loadSession(String sessionName){
        GameSession session=null;
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement sta = conn.prepareStatement(
                    "SELECT session FROM GameSessions WHERE id = '"+sessionName+"'");
            ResultSet res = sta.executeQuery();
            while (res.next()) {
                byte[] st =  res.getBytes(1);
                ByteArrayInputStream baip = new ByteArrayInputStream(st);
                ObjectInputStream ois = new ObjectInputStream(baip);
                session = (GameSession) ois.readObject();
            }
            conn.close();
            res.close();
            sta.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return session;
    }

    /**
     * Saves the GameSession
     * @param gs the GameSession to save
     * @return true if saving was successfull else false
     */
    public boolean saveSession(GameSession gs){

  if(getSessionList().matches("(.*)"+gs.getName()+"(.*)")){
      try{
          Connection conn = DriverManager.getConnection(DB_URL);
          String update="UPDATE GameSessions SET session = ? WHERE id= ?";
          PreparedStatement pstmt = conn.prepareStatement(update);
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(gs);
          byte[] gBytes = baos.toByteArray();
          ByteArrayInputStream bais = new ByteArrayInputStream(gBytes);
          pstmt.setBinaryStream(1, bais, gBytes.length);
          pstmt.setString(2,gs.getName());
          pstmt.executeUpdate();
          pstmt.close();
          conn.close();
          return true;
      }catch(Exception e){
          e.printStackTrace();System.out.println(e.getMessage());
        System.out.println("Beim Update der GameSession ist etwas fehlgeschlagen!");
          return false;
      }
  }
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gs);
            byte[] gBytes = baos.toByteArray();
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO GameSessions (id, session) VALUES(?,?)");
            ByteArrayInputStream bais = new ByteArrayInputStream(gBytes);
            pstmt.setBinaryStream(2, bais, gBytes.length);
            pstmt.setString(1, gs.getName());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();System.out.println(e.getMessage());
            System.out.println("Beim Speichern der GameSession ist etwas fehlgeschlagen!");
            return false;
        }
    }

    /**
     * Registers a new Account with the given Data, in the Database
     *
     * @param name     Name of the Account
     * @param password Password of the Account
     * @return true if registration was a success else false
     */
    public static boolean registerAccount(String name, String password){
try {
    if(getAccountList().matches("(.*)"+name+"(.*)")) {
        System.out.println("Account mit Namen: '"+name+"' existiert bereits.");
        return false;
    }
    Connection conn = DriverManager.getConnection(DB_URL);
    PreparedStatement pstmt = conn
            .prepareStatement("INSERT INTO Accounts (id, password) VALUES(?,?)");
    pstmt.setString(1,name);
    pstmt.setString(2,password);
    pstmt.executeUpdate();
    pstmt.close();
    conn.close();
    System.out.println("Account mit Namen: '"+name+"' wurde erstellt.");
    return true;
}catch(Exception e){return false;}
    }

    /**
     * Checks the given Data to it's corresponding Account counterpart in the Database
     *
     * @param name     Name of the Account
     * @param password Password of the Account
     * @return true if the given Data is correct else false
     */
    public static boolean checkAccount(String name, String password){
        if(getAccountList().matches("(.*)"+name+"(.*)")) {
            Connection conn = null;
            PreparedStatement sta = null;
            ResultSet res = null;
try {
    conn = DriverManager.getConnection(DB_URL);
    sta = conn.prepareStatement(
            "SELECT password FROM Accounts WHERE id = '" + name + "'");
    res = sta.executeQuery();
    String pw="";
    while (res.next()) {
        pw = res.getString(1);
    }

    if(pw.equals(password)){
        System.out.println("Account mit Namen: '"+name+"' existiert!");
        return true;}
}catch (SQLException e) {
    e.printStackTrace();
    return false;
} finally {
    try {
        if (sta != null)
            sta.close();

        if(res != null)
            res.close();

        if(conn != null);
        conn.close();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}
        }
        return false;
    }

    /**
     * Returns all GameSession names in the Database
     * @return All name separated with an ;
     */
    public static String getSessionList(){
        String list="";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id FROM GameSessions");
            while (rs.next()) {
                String s = rs.getString(1);
                list=list+s+";";
            }
            st.close();
            rs.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Returns all account names in the Database
     * @return All accountNames separated with an ;
     */
    public static String getAccountList(){
        String list="";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id FROM ACCOUNTS");
            while (rs.next()) {
                String s = rs.getString(1);
                list=list+s+";";
            }
            rs.close();
            st.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Deletes every GameSession and Account entry in the Database
     */
    public static void dropTables()
    {
        try
        {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE GameSessions");
            stmt.execute("DROP TABLE Accounts");
            System.out.println("GameSessions und Accounts wurden geloescht.");
            stmt.close();
            conn.close();
        } catch (SQLException ex)
        {
            System.out.println("Fehler: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Deletes the GameSession with the given Name
     * @param name Name of the GameSession to delete
     * @return true if deleting was a succcess, else false
     */
    public static Boolean deleteGameSession(String name){
        try{
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn
                    .prepareStatement("DELETE FROM GameSessions WHERE id = ?");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            System.out.println("GameSession mit Namen: '"+name+"' wurde geloescht!");
            return true;
        }catch(Exception e){
            System.out.println("Die GameSession mit Namen: '"+name+"' existiert nicht!");
            return false;
        }
    }

    /**
     * Creates Table for GameSession
     * @param conn The connection to use
     */
    public static void buildGameSessionTable(Connection conn)
    {
        try
        {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE GameSessions (id VARCHAR(100), session Blob)");
            conn.commit();
            System.out.println("GameSession-Table wurde erstellt.");
        } catch (Exception ex) {}
    }

    /**
     * Creates the Table for the Accounts
     * @param conn The connection to use
     */
    public static void buildAccountTable(Connection conn){
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Accounts (id VARCHAR(100), password VARCHAR(100))");
            conn.commit();
            System.out.println("Account-Table wurde erstellt.");
        }catch(Exception e){}
    }
}


