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
    private Map level;
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
        teams.add(new Team(players,"Blau"));
            level = new Map("NoName", 4, 2, this);
            level.init();
    }

    /**
     * Ruft bei allen Klassen, die von der GameSession verwaltet werden, die Update-Methode auf.
     */
    @Override
    public void update(){
        level.update();

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
    public void finishTurn(Player p)throws RemoteException{
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
            finish();
            save();
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
    public Map getMap() {
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


    public Chat getSessionChat()throws RemoteException {
        return sessionChat;
    }

    public void setSessionChat(Chat sessionChat)throws RemoteException {
        this.sessionChat = sessionChat;
    }

    public int getTurn()throws RemoteException {
        return turn;
    }

    public void setTurn(int turn)throws RemoteException {
        this.turn = turn;
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
        active.setTurn(true);
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

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
