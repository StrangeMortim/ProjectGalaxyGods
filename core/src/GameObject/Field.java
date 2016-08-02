package GameObject;

import Action.Buff;
import Player.Player;
import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Field implements IField,Serializable {

  //  private static final long serialVersionUID = -7532038227070675590L;
    private int resType = Constants.NONE_OR_NOT_SET;
    private int resValue = 0;
    private int xPos = Constants.NONE_OR_NOT_SET;
    private int yPos = Constants.NONE_OR_NOT_SET;
    private Unit current;
    private boolean walkable=true;
    private int roundsRemain = Constants.NONE_OR_NOT_SET;
    private String spriteName = "";
    private boolean baseBuilding = false;
    private Player builder = null;
    private boolean mineBuilding = false;
    private boolean hasMine = false;
    private Map map = null;

    public Field(int resType, int resValue, int xPos, int yPos, Map map){
        if(resType < Constants.NONE_OR_NOT_SET || resType > Constants.MANA || xPos < 0 || xPos > Constants.FIELDXLENGTH || yPos < 0 || yPos >Constants.FIELDYLENGTH || map == null)
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
        this.spriteName = SpriteNames.NORMAL_FIELD.getSpriteName();
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

        if(current != null)
        return current.update();
        else
            return new ArrayList<Buff>();
        /*TODO check*/
    }

    /**
     * Verteilt Ressourcen an alle umstehenden Einheiten, falls das Feld ressourcen besitzt
     */
    private void distributeRessources(){
        /*TODO specify how ressources are distributed*/
        //if field has ressources
        if(resType != Constants.NONE_OR_NOT_SET) {
            List<Unit> nearUnits = null;
            nearUnits = this.getNearUnits();

            Player tmp = null;

            //add ressources to all near units depending on the fields ressource
            switch (resType) {
                case Constants.WOOD:
                    for (Unit u : nearUnits) {
                        tmp = u.getOwner();
                        if(u.type == UnitType.WORKER && tmp.getTurn()) {  //check player
                            u.getRessources()[Constants.WOOD] += Constants.WOOD_RES_VALUE + tmp.getRessourceBoni()[Constants.WOOD];
                            resValue -= (Constants.WOOD_RES_VALUE + tmp.getRessourceBoni()[Constants.WOOD]);
                        }
                    }
                    break;
                case Constants.IRON:
                    if(hasMine) {
                        for (Unit u : nearUnits) {
                            tmp = u.getOwner();
                            if(u.type == UnitType.WORKER && tmp.getTurn()) { //check player
                                u.getRessources()[Constants.IRON] += Constants.IRON_RES_VALUE + tmp.getRessourceBoni()[Constants.IRON];
                                resValue -= (Constants.IRON_RES_VALUE + tmp.getRessourceBoni()[Constants.IRON]);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            //if no more ressources left after  dispersal, set no-ressource values
            if(resValue <= Constants.FINISHED){
                resType = Constants.NONE_OR_NOT_SET;
                resValue = 0;
            }
        }
    }

    /**
     * Aktualisiert die Bauprozesse
     */
    private void updateBuildingProcesses(){
        /*TODO check*/
        if(roundsRemain > Constants.FINISHED){ //there is something to build

            List<Unit> nearUnits = this.getNearUnits();
            if (baseBuilding) {
                for(Unit u: nearUnits)
                    if(u.getOwner() == builder) {       //if it's a base and owner units are near, count down the rounds, -1 for every unit near
                        roundsRemain--;
                        if (roundsRemain == Constants.FINISHED) {        //if the rounds reach zero, build the base and stop
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
                    for(Unit u: nearUnits){
                        if(u.getOwner().getRessources()[Constants.WOOD] >= Building.MINE.getRessourceCost()[Constants.WOOD]){
                            u.getOwner().getRessources()[Constants.WOOD] -= Building.MINE.getRessourceCost()[Constants.WOOD];
                            roundsRemain--;
                        }
                    }
                if(roundsRemain <= Constants.FINISHED) {
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
        if(player == null)
            throw new IllegalArgumentException("Player is null in Buildbase");

        try {
            if(current == null && !hasMine && player.getTechTree().isCultureFull()){
                int[] baseCost = UnitType.BASE.getRessourceCost();
                int[] availableRessources = player.getRessources();
                //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                for(int j = Constants.WOOD; j<=Constants.MANA; ++j)
                    if (baseCost[j] > availableRessources[j])
                        return false;

                List<Unit> nearUnits = this.getNearUnits();
                for(Unit u: nearUnits){
                    if(u.getOwner() == player){
                        baseBuilding = true;
                        roundsRemain = UnitType.BASE.getRecruitingTime();
                        //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                        for(int k = Constants.WOOD; k<=Constants.MANA; ++k)
                            player.getRessources()[k] -= baseCost[k];

                        builder = player;
                        walkable = false;
                        return true;
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
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

                //Order is WOOD=0;IRON=1;GOLDINDEX=2;MANA=3//
                for(int i = Constants.WOOD; i<=Constants.MANA; ++i)
                    player.getRessources()[i] = (int)(ressourcesLeft*originalCost[i]);

                roundsRemain = Constants.NONE_OR_NOT_SET;
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
        if(roundsRemain == Constants.NONE_OR_NOT_SET && current == null && resType == Constants.IRON){
            List<Unit> nearUnits = this.getNearUnits();
            //check near units if one of them belongs to player, player may start building
            for(Unit u: nearUnits)
                if(u.getOwner() == player){
                    roundsRemain = Building.MINE.getBuildTime();
                    mineBuilding = true;
                    walkable = false;
                    player.getRessources()[Constants.WOOD] -= Building.MINE.getInitialCost()[Constants.WOOD];  //initial starting cost
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
        if(resType < Constants.NONE_OR_NOT_SET || resType > Constants.MANA)
            throw new IllegalArgumentException("That ressource does not exist");



        this.resType = resType;
        walkable=false;
        if(resType==Constants.MANA) {walkable=true;}

        this.spriteName = (resType == Constants.WOOD) ? SpriteNames.FOREST.getSpriteName()
                        : (resType == Constants.IRON) ? SpriteNames.IRON_FIELD.getSpriteName()
                        : (resType == Constants.MANA) ? SpriteNames.MIRACLE.getSpriteName()
                        : SpriteNames.NORMAL_FIELD.getSpriteName();
        
    }

    @Override
    public int getResType() {
        return resType;
    }

    @Override
    public void setResValue(int resValue){
        if(resValue < 0) {
            this.resValue = 0;
            this.setResType(Constants.NONE_OR_NOT_SET);
        }else
            this.resValue = resValue;


    }

    @Override
    public int getResValue(){
        return resValue;
    }

    @Override
    public void setXPos(int xPos) {
        if(xPos < 0 || xPos > Constants.FIELDXLENGTH)
            throw new IllegalArgumentException("Invalid coordinates");

        this.xPos = xPos;
    }

    @Override
    public int getXPos() {
        return xPos;
    }

    @Override
    public void setYPos(int yPos) {
        if(yPos < 0 || yPos > Constants.FIELDYLENGTH)
            throw new IllegalArgumentException("Invalid coordinates");

        this.yPos = yPos;
    }

    @Override
    public int getYPos() {
        return yPos;
    }

    @Override
    public void setCurrent(Unit current) {
        if(current==null){
            walkable=true;
            setSpriteName(SpriteNames.NORMAL_FIELD.getSpriteName());
            this.current=null;
            return;
        }
        this.current = current;
        current.setField(this);
        if(current.getSpriteName()!="")
        setSpriteName(current.getSpriteName());


        if(resType == Constants.MANA){
            current.getOwner().getRessources()[Constants.MANA] += resValue;
            resValue = 0;
        }
        walkable=false;
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
        if(roundsRemain < Constants.NONE_OR_NOT_SET)
            this.roundsRemain = Constants.NONE_OR_NOT_SET;
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
       // this.texture = new Texture(Gdx.files.internal("assets/"+this.spriteName));
    }

    @Override
    public String getSpriteName() {
        return spriteName;
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
