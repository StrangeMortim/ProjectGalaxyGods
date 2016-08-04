package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import server.ServerInterface;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by benja_000 on 24.07.2016.
 */
public class CreateAccountScreen implements Screen {
    private Game game;
    private TextArea tID,tPW;
    private Stage stage;
    private Skin skin;
    private TextButton bCancel,bApply;
    private Registry reg;
    private ServerInterface stub;
    private Sprite backGround;
    private SpriteBatch batch;
    private Label lMessage,lID,lPW;
    private Table table;

    public CreateAccountScreen(Game game){
        this.game=game;
        try {
            reg = LocateRegistry.getRegistry();
            stub = (ServerInterface) reg.lookup("ServerInterface");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        backGround = new Sprite(new Texture(Gdx.files.internal("assets/splash.jpg")));
        backGround.setBounds(0, (stage.getHeight()* 3/4), stage.getWidth(), stage.getHeight()/4);
        bCancel=new TextButton("Abbrechen",skin);
        bApply=new TextButton("Erstellen",skin);
        tID=new TextArea("",skin);
        tPW=new TextArea("",skin);
        lMessage=new Label("",skin);
        lID=new Label("Name: ",skin);
        lPW=new Label("Passwort : ",skin);
        bCancel.setPosition(10,50);
        bApply.setPosition(Gdx.graphics.getWidth()-100,50);
        table=new Table();
        table.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-400);
        table.padTop(100);
        table.row().padBottom(40);
        table.add(lID);
        table.add(tID);
        table.row().padBottom(40);
        table.add(lPW);
        table.add(tPW);
        table.row().padBottom(40);
        table.add(lMessage);
        stage.addActor(table);

        stage.addActor(bCancel);
        stage.addActor(bApply);

        bCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { game.setScreen(new MenuScreen(game));}
        });
        bApply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if(!stub.checkAccount(tID.getText(),tPW.getText())){
                        lMessage.setText("Account wurde erstellt");
                        stub.registerAccount(tID.getText(),tPW.getText());
                    }else{
                        lMessage.setText("Account existiert bereits!");
                    }
                } catch (RemoteException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    lMessage.setText("Account wurde erstellt");
                    try {
                        stub.registerAccount(tID.getText(),tPW.getText());
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
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
        stage.act(delta);
        stage.draw();
        batch.end();
    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     */
    @Override
    public void pause() {

    }

    /**
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
      batch.dispose();
    }
}
