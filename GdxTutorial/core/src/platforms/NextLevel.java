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
	public boolean collideX = false, collideY = false;

	
	public NextLevel(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	public boolean platCollisionX(double velocidadeX, Player ply) {
		if(ply.rect.overlaps(rect)) {
			collideX = false;
			System.out.println(nextLevel);
			if(!collideY) {
				if(nextLevel == "Level2") {
					game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
	              	game.t1.interrupt();
					collideX = true;
					game.setScreen(new Level2(game));
				}
				else if(nextLevel == "TitleScreen") {
					game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
	              	game.t1.interrupt();
					collideX = true;
					game.setScreen(new TitleScreen(game));
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	public boolean platCollisionY(double velocidadeY, Player ply) {
		if(ply.rect.overlaps(rect)) {
			collideY = false;
			System.out.println(nextLevel);
			if(!collideX) {
				if(nextLevel == "Level2") {
					collideY = true;
					game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
	              	game.t1.interrupt();
					game.setScreen(new Level2(game));
				}
				else if(nextLevel == "TitleScreen") {
					collideY = true;
					game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
	              	game.t1.interrupt();
					game.setScreen(new TitleScreen(game));
				}
			}
			return true;
		}
		else {
			return false;
		}
		
	}

}
