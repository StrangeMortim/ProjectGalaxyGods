package Player;

import GameObject.GameSession;
import chat.Chat;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Team implements Serializable {

    private static final long serialVersionUID = -1231492845712879611L;
    private List<Player> players;
    private Chat chat;
    private String color;
    private int[] check = new int[4];
    private GameSession session;
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
        try {
            iD = session.registerObject(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



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

    //Check = Kasse

    public void setCheck(int[] check) {
        this.check = check;
    }


    public int[] getCheck() {
        return check;
    }
}
