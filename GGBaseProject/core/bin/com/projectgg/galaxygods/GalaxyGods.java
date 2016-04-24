package com.projectgg.galaxygods;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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


        this.img = new Texture(Gdx.files.internal("test.gif"));
        this.img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        TextureRegion region = new TextureRegion(img,0,0,512,275);

        sprite = new Sprite(region);
        sprite.setSize(0.9f,0.9f * sprite.getHeight() / sprite.getWidth());
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);
    },

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
        this.batch.draw(batch);
        this.batch.end();
    }

    @Override
    public void resize/int width, int height){

    }

    @Override
    public void pause(){

    }

    @Override
    publci void resume(){

    }
}