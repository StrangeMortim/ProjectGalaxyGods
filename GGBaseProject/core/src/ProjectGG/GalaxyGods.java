package ProjectGG;

import 	com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class GalaxyGods extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture img;
	private Sprite sprite;
	private float rot=0;
	static Registry reg;

	public GalaxyGods() {
	}

	@Override
	public void create() {
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new RMIsecurityManager());

		try{
			reg = LocateRegistry.getRegistry();
			ServerInterface stub = (ServerInterface) reg.lookup("ServerInterface");
			System.out.println("Client: "+ stub.sayHello());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}


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
		reg = null;
		batch.dispose();
		img.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.batch.setProjectionMatrix(camera.combined);
		this.batch.begin();
		final float degreesPerSecond = 10.0f;
		rot = (rot+Gdx.graphics.getDeltaTime() * degreesPerSecond) % 360;
		final float shakeAmplitude = 5.0f;
		float shake = MathUtils.sin(rot)*shakeAmplitude;
		sprite.setRotation(shake);
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
