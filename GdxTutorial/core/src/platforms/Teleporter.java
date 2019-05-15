package platforms;

import com.badlogic.gdx.graphics.Color;

import entities.Player;
import projeteis.TiroNormal;
import projeteis.TiroRicochete;

public class Teleporter extends Platform{
	
	public int teleporterCount = 0;
	public float posX = 0;
	public float posY = 0;

	public Teleporter(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	
	public boolean platCollisionX(double velocidadeX, Player ply) {
		if(ply.rect.overlaps(rect)) return true;
		else return false;
	}
	public boolean platCollisionY(double velocidadeY, Player ply) {
		if(ply.rect.overlaps(rect)) return true;
		else return false;
	}
	public void platCollisionBulletX(TiroRicochete tiro) {}
    public void platCollisionBulletY(TiroRicochete tiro) {}
    public void normalBulletCollision(TiroNormal tiro) {}
    public boolean isPlatform() {
    	return false;
    }

}
