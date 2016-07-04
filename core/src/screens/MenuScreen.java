package screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Benjamin Brennecke on 08.05.2016.
 */
public class MenuScreen implements Screen{
    private Game game;
    private Stage stage;
    private Skin skin;
    private Sprite backGround;
    private SpriteBatch batch;

    private Table table;
    private TextButton networkButton;
    private TextButton chatButton;
    private TextButton optionButton;
    private TextButton exitButton;
    private TextButton newGameButton;


    public MenuScreen(Game pGame){
        game=pGame;
    }

    @Override
    public void show() {
        ////////////////////////Attribut inititialisierung und Setup////////////////////
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0, Gdx.graphics.getHeight()* 3/4);

        ///////////////////////Button Generierung///////////////////////////////////////
        networkButton = new TextButton("Start", skin);
        chatButton = new TextButton("Chat", skin);
        optionButton = new TextButton("Optionen", skin);
        exitButton = new TextButton("Beenden", skin);
        newGameButton = new TextButton("Neues Spiel", skin);

        //////////////////////Listener Generierung/////////////////////////////////////

        networkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { game.setScreen(new NetworkScreen(game));}
        });

        chatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { game.setScreen(new ChatScreen(game));}
        });

        optionButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ game.setScreen(new OptionScreen(game));}
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){ Gdx.app.exit();}
        });

        newGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){game.setScreen(new InitScreen(game,null));}
        });


        /////////////////////Bauen der Tabelle////////////////////////////////////////
        table.padTop(30);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(networkButton);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(newGameButton);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(chatButton);
        table.row().padBottom(10).fill().width(150).height(50);
        table.add(optionButton);
        table.row().fill().width(150).height(50);
        table.add(exitButton);
        stage.addActor(table);


        ////////////////////Stage sprites setzen///////////////////////////////////////
        batch = new SpriteBatch();
        backGround = new Sprite(new Texture(Gdx.files.internal("assets/splash.jpg")));
        backGround.setBounds(0, (stage.getHeight()* 3/4), stage.getWidth(), stage.getHeight()/4);

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

        batch.begin();
        backGround.draw(batch);
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
}
