package platforms;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import levels.EndScreen;
import levels.Level2;
import levels.TitleScreen;
import projeteis.TiroNormal;
import projeteis.TiroRicochete;

public class Hole extends Platform{
	public static MyGdxGame game;
	public static String nextLevel;
	public boolean collideX = false, collideY = false;

	
	public Hole(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	public boolean platCollisionX(double velocidadeX, Player ply) {
		if(ply.rect.overlaps(rect)) {
			game.setScreen(new EndScreen(game));
			return true;
		}
		else {
			return false;
		}
	}
	public boolean platCollisionY(double velocidadeY, Player ply) {
		if(ply.rect.overlaps(rect)) {
			game.setScreen(new EndScreen(game));
			return true;
		}
		else {
			return false;
		}
		
	}
	public void platCollisionBulletX(TiroRicochete tiro) {}
    public void platCollisionBulletY(TiroRicochete tiro) {}
    public void normalBulletCollision(TiroNormal tiro) {}
    public boolean isPlatform() {
    	return false;
    }
}