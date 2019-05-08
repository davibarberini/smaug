package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Player;
import projeteis.TiroNormal;

public class CientistaAtirador extends Cientista {
	
	public TiroNormal tiro;
	public float velTiroX = 0;
	public float velTiroY = 0;
	public boolean isNear = false;
	public String comingFrom = "esquerda";
	public int waitUntilShoot = 50;

	public CientistaAtirador(float x, float y, float w, float h, float vel, int pixelsToWalkRight,
			int pixelsToWalkLeft, Player ply) {
		super(x, y, w, h, vel, pixelsToWalkRight, pixelsToWalkLeft, ply);
		tiro = new TiroNormal(0, 0, 10, 10, 0, 0, ply);
	}
	
	public void update(SpriteBatch sb) {
		if(ply.isAttacking) {
			if(rect.overlaps(ply.rect)) {
				isAlive = false;
			}
		}
		if(isAlive) {
			if(isNear) {
				tiro.count += 1;
				if(tiro.count >= waitUntilShoot && tiro.isAlive == false) {
					tiro.rect.x = rect.x;
					tiro.rect.y = rect.y + 20;
					tiro.velX = velTiroX;
					tiro.velY = velTiroY;
					tiro.wait = waitUntilShoot + 50;
					tiro.isAlive = true;
				}
			}
			else {
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
			this.draw(sb);
			tiro.update(sb);
			
		}
		
		
		
	}
	public void draw(SpriteBatch sb) {
		if(animState == "running") {
			if(velX > 0) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y, -spriteLargura, spriteAltura);
			}	
		}
		else if(animState == "parado") {
			if(comingFrom == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y, -spriteLargura, spriteAltura);
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
