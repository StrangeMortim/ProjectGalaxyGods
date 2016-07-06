package screens;

import GameObject.GameSession;
import GameObject.Unit;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


/**
 * Created by benja_000 on 03.07.2016.
 */
public class GameScreen implements Screen{

    private Game game;
    private GameSession session;
    private Unit selected;

    private Skin skin;
    private SpriteBatch batch;
    Texture bg = new Texture(Gdx.files.internal("assets/texturbsp.png"));
    OrthographicCamera camera;



    public  GameScreen(Game game){
        batch=new SpriteBatch();
      this.game = game;
        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
     int x =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[0]);
     int y =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[1]);
        camera= new OrthographicCamera(x,y);
        camera.translate(x/2,y/2);
        camera.setToOrtho(false,x,y);

    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        if(Gdx.input.getX()>=800&&camera.position.x<2000) {camera.position.set(camera.position.x+10, camera.position.y, 0);}
        if(Gdx.input.getX()<=50&&camera.position.x>0) {camera.position.set(camera.position.x-10, camera.position.y, 0);}
        if(Gdx.input.getY()>=600&&camera.position.y>0) {camera.position.set(camera.position.x,camera.position.y-10, 0);}
        if(Gdx.input.getY()<=50&&camera.position.y<2000) {camera.position.set(camera.position.x,camera.position.y+10, 0);}
        camera.update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bg, 0, 0, 0, 0, 2000, 2000);
        batch.end();
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
