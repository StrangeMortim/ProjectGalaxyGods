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
 * This class represents all Data and References in one game, therefor she realises the whole status
 * of the game
 *
 * @author Benjamin, Fabi
 */
public class GameSession implements IGameSession, Serializable{

    /**
     * Name of the GameSession.
     */
    private String name = "TestSession";
    /**
     * Password of the GameSesion. -currently unused-
     */
    private String password = "GameSession";
    /**
     * List with the Teams
     */
    private List<Team> teams;
    /**
     * The Map that is played on
     */
    private Map level;
    /**
     * The player who is on turn
     */
    private Player active;
    /**
     * All currently active Buffs
     */
    private List<Buff> buffs;
    /**
     * Identifier for Account-Player relations
     */
    private HashMap<Account,Player> identities;

    /**
     * The global Chat of the GameSession
     */
    private Chat sessionChat;
    /**
     * The current turn number
     */
    private int turn = 1;

    /**
     * How many rounds the game should proceed at most -currently unused-
     */
    private int round = 0;
    /**
     * How many players can join
     */
    private int maxPlayers = 2;
    /**
     * determines if the game has started or is still waiting for players
     */
    private boolean hasStarted = false;
    /**
     * The marketplace for all player
     */
    private Market market;
    /**
     * How many players are in the game
     */
    private int numberOfPlayers=0;

    /**
     * Id counter for the global IDs
     */
    private int currentId = 1;

    /**
     * Archives every registered object, so that it can be easily accessed via the ID
     */
    private HashMap<Integer,Object> archive = new HashMap<>();

    /**
     * The currently selected object
     */
    private Object selected = null;

    /**
     * The winning team
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
     * Updates all objects in the game, starting with the level(which updates the fields and so on)
     * also updates every Buff an removes it if needed
     * at last distributes gold to the players
     */
    @Override
    public void update(){
            level.update();

        //active buffs
        Iterator<Buff> it = buffs.iterator();
        while (it.hasNext())
            if(it.next().execute())
                it.remove();

        for(Team t: teams)
            for(Player p: t.getPlayers()){
                p.getRessources()[Constants.GOLD] += Constants.GOLD_RES_VALUE+p.getRessourceBoni()[Constants.GOLD];
                //remove the reference Buff objects in Player
                it = p.getTemporaryBuffs().iterator();
                while (it.hasNext())
                    if(it.next().execute())
                        it.remove();
            }

    }

    /**
     * provides a new unit with all buffs applying for it
     * @param u the unit to register
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
     * Removes a Unit and her Buffs from the Game,
     * and provides the resources for the owning player out of the teamCheck
     * also arranges the conquering of a base and sets the field free if the unit
     * was not base
     *
     * @param u the Unit to remove
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

        if(!(u instanceof Base))
            u.getField().setCurrent(null);
    }

    /**
     * sends a chat message
     *
     * @param playerId the id of the sending player
     * @param team     determines if it should be send in the team chat or not
     * @param content  the content of the message
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
     * Removes a team from the game
     * @param t The Team that shall be removed
     */
    @Override
    public void removeTeam(Team t) {
      teams.remove(t);
    }

    /**
     * Adds a team to the game
     * @param t The team that shall be added
     */
    @Override
    public void addTeam(Team t) {
     teams.add(t);
    }


    /**
     * Adds a list of active Buffs to the game
     * @param b The buffs that shall be added to the active ones
     */
    @Override
    public void addBuffs(List<Buff> b){
        buffs.addAll(b);
    }

    /**
     * adds a single active Buff to the game
     * @param b the Buff to add
     */
    @Override
    public void addSingleBuff(Buff b)  {
        if(b == null)
            throw new IllegalArgumentException("Buff ist null - addSingleBuff");

        buffs.add(b);
    }

    /**
     * Removes a Buff from the active ones
     * @param b the Buff to Remove
     */
    @Override
    public void removeBuff(Buff b){
        buffs.remove(b);
    }

    /**
     * Does everything needed, at the start of a turn
     * -which is currently nothing-
     */
    @Override
    public void startTurn(){

    };

    /**
     * Finishes a turn for the given player and sets the next player active,
     * also triggers update, saving of the game and the check if a player/team has won
     * also distributes gold to all players
     *
     * @param playerId the id of the player who wants to finish the turn, must be the same as active.ID
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
     * Removes a player from the game an kills all of his Units
     * @param playerId the id of the removing player
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
     * Lets an account join the game in the given team and if needed creates a player for the account
     * @param a Account to join
     * @param teamColor the team he shall belong to
     * @return the id of the corresponding player-object
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

    /**
     * Prints out some Information about the Session, for Debug Purposes
     */
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
     * Prompts the DBManager to save the session
     * @return whether the action was a success or not
     */
    @Override
    public boolean save(){
        return new DBManager().saveSession(this);
    }

    /**
     * Checks if any team is the remaining winner team
     * @return true if a team has won, else false
     */
    @Override
    public boolean finish() {
    if(teams.size()==1){
        winner = teams.get(0);
        return true;
    }
        return false;
    }

    /**
     * Registers a new buff as an active one an creates instances for all Unit
     * it applies to
     * @param originID   the unit the Buff is meant for(if a specific one)
     * @param playerId   the id of the player who registers the buff
     * @param info       the info on which the buff is based
     * @return           if the action was a success or not
     */
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
                        Unit u = (Unit) archive.get(originID);
                        b = new Buff(u, player, info, this);
                        break;
                }


                Field tmp[][] = level.getFields();

                for (int i = Constants.WOOD; i <= Constants.MANA; ++i)
                    if (b.getPlayer().getRessources()[i] < b.getBuffInfo().getBuffCost()[i])
                        return false;

                for (int j = Constants.WOOD; j <= Constants.MANA; ++j)
                    b.getPlayer().getRessources()[j] -= b.getBuffInfo().getBuffCost()[j];

                //case of a normal Buff, register it for all existing units
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
                    //case special buff, just activate it
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

    /**
     * Registers a new created Object
     * @param toRegister  the Object who wants itself register
     * @return the id of the new registered object
     */
    @Override
    public int registerObject(Object toRegister)  {
        if(toRegister == null)
            throw new IllegalArgumentException("Cannot Register null");

        int id = this.currentId;
        currentId++;
        archive.put(id,toRegister);
        return id;
    }

    /**
     * selects the Object on the given coordinates-> see Field.select
     * @param x  the x coordinate
     * @param y  the y coordinate
     * @return   the id of the selected object
     */
    @Override
    public int select(int x, int y)  {
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

    /**
     * Returns the correct spriteIndex for the prepared Textures, on the given coordinates
     * @param x  the x coordinate
     * @param y  the y coordinate
     * @return the SpriteIndex
     */
    @Override
    public int[] getSpriteIndex(int x, int y)  {
        if(x < 0 || x > Constants.FIELDXLENGTH || y < 0 || y > Constants.FIELDYLENGTH)
            return new int[2];

        Object tmp = level.getField(x,y).select();
        if(tmp == null){
            System.out.println("Das Feld (" + x + ","+y+") gibt null zurueck");
            return new int[2];
        } else if(tmp instanceof Field || tmp instanceof Base){
            return new int[]{level.getField(x,y).getSpriteIndex(),-1};
        } else if(tmp instanceof Unit){
            return new int[]{((Unit)tmp).getSpriteIndex(),((Unit)tmp).getId()};
        }

        System.out.println("Das Feld: (" + x+","+y + ") besitzt kein Sprite");
        return new int[2];
    }

    /**
     * gets the Unit-Information for the given ID, if the id doesn't belong to a unit
     * the values are "-1/-1"
     * @param id the id of the Unit
     * @return  a List of Strings, each corresponding to 1 Attribute Info
     */
    @Override
    public List<String> getInformation(int id){
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

    /**
     * @param playerId  the id of the wanted player
     * @return the current resources of the player
     */
    @Override
    public int[] getRessources(int playerId){
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

    /**
     * checks if the given player ID is the one of the owner of selected
     * @param playerId the id of the player to check
     * @return whether the given player is the owner or not
     */
    @Override
    public boolean isSelectedOwner(int playerId){
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

    /**
     * checks if the given player has access to the market
     * @param playerId the id of the player to check
     * @return  true if the id belongs to a player and he has access to the market, else false
     */
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

    /**
     * checks if the given player has reached the end of the given path on his TechTree
     * @param playerId  the id of the player to check
     * @param treePath  the path on the tree to check
     * @return whether the player has reached the end or not
     */
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

    /**
     * checks if selected is a field an blocked
     * @return true if selected is a field an not free, else false
     */
    @Override
    public boolean hasSelectedCurrent(){
        if(selected instanceof Field)
            return ((Field)selected).getCurrent() == null;

        System.out.println("hasSelectedCurrent wurde fuer nicht-Field Objekt aufgerufen");
        return false;
    }

    /**
     * checks if the current selected object is  of the given type
     * @param classType one of the selectable class types
     * @return if selectable is instanceof of the given type or not
     */
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

    /**
     * checks if selected is a field and of the given resource-type
     * @param type  the resource index to check(see Constants)
     * @return true if selected is field and of the given resource type, else false
     */
    @Override
    public boolean isSelectedRessourceType(int type){
        if(!(selected instanceof Field)){
            System.out.println("Ressource Check wurde fuer ein nicht-Field Objekt aufgerufen");
            return false;
        }

        return ((Field)selected).getResType() == type;
    }

    /**
     * gets the x coordinate for the object with the given id
     * @param id the id of the object
     * @return the x coordinate
     */
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

    /**
     * gets the y coordinate for the object with the given id
     * @param id the id of the object
     * @return the y coordinate
     */
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

    /**
     * checks if selected is a base an the given unitType is available for it
     * @param type the UnitType to check if available
     * @return whether selected is a base and the UnitType is available
     */
    @Override
    public boolean checkHasSelectedUnit(UnitType type){
        if(selected instanceof Base)
            return ((Base)selected).getAvaibleUnits().contains(type);
        else{
            System.out.println("Unit check wurde fuer ein nicht-Base Objekt aufgerufen");
            return false;
        }
    }

    /**
     * checks if the given building has finished building
     * @param b the building to check
     * @return true if selected is a Base and the building has finished, else false
     */
    @Override
    public boolean checkSelectedBuildingFinished(Building b){
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

    /**
     * checks if the given building is currently building
     * @param b the building to check
     * @return true if selected is a Base and the building is currently building, else false
     */
    @Override
    public boolean checkIsBuilding(Building b){
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

    /**
     * checks if the field is free or not
     * @param x the x coordinate of the field
     * @param y the y coordinate of the field
     * @return whether the field is free or not
     */
    @Override
    public boolean checkWalkable(int x, int y){
        return level.getField(x,y).getWalkable();
    }

    /**
     * gets the Teamcheck of the given player
     * @param playerId the player of whom you want the teamcheck
     * @return the team resources or a 0-filled array if the id doesn't belong to a player
     */
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

    /**
     * returns the account name to the given player
     * @param playerId the id of the searched player
     * @return the name of the player or "Not_a_Player_or_not_existent" if the id doesn't belong to a player
     */
    @Override
    public String getPlayerName(int playerId){
        Object tmp = archive.get(playerId);

        if(tmp instanceof Player)
            return ((Player)tmp).getAccount().getName();

        return "Not_a_Player_or_not_existent";
    }

    /**
     * gets the chat for the given player
     * @param playerId the id of the player
     * @param team whether we want the session chat or the teamchat
     * @return the chat-content in form,where every entry is an entry in the list
     */
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
            if(m.getVisibleForAll() || m.getVisibleFor().contains(player)) // check if the player is allowed to see
                result.add(m.getContent());

        return result;
    }

    /**
     * @return the current values on the market
     */
    @Override
    public int[] getMarketInfo(){
        return market.getMarketInfo();
    }

    /**
     * Lets the given player add some of his resources to the check of his team
     * @param playeriD      the player that wants to add some resources
     * @param resourceType which resource should be added(see Constants)
     * @param amount        how much of the resource should be added
     * @return whether the action was a success or not
     */
    @Override
    public boolean addTeamRessources(int playeriD, int resourceType, int amount){
        Object player = archive.get(playeriD);

        if(!(player instanceof Player)){
            System.out.println("addTeamRessources fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        switch (resourceType){
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

    /**
     * Recruits a unit on the selected object if it is a base
     * @param selectedId to check if it's the correct selected object
     * @param type  the type that should be created
     * @return  whether the action was a success
     */
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

    /**
     * starts the building-process of a building or aborts it
     * @param playerId    the player who wants to build something
     * @param selectedId  the id of selected, to check if it's the right one, in case it's a base
     * @param building    which building should be build or cancelled
     * @param abort       whether the process should be aborted or not(in that case started)
     * @return   whether the action was a success or not
     */
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

    /**
     * Activates a hero skill of the given player
     * @param playerId the id of the player
     * @param left if the left hand skill should be activated or not(other case is right hand)
     * @return  whether the activation was a success or not(also false if playerId doesn't belong to a player)
     */
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

    /**
     * returns the hero id of the given player
     * @param playerId the id of the player
     * @return the hero id or -1 if playerId doesn't belong to a player
     */
    @Override
    public int getHero(int playerId){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("activateHeroPower fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return -1;
        }

        return ((Player)player).getHero().getId();
    }

    /**
     * checks if a field has units around it or not
     * @param x the x coordinate of the field
     * @param y the y coordinate of the field
     * @return whether there are units or not
     */
    @Override
    public boolean hasNearUnits(int x, int y){
        return !(level.getField(x,y).getNearUnits().isEmpty());
    }

    /**
     * starts a research on the selected object
     * @param selectedId the id of selected to check if it's the correct object
     * @param r what should be researched
     * @return true if selected is a base and the action a success, else false
     */
    @Override
    public boolean researchOnSelected(int selectedId, Research r){
        if(!(selected instanceof Base) || ((Base)selected).getId() != selectedId){
            System.out.println("Client selected ID und selected sind nicht mehr synchron");
            return false;
        }

        return Research.RESEARCH_ARCHER.research((Base) selected);
    }

    /**
     * lets a player buy something on the market
     * @param playerId the player who want sot buy
     * @param type   which resource he wants to buy
     * @param amount  how much he wants to buy
     * @return whether he could buy it or not
     */
    @Override
    public boolean buyOnMarket(int playerId, int type, int amount){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("buyOnMarket fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return market.buy((Player)player,type,amount);
    }

    /**
     * lets a player sell something on the market
     * @param playerId the player who want sot sell
     * @param type   which resource he wants to sell
     * @param amount  how much he wants to sell
     * @return whether he could sell it or not
     */
    @Override
    public boolean sellOnMarket(int playerId, int type, int amount){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("sellOnMarket fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return market.sell((Player)player,type,amount);
    }

    /**
     * lets a player add a tree-element on his techTree
     * @param playerId  the id of the player who wants to add an element
     * @param element   the element he wants to add
     * @return  whether he could add it or not
     */
    @Override
    public boolean advanceOnTechtree(int playerId, TreeElement element){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("advanceOnTechtree fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return ((Player)player).advanceOnTechTree(element);
    }

    /**
     * moves the selected object the given amount of fields
     * also triggers a fight afterwards if enemy units are near enough
     * @param playerId   check if it's the rightful owner
     * @param selectedId check if it's the right object
     * @param x number fields in x direction
     * @param y number fields in y direction
     * @return
     */
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

    /**
     * checks all possible fields to attack enemy units and triggers duel if one fitting enemy is found
     * @param u the unit to check the fields for
     * @return an int-array containing 0 or 1 for each direction, determining if there was a fight or not(1=fight, 0=no fight)
     */
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

    /**
     * arranges an direct fight between two units, calc's all the damage and calls if needed remove unit
     * @param unit   the attacking unit
     * @param enemy  the defending unit
     * @param both   if the defending unit shall attack back
     * @return whether the attacker died or not
     */
    private boolean duel(Unit unit, Unit enemy, boolean both){
        unit.setMovePointsLeft(0);
        enemy.setCurrentHp(enemy.getCurrentHp() - (unit.getAtk()-enemy.getDef()));

        if (enemy.getCurrentHp() <= 0) {
            if(enemy instanceof Base){
                Player attacker = unit.getOwner();
                Player defender = enemy.getOwner();
                for(int i=Constants.WOOD; i<=Constants.MANA; i++){
                    if(defender.getRessources()[i] >= (int)(UnitType.BASE.getRessourceCost()[i]*Constants.UNIT_RECYCLING_MODIFIER)) {
                        attacker.getRessources()[i] += (int) (UnitType.BASE.getRessourceCost()[i] * Constants.UNIT_RECYCLING_MODIFIER);
                        defender.getRessources()[i] -= (int) (UnitType.BASE.getRessourceCost()[i] * Constants.UNIT_RECYCLING_MODIFIER);
                    }else{
                        attacker.getRessources()[i] += defender.getRessources()[i];
                        defender.getRessources()[i] = 0;
                    }
                }
                enemy.setOwner(unit.getOwner());
                enemy.setCurrentHp(enemy.getMaxHp());
            }


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

    /**
     * checks if the given player is the active player(is on turn) or not
     * @param playerId the id of the player to check
     * @return true if he is the active, else false(also if playerId doesn't belong to a player)
     */
    @Override
    public boolean isActive(int playerId){
        Object player = archive.get(playerId);

        if(!(player instanceof Player)){
            System.out.println("isActive fuer ein nicht-Spieler Objekt wurde aufgerufen");
            return false;
        }

        return active == player;
    }

    /**
     * checks if the Shield(Macht des Drachen) skill of the Hero
     * was triggered as Shield or as Dragonfist
     *
     * @param heroId the id of the hero to check
     * @return true if it was dragonfist, else false
     */
    @Override
    public boolean hasCalledTheDragon(int heroId){
        Object hero = archive.get(heroId);

        if(hero instanceof Hero)
            return ((Hero)hero).getCalledTheDragon();

        return false;
    }

    /**
     * resets the hasCalledTheDragon value of the given hero back to false
     *
     * @param heroId the id of the hero to reset
     */
    @Override
    public void deactivateTheDragon(int heroId){
        Object hero = archive.get(heroId);

        if(hero instanceof Hero)
            ((Hero)hero).setCalledTheDragon(false);
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

 }
