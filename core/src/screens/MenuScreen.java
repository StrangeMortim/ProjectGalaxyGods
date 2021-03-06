package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import server.ServerInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This Screen displays the main menu
 * not much comment because it's mostly displaying stuff and should be self-explaining to the most part
 * @author Fabi
 */
public class MenuScreen implements Screen{
    private Game game;
    private Stage stage;
    private Skin skin;
    private Sprite backGround;
    private SpriteBatch batch;

    private Table table;
    private TextButton networkButton;
    private TextButton serverButton;
    private TextButton optionButton;
    private TextButton exitButton;
    private TextButton newGameButton;
    private TextButton createGameButton;
    Texture bg;
    ParticleEffect pe;
    ParticleEffect pe2;

    public MenuScreen(Game pGame){
        game=pGame;
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        bg = new Texture(Gdx.files.internal("assets/stone.png"));

    }

    @Override
    public void show() {
        ////////////////////////Attribut inititialisierung und Setup////////////////////

        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight()* 3/4);


        ///////////////////////Button Generierung///////////////////////////////////////
        networkButton = new TextButton("Start", skin);
        serverButton = new TextButton("Server starten", skin);
        optionButton = new TextButton("Optionen", skin);
        exitButton = new TextButton("Beenden", skin);
        createGameButton=new TextButton("Erstelle Spiel",skin);
        //////////////////////Listener Generierung/////////////////////////////////////

        networkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { game.setScreen(new NetworkScreen(game));}
        });

        serverButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try{
                    server.Server.init();
                    checkAccount();
                    serverButton.setText("Server ist gestartet");
                    serverButton.setTouchable(Touchable.disabled);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        optionButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ game.setScreen(new OptionScreen(game));}
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ Gdx.app.exit();}
        });

        createGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new InitScreen(game,null));
            }
        });



        /////////////////////Bauen der Tabelle////////////////////////////////////////
        table.padTop(150);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(networkButton);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(createGameButton);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(optionButton);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(serverButton);
        table.row().fill().width(150).height(50);
        table.add(exitButton);
        stage.addActor(table);


        ////////////////////Stage sprites setzen///////////////////////////////////////
        batch = new SpriteBatch();
        backGround = new Sprite(new Texture(Gdx.files.internal("assets/splash.png")));
        backGround.setBounds(stage.getWidth()/2-512, (stage.getHeight()-320), 1024, 300);


        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("assets/torch.party"), Gdx.files.internal(""));
        pe.getEmitters().first().setPosition(backGround.getX()+40, backGround.getY()+95);
        pe.start();
        pe2 = new ParticleEffect();
        pe2.load(Gdx.files.internal("assets/torch.party"), Gdx.files.internal(""));
        pe2.getEmitters().first().setPosition(backGround.getX()+backGround.getWidth()-50, backGround.getY()+95);
        pe2.start();

        ////////////////////Input Regeln///////////////////////////////////////////////
        Gdx.input.setInputProcessor(stage);


        ///////////////////brauchst du das noch?////////////////////////////////////////
       /* Texture splashTexture = new Texture(Gdx.files.internal("assets/splash.jpg"));
        TextureRegion region = new TextureRegion(splashTexture,0,0,1024,216);
        splash=new Sprite(region);
        splashTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        splash.setSize(1024,216);
        splash.setOrigin(splash.getWidth()/2,splash.getHeight()/2);
        splash.setPosition(0,552);*/

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pe.update(Gdx.graphics.getDeltaTime());
        pe2.update(Gdx.graphics.getDeltaTime());
        batch.begin();
        batch.draw(bg, 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(pe.isComplete())pe.reset();


        backGround.draw(batch);
        pe.draw(batch);
        pe2.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        backGround.getTexture().dispose();
}

    private void checkAccount() {
        try {
            Registry reg = LocateRegistry.getRegistry();
            ServerInterface stub = (ServerInterface) reg.lookup("ServerInterface");
                stub.registerAccount("Spieler 1", "1234");


                stub.registerAccount("Spieler 2", "1234");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}
