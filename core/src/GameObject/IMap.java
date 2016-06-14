package GameObject;


import Action.Buff;

import java.rmi.Remote;
import java.util.List;

public interface IMap extends Remote {

    /**
     * Initialisiert die Map, d.h. platziert alle Felder, Basen und Standardobjekte
     */
    public void init();

    /**
     * Generiert zufaellig eine Karte, nur bei genuegend Zeit zu implementieren
     */
    public void generateRandom();

    /**
     * Speichert die generierte Karte um sie spaeter laden zu koennen
     * wird ebenfalls nur bei genuegend Zeit implementiert
     */
    public void saveConfiguration();

    /**
     * Aktualisiert alle Felder der Karte ueber ihre update-Methode
     */
    public List<Buff> update();

    /**
     * Gibt ein spezielles Feld zurueck
     */
    public Field getField(int x, int y);

    /**
     * Getter und Setter
     */
    public void setFields(Field fields[][]);

    public Field[][] getFields();

    public void setMaxPlayers(int maxPlayers);

    public int getMaxPlayers();

    public void setMinPlayers(int minPlayers);

    public int getMinPlayers();

    public void setLevelName(String levelName);

    public String getLevelName();

}
