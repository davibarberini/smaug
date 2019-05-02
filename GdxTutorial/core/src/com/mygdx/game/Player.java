package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	public int spriteLargura = 45;
	public int spriteAltura = 70;
	public String animState = "parado";
	public float stateTime;
	public String facing = "direita";
	
	Animation<TextureRegion> correndoAnim;
	Animation<TextureRegion> paradoAnim;
	Animation<TextureRegion> jumpingAnim;
	
	TextureRegion currentFrame;
	Texture ninja = new Texture(Gdx.files.internal("ninja.png"));
	
	TextureRegion[][] correndoTmp = TextureRegion.split(ninja, ninja.getWidth() / numColunas, ninja.getHeight()/ numLinhas);
	TextureRegion[] correndo = new TextureRegion[numColunas * numLinhas];
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] jumping = new TextureRegion[1];

	public Player(float x, float y, float w, float h, double g, double vX, double vY) {
		rect = new Rectangle(x, y, w, h);
		gravity = g; 
		velX = vX;
		velY = vY;
		int count = 0;
		for(int i=0; i < numLinhas; i++) {
			for(int e=0; e < numColunas; e++) {
				correndo[count ++] = correndoTmp[i][e];

			}
		}
		System.out.println(correndo.length + "--" + correndoTmp.length);

		parado[0] = correndoTmp[0][1];
		jumping[0] = correndoTmp[0][0];
		
		correndoAnim = new Animation<TextureRegion>(0.06f, correndo);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		jumpingAnim = new Animation<TextureRegion>(0.06f, jumping);
		
	}
 
	public void update() {
		gravity += -1000 * Gdx.graphics.getDeltaTime();
		if(gravity < 50) {
			if(velX == 0) {
				animState = "parado";
			}
			else {
				animState = "running";
			}
		}
	}
	
	
	public void draw(SpriteBatch sb) {
		if(animState == "parado") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + 35, this.rect.y, -spriteLargura, spriteAltura);
			}
			
		}		
		else if(animState == "jumping") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = jumpingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = jumpingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + 35, this.rect.y, -spriteLargura, spriteAltura);
			}
			
		}
		else if(animState == "running") {
			if(velX > 0) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + 35, this.rect.y, -spriteLargura, spriteAltura);
			}
		}
		else {
			stateTime = 0;
		}
	}
	
	public void keyDown(int keyCode) {
		if(keyCode == Input.Keys.W) {
            this.gravity = 500;
            animState = "jumping";
        }
        else if(keyCode == Input.Keys.D) {
        	this.velX = 500;
        	if(animState != "jumping") animState = "running";
        	facing = "direita";
        }
        else if(keyCode == Input.Keys.A) {
        	this.velX = -500;
        	if(animState != "jumping") animState = "running";
        	facing = "esquerda";
        }
	}
	
	public void keyUp(int keyCode) {
	  if(keyCode == Input.Keys.D && this.velX > 0) {
        	this.velX = 0;
        	if(animState != "jumping") animState = "parado";
      }
	  else if(keyCode == Input.Keys.A && this.velX < 0) {
        	this.velX = 0;
        	if(animState != "jumping") animState = "parado";
      }
	}
}