package screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragScrollListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Fabi on 29.06.2016.
 */
public class OptionScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private Sprite backGround;
    private SpriteBatch batch;

    private Table lowerButtons;
    private TextButton homeButton;
    private TextButton applyButton;

    private Table actualOptions;
    private Label resolutionLabel;
    private Label musicLabel;
    private Label musicValueLabel;
    private Label soundLabel;
    private Label soundValueLabel;
    private Label serverLabel;
    private Label selectedServer;
    private TextButton addServerButton;
    private Label addServerLabel;
    private TextField newServerUrl;
    private Label malFormedURL;


    private Preferences prefs = Gdx.app.getPreferences("GGConfig");
    private List<String> servers = new ArrayList<>(Arrays.asList(prefs.getString("Servers", "localhost").split(" ")));
    private String resolution = prefs.getString("res", "1024x768");
    private int musicVolume = prefs.getInteger("musicVol", 100);
    private int soundVolume = prefs.getInteger("soundVol", 100);

    public OptionScreen(Game game){ this.game = game;}


    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        ////////////////////////Attribut inititialisierung und Setup////////////////////
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        actualOptions = new Table();
        actualOptions.setWidth(stage.getWidth()* 1/4);
        actualOptions.align(Align.left);
        actualOptions.setPosition(stage.getWidth() * 1/3, stage.getHeight() * 2/4);
        //actualOptions.debug(Table.Debug.all);

        lowerButtons = new Table();
        lowerButtons.setWidth(stage.getWidth());
        lowerButtons.align(Align.bottomRight);
        lowerButtons.setPosition(0, 0);

        ///////////////////////Label Gen///////////////////////////////////////////////
        resolutionLabel = new Label("Aufloesung: ", skin);
        resolutionLabel.setAlignment(Align.right);
        ////Resolution Auswahlmoeglichkeiten////
        String[] resContent = new String[7];
        resContent[0] = "800x600";//new Label("800x600",skin);
        resContent[1] = "1024x768";//new Label("1024x768",skin);
        resContent[2] = "1280x720";//new Label("1280x720",skin);
        resContent[3] = "1366x768";//new Label("1366x768",skin);
        resContent[4] = "1400x1050";//new Label("1400x1050",skin);
        resContent[5] = "1600x1200";//new Label("1600x1200",skin);
        resContent[6] = "1920x1080";//new Label("1920x1080",skin);
        final SelectBox<Object> resSb = new SelectBox<Object>(skin);
        resSb.setItems((Object[])resContent);
        for(String s: resContent)
        if(s.equals(resolution)){
            resSb.setSelected(s);
            break;
        }


        musicLabel = new Label("Musik: ", skin);
        musicLabel.setAlignment(Align.right);
        musicValueLabel = new Label(Integer.toString(musicVolume), skin);
        musicValueLabel.setAlignment(Align.center);
        ////Music Slider////
        Slider musicSlider = new Slider(0,100,1,false,skin);
        musicSlider.setValue(musicVolume);
        musicSlider.setColor(Color.CORAL);

        soundLabel = new Label("Sound: ", skin);
        soundLabel.setAlignment(Align.right);
        soundValueLabel = new Label(Integer.toString(soundVolume), skin);
        soundValueLabel.setAlignment(Align.center);
        ////Sound Label////
        Slider soundSlider = new Slider(0,100,1,false,skin);
        soundSlider.setValue(soundVolume);
        soundSlider.setColor(Color.CORAL);


        serverLabel = new Label("Bekannte Server: ",skin);
        serverLabel.setAlignment(Align.topRight);
        addServerLabel = new Label("Server URL: ", skin);
        addServerLabel.setAlignment(Align.topRight);
        newServerUrl = new TextField("",skin);
        Label malFormedURL = new Label("Die URL oder IP-Adresse beinhaltet unerlaubte Zeichen",skin);
        malFormedURL.setWrap(true);
        malFormedURL.setColor(Color.RED);
        malFormedURL.setVisible(false);

        ////Nested Table fuer die Server////
        Table serverTable = new Table();
        serverTable.align(Align.center|Align.top);
        for(String s: servers){
            Label tmp = new Label(s, skin);
            tmp.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    if(selectedServer != null)
                    selectedServer.setColor(selectedServer.getStyle().fontColor);

                    selectedServer = tmp;
                    selectedServer.setColor(Color.CYAN);
                }
            });
            serverTable.add(tmp);
            serverTable.row();
        }
        ScrollPane serverScroller = new ScrollPane(serverTable,skin);
        serverScroller.setForceScroll(false, false);



        ///////////////////////Button Generierung///////////////////////////////////////
        homeButton = new TextButton("Zurueck", skin);
        applyButton = new TextButton("Bestaetigen", skin);
        addServerButton = new TextButton("Hinzufuegen", skin);

        //////////////////////Listener Generierung/////////////////////////////////////
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { game.setScreen(new MenuScreen(game));}
        });

        resSb.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resolution = (String)resSb.getSelected();//((Label)resSb.getSelected()).toString().replaceAll("Label: ","");
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVolume = (int)musicSlider.getValue();
                musicValueLabel.setText(Integer.toString(musicVolume));
            }
        });

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundVolume = (int)soundSlider.getValue();
                soundValueLabel.setText(Integer.toString(soundVolume));
            }
        });

        addServerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                String tmp = newServerUrl.getText();
                if(!tmp.equals("")){
                    if(tmp.contains(" ")) {
                        malFormedURL.setVisible(true);
                        Timer timer = new Timer();
                        timer.scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {
                                malFormedURL.setVisible(false);
                            }
                        },5);
                    }else {
                        servers.add(tmp);
                        newServerUrl.setText("");

                        Label newServer = new Label(tmp, skin);
                        newServer.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y){
                                if(selectedServer != null)
                                    selectedServer.setColor(selectedServer.getStyle().fontColor);

                                selectedServer = newServer;
                                selectedServer.setColor(Color.CYAN);
                            }
                        });
                        serverTable.add(newServer);
                        serverTable.row();
                    }
                }
            }
        });

        applyButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                prefs.putInteger("musicVol",musicVolume);
                prefs.putInteger("soundVol",soundVolume);
                prefs.putString("res",resolution);
                prefs.putString("Servers",servers.toString().replaceAll("[\\[\\],]",""));
                prefs.flush();
                Gdx.graphics.setWindowedMode(Integer.parseInt(resolution.split("x")[0]),Integer.parseInt(resolution.split("x")[1]));
                stage.getViewport().update(Integer.parseInt(resolution.split("x")[0]),Integer.parseInt(resolution.split("x")[1]), true);
            }
        });


        /////////////////////Bauen der Tabelle////////////////////////////////////////
        ////////////Actual Options////////////////
        actualOptions.padTop(30);
        actualOptions.row().fill().width(150).height(50);
        actualOptions.add(resolutionLabel);
        actualOptions.add(resSb);
        actualOptions.row().fill().width(150).height(50);
        actualOptions.add(musicLabel);
        actualOptions.add(musicSlider);
        actualOptions.add(musicValueLabel);
        actualOptions.row().fill().width(150).height(50);
        actualOptions.add(soundLabel);
        actualOptions.add(soundSlider);
        actualOptions.add(soundValueLabel);
        actualOptions.row().fill().width(150).height(100);
        actualOptions.add(serverLabel);
        actualOptions.add(serverScroller);
        actualOptions.row().fill().width(150).height(30);
        actualOptions.add(addServerLabel);
        actualOptions.add(newServerUrl);
        actualOptions.add(addServerButton).padLeft(10);
        actualOptions.row().fill().prefWidth(250).height(50);
        actualOptions.add();
        actualOptions.add(malFormedURL).colspan(2);
        stage.addActor(actualOptions);


        /////Lower Button Table//////////////
        lowerButtons.pad(30);
        lowerButtons.row().fill().width(150).height(50);
        lowerButtons.add(applyButton).padRight(10);
        lowerButtons.add(homeButton);
        stage.addActor(lowerButtons);


        ////////////////////Stage sprites setzen///////////////////////////////////////
        batch = new SpriteBatch();
        backGround = new Sprite(new Texture(Gdx.files.internal("assets/splash.jpg")));
        backGround.setBounds(0, (stage.getHeight()* 3/4), stage.getWidth(), stage.getHeight()/4);

        ////////////////////Input Regeln///////////////////////////////////////////////
        Gdx.input.setInputProcessor(stage);
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
