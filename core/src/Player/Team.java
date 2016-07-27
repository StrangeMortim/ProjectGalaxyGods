package Player;

import chat.Chat;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Fabi on 11.06.2016.
 */
public class Team implements ITeam,Serializable {
    private List<Player> players;
    private Chat chat = new Chat();
    private String color;
    private int[] check = new int[3];


    public Team(List<Player> players, String color){
        if(players == null || color.equals(""))
            throw new IllegalArgumentException("Players is null or color is empty");

        this.players = players;
        for(Player p: this.players)
            try {
                p.setTeam(this);
                chat.addParticipant(p);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        this.color = color;
    }


    @Override
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void setChat(Chat chat) {
            this.chat = chat;
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getColor() {
        return color;
    }

    //Check = Kasse
    @Override
    public void setCheck(int[] check) {
        this.check = check;
    }

    @Override
    public int[] getCheck() {
        return check;
    }
}
