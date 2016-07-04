package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import server.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Fabi on 03.07.2016.
 */
public class NetworkScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private Sprite backGround;
    private SpriteBatch batch;

    private Preferences prefs = Gdx.app.getPreferences("GGConfig");
    private String[] servers = prefs.getString("Servers", "localhost").split(" ");
    private ArrayList<String> games = new ArrayList<>();
    private String selectedServer = servers[0];
    private ServerInterface serverObject;
    private Label selectedGame;

    private Table lowerButtons;
    private TextButton homeButton;
    private TextButton joinButton;

    private Table serverContent;
    private Label serverLabel;
    private Label serverOffline;

    private Table gameTable;


    public NetworkScreen(Game game){this.game = game;}

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        lowerButtons = new Table();
        lowerButtons.setWidth(stage.getWidth());
        lowerButtons.align(Align.bottomLeft);
        lowerButtons.setPosition(0, 0);

        serverContent = new Table();
        serverContent.setWidth(stage.getWidth());
        serverContent.align(Align.center|Align.top);
        serverContent.setPosition(0, stage.getHeight() * 3/4);
        //serverContent.debugAll();

        ///////////////Label Gen/////////////////////////////
        serverLabel = new Label("Server: ", skin);
        serverOffline = new Label("Everythings OK",skin);
        serverOffline.setColor(Color.RED);
        serverOffline.setVisible(false);
        ////SelectBox für Server////
        final SelectBox<Object> serverSb = new SelectBox<Object>(skin);
        serverSb.setItems((Object[])servers);

        this.connect();

        ////Gamelist////
        gameTable = new Table();
        gameTable.align(Align.left|Align.top);
        this.buildGameTable();
        ScrollPane gameScroller = new ScrollPane(gameTable,skin);



        ///////////////Buttons//////////////////
        homeButton = new TextButton("Zurueck", skin);
        joinButton = new TextButton("Beitreten/Erstellen", skin);

        //////////////////////Listener Generierung/////////////////////////////////////
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { game.setScreen(new MenuScreen(game));}
        });

        joinButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent evnet, float x, float y){
                //TODO initScreen ran binden
            }
        });

        serverSb.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedServer = (String)serverSb.getSelected();
                games.clear();
                buildGameTable();
                connect();
            }
        });

        ////////////////ServerContent Table/////////////////
        //serverContent.padTop(30);
        serverContent.row().fill().width(70).height(30);
        serverContent.add(serverLabel).left();
        serverContent.add(serverSb).width(150);
        serverContent.add(serverOffline).left();
        serverContent.row().fill().width(stage.getWidth()/2).height(stage.getHeight()/2).colspan(3);
        serverContent.add(gameScroller);
        stage.addActor(serverContent);

        /////Lower Button Table//////////////
        lowerButtons.pad(30);
        lowerButtons.row().fill().width(150).height(50);
        lowerButtons.add(homeButton).padRight(lowerButtons.getWidth()-homeButton.getWidth()-2*joinButton.getWidth());
        lowerButtons.add(joinButton);
        stage.addActor(lowerButtons);


        ////////////////////Stage sprites setzen///////////////////////////////////////
        batch = new SpriteBatch();
        backGround = new Sprite(new Texture(Gdx.files.internal("assets/splash.jpg")));
        backGround.setBounds(0, (stage.getHeight()* 3/4), stage.getWidth(), stage.getHeight()/4);

        ////////////////////Input Regeln///////////////////////////////////////////////
        Gdx.input.setInputProcessor(stage);
    }

    private void buildGameTable(){
        for(String s: games){
            Label tmp = new Label(s, skin);
            tmp.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    if(selectedGame != null)
                        selectedGame.setColor(selectedGame.getStyle().fontColor);

                    selectedGame = tmp;
                    selectedGame.setColor(Color.CYAN);
                }
            });
            gameTable.add(tmp);
            gameTable.row();
        }
    }

    private void connect(){
        try{
            System.out.println("1");
            Registry reg = (selectedServer.equals("")) ? LocateRegistry.getRegistry() : LocateRegistry.getRegistry(selectedServer);
            System.out.println("2");

            serverObject = (ServerInterface) reg.lookup("ServerInterface");
            System.out.println("3");
            games = new ArrayList<String>(Arrays.asList(serverObject.getSessionList().split(";")));
            System.out.println("4");
            serverOffline.setVisible(false);
            if(((String)games.get(0)).equals(""))
                games.clear();
            if(games.isEmpty())
                games.add("Currently no Games avaible, create a new one");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            serverOffline.setText("Server ist nicht erreichbar");
        } catch (NotBoundException e) {
            serverOffline.setText("Server-Programm ist nicht initialisiert");
        } finally {
            serverOffline.setVisible(true);
        }
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backGround.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    /**
     * @param width
     * @param height
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
}
