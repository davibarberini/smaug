package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Player;
import projeteis.Escudo;


public class CientistaEscudo extends Cientista {
	
	public Escudo escudo;
	public float velTiroX = 0;
	public float velTiroY = 400;
	public boolean isNear = false;
	public String comingFrom = "esquerda";
	public int waitUntilShoot = 100;

	public CientistaEscudo(float x, float y, float w, float h, float vel, int pixelsToWalkRight,
			int pixelsToWalkLeft, Player ply) {
		super(x, y, w, h, vel, pixelsToWalkRight, pixelsToWalkLeft, ply);
		Color color = new Color(1, 1, 1, 1);
		escudo = new Escudo(0, 0, 0, 0, 0, color, ply);
	}
	
	public void update(SpriteBatch sb) {
		if(ply.isAttacking) {
			if(rect.overlaps(ply.rect)) {
				isAlive = false;
			}
		}
		if(isAlive) {
			if(isNear) {
				escudo.count += 1;
				if(escudo.count >= waitUntilShoot && escudo.isAlive == false) {
					escudo.rect.x = rect.x - (escudo.pixelsToProtect / 2);
					escudo.rect.width = rect.width + escudo.pixelsToProtect - 15;
					escudo.rect.height = rect.height + escudo.pixelsToProtect;
					escudo.rect.y = rect.y;
					escudo.isAlive = true;
				}
			}
			else if(!escudo.isAlive){
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
			escudo.update(sb);
			
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
			}
			else {
				comingFrom = "direita";
			}
		}
		else {
			isNear = false;
			if(!escudo.isAlive) animState = "running";
		} 
		
		
	}
	public Escudo getEscudo() {
		return escudo;
		
	}

}
