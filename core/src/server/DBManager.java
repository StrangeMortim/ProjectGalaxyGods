package server;

import GameObject.GameSession;
import GameObject.Market;

import java.io.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;

/**
 * Diese Klasse realisiert das Speichern,Erstellen sowie Laden von GameSession- und Account-Objekten.
 * Sie nutzt dazu eine Derby Datenbank mit der sie ueber JDBC kommuniziert.
 */
public class DBManager implements Remote {
    //Verbindungsadresse der Datenbank.
    final static String DB_URL = "jdbc:derby:DerbyDB;create=true";

    /**
     * Dieser Konstruktor erstellt, bei Bedarf, fehlende Bereiche der Datenbank.
     */
    public DBManager()throws RemoteException
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
     * Laedt die GameSession mit spezifischem Namen.
     * @param sessionName Name der GameSession
     * @return GameSession mit dem jeweiligen Namen, sonst null.
     */
    public static GameSession loadSession(String sessionName)throws RemoteException{
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
     *Speichert die uebergebene GameSession.
     * @param gs zu speichernde GameSession
     * @return true, falls Speichern erfolgreich, sonst false.
     */
    public boolean saveSession(GameSession gs)throws RemoteException{

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
     * Diese Methode realisiert die Registration eines neuen Accounts.
     *
     * @param name     Name vom Account
     * @param password Passwort vom Account
     * @return true, wenn Registration geklappt hat, sonst false
     */
    public static boolean registerAccount(String name, String password)throws RemoteException{
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
     * Prueft, ob Eingaben zum Account korrekt sind.
     *
     * @param name     Name des Accounts
     * @param password Passwort des Accounts
     * @return true, wenn Pruefung erfolgreich, sonst false.
     */
    public static boolean checkAccount(String name, String password)throws RemoteException{
        if(getAccountList().matches("(.*)"+name+"(.*)")) {
try {
    Connection conn = DriverManager.getConnection(DB_URL);
    PreparedStatement sta = conn.prepareStatement(
            "SELECT password FROM Accounts WHERE id = '" + name + "'");
    ResultSet res = sta.executeQuery();
    String pw="";
    while (res.next()) {
        pw = res.getString(1);
    }
    sta.close();
    res.close();
    conn.close();
    if(pw.equals(password)){
        System.out.println("Account mit Namen: '"+name+"' existiert!");
        return true;}
}catch(Exception e){return false;}}
        return false;
    }

    /**
     * Gibt alle GameSession-Namen zurueck, die auf dem Server gespeichert sind.
     * @return Namen der GameSession-Objekte
     */
    public static String getSessionList()throws RemoteException{
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
     * Gibt alle Accountnamen zurueck, die auf dem Server gespeichert sind.
     * @return Namen der Accounts
     */
    public static String getAccountList()throws RemoteException{
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
     * Loescht alle Datenbankeintraege der GameSessions und Accounts
     */
    public static void dropTables()throws RemoteException
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
     * Loescht die GameSession mit eingegebenen Namen.
     * @param name Name des GameSession-Objektes
     * @return true, falls es geklappt hat, sonst false
     */
    public static Boolean deleteGameSession(String name)throws RemoteException{
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
     * Erstellt die Tabelle in der die GameSession-Objekte mit ihren spezifischen Namen
     * gespeichert werden sollen.
     * @param conn Verbindung zu der Datenbank
     */
    public static void buildGameSessionTable(Connection conn)throws RemoteException
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
     * Erstellt die Tabelle in der die Account-Daten gespeichert werden.
     * @param conn
     */
    public static void buildAccountTable(Connection conn)throws RemoteException {
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Accounts (id VARCHAR(100), password VARCHAR(100))");
            conn.commit();
            System.out.println("Account-Table wurde erstellt.");
        }catch(Exception e){}
    }
}


