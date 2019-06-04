package entities.soldados;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import platforms.Platform;
import projeteis.TiroAtravessa;
import projeteis.TiroSoldado;

public class SoldadoAtirador extends Soldado {
	
	public ArrayList<TiroSoldado> tiros = new ArrayList<TiroSoldado>();
	public float velTiroX = 0;
	public float velTiroY = 0;
	public boolean isNear = false;
	public String comingFrom = "esquerda";
	int countTiro = 0;
	int atiradoCount = 0;
	Texture vidaTXT = new Texture("Player/vida.png");
	public int waitUntilShoot = 50;
	boolean reload = false;
	int reloadCount = 0;
	public Thread thread;
	public boolean runningThread = false;
	Rectangle p1Rect = ply.rect;
	Platform[] platforms;
	Texture sprite = new Texture(Gdx.files.internal("Soldado/soldado.png"));
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 80, 80);
	
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] paradoAtirando = new TextureRegion[1];
	TextureRegion[] morrendo = new TextureRegion[4];
	TextureRegion[] morrendoCanhao = new TextureRegion[4];
	
	public SoldadoAtirador(float x, float y, float w, float h, float vel, int pixelsToWalkRight,
			int pixelsToWalkLeft, Player ply, Platform[] platforms) {
		super(x, y, w, h, vel, pixelsToWalkRight, pixelsToWalkLeft, ply);
		//tiro = new TiroAtravessa(0, 0, 10, 5, 0, 0, ply);
		
		this.platforms = platforms;
		parado[0] = spriteSheet[1][0];
		paradoAtirando[0] = spriteSheet[1][1];
		for(int e=0; e < 6; e++) {
			correndo[e] = spriteSheet[0][e];
		}
		for(int i=0; i < 4; i++) {
			morrendo[i] = spriteSheet[2][i];
		}
		for(int i=0; i < 4; i++) {
			morrendoCanhao[i] = spriteSheet[3][i];
		}
		
		morrendoAnim = new Animation<TextureRegion>(0.06f, morrendo);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		paradoAtirandoAnim = new Animation<TextureRegion>(0.06f, paradoAtirando);
		correndoAnim = new Animation<TextureRegion>(0.1f, correndo);
		morrendoCanhaoAnim = new Animation<TextureRegion>(0.06f, morrendoCanhao);
		
	}
	
	public void update(SpriteBatch sb) {
		if(!runningThread) {
			thread = new Thread(this);
			thread.start();
			runningThread = true;
		}
		
		if(isAlive) {
			if(!vulnerable) {
				if(fixedX > rect.x) {
					rect.x -= 1;
					for(int k=0; k < platforms.length; k++) {   //Colisao após a movimentação X
						  if(platforms[k] != null) {
							  Platform plat = platforms[k];
							  plat.genericPlatformCollisionX(rect, -10);
						  }  
					}
				} 
				else {
					rect.x += 1;
					for(int k=0; k < platforms.length; k++) {   //Colisao após a movimentação X
						  if(platforms[k] != null) {
							  Platform plat = platforms[k];
							  plat.genericPlatformCollisionX(rect, 10);
						  }  
					}
				}
				if(vulnerableCount < 15) rect.y += 1;
				else if(vulnerableCount < 30) rect.y -= 1;
				vulnerableCount += 1;
				if(vulnerableCount > 50) {
					velX = vel;
					vulnerable = true;
					vulnerableCount = 0;
				}
			}
			if(animState == "morrendo" || animState == "morrendoCanhao") {
				deathCount += 1;
				if(deathCount > 40) {
					isAlive = false;
					deathCount = 0;
				}
			}
			else {
				if(isNear) {
					if(animState == "paradoAtirando") atirandoAnimCount += 1;
					if(atirandoAnimCount > 5){
						animState = "parado";
						atirandoAnimCount = 0;
					}
					if(atiradoCount < 10 && !reload && countTiro > 5) {
						TiroSoldado tiro = new TiroSoldado(0, 0, 10, 5, 0, 0, ply);
						animState = "paradoAtirando";
						tiro.rect.x = rect.x;
						tiro.rect.y = rect.y + 20;
						tiro.velX = velTiroX;
						tiro.velY = velTiroY;
						tiro.wait = waitUntilShoot + 50;
						tiros.add(tiro);
						countTiro = 0;
						atiradoCount += 1;
					}
					else {
						countTiro += 1;
					}
					if(atiradoCount >= 10) reload = true;
				}
				else {
					rect.x += velX * Gdx.graphics.getDeltaTime();
					for(int k=0; k < platforms.length; k++) {   //Colisao após a movimentação X
						  if(platforms[k] != null) {
							  Platform plat = platforms[k];
							  plat.genericPlatformCollisionX(rect, velX);
						  }
						  
					}
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
			for(int e=0; e < tiros.size(); e++) {
				TiroSoldado tiro = tiros.get(e);
				if(comingFrom == "direita") tiro.fixedX = rect.x + 20;
				else if(comingFrom == "esquerda") tiro.fixedX = rect.x - 20;
				tiro.fixedY = rect.y + 20;
				tiro.update(sb);
				if(tiro.rect.overlaps(ply.rect)) {
					Player.vida -= 10;
					tiros.remove(e);
				}
				if(tiro.count > tiro.wait) {
					tiros.remove(e);
				}
			}
			
			//sb.draw(vidaTXT, rect.x, rect.y, rect.width, rect.height);
			this.draw(sb);
			if(reload) {
				reloadCount += 1;
				if(reloadCount > 100) {
					reload = false;
					atiradoCount = 0;
					reloadCount = 0;
				}
			}
			
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
		else if(animState == "paradoAtirando") {
			if(comingFrom == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAtirandoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + pCorrectX, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAtirandoAnim.getKeyFrame(stateTime, true);
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
		else if(animState == "morrendoCanhao") {
			if(velX > 0) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = morrendoCanhaoAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x  + pCorrectX, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = morrendoCanhaoAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + this.rect.width - pCorrectX, this.rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}	
		}
		else {
			stateTime = 0;
		}
		
	}
	
	public void checkNear() {
		if(rect.y + 120 > ply.rect.y && ply.rect.y > rect.y - 10 && ply.rect.x > rect.x - 300 && ply.rect.x < rect.x + 300) {
			isNear = true;
			if(animState != "paradoAtirando") animState = "parado";
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
	public void collisionPlayer() {
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
	
	@Override
	public void run() {
		while(runningThread) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(animState != "morrendo" && vulnerable) {
				if(ply.isAttacking) {
					collisionPlayer();
					if(rect.overlaps(p1Rect)) {
						vida -= 10;
						if(vida <= 0) {
							stateTime = 0;
							animState = "morrendo";
							Player.swordKills += 1;
							if(ply.animState == "attacking") {
								Player.attack1Kills += 1;
							}
							else if(ply.animState == "attacking2") {
								Player.attack2Kills += 1;
							}
							else if(ply.animState == "attacking3") {
								Player.attack3Kills += 1;
							}
							else if(ply.animState == "airAttack") {
								Player.airAttackKills += 1;
							}
							this.dispose();
						} else {
							vulnerable = false;
							velX = 0;
							fixedX = ply.rect.x;
						}
					}
				}
				if(ply.tiro.rect.overlaps(rect) && ply.tiro.isAlive) {
					ply.tiro.isAlive = false;
					ply.tiro.count = 0;
					vida -= 10;
					if(vida <= 0) {
						stateTime = 0;
						animState = "morrendoCanhao";
						Player.cannonKills += 1;
					} else {
						vulnerable = false;
						velX = 0;
						fixedX = ply.rect.x;
					}
				}
				
			}
			
		}
	}
	
	public void dispose() {
		this.runningThread = false;
		this.thread.interrupt();
	}
	

}
