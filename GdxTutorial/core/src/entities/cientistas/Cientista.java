package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

import entities.Player;
import projeteis.Escudo;


public class Cientista extends Sprite implements Runnable{
	public boolean runningThread = false;
	public Thread thread;
	public Rectangle rect;
	public float velX, vel;
	public float fixedY, fixedX;
	public float vida = 20;
	public int numColunas = 8;
	public int numLinhas = 1;
	public float spriteLargura = 80; 
	public float spriteAltura = 80;
	public float pCorrectX = -25;
	public float pCorrectY = -20;
	public String animState = "running";
	public float stateTime;
	public int toWalkLeft, toWalkRight;
	public float walked = 0;
	public boolean isAlive = true;
	public boolean vulnerable = true;
	public int vulnerableCount = 0;
	public Player ply;
	public int atirandoAnimCount = 0;
	public int deathCount = 0;
	Rectangle p1Rect;
	Random rand = new Random();
	Sound tiroSound = Gdx.audio.newSound(Gdx.files.internal("Cientista/Sounds/cientistaTiro.wav"));
	Sound morrendoSound = Gdx.audio.newSound(Gdx.files.internal("Cientista/Sounds/morrendo.wav"));
	Sound damageSound = Gdx.audio.newSound(Gdx.files.internal("Cientista/Sounds/damage.wav"));
	
	Animation<TextureRegion> paradoAnim;
	Animation<TextureRegion> paradoAtirandoAnim;
	Animation<TextureRegion> correndoAnim;
	Animation<TextureRegion> morrendoAnim;
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Cientista/cientista.png"));
	Texture teste = new Texture("Player/vida.png");
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 80, 80);
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] paradoAtirando = new TextureRegion[1];
	TextureRegion[] correndo = new TextureRegion[4];
	TextureRegion[] morrendo = new TextureRegion[3];

	public Cientista(float x, float y, float w, float h, float vel, int pixelsToWalkRight, int pixelsToWalkLeft, Player ply) {
		rect = new Rectangle(x, y, w, h);
		fixedY = y;
		this.ply = ply;
		this.vel = vel;
		velX = vel;
		toWalkRight = pixelsToWalkRight;
		toWalkLeft = pixelsToWalkLeft;
		
		parado[0] = spriteSheet[4][0];
		for(int e=0; e < 4; e++) {
			correndo[e] = spriteSheet[4][e];
		}
		
		for(int i=0; i < 3; i++) {
			morrendo[i] = spriteSheet[4][i + 4];
		}
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		correndoAnim = new Animation<TextureRegion>(0.06f, correndo);
		morrendoAnim = new Animation<TextureRegion>(0.06f, morrendo);
		
	}
 
	public void update(SpriteBatch sb) {
		if(!runningThread) {
			runningThread = true;
			thread = new Thread(this);
			thread.start();
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
				rect.x += velX * Gdx.graphics.getDeltaTime();
				walked += velX * Gdx.graphics.getDeltaTime();
				if(walked >= toWalkRight) {
					velX = -vel;
				}
				else if(walked <= -toWalkLeft) {
					velX = vel;
				}
			}
			this.draw(sb);
		}
		
		
		
	}
	public void draw(SpriteBatch sb) {
		if(animState == "running") {
			if(velX > 0) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x  + pCorrectX, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
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
	public Escudo getEscudo() {
		Color color = new Color(1, 1, 1, 1);
		return new Escudo(0, 0, 0, 0, 0, color, ply);
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
	
	public void ganhaVida() {
		//pega um valor de 0 a 3
		int n = rand.nextInt(4);
		if(n == 2) {
			Player.vida += 10;
			if(Player.vida > 100) Player.vida = 100;
			ply.regen.alive = true;
			ply.regenSound.play(0.3f);
		}
		System.out.println(n);
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
							morrendoSound.play(0.5f);
							stateTime = 0;
							animState = "morrendo";
							ganhaVida();
							Player.swordKills += 1;
							if(ply.animState == "attacking") {
								Player.attack1Kills += 1;
								//System.out.println(Player.attack1Kills);
							}
							else if(ply.animState == "attacking2") {
								Player.attack2Kills += 1;
								//System.out.println(Player.attack2Kills);
							}
							else if(ply.animState == "attacking3") {
								Player.attack3Kills += 1;
								//System.out.println(Player.attack3Kills);
							}
							else if(ply.animState == "airAttack") {
								Player.airAttackKills += 1;
								//System.out.println(Player.airAttackKills);
							}
							this.dispose();
						} else {
							damageSound.play(0.3f);
							vulnerable = false;
							velX = 0;
							fixedX = ply.rect.x;
						}
						
					}
				}
				if(ply.tiro.rect.overlaps(rect) && ply.tiro.isAlive) {
					vida -= 10;
					if(vida <= 0) {
						morrendoSound.play(0.5f);
						stateTime = 0;
						animState = "morrendo";
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