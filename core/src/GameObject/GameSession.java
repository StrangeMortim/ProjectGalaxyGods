package GameObject;

import Action.*;
import Player.*;
import chat.Message;
import server.DBManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Diese Klasse umfasst alle Daten und Relationen, die fuer das Spiel relevant sind. Sie realisiert
 * damit die Speicherung und Verwaltung des gesamten Spielzustandes.
 */
public class GameSession implements IGameSession, Serializable{
    /**
     * Name der GameSession.
     */
    private String name;
    /**
     * Liste mit den verbuendeten Spielern.
     */
    private List<Team> teams;
    /**
     * Realisiert das Spielfeld.
     */
    private Map level;
    /**
     * Spieler der am Zug ist.
     */
    private Player active;
    /**
     * Liste mit Buffs, die aktiv sind.
     */
    private List<Buff>buffs;
    /**
     * Liste mit den Accounts und zugehoerigen Spielern.
     */
    private HashMap<Account,Player> identities;
    /**
     * Der Actionprocessor der derzeitigen Runde.
     */
    private ActionProcessor currentTurn;
    /**
     * Der Chat des Spiels.
     */
    private int sessionChat;
    /**
     * Die Spielrunde in der sich die Spieler befinden.
     */
    private int round;
    /**
     * Die Anzahl der maximalen Spieler pro Team.
     */
    private int maxPlayersPerTeam;
    /**
     * Gibt an, ob die Runde gestartet ist.
     */
    private boolean hasStarted;
    /**
     * Der Marktplatz des Spiels.
     */
    private Market market;

    /**
     * Ruft bei allen Klassen, die von der GameSession verwaltet werden, die Update-Methode auf.
     */
    @Override
    public void update(){

    }

    /**
     *
     * @param u
     */
    @Override
    public void registerUnit(Unit u) {
        for(Buff b: buffs)
            if(b.getPlayer() == u.getOwner() && b.appliesForUnit(u.getType()))
                buffs.add(b.getPersonalCopy(u));
    }

    /**
     * Entfernt alle Buffs für die übergebene Einheit
     *
     * @param u
     */
    @Override
    public void removeUnit(Unit u) {
        Iterator it = buffs.iterator();
        while (it.hasNext()){
            if(((Action)it.next()).getOrigin() == u)
                it.remove();
        }
    }

    /**
     * Erstellt eine Nachricht, die ausgewaehlte Spieler sehen koennen.
     * @param m Nachricht die versendet werden soll.
     */
    @Override
    public void sendMessage(Message m) {

    }

    /**
     * Entfernt ein Team aus dem Spiel.
     * @param t Team das entfernt werden soll.
     */
    @Override
    public void removeTeam(Team t) {

    }

    /**
     * Fuegt ein Team dem Spiel hinzu.
     * @param t Team das hinzugefuegt werden soll.
     */
    @Override
    public void addTeam(Team t) {

    }


    /**
     * Fuegt dem Spiel eine Liste von Buffs hinzu.
     * @param b Liste von Buffs, die hinzugefuegt werden sollen.
     */
    public void addBuffs(List<Buff> b){}

    /**
     * Entfernt Buff aus der Liste von Buffs.
     * @param b Buff der entfernt werden soll.
     */
    public void removeBuff(Buff b){}

    /**
     * Leitet alle noetigen Schritte fuer den Beginn eines Zuges ein.
     */
    public void startTurn(){};

    /**
     * Leitet alle noetigen Schritte fuer das Beenden eines Zuges ein.
     */
    public void finishTurn(){}

    @Override
    public void playerLeave(Player p) {

    }

    @Override
    public boolean playerJoin(Account a, Player p, Team t) {
        return false;
    }


    /**
     * Speichert dieses Objekt in der Datenbank.
     * @return true, wenn es geklappt hat, sonst false
     */
    @Override
    public boolean save() {
        return new DBManager().saveSession(this);
    }

    /**
     * 
     * @return
     */
    @Override
    public boolean finish() {
        return false;
    }




    //Getter Setter
    @Override
    public Map getMap() {
        return level;
    }

    public int getSessionChat() {
        return sessionChat;
    }

    public void setSessionChat(int sessionChat) {
        this.sessionChat = sessionChat;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Map getLevel() {
        return level;
    }

    public void setLevel(Map level) {
        this.level = level;
    }

    public Player getActive() {
        return active;
    }

    public void setActive(Player active) {
        this.active = active;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }

    public HashMap<Account, Player> getIdentities() {
        return identities;
    }

    public void setIdentities(HashMap<Account, Player> identities) {
        this.identities = identities;
    }

    public ActionProcessor getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(ActionProcessor currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length()>100){
            this.name=name.substring(0,99);
        }else{
        this.name = name;
        }
    }
}
