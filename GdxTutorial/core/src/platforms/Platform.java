package platforms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;

public class Platform {
	public Rectangle rect;
	public int platformType; 
	public Color color;
	
	public Platform(float x, float y, float w, float h, int pType, Color cor) {
		rect = new Rectangle(x, y, w, h);
		platformType = pType;
		color = cor;
	}
	
    public boolean platCollisionX(double velocidadeX, Player ply) {
    	if(ply.rect.overlaps(rect)) {
    		if(velocidadeX < 0) {
    	    	ply.rect.x = rect.x + rect.width;
    	    }
    	   
    	    else if(velocidadeX > 0) {
    	    	ply.rect.x = rect.x - ply.rect.width;
    	    }
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    public boolean platCollisionY(double velocidadeY, Player ply) {
    	if(ply.rect.overlaps(rect)) {
    		if(velocidadeY < 0) {
    	    	ply.rect.y = rect.y + rect.height;
    	    	ply.gravity = 0;
    	    }
    	   
    	    else if(velocidadeY > 0) {
    	    	ply.rect.y = rect.y - ply.rect.height; 
    	    	ply.gravity = 0;
    	    }
    		return true;
    	}
    	else {
    		return false;
    	}
	    
	   
    }

}
