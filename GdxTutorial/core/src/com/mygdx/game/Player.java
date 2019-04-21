package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Player {
	public Rectangle rect;
	public double gravity, velX, velY;
 
	public Player(float x, float y, float w, float h, double g, double vX, double vY) {
		rect = new Rectangle(x, y, w, h);
		gravity = g;
		velX = vX;
		velY = vY;
	}
 
	public void update() {
		gravity += -1000 * Gdx.graphics.getDeltaTime();
	}
}