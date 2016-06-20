package GameObject;

import Action.*;
import Player.*;
import chat.Chat;
import chat.Message;
import server.DBManager;

import java.io.Serializable;
import java.rmi.RemoteException;
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
        Buff tmp = null;
        for(Research r: u.getOwner().getPermaBuffs()){
            tmp = new Buff(u, null, u.getOwner());
            tmp.setSource(r);
            tmp.execute();
        }

        for(Research r2: u.getOwner().getTemporaryBuffs()){
            tmp = new Buff(u, null, u.getOwner());
            tmp.setSource(r2);
            buffs.add(tmp);
            /*TODO use global round counter?*/
        }
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
    public void finishTurn()throws RemoteException{
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
    public boolean save() throws RemoteException{
        return new DBManager().saveSession(this);
    }

    /**
     * Prueft, ob ein Team oder ein Spieler das Spiel gewonnen hat.
     * @return true, wenn jemand gewonnen hat.
     */
    @Override
    public boolean finish()throws RemoteException {
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

    public Chat getSessionChat()throws RemoteException {
        return sessionChat;
    }

    public void setSessionChat(Chat sessionChat)throws RemoteException {
        this.sessionChat = sessionChat;
    }

    public int getRound()throws RemoteException {
        return round;
    }

    public void setRound(int round)throws RemoteException {
        this.round = round;
    }

    public boolean isHasStarted()throws RemoteException {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted)throws RemoteException {
        this.hasStarted = hasStarted;
    }

    public int getMaxPlayersPerTeam()throws RemoteException {
        return maxPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam)throws RemoteException {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public List<Team> getTeams()throws RemoteException {
        return teams;
    }

    public void setTeams(List<Team> teams)throws RemoteException {
        this.teams = teams;
    }

    public Map getLevel()throws RemoteException {
        return level;
    }

    public void setLevel(Map level)throws RemoteException {
        this.level = level;
    }

    public Player getActive()throws RemoteException {
        return active;
    }

    public void setActive(Player active)throws RemoteException {
        this.active = active;
    }

    public List<Buff> getBuffs()throws RemoteException {
        return buffs;
    }

    public void setBuffs(List<Buff> buffs)throws RemoteException {
        this.buffs = buffs;
    }

    public HashMap<Account, Player> getIdentities()throws RemoteException {
        return identities;
    }

    public void setIdentities(HashMap<Account, Player> identities)throws RemoteException {
        this.identities = identities;
    }

    public ActionProcessor getCurrentTurn()throws RemoteException {
        return currentTurn;
    }

    public void setCurrentTurn(ActionProcessor currentTurn)throws RemoteException {
        this.currentTurn = currentTurn;
    }

    public Market getMarket()throws RemoteException {
        return market;
    }

    public void setMarket(Market market)throws RemoteException {
        this.market = market;
    }

    public String getName()throws RemoteException {
        return name;
    }

    public void setName(String name)throws RemoteException {
        if(name.length()>100){
            this.name=name.substring(0,99);
        }else{
        this.name = name;
        }
    }
}
