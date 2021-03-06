package screens;

import GameObject.IGameSession;
import Player.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import server.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * This Screen is used to create a new GameSession
 * not much comment because it's mostly displaying stuff and should be self-explaining to the most part
 *
 * @author Benjamin
 */
public class InitScreen implements Screen {
    private Game game;
    private IGameSession session;
    private Stage stage;
    private Skin skin;
    private Sprite backGround;
    private Label lID,lPW,lTeam,lRunden,lSpielerzahl,lError,lMessage;
    private TextButton bCancel,bApply,bHero;
    private TextArea tID,tPW;
    private SelectBox<Object> sTeam, sSpieler,sRunden;
    private Table tLeft,tRight,tDown,tInfo;
    private String name="",password="";
    private SpriteBatch batch;
    private boolean checkSession=true;
    private boolean checkAccount;
    private Registry reg;
    private ServerInterface stub;
    private int counter=0;
    private boolean lastCheck=false;


    public InitScreen(Game game, IGameSession session){
        this.session=session;
        this.game=game;

        try {
            reg = LocateRegistry.getRegistry();
            stub = (ServerInterface) reg.lookup("ServerInterface");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void show() {

        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        backGround = new Sprite(new Texture(Gdx.files.internal("assets/splash.jpg")));
        backGround.setBounds(0, (stage.getHeight()* 3/4), stage.getWidth(), stage.getHeight()/4);

        checkAccount();


    }

    private void init(){

        //instanziiert Labels und Buttons+Listener
        lID=new Label("Spielname:",skin);
        lPW=new Label("Passwort:",skin);
        lPW.setVisible(false);
        lTeam=new Label("Team",skin);
        lRunden=new Label("Rundenanzahl:",skin);
        lRunden.setVisible(false);
        lSpielerzahl=new Label("Spieleranzahl:",skin);
        lError=new Label("",skin);
        lMessage=new Label("",skin);
        lMessage.setColor(Color.GREEN);
        lError.setColor(Color.RED);
        bCancel=new TextButton("Abbrechen",skin);
        bApply=new TextButton("Bestaetigen",skin);
        bHero=new TextButton("Held konfigurieren",skin);
        tID=new TextArea("",skin);
        tPW=new TextArea("",skin);
        tPW.setVisible(false);
        //instanziiert Auswahlmenues
        sTeam    = new SelectBox<>(skin);
        sSpieler = new SelectBox<>(skin);
        sRunden  = new SelectBox<>(skin);
        sRunden.setVisible(false);
        sTeam.setItems((Object[]) new String[]{"Rot", "Blau"});
        sTeam.setSelected("Rot");
        sSpieler.setItems((Object[]) new String[]{"2", "3", "4"});
        sRunden.setItems((Object[]) new String[]{"15", "20", "30", "40", "50", "75", "100", "Endlos"});
        //sTeam.setVisible(false);

        if(session!=null){
            try {
                bApply.setText("Spielen");
                tID.setText(session.getName());
                tPW.setText(session.getPassword());
                sRunden.setItems((Object[]) new String[]{session.getTurn() + ""});
                sSpieler.setItems((Object[]) new String[]{session.getMaxPlayers() + ""});
            } catch (RemoteException e){
                e.printStackTrace();
            }
            tID.setDisabled(true);
            sTeam.setVisible(true);
            tPW.setDisabled(true);
            sRunden.setDisabled(true);
            sSpieler.setDisabled(true);
        }


        bCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { game.setScreen(new MenuScreen(game));}
        });

        boolean endlos = bApply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               if(session==null) {
                   try {
                       String name = stub.createSession(tID.getText());
                       if(!name.equals(""))
                       session = (IGameSession)reg.lookup(name);
                       System.out.println(session.toString());
                   } catch (RemoteException e) {
                       e.printStackTrace();
                       return;
                   } catch (NotBoundException e) {
                       e.printStackTrace();
                       return;
                   }
               }
                  /* if(lastCheck){
                       try {
                           for (Team t : session.getTeams()) {
                               for (Player p : t.getPlayers()) {
                                   if (p.getAccount().getName().equals(name)) {
                                       game.setScreen(new GameScreen(game, session, session.playerJoin(p.getAccount(),p,t)));
                                       return;
                                   }
                               }
                           }
                       } catch (RemoteException e){
                           e.printStackTrace();
                       }

                   }*///TODO

                try {
                    //session.setName(tID.getText());

                    session.setPassword(tPW.getText());
                    if (!sRunden.getSelected().toString().equals("Endlos")) {
                        session.setRound(Integer.parseInt(sRunden.getSelected().toString()));
                    } else {
                        session.setRound(99999);
                    }
                   // session.setNumberOfPlayers(Integer.parseInt(sSpieler.getSelected().toString()));
                    ArrayList<Player> players = new ArrayList<Player>();
                    Account account = new Account(name,password);
                    int playerId = -1;
                   /////////////////////////////////////////////////////////////////////////////////////////
                   /* if(lastCheck) {
                        if(session.getTeams().size()>0) {
                            for (Team t : session.getTeams()) {
                                if (t.getColor().equals(sTeam.getSelected().toString())&&!t.getPlayers().contains(player)) {
                                    player = session.playerJoin(account,(Player)player,t);
                                }
                            }
                        }*/
                        stub.saveSession(session);
                        game.setScreen(new GameScreen(game,session,session.playerJoin(account,(String)sTeam.getSelected())));

                   /////////////////////////////////////////////////////////////////////////////////////////////

                    if (checkSession() && checkSession) {
                        try {
                            stub.saveSession(session);
                            lastCheck=true;
                            init();
                        } catch (Exception e) {
                            System.out.println("Session konnte nicht erstellt werden");
                            e.printStackTrace();
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        //instanziiert Tables
        tLeft = new Table();
        tLeft.setWidth(stage.getWidth());
        tLeft.align(Align.topLeft);
        tLeft.setPosition(20,stage.getHeight());
        tRight = new Table();
        tRight.setWidth(stage.getWidth()/2);
        tRight.align(Align.right);
        tRight.setPosition(stage.getWidth()/2, stage.getHeight()*3/4);
        tDown = new Table();
        tDown.setWidth(stage.getWidth());
        tDown.align(Align.bottomRight);
        tDown.setPosition(0, 0);
        tInfo=new Table();
        tInfo.setWidth(200);
        tInfo.setPosition(stage.getWidth()-200,stage.getHeight()*3/4);

        tLeft.padTop(100);
        tLeft.row().padBottom(40).fill().width(350).height(50);
        tLeft.add(lID);
        tLeft.add(tID);
//        tLeft.row().padBottom(40).fill().width(350).height(50);
//        tLeft.add(lPW);
//        tLeft.add(tPW);
        tLeft.row().padBottom(40).fill().width(350).height(50);
        tLeft.add(lSpielerzahl);
        tLeft.add(sSpieler);
        tLeft.row().padBottom(40).fill().width(350).height(50);
        tLeft.add(lTeam);
        tLeft.add(sTeam);
        tLeft.row().padBottom(40).fill().width(350).height(50);
        tLeft.add(lRunden);
        tLeft.add(sRunden);
        tLeft.row().padBottom(40).fill().width(350).height(50);
        tLeft.row().padBottom(40).fill().width(350).height(50);
        tDown.pad(30);
        tDown.row().fill().width(150).height(50);
        tDown.add(bCancel).padLeft(10);
        tDown.add(bApply).padRight(10);
        tInfo.add(lError);
        tInfo.add(lMessage);
        stage.clear();
        stage.addActor(tLeft);
        stage.addActor(tDown);
        stage.addActor(tInfo);
        Gdx.input.setInputProcessor(stage);

        if(session!=null)lastCheck=true;
    }

        private boolean checkSession() throws RemoteException {
           boolean check=true;

            try {

              //  if(stub.loadSession(session.getName())!=null) {
                //    lError.setText("Ein Spiel mit dem Namen \n existiert bereits! "); check=false;}

            } catch (Exception e) {
                System.out.println("Fehler bei der Verbindung!");
            }
           // if(session.getTeams().size()<2)
            //{lError.setText(lError.getText() + " Mehr als ein Team erforderlich! "); check=false;}

            if(session.getName().equals(""))
            {lError.setText(lError.getText() + " Name fehlt! "); check=false;}


            return check;
        }

    /**
     *
     * @return
     */
       private void checkAccount(){
          /* try {
               Registry reg = LocateRegistry.getRegistry();
               ServerInterface stub = (ServerInterface) reg.lookup("ServerInterface");
               if(!stub.checkAccount("test","1234")){
               stub.registerAccount("test","1234");}
           }catch(Exception e){
               e.printStackTrace();
           }*/


          Input.TextInputListener nameListener = new Input.TextInputListener()
           {   @Override
               public void input(String input) {name=input;
               Input.TextInputListener passwordListener = new Input.TextInputListener()
               {   @Override
               public void input(String input) {password=input;
                   try{
                       Registry reg = LocateRegistry.getRegistry();
                       ServerInterface stub = (ServerInterface) reg.lookup("ServerInterface");
                       if(stub.checkAccount(name,password)){
                           checkAccount=true;
                           init();
                       }else{
                           checkAccount=false;
                           game.setScreen(new MenuScreen(game));
                       }
                   }catch(RemoteException e){
                       System.out.println(e.getMessage());
                       e.printStackTrace();
                   // System.out.println("Wahrscheinlich laeuft die Datenbank noch im Hintergrund.");
                   } catch (NotBoundException e) {
                       e.printStackTrace();
                   }
               }
                   @Override
                   public void canceled() {
                       checkSession=false;
                       game.setScreen(new MenuScreen(game));
                   }};
               Gdx.input.getTextInput(passwordListener, "Bitte geben Sie ein Passwort ein.", "", "");
           }
               @Override
               public void canceled() {
                   checkSession=false;
                   game.setScreen(new MenuScreen(game));}};


           Gdx.input.getTextInput(nameListener, "Bitte geben Sie einen Nutzernamen ein.", "", "");


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
        if(lastCheck)
            lMessage.setText("Waehle ein Team!");
        if(session!=null){
            try {
                String error="";
                if(counter==200)
                    if (checkSession()){
                        lError= new Label("",skin);
                lError.setText(error);}
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
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
     *
     */
    @Override
    public void pause() {

    }


    @Override
    public void resume() {

    }

    /**
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        backGround.getTexture().dispose();
        batch.dispose();
    }
}
