package com.projectgg.galaxygods.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.projectgg.galaxygods.GalaxyGods;

public class Main {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Galaxy Gods";
		cfg.width = 1024;
		cfg.height = 768;

		new LwjglApplication(new GalaxyGods(), cfg);
	}
}
