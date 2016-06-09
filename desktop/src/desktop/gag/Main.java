package desktop.gag;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import projectgg.gag.GoldAndGreed;
import server.DBManager;
import server.Server;

import java.util.Properties;

public class Main {
	public static void main (String[] arg) {
		DBManager man = new DBManager();
		/*LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Gold and Greed";
		cfg.width = 1024;
		cfg.height = 768;
		server.Server.init();
		new LwjglApplication(new GoldAndGreed(), cfg);*/
	}
}
