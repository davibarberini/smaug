package platforms;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Player;

public class Teleporter extends Platform{

	public Teleporter(float x, float y, float w, float h, int pType, Color cor) {
		super(x, y, w, h, pType, cor);
	}
	
	public void platCollision(double velocidadeX, double velocidadeY, Player ply) {
		System.out.println("Teleportado");
	}

}
