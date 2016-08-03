package GameObject;

import Action.*;
import Player.*;
import chat.Chat;
import chat.Message;
import server.DBManager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Diese Klasse umfasst alle Daten und Relationen, die fuer das Spiel relevant sind. Sie realisiert
 * damit die Speicherung und Verwaltung des gesamten Spielzustandes.
 */
public class GameSession implements IGameSession, Serializable{

   // private static final long serialVersionUID = -3697414040192093513L;
    /**
     * Name der GameSession.
     */
    private String name = "TestSession";
    /**
     * Passwort der GameSesion.
     */
    private String password = "GameSession";
    /**
     * Liste mit den verbuendeten Spielern.
     */
    private List<Team> teams;
    /**
     * Realisiert das Spielfeld.
     */
    private IMap level;
    /**
     * Spieler der am Zug ist.
     */
    private Player active;
    /**
     * Liste mit Buffs, die aktiv sind.
     */
    private List<Buff> buffs;
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
    private int turn = 1;

    /**
     * DIe maximale Rundenanzahl
     */
    private int round = 0;
    /**
     * Die Anzahl der maximalen Spieler pro Team.
     */
    private int maxPlayersPerTeam = 2;
    /**
     * Gibt an, ob die Runde gestartet ist.
     */
    private boolean hasStarted = false;
    /**
     * Der Marktplatz des Spiels.
     */
    private Market market;
    /**
     * Max Anzahl an Spielern
     */
    private int numberOfPlayers;


    /**
     * Das Gewinnerteam
     */
    private Team winner;

    public GameSession(){
        identities = new HashMap<>();
        market = new Market();
        buffs  = new ArrayList<Buff>();
        teams = new ArrayList<>();
        currentTurn = new ActionProcessor(this);
        sessionChat = new Chat();
        ArrayList<Player> players= new ArrayList<>();
        teams.add(new Team(players,"Rot"));
        players= new ArrayList<>();
        teams.add(new Team(players,"Blau"));
            level = new Map("NoName", 4, 2, this);
        try {
            level.init();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ruft bei allen Klassen, die von der GameSession verwaltet werden, die Update-Methode auf.
     */
    @Override
    public void update(){
        try {
            level.update();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Iterator<Buff> it = buffs.iterator();
        while (it.hasNext())
            if(it.next().execute())
                it.remove();

        for(Team t: teams)
            for(Player p: t.getPlayers()){
                it = p.getTemporaryBuffs().iterator();
                while (it.hasNext())
                    if(it.next().execute())
                        it.remove();
            }

        //Currently not used
        buffs.addAll(currentTurn.execute());
    }

    /**
     *
     * @param u
     */
    @Override
    public void registerUnit(Unit u) {
        Buff tmp = null;
        for(Buff b: u.getOwner().getPermaBuffs()){
            tmp = b.getPersonalCopy(u);
            tmp.execute();
        }

        for(Buff b2: u.getOwner().getTemporaryBuffs()){
            tmp = b2.getPersonalCopy(u);
            buffs.add(tmp);
            tmp.execute();
            /*TODO use global turn counter?*/
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
    @Override
    public void addBuffs(List<Buff> b){
        buffs.addAll(b);
    }

    @Override
    public void addSingleBuff(Buff b) throws RemoteException {
        if(b == null)
            throw new IllegalArgumentException("Buff ist null - addSingleBuff");

        buffs.add(b);
    }

    /**
     * Entfernt Buff aus der Liste von Buffs.
     * @param b Buff der entfernt werden soll.
     */
    @Override
    public void removeBuff(Buff b){
        buffs.remove(b);
    }

    /**
     * Leitet alle noetigen Schritte fuer den Beginn eines Zuges ein.
     */
    @Override
    public void startTurn(){

    };

    /**
     * Leitet alle noetigen Schritte fuer das Beenden eines Zuges ein.
     */
    @Override
    public void finishTurn(Player p){
        if(p == active) {
            List<Player> player = new ArrayList<>();
            for(Team t:teams){player.addAll(t.getPlayers());}
            int index = (player.indexOf(p)+1) % player.size();
            p.setTurn(false);
            setActive(player.get(index));

            //if it's not the first round distribute Gold to the next player
            if(turn > player.size())
                player.get(index).getRessources()[Constants.GOLD] += Constants.GOLD_RES_VALUE + player.get(index).getRessourceBoni()[Constants.GOLD];

            update();
            try {
                finish();
                save();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            turn++;
        }
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
        try {
            for(Field[] f:level.getFields()){
                for(Field f2 :f){
                    if(f2.getCurrent().getOwner()==p){
                        f2.setCurrent(null);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
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
    public Player playerJoin(Account a, Player p, Team t) {
        if(a == null)
            throw new IllegalArgumentException("Account ist null");

        if(identities.containsKey(a))
            return identities.get(a);
        else if(p == null || t == null)
            throw new IllegalArgumentException("Player or Team is null");
        int pCounter=0;
        for(Team t2: teams){
            for(Player pCount:t2.getPlayers()){
                pCounter++;
            }
            if(t2 == t){
                try {
                    p.setTeam(t2);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                t2.getPlayers().add(p);
                identities.put(a, p);
                level.addBase(p, pCounter);
                return p;
            }

        }

        teams.add(t);
        t.getPlayers().add(p);

        identities.put(a, p);
        level.addBase(p, pCounter);
        return p;
    }

    @Override
    public void showSessionDetails(){
        GameSession session=this;

        String stats="";
        try {
            stats+="\n"+"Name: "+session.getName();
            stats+="\n"+"Passwort: "+session.getPassword();
            stats+="\n"+"Anzahl der Spieler: "+session.getNumberOfPlayers();
            stats+="\n"+"--------------------------------------------------"
                    +"\n"+"Nachrichten: ";
            for(Message m: session.getSessionChat().getBacklog()){
                stats+="\n"+m.getContent();
            }
            stats+="\n"+"--------------------------------------------------"
                    +"\n"+"Teams: ";
            for(Team t : session.getTeams()){
                stats+="\n Team-Name: "+t.getColor();
                for(Player p: t.getPlayers()){
                    stats+="  \n Spielername: "+p.getAccount().getName();
                    stats+="  Holz : "+p.getRessources()[0];
                    stats+="  Eisen : "+p.getRessources()[1];
                    stats+="  Gold : "+p.getRessources()[2];
                    stats+="  Hat Markt: "+p.getMarket();
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(stats);
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
        winner = teams.get(0);
        return true;
    }
        return false;
    }


    //Getter Setter
    @Override
    public IMap getMap() {
        return level;
    }

    @Override
    public int getNumberOfPlayers() throws RemoteException {
        return numberOfPlayers;
    }

    @Override
    public void setNumberOfPlayers(int number) throws RemoteException {
       numberOfPlayers=number;
    }

    @Override
    public boolean registerBuff(Buff b) throws RemoteException {
        Field tmp[][] = level.getFields();

        for(int i=Constants.WOOD; i<=Constants.MANA; ++i)
           if(b.getPlayer().getRessources()[i] < b.getBuffInfo().getBuffCost()[i])
               return false;

        for(int j=Constants.WOOD; j<=Constants.MANA;++j)
            b.getPlayer().getRessources()[j] -= b.getBuffInfo().getBuffCost()[j];

        if(b.getClass().isAssignableFrom(Buff.class)) {
            Buff current = null;
            Unit currentUnit = null;
            if (b.isPermanent()) {
                b.getPlayer().addPermaBuff(b);
                for (Field[] fArray : tmp) {
                    for (Field f : fArray) {
                        currentUnit = f.getCurrent();
                        if (currentUnit != null && b.appliesForUnit(currentUnit)) {
                            current = b.getPersonalCopy(currentUnit);
                            current.execute();
                        }
                    }
                }
            } else {
                b.getPlayer().addTemporaryBuff(b);
                for (Field[] fArray : tmp) {
                    for (Field f : fArray) {
                        currentUnit = f.getCurrent();
                        if (currentUnit != null && b.appliesForUnit(currentUnit)) {
                            current = b.getPersonalCopy(currentUnit);
                            current.execute();
                            buffs.add(current);
                        }
                    }
                }
            }
        } else {
            b.execute();
        }

        return true;
    }

    @Override
    public Chat getSessionChat()throws RemoteException {
        return sessionChat;
    }

    @Override
    public void setSessionChat(Chat sessionChat) {
        this.sessionChat = sessionChat;
    }

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public void setTurn(int turn){
        this.turn = turn;
    }

    @Override
    public boolean isHasStarted(){
        return hasStarted;
    }

    @Override
    public void setHasStarted(boolean hasStarted){
        this.hasStarted = hasStarted;
    }

    @Override
    public int getMaxPlayersPerTeam(){
        return maxPlayersPerTeam;
    }

    @Override
    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    @Override
    public List<Team> getTeams(){
        return teams;
    }

    @Override
    public void setTeams(List<Team> teams)  {
        this.teams = teams;
    }

    @Override
    public IMap getLevel() {
        return level;
    }

    @Override
    public void setLevel(Map level) {
        this.level = level;
    }

    @Override
    public Player getActive(){
        return active;
    }

    @Override
    public void setActive(Player active){
        this.active = active;
        active.setTurn(true);
    }

    @Override
    public List<Buff> getBuffs(){
        return buffs;
    }

    @Override
    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }

    @Override
    public HashMap<Account, Player> getIdentities()  {
        return identities;
    }

    @Override
    public void setIdentities(HashMap<Account, Player> identities) {
        this.identities = identities;
    }

    @Override
    public ActionProcessor getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public void setCurrentTurn(ActionProcessor currentTurn) {
        this.currentTurn = currentTurn;
    }

    @Override
    public Market getMarket(){
        return market;
    }

    @Override
    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name){
        if(name.length()>100){
            this.name=name.substring(0,99);
        }else{
        this.name = name;
        }
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    public void setRound(int round) {
        this.round = round;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
