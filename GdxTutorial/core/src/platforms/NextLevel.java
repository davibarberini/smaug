package platforms;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import levels.Level2;
import levels.Level3;
import levels.TitleScreen;
import projeteis.TiroNormal;
import projeteis.TiroRicochete;

public class NextLevel extends Platform{
	public MyGdxGame game;
	public String nextLevel;
	public boolean collideX = false, collideY = false;

	
	public NextLevel(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	public boolean platCollisionX(double velocidadeX, Player ply) {
		if(ply.rect.overlaps(rect)) {
			collideX = false;
			System.out.println(nextLevel);
			if(!collideY) {
				game.transitionSound.play(0.2f);
				if(nextLevel == "Level2") {
					collideX = true;
					game.transition = true;
				}
				else if(nextLevel == "Level3") {
					collideX = true;
					game.transition = true;
				}
				else if(nextLevel == "TitleScreen") {
					collideX = true;
					game.transition = true;
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
				game.transitionSound.play(0.2f);
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
	public void platCollisionBulletX(TiroRicochete tiro) {}
    public void platCollisionBulletY(TiroRicochete tiro) {}
    public void normalBulletCollision(TiroNormal tiro) {}
    public boolean isPlatform() {
    	return false;
    }
}
