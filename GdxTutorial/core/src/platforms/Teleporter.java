package platforms;

import com.badlogic.gdx.graphics.Color;

import entities.Player;

public class Teleporter extends Platform{
	
	public int teleporterCount = 0;
	public float posX = 0;
	public float posY = 0;

	public Teleporter(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	
	public void platCollisionX(double velocidadeX, Player ply) {
		
	}
	public void platCollisionY(double velocidadeY, Player ply) {
		
	}

}
