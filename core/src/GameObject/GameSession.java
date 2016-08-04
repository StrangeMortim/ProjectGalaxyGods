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
    private int maxPlayers = 2;
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
    private int numberOfPlayers=0;

    private int currentId = 1;

    private HashMap<Integer,Object> archive = new HashMap<>();

    private Object selected = null;


    /**
     * Das Gewinnerteam
     */
    private Team winner;

    public GameSession(){
        identities = new HashMap<>();
        market = new Market(this);
        buffs  = new ArrayList<Buff>();
        teams = new ArrayList<>();
        sessionChat = new Chat(this);
        ArrayList<Player> players= new ArrayList<>();
        teams.add(new Team(players,"Rot",this));
        players= new ArrayList<>();
        teams.add(new Team(players,"Blau",this));
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
                p.getRessources()[Constants.GOLD] += Constants.GOLD_RES_VALUE+p.getRessourceBoni()[Constants.GOLD];
                it = p.getTemporaryBuffs().iterator();
                while (it.hasNext())
                    if(it.next().execute())
                        it.remove();
            }

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
        Player p = u.getOwner();
        for(int i=Constants.WOOD; i<=Constants.MANA; i++){
            if(p.getTeam().getCheck()[i] >= (int)(u.getType().getRessourceCost()[i]*Constants.UNIT_RECYCLING_MODIFIER)) {
                p.getRessources()[i] += (int) (u.getType().getRessourceCost()[i] * Constants.UNIT_RECYCLING_MODIFIER);
                p.getTeam().getCheck()[i] -= (int) (u.getType().getRessourceCost()[i] * Constants.UNIT_RECYCLING_MODIFIER);
            }else{
                p.getRessources()[i] += p.getTeam().getCheck()[i];
                p.getTeam().getCheck()[i] = 0;
            }
        }
    }

    /**
     * Erstellt eine Nachricht, die ausgewaehlte Spieler sehen koennen.
     */
    @Override
    public void sendMessage(int playerId, boolean team, String content) {
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("addTeamRessources fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return;
        }

        if(team){
            ((Player)player).getTeam().getChat().addMessage(((Player)player),content);
        }else {
            sessionChat.addMessage(((Player)player),content);
        }
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
    public void addSingleBuff(Buff b)  {
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
    public void finishTurn(int playerId){
        try {
            Player p = (Player) archive.get(playerId);
            if (p != null && p == active) {
                List<Player> player = new ArrayList<>();
                for (Team t : teams) {
                    player.addAll(t.getPlayers());
                }
                int index = (player.indexOf(p) + 1) % player.size();
                p.setTurn(false);
                setActive(player.get(index));

                //if it's not the first round distribute Gold to the next player
                if (turn > player.size())
                    player.get(index).getRessources()[Constants.GOLD] += Constants.GOLD_RES_VALUE + player.get(index).getRessourceBoni()[Constants.GOLD];

                update();
                finish();
                save();


                turn++;
            } else if(p == null){
                System.out.println("Die Id: " + playerId + " gehoert zu keinem Objekt");
            }
        }catch(ClassCastException e){
            e.printStackTrace();
            System.out.println("Die Id: " + playerId + " gehoert zu keinem Spieler");
        }
    }

    /**
     * Entfernt Spieler aus dem Spiel und alle seine Einheiten.
     * @param playerId
     */
    @Override
    public void playerLeave(int playerId) {
        try {
            Player p = (Player) archive.get(playerId);
            for (Team t : teams) {
                if (t.getPlayers().contains(p)) {
                    t.getPlayers().remove(p);
                }
            }

            for (Field[] f : level.getFields()) {
                for (Field f2 : f) {
                    if (f2.getCurrent().getOwner() == p) {
                        f2.setCurrent(null);
                    }
                }
            }
        }catch(ClassCastException e){
            e.printStackTrace();
            System.out.println("Die Id: " + playerId + " gehoert zu keinem Spieler");
        } catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Die Id: " + playerId + " gehoert zu keinem Objekt");
        }
    }

    /**
     * Fuegt Spieler dem Spiel hinzu.
     * @param a Account des Spielers.
     * @param teamColor das Team zu dem er gehoert.
     * @return true, wenn er hinzugefuegt werden konnte, sonst false.
     */
    @Override
    public int playerJoin(Account a, String teamColor) {
        if(a == null)
            throw new IllegalArgumentException("Account ist null");
        numberOfPlayers++;



        for(Account a2: this.identities.keySet()){
            if(a2.getName().equals(a.getName())){
                return identities.get(a2).getId();
            }
        }

        int pCounter=0;
        Player p = new Player(a, this);
        for(Team t2: teams){
            for(Player pCount:t2.getPlayers()){
                pCounter++;
            }
            if(t2.getColor().equals(teamColor)){
                    p.setTeam(t2);
                t2.addPlayer(p);
                identities.put(a, p);
                sessionChat.addParticipant(p);
                level.addBase(p, pCounter);
                if(numberOfPlayers >= maxPlayers)
                    active = p;
                return p.getId();
            }

        }

        Team t = new Team(new ArrayList<Player>(),teamColor,this);
        teams.add(t);
        t.addPlayer(p);
        p.setTeam(t);
        identities.put(a, p);
        sessionChat.addParticipant(p);
        level.addBase(p, pCounter);
        if(numberOfPlayers >= maxPlayers)
            active = p;
        return p.getId();

    }

    @Override
    public void showSessionDetails(){
        GameSession session=this;

        String stats="";
            stats+="\n"+"Name: "+session.getName();
            stats+="\n"+"Passwort: "+session.getPassword();
            stats+="\n"+"Anzahl der Spieler: "+session.getNumberOfPlayers();
            stats+="\n"+"Der aktive Spieler in der Session: "+session.getActive();
            stats+="\n"+"Die Anzahl der Runden der Session: "+session.getTurn();
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
                    stats+="  \n Objektname: "+p.toString();
                    stats+="  Holz : "+p.getRessources()[0];
                    stats+="  Eisen : "+p.getRessources()[1];
                    stats+="  Gold : "+p.getRessources()[2];
                    stats+="  Hat Markt: "+p.getMarket();
                }
                stats+="\n"+"--------------------------------------------------"
                        +"\n"+"Nachrichten des Teams: ";
                for(Message m : t.getChat().getBacklog()){
                    stats+="\n"+m.getContent();
                }
                stats+="\n"+"--------------------------------------------------";
            }
        System.out.println(stats);
    }


    /**
     * Speichert dieses Objekt in der Datenbank.
     * @return true, wenn es geklappt hat, sonst false
     */
    @Override
    public boolean save(){
        return new DBManager().saveSession(this);
    }

    /**
     * Prueft, ob ein Team oder ein Spieler das Spiel gewonnen hat.
     * @return true, wenn jemand gewonnen hat.
     */
    @Override
    public boolean finish() {
    if(teams.size()==1){
        winner = teams.get(0);
        return true;
    }
        return false;
    }


    //Getter Setter
    @Override
    public int getMap() {
        return level.getId();
    }

    @Override
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    @Override
    public void setNumberOfPlayers(int number) {
       numberOfPlayers=number;
    }

    @Override
    public boolean registerBuff(int originID, int playerId, BuffInfo info) {
        try {
            Player player = (Player) archive.get(playerId);
            if (player != null) {
                Buff b = null;
                switch (info) {
                    case EMPOWER_SHIELD:
                        b = new EmpowerShield(player.getHero(), player, this);
                        break;
                    case REDUCED_UNIT_COST:
                        b = new ReduceUnitCosts(null, player, this);
                        break;
                    default:
                        Unit u = (Unit)archive.get(originID);
                        b = new Buff(u, player, info, this);
                        break;
                }


                Field tmp[][] = level.getFields();

                for (int i = Constants.WOOD; i <= Constants.MANA; ++i)
                    if (b.getPlayer().getRessources()[i] < b.getBuffInfo().getBuffCost()[i])
                        return false;

                for (int j = Constants.WOOD; j <= Constants.MANA; ++j)
                    b.getPlayer().getRessources()[j] -= b.getBuffInfo().getBuffCost()[j];

                if (b.getClass().isAssignableFrom(Buff.class)) {
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
        }catch (ClassCastException e){
            e.printStackTrace();
        } catch (NullPointerException e2){
            e2.printStackTrace();
        }
        return false;
    }

    @Override
    public Chat getSessionChat() {
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

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public List<Team> getTeams(){
        return teams;
    }


    @Override
    public int getActive(){
        if(active==null)
            return -1;
        else
        return active.getId();
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
    public int getMarket(){
            return market.getId();
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

    @Override
    public Player getPlayerPerName(String name){
        for(java.util.Map.Entry<Account,Player> entry: identities.entrySet())
            if(entry.getKey().getName().equals(name))
                return entry.getValue();

        return null;
    }


    ////////////////////////////////////////////
    @Override
    public int registerObject(Object toRegister) throws RemoteException {
        if(toRegister == null)
            throw new IllegalArgumentException("Cannot Register null");

        int id = this.currentId;
        currentId++;
        archive.put(id,toRegister);
        return id;
    }

    @Override
    public int select(int x, int y) throws RemoteException {
        if(x < 0 || x > Constants.FIELDXLENGTH || y < 0 || y > Constants.FIELDYLENGTH){
            selected = null;
            return -1;
        } else {
            selected = level.getField(x,y).select();
            if(selected instanceof Field)
                return ((Field)selected).getId();
            else if (selected instanceof Unit)
                return ((Unit)selected).getId();
            else
            throw new IllegalArgumentException("Das Feld hat ein unerlaubtes Objekt als current erhalten");
        }
    }

    @Override
    public int[] getSpriteIndex(int x, int y) throws RemoteException {
        if(x < 0 || x > Constants.FIELDXLENGTH || y < 0 || y > Constants.FIELDYLENGTH)
            return new int[3];

        Object tmp = level.getField(x,y).select();
        if(tmp == null){
            System.out.println("Das Feld (" + x + ","+y+") gibt null zurueck");
            return new int[3];
        } else if(tmp instanceof Field){
            return new int[]{((Field)tmp).getSpriteIndex(),-1,-1};
        } else if(tmp instanceof Unit){
            return new int[]{level.getField(x,y).getSpriteIndex(),((Unit)tmp).getSpriteIndex(),((Unit)tmp).getId()};
        }

        System.out.println("Das Feld: (" + x+","+y + ") besitzt kein Sprite");
        return new int[3];
    }

    @Override
    public List<String> getInformation(int id) throws RemoteException {
        Object tmp = archive.get(id);
        if(tmp == null) {
            //System.out.println("Id: " + id + " besitzt kein Objekt");
            return new ArrayList<>();
        } else if(tmp instanceof Unit){
            return ((Unit)tmp).getInfo();
        }

        ArrayList<String> failSafe = new ArrayList<>();
        for(int i=0; i<8;++i)
            failSafe.add("-1/-1");

        System.out.println("Die id: " + id + " gehoert zu keiner Einheit");
        return failSafe;
    }

    @Override
    public int[] getRessources(int playerId) throws RemoteException {
        Object tmp = archive.get(playerId);

        if(tmp == null) {
            System.out.println("Id: " + playerId + " besitzt kein Objekt");
            return new int[0];
        } else if(tmp instanceof Player){
            int[] result = ((Player)tmp).getRessources();
            return result;
        }

        System.out.println("Die id: " + playerId + " gehoert zu keinem Spieler");
        return new int[0];
    }

    @Override
    public boolean isSelectedOwner(int playerId) throws RemoteException {
        if(selected == null)
            return false;

        Object tmp = archive.get(playerId);

        if(tmp == null) {
            System.out.println("Id: " + playerId + " besitzt kein Objekt");
            return false;
        }else if(tmp instanceof Player && selected instanceof Unit){
            //System.out.println("Ist selected-Owner-Id und tmp id gleich?: " + (((Player)tmp).getId()==((Unit)selected).getOwner().getId()));
            return ((Unit)selected).getOwner() == tmp;
        }

        return false;
    }

    @Override
    public boolean checkMarket(int playerId){
        Object tmp = archive.get(playerId);

        if(tmp == null) {
            System.out.println("Id: " + playerId + " besitzt kein Objekt");
            return false;
        } else if(tmp instanceof Player){
            return ((Player)tmp).getMarket();
        }

        System.out.println("Die id: " + playerId + " gehoert zu keinem Spieler");
        return false;
    }

    @Override
    public boolean checkPathFull(int playerId, int treePath){
        Object tmp = archive.get(playerId);

        if(tmp == null) {
            System.out.println("Id: " + playerId + " besitzt kein Objekt");
            return false;
        } else if(tmp instanceof Player){
            switch (treePath){
                case Constants.TECHTREE_STEEL:
                    return ((Player)tmp).getTechTree().isSteelFull();
                case Constants.TECHTREE_MAGIC:
                    return ((Player)tmp).getTechTree().isMagicFull();
                case Constants.TECHTREE_CULTURE:
                    return ((Player)tmp).getTechTree().isCultureFull();
            }
        }

        System.out.println("Die id: " + playerId + " gehoert zu keinem Spieler");
        return false;
    }

    @Override
    public boolean hasSelectedCurrent(){
        if(selected instanceof Field)
            return ((Field)selected).getCurrent() == null;

        System.out.println("hasSelectedCurrent wurde fuer nicht-Field Objekt aufgerufen");
        return false;
    }

    @Override
    public boolean isSelectedClassOf(Selectable classType){
        switch (classType){
            case FIELD:
                return  selected instanceof Field;
            case UNIT:
                return selected instanceof Unit;
            case BASE:
                return selected instanceof Base;
            case HERO:
                return selected instanceof Hero;
            default:
                return false;
        }
    }

    @Override
    public boolean isSelectedRessourceType(int type){
        if(!(selected instanceof Field)){
            System.out.println("Ressource Check wurde fuer ein nicht-Field Objekt aufgerufen");
            return false;
        }

        return ((Field)selected).getResType() == type;
    }

    @Override
    public int getSelectedX(int id){
        Object tmp = archive.get(id);

        if(tmp == null) {
            System.out.println("Id: " + id + " besitzt kein Objekt");
            return -1;
        } else if(tmp instanceof Field){
            return ((Field)tmp).getXPos();
        } else if(tmp instanceof Unit){
            return ((Unit)tmp).getField().getXPos();
        }

        System.out.println("Die id: " + id + " gehoert zu keinem Feld oder Einheit");
        return -1;
    }

    @Override
    public int getSelectedY(int id){
        Object tmp = archive.get(id);

        if(tmp == null) {
            System.out.println("Id: " + id + " besitzt kein Objekt");
            return -1;
        } else if(tmp instanceof Field){
            return ((Field)tmp).getYPos();
        } else if(tmp instanceof Unit){
            return ((Unit)tmp).getField().getYPos();
        }

        System.out.println("Die id: " + id + " gehoert zu keinem Feld oder Einheit");
        return -1;
    }

    @Override
    public boolean checkHasSelectedUnit(UnitType type){
        if(selected == null)
            return false;

        if(selected instanceof Base)
            return ((Base)selected).getAvaibleUnits().contains(type);
        else{
            System.out.println("Unit check wurde fuer ein nicht-Base Objekt aufgerufen");
            return false;
        }
    }

    @Override
    public boolean checkSelectedBuildingFinished(Building b){
        if(selected == null)
            return false;

        if(selected instanceof Base) {
            switch (b){
                case LABOR:
                    return ((Base)selected).getLabRoundsRemaining() == Constants.FINISHED;
                case CASERNE:
                    return ((Base)selected).getCaserneRoundsRemaining() == Constants.FINISHED;
                default:
                    return false;
            }

        }else{
            System.out.println("Building check wurde fuer ein nicht-Base Objekt aufgerufen");
            return false;
        }
    }

    @Override
    public boolean checkIsBuilding(Building b){
        if(selected == null)
            return false;

        if(selected instanceof Base) {
            switch (b){
                case LABOR:
                    return ((Base)selected).getLabRoundsRemaining() != Constants.NONE_OR_NOT_SET;
                case CASERNE:
                    return ((Base)selected).getCaserneRoundsRemaining() != Constants.NONE_OR_NOT_SET;
                default:
                    return false;
            }

        }else{
            System.out.println("Building check wurde fuer ein nicht-Base Objekt aufgerufen");
            return false;
        }
    }

    @Override
    public boolean checkWalkable(int x, int y){
        return level.getField(x,y).getWalkable();
    }

    @Override
    public int[] getTeamRessources(int playerId){
        Object tmp = archive.get(playerId);

        if(tmp == null) {
            System.out.println("Id: " + playerId + " besitzt kein Objekt");
            return new int[4];
        }else if(tmp instanceof Player){
            try {
                return ((Player) tmp).getTeam().getCheck();
            } catch (NullPointerException e){
                e.printStackTrace();
                System.out.println("Das Team von: " + playerId + " mit Spielernamen: " +((Player)tmp).getAccount().getName() + " ist null");
                return new int[4];
            }
        }

        System.out.println("getTeamRessources wurde fuer einen nicht-Spieler ausgefuehrt");
        return new int[4];
    }

    @Override
    public String getPlayerName(int playerId){
        Object tmp = archive.get(playerId);

        if(tmp instanceof Player)
            return ((Player)tmp).getAccount().getName();

        return "Not a Player or not existent";
    }

    @Override
    public List<String> getChatBackLog(int playerId, boolean team){
        Object player = archive.get(playerId);
        List<String> result = new ArrayList<>();

        if(!(player instanceof Player)){
            System.out.println("Der Chat fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return result;
        }

        List<Message> backLog = null;

        if(team){
            backLog = ((Player)player).getTeam().getChat().getBacklog();
        }else {
            backLog = sessionChat.getBacklog();
        }

        for(Message m: backLog)
            if(m.getVisibleForAll() || m.getVisibleFor().contains(player))
                result.add(m.getContent());

        return result;
    }

    @Override
    public int[] getMarketInfo(){
        return market.getMarketInfo();
    }

    @Override
    public boolean addTeamRessources(int playeriD, int ressourceType, int amount){
        Object player = archive.get(playeriD);

        if(!(player instanceof Player)){
            System.out.println("addTeamRessources fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        switch (ressourceType){
            case Constants.WOOD:
                if(((Player)player).getRessources()[Constants.WOOD]<amount){
                    return false;
                } else {
                    ((Player)player).getRessources()[Constants.WOOD]-=amount;
                    ((Player)player).getTeam().getCheck()[Constants.WOOD] += amount;
                    return true;
                }
            case Constants.IRON:
                if(((Player)player).getRessources()[Constants.IRON]<amount){
                    return false;
                } else {
                    ((Player)player).getRessources()[Constants.IRON]-=amount;
                    ((Player)player).getTeam().getCheck()[Constants.IRON] += amount;
                    return true;
                }
            case Constants.GOLD:
                if(((Player)player).getRessources()[Constants.GOLD]<amount){
                    return false;
                } else {
                    ((Player)player).getRessources()[Constants.GOLD]-=amount;
                    ((Player)player).getTeam().getCheck()[Constants.GOLD] += amount;
                    return true;
                }
                default:
                    return false;
        }
    }

    @Override
    public boolean createUnit(int selectedId, UnitType type){
        if(selected instanceof Base){
            if(((Base)selected).getId() != selectedId){
                System.out.println("Client selected ID und selected sind nicht mehr synchron");
                return false;
            }

            return ((Base)selected).createUnit(type);
        }

        System.out.println("Create unit wurde auf einem nicht-Base Objekt aufgerufen");
        return false;
    }

    @Override
    public boolean buildOrAbortBuildingOnSelected(int playerId, int selectedId,Building building, boolean abort){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("Bau Methode wurde fuer ein unpassendes Objekt aufgerufen");
            return false;
        }

        if(!(selected instanceof Base) && !(selected instanceof Field))
            if(((Base)selected).getId() != selectedId){
                System.out.println("Client selected ID und selected sind nicht mehr synchron");
                return false;
            }

            System.out.println("Lv1");
            if(selected instanceof Field){
                System.out.println("Lv2");
                if(abort){
                    switch (building){
                        case BASE:
                            return ((Field)selected).abortBuild(((Player)player));
                        default:
                            return false;
                    }
                }else{
                    switch (building){
                        case BASE:
                            return ((Field)selected).buildBase((Player)player);
                        case MINE:
                            return ((Field)selected).buildMine(((Player)player));
                        default:
                            return false;
                    }
                }
            } else if(selected instanceof Base) {
                System.out.println("Funktioniert bis hierhin");
                if (abort) {
                    switch (building) {
                        case LABOR:
                            ((Base) selected).abortLab();
                            return true;
                        case CASERNE:
                            ((Base) selected).abortCaserne();
                            return true;
                        default:
                            return false;
                    }
                } else {
                    switch (building) {
                        case MARKET:
                            return ((Base)selected).buildMarket();
                        case LABOR:
                            return ((Base) selected).buildLab();
                        case CASERNE:
                            return ((Base) selected).buildCaserne();
                        default:
                            return false;
                    }
                }
            }


        System.out.println("Build Or Abort wurde auf einem nicht-Base Objekt aufgerufen");
        return false;
    }

    @Override
    public boolean activateHeroPower(int playerId, boolean left){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("activateHeroPower fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        if(active == player) {
            if (left)
                return ((Player) player).getHero().getLeftHand().execute();
            else
                return ((Player) player).getHero().getRightHand().execute();
        }

        return false;
    }

    @Override
    public int getHero(int playerId){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("activateHeroPower fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return -1;
        }

        return ((Player)player).getHero().getId();
    }

    @Override
    public boolean hasNearUnits(int x, int y){
        return !(level.getField(x,y).getNearUnits().isEmpty());
    }

    @Override
    public boolean researchOnSelected(int selectedId, Research r){
        if(!(selected instanceof Base) || ((Base)selected).getId() != selectedId){
                System.out.println("Client selected ID und selected sind nicht mehr synchron");
                return false;
            }

       return Research.RESEARCH_ARCHER.research((Base) selected);
    }

    @Override
    public boolean buyOnMarket(int playerId, int type, int amount){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("buyOnMarket fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return market.buy((Player)player,type,amount);
    }

    @Override
    public boolean sellOnMarket(int playerId, int type, int amount){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("sellOnMarket fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return market.sell((Player)player,type,amount);
    }

    @Override
    public boolean advanceOnTechtree(int playerId, TreeElement element){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("advanceOnTechtree fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return ((Player)player).advanceOnTechTree(element);
    }

    @Override
    public int[] moveSelected(int playerId, int selectedId, int x, int y){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("moveSelected fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return new int[4];
        }

        if(active != player)
            return new int[4];

        if(selected instanceof Unit){
            if(selected instanceof Base)
                return new int[4];

            if(((Unit)selected).getId() != selectedId){
                System.out.println("selected ist asynchron");
                return new int[4];
            }


            Object obj = level.getField(x,y).select();
            if (obj instanceof Field) {
                Unit unit = ((Unit) selected);
                Field target = (Field) obj;
                    if(unit.getOwner()==player)
                        try {
                            int diff=Math.max(Math.abs(unit.getField().getXPos() - target.getXPos()), Math.abs(unit.getField().getYPos() - target.getYPos()));
                            if (target.getWalkable()&&diff <= unit.getMovePointsLeft()) {
                                if(unit.getField().getYPos()<target.getYPos()){unit.setDirection(1);}else{unit.setDirection(0);}
                                unit.getField().setCurrent(null);
                                target.setCurrent(unit);
                                unit.setMovePointsLeft(unit.getMovePointsLeft()-diff);
                                return fight(unit);

                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
            }
        }

        return new int[4];

    }

    private int[] fight(Unit u){
        Unit unit = u;
        boolean both=false;
        int[] result = new int[4];
        for(int x = 0;x<=unit.getRange();x++){
            //positive y direction
            try{
                if(level.getField(unit.getField().getXPos(),unit.getField().getYPos()).getCurrent()instanceof Unit)
            {
                if(level.getField(unit.getField().getXPos(),unit.getField().getYPos()+x).getCurrent().getOwner()!=active){
                final Unit enemy=level.getField(unit.getField().getXPos(),unit.getField().getYPos()+x).getCurrent();

                if(enemy.getRange()>=x)
                    both=true;

                result[0] = 1;

                //fightAnimation(unit, enemy,50,100,both);
                    if(duel(unit,enemy,both))
                        return result;

                break;
                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }

            //negative y direction
            try{
                if(level.getField(unit.getField().getXPos(),unit.getField().getYPos()-x).getCurrent()instanceof Unit)
            {
                if(level.getField(unit.getField().getXPos(),unit.getField().getYPos()-x).getCurrent().getOwner()!=active){
                final Unit enemy=level.getField(unit.getField().getXPos(),unit.getField().getYPos()-x).getCurrent();

                if(enemy.getRange()>=x)
                    both=true;

                    result[1] = 1;

                //fightAnimation(unit, enemy,50,0,both);
                    if(duel(unit,enemy,both))
                        return result;

                break;
                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }

            //negative x direction
            try{
                if(level.getField(unit.getField().getXPos()-x,unit.getField().getYPos()).getCurrent()instanceof Unit)
            {
                if(level.getField(unit.getField().getXPos()-x,unit.getField().getYPos()).getCurrent().getOwner()!=active){
                final Unit enemy=level.getField(unit.getField().getXPos()-x,unit.getField().getYPos()).getCurrent();

                if(enemy.getRange()>=x)
                    both=true;

                    result[2] = 1;
                //fightAnimation(unit, enemy,0,50,both);
                    if(duel(unit,enemy,both))
                        return result;

                break;
                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }

            //positive x direction
            try{
                if(level.getField(unit.getField().getXPos()+x,unit.getField().getYPos()).getCurrent()instanceof Unit)
            {
                if(level.getField(unit.getField().getXPos()+x,unit.getField().getYPos()).getCurrent().getOwner()!=active){
                final Unit enemy=level.getField(unit.getField().getXPos()+x,unit.getField().getYPos()).getCurrent();

                if(enemy.getRange()>=x)
                    both=true;

                    result[3] = 1;
                //fightAnimation(unit, enemy,100,50,both);
                    if(duel(unit,enemy,both))
                        return result;

                break;
                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return result;
    }

    private boolean duel(Unit unit, Unit enemy, boolean both){
        unit.setMovePointsLeft(0);
        enemy.setCurrentHp(enemy.getCurrentHp() - (unit.getAtk()-enemy.getDef()));

        if (enemy.getCurrentHp() <= 0) {
            if(enemy instanceof Base){
                enemy.setOwner(unit.getOwner());
                enemy.setCurrentHp(enemy.getMaxHp());
            }
            enemy.getField().setCurrent(null);
        }
        if(both){
            unit.setCurrentHp(unit.getCurrentHp() - (enemy.getAtk()-unit.getDef()));

            if (unit.getCurrentHp() <= 0){
                unit.getField().setCurrent(null);
                return true;
            }
        }

        return false;
    }


    public boolean isActive(int playerId){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("isActive fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return active == player;
    }

    public BuffInfo getActiveBuff(int id){
        Object unit = archive.get(id);

        if(!(unit instanceof Unit)){
            return BuffInfo.NONE;
        }

        return ((Unit)unit).getSignificantBuff();
    }
}
