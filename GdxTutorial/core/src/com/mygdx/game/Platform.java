package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class Platform {
	public Rectangle rect;
	
	public Platform(float x, float y, float w, float h) {
		rect = new Rectangle(x, y, w, h);
	}
	
    public void platCollision(double velocidadeX, double velocidadeY, Player ply) {
    	
	    if(velocidadeY < 0) {
	    	ply.rect.y = rect.y + rect.height;
	    	ply.gravity = 0;
	    }
	   
	    else if(velocidadeY > 0) {
	    	ply.rect.y = rect.y - ply.rect.height; 
	    	ply.gravity = 0;
	    }
	   
	    if(velocidadeX < 0) {
	    	ply.rect.x = rect.x + rect.width;
	    	ply.velX = 0; 
	    }
	   
	    else if(velocidadeX > 0) {
	    	ply.rect.x = rect.x - ply.rect.width;
	    	ply.velX = 0;
	    }
    }

}
