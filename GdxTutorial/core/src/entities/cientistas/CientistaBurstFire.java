package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import platforms.Platform;
import projeteis.TiroBurst;

public class CientistaBurstFire extends Cientista {
	
	public TiroBurst[] tiros = new TiroBurst[3];
	public float velTiroX = 0;
	public float velTiroY = 0;
	public int countBurst = 0;
	public boolean isNear = false;
	public String comingFrom = "esquerda";
	public int waitUntilShoot = 100;
	Rectangle p1Rect = ply.rect;
	
	public CientistaBurstFire(float x, float y, float w, float h, float vel, int pixelsToWalkRight,
			int pixelsToWalkLeft, Player ply, Platform[] platforms) {
		super(x, y, w, h, vel, pixelsToWalkRight, pixelsToWalkLeft, ply);
		tiros[0] = new TiroBurst(0, 0, 5, 5, 0, 0, ply, platforms);
		tiros[1] = new TiroBurst(0, 0, 5, 5, 0, 0, ply, platforms);
		tiros[2] = new TiroBurst(0, 0, 5, 5, 0, 0, ply, platforms);
		
		parado[0] = spriteSheet[3][0];
		for(int e=0; e < 4; e++) {
			correndo[e] = spriteSheet[3][e];
		}
		for(int i=0; i < 3; i++) {
			morrendo[i] = spriteSheet[3][i + 4];
		}
		
		morrendoAnim = new Animation<TextureRegion>(0.06f, morrendo);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		correndoAnim = new Animation<TextureRegion>(0.06f, correndo);
	}
	
	public void update(SpriteBatch sb) {
		if(animState != "morrendo" && vulnerable) {
			if(ply.isAttacking) {
				collisionPlayer();
				if(rect.overlaps(p1Rect)) {
					vida -= 10;
					if(vida <= 0) {
						stateTime = 0;
						animState = "morrendo";
					} else {
						vulnerable = false;
						velX = 0;
						fixedX = ply.rect.x;
					}
				}
			}
			if(ply.tiro.rect.overlaps(rect)) {
				vida -= 10;
				if(vida <= 0) {
					stateTime = 0;
					animState = "morrendo";
				} else {
					vulnerable = false;
					velX = 0;
					fixedX = ply.rect.x;
				}
			}
			
		}
		
		if(isAlive) {
			if(!vulnerable) {
				if(fixedX > rect.x) rect.x -= 1;
				else rect.x += 1;
				if(vulnerableCount < 15) rect.y += 1;
				else if(vulnerableCount < 30) rect.y -= 1;
				vulnerableCount += 1;
				if(vulnerableCount > 50) {
					velX = vel;
					vulnerable = true;
					vulnerableCount = 0;
				}
			}
			if(animState == "morrendo") {
				deathCount += 1;
				if(deathCount > 40) {
					isAlive = false;
					deathCount = 0;
				}
			}
			else {
				if(isNear) {
					countBurst += 1;
					for(int e=0; e < tiros.length; e++) {
						tiros[e].drawFlash = true;
						tiros[e].count += 1;
						if(countBurst > e * 10) {
							if(tiros[e].count >= waitUntilShoot && tiros[e].isAlive == false) {
								tiros[e].rect.x = rect.x;
								tiros[e].rect.y = rect.y + 20;
								tiros[e].velX = velTiroX;
								tiros[e].velY = velTiroY;
								tiros[e].wait = waitUntilShoot + 50;
								tiros[e].isAlive = true;
							}
						}
					}
					if(countBurst > 30) countBurst = 0;
				}
					
				else {
					for(int e=0; e < tiros.length; e++) {
						tiros[e].drawFlash = false;
					}
					rect.x += velX * Gdx.graphics.getDeltaTime();
					walked += velX * Gdx.graphics.getDeltaTime();
					if(walked >= toWalkRight) {
						velX = -vel;
					}
						
					else if(walked <= -toWalkLeft) {
						velX = vel;
					}
				}
				this.checkNear();
			}
			
			for(int e=0; e < tiros.length; e++) {
				if(comingFrom == "direita") tiros[e].fixedX = rect.x + 20;
				else if(comingFrom == "esquerda") tiros[e].fixedX = rect.x - 20;
				tiros[e].fixedY = rect.y + 20;
				tiros[e].update(sb);
			}
			this.draw(sb);
		}
		
	}
	public void draw(SpriteBatch sb) {
		if(animState == "running") {
			if(velX > 0) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + pCorrectX, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width - pCorrectX, this.rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}	
		}
		else if(animState == "parado") {
			if(comingFrom == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + pCorrectX, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width - pCorrectX, this.rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}	
		}
		else if(animState == "morrendo") {
			if(velX > 0) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = morrendoAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x  + pCorrectX, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = morrendoAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + this.rect.width - pCorrectX, this.rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}	
		}
		else {
			stateTime = 0;
		}
		
	}
	
	public void checkNear() {
		if(rect.y + 80 > ply.rect.y && ply.rect.y > rect.y - 40) {
			isNear = true;
			animState = "parado";
			if(rect.x > ply.rect.x) {
				comingFrom = "esquerda";
				velTiroX = -400;
			}
			else {
				comingFrom = "direita";
				velTiroX = 400;
			}
		}
		else {
			isNear = false;
			animState = "running";
		} 
		
		
	}

}
