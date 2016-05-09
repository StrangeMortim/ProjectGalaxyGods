package com.projectgg.galaxygods.screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Benjamin Brennecke on 08.05.2016.
 */
public class Menu implements Screen{
    private Game game;
    private SpriteBatch batch;
private Sprite splash;
private Stage stage;
    private TextureAtlas atlas;
    private Table table;
    private Skin skin;
    private BitmapFont white,black;
    private TextButton playB,exitB;
    private Label header;

    public Menu(Game pGame){
        game=pGame;
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();//
        white= new BitmapFont(Gdx.files.internal("desktop/assets/fonts/white.fnt"),false);
        black= new BitmapFont(Gdx.files.internal("desktop/assets/fonts/black.fnt"),false);

        //Create a font
        BitmapFont font = new BitmapFont();

        skin = new Skin();
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background",new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        TextButton playB = new TextButton("Start", skin);
        playB.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new Chat(game));
            }
        });
        playB.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , 300);
        stage.addActor(playB);

        TextButton exitB = new TextButton("Exit", skin);
        exitB.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
        exitB.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , 200);
        stage.addActor(exitB);
        Gdx.input.setInputProcessor(stage);

        Texture splashTexture = new Texture(Gdx.files.internal("desktop/assets/splash.jpg"));
        TextureRegion region = new TextureRegion(splashTexture,0,0,1024,216);
        splash=new Sprite(region);
        splashTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        splash.setSize(1024,216);
        splash.setOrigin(splash.getWidth()/2,splash.getHeight()/2);
        splash.setPosition(0,552);

    }

    @Override
    public void render(float delta) {
Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        batch.begin();
        splash.draw(batch);
        batch.end();
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
        splash.getTexture().dispose();
    }
}
