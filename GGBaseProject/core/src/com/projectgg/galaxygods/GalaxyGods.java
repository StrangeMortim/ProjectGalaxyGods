package com.projectgg.galaxygods;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GalaxyGods extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture img;
	private Sprite sprite;

	public GalaxyGods() {
	}

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h/w);
		this.batch = new SpriteBatch();

		this.img = new Texture(Gdx.files.internal("desktop/assets/badlogic.jpg"));
		this.img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		TextureRegion region = new TextureRegion(img,0,0,512,275);

		sprite = new Sprite(region);
		sprite.setSize(0.9f,0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);
	}

	@Override
	public void dispose(){
		batch.dispose();
		img.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.batch.setProjectionMatrix(camera.combined);
		this.batch.begin();
		sprite.setRotation(45);
		this.sprite.draw(batch);
		this.batch.end();
	}

	@Override
	public void resize(int width, int height){

	}

	@Override
	public void pause(){

	}

	@Override
	public void resume(){

	}
}
