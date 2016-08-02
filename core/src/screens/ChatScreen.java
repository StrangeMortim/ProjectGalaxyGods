package screens;

import chat.ChatInterface;
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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import projectgg.gag.GoldAndGreed;
import server.ServerInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by benja_000 on 07.05.2016.
 */
public class ChatScreen implements Screen{
    private Game game;
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private Label labelDetails;
    private Label labelMessage;
    private TextButton button;
    private TextArea textIPAddress;
    private TextArea textMessage;
    private ScrollPane scroll;
    private List chatMessagesList;
    private Table table;
    private Container con;
    private ChatInterface chat;



    public ChatScreen(Game pGame) {
        game = pGame;
        boolean chatFound = false;

        while (!chatFound) {
            try {
                chat = (ChatInterface) GoldAndGreed.reg.lookup("Chat");
                chatFound = true;
                System.out.println("Chat is now connected");
            } catch (AccessException e) {
                System.out.println(e.getMessage());
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            } catch (NotBoundException e) {
                try {
                    ServerInterface tmp = (ServerInterface)GoldAndGreed.reg.lookup("ServerInterface");
                    tmp.createChat();
                } catch (RemoteException e1) {
                    System.out.println("Can't connect, continue without chat");
                    break;
                } catch (NotBoundException e1) {
                    System.out.println("No Server object found, continue without chat");
                    break;
                }
                System.out.println("No Chat found, waiting for new one");
            }
        }
    }


    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();//

        //Create a font
        BitmapFont font = new BitmapFont();

        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        skin.add("default", font);

        Gdx.input.setInputProcessor(stage);

        // Create our controls
        labelDetails = new Label("Hier steht die IP-Adresse",skin);
        labelMessage = new Label("Chat-Prototyp",skin);
        button = new TextButton("Send message",skin);
        textIPAddress = new TextArea("",skin);
        textMessage = new TextArea("",skin);


        // Vertical group groups contents vertically.  I suppose that was probably pretty obvious
        VerticalGroup vg = new VerticalGroup().space(3).pad(5).fill();//.space(2).pad(5).fill();//.space(3).reverse().fill();
        // Set the bounds of the group to the entire virtual display
        vg.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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

        TextButton playB = new TextButton("Zurück", skin);
        playB.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MenuScreen(game));
            }
        });


        //
        table = new Table(skin);
        table.setBounds(0,0,Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/3);
        table.debug();
        chatMessagesList = new List(skin);
        chatMessagesList.setItems((Object[]) new String[]{"1"});
        scroll=new ScrollPane(chatMessagesList);
        //scroll.setForceScroll(true,false);
        table.add(scroll).expandY();

        con = new Container(scroll);
        con.height(200);


        playB.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , 300);
      /*  button.addListener(new ClickListener(){
                               @Override
                               public void clicked(InputEvent event, float x, float y){

                /*String[]liste=new String[chatMessagesList.getItems().size+1];
                for(int i=0;i<liste.length-1;i++){
                    liste[i]=(String)chatMessagesList.getItems().get(i);
                }
                liste[liste.length-1]=textMessage.getText();
                chatMessagesList.setItems(liste);/

                                   if(chat != null) {
                                       try {
                                           chat.addMessage("Player 1", textMessage.getText());
                                       } catch (RemoteException e) {
                                           System.out.println(e.getMessage());
                                       }
                                   }
                                   else
                                       System.out.println("Something went wrong, there is no Chat");
                               }
                           }

        );*/



        // Add them to scene
        vg.addActor(labelDetails);
        vg.addActor(labelMessage);
        vg.addActor(textIPAddress);
        vg.addActor(con);
        //vg.addActor(table);
        vg.addActor(textMessage);
        vg.addActor(button);
        vg.addActor(playB);

        // Add scene to stage
        stage.addActor(vg);

    }

    @Override
    public void render(float delta) {
        if(chat != null)
            try {
                chatMessagesList.setItems(chat.getBacklog().toArray());
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        stage.act(delta);
        stage.draw();
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
    }
}
