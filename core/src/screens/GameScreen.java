package screens;

import Action.*;
import GameObject.*;
import GameObject.Field;
import Player.*;
import chat.Chat;
import chat.Message;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import server.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.List;


/**
 * Der Screen fuer ein Spiel...
 *
 * Created by benja_000 on 03.07.2016.
 */
public class GameScreen implements Screen, InputProcessor{

    private Game game;
    private IGameSession session;
    private Field[][] map = null;
    private boolean unrendered = true;
    private int selected=-1;
    private int player;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    Texture bg = new Texture(Gdx.files.internal("assets/sprites/normal0.png"));
    Texture bg2 = new Texture(Gdx.files.internal("assets/sprites/normal1.png"));
    OrthographicCamera camera;
    int[][] fields = new int[26][24];
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Table table;
    private boolean archerOrBuff = false;
    private boolean spearfighterOrBuff = false;
    private double timer = 0;

    //region Chat
    private Table chatTable;
    private TextButton sendMessageButton, generalChat, teamChat;
    private TextField messageField;
    private Table backLog;
    private Label userName;
    private ScrollPane chatScroller;
    private int lastMessageCountGeneral, lastMessageCountTeam;
    private boolean showTeamChat=false;
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

    //region TeamBox
    private Table teamTable;
    private Label tWood,tIron,tGold;
    private TextField addWood,addIron,addGold;
    private TextButton addButton,tBcloseButton;
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
    private Texture[] textures = new Texture[SpriteNames.getSpriteAmount()];
    //endregion

    ParticleEffect pe; //For fighting scenes
    ParticleEffect shield;

    public  GameScreen(Game game, IGameSession session, int playerId){

        for(int i=0; i<SpriteNames.getSpriteAmount(); ++i)
            textures[i] = new Texture(Gdx.files.internal(SpriteNames.getValueForIndex(i).getSpriteName()));

        this.player = playerId;
        batch=new SpriteBatch();
        this.session = session;
        System.out.println(session.toString());
        try {
            session.showSessionDetails();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));

        //bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
     int x =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[0]);
     int y =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[1]);
        camera= new OrthographicCamera(x,y);
        camera.translate(x/2,y/2);
        camera.setToOrtho(false,x,y);

  /*      try{
            map = (session.getMap()).getFields();
//TODO: Irgendwann entfernen
            Unit testUnit = new Unit(UnitType.SPEARFIGHTER, (Player)this.player);
            testUnit.setMovePointsLeft(8);
            testUnit.setSpriteIndex(SpriteNames.SPEARFIGHTER.getSpriteName());
            testUnit.setOwner((Player)this.player);
            map[5][5].setCurrent(testUnit);

            Hero testUnit2 = new Hero(UnitType.HERO,(Player)this.player,"harald");
            testUnit2.setMovePointsLeft(30);
            testUnit2.setSpriteIndex(SpriteNames.HERO.getSpriteName());
            testUnit2.setOwner((Player)this.player);
            testUnit2.setAtk(2000);
            testUnit2.setCurrentHp(2);
            map[6][6].setCurrent(testUnit2);
         //   Sound testMusic = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/song1.wav"));
         //   testMusic.play();
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
            e.printStackTrace();

            Random r = new Random();
            for(int i=0;i<Constants.FIELDXLENGTH;++i)
                for(int j=0;j<Constants.FIELDYLENGTH;++j)
                    fields[i][j] = r.nextInt(2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/


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
        showTeamBox();
        buildListeners();

          //  InputMultiplexer im = new InputMultiplexer(stage, this);
            //Gdx.input.setInputProcessor(im);
    }



    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        InputMultiplexer im = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(im);
        timer += delta;
        if(timer >= 1/2) {
            int batchWidth = Constants.FIELDXLENGTH * 100;
            int batchHeight = Constants.FIELDYLENGTH * 100;
            int i = 0;
            int j = 0;

            if (pe != null) {
                pe.update((float)timer);

                if (pe.isComplete()) {
                    pe.dispose();
                    pe = null;
                }
            }
            if (shield != null) shield.update((float)timer);

            batch.setProjectionMatrix(camera.combined);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

            try {
                int[] ressources = session.getRessources(player);
                label1.setText(ressources[Constants.GOLD] + "");
                label2.setText(ressources[Constants.WOOD] + "");
                label3.setText(ressources[Constants.IRON] + "");
                label4.setText(ressources[Constants.MANA] + "");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            //region Buttonumstellungen fuer Auswahlbuttons
            try {
                finishRound.setVisible(session.isActive(player));

                if (unrendered) {
                    if (session.isSelectedClassOf(Selectable.UNIT)) {
                        List<String> info = session.getInformation(selected);
                        unitAtk.setText("ATK: " + info.get(0));
                        unitDef.setText("DEF: " + info.get(1));
                        unitHp.setText("HP: " + info.get(2));
                        unitMPoints.setText("BP: " + info.get(3));
                        unitName.setText("NAME: " + info.get(4));
                        unitRange.setText("RW: " + info.get(5));
                        unitOwner.setText("SPIELER: " + info.get(6));
                    } else {
                        unitAtk.setText("");
                        unitDef.setText("");
                        unitHp.setText("");
                        unitMPoints.setText("");
                        unitName.setText("");
                        unitRange.setText("");
                        unitOwner.setText("");
                    }

                    if(session.isSelectedOwner(player) && session.isActive(player)) {
                        if (session.isSelectedClassOf(Selectable.BASE)) {
                            if (baseRecruitButtons) {
                                selectionUpLeft.setVisible(true);
                                selectionUpLeft.setText("");
                                selectionUpLeft.setTouchable(Touchable.enabled);
                                selectionUpLeft.getStyle().up = skin.getDrawable("swordfighterIcon");
                                selectionUpRight.setVisible(session.checkHasSelectedUnit(UnitType.SPEARFIGHTER));
                                selectionUpRight.setText("");
                                selectionUpRight.getStyle().up = skin.getDrawable("spearfighterIcon");
                                selectionDownLeft.setVisible(session.checkHasSelectedUnit(UnitType.ARCHER));
                                selectionDownLeft.setText("Bogenschuetze");
                                selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                                selectionDownRight.setVisible(true);
                                selectionDownRight.setText("");
                                selectionDownRight.getStyle().up = skin.getDrawable("workerIcon");
                            } else if (laborEntered) {
                                selectionUpLeft.setVisible(true);
                                selectionUpLeft.setText("Reduziere Einheitenkosten");
                                selectionUpLeft.setTouchable(Touchable.enabled);
                                selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");
                                selectionUpRight.setVisible(true);
                                selectionUpRight.setText("Schildkroetenstil");
                                selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                                selectionDownLeft.setVisible(true);

                                if (archerOrBuff)
                                    selectionDownLeft.setText("Fernkampfstil");
                                else
                                    selectionDownLeft.setText("Bogenschuetze erforschen");

                                selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                                selectionDownRight.setVisible(true);

                                if (spearfighterOrBuff)
                                    selectionDownRight.setText("Hornissenstil");
                                else
                                    selectionDownRight.setText("Speerkaempfer erforschen");

                                selectionDownRight.getStyle().up = skin.getDrawable("defaultIcon");
                            } else {
                                selectionUpLeft.setVisible(true);

                                if (session.checkSelectedBuildingFinished(Building.LABOR)) //TODO
                                    selectionUpLeft.setText("Labor");
                                else if (session.checkIsBuilding(Building.LABOR))
                                    selectionUpLeft.setText("Labor wird gebaut");
                                else
                                    selectionUpLeft.setText("Labor bauen");

                                selectionUpLeft.setTouchable(Touchable.enabled);
                                selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");

                                selectionUpRight.setVisible(true);

                                if (session.checkSelectedBuildingFinished(Building.CASERNE))
                                    selectionUpRight.setText("Kaserne");
                                else if (session.checkIsBuilding(Building.CASERNE))
                                    selectionUpRight.setText("Kaserne wird gebaut");
                                else
                                    selectionUpRight.setText("Kaserne bauen");

                                selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                                selectionDownLeft.setVisible(false);
                                selectionDownLeft.setText("Einheiten Rekrutieren");
                                selectionDownLeft.getStyle().up = skin.getDrawable("defaultIcon");
                                selectionDownRight.setVisible(!session.checkMarket(player));
                                selectionDownRight.setText("Marktplatz bauen");
                                selectionDownRight.getStyle().up = skin.getDrawable("defaultIcon");
                            }
                        } else if (session.isSelectedClassOf(Selectable.HERO)) {
                            selectionUpLeft.setVisible(true);
                            selectionUpLeft.setText("Heldenfaehigkeit links");
                            selectionUpLeft.setTouchable(Touchable.enabled);
                            selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");
                            selectionUpRight.setVisible(true);
                            selectionUpRight.setText("Heldenfaehigkeit rechts");
                            selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                            selectionDownLeft.setVisible(false);
                            selectionDownRight.setVisible(false);
                            if (shield != null)
                                shield.getEmitters().first().setPosition(session.getSelectedX(selected) * 100 + 50, session.getSelectedY(selected) * 100 + 50);

                        } else if (session.isSelectedClassOf(Selectable.FIELD)) {
                            selectionUpLeft.setVisible(session.isSelectedRessourceType(Constants.IRON));
                            selectionUpLeft.setText("Mine bauen");
                            selectionUpLeft.getStyle().up = skin.getDrawable("defaultIcon");

                            selectionUpRight.setVisible(session.checkPathFull(player, Constants.TECHTREE_CULTURE)
                                    && session.hasSelectedCurrent()
                                    && session.isSelectedRessourceType(Constants.NONE_OR_NOT_SET));
                            selectionUpRight.setText("Basis bauen");
                            selectionUpRight.getStyle().up = skin.getDrawable("defaultIcon");
                            selectionDownLeft.setVisible(false);
                            selectionDownRight.setVisible(false);
                        }
                    }else {
                        selectionUpLeft.setVisible(false);
                        selectionUpRight.setVisible(false);
                        selectionDownLeft.setVisible(false);
                        selectionDownRight.setVisible(false);
                    }
                }
                unrendered = true;


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            //endregion

            buildChatString();

            //region Kamerabeweung
            //Move screen right
            if ((Gdx.input.getX() >= (Gdx.graphics.getWidth() - 10) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) && camera.position.x < batchWidth) {
                camera.position.set(camera.position.x + (500*(float)timer), camera.position.y, 0);
            }

            //Move screen left
            if ((Gdx.input.getX() <= (10) || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) && camera.position.x > 0) {
                camera.position.set(camera.position.x - (500*(float)timer), camera.position.y, 0);

            }

            //Move screen down
            if ((Gdx.input.getY() >= (Gdx.graphics.getHeight() - 10) || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) && camera.position.y > 0) {
                camera.position.set(camera.position.x, camera.position.y - (500*(float)timer), 0);

            }

            //Move screen up
            if ((Gdx.input.getY() <= (10) || Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) && camera.position.y < batchHeight) {
                camera.position.set(camera.position.x, camera.position.y + (500*(float)timer), 0);
            }
            camera.update();
            //endregion

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            batch.begin();
            //region Felder zeichnen
            String textureName = "";
            int textureIndex = 0;
            try {
                for (int k = 0; k < Constants.FIELDXLENGTH; k++)
                    for (int h = 0; h < Constants.FIELDYLENGTH; h++) {
                        int[] spriteIndeces = session.getSpriteIndex(k, h);
                        batch.draw(textures[spriteIndeces[0]], 100 * k, 100 * h, 100, 100);

                        //TODO check fuer particle effects
                    }
            } catch (RemoteException e) {
                System.out.println("Render Fields");
                e.printStackTrace();
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
            if (pe != null)
                pe.draw(batch);
            if (shield != null)
                shield.draw(batch);
            batch.end();

            try {
                if(session.isActive(player))
                showMovementRange();
            } catch (RemoteException e) {
                e.printStackTrace();
            }


            Vector3 vector = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float x = vector.x > batchWidth ? batchWidth - 100 : vector.x < 0 ? 0 : vector.x;
            float y = vector.y > batchHeight ? batchHeight - 100 : vector.y < 0 ? 0 : vector.y;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(((int) x / 100) * 100, ((int) y / 100) * 100, 100, 100);
            shapeRenderer.end();

            stage.act();
            stage.draw();

            timer = 0;
        }
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
                    selected = session.select(getFieldXPos(Constants.FIELDXLENGTH*100),getFieldYPos(Constants.FIELDYLENGTH*100)); //TODO batchBounds->attribute
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
                    selected = session.select(-1,-1);
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
        try {
            if (selected != -1 && session.isSelectedClassOf(Selectable.UNIT)) {
                    if (!session.isSelectedClassOf(Selectable.BASE)
                            && session.isSelectedOwner(player)) {
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                        shapeRenderer.setColor(Color.GREEN);
                        int radius = Integer.parseInt(session.getInformation(selected).get(Constants.INFO_MOVEPOINTS).split("/")[0]);
                        int selectedXPos = session.getSelectedX(selected);
                        int selectedYPos = session.getSelectedY(selected);
                        for (int x = 0 - radius; x < radius + 1; x++) {
                            for (int y = 0 - radius; y < radius + 1; y++) {
                                if ( selectedXPos* 100 + x * 100 >= 0
                                        &&  selectedYPos* 100 + y * 100 >= 0
                                        && selectedYPos * 100 + y * 100 <= Constants.FIELDYLENGTH*100-100
                                        && selectedXPos * 100 + x * 100 <= Constants.FIELDXLENGTH*100-100)

                                    if(session.checkWalkable(selectedXPos+x,selectedYPos+y)) {
                                        shapeRenderer.rect(selectedXPos * 100 + x * 100, selectedYPos * 100 + y * 100, 100, 100);

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
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zeigt die obere Menuebar an.
     */
    public void showTopMenu(){
try {
    int[] ressources = session.getRessources(player);
    table = new Table();
    table.setWidth(stage.getWidth());
    table.align(Align.left | Align.top);
    table.setPosition(10, Gdx.graphics.getHeight());
    HorizontalGroup group = new HorizontalGroup();
    Image image = new Image(textures[SpriteNames.GOLD_ICON.getSpriteIndex()]);//Gold
    label1 = new Label(ressources[Constants.GOLD] + "", skin);
    label1.setColor(Color.WHITE);
    group.addActor(image);
    group.addActor(label1);
    Image image2 = new Image(textures[SpriteNames.WOOD_ICON.getSpriteIndex()]);//Holz
    label2 = new Label(ressources[Constants.WOOD] + "", skin);
    label2.setColor(Color.WHITE);
    group.addActor(image2);
    group.addActor(label2);
    Image image3 = new Image(textures[SpriteNames.IRON_ICON.getSpriteIndex()]);//Eisen
    label3 = new Label(ressources[Constants.IRON] + "", skin);
    label3.setColor(Color.WHITE);
    group.addActor(image3);
    group.addActor(label3);
    Image image4 = new Image(textures[SpriteNames.MANA_ICON.getSpriteIndex()]);//Mana
    label4 = new Label(ressources[Constants.MANA] + "", skin);
    label4.setColor(Color.WHITE);
    group.addActor(image4);
    group.addActor(label4);


        NinePatch tmp = new NinePatch(textures[SpriteNames.CHEST_ICON.getSpriteIndex()],0,0,0,0);
        skin.add("teamBox",tmp);
        tmp = new NinePatch(textures[SpriteNames.TEAMBOX_OPEN.getSpriteIndex()],0,0,0,0);
        skin.add("teamBoxOpen",tmp);

        ImageButtonStyle buttonStyle = new ImageButtonStyle();
        buttonStyle.up = skin.getDrawable("teamBox");
        buttonStyle.over = skin.getDrawable("teamBoxOpen");
        ImageButton teamBox = new ImageButton(buttonStyle);
        teamBox.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                teamTable.setVisible(true);
                try {
                    int[] teamRessources = session.getTeamRessources(player);
                        tWood = new Label("Holz: "+teamRessources[Constants.WOOD],skin);
                        tIron = new Label("Eisen: "+teamRessources[Constants.IRON],skin);
                        tGold = new Label("Gold: "+teamRessources[Constants.GOLD],skin);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        group.addActor(teamBox);
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
        beenden.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    Registry reg = LocateRegistry.getRegistry();
                    ServerInterface stub = (ServerInterface) reg.lookup("ServerInterface");
                    stub.saveSession(session);
                    game.setScreen(new MenuScreen(game));
                }catch(Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        TextButton debug = new TextButton("Debug",skin);
        debug.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    session.showSessionDetails();
                    System.out.println("\n Der aktive Spieler des Clients: "+player + " Name: " + session.getPlayerName(player));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        optionTable.add(einstellungen);
        optionTable.row();
        optionTable.add(aufgeben);
        optionTable.row();
        optionTable.add(debug);
        optionTable.row();
        optionTable.add(beenden);
        optionTable.setPosition(Gdx.graphics.getWidth()-70,Gdx.graphics.getHeight()-80);

        table.add(optionen).spaceLeft(stage.getWidth()/4);
        table.row();

        stage.addActor(table);
        stage.addActor(optionTable);

} catch (RemoteException e){
    e.printStackTrace();
}
    }

    private void showBottomMenu(){
        NinePatch tmp = null;
        TextButton.TextButtonStyle style = null;

        tmp = new NinePatch(textures[SpriteNames.BUTTON_BG.getSpriteIndex()], 0, 0, 0, 0);
        skin.add("defaultIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.BUTTON_WORKER.getSpriteIndex()], 0, 0, 0, 0);
        skin.add("workerIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.MARKETPLACE.getSpriteIndex()], 0, 0, 0, 0);
        skin.add("marketIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.MENU_BG.getSpriteIndex()], 10, 10, 10, 10);
        skin.add("background",tmp);
        tmp = new NinePatch(textures[SpriteNames.SPEARFIGHTER.getSpriteIndex()], 10, 10, 10, 10);
        skin.add("spearfighterIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.SWORDFIGHTER.getSpriteIndex()], 10, 10, 10, 10);
        skin.add("swordfighterIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.TECHTREE.getSpriteIndex()], 10, 10, 10, 10);
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
        generalChat = new TextButton("Spiel", skin);
        teamChat = new TextButton("Team",skin);

        //backLog = new List(skin);
        chatTable.row().fill().expandX();
        chatTable.add(generalChat).width(chatTable.getWidth()/2);
        generalChat.setVisible(false);
        chatTable.add(teamChat).width(chatTable.getWidth()/2);
        chatTable.row().fill();
        backLog = new Table();
        backLog.align(Align.left);
        backLog.row().fill().expandX().align(Align.left).height(skin.getFont("default-font").getLineHeight());
        this.buildChatString();
        chatScroller = new ScrollPane(backLog,skin);
        chatScroller.setFadeScrollBars(false);
        chatTable.add(chatScroller).expand().fill().colspan(2);
        chatTable.row().fill();
        try {
            userName = new Label(session.getPlayerName(player),skin);
        } catch (RemoteException e) {
            e.printStackTrace();
            userName = new Label("n/a",skin);
        }

        chatTable.add(messageField);
        chatTable.add(sendMessageButton);
        stage.addActor(chatTable);
    }

    private void buildChatString(){
        try {
            List<String> backLogTmp = session.getChatBackLog(player,showTeamChat);
            Label tmp;
            backLog.clear();

            for(String s: backLogTmp){
                tmp = new Label(s,skin);
                tmp.setWrap(true);
                backLog.row().fill().expandX().align(Align.left).height(tmp.getHeight());
                backLog.add(tmp);
                backLog.row().fill().expandX().align(Align.left).height(tmp.getHeight());
                chatScroller.layout();
                chatScroller.setScrollPercentY(100);
            }

            //region Deprecated
            /*Chat chat = null;
            if(showTeamChat) {
                chat = player.getTeam().getChat();

                ArrayList<Message> backLogTmp = new ArrayList<>(chat.getBacklog());
                if (backLogTmp.size() > lastMessageCountTeam && (chat.getParticipants().isEmpty() || chat.getParticipants().contains(player))) {
                    Label tmp;
                    for (int i = lastMessageCountTeam; i < backLogTmp.size(); ++i) {
                        if (backLogTmp.get(i).getVisibleForAll() || backLogTmp.get(i).getVisibleFor().contains(player)) {
                            tmp = new Label(backLogTmp.get(i).getContent(), skin);
                            tmp.setWrap(true);
                            backLog.row().fill().expandX().align(Align.left).height(tmp.getHeight());
                            backLog.add(tmp);
                            chatScroller.layout();
                            chatScroller.setScrollPercentY(100);
                        }
                    }
                    lastMessageCountTeam = backLogTmp.size();
                }
            } else {
                chat = session.getSessionChat();

                ArrayList<Message> backLogTmp = new ArrayList<>(chat.getBacklog());
                if (backLogTmp.size() > lastMessageCountGeneral && (chat.getParticipants().isEmpty() || chat.getParticipants().contains(player))) {
                    Label tmp;
                    for (int i = lastMessageCountGeneral; i < backLogTmp.size(); ++i) {
                        if (backLogTmp.get(i).getVisibleForAll() || backLogTmp.get(i).getVisibleFor().contains(player)) {
                            tmp = new Label(backLogTmp.get(i).getContent(), skin);
                            tmp.setWrap(true);
                            backLog.row().fill().expandX().align(Align.left).height(tmp.getHeight());
                            backLog.add(tmp);
                            chatScroller.layout();
                            chatScroller.setScrollPercentY(100);
                        }
                    }
                    lastMessageCountGeneral = backLogTmp.size();
                }
            }*/
            //endregion
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
            int[] tmp = session.getMarketInfo();

            woodAmount = new Label("Holz: " + tmp[Constants.WOOD],skin);
            woodPrice = new Label("Preis: " + tmp[Constants.WOOD_PRICE_MARKET_INDEX],skin);

            ironAmount = new Label("Eisen: " + tmp[Constants.IRON],skin);
            ironPrice = new Label("Preis: " + tmp[Constants.IRON_PRICE_MARKET_INDEX],skin);

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
    private void showTeamBox(){
       teamTable = new Table();
        teamTable.align(Align.left);
        teamTable.setPosition(stage.getWidth()/3, stage.getHeight()/2);
        teamTable.setWidth(stage.getWidth()/3);
        teamTable.setHeight(stage.getHeight()/2);
        teamTable.setBackground(skin.getDrawable("treeBackground"));
        try {
           /* if(player.getTeam()==null){
                for(Team t :session.getTeams()){
                    for(Player p : t.getPlayers())
                        if(p.getAccount().getName().equals(player.getAccount().getName()))
                        player.setTeam(t);

                }

            }*/
            int[] teamRessources = session.getTeamRessources(player);
                tWood = new Label("Holz: "+teamRessources[Constants.WOOD],skin);

                tIron = new Label("Eisen: "+teamRessources[Constants.IRON],skin);

                tGold = new Label("Gold: "+teamRessources[Constants.GOLD],skin);


            addWood = new TextField("", skin);
            addIron = new TextField("", skin);
            addGold = new TextField("", skin);

            addButton = new TextButton("Einzahlen",skin);
            addButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    try{
                        int wood=Integer.parseInt(addWood.getText());
                        int iron=Integer.parseInt(addIron.getText());;
                        int gold=Integer.parseInt(addGold.getText());;
                        if(wood<0||iron<0||gold<0) {
                            addWood.setText("Die Zahlen muessen positiv sein!");return;
                        }

                        if(!session.addTeamRessources(player,Constants.WOOD, wood)) {
                            addWood.setText("Du besitzt nicht genug Holz"); return;
                        }
                        addWood.setText("");
                        if(!session.addTeamRessources(player,Constants.IRON, iron)) {
                            addIron.setText("Du besitzt nicht genug Eisen"); return;
                        }
                        addIron.setText("");

                        if(!session.addTeamRessources(player,Constants.GOLD, gold)) {
                            addGold.setText("Du besitzt nicht genug Gold"); return;
                        }
                        addGold.setText("");

                        int[] teamRessources = session.getTeamRessources(player);
                            tWood.setText("Holz: "+teamRessources[Constants.WOOD]);
                            tIron.setText("Eisen: "+teamRessources[Constants.IRON]);
                            tGold.setText("Gold: "+teamRessources[Constants.GOLD]);


                    }catch(Exception e){
                        addWood.setText("Nur Zahlen  eingeben!");
                    }
                }
            });

            tBcloseButton = new TextButton("Schliessen",skin);
            tBcloseButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    teamTable.setVisible(false);
                }
            });

        teamTable.row().pad(10).fill().space(10);
            teamTable.add(addWood);
            teamTable.add(tWood);
            teamTable.row().pad(10).fill().space(10);
            teamTable.add(addIron);
            teamTable.add(tIron);
            teamTable.row().pad(10).fill().space(10);
            teamTable.add(addGold);
            teamTable.add(tGold);
            teamTable.row().pad(10).fill().space(100);
            teamTable.add(addButton);
            teamTable.add(tBcloseButton);
            teamTable.setVisible(false);
            stage.addActor(teamTable);


        }catch(Exception e){
            System.out.println("Bei der Teamkasse ist was schiefgegangen:");
            e.printStackTrace();
        }
    }


    private void showTechtree(){
        NinePatch tmp = null;
        TextButton.TextButtonStyle style = null;

        //TODO change Textureindex
        tmp = new NinePatch(textures[SpriteNames.BUTTON_GREY.getSpriteIndex()], 0, 0, 0, 0);
        skin.add("defaultIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.BUTTON_GREY.getSpriteIndex()], 0, 0, 0, 0);
        skin.add("steelIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.BUTTON_BLUE.getSpriteIndex()], 0, 0, 0, 0);
        skin.add("magicIcon",tmp);
        tmp = new NinePatch(textures[SpriteNames.BUTTON_GOLD.getSpriteIndex()], 0, 0, 0, 0);
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
        steelInfo = new Label("Jede Stufe erhoeht das gewonnene Eisen und Holz um 5",skin);
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
        magicInfo = new Label("Jede Stufe verringert den Mana-Verbrauch um 3",skin);
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
                try {
                    if(session.isSelectedClassOf(Selectable.BASE)){
                        if(baseRecruitButtons){
                            session.createUnit(selected,UnitType.SWORDFIGHTER);
                        } else if(laborEntered) {
                                session.registerBuff(-1,player,BuffInfo.REDUCED_UNIT_COST);

                            System.out.println("UpLeft in Labor");
                        }else {
                                if(session.checkSelectedBuildingFinished(Building.LABOR)) {
                                    laborEntered = true;
                                    baseRecruitButtons = false;
                                }else if(session.checkIsBuilding(Building.LABOR))
                                    session.buildOrAbortBuildingOnSelected(player,selected,Building.LABOR,true);
                                else
                                    session.buildOrAbortBuildingOnSelected(player,selected,Building.LABOR,false);


                            }
                    } else if (session.isSelectedClassOf(Selectable.HERO)){
                       if(session.activateHeroPower(player,true))
                        {
                            int heroId = session.getHero(player);
                            pe = new ParticleEffect();
                        pe.load(Gdx.files.internal("assets/sprites/heal.party"), Gdx.files.internal("assets/sprites/"));
                        pe.getEmitters().first().setPosition(session.getSelectedX(heroId) * 100 + 50, session.getSelectedY(heroId) * 100 + 50);
                        pe.setDuration(1);
                        pe.scaleEffect(1);
                        pe.start();
                          int heroXPos = session.getSelectedX(heroId);
                            int heroYPos = session.getSelectedY(heroId);
                            if(session.hasNearUnits(heroXPos,heroYPos)){
                                pe.load(Gdx.files.internal("assets/sprites/heal2.party"), Gdx.files.internal("assets/sprites/"));
                                pe.getEmitters().first().setPosition(heroXPos * 100 + 50, heroYPos * 100 + 50);
                                pe.setDuration(1/2);
                                pe.scaleEffect(2);
                                pe.start();
                            }}
                    } else if(session.isSelectedClassOf(Selectable.FIELD)){
                            session.buildOrAbortBuildingOnSelected(player,selected,Building.MINE,false);
                    } else {
                        System.out.println("SelectionUpLeft selected ist kein gueltiges objekt");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                unrendered = true;
            }
        });

        selectionUpRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if(session.isSelectedClassOf(Selectable.BASE)){
                        if(baseRecruitButtons){
                           session.createUnit (selected, UnitType.SPEARFIGHTER);
                        } else if(laborEntered) {
                                session.registerBuff(session.getHero(player),player,BuffInfo.EMPOWER_SHIELD);
                        }else{
                                if(session.checkSelectedBuildingFinished(Building.CASERNE)) {
                                    baseRecruitButtons = true;
                                    laborEntered = false;
                                }else if(session.checkIsBuilding(Building.CASERNE))
                                    session.buildOrAbortBuildingOnSelected(player,selected,Building.CASERNE,true);
                                else
                                    session.buildOrAbortBuildingOnSelected(player,selected,Building.CASERNE,false);


                            }
                    } else if (session.isSelectedClassOf(Selectable.HERO)){
                        if(session.activateHeroPower(player,false))
                        {
                            shield = new ParticleEffect();
                            shield.load(Gdx.files.internal("assets/sprites/shield.party"), Gdx.files.internal("assets/sprites/"));
                            shield.getEmitters().first().setPosition(session.getSelectedX(session.getHero(player)) * 100 + 50, session.getSelectedY(session.getHero(player)) * 100 + 50);
                            shield.scaleEffect(2);
                            shield.start();}
                    } else if(session.isSelectedClassOf(Selectable.FIELD)){
                            session.buildOrAbortBuildingOnSelected(player,selected,Building.BASE,false);
                        //TODO abort nicht vergessen
                    }else {
                        System.out.println("SelectionUpRight selected ist kein gueltiges objekt");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                unrendered = true;
            }
        });

        selectionDownLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if(session.isSelectedClassOf(Selectable.BASE)){
                        if(baseRecruitButtons){
                            session.createUnit(selected,UnitType.ARCHER);
                        } else if(laborEntered){

                            if(archerOrBuff)
                                    session.registerBuff(-1,player, BuffInfo.RANGED_STYLE);
                        else if(session.researchOnSelected(selected,Research.RESEARCH_ARCHER))
                                    archerOrBuff = true;

                        }
                    } else if (session.isSelectedClassOf(Selectable.HERO)){
                        System.out.println("DownLeft-Hero");
                        //TODO/////////////////////////////////////////////////////////////////////////////////
                    } else if(session.isSelectedClassOf(Selectable.FIELD)){
                        System.out.println("DownLeft-Field");
                        //TODO/////////////////////////////////////////////////////////////////////////////////
                    }else {
                        System.out.println("SelectionDownLeft selected ist kein gueltiges objekt");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                unrendered = true;
            }
        });

        selectionDownRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if(session.isSelectedClassOf(Selectable.BASE)){
                        if(baseRecruitButtons){
                           session.createUnit (selected,UnitType.WORKER);
                        } else if(laborEntered) {

                            if(spearfighterOrBuff)
                                    session.registerBuff(-1,player, BuffInfo.HORNET_STYLE);
                            else if(session.researchOnSelected(selected,Research.RESEARCH_SPEARFIGHTER))
                                    spearfighterOrBuff = true;

                        }else {
                                    if(session.buildOrAbortBuildingOnSelected(player,selected,Building.MARKET,false)) {
                                        marketPlace.setVisible(true);
                                        unrendered = true;
                                    }
                            }
                    } else if (session.isSelectedClassOf(Selectable.HERO)){
                        System.out.println("DownRight-Hero");
                        //TODO/////////////////////////////////////////////////////////////////////////////////
                    } else if(session.isSelectedClassOf(Selectable.FIELD)){
                        System.out.println("DownRight-Field");
                        //TODO/////////////////////////////////////////////////////////////////////////////////
                    }else {
                        System.out.println("SelectionDownRight selected ist kein gueltiges objekt");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                unrendered = true;
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
                  int[] marketInfo = session.getMarketInfo();

                  if(!woodField.getText().equals("")) {
                      boolean wood = session.buyOnMarket(player,Constants.WOOD,Integer.parseInt(woodField.getText()));
                      marketInfo = session.getMarketInfo();
                      woodAmount.setText("Holz: " + marketInfo[Constants.WOOD]);
                      woodPrice.setText("Preis: " + marketInfo[Constants.WOOD_PRICE_MARKET_INDEX]);
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
                      boolean iron = session.buyOnMarket(player,Constants.IRON,Integer.parseInt(ironField.getText()));
                      marketInfo = session.getMarketInfo();
                      ironAmount.setText("Eisen: " + marketInfo[Constants.IRON]);
                      ironPrice.setText("Preis: " + marketInfo[Constants.IRON_PRICE_MARKET_INDEX]);

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
                    int[] marketInfo = session.getMarketInfo();

                    if(!woodField.getText().equals("")) {
                        boolean wood = session.sellOnMarket(player,Constants.WOOD,Integer.parseInt(woodField.getText()));
                        marketInfo = session.getMarketInfo();
                        woodAmount.setText("Holz: " + marketInfo[Constants.WOOD]);
                        woodPrice.setText("Preis: " + marketInfo[Constants.WOOD_PRICE_MARKET_INDEX]);

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
                        boolean iron = session.sellOnMarket(player,Constants.IRON,Integer.parseInt(ironField.getText()));
                        marketInfo = session.getMarketInfo();
                        ironAmount.setText("Eisen: " + marketInfo[Constants.IRON]);
                        ironPrice.setText("Preis: " + marketInfo[Constants.IRON_PRICE_MARKET_INDEX]);

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
                try {
                    if(session.advanceOnTechtree(player,TreeElement.STEEL1)) {
                        steelButtonLv2.setVisible(true);
                        steelButtonLv1.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        steelButtonLv2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.STEEL2)) {
                        steelButtonLv3.setVisible(true);
                        steelButtonLv2.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        steelButtonLv3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.STEEL3)) {
                        steelButtonLv4.setVisible(true);
                        steelButtonLv3.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        steelButtonLv4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.STEEL4)) {
                        steelButtonLv5.setVisible(true);
                        steelButtonLv4.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        steelButtonLv5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.STEEL5)) {
                        steelButtonLv5.setText("");
                        NinePatch tmp = null;
                        tmp = new NinePatch(textures[SpriteNames.IRON_ICON.getSpriteIndex()], 0, 0, 0, 0);
                        skin.add("ironIcon", tmp);
                        steelButtonLv5.getStyle().up = skin.getDrawable("ironIcon");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        //endregion

        //region Magic
        magicButtonLv1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.MAGIC1)) {
                        magicButtonLv2.setVisible(true);
                        magicButtonLv1.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        magicButtonLv2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.MAGIC2)) {
                        magicButtonLv3.setVisible(true);
                        magicButtonLv2.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        magicButtonLv3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.MAGIC3)) {
                        magicButtonLv4.setVisible(true);
                        magicButtonLv3.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        magicButtonLv4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.MAGIC4)) {
                        magicButtonLv5.setVisible(true);
                        magicButtonLv4.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        magicButtonLv5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.MAGIC5)) {
                        magicButtonLv5.setText("");
                        NinePatch tmp = null;
                        tmp = new NinePatch(textures[SpriteNames.MANA_ICON.getSpriteIndex()], 0, 0, 0, 0);
                        skin.add("manaIcon", tmp);
                        magicButtonLv5.getStyle().up = skin.getDrawable("manaIcon");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        //endregion

        //region Culture
        cultureButtonLv1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.CULTURE1)) {
                        cultureButtonLv2.setVisible(true);
                        cultureButtonLv1.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        cultureButtonLv2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.CULTURE2)) {
                        cultureButtonLv3.setVisible(true);
                        cultureButtonLv2.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        cultureButtonLv3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.CULTURE3)) {
                        cultureButtonLv4.setVisible(true);
                        cultureButtonLv3.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        cultureButtonLv4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.CULTURE4)) {
                        cultureButtonLv5.setVisible(true);
                        cultureButtonLv4.setVisible(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        cultureButtonLv5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    if(session.advanceOnTechtree(player,TreeElement.CULTURE5)) {
                        cultureButtonLv5.setText("");
                        NinePatch tmp = null;
                        tmp = new NinePatch(textures[SpriteNames.GOLD_ICON.getSpriteIndex()], 0, 0, 0, 0);
                        skin.add("goldIcon", tmp);
                        cultureButtonLv5.getStyle().up = skin.getDrawable("goldIcon");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
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
                    session.sendMessage(player,showTeamChat,messageField.getText());
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
                        session.sendMessage(player,showTeamChat,messageField.getText());
                        messageField.setText("");
                    }catch (RemoteException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        });

        generalChat.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                backLog.clear();
                lastMessageCountGeneral = 0;
                showTeamChat = false;
                generalChat.setVisible(false);
                teamChat.setVisible(true);
            }
        });

        teamChat.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                backLog.clear();
                lastMessageCountTeam = 0;
                showTeamChat = true;
                teamChat.setVisible(false);
                generalChat.setVisible(true);
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
        try {
            int xPos = getFieldXPos(Constants.FIELDXLENGTH * 100);
            int yPos = getFieldYPos(Constants.FIELDYLENGTH * 100);
            boolean fight = false;
            int[] result = session.moveSelected(player,selected,xPos,yPos);

            for(int i: result)
            if(i==1){
                fight =true;
                break;
            }

            if(fight){
                try {
                    pe = new ParticleEffect();
                    pe.load(Gdx.files.internal("assets/sprites/fight.party"), Gdx.files.internal(""));
                    pe.getEmitters().first().setPosition(xPos * 100 + 50,yPos * 100 + 50);
                    pe.setDuration(-2800);
                    pe.scaleEffect(3);
                    pe.start();
                }catch(Exception e){
                    e.printStackTrace();
                }//zur Sicherheit

                for(int k=0;k<result.length;++k){
                    if(result[k]==1){
                        switch (k){
                            case 0:
                                fightAnimation(xPos,yPos,50,100);
                                break;
                            case 1:
                                fightAnimation(xPos,yPos,50,0);
                                break;
                            case 2:
                                fightAnimation(xPos,yPos,0,50);
                                break;
                            case 3:
                                fightAnimation(xPos,yPos,100,50);
                                break;
                        }
                    }
                }
                unrendered=true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks if enemies are nearby and let them fight.
     */
    public void fight(){

    }

    /**
     * Show a fighting scene
     */
    private void fightAnimation(int xOrigin, int yOrigin, int x,int y){

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {


            }
        }, 2);
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("assets/sprites/fightAnimation.party"), Gdx.files.internal("assets/sprites/"));
        pe.setDuration(1);
        pe.scaleEffect(2);
        pe.getEmitters().first().setPosition(xOrigin * 100+x, yOrigin * 100+y);
        pe.start();

    }





}





