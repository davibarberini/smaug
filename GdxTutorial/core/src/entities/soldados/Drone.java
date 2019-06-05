package entities.soldados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import platforms.Platform;
import projeteis.TiroDrone;

public class Drone extends Soldado {
	
	public TiroDrone tiro;
	public float velTiroX = 0;
	public float velTiroY = -400;
	public boolean isNear = false;
	public String comingFrom = "esquerda";
	Texture vidaTXT = new Texture("Player/vida.png");
	public int waitUntilShoot = 30;
	public float spriteLargura = 80; 
	public float spriteAltura = 80;
	public float pCorrectX = -14;
	public float pCorrectY = -30;
	public Thread thread;
	public boolean runningThread = false;
	Rectangle p1Rect = ply.rect;
	Platform[] platforms;
	Texture sprite = new Texture(Gdx.files.internal("Soldado/drone.png"));
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 50, 50);
	
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] paradoAtirando = new TextureRegion[1];
	TextureRegion[] morrendo = new TextureRegion[4];
	TextureRegion[] morrendoCanhao = new TextureRegion[3];
	TextureRegion[] correndo = new TextureRegion[1];
	
	public Drone(float x, float y, float w, float h, float vel, int pixelsToWalkRight,
			int pixelsToWalkLeft, Player ply, Platform[] platforms) {
		super(x, y, w, h, vel, pixelsToWalkRight, pixelsToWalkLeft, ply);
		tiro = new TiroDrone(0, 0, 10, 10, 0, 0, ply, platforms);
		this.platforms = platforms;
		parado[0] = spriteSheet[0][0];
		paradoAtirando[0] = spriteSheet[0][0];
		correndo[0] = spriteSheet[0][1];
		
		for(int i=0; i < 4; i++) {
			morrendo[i] = spriteSheet[1][i];
		}
		for(int i=0; i < 3; i++) {
			morrendoCanhao[i] = spriteSheet[2][i];
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
					if(atirandoAnimCount > 10){
						animState = "parado";
						atirandoAnimCount = 0;
					}
					tiro.count += 1;
					if(tiro.count >= waitUntilShoot && tiro.isAlive == false) {
						animState = "paradoAtirando";
						tiro.rect.x = rect.x + 32;
						tiro.rect.y = rect.y - 10;
						tiro.velX = velTiroX;
						tiro.velY = velTiroY;
						tiro.wait = waitUntilShoot + 100;
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
			}
			tiro.fixedY = rect.y + 20;
			//sb.draw(vidaTXT, rect.x, rect.y, rect.width, rect.height);
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
		if(rect.y + 10 > ply.rect.y && ply.rect.y > rect.y - 800 && ply.rect.x > rect.x - 120 && ply.rect.x < rect.x + 120) {
			isNear = true;
			if(animState != "paradoAtirando") animState = "parado";
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
