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
		
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		correndoAnim = new Animation<TextureRegion>(0.06f, correndo);
	}
	
	public void update(SpriteBatch sb) {
		if(ply.isAttacking) {
			Rectangle p1Rect = new Rectangle(ply.rect);
			p1Rect.width = ply.rect.width + 25;
			if(rect.overlaps(p1Rect)) {
				isAlive = false;
			}
		}
		if(isAlive) {
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
			if(comingFrom == "direita") tiro.fixedX = rect.x + 5;
			else if(comingFrom == "esquerda") tiro.fixedX = rect.x - 5;
			tiro.fixedY = rect.y + 18;
			tiro.fixedVelX = velTiroX;
			this.checkNear();
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
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y + pCorrectY, -spriteLargura, spriteAltura);
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
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y + pCorrectY, -spriteLargura, spriteAltura);
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
