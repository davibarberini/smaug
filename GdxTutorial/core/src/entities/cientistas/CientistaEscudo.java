package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import projeteis.Escudo;


public class CientistaEscudo extends Cientista {
	
	public Escudo escudo;
	public float velTiroX = 0;
	public float velTiroY = 400;
	public boolean isNear = false;
	public String comingFrom = "esquerda";
	public int waitUntilShoot = 50;

	public CientistaEscudo(float x, float y, float w, float h, float vel, int pixelsToWalkRight,
			int pixelsToWalkLeft, Player ply) {
		super(x, y, w, h, vel, pixelsToWalkRight, pixelsToWalkLeft, ply);
		Color color = new Color(1, 1, 1, 1);
		escudo = new Escudo(0, 0, 0, 0, 0, color, ply);
		
		parado[0] = spriteSheet[2][0];
		for(int e=0; e < 4; e++) {
			correndo[e] = spriteSheet[2][e];
		}
		for(int i=0; i < 3; i++) {
			morrendo[i] = spriteSheet[2][i + 4];
		}
		
		morrendoAnim = new Animation<TextureRegion>(0.06f, morrendo);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		correndoAnim = new Animation<TextureRegion>(0.06f, correndo);
	}
	
	public void update(SpriteBatch sb) {
		if(!escudo.isAlive && animState != "morrendo") {
			if(ply.isAttacking) {
				collisionPlayer(sb);
				if(rect.overlaps(p1Rect)) {
					animState = "morrendo";
					stateTime = 0;
					Player.swordKills += 1;
					ganhaVida();
					if(ply.animState == "attacking") {
						Player.attack1Kills += 1;
						System.out.println(Player.attack1Kills);
					}
					else if(ply.animState == "attacking2") {
						Player.attack2Kills += 1;
						System.out.println(Player.attack2Kills);
					}
					else if(ply.animState == "attacking3") {
						Player.attack3Kills += 1;
						System.out.println(Player.attack3Kills);
					}
					else if(ply.animState == "airAttack") {
						Player.airAttackKills += 1;
						System.out.println(Player.airAttackKills);
					}
				}
			}
			if(ply.tiro.rect.overlaps(rect)) {
				animState = "morrendo";
				stateTime = 0;
				Player.cannonKills += 1;
			}
		}
		
		if(isAlive) {
			if(animState == "morrendo") {
				deathCount += 1;
				if(deathCount > 40) {
					isAlive = false;
					deathCount = 0;
				}
			}
			else {
				if(isNear) {
					escudo.count += 1;
					if(escudo.count >= waitUntilShoot && escudo.isAlive == false) {
						escudo.rect.x = rect.x - (escudo.pixelsToProtect / 2);
						escudo.rect.width = rect.width + escudo.pixelsToProtect;
						escudo.rect.height = rect.height + escudo.pixelsToProtect;
						escudo.rect.y = rect.y  - 10;
						escudo.vida = 100;
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
			}
			
			this.draw(sb);
			escudo.update(sb);
			
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
		if(rect.y + 100 > ply.rect.y && ply.rect.y > rect.y - 10) {
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
	public void collisionPlayer(SpriteBatch sb) {
		p1Rect = new Rectangle(ply.rect);
		p1Rect.width = ply.rect.width + ply.widthLimit;
		p1Rect.height = ply.rect.height + ply.heightLimit;
		p1Rect.y = ply.rect.y - (ply.heightLimit / 2);
		if(ply.facing == "esquerda") {
			p1Rect.x = ply.rect.x - ply.widthLimit;
		}
		//sb.draw(teste, p1Rect.x, p1Rect.y, p1Rect.width, p1Rect.height);
		//sb.draw(teste, rect.x, rect.y, rect.width, rect.height);
	}

}
