package screens;

import GameObject.Field;
import GameObject.GameSession;
import GameObject.Unit;
import GameObject.UnitType;
import Player.Account;
import Player.Player;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;


/**
 * Der Screen fuer ein Spiel...
 *
 * Created by benja_000 on 03.07.2016.
 */
public class GameScreen implements Screen, InputProcessor{

    private Game game;
    private GameSession session;
    private Field[][] map = null;
    private Object selected;
    private Account account;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    Texture bg = new Texture(Gdx.files.internal("assets/sprites/normal0.png"));
    Texture bg2 = new Texture(Gdx.files.internal("assets/sprites/normal1.png"));
    OrthographicCamera camera;
    int[][] fields = new int[26][24];
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    //Prepare Textures
    private Texture[] textures = new Texture[]{new Texture(Gdx.files.internal("assets/sprites/normal0.png")),
            new Texture(Gdx.files.internal("assets/sprites/normal1.png")),
            new Texture(Gdx.files.internal("assets/sprites/forest.png")),
            new Texture(Gdx.files.internal("assets/sprites/ironNoMine0.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseFullRight.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseFullLeft.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseDownRightFull.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseDownRightLabor.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseDownRightCaserne.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseDownRightEmpty.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseDownLeftCaserne.png")),
            new Texture(Gdx.files.internal("assets/sprites/baseDownLeftEmpty.png")),
            new Texture(Gdx.files.internal("assets/sprites/normal2.png")),
            new Texture(Gdx.files.internal("assets/sprites/ironNoMine1.png")),
    };


    public  GameScreen(Game game, GameSession session, Account account){
        this.account=account;
        batch=new SpriteBatch();
        this.session = session;
      this.game = game;
        //bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
     int x =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[0]);
     int y =Integer.parseInt(Gdx.app.getPreferences("GGConfig").getString("res", "1024x768").split("x")[1]);
        camera= new OrthographicCamera(x,y);
        camera.translate(x/2,y/2);
        camera.setToOrtho(false,x,y);

        try{
            map = session.getMap().getFields();
        } catch (NullPointerException e){
        }

        Random r = new Random();
        for(int i=0;i<26;++i)
            for(int j=0;j<24;++j)
                fields[i][j] = r.nextInt(2);

    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

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
        int batchWidth = 2600;
        int batchHeight = 2600;
        int i=0;
        int j=0;
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());


        //Move screen right
        if((Gdx.input.getX()>=(Gdx.graphics.getWidth()* 9/10) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))&&camera.position.x<batchWidth)
        {camera.position.set(camera.position.x+10, camera.position.y, 0);
         }

        //Move screen left
        if((Gdx.input.getX()<=(Gdx.graphics.getWidth()/10)|| Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) &&camera.position.x>0)
        {camera.position.set(camera.position.x-10, camera.position.y, 0);

        }

        //Move screen down
        if((Gdx.input.getY()>=(Gdx.graphics.getHeight()* 9/10)|| Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))&&camera.position.y>0)
        {camera.position.set(camera.position.x,camera.position.y-10, 0);

        }

        //Move screen up
        if((Gdx.input.getY()<=(Gdx.graphics.getHeight()/10)|| Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))&&camera.position.y<batchHeight)
        {camera.position.set(camera.position.x,camera.position.y+10, 0);
           }
        camera.update();



        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
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
            for (i = 0; i < 26; ++i)
                for (j = 0; j < 24; ++j) {

                    if (fields[i][j] == 0)
                        batch.draw(bg, 100 * i, 100 * j, 100, 100);
                    else
                        batch.draw(bg2, 100 * i, 100 * j, 100, 100);

                }
        }

//testweise
        batch.draw(new Texture(Gdx.files.internal("assets/sprites/spearfighter.png")), 500,500,100,100);

        batch.end();

        showMovementRange();


        Vector3 vector=camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float x = vector.x > batchWidth ? batchWidth-100 : vector.x < 0 ? 0 : vector.x;
        float y = vector.y > batchHeight ? batchHeight-100 : vector.y < 0 ? 0 : vector.y;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(((int)x/100)*100, ((int)y/100)*100, 100, 100);
        shapeRenderer.end();

    }

    public boolean showUnitRadius(){

        return false;
    }

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
                    selected = map[getFieldXPos(2600)][getFieldYPos(2600)]; //TODO batchBounds->attribute
                    break;
                case Input.Buttons.RIGHT:
                    System.out.println("No action assigned");
                    break;
                case Input.Buttons.MIDDLE:
                    System.out.println("No action assigned");
                    break;
            }
        } catch (NullPointerException e){
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

    /**
     * Zeigt den Bewegungsradius eigener Einheiten an.
     */
    public void showMovementRange() {
//Testweise-------------------------------------
     //   session = new GameSession();
     //   session.getMap().getFields()[2][4] = new Field(1, 1, 2, 4, session.getMap());
        this.map = session.getMap().getFields();
        Unit testUnit = new Unit(UnitType.SPEARFIGHTER, new Player(account));
        testUnit.setMovePointsLeft(3);
        testUnit.setSpriteName("sprites/spearfighter.png");
        testUnit.setOwner(new Player(account));
        map[2][4].setCurrent(testUnit);
        //----------------------------------------------
        if (selected != null && selected instanceof Field & ((Field) selected).getCurrent()!= null) {
            if (((Field) selected).getCurrent().getOwner() != null&&((Field) selected).getCurrent().getType() != UnitType.BASE && ((Field) selected).getCurrent().getOwner().getAccount() == account) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GREEN);
                int radius = ((Field) selected).getCurrent().getMovePointsLeft();
                for (int x = 0 - radius; x < radius + 1; x++) {
                    for (int y = 0 - radius; y < radius + 1; y++) {
                        if (((Field) selected).getXPos() * 100 + x * 100 >= 0 && ((Field) selected).getYPos() * 100 + y * 100 >= 0
                                && ((Field) selected).getYPos() * 100 + y * 100 <= 4900 && ((Field) selected).getXPos() * 100 + x * 100 <= 4900)
                            // if(map[((Field) selected).getXPos()+x][((Field) selected).getYPos()+y].getWalkable()==true)
                            shapeRenderer.rect(((Field) selected).getXPos() * 100 + x * 100, ((Field) selected).getYPos() * 100 + y * 100, 100, 100);
                    }
                }
                shapeRenderer.end();
            }
        }
    }
}
