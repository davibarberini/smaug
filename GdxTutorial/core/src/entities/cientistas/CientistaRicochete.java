package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import platforms.Platform;
import projeteis.TiroNormal;
import projeteis.TiroRicochete;

public class CientistaRicochete extends Cientista {
	
	public TiroRicochete tiro;
	public float velTiroX = 0;
	public float velTiroY = 400;
	public boolean isNear = false;
	public String comingFrom = "esquerda";
	public int waitUntilShoot = 50;

	public CientistaRicochete(float x, float y, float w, float h, float vel, int pixelsToWalkRight,
			int pixelsToWalkLeft, Player ply, Platform[] platforms) {
		super(x, y, w, h, vel, pixelsToWalkRight, pixelsToWalkLeft, ply);
		tiro = new TiroRicochete(0, 0, 10, 10, 0, 0, ply, platforms);
		
		parado[0] = spriteSheet[1][0];
		for(int e=0; e < 4; e++) {
			correndo[e] = spriteSheet[1][e];
		}
		for(int i=0; i < 3; i++) {
			morrendo[i] = spriteSheet[1][i + 4];
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
					tiro.drawFlash = true;
					tiro.count += 1;
					if(tiro.count >= waitUntilShoot && tiro.isAlive == false) {
						tiro.moved = 0;
						tiro.rect.x = rect.x;
						tiro.rect.y = rect.y + 20;
						tiro.velX = velTiroX;
						tiro.vel = velTiroY;
						tiro.velY = tiro.vel;
						tiro.wait = waitUntilShoot + 150;
						tiro.isAlive = true;
					}
				}
				else {
					tiro.drawFlash = false;
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
			
			if(comingFrom == "direita") tiro.fixedX = rect.x + 5;
			else if(comingFrom == "esquerda") tiro.fixedX = rect.x - 5;
			tiro.fixedY = rect.y + 18;
			tiro.fixedVelX = velTiroX;
			this.draw(sb);
			tiro.update(sb);
			
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
				velTiroX = -300;
			}
			else {
				comingFrom = "direita";
				velTiroX = 300;
			}
		}
		else {
			isNear = false;
			animState = "running";
		} 
		
		
	}

}
