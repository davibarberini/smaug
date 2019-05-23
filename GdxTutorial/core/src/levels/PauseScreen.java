package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MyGdxGame;

public class PauseScreen {
	
	MyGdxGame game;
	OrthographicCamera camera;
	
	public PauseScreen(MyGdxGame game, OrthographicCamera camera) {
		this.game = game;
		this.camera = camera;
	}
	
	public void update() {
		camera.position.set(0, 0, 0);
		camera.update();
		this.draw();
	}
	
	public void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.fontSmaller.draw(game.batch, "Press Enter to return to the MainMenu", -255, 0);
		game.fontSmaller.draw(game.batch, "Press Escape to return to the Game", -237, -50);
		game.titlefont.draw(game.batch, "Paused", -145, 150);
		game.batch.end();
	}
}
