package projectgg.gag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import screens.MenuScreen;
import server.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The Game Class, starts the game not much to say
 */
public class GoldAndGreed extends Game {

	public static Registry reg;

	public GoldAndGreed() {
	}
	Game game;
	@Override
	public void create() {
		String resolution = Gdx.app.getPreferences("GGConfig").getString("res", "1024x768");
		Gdx.graphics.setWindowedMode(Integer.parseInt(resolution.split("x")[0]),Integer.parseInt(resolution.split("x")[1]));
		setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose(){
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height){
		super.resize(width,height);
	}

	@Override
	public void pause(){
		super.pause();
	}

	@Override
	public void resume(){
		super.resume();
	}
}
