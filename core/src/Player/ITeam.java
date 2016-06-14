package Player;


import chat.Chat;

import java.rmi.Remote;
import java.util.List;

public interface ITeam extends Remote {

    /*
    Getter und Setter fuer die Attribute
     */
    public void setPlayers(List<Player> players);

    public List<Player> getPlayers();

    public void setChat(Chat chat);

    public Chat getChat();

    public void setColor(String color);

    public String getColor();

    /*
    Check = Kasse
     */
    public void setCheck(int check[]);

    public int[] getCheck();
}
