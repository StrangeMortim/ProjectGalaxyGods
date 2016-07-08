package GameObject;

import Action.Buff;
import Player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Field implements IField,Serializable {

    private int resType = -1;
    private int resValue = 0;
    private int xPos = -1;
    private int yPos = -1;
    private Unit current;
    private boolean walkable=true;
    private int roundsRemain = -1;
    private String spriteName = "";
    private Texture texture;
    private boolean baseBuilding = false;
    private Player builder = null;
    private boolean mineBuilding = false;
    private boolean hasMine = false;
    private Map map = null;

    public Field(int resType, int resValue, int xPos, int yPos, Map map){
        if(resType < -1 || resType > 4 || xPos < 0 || xPos > 26 || yPos < 0 || yPos >24 || map == null)
            throw new IllegalArgumentException("Invalid Values");

        this.resType = resType;
        this.resValue = resValue;
        this.xPos = xPos;
        this.yPos = yPos;
        this.map = map;

        //Choose random default Fieldsprite, increase number for new sprites,
        //sprites must be named "normal" + number(next higher int) and be png files
        //sprites must be located in sprites folder
        Random r = new Random();


        this.spriteName = "assets/sprites/normal" + r.nextInt(3) + ".png";

        //this.texture = new Texture(Gdx.files.internal("assets/"+this.spriteName));
    }

    /**
     * Aktualisiert das Objekt auf dem Feld
     * Und die bestehenden Bauprozesse
     * Außerdem werden Ressourcen verteilt
     */
    @Override
    public List<Buff> update() {

        this.updateBuildingProcesses();
        this.distributeRessources();

        return current.update();
        /*TODO check*/
    }

    /**
     * Verteilt Ressourcen an alle umstehenden Einheiten, falls das Feld ressourcen besitzt
     */
    private void distributeRessources(){
        /*TODO specify how ressources are distributed*/
        //if field has ressources
        if(resType != -1) {
            List<Unit> nearUnits = null;
            nearUnits = this.getNearUnits();

            Player tmp = null;

            //add ressources to all near units depending on the fields ressource
            switch (resType) {
                case 0:
                    for (Unit u : nearUnits) {
                        tmp = u.getOwner();
                        if(tmp.getTurn()) {  //check player
                            u.getRessources()[0] += 10 + tmp.getRessourceBoni()[0];
                            resValue -= (10 + tmp.getRessourceBoni()[0]);
                        }
                    }
                    break;
                case 1:
                    if(hasMine) {
                        for (Unit u : nearUnits) {
                            tmp = u.getOwner();
                            if(tmp.getTurn()) { //check player
                                u.getRessources()[1] += 10 + tmp.getRessourceBoni()[1];
                                resValue -= (10 + tmp.getRessourceBoni()[1]);
                            }
                        }
                    }
                    break;
                case 3:
                    for (Unit u : nearUnits) {
                        tmp = u.getOwner();
                        if(tmp.getTurn()) { //check player
                            u.getRessources()[3] += 10 + tmp.getRessourceBoni()[3];
                            resValue -= (10 + tmp.getRessourceBoni()[3]);
                        }
                    }
                    break;
                default:
                    break;
            }

            //if no more ressources left after  dispersal, set no-ressource values
            if(resValue <= 0){
                resType = -1;
                resValue = 0;
            }
        }
    }

    /**
     * Aktualisiert die Bauprozesse
     */
    private void updateBuildingProcesses(){
        /*TODO check*/
        if(roundsRemain > 0){ //there is something to build

            List<Unit> nearUnits = this.getNearUnits();
            if (baseBuilding) {
                for(Unit u: nearUnits)
                    if(u.getOwner() == builder) {       //if it's a base and owner units are near, count down the rounds, -1 for every unit near
                        roundsRemain--;
                        if (roundsRemain == 0) {        //if the rounds reach zero, build the base and stop
                            current = new Base(UnitType.BASE, builder);
                            builder = null;
                            baseBuilding = false;
                            break;
                        }
                    }
                    /*
                    if it's a mine check all near units and reduce the rounds for each, as long as the owner has enough wood,
                    also reduce the owners wood by 5
                     */
            } else if(mineBuilding){
                if(!nearUnits.isEmpty())
                    nearUnits.stream().filter(u -> u.getOwner().getRessources()[0] >= 5).forEach(u -> {
                        u.getOwner().getRessources()[0] -= 5;
                        roundsRemain--;
                    });

                if(roundsRemain <= 0) {
                    hasMine = true;
                    mineBuilding = false;
                }
            }
        }
    }

    /**
     * Searches all Unit in the direct environment to the Field(the (max) 8 surrounding Fields
     * @return the found Units
     */
    @Override
    public List<Unit> getNearUnits(){
        List<Unit> result = new ArrayList<>();
        for(int i=yPos-1; i<=yPos+1; ++i){
            for(int k=xPos-1; k<=xPos+1; ++k){
                try {
                    Unit tmp = map.getField(k,i).getCurrent();
                    if(tmp != null){
                       result.add(tmp);
                    }
                } catch (NullPointerException e){
                    continue;
                }
            }
        }

        return result;
        /*TODO check*/
    }

    /**
     * Faengt an eine Basis auf dem Feld zu bauen
     *
     * @param player der Spieler der die Basis baut
     * @return gibt an ob der Spieler die Basis bauen kann oder nicht
     */
    @Override
    public boolean buildBase(Player player) {
        if(current == null && !hasMine){
            int[] baseCost = UnitType.BASE.getRessourceCost();
            int[] availableRessources = player.getRessources();
            for(int j=0; j<4; ++j)
                if (baseCost[j] > availableRessources[j])
                    return false;

            List<Unit> nearUnits = this.getNearUnits();
            for(Unit u: nearUnits){
                if(u.getOwner() == player){
                    baseBuilding = true;
                    roundsRemain = UnitType.BASE.getRecruitingTime();
                    for(int j2=0; j2<4; ++j2)
                        player.getRessources()[j2] -= baseCost[j2];

                    builder = player;
                    walkable = false;
                    return true;
                }
            }
        }
        /*TODO check*/
        return false;
    }

    /**
     * Bricht den Bau der Basis ab, findet keiner statt passiert nichts
     *
     * @param player der Spieler der versucht den Bau abzubrechen
     * @return Ob der Vorgang erfolgreich war oder nicht(ob nach dem Methodenaufruf kein Bau mehr stattfindet oder nicht)
     */
    @Override
    public boolean abortBuild(Player player) {
        List<Unit> nearUnits = this.getNearUnits();
        for(Unit u: nearUnits){
            if(u.getOwner() == player){
                int[] originalCost = UnitType.BASE.getRessourceCost();
                float ressourcesLeft = roundsRemain / UnitType.BASE.getRecruitingTime();
                for(int i=0; i<4; ++i)
                    player.getRessources()[i] = (int)(ressourcesLeft*originalCost[i]);

                roundsRemain = -1;
                walkable = true;
                baseBuilding = false;
                return true;
            }
        }
                /*TODO check*/
        return false;
    }

    /**
     * Startet den Bau einer Mine, der Bau kann nicht abgebrochen werden
     *
     * @param player der Spieler der den Bau starten will
     * @return gibt an ob das Starten erfolgreich war
     */
    @Override
    public boolean buildMine(Player player) {
        if(roundsRemain == -1 && current == null && resType == 1){
            List<Unit> nearUnits = this.getNearUnits();
            //check near units if one of them belongs to player, player may start building
            for(Unit u: nearUnits)
                if(u.getOwner() == player){
                    roundsRemain = 8;
                    mineBuilding = true;
                    walkable = false;
                    player.getRessources()[0] -= 10;  //initial starting cost
                    return true;
                }
        }

        /*TODO check*/
        return false;
    }

    @Override
    public Object select(){
        return (current == null) ? this : current;
    }

    /**
     * Getter und setter
     *
     * @param resType
     */
    @Override
    public void setResType(int resType) {
        if(resType < -1 || resType > 4)
            throw new IllegalArgumentException("That ressource does not exist");

        this.resType = resType;

        this.spriteName = (resType == 0) ? "assets/sprites/forest.png" : (resType == 1) ? "assets/sprites/ironNoMine"+new Random().nextInt(2)+".png" : this.spriteName;
    }

    @Override
    public int getResType() {
        return resType;
    }

    @Override
    public void setResValue(int resValue){
        if(resValue < 0)
            this.resValue = 0;
        else
            this.resValue = resValue;
    }

    @Override
    public int getResValue(){
        return resValue;
    }

    @Override
    public void setXPos(int xPos) {
        if(xPos < 0 || xPos > 8)
            throw new IllegalArgumentException("Invalid coordinates");

        this.xPos = xPos;
    }

    @Override
    public int getXPos() {
        return xPos;
    }

    @Override
    public void setYPos(int yPos) {
        if(yPos < 0 || yPos > 8)
            throw new IllegalArgumentException("Invalid coordinates");

        this.yPos = yPos;
    }

    @Override
    public int getYPos() {
        return yPos;
    }

    @Override
    public void setCurrent(Unit current) {
        this.current = current;
        current.setField(this);
    }

    @Override
    public Unit getCurrent() {
        return current;
    }

    @Override
    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    @Override
    public boolean getWalkable() {
        return walkable;
    }

    @Override
    public void setRoundsRemain(int roundsRemain) {
        if(roundsRemain < -1)
            this.roundsRemain = 0;
        else
            this.roundsRemain = roundsRemain;
    }

    @Override
    public int getRoundsRemain() {
        return roundsRemain;
    }

    //Sprite name must contain sprite folder
    @Override
    public void setSpriteName(String spriteName) {
        if(spriteName.equals(""))
            throw new IllegalArgumentException("SpriteName is empty");

        this.spriteName = spriteName;
        this.texture = new Texture(Gdx.files.internal("assets/"+this.spriteName));
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    @Override
    public boolean getHasMine() {
        return hasMine;
    }

    @Override
    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public Map getMap() {
        return map;
    }


}
