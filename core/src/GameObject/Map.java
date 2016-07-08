package GameObject;

import Action.Buff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Map implements IMap,Serializable {

    private Field[][] fields = new Field[26][24];
    private int maxPlayers = 4;
    private int minPlayers = 2;
    private String levelName = "";
    private GameSession session = null;


    public Map(String levelName, int maxPlayers, int minPlayers, GameSession session){
        if (levelName.equals("") || maxPlayers > 4 || minPlayers < 2 || maxPlayers < minPlayers ||session == null)
            throw new IllegalArgumentException("Map: Constructor invalid values");

        this.session = session;
        this.levelName = levelName;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    /**
     * Initialisiert die Map, d.h. platziert alle Felder, Basen und Standardobjekte
     */
    @Override
    public void init() {
        int i=0;
        int j=0;

        for(Field[] f: fields){
            for(Field f2: f){
                fields[i][j] = new Field(-1,0,i,j,this);
                j++;
            }
            j=0;
            i++;
        }


        //Forest top
        for(i=0; i<2; ++i)
            for(j=((i==0) ? 0 : 20); j<((i==0) ? 6 : 26); ++j){
                fields[j][i].setResType(0);
                fields[j][i].setResValue(300);
            }

        for(i=2; i<4; ++i)
            for(j=((i==2) ? 0 : 22); j<((i==2) ? 4 : 26); ++j){
                fields[j][i].setResType(0);
                fields[j][i].setResValue(300);
            }

        for(i=4; i<6; ++i)
            for(j=((i==4) ? 0 : 24); j<((i==4) ? 2 : 26); ++j){
                fields[j][i].setResType(0);
                fields[j][i].setResValue(300);
            }

        //Forest bottom
        for(i=19; i<21; ++i)
            for(j=((i==19) ? 0 : 24); j<((i==19) ? 2 : 26); ++j){
                fields[j][i].setResType(0);
                fields[j][i].setResValue(300);
            }

        for(i=21; i<23; ++i)
            for(j=((i==21) ? 0 : 22); j<((i==21) ? 4 : 26); ++j){
                fields[j][i].setResType(0);
                fields[j][i].setResValue(300);
            }

        for(i=23; i<24; ++i)
            for(j=((i==23) ? 0 : 20); j<((i==23) ? 6 : 26); ++j){
                fields[j][i].setResType(0);
                fields[j][i].setResValue(300);
            }

        //Forest middle
        for(i=12; i<16; ++i)
            for(j=11;j<14;++j){
                fields[i][j].setResType(0);
                fields[i][j].setResValue(300);
            }

        //Iron
        fields[7][12].setResType(1);
        fields[7][12].setResValue(300);
        fields[7][13].setResType(1);
        fields[7][13].setResValue(300);

        fields[13][7].setResType(1);
        fields[13][7].setResValue(300);
        fields[14][7].setResType(1);
        fields[14][7].setResValue(300);

        fields[20][12].setResType(1);
        fields[20][12].setResValue(300);
        fields[20][13].setResType(1);
        fields[20][13].setResValue(300);

        fields[13][19].setResType(1);
        fields[13][19].setResValue(300);
        fields[14][19].setResType(1);
        fields[14][19].setResValue(300);

        int k=11;
        j=14;
        for(i=9;i<12;++i) {
            fields[i][k].setResType(1);
            fields[i][k].setResValue(300);
            fields[i][j].setResType(1);
            fields[i][j].setResValue(300);
            k--;
            j++;
        }

        for(i=16;i<19;++i) {
            fields[i][k].setResType(1);
            fields[i][k].setResValue(300);
            fields[i][j].setResType(1);
            fields[i][j].setResValue(300);
            k++;
            j--;
        }



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

    @Override
    public boolean checkMovement(int xPos, int yPos) {
        Field toCheck = fields[xPos][yPos];
        return (toCheck.getCurrent() == null && toCheck.getHasMine());
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

    @Override
    public GameSession getSession(){
        return this.session;
    }
}
