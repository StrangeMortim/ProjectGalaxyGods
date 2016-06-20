package GameObject;

import Action.*;
import Player.*;
import chat.Chat;
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
    private Chat sessionChat;
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
     level.update();
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
     * Entfernt alle Buffs für die uebergebene Einheit
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
        //TODO: unklar
    }

    /**
     * Entfernt ein Team aus dem Spiel.
     * @param t Team das entfernt werden soll.
     */
    @Override
    public void removeTeam(Team t) {
      teams.remove(t);
    }

    /**
     * Fuegt ein Team dem Spiel hinzu.
     * @param t Team das hinzugefuegt werden soll.
     */
    @Override
    public void addTeam(Team t) {
     teams.add(t);
    }


    /**
     * Fuegt dem Spiel eine Liste von Buffs hinzu.
     * @param b Liste von Buffs, die hinzugefuegt werden sollen.
     */
    public void addBuffs(List<Buff> b){
        buffs.addAll(b);
    }

    /**
     * Entfernt Buff aus der Liste von Buffs.
     * @param b Buff der entfernt werden soll.
     */
    public void removeBuff(Buff b){
        buffs.remove(b);
    }

    /**
     * Leitet alle noetigen Schritte fuer den Beginn eines Zuges ein.
     */
    public void startTurn(){

    };

    /**
     * Leitet alle noetigen Schritte fuer das Beenden eines Zuges ein.
     */
    public void finishTurn(){
        update();
        finish();
        save();
    }

    /**
     * Entfernt Spieler aus dem Spiel und alle seine Einheiten.
     * @param p
     */
    @Override
    public void playerLeave(Player p) {
        for(Team t : teams){
            if(t.getPlayers().contains(p)){
                t.getPlayers().remove(p);
            }
        }
        for(Field[] f:level.getFields()){
            for(Field f2 :f){
                if(f2.getCurrent().getOwner()==p){
                    f2.setCurrent(null);
                }
            }
        }
    }

    /**
     * Fuegt Spieler dem Spiel hinzu.
     * @param a Account des Spielers.
     * @param p Spieler selbst.
     * @param t und das Team zu dem er gehoert.
     * @return true, wenn er hinzugefuegt werden konnte, sonst false.
     */
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
     * Prueft, ob ein Team oder ein Spieler das Spiel gewonnen hat.
     * @return true, wenn jemand gewonnen hat.
     */
    @Override
    public boolean finish() {
    if(teams.size()==1){
        return true;
    }
        return false;
    }


    //Getter Setter
    @Override
    public Map getMap() {
        return level;
    }

    public Chat getSessionChat() {
        return sessionChat;
    }

    public void setSessionChat(Chat sessionChat) {
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
