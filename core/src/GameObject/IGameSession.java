package GameObject;

import Action.*;
import Player.*;
import chat.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;


/**
 * Created by benja_000 on 10.06.2016.
 */
public interface IGameSession extends Remote {
    /**
     *
     */
    public void update()throws RemoteException;

    /**
     * Registriert eine neue Einheit und gibt ihr alle Buffs
     */
    public void registerUnit(Unit u)throws RemoteException;

    /**
     * Entfernt alle Buffs für die übergebene Einheit
     */
    public void removeUnit(Unit u)throws RemoteException;

    //region Spaeter
    /**
     *
     * @param t
     */
    public void addTeam(Team t)throws RemoteException;
    /**
     *
     * @param t
     */
    public void removeTeam(Team t)throws RemoteException;
    /**
     *
     * @param
     */
    public void sendMessage(int playerId, boolean team, String content)throws RemoteException;
    /**
     *
     * @param b
     */
    public void addBuffs(List<Buff> b)throws RemoteException;

    public void addSingleBuff(Buff b) throws RemoteException;
    /**
     *
     * @param b
     */
    public void removeBuff(Buff b)throws RemoteException;
    /**
     *
     */
    public void startTurn()throws RemoteException;
    //endregion

    /**
     *
     */
    public void finishTurn(int playerID)throws RemoteException;

    /**
     */
    public int playerJoin(Account a, String teamColor)throws RemoteException;
    /**
     *
     * @param playerID
     */
    public void playerLeave(int playerID)throws RemoteException;
    /**
     *
     * @return
     */
    public boolean save()throws RemoteException;
    /**
     *
     * @return
     */
    public boolean finish()throws RemoteException;

    /**
     * Getter für die Map
     */
    public int getMap()throws RemoteException;

    public int getNumberOfPlayers()throws RemoteException;

    public void setNumberOfPlayers(int number)throws RemoteException;

    public boolean registerBuff(int originID, int playerID, BuffInfo info) throws RemoteException;

    public String getName() throws RemoteException;

    public void setName(String name) throws RemoteException;

    public String getPassword() throws RemoteException;

    public int getMarket() throws RemoteException;

    public int getActive() throws RemoteException;

    public void setActive(Player p) throws RemoteException;

    public List<Buff> getBuffs() throws RemoteException;

    public List<Team> getTeams() throws RemoteException;

    public boolean isHasStarted() throws RemoteException;

    public void setHasStarted(boolean b) throws RemoteException;

    public Chat getSessionChat() throws RemoteException;

    public void showSessionDetails() throws RemoteException;

    public void setSessionChat(Chat sessionChat)throws RemoteException;

    public int getTurn()throws RemoteException;

    public void setTurn(int turn)throws RemoteException;

    public int getMaxPlayers()throws RemoteException;

    public void setMaxPlayers(int maxPlayers)throws RemoteException;

    public void setPassword(String password) throws RemoteException;

    public int getRound() throws RemoteException;

    public void setRound(int round) throws RemoteException;

    public Player getPlayerPerName(String name) throws RemoteException;


    public int select(int x, int y) throws RemoteException;

    public int[] getSpriteIndex(int x, int y) throws RemoteException;

    public List<String> getInformation(int id) throws RemoteException;

    public int[] getRessources(int playerId) throws RemoteException;

    public boolean isSelectedOwner(int playerId) throws RemoteException;

    public int registerObject(Object toRegister) throws RemoteException;

    public boolean checkMarket(int playerId) throws RemoteException;

    public boolean checkPathFull(int playerId, int treePath) throws RemoteException;

    public boolean hasSelectedCurrent() throws RemoteException;

    public boolean isSelectedClassOf(Selectable classType) throws RemoteException;

    public boolean isSelectedRessourceType(int type) throws RemoteException;

    public int getSelectedX(int id) throws RemoteException;

    public int getSelectedY(int id) throws RemoteException;

    public boolean checkHasSelectedUnit(UnitType type) throws RemoteException;

    public boolean checkSelectedBuildingFinished(Building b) throws RemoteException;

    public boolean checkIsBuilding(Building b) throws RemoteException;

    public boolean checkWalkable(int x, int y) throws RemoteException;

    public int[] getTeamRessources(int playerId) throws RemoteException;

    public String getPlayerName(int playerId) throws RemoteException;

    public List<String> getChatBackLog(int playerId, boolean teamChat) throws RemoteException;

    public int[] getMarketInfo() throws RemoteException;

    public boolean buyOnMarket(int playerId, int type, int amount) throws RemoteException;

    public boolean sellOnMarket(int playerId, int type, int amount) throws RemoteException;

    public boolean addTeamRessources(int playerId, int ressourceType, int amount) throws RemoteException;

    public boolean createUnit(int selectedId, UnitType type) throws RemoteException;

    public boolean buildOrAbortBuildingOnSelected(int playerId, int selectedId,Building building, boolean abort) throws RemoteException;

    public boolean activateHeroPower(int playerId, boolean left) throws RemoteException;

    public int getHero(int playerId) throws RemoteException;

    public boolean hasNearUnits(int x, int y) throws RemoteException;

    public boolean researchOnSelected(int selectedId, Research r) throws RemoteException;

    public boolean advanceOnTechtree(int playerId, TreeElement element) throws RemoteException;

    public int[] moveSelected(int playerId, int selectedId, int x, int y) throws RemoteException;

    public boolean isActive(int playerId) throws RemoteException;

    public BuffInfo getActiveBuff(int id) throws RemoteException;
}
