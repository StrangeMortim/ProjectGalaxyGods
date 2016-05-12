package ProjectGG;

import com.badlogic.gdx.Game;
import Screens.Menu;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GalaxyGods extends Game {

	public static Registry reg;

	public GalaxyGods() {
	}
	Game game;
	@Override
	public void create() {
		setScreen(new Menu(this));
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new RMIsecurityManager());

		try{
			reg = LocateRegistry.getRegistry();
			ServerInterface stub = (ServerInterface) reg.lookup("ServerInterface");
			System.out.println("Client: "+ stub.sayHello());
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
		} catch (NotBoundException e) {
			System.out.println(e.getMessage());
		}
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
