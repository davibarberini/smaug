package platforms;

import com.badlogic.gdx.graphics.Color;

import entities.Player;

public class Teleporter2 extends Platform{
	
	public int teleporterCount = 0;
	public float posX = 0;
	public float posY = 0;

	public Teleporter2(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	
	public boolean platCollisionX(double velocidadeX, Player ply) {
		if(ply.rect.overlaps(rect)) {
			if(!ply.isColliding) {
				ply.rect.x = posX;
				ply.rect.y = posY;
				ply.isColliding = true;
			}
			return true;
		}
		else {
			ply.isColliding = false;
			return false;
		}
		
		
	}
	public boolean platCollisionY(double velocidadeY, Player ply) {
		if(ply.rect.overlaps(rect)) {
			if(!ply.isColliding) {
				ply.rect.x = posX;
				ply.rect.y = posY;
				ply.isColliding = true;
			}
			return true;
		}
		else {
			ply.isColliding = false;
			return false;
		}
		
		
	}

}
