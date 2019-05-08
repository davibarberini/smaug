package platforms;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import levels.Level2;
import levels.TitleScreen;

public class NextLevel extends Platform{
	public static MyGdxGame game;
	public static String nextLevel;
	
	public NextLevel(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	public void platCollisionX(double velocidadeX, Player ply) {
		System.out.println(nextLevel);
		if(nextLevel == "Level2") {
			game.setScreen(new Level2(game));
		}
		else if(nextLevel == "TitleScreen") {
			game.setScreen(new TitleScreen(game));
		}
	}
	public void platCollisionY(double velocidadeY, Player ply) {
		System.out.println(nextLevel);
		if(nextLevel == "Level2") {
			game.setScreen(new Level2(game));
		}
		else if(nextLevel == "TitleScreen") {
			game.setScreen(new TitleScreen(game));
		}
	}

}
