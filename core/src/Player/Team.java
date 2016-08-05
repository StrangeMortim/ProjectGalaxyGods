package Player;

import GameObject.GameSession;
import chat.Chat;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Realises a Team objects, team members are allied in a game
 */
public class Team implements Serializable {

    /**
     * The Teammembers
     */
    private List<Player> players;

    /**
     * The private chat of the Team
     */
    private Chat chat;

    /**
     * The Color of the Team
     */
    private String color;

    /**
     * The Team Resources
     */
    private int[] check = new int[4];

    /**
     * The session where this object belongs to and is registered
     */
    private GameSession session;

    /**
     * The global ID of the Object
     */
    private int iD;


    public Team(List<Player> players, String color, GameSession session){
        if(session == null || players == null || color.equals(""))
            throw new IllegalArgumentException("Players is null or color is empty");

        chat = new Chat(session);
        this.players = players;
        for(Player p: this.players) {
            p.setTeam(this);
            chat.addParticipant(p);
        }

        this.color = color;
        this.session = session;
            iD = session.registerObject(this);

    }


    /**
     * Getter/setter
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public void setChat(Chat chat) {
            this.chat = chat;
    }
    public Chat getChat() {
        return chat;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }

    public void setCheck(int[] check) {
        this.check = check;
    }
    public int[] getCheck() {
        return check;
    }

    /**
     * adds a team member
     * @param player the new member
     * @return if the action was a success
     */
    public boolean addPlayer(Player player){
        if(player != null) {
            players.add(player);
            chat.addParticipant(player);
            return true;
        }

        return false;
    }
}
