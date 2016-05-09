package com.projectgg.galaxygods;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectgg.galaxygods.screens.Menu;

public class GalaxyGods extends Game {

	public GalaxyGods() {
	}
   Game game;
	@Override
	public void create() {
		setScreen(new Menu(this));
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
