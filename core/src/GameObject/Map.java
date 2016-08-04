package GameObject;

import Action.Buff;
import Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map implements Serializable {

  //  private static final long serialVersionUID = 1069218016788334349L;
    private Field[][] fields = new Field[Constants.FIELDXLENGTH][Constants.FIELDYLENGTH];
    private int maxPlayers = 4;
    private int minPlayers = 2;
    private String levelName = "";
    private GameSession session = null;
    private int currentPlayers = 0;
    private int[] baseXPositions = new int[]{12, 12, 1, 23};
    private int[] baseYPositions = new int[]{2,22,13,13};
    private int iD;


    public Map(String levelName, int maxPlayers, int minPlayers, GameSession session){
        if (session == null || levelName.equals("") || maxPlayers > 4 || minPlayers < 2 || maxPlayers < minPlayers ||session == null)
            throw new IllegalArgumentException("Map: Constructor invalid values");

        this.session = session;
        this.levelName = levelName;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        try {
           iD = session.registerObject(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisiert die Map, d.h. platziert alle Felder, Basen und Standardobjekte
     */

    public void init() {
         /*Hardcoded Map, Magic-Numbers are Magic because they are only used in this specific Map
        Currently this is the only Map, should at any Time the possibility to choose an individual map be added
        than this will be deleted and maps will be generated from Files, using JSON or similar
        */
        int i=0;
        int j=0;

        //Generate a Map with only Normal Fields as a base
        for(Field[] f: fields){
            for(Field f2: f){
                fields[i][j] = new Field(-1,0,i,j,this, session);
                j++;
            }
            j=0;
            i++;
        }

        //Forest top
        for(i=0; i<Constants.FIELDXLENGTH; ++i){
            switch (i){
                case 0:
                case 1:
                case 24:
                case 25:
                    for(int j1=0;j1<6;++j1){
                        fields[i][j1].setResType(Constants.WOOD);
                        fields[i][j1].setResValue(300);
                    }
                    for(int j2=19;j2<24;++j2){
                        fields[i][j2].setResType(Constants.WOOD);
                        fields[i][j2].setResValue(300);
                    }
                    break;
                case 2:
                case 3:
                case 22:
                case 23:
                    for(int j3=0;j3<4;++j3){
                        fields[i][j3].setResType(Constants.WOOD);
                        fields[i][j3].setResValue(300);
                    }
                    for(int j4=21;j4<24;++j4){
                        fields[i][j4].setResType(Constants.WOOD);
                        fields[i][j4].setResValue(300);
                    }
                    break;
                case 4:
                case 5:
                case 20:
                case 21:
                    for(int j5=0;j5<2;++j5){
                        fields[i][j5].setResType(Constants.WOOD);
                        fields[i][j5].setResValue(300);
                    }
                    for(int j6=22;j6<24;++j6){
                        fields[i][j6].setResType(Constants.WOOD);
                        fields[i][j6].setResValue(300);
                    }
            }
        }

        //Forest middle
        for(int i1=12; i1<16; ++i1)
            for(int j7=11;j7<14;++j7){
                fields[i1][j7].setResType(Constants.WOOD);
                fields[i1][j7].setResValue(300);
            }

        //Iron
        fields[7][12].setResType(Constants.IRON);
        fields[7][12].setResValue(300);
        fields[7][13].setResType(Constants.IRON);
        fields[7][13].setResValue(300);

        fields[13][8].setResType(Constants.IRON);
        fields[13][8].setResValue(300);
        fields[14][8].setResType(Constants.IRON);
        fields[14][8].setResValue(300);

        fields[20][12].setResType(Constants.IRON);
        fields[20][12].setResValue(300);
        fields[20][13].setResType(Constants.IRON);
        fields[20][13].setResValue(300);

        fields[13][17].setResType(Constants.IRON);
        fields[13][17].setResValue(300);
        fields[14][17].setResType(Constants.IRON);
        fields[14][17].setResValue(300);

        int k=11;
        int j8=14;
        for(int i2=9;i2<12;++i2) {
            fields[i2][k].setResType(Constants.IRON);
            fields[i2][k].setResValue(300);
            fields[i2][j8].setResType(Constants.IRON);
            fields[i2][j8].setResValue(300);
            k--;
            j8++;
        }

        k++;
        j8--;
        for(int i3=16;i3<19;++i3) {
            fields[i3][k].setResType(Constants.IRON);
            fields[i3][k].setResValue(300);
            fields[i3][j8].setResType(Constants.IRON);
            fields[i3][j8].setResValue(300);
            k++;
            j8--;
        }



    }

    /**
     * Generiert zufaellig eine Karte, nur bei genuegend Zeit zu implementieren
     */

    public void generateRandom() {
        /*TODO*/
    }

    /**
     * Speichert die generierte Karte um sie spaeter laden zu koennen
     * wird ebenfalls nur bei genuegend Zeit implementiert
     */

    public void saveConfiguration() {
        /*TODO*/
    }

    /**
     * Aktualisiert alle Felder der Karte ueber ihre update-Methode
     */

    public List<Buff> update() {
        int timesSpawned = 0;
        Random rng = new Random();
        List<Buff> result = new ArrayList<Buff>();

        for(Field[] f: fields)
            for(Field f2: f) {
                result.addAll(f2.update());

                if(timesSpawned < 2 && rng.nextInt(100) < 5 && f2.getCurrent() == null && f2.getResType() == -1){
                    f2.setResType(Constants.MANA);
                    f2.setResValue(Constants.MANA_RES_VALUE);
                    timesSpawned++;
                }
            }

        return result;
    }


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

    public void setFields(Field[][] fields) {
        this.fields= fields;
    }


    public Field[][] getFields() {
        return fields;
    }


    public void setMaxPlayers(int maxPlayers) {
        if(maxPlayers < 2)
            throw new IllegalArgumentException("A game cannot be played with less than 2 players");

        this.maxPlayers = maxPlayers;
    }


    public int getMaxPlayers() {
        return maxPlayers;
    }


    public void setMinPlayers(int minPlayers) {
        if(minPlayers < 2)
            throw new IllegalArgumentException("A game cannot be played with less than 2 players");

        if(minPlayers > maxPlayers)
            this.minPlayers = maxPlayers;
        else
            this.minPlayers = minPlayers;

    }


    public int getMinPlayers() {
        return minPlayers;
    }


    public void setLevelName(String levelName) {
        if(levelName.equals(""))
            throw new IllegalArgumentException("Levelname is empty");

        this.levelName = levelName;
    }


    public String getLevelName() {
        return levelName;
    }


    public IGameSession getSession(){
        return this.session;
    }


    public boolean addBase(Player p, int playerNumber) {
        if(playerNumber > maxPlayers-1 || playerNumber < 0)
            return false;

        Base tmp = new Base(UnitType.BASE, p, session);
        fields[baseXPositions[playerNumber]+1][baseYPositions[playerNumber]].setCurrent(tmp);
        fields[baseXPositions[playerNumber]+1][baseYPositions[playerNumber]].setSpriteIndex(SpriteNames.BASE_UP_RIGHT.getSpriteIndex());
        fields[baseXPositions[playerNumber]][baseYPositions[playerNumber]-1].setCurrent(tmp);
        fields[baseXPositions[playerNumber]][baseYPositions[playerNumber]-1].setSpriteIndex(SpriteNames.BASE_DOWN_LEFT_EMPTY.getSpriteIndex());
        fields[baseXPositions[playerNumber]+1][baseYPositions[playerNumber]-1].setCurrent(tmp);
        fields[baseXPositions[playerNumber]+1][baseYPositions[playerNumber]-1].setSpriteIndex(SpriteNames.BASE_DOWN_RIGHT_EMPTY.getSpriteIndex());
        fields[baseXPositions[playerNumber]][baseYPositions[playerNumber]].setCurrent(tmp);
        fields[baseXPositions[playerNumber]][baseYPositions[playerNumber]].setSpriteIndex(SpriteNames.BASE_UP_LEFT.getSpriteIndex());
        return true;
    }


    public int getId(){
        return iD;
    }
}
