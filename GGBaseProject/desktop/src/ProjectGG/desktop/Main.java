package ProjectGG.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ProjectGG.GalaxyGods;

public class Main {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Galaxy Gods";
        cfg.width = 480;
        cfg.height = 320;
		new LwjglApplication(new GalaxyGods(), cfg);
	}
}
