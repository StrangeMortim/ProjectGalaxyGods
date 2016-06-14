package GameObject;

import Action.Buff;

import java.util.ArrayList;
import java.util.List;

public class Map implements IMap {

    private Field[][] fields = new Field[8][8];
    private int maxPlayers = 4;
    private int minPlayers = 2;
    private String levelName = "";


    /**
     * Initialisiert die Map, d.h. platziert alle Felder, Basen und Standardobjekte
     */
    @Override
    public void init() {
        /* TODO*/
    }

    /**
     * Generiert zufaellig eine Karte, nur bei genuegend Zeit zu implementieren
     */
    @Override
    public void generateRandom() {
        /*TODO*/
    }

    /**
     * Speichert die generierte Karte um sie spaeter laden zu koennen
     * wird ebenfalls nur bei genuegend Zeit implementiert
     */
    @Override
    public void saveConfiguration() {
        /*TODO*/
    }

    /**
     * Aktualisiert alle Felder der Karte ueber ihre update-Methode
     */
    @Override
    public List<Buff> update() {
        List<Buff> result = new ArrayList<Buff>();

        for(Field[] f: fields)
            for(Field f2: f)
                result.addAll(f2.update());

        return result;
    }

    /**
     * Gibt ein spezielles Feld zurueck
     *
     * @param x
     * @param y
     */
    @Override
    public Field getField(int x, int y) {
        if(x < 0 || y < 0 || x > fields.length || y > fields[0].length)
           return null;


        return fields[x][y];
    }

    /**
     * Getter und Setter
     *
     * @param fields
     */
    @Override
    public void setFields(Field[][] fields) {
        this.fields= fields;
    }

    @Override
    public Field[][] getFields() {
        return fields;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        if(maxPlayers < 2)
            throw new IllegalArgumentException("A game cannot be played with less than 2 players");

        this.maxPlayers = maxPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        if(minPlayers < 2)
            throw new IllegalArgumentException("A game cannot be played with less than 2 players");

        if(minPlayers > maxPlayers)
            this.minPlayers = maxPlayers;
        else
            this.minPlayers = minPlayers;

    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    @Override
    public void setLevelName(String levelName) {
        if(levelName.equals(""))
            throw new IllegalArgumentException("Levelname is empty");

        this.levelName = levelName;
    }

    @Override
    public String getLevelName() {
        return levelName;
    }
}
