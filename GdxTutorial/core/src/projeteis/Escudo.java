package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import platforms.Platform;

public class Escudo extends Platform {
	public int vida = 100;
	public int pixelsToProtect = 35;
	public int vulCount;
	public boolean isVulnerable = true;
	public boolean isAlive;
	Texture sprite = new Texture(Gdx.files.internal("Cientista/shield.png"));
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, sprite.getWidth() / 3, sprite.getHeight());
	TextureRegion[] escudo = new TextureRegion[3];
	TextureRegion atualShield;
	public int count;
	Player ply;
	
	public Escudo(float x, float y, float w, float h, int pType, Color cor, Player ply) {
		super(x, y, w, h, pType, cor);
		this.ply = ply;
		escudo[0] = spriteSheet[0][0];
		escudo[1] = spriteSheet[0][1];
		escudo[2] = spriteSheet[0][2];
		atualShield = escudo[0];
	}
	
	public void update(SpriteBatch sb) {
		if(isAlive) {
			Rectangle temp = new Rectangle(ply.rect);
			if(ply.facing == "esquerda") temp.x = ply.rect.x - 20;
			temp.width = ply.rect.width + 25;
			if(temp.overlaps(rect)) {
				//System.out.println(isVulnerable);
				checkAttack();
			}
			this.draw(sb);
		}
	}
	public void draw(SpriteBatch sb) {
		sb.draw(atualShield, rect.x, rect.y, rect.width, rect.height);
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
    public void checkAttack() {
    	if(isVulnerable) {
	    	if(ply.isAttacking) {
	    			vida -= 50;
	    			if(vida < 60) atualShield = escudo[1];
	    			vulCount = 0; 
	    			isVulnerable = false;
	    			if(vida <= 0) {
	    				isAlive = false;
	    				count = 0;
	    				atualShield = escudo[0];
	    			}
	    		}
	    	if(ply.tiro.rect.overlaps(rect)) {
	    		vida -= 50;
    			vulCount = 0;
    			isVulnerable = false;
    			if(vida <= 0) {
    				isAlive = false;
    				count = 0;   
    			}
    		}
    	}
    	else {
			vulCount += 1;
			if(vulCount > 50) {
				isVulnerable = true;
			}
		}
    	
    }
    public boolean isEscudo() {
    	return true;
    }
}
