package screens;

import GameObject.*;
import GameObject.Field;
import Player.Player;
import chat.Chat;
import chat.Message;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
//endregion

    //region Market
    private Table marketTable;
    private Label woodPrice, woodAmount, woodError, ironPrice, ironAmount, ironError, connectionError;
    private TextField woodField, ironField;
    private TextButton buyButton, sellButton, closeButton;
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
    };
    //endregion


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

            Unit testUnit = new Unit(UnitType.SPEARFIGHTER, this.player);
            testUnit.setMovePointsLeft(3);
            testUnit.setSpriteName(SpriteNames.SPEARFIGHTER.getSpriteName());
            testUnit.setOwner(this.player);
            map[5][5].setCurrent(testUnit);
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
                        selectionUpLeft.setText("Schwertkaempfer");
                        selectionUpLeft.setTouchable(Touchable.enabled);
                        selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionUpRight.setVisible(true);
                        selectionUpRight.setText("Speerkaempfer");
                        selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionDownLeft.setVisible(true);
                        selectionDownLeft.setText("Bogenschuetze");
                        selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionDownRight.setVisible(true);
                        selectionDownRight.setText("");
                        selectionDownRight.getStyle().up = skin.getDrawable("workerIcon");
                    } else {
                        selectionUpLeft.setVisible(true);
                        selectionUpLeft.setText("Labor bauen");
                        selectionUpLeft.setTouchable(Touchable.enabled);
                        selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionUpRight.setVisible(true);
                        selectionUpRight.setText("Kaserne bauen");
                        selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionDownLeft.setVisible(true);
                        selectionDownLeft.setText("Einheiten Rekrutieren");
                        selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                        selectionDownRight.setVisible(true);
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
        batch.draw(new Texture(Gdx.files.internal(SpriteNames.SPEARFIGHTER.getSpriteName())), 500,500,100,100);

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
                    unrendered = true;
                    return true;
                case Input.Buttons.RIGHT:
                    move();
                    System.out.println("No action assigned");
                    return true;
                case Input.Buttons.MIDDLE:
                    System.out.println("No action assigned");
                    return true;
                default:
                    return false;
            }
        } catch (Exception e){
            System.out.println("Probably everything ok, just dummies missing: GameScreen - touchDown");
            //e.printStackTrace();
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
                                && ((Unit) selected).getField().getYPos() * 100 + y * 100 <= 4900
                                && ((Unit) selected).getField().getXPos() * 100 + x * 100 <= 4900)

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
        selection.padLeft(40);
        selection.row().fill().expand().space(10).size(bottomTable.getHeight()/2,bottomTable.getHeight()/3);
        selection.add(selectionUpLeft);
        selection.add(selectionUpRight);
        selection.row().fill().expand().space(10).size(bottomTable.getHeight()/2,bottomTable.getHeight()/3);
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

    private void buildListeners(){
        //region Selection
        selectionUpLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(selected instanceof Base){
                    if(baseRecruitButtons){
                        ((Base)selected).createUnit(UnitType.SWORDFIGHTER);
                    } else{
                        if(((Base)selected).getLabRoundsRemaining() != Constants.NONE_OR_NOT_SET)
                            ((Base)selected).abortLab();
                        else
                        ((Base)selected).buildLab();
                    }
                    System.out.println("UpLeft-Base");
                } else if (selected instanceof Hero){
                    ((Hero)selected).getLeftHand().execute();
                    System.out.println("UpLeft-Hero");
                } else if(selected instanceof  Field){
                    System.out.println("UpLeft-Field");
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
                    } else{
                        if(((Base)selected).getCaserneRoundsRemaining() != Constants.NONE_OR_NOT_SET)
                            ((Base)selected).abortCaserne();
                        else
                            ((Base)selected).buildCaserne();
                    }
                    System.out.println("UpRight-Base");
                } else if (selected instanceof Hero){
                    ((Hero)selected).getRightHand().execute();
                    System.out.println("UpRight-Hero");
                } else if(selected instanceof  Field){
                    System.out.println("UpRight-Field");
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
                    } else{
                        baseRecruitButtons = true;
                        unrendered = true;
                    }
                    System.out.println("DownLeft-Base");
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
                    } else{
                        try {
                            boolean success = ((Base)selected).buildMarket();
                            if(success) {
                                marketPlace.setTouchable(Touchable.enabled);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("DownRight-Base");
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
                      ironAmount.setText("Eisen: " + market.getWood());
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
                System.out.println("Techtree kommt noch");
                //TODO
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
    public void move(){
        Object obj = map[getFieldXPos(Constants.FIELDXLENGTH*100)][getFieldYPos(Constants.FIELDYLENGTH*100)].select();
        if(selected instanceof Unit && obj instanceof Field){
            Unit unit = ((Unit)selected);
            Field target = (Field)obj;
            int radius = unit.getMovePointsLeft();
            for (int x = 0 - radius; x < radius + 1; x++) {
                for (int y = 0 - radius; y < radius + 1; y++) {
                    try{
                        if(map[target.getXPos()+x][target.getYPos()+y].getWalkable()) {
                            if(unit.getMovePointsLeft()<=0){return;}
                         unit.getField().setCurrent(null);
                            target.setCurrent(unit);
                            if(x<0)x=(x*-1);
                            if(y<0)y=(y*-1);
                            System.out.println(x+":"+y);
                            if(x>y){unit.setMovePointsLeft(unit.getMovePointsLeft()-(x-1));
                                if(x-1==0){unit.setMovePointsLeft(0);}
                                return;
                            }else{unit.setMovePointsLeft(unit.getMovePointsLeft()-(y-1));
                                if(y-1==0){unit.setMovePointsLeft(0);}
                                return;
                            }

                        }
                    }catch(Exception e){
                    }
                }}
        }

    }


}
