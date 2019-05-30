package platforms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import projeteis.TiroNormal;
import projeteis.TiroPlayer;
import projeteis.TiroRicochete;

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
    	    	ply.jumpCount = 0;
    	    	if(ply.velX == 0 && (!ply.isAttacking || ply.animState == "airAttack")) {
    				ply.animState = "parado";
    				ply.resetAttack();
    			}
    			else {
    				if(!ply.isAttacking || ply.animState == "airAttack") {
    					ply.animState = "running";
    					ply.resetAttack();
    			}	}
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
    public void platCollisionBulletX(TiroRicochete tiro) {
    	if(tiro.rect.overlaps(rect)) {
    		if(tiro.velX > 0) {
    			tiro.rect.x = rect.x - tiro.rect.width;
    			tiro.velX = -tiro.vel;
    		}
    		else if(tiro.velX < 0) {
    			tiro.rect.x = rect.x + rect.width;
    			tiro.velX = tiro.vel;
    		}
    	}
    	
    }
    public void platCollisionBulletY(TiroRicochete tiro) {
    	if(tiro.rect.overlaps(rect)) {
    		if(tiro.velY > 0) {
    			tiro.rect.y = rect.y - tiro.rect.height;
    			tiro.velY = -tiro.vel;
    		}
    		else if(tiro.velY < 0) {
    			tiro.rect.y = rect.y + rect.height;
    			tiro.velY = tiro.vel;
    		}
    	}
    	
    }
    public void normalBulletCollision(TiroNormal tiro) {
    	if(tiro.rect.overlaps(rect)) {
    		tiro.count = 0;
    		tiro.isAlive = false;
    	}
    }
    public void playerBulletCollision(TiroPlayer tiro) {
    	if(tiro.rect.overlaps(rect) && MyGdxGame.actualLevel == "Level1") {
    		tiro.count = 0;
    		tiro.isAlive = false;
    	}
    }
    public void genericPlatformCollisionX(Rectangle secondRect, double velX) {
    	if(secondRect.overlaps(rect)) {
    		if(velX > 0) {
    			secondRect.x = rect.x - secondRect.width;
    		}
    		else if(velX < 0) {
    			secondRect.x = rect.x + rect.width;
    		}
    	}
    	
    }
    public void genericPlatformCollisionY(Rectangle secondRect, double velY) {
    	if(secondRect.overlaps(rect)) {
    		if(velY > 0) {
    			secondRect.y = rect.y - secondRect.height;
    		}
    		else if(velY < 0) {
    			secondRect.y = rect.y + rect.height;
    		}
    	}
    	
    }
    
    public boolean isPlatform() {
    	return true;
    }
    public boolean isEscudo() {
    	return false;
    }

}
