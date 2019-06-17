package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;

public class PauseScreen {
	
	MyGdxGame game;
	OrthographicCamera camera;
	public static String selected = "return";
	String lastSelected = selected;
	Sound interaction = Gdx.audio.newSound(Gdx.files.internal("TitleScreen/interaction.wav"));
	Texture select = new Texture("TitleScreen/icon.png");
	
	public PauseScreen(MyGdxGame game, OrthographicCamera camera) {
		this.game = game;
		this.camera = camera;
		selected = "return";
	}
	
	public void update() {
		if(lastSelected != selected) {
			lastSelected = selected;
			interaction.play(0.3f);
		}
		camera.position.set(0, 0, 0);
		camera.update();
		this.draw();
	}
	
	public void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.fontSmaller.draw(game.batch, "Return to Game", -120, 0);
		game.fontSmaller.draw(game.batch, "Return to Main Menu", -120, -50);
		game.fontSmaller.draw(game.batch, "Exit Game", -120, -100);
		game.titlefont.draw(game.batch, "Paused", -145, 150);
		if(selected == "return") game.batch.draw(select, -145, -18, 20, 20);
        else if(selected == "mainmenu") game.batch.draw(select, -145, -68, 20, 20);
        else if(selected == "exit") game.batch.draw(select, -145, -118, 20, 20);
    	
		game.batch.end();
	}
}
