package chat;

import Player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benja_000 on 12.06.2016.
 * Diese Klasse realisert das Message-Objekt, dass die Eigenschaften
 * einer Nachricht im Chat verkörpert.
 */
public class Message implements IMessage,Serializable {

    private static final long serialVersionUID = -7904810564348423122L;
    /**
     * Inhalt der Nachricht.
     */
    private String content;
    /**
     * Gibt an, ob Nachricht fuer alle Spieler sichtbar ist.
     */
    private boolean visibleForAll = true;
    /**
     * List der Spieler, fuer die die Nachricht sichtbar ist.
     */
    private List<Player> visibleFor=new ArrayList<Player>();

    /**
     * Macht die Nachricht sichtbar fuer ausgewaehlte Spieler.
     * @param p der ausgewaehlte Spieler
     */
    public void makeVisibleFor(Player p) {
        if(p == null)
            throw new IllegalArgumentException("MakeVisibleFor: the Player is null");

            if(!visibleFor.contains(p))
                visibleFor.add(p);
    }




    //Getter Setter
    @Override
    public String getContent() {
        return content;
    }
    @Override
    public void SetContent(String s) {
    content=s;
    }
    @Override
    public boolean getVisibleForAll() {
        return visibleForAll;
    }
    @Override
    public void setVisibleForAll(boolean b) {
    visibleForAll=b;
    }
    @Override
    public List<Player> getVisibleFor() {
        return visibleFor;
    }
    @Override
    public void setVisibleFor(List<Player> p) {
    visibleFor=p;
    }
}
