package screens;

import GameObject.*;
import GameObject.Field;
import Player.Player;
import Player.TreeElement;
import chat.Chat;
import chat.Message;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.reflect.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import javafx.scene.text.Font;

import java.rmi.RemoteException;
import java.util.*;
import java.util.List;


/**
 * Der Screen fuer ein Spiel...
 *
 * Created by benja_000 on 03.07.2016.
 */
public class GameScreen implements Screen, InputProcessor{

    private Game game;
    private GameSession session;
    private Field[][] map = null;
    private boolean unrendered = true;
    private Object selected;
    private Player player;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    Texture bg = new Texture(Gdx.files.internal("assets/sprites/normal0.png"));
    Texture bg2 = new Texture(Gdx.files.internal("assets/sprites/normal1.png"));
    OrthographicCamera camera;
    int[][] fields = new int[26][24];
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Table table;

    //region Chat
    private Table chatTable;
    private TextButton sendMessageButton;
    private TextField messageField;
    private Table backLog;
    private Label userName;
    private ScrollPane chatScroller;
    private int lastMessageCount;
    //endregion

    private Label label1,label2,label3,label4,label5;

    //region Bottomtable
    private Table bottomTable;
    Label unitName, unitHp, unitRange, unitAtk, unitDef, unitMPoints, unitOwner;
    private TextButton selectionUpLeft, selectionUpRight, selectionDownLeft, selectionDownRight,
                        marketPlace, techTree, finishRound;
    private boolean baseRecruitButtons = false;
    private boolean laborEntered = false;
    private boolean caserneEntered = false;
//endregion

    //region Market
    private Table marketTable;
    private Label woodPrice, woodAmount, woodError, ironPrice, ironAmount, ironError, connectionError;
    private TextField woodField, ironField;
    private TextButton buyButton, sellButton, closeButton;
    //endregion

    //region Techtree
    private Table treeTable;
    private TextButton steelButtonLv1,steelButtonLv2,steelButtonLv3,steelButtonLv4,steelButtonLv5,
                       magicButtonLv1,magicButtonLv2,magicButtonLv3,magicButtonLv4,magicButtonLv5,
                       cultureButtonLv1,cultureButtonLv2,cultureButtonLv3,cultureButtonLv4,cultureButtonLv5,
                       treeCloseButton;
    private Label steelPath,steelInfo, magicPath, magicInfo, culturePath,cultureInfo;
    //endregion

    //region Textures
    private Texture[] textures = new Texture[]{new Texture(Gdx.files.internal(SpriteNames.NORMAL_FIELD.getSpecificName(0))),//0
            new Texture(Gdx.files.internal(SpriteNames.NORMAL_FIELD.getSpecificName(1))),        //1
            new Texture(Gdx.files.internal(SpriteNames.FOREST.getSpriteName())),      //2
            new Texture(Gdx.files.internal(SpriteNames.IRON_FIELD.getSpecificName(0))),//3
            new Texture(Gdx.files.internal(SpriteNames.BASE_UP_RIGHT.getSpriteName())),//4
            new Texture(Gdx.files.internal(SpriteNames.BASE_UP_LEFT.getSpriteName())),//5
            new Texture(Gdx.files.internal(SpriteNames.BASE_DOWN_RIGHT_FULL.getSpriteName())),//6
            new Texture(Gdx.files.internal(SpriteNames.BASE_DOWN_RIGHT_LAB.getSpriteName())),//7
            new Texture(Gdx.files.internal(SpriteNames.BASE_DOWN_RIGHT_CASERNE.getSpriteName())),//8
            new Texture(Gdx.files.internal(SpriteNames.BASE_DOWN_RIGHT_EMPTY.getSpriteName())),//9
            new Texture(Gdx.files.internal(SpriteNames.BASE_DOWN_LEFT_CASERNE.getSpriteName())),//10
            new Texture(Gdx.files.internal(SpriteNames.BASE_DOWN_LEFT_EMPTY.getSpriteName())),//11
            new Texture(Gdx.files.internal(SpriteNames.NORMAL_FIELD.getSpecificName(2))),//12
            new Texture(Gdx.files.internal(SpriteNames.IRON_FIELD.getSpecificName(1))),//13
            new Texture(Gdx.files.internal(SpriteNames.GOLD_ICON.getSpriteName())),//14
            new Texture(Gdx.files.internal(SpriteNames.WOOD_ICON.getSpriteName())),//15
            new Texture(Gdx.files.internal(SpriteNames.IRON_ICON.getSpriteName())),//16
            new Texture(Gdx.files.internal(SpriteNames.MANA_ICON.getSpriteName())),//17
            new Texture(Gdx.files.internal(SpriteNames.CHEST_ICON.getSpriteName())),//18
            new Texture(Gdx.files.internal(SpriteNames.MENU_BG.getSpriteName())),//19
            new Texture(Gdx.files.internal(SpriteNames.MARKETPLACE.getSpriteName())),//20
            new Texture(Gdx.files.internal(SpriteNames.BUTTON_WORKER.getSpriteName())), // 21
            new Texture(Gdx.files.internal(SpriteNames.BUTTON_BG.getSpriteName())), // 22
            new Texture(Gdx.files.internal(SpriteNames.SPEARFIGHTER.getSpriteName())),//23
            new Texture(Gdx.files.internal(SpriteNames.SWORDFIGHTER.getSpriteName())),//24
            new Texture(Gdx.files.internal(SpriteNames.WORKER.getSpriteName())),//25
            new Texture(Gdx.files.internal(SpriteNames.HERO.getSpriteName())),//26
            new Texture(Gdx.files.internal(SpriteNames.BUTTON_SPEARFIGHTER.getSpriteName())),//27
            new Texture(Gdx.files.internal(SpriteNames.BUTTON_SWORDFIGHTER.getSpriteName())),//28
            new Texture(Gdx.files.internal(SpriteNames.BUTTON_GREY.getSpriteName())),//29
            new Texture(Gdx.files.internal(SpriteNames.BUTTON_GOLD.getSpriteName())),//30
            new Texture(Gdx.files.internal(SpriteNames.BUTTON_BLUE.getSpriteName())),//31
            new Texture(Gdx.files.internal(SpriteNames.SPEARFIGHTERBACK.getSpriteName())),//32
            new Texture(Gdx.files.internal(SpriteNames.SWORDFIGHTERBACK.getSpriteName())),//33
            new Texture(Gdx.files.internal(SpriteNames.WORKERBACK.getSpriteName())),//34
            new Texture(Gdx.files.internal(SpriteNames.HEROBACK.getSpriteName())),//35
            new Texture(Gdx.files.internal(SpriteNames.TECHTREE.getSpriteName())),//36


    };
    //endregion

    ParticleEffect pe; //For fighting scenes
    ParticleEffect shield;

    public  GameScreen(Game game, GameSession session, Player player){

        this.player=player;
        batch=new SpriteBatch();
        this.session = session;
      this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        //bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
     int x =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[0]);
     int y =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[1]);
        camera= new OrthographicCamera(x,y);
        camera.translate(x/2,y/2);
        camera.setToOrtho(false,x,y);

        try{
            map = session.getMap().getFields();
//TODO: Irgendwann entfernen
            Unit testUnit = new Unit(UnitType.SPEARFIGHTER, this.player);
            testUnit.setMovePointsLeft(8);
            testUnit.setSpriteName(SpriteNames.SPEARFIGHTER.getSpriteName());
            testUnit.setOwner(this.player);
            map[5][5].setCurrent(testUnit);

            Hero testUnit2 = new Hero(UnitType.HERO,this.player,"harald");
            testUnit2.setMovePointsLeft(8);
            testUnit2.setSpriteName(SpriteNames.HERO.getSpriteName());
            testUnit2.setOwner(this.player);
            testUnit2.setCurrentHp(2);
            map[6][6].setCurrent(testUnit2);
            Sound testMusic = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/song1.wav"));
            testMusic.play();
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
            e.printStackTrace();

            Random r = new Random();
            for(int i=0;i<Constants.FIELDXLENGTH;++i)
                for(int j=0;j<Constants.FIELDYLENGTH;++j)
                    fields[i][j] = r.nextInt(2);
        }


    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        showTopMenu();
        showBottomMenu();
        showChat();
        showMarket();
        showTechtree();
        buildListeners();

        InputMultiplexer im = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(im);
    }



    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        int batchWidth = Constants.FIELDXLENGTH*100;
        int batchHeight = Constants.FIELDYLENGTH*100;
        int i=0;
        int j=0;

        if(pe!=null) {
            pe.update(Gdx.graphics.getDeltaTime());

            if (pe.isComplete()){
            pe.dispose();
            pe=null;}
        }
        if(shield!=null)shield.update(Gdx.graphics.getDeltaTime());

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        label1.setText(player.getRessources()[Constants.GOLD]+"");
        label2.setText(player.getRessources()[Constants.WOOD]+"");
        label3.setText(player.getRessources()[Constants.IRON]+"");
        label4.setText(player.getRessources()[Constants.MANA]+"");

        //region Buttonumstellungen fuer Auswahlbuttons
        try{
            if(unrendered) {
                if (selected instanceof Unit) {
                    unitAtk.setText("ATK: " + ((Unit) selected).getAtk());
                    unitDef.setText("DEF: " + ((Unit) selected).getDef());
                    unitHp.setText("HP: " + ((Unit) selected).getCurrentHp() + "/" + ((Unit) selected).getMaxHp());
                    unitMPoints.setText("BP: " + ((Unit) selected).getMovePointsLeft() + "/" + ((Unit) selected).getMovePoints());
                    unitName.setText("NAME: " + ((Unit) selected).getType().toString());
                    unitRange.setText("RW: " + ((Unit) selected).getRange());
                    unitOwner.setText("SPIELER: " + ((Unit) selected).getOwner().getAccount().getName());
                } else {
                    unitAtk.setText("");
                    unitDef.setText("");
                    unitHp.setText("");
                    unitMPoints.setText("");
                    unitName.setText("");
                    unitRange.setText("");
                    unitOwner.setText("");
                }

                if (selected instanceof Base && ((Unit)selected).getOwner() == player) {
                    if (baseRecruitButtons) {
                        selectionUpLeft.setVisible(true);
                        selectionUpLeft.setText("");
                        selectionUpLeft.setTouchable(Touchable.enabled);
                        selectionUpLeft.getStyle().up = skin.getDrawable("swordfighterIcon");
                        selectionUpRight.setVisible(((Base)selected).getAvaibleUnits().contains(UnitType.SPEARFIGHTER));
                        selectionUpRight.setText("");
                        selectionUpRight.getStyle().up = skin.getDrawable("spearfighterIcon");
                        selectionDownLeft.setVisible(((Base)selected).getAvaibleUnits().contains(UnitType.ARCHER));
                        selectionDownLeft.setText("Bogenschuetze");
                        selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionDownRight.setVisible(true);
                        selectionDownRight.setText("");
                        selectionDownRight.getStyle().up = skin.getDrawable("workerIcon");
                    } else if(laborEntered) {
                        selectionUpLeft.setVisible(true);
                        selectionUpLeft.setText("Buff oder so");
                        selectionUpLeft.setTouchable(Touchable.enabled);
                        selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionUpRight.setVisible(true);
                        selectionUpRight.setText("anderer Buff oder so");
                        selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionDownLeft.setVisible(true);
                        selectionDownLeft.setText("Bogenschuetze erforschen");
                        selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionDownRight.setVisible(true);
                        selectionDownRight.setText("Speerkaempfer erforschen");
                        selectionDownRight.getStyle().up = skin.getDrawable("defaultIcon");
                    } else  {
                        selectionUpLeft.setVisible(true);

                        if(((Base)selected).getLabRoundsRemaining() == Constants.FINISHED)
                                selectionUpLeft.setText("Labor");
                        else if(((Base)selected).getLabRoundsRemaining() != Constants.NONE_OR_NOT_SET)
                            selectionUpLeft.setText("Labor wird gebaut");
                            else
                                selectionUpLeft.setText("Labor bauen");

                            selectionUpLeft.setTouchable(Touchable.enabled);
                            selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");

                            selectionUpRight.setVisible(true);

                        if(((Base)selected).getCaserneRoundsRemaining() == Constants.FINISHED)
                            selectionUpRight.setText("Kaserne");
                         else if(((Base)selected).getCaserneRoundsRemaining() != Constants.NONE_OR_NOT_SET)
                                selectionUpRight.setText("Kaserne wird gebaut");
                        else
                            selectionUpRight.setText("Kaserne bauen");

                            selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                            selectionDownLeft.setVisible(false);
                            selectionDownLeft.setText("Einheiten Rekrutieren");
                            selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                            selectionDownRight.setVisible(!player.getMarket());
                            selectionDownRight.setText("Marktplatz bauen");
                            selectionDownRight.getStyle().up = skin.getDrawable("defaultIcon");
                        }
                } else if (selected instanceof Hero) {
                    selectionUpLeft.setVisible(true);
                    selectionUpLeft.setText("Heldenfaehigkeit links");
                    selectionUpLeft.setTouchable(Touchable.enabled);
                    selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");
                    selectionUpRight.setVisible(true);
                    selectionUpRight.setText("Heldenfaehigkeit rechts");
                    selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                    selectionDownLeft.setVisible(false);
                    selectionDownRight.setVisible(false);
                 if(shield!=null)shield.getEmitters().first().setPosition(((Hero) selected).getField().getXPos() * 100 + 50, ((Hero) selected).getField().getYPos() * 100 + 50);
                } else if (selected instanceof Field) {
                    selectionUpLeft.setVisible(true);
                    selectionUpLeft.setText("Mine bauen");
                    selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");

                    if (((Field) selected).getResType() != Constants.IRON)
                        selectionUpLeft.setTouchable(Touchable.disabled);
                    else
                        selectionUpLeft.setTouchable(Touchable.enabled);

                    selectionUpRight.setVisible(true);
                    selectionUpRight.setText("Basis bauen");
                    selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                    selectionDownLeft.setVisible(false);
                    selectionDownRight.setVisible(false);
                } else {
                    selectionUpLeft.setVisible(false);
                    selectionUpRight.setVisible(false);
                    selectionDownLeft.setVisible(false);
                    selectionDownRight.setVisible(false);
                }
            }
        unrendered = false;


        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        //endregion

        buildChatString();

        //region Kamerabeweung
        //Move screen right
        if((Gdx.input.getX()>=(Gdx.graphics.getWidth()-10) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))&&camera.position.x<batchWidth)
        {camera.position.set(camera.position.x+10, camera.position.y, 0);
         }

        //Move screen left
        if((Gdx.input.getX()<=(10)|| Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) &&camera.position.x>0)
        {camera.position.set(camera.position.x-10, camera.position.y, 0);

        }

        //Move screen down
        if((Gdx.input.getY()>=(Gdx.graphics.getHeight()-10)|| Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))&&camera.position.y>0)
        {camera.position.set(camera.position.x,camera.position.y-10, 0);

        }

        //Move screen up
        if((Gdx.input.getY()<=(10)|| Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))&&camera.position.y<batchHeight)
        {camera.position.set(camera.position.x,camera.position.y+10, 0);
           }
        camera.update();
        //endregion

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        //region Felder zeichnen
        String textureName = "";
        int textureIndex = 0;
        try{
            for(Field[] f: map){
                for(Field f2: f){
                    textureName = f2.getSpriteName();
                    switch (textureName){
                        case "assets/sprites/normal0.png":
                            textureIndex = 0;
                        break;
                        case "assets/sprites/normal1.png":
                            textureIndex = 1;
                        break;
                        case "assets/sprites/forest.png":
                            textureIndex = 2;
                        break;
                        case "assets/sprites/ironNoMine0.png":
                            textureIndex = 3;
                        break;
                        case "assets/sprites/baseFullRight.png":
                            textureIndex = 4;
                        break;
                        case "assets/sprites/baseFullLeft.png":
                            textureIndex = 5;
                        break;
                        case "assets/sprites/baseDownRightFull.png":
                            textureIndex = 6;
                        break;
                        case "assets/sprites/baseDownRightLabor.png":
                            textureIndex = 7;
                        break;
                        case "assets/sprites/baseDownRightCaserne.png":
                            textureIndex = 8;
                        break;
                        case "assets/sprites/baseDownRightEmpty.png":
                            textureIndex = 9;
                        break;
                        case "assets/sprites/baseDownLeftCaserne.png":
                            textureIndex = 10;
                        break;
                        case "assets/sprites/baseDownLeftEmpty.png":
                            textureIndex = 11;
                        break;
                        case "assets/sprites/normal2.png":
                            textureIndex = 12;
                            break;
                        case "assets/sprites/ironNoMine1.png":
                            textureIndex = 13;
                            break;
                        case "assets/sprites/spearfighter.png":
                            textureIndex = 23;
                            break;
                        case "assets/sprites/swordfighter.png":
                            textureIndex = 24;
                            break;
                        case "assets/sprites/worker.png":
                            textureIndex = 25;
                            break;
                        case "assets/sprites/hero.png":
                            textureIndex = 26;
                            break;
                        case "assets/sprites/spearfighterBack.png":
                            textureIndex = 32;
                            break;
                        case "assets/sprites/swordfighterBack.png":
                            textureIndex = 33;
                            break;
                        case "assets/sprites/workerBack.png":
                            textureIndex = 34;
                            break;
                        case "assets/sprites/heroBack.png":
                            textureIndex = 35;
                            break;
                        default:
                            textureIndex = 0;
                            break;
                    }
                    batch.draw(textures[textureIndex], 100*i,100*j,100,100);
                            j++;
                }
                j=0;
                i++;
            }
        }catch (NullPointerException e) {
            System.out.println("GameScreen 370: " + e.getMessage());
            for (i = 0; i < Constants.FIELDXLENGTH; ++i)
                for (j = 0; j < Constants.FIELDYLENGTH; ++j) {

                    if (fields[i][j] == 0)
                        batch.draw(bg, 100 * i, 100 * j, 100, 100);
                    else
                        batch.draw(bg2, 100 * i, 100 * j, 100, 100);

                }
        }
        //endregion

//testweise
        if(pe!=null)
        pe.draw(batch);
        if(shield!=null)
        shield.draw(batch);
        batch.end();

        showMovementRange();


        Vector3 vector=camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float x = vector.x > batchWidth ? batchWidth-100 : vector.x < 0 ? 0 : vector.x;
        float y = vector.y > batchHeight ? batchHeight-100 : vector.y < 0 ? 0 : vector.y;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(((int)x/100)*100, ((int)y/100)*100, 100, 100);
        shapeRenderer.end();

        stage.act();
        stage.draw();
    }

    public boolean showUnitRadius(){

        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link //Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        try {
            switch (button) {
                case Input.Buttons.LEFT:
                    selected = map[getFieldXPos(Constants.FIELDXLENGTH*100)][getFieldYPos(Constants.FIELDYLENGTH*100)].select(); //TODO batchBounds->attribute
                    baseRecruitButtons = false;
                    laborEntered = false;
                    unrendered = true;
                    return true;
                case Input.Buttons.RIGHT:
                    baseRecruitButtons = false;
                    laborEntered = false;
                    unrendered = true;
                    move();
                    System.out.println("No action assigned");
                    return true;
                case Input.Buttons.MIDDLE:
                    baseRecruitButtons = false;
                    laborEntered = false;
                    selected = null;
                    unrendered = true;
                    System.out.println("No action assigned");
                    return true;
                default:
                    return false;
            }
        } catch (Exception e){
            System.out.println("Probably everything ok, just dummies missing: GameScreen - touchDown");
            e.printStackTrace();
        }
        return false;
    }

    private int getFieldXPos(int scrWidth){
        Vector3 input = camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
        return (int)((input.x > scrWidth ? scrWidth : input.x < 0 ? 0 : input.x)/100);
    }

    private int getFieldYPos(int scrHeight){
        Vector3 input = camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
        return (int)((input.y > scrHeight ? scrHeight : input.y < 0 ? 0 : input.y)/100);
    }


    /**
     * Zeigt den Bewegungsradius eigener Einheiten an.
     */
    public void showMovementRange() {
        if (selected != null && selected instanceof Unit) {
            if (((Unit) selected).getOwner() != null&&((Unit) selected).getType() != UnitType.BASE
                    && ((Unit) selected).getOwner() == player) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GREEN);
                int radius = ((Unit) selected).getMovePointsLeft();
                for (int x = 0 - radius; x < radius + 1; x++) {
                    for (int y = 0 - radius; y < radius + 1; y++) {
                        if (((Unit) selected).getField().getXPos() * 100 + x * 100 >= 0
                                && ((Unit) selected).getField().getYPos() * 100 + y * 100 >= 0
                                && ((Unit) selected).getField().getYPos() * 100 + y * 100 <= Constants.FIELDYLENGTH*100-100
                                && ((Unit) selected).getField().getXPos() * 100 + x * 100 <= Constants.FIELDXLENGTH*100-100)

                            if(map[((Unit) selected).getField().getXPos()+x][((Unit) selected).getField().getYPos()+y].getWalkable()) {
                                shapeRenderer.rect(((Unit) selected).getField().getXPos() * 100 + x * 100, ((Unit) selected).getField().getYPos() * 100 + y * 100, 100, 100);

                            }/*else{
                                shapeRenderer.setColor(Color.RED);
                                shapeRenderer.rect(((Unit) selected).getField().getXPos() * 100 + x * 100, ((Unit) selected).getField().getYPos() * 100 + y * 100, 100, 100);
                                shapeRenderer.setColor(Color.GREEN);
                            }*/
                    }
                }
                shapeRenderer.end();
            }
        }
    }

    /**
     * Zeigt die obere Menuebar an.
     */
    public void showTopMenu(){

        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.left|Align.top);
        table.setPosition(10, Gdx.graphics.getHeight());
        HorizontalGroup group = new HorizontalGroup();
        Image image =new Image(textures[14]);//Gold
        player.getRessources()[2]=10000;
        label1 = new Label(player.getRessources()[2]+"", skin);label1.setColor(Color.WHITE);
        group.addActor(image);
        group.addActor(label1);
        Image image2 =new Image(textures[15]);//Holz
        label2 = new Label(player.getRessources()[0]+"", skin);label2.setColor(Color.WHITE);
        group.addActor(image2);
        group.addActor(label2);
        Image image3 =new Image(textures[16]);//Eisen
        label3 = new Label(player.getRessources()[1]+"", skin);label3.setColor(Color.WHITE);
        group.addActor(image3);
        group.addActor(label3);
        Image image4 =new Image(textures[17]);//Mana
        label4 = new Label(player.getRessources()[3]+"", skin);label4.setColor(Color.WHITE);
        group.addActor(image4);
        group.addActor(label4);
        label5 = new Label("9000", skin);label4.setColor(Color.WHITE);
        Image image5 =new Image(textures[18]);//Teamkasse
        group.addActor(image5);
        group.addActor(label5);
        group.space(10);
        table.add(group);
        Table optionTable= new Table();
        optionTable.padRight(10);
        optionTable.setVisible(true);
        TextButton optionen = new TextButton("Optionen",skin);
        optionen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(optionTable.isVisible()){
                    optionTable.setVisible(false);
                }else{
                    optionTable.setVisible(true);
                }
            }
        });
        TextButton einstellungen = new TextButton("Einstellungen",skin);
        einstellungen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new OptionScreen(game,(GameScreen) game.getScreen()));
            }
        });
        TextButton aufgeben = new TextButton("Aufgeben",skin);
        TextButton beenden = new TextButton("Beenden",skin);
        optionTable.add(einstellungen);
        optionTable.row();
        optionTable.add(aufgeben);
        optionTable.row();
        optionTable.add(beenden);
        optionTable.setPosition(Gdx.graphics.getWidth()-70,Gdx.graphics.getHeight()-80);

        table.add(optionen).spaceLeft(stage.getWidth()/4);
        table.row();

        stage.addActor(table);
        stage.addActor(optionTable);


    }

    private void showBottomMenu(){
        NinePatch tmp = null;
        TextButton.TextButtonStyle style = null;

        tmp = new NinePatch(textures[22], 0, 0, 0, 0);
        skin.add("defaultIcon",tmp);
        tmp = new NinePatch(textures[21], 0, 0, 0, 0);
        skin.add("workerIcon",tmp);
        tmp = new NinePatch(textures[20], 0, 0, 0, 0);
        skin.add("marketIcon",tmp);
        tmp = new NinePatch(textures[19], 10, 10, 10, 10);
        skin.add("background",tmp);
        tmp = new NinePatch(textures[27], 10, 10, 10, 10);
        skin.add("spearfighterIcon",tmp);
        tmp = new NinePatch(textures[28], 10, 10, 10, 10);
        skin.add("swordfighterIcon",tmp);
        tmp = new NinePatch(textures[36], 10, 10, 10, 10);
        skin.add("treeBackground",tmp);


        ////Grundlegende Tabelle////
        bottomTable=new Table();
        bottomTable.setWidth(stage.getWidth());
        bottomTable.setHeight(stage.getHeight()/6);
        bottomTable.setPosition(0,0);
        bottomTable.align(Align.bottomLeft);

        ////Innere Tabellen initialisieren////
        Table stats = new Table();
        stats.setHeight(stage.getHeight()/6);
        stats.setPosition(0,0);
        stats.align(Align.bottomLeft);
        Table selection = new Table();
        Table  buttons= new Table();

        ///////////Statistik Tabelle///////////////
        ////Label generieren////
        unitName=new Label("",skin);
        unitRange= new Label("",skin);
        unitAtk=new Label("",skin);
        unitDef=new Label("",skin);
        unitMPoints=new Label("",skin);
        unitOwner=new Label("",skin);
        unitHp=new Label("",skin);

        ////Tabelle bauen////
        stats.row().fill().expand();
        stats.add(unitName);
        stats.row().fill().expand();
        stats.add(unitHp);
        stats.add(unitMPoints);
        stats.row().fill().expand();
        stats.add(unitAtk);
        stats.add(unitDef);
        stats.row().fill().expand();
        stats.add(unitRange);
        stats.add(unitOwner);
        stats.setWidth(stage.getWidth()/6);
        stats.setScaleX(stage.getWidth()/6);
       // stats.debugAll();

        /////////////Auswahltabelle/////////////
        ////Damit die Buttons nicht mit null anfangen////
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        selectionUpLeft = new TextButton("Oben links",style);
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        selectionUpRight = new TextButton("Oben rechts",style);
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        selectionDownLeft = new TextButton("Unten links",style);
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        selectionDownRight = new TextButton("Unten rechts",style);

        /////Tabelle bauen/////
        float buttonHeight = bottomTable.getHeight()/3;
        float buttonWidth = bottomTable.getHeight()/2;
        selection.padLeft(40);
        selection.row().fill().expand().space(10).size(buttonWidth,buttonHeight);
        selection.add(selectionUpLeft);
        selection.add(selectionUpRight);
        selection.row().fill().expand().space(10).size(buttonWidth,buttonHeight);
        selection.add(selectionDownLeft);
        selection.add(selectionDownRight);
        selection.setHeight(bottomTable.getHeight());

        //////////////Buttontabelle//////////////
        ////Buttons != null////
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        marketPlace = new TextButton("",style);
        marketPlace.getStyle().up = skin.getDrawable("marketIcon");
        marketPlace.getStyle().down = skin.getDrawable("marketIcon");
        marketPlace.setTouchable(Touchable.disabled);
        marketPlace.setVisible(false);
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        techTree = new TextButton("Technologiebaum",style);
        techTree.getStyle().up = skin.getDrawable("defaultIcon");
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        finishRound = new TextButton("Runde beenden",style);
        finishRound.getStyle().up = skin.getDrawable("defaultIcon");



        /////Tabelle bauen/////
        buttons.padLeft(10);
        buttons.align(Align.right);
        buttons.row().fill().expand().space(10).size(bottomTable.getHeight()*(float)0.8,bottomTable.getHeight()*(float)0.75);
        buttons.add(marketPlace);
//        buttons.row().fill().expand().space(10);
        buttons.add(techTree);
        buttons.add(finishRound);
        buttons.setHeight(bottomTable.getHeight());

        bottomTable.setBackground(skin.getDrawable("background"));
        bottomTable.add(stats).minWidth(bottomTable.getWidth()/4);
        bottomTable.add(selection).minWidth(bottomTable.getWidth()/3);
        bottomTable.add(buttons).minWidth(bottomTable.getWidth()/4);
        bottomTable.add(finishRound).padLeft(50);
        stage.addActor(bottomTable);
    }

    private void showChat(){
        chatTable = new Table();
        chatTable.setBounds(Gdx.graphics.getWidth()*4/5,Gdx.graphics.getHeight()/6,Gdx.graphics.getWidth()*1/5,Gdx.graphics.getHeight()/4);
        chatTable.align(Align.bottomLeft);
        sendMessageButton = new TextButton("Senden",skin);
        messageField = new TextArea("",skin);

        try {
            lastMessageCount = session.getSessionChat().getBacklog().size();
            session.getSessionChat().addParticipant(player); //TODO rausnehmen nicht vergessen
        } catch (RemoteException e){
            System.out.println(e.getMessage());
        }

        //backLog = new List(skin);
        backLog = new Table();
        backLog.align(Align.left);
        backLog.row().fill().expandX().align(Align.left).height(skin.getFont("default-font").getLineHeight());
        this.buildChatString();
        chatScroller = new ScrollPane(backLog,skin);
        chatScroller.setFadeScrollBars(false);
        chatTable.add(chatScroller).expand().fill().colspan(2);
        chatTable.row().fill();
        userName = new Label(player.getAccount().getName(),skin);

        chatTable.add(messageField);
        chatTable.add(sendMessageButton);
        stage.addActor(chatTable);
    }

    private void buildChatString(){
        try {
            Chat chat = session.getSessionChat();
            ArrayList<Message> backLogTmp = new ArrayList<>(chat.getBacklog());
            if(backLogTmp.size() > lastMessageCount && (chat.getParticipants().isEmpty() || chat.getParticipants().contains(player))) {
                Label tmp;
                for(int i=lastMessageCount; i<backLogTmp.size(); ++i){
                    if(backLogTmp.get(i).getVisibleForAll() || backLogTmp.get(i).getVisibleFor().contains(player)){
                        tmp = new Label(backLogTmp.get(i).getContent(),skin);
                        tmp.setWrap(true);
                        backLog.add(tmp);
                        backLog.row().fill().expandX().align(Align.left).height(tmp.getHeight());
                        chatScroller.layout();
                        chatScroller.setScrollPercentY(100);
                    }
                }
                lastMessageCount = backLogTmp.size();
            }
        } catch (RemoteException e){
            System.out.println(e.getMessage());
        }
    }

    private void showMarket(){
        marketTable = new Table();
        marketTable.align(Align.left);
        marketTable.setPosition(stage.getWidth()/3, stage.getHeight()/2);
        marketTable.setWidth(stage.getWidth()/3);
        marketTable.setHeight(stage.getHeight()/3);


        try {
            Market tmp = session.getMarket();

            woodAmount = new Label("Holz: " + tmp.getWood(),skin);
            woodPrice = new Label("Preis: " + tmp.woodPrice(),skin);

            ironAmount = new Label("Eisen: " + tmp.getIron(),skin);
            ironPrice = new Label("Preis: " + tmp.ironPrice(),skin);

        } catch (RemoteException e) {
            woodAmount = new Label("Holz: n/a",skin);
            woodPrice = new Label("Preis: n/a",skin);

            ironAmount = new Label("Eisen: n/a",skin);
            ironPrice = new Label("Preis: n/a",skin);

            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        woodField = new TextField("",skin);
        woodField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        woodError = new Label("Nicht genug Ressourcen",skin);
        woodError.setColor(Color.RED);
        woodError.setVisible(false);

        ironField = new TextField("",skin);
        ironField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        ironError = new Label("Nicht genug Ressourcen",skin);
        ironError.setColor(Color.RED);
        ironError.setVisible(false);

        connectionError = new Label("Keine Verbindung zum Marktplatz",skin);
        connectionError.setColor(Color.RED);
        connectionError.setVisible(false);

        buyButton = new TextButton("Kaufen",skin);
        sellButton = new TextButton("Verkaufen",skin);
        closeButton = new TextButton("Schliessen",skin);

        marketTable.row().fill().space(10);
        marketTable.add(woodAmount);
        marketTable.add(woodPrice);
        marketTable.add(woodField);
        marketTable.row().fill().space(10);
        marketTable.add(woodError);
        marketTable.row().fill().space(10);
        marketTable.add(ironAmount);
        marketTable.add(ironPrice);
        marketTable.add(ironField);
        marketTable.row().fill().space(10);
        marketTable.add(ironError);
        marketTable.row().fill().space(10);
        marketTable.add(buyButton);
        marketTable.add(sellButton);
        marketTable.add(closeButton);
        marketTable.row().fill().space(10);
        marketTable.add(connectionError).colspan(2);
        marketTable.setBackground(skin.getDrawable("background"));
        marketTable.setVisible(false);
        stage.addActor(marketTable);
    }

    private void showTechtree(){
        NinePatch tmp = null;
        TextButton.TextButtonStyle style = null;

        //TODO change Textureindex
        tmp = new NinePatch(textures[29], 0, 0, 0, 0);
        skin.add("defaultIcon",tmp);
        tmp = new NinePatch(textures[29], 0, 0, 0, 0);
        skin.add("steelIcon",tmp);
        tmp = new NinePatch(textures[31], 0, 0, 0, 0);
        skin.add("magicIcon",tmp);
        tmp = new NinePatch(textures[30], 0, 0, 0, 0);
        skin.add("cultureIcon",tmp);

        treeTable = new Table();
        treeTable.align(Align.left);
        treeTable.setPosition(stage.getWidth()/12, stage.getHeight()/3);
        treeTable.setWidth(stage.getWidth()/4*3);
        treeTable.setHeight(stage.getHeight()/5*3);

        //Steel
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        style.up = skin.getDrawable("steelIcon");
        style.fontColor = Color.WHITE;
        steelButtonLv1 = new TextButton("St.1",style);
        steelButtonLv2 = new TextButton("St.2",style);
        steelButtonLv2.setVisible(false);
        steelButtonLv3 = new TextButton("St.3",style);
        steelButtonLv3.setVisible(false);
        steelButtonLv4 = new TextButton("St.4",style);
        steelButtonLv4.setVisible(false);
        steelButtonLv5 = new TextButton("St.5",style);
        steelButtonLv5.setVisible(false);
        steelPath = new Label("Zweig des Stahl: ",skin);
        steelPath.setColor(Color.LIGHT_GRAY);
        steelInfo = new Label("Jede Stufe erhoeht das gewonnene Eisen und Holz um 5%",skin);
        steelInfo.setColor(Color.LIGHT_GRAY);

        //MagicButtons
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        style.up = skin.getDrawable("magicIcon");
        style.fontColor = Color.WHITE;
        magicButtonLv1 = new TextButton("St.1",style);
        magicButtonLv2 = new TextButton("St.2",style);
        magicButtonLv2.setVisible(false);
        magicButtonLv3 = new TextButton("St.3",style);
        magicButtonLv3.setVisible(false);
        magicButtonLv4 = new TextButton("St.4",style);
        magicButtonLv4.setVisible(false);
        magicButtonLv5 = new TextButton("St.5",style);
        magicButtonLv5.setVisible(false);
        magicPath = new Label("Zweig der Magie: ",skin);
        magicPath.setColor(Color.ROYAL);
        magicInfo = new Label("Jede Stufe verringert den Mana-Verbrauch um 3%",skin);
        magicInfo.setColor(Color.ROYAL);

        //CultureButtons
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        style.up = skin.getDrawable("cultureIcon");
        style.fontColor = Color.WHITE;
        cultureButtonLv1 = new TextButton("St.1",style);
        cultureButtonLv2 = new TextButton("St.2",style);
        cultureButtonLv2.setVisible(false);
        cultureButtonLv3 = new TextButton("St.3",style);
        cultureButtonLv3.setVisible(false);
        cultureButtonLv4 = new TextButton("St.4",style);
        cultureButtonLv4.setVisible(false);
        cultureButtonLv5 = new TextButton("St.5",style);
        cultureButtonLv5.setVisible(false);
        culturePath = new Label("Zweig der Kultur: ",skin);
        culturePath.setColor(Color.GOLD);
        cultureInfo = new Label("Jede Stufe erhoet das pro Runde erhaltende Gold um 5", skin);
        cultureInfo.setColor(Color.GOLD);

        //
        style = new TextButton.TextButtonStyle(skin.get("default",TextButton.TextButtonStyle.class));
        style.up = skin.getDrawable("defaultIcon");
        treeCloseButton = new TextButton("Schliessen",style);

        float buttonSideSize = treeTable.getHeight()/9;
        //Build Table
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(steelPath);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(steelButtonLv1);
        treeTable.add(steelButtonLv2);
        treeTable.add(steelButtonLv3);
        treeTable.add(steelButtonLv4);
        treeTable.add(steelButtonLv5);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(steelInfo);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(magicPath);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(magicButtonLv1);
        treeTable.add(magicButtonLv2);
        treeTable.add(magicButtonLv3);
        treeTable.add(magicButtonLv4);
        treeTable.add(magicButtonLv5);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(magicInfo);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(culturePath);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(cultureButtonLv1);
        treeTable.add(cultureButtonLv2);
        treeTable.add(cultureButtonLv3);
        treeTable.add(cultureButtonLv4);
        treeTable.add(cultureButtonLv5);
        treeTable.row().fill().expand().size(buttonSideSize,buttonSideSize);
        treeTable.add(cultureInfo);
        treeTable.row().fill().expand().size(buttonSideSize*3,buttonSideSize*(float)1.5);
        treeTable.add();
        treeTable.add();
        treeTable.add();
        treeTable.add();
        treeTable.add();
        treeTable.add(treeCloseButton);

        treeTable.setBackground(skin.getDrawable("treeBackground"));
        treeTable.setVisible(false);
        stage.addActor(treeTable);

    }

    private void buildListeners(){
        //region Selection
        selectionUpLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected instanceof Base){
                    if(baseRecruitButtons){
                        ((Base)selected).createUnit(UnitType.SWORDFIGHTER);
                    } else if(laborEntered) {
                        System.out.println("UpLeft in Labor");
                    }else {
                            if(((Base)selected).getLabRoundsRemaining() == Constants.FINISHED) {
                                laborEntered = true;
                                baseRecruitButtons = false;
                            }else if(((Base)selected).getLabRoundsRemaining() != Constants.NONE_OR_NOT_SET)
                                ((Base)selected).abortLab();
                            else
                                ((Base) selected).buildLab();

                            unrendered = true;
                        }
                } else if (selected instanceof Hero){
                   if(((Hero)selected).getLeftHand().execute())
                    {pe = new ParticleEffect();
                    pe.load(Gdx.files.internal("assets/sprites/heal.party"), Gdx.files.internal("assets/sprites/"));
                    pe.getEmitters().first().setPosition(((Hero) selected).getField().getXPos() * 100 + 50, ((Hero) selected).getField().getYPos() * 100 + 50);
                    pe.setDuration(1);
                    pe.scaleEffect(1);
                    pe.start();
                    unrendered=true;}
                } else if(selected instanceof  Field){
                    ((Field)selected).buildMine(player);
                } else {
                    System.out.println(selected.getClass().getName());
                }
            }
        });

        selectionUpRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected instanceof Base){
                    if(baseRecruitButtons){
                        ((Base)selected).createUnit(UnitType.SPEARFIGHTER);
                    } else if(laborEntered) {
                        System.out.println("UpRight in Labor");
                    }else{
                            if(((Base)selected).getCaserneRoundsRemaining() == Constants.FINISHED) {
                                baseRecruitButtons = true;
                                laborEntered = false;
                            }else if(((Base)selected).getCaserneRoundsRemaining() != Constants.NONE_OR_NOT_SET)
                                ((Base)selected).abortCaserne();
                            else
                                ((Base)selected).buildCaserne();

                            unrendered = true;
                        }
                } else if (selected instanceof Hero){
                    if(((Hero)selected).getRightHand().execute())
                    {
                        shield = new ParticleEffect();
                        shield.load(Gdx.files.internal("assets/sprites/shield.party"), Gdx.files.internal("assets/sprites/"));
                        shield.getEmitters().first().setPosition(((Hero) selected).getField().getXPos() * 100 + 50, ((Hero) selected).getField().getYPos() * 100 + 50);
                        shield.scaleEffect(2);

                        shield.start();
                        unrendered=true;}
                } else if(selected instanceof  Field){
                    ((Field)selected).buildBase(player);
                }else {
                    System.out.println(selected.getClass().getName());
                }
            }
        });

        selectionDownLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected instanceof Base){
                    if(baseRecruitButtons){
                        ((Base)selected).createUnit(UnitType.ARCHER);
                    } else if(laborEntered){
                        System.out.println("DownLeft in Labor");
                    }
                } else if (selected instanceof Hero){
                    System.out.println("DownLeft-Hero");
                    //TODO/////////////////////////////////////////////////////////////////////////////////
                } else if(selected instanceof  Field){
                    System.out.println("DownLeft-Field");
                    //TODO/////////////////////////////////////////////////////////////////////////////////
                }else {
                    System.out.println(selected.getClass().getName());
                }
            }
        });

        selectionDownRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected instanceof Base){
                    if(baseRecruitButtons){
                        ((Base)selected).createUnit(UnitType.WORKER);
                    } else if(laborEntered) {
                        System.out.println("DownRIght in Labor");
                    }else {
                            try {
                                boolean success = ((Base)selected).buildMarket();
                                if(success) {
                                    marketPlace.setVisible(true);
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                } else if (selected instanceof Hero){
                    System.out.println("DownRight-Hero");
                    //TODO/////////////////////////////////////////////////////////////////////////////////
                } else if(selected instanceof  Field){
                    System.out.println("DownRight-Field");
                    //TODO/////////////////////////////////////////////////////////////////////////////////
                }else {
                    System.out.println(selected.getClass().getName());
                }
            }
        });
        //endregion

        //region Bottomgeneral
        //region Market
        marketPlace.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                marketTable.setVisible(!marketTable.isVisible());
            }
        });

        buyButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
              try{
                  Market market = session.getMarket();

                  if(!woodField.getText().equals("")) {
                      boolean wood = market.buy(player,Constants.WOOD,Integer.parseInt(woodField.getText()));
                      woodAmount.setText("Holz: " + market.getWood());
                      woodPrice.setText("Preis: " + market.woodPrice());

                      if(wood)
                          woodField.setText("");
                      else{
                          woodError.setVisible(true);
                          Timer timer = new Timer();
                          timer.scheduleTask(new Timer.Task() {
                              @Override
                              public void run() {
                                  woodError.setVisible(false);
                              }
                          },5);
                      }
                  }

                  if(!ironField.getText().equals("")) {
                      boolean iron = market.buy(player,Constants.IRON,Integer.parseInt(ironField.getText()));
                      ironAmount.setText("Eisen: " + market.getIron());
                      ironPrice.setText("Preis: " + market.ironPrice());

                      if(iron)
                          ironField.setText("");
                      else{
                          ironError.setVisible(true);
                          Timer timer = new Timer();
                          timer.scheduleTask(new Timer.Task() {
                              @Override
                              public void run() {
                                  ironError.setVisible(false);
                              }
                          },5);
                      }
                  }

              }catch (RemoteException e){
                  System.out.println(e.getMessage());
                  e.printStackTrace();
              }
            }
        });

        sellButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try{
                    Market market = session.getMarket();

                    if(!woodField.getText().equals("")) {
                        boolean wood = market.sell(player,Constants.WOOD,Integer.parseInt(woodField.getText()));
                        woodAmount.setText("Holz: " + market.getWood());
                        woodPrice.setText("Preis: " + market.woodPrice());

                        if(wood)
                            woodField.setText("");
                        else{
                            woodError.setVisible(true);
                            Timer timer = new Timer();
                            timer.scheduleTask(new Timer.Task() {
                                @Override
                                public void run() {
                                    woodError.setVisible(false);
                                }
                            },5);
                        }
                    }

                    if(!ironField.getText().equals("")) {
                        boolean iron = market.sell(player,Constants.IRON,Integer.parseInt(ironField.getText()));
                        ironAmount.setText("Eisen: " + market.getIron());
                        ironPrice.setText("Preis: " + market.woodPrice());

                        if(iron)
                            ironField.setText("");
                        else{
                            ironError.setVisible(true);
                            Timer timer = new Timer();
                            timer.scheduleTask(new Timer.Task() {
                                @Override
                                public void run() {
                                    ironError.setVisible(false);
                                }
                            },5);
                        }
                    }

                }catch (RemoteException e){
                    connectionError.setVisible(true);
                    Timer timer = new Timer();
                    timer.scheduleTask(new Timer.Task(){
                        @Override
                        public void run(){ connectionError.setVisible(false);}
                    },5);
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                marketTable.setVisible(false);
            }
        });

        //endregion

        //region TechTree
        techTree.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                treeTable.setVisible(!treeTable.isVisible());
            }
        });

        //region Steelbuttons
        steelButtonLv1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.STEEL1)) {
                    steelButtonLv2.setVisible(true);
                    steelButtonLv1.setVisible(false);
                }
            }
        });

        steelButtonLv2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.STEEL2)) {
                    steelButtonLv3.setVisible(true);
                    steelButtonLv2.setVisible(false);
                }
            }
        });

        steelButtonLv3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.STEEL3)) {
                    steelButtonLv4.setVisible(true);
                    steelButtonLv3.setVisible(false);
                }
            }
        });

        steelButtonLv4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.STEEL4)) {
                    steelButtonLv5.setVisible(true);
                    steelButtonLv4.setVisible(false);
                }
            }
        });

        steelButtonLv5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.STEEL5)) {
                    steelButtonLv5.setText("");
                    NinePatch tmp = null;
                    tmp = new NinePatch(textures[16], 0, 0, 0, 0);
                    skin.add("ironIcon", tmp);
                    steelButtonLv5.getStyle().up = skin.getDrawable("ironIcon");
                }
            }
        });
        //endregion

        //region Magic
        magicButtonLv1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.MAGIC1)) {
                    magicButtonLv2.setVisible(true);
                    magicButtonLv1.setVisible(false);
                }
            }
        });

        magicButtonLv2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.MAGIC2)) {
                    magicButtonLv3.setVisible(true);
                    magicButtonLv2.setVisible(false);
                }
            }
        });

        magicButtonLv3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.MAGIC3)) {
                    magicButtonLv4.setVisible(true);
                    magicButtonLv3.setVisible(false);
                }
            }
        });

        magicButtonLv4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.MAGIC4)) {
                    magicButtonLv5.setVisible(true);
                    magicButtonLv4.setVisible(false);
                }
            }
        });

        magicButtonLv5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.MAGIC5)) {
                    magicButtonLv5.setText("");
                    NinePatch tmp = null;
                    tmp = new NinePatch(textures[17], 0, 0, 0, 0);
                    skin.add("manaIcon", tmp);
                    magicButtonLv5.getStyle().up = skin.getDrawable("manaIcon");
                }
            }
        });
        //endregion

        //region Culture
        cultureButtonLv1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.CULTURE1)) {
                    cultureButtonLv2.setVisible(true);
                    cultureButtonLv1.setVisible(false);
                }
            }
        });

        cultureButtonLv2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.CULTURE2)) {
                    cultureButtonLv3.setVisible(true);
                    cultureButtonLv2.setVisible(false);
                }
            }
        });

        cultureButtonLv3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.CULTURE3)) {
                    cultureButtonLv4.setVisible(true);
                    cultureButtonLv3.setVisible(false);
                }
            }
        });

        cultureButtonLv4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.CULTURE4)) {
                    cultureButtonLv5.setVisible(true);
                    cultureButtonLv4.setVisible(false);
                }
            }
        });

        cultureButtonLv5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(player.advanceOnTechTree(TreeElement.CULTURE5)) {
                    cultureButtonLv5.setText("");
                    NinePatch tmp = null;
                    tmp = new NinePatch(textures[14], 0, 0, 0, 0);
                    skin.add("goldIcon", tmp);
                    cultureButtonLv5.getStyle().up = skin.getDrawable("goldIcon");
                }
            }
        });
        //endregion

        treeCloseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x, float y){
                treeTable.setVisible(false);
            }
        });

        //endregion

        finishRound.addListener(new ClickListener(){
            @Override
            public  void clicked(InputEvent event, float x, float y){
                try {
                    session.finishTurn(player);
                } catch (RemoteException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        //endregion

        //region Chat
        sendMessageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try{
                    session.getSessionChat().addMessage(player.getAccount().getName() + ": ", messageField.getText());
                    messageField.setText("");
                }catch (RemoteException e){
                    System.out.println(e.getMessage());
                }
            }
        });

        messageField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if(c == '\n' || c == '\r'){
                    try{
                        session.getSessionChat().addMessage(player.getAccount().getName(), messageField.getText());
                        messageField.setText("");
                    }catch (RemoteException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        //endregion
    }

//region Standardmethoden fuer Screen/InputProcessor
    ////Aktuell nicht gebraucht////
    /**
     * @param width trivial
     * @param height trivial
     * @see //ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see //ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see //ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link //Buttons#LEFT} on iOS.
     *
     * @param screenX  trivial
     * @param screenY  trivial
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX   trivial
     * @param screenY      trivial
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX trivial
     * @param screenY trivial
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    //endregion

    /**
     * This method implements the movement of units.
     */
    public void move() {
        Object obj = map[getFieldXPos(Constants.FIELDXLENGTH * 100)][getFieldYPos(Constants.FIELDYLENGTH * 100)].select();
        if (selected instanceof Unit && obj instanceof Field) {
            Unit unit = ((Unit) selected);
            Field target = (Field) obj;
            int radius = unit.getMovePointsLeft();
            if(unit.getOwner()==player)
            try {
                  int diff=Math.max(Math.abs(unit.getField().getXPos() - target.getXPos()), Math.abs(unit.getField().getYPos() - target.getYPos()));
                    if (target.getWalkable()&&diff <= unit.getMovePointsLeft()) {
                       try {
                           pe = new ParticleEffect();
                           pe.load(Gdx.files.internal("assets/sprites/fight.party"), Gdx.files.internal(""));
                           pe.getEmitters().first().setPosition(unit.getField().getXPos() * 100 + 50, unit.getField().getYPos() * 100 + 50);
                           pe.setDuration(-2800);
                           pe.scaleEffect(3);
                           pe.start();
                       }catch(Exception e){}//zur Sicherheit
                        if(unit.getField().getYPos()<target.getYPos()){unit.setDirection(1);}else{unit.setDirection(0);}
                        unit.getField().setCurrent(null);
                        target.setCurrent(unit);
                        unit.setMovePointsLeft(unit.getMovePointsLeft()-diff);
                        unrendered=true;
                        fight();

                    }
            }catch(Exception e){}
        }

    }

    /**
     * This method checks if enemies are nearby and let them fight.
     */
    public void fight(){
            Unit unit = ((Unit) selected);
        boolean both=false;
           for(int x = 0;x<=unit.getRange();x++){
               //positive y direction
               try{if(map[unit.getField().getXPos()][unit.getField().getYPos()+x].getCurrent()instanceof Unit)
               {if(map[unit.getField().getXPos()][unit.getField().getYPos()+x].getCurrent().getOwner()!=session.getActive()){
                   final Unit enemy=map[unit.getField().getXPos()][unit.getField().getYPos()+x].getCurrent();
                   if(enemy.getRange()>=x)both=true;
                   fightAnimation(unit, enemy,50,100,both);
                   break;}}}catch(Exception e){}
               //negative y direction
               try{if(map[unit.getField().getXPos()][unit.getField().getYPos()-x].getCurrent()instanceof Unit)
               {if(map[unit.getField().getXPos()][unit.getField().getYPos()-x].getCurrent().getOwner()!=session.getActive()){
                   final Unit enemy=map[unit.getField().getXPos()][unit.getField().getYPos()-x].getCurrent();
                   if(enemy.getRange()>=x)both=true;
                   fightAnimation(unit, enemy,50,0,both);
                   break;}}}catch(Exception e){}
               //negative x direction
               try{if(map[unit.getField().getXPos()-x][unit.getField().getYPos()].getCurrent()instanceof Unit)
               {if(map[unit.getField().getXPos()-x][unit.getField().getYPos()].getCurrent().getOwner()!=session.getActive()){
                   final Unit enemy=map[unit.getField().getXPos()-x][unit.getField().getYPos()].getCurrent();
                   if(enemy.getRange()>=x)both=true;
                   fightAnimation(unit, enemy,0,50,both);
                   break;}}}catch(Exception e){}
               //positive x direction
               try{if(map[unit.getField().getXPos()+x][unit.getField().getYPos()].getCurrent()instanceof Unit)
               {if(map[unit.getField().getXPos()+x][unit.getField().getYPos()].getCurrent().getOwner()!=session.getActive()){
                   final Unit enemy=map[unit.getField().getXPos()+x][unit.getField().getYPos()].getCurrent();
                   if(enemy.getRange()>=x)both=true;
                   fightAnimation(unit, enemy,100,50,both);
                   break;}}}catch(Exception e){}
           }
    }

    /**
     * Show a fighting scene
     * @param unit
     * @param enemy
     */
    private void fightAnimation(Unit unit,Unit enemy, int x,int y, boolean both){
        unit.setMovePointsLeft(0);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                selected=unit.getField();
                enemy.setCurrentHp(enemy.getCurrentHp() - (unit.getAtk()-enemy.getDef()));
                if (enemy.getCurrentHp() <= 0) enemy.getField().setCurrent(null);
                if(both){
                unit.setCurrentHp(unit.getCurrentHp() - (enemy.getAtk()-unit.getDef()));
                if (unit.getCurrentHp() <= 0) unit.getField().setCurrent(null);
                }
            }
        }, 2);
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("assets/sprites/fightAnimation.party"), Gdx.files.internal("assets/sprites/"));
        pe.setDuration(1);
        pe.scaleEffect(2);
        pe.getEmitters().first().setPosition(unit.getField().getXPos() * 100+x, unit.getField().getYPos() * 100+y);
        pe.start();

    }



}





