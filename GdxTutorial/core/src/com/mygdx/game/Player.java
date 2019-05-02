package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Player extends Sprite {
	public Rectangle rect;
	public double gravity, velX, velY;
	public int numColunas = 5;
	public int numLinhas = 2;
	public String animState = "parado";
	public float stateTime;
	Animation<TextureRegion> correndoDireitaAnim;
	Animation<TextureRegion> paradoAnim;
	TextureRegion currentFrame;
	Texture ninja = new Texture(Gdx.files.internal("ninja.png"));
	TextureRegion[][] correndoDireitaTmp = TextureRegion.split(ninja, ninja.getWidth() / numColunas, ninja.getHeight()/ numLinhas);
	TextureRegion[] correndoDireita = new TextureRegion[numColunas * numLinhas];
	TextureRegion[] parado = new TextureRegion[1];

	public Player(float x, float y, float w, float h, double g, double vX, double vY) {
		rect = new Rectangle(x, y, w, h);
		gravity = g; 
		velX = vX;
		velY = vY;
		int count = 0;
		for(int i=0; i < numLinhas; i++) {
			for(int e=0; e < numColunas; e++) {
				correndoDireita[count ++] = correndoDireitaTmp[i][e];

			}
		}
		System.out.println(correndoDireita.length + "--" + correndoDireitaTmp.length);

		parado[0] = correndoDireitaTmp[0][0];
		correndoDireitaAnim = new Animation<TextureRegion>(0.06f, correndoDireita);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		
	}
 
	public void update() {
		gravity += -1000 * Gdx.graphics.getDeltaTime();
	}
	
	
	public void draw(SpriteBatch sb) {
		if(animState == "parado") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = correndoDireitaAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, this.rect.x, this.rect.y, 35, 35);
		}
	}
}