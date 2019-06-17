package entities;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;

import platforms.Platform;
import projeteis.RaioCap;
import projeteis.TiroCap;
import projeteis.TiroTank;

public class Boss extends Sprite{
	public Rectangle rect;
	Player ply;
	float stateTime = 0;
	int spriteAdjustmentY = -115;
	int spriteAdjustmentX = -20;
	int spriteLargura = 300;
	int spriteAltura = 300;
	public boolean alive = false;
	int count = 0;
	String animState = "tankFlying";
	String attackState = "negev";
	float walked = 0;
	float toWalkRight = -150;
	float toWalkLeft = 500;
	float vel = 200;
	float velX = vel;
	float vidaTank = 150;
	float vidaCap = 100;
	boolean vulnerable = true;
	int vulnerableCount = 0;
	public boolean tankCollision = false;
	public Platform tPlatform;
	int countTransition = 0;
	String deathState = "falling";
	String state = "Tank";
	RaioCap raio;
	int cooldown = 0;
	int maxCooldown = 60;
	Texture teste = new Texture("Player/vida.png");
	
	Sound tiroSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/tankTiro.wav"));
	Sound tiroExplosionSound = Gdx.audio.newSound(Gdx.files.internal("Player/Sounds/explosion.wav"));
	Sound damage = Gdx.audio.newSound(Gdx.files.internal("Cientista/Sounds/morrendo.wav"));
	public static Sound flyingSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/flying.wav"));
	Sound morrendoTankSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/tankMorrendo.wav"));
	
	Sound landSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/land.wav"));
	public static Sound negevSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/negev.wav"));
	Sound capDamage = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/capDano.wav"));
	Sound transformationSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/transformation.wav"));
	public static Sound tiroLaserSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/rajadaLaser.wav"));
	public static Sound raioSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/laserOlho.wav"));
	Sound jetpackSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/jetpack.wav"));
	Sound morrendoCapSound = Gdx.audio.newSound(Gdx.files.internal("Boss/Sounds/bossCaindo.wav"));
	
	Texture tankSheet = new Texture(Gdx.files.internal("Boss/tank.png"));
	TextureRegion[][] tank = TextureRegion.split(tankSheet, 300, 300);
	TextureRegion[] tankWalk = new TextureRegion[3];
	TextureRegion[] tankRachado = new TextureRegion[1];
	TextureRegion[] tankDeath = new TextureRegion[5];
	Texture tankPlatform = new Texture(Gdx.files.internal("Boss/deathPlatform.png"));
	
	Texture capSheet = new Texture(Gdx.files.internal("Boss/asadelta.png"));
	TextureRegion[][] cap = TextureRegion.split(capSheet, 120, 120);
	TextureRegion[] leavingTank = new TextureRegion[1];
	TextureRegion[] transformation = new TextureRegion[9];
	TextureRegion[] capShooting = new TextureRegion[3];
	TextureRegion[] capLaser = new TextureRegion[3];
	TextureRegion[] capFlying = new TextureRegion[3];
	TextureRegion[] eyeLaser = new TextureRegion[5];
	TextureRegion[] capFalling = new TextureRegion[4];
	TextureRegion[] capLanding = new TextureRegion[1];
	TextureRegion[] capDying = new TextureRegion[9];
	
	Animation<TextureRegion> tankIdle;
	Animation<TextureRegion> tankDying;
	
	Animation<TextureRegion> capLeaving;
	Animation<TextureRegion> capTransformationAnim;
	Animation<TextureRegion> capIdleAnim;
	Animation<TextureRegion> capFallingAnim;
	Animation<TextureRegion> capLandingAnim;
	Animation<TextureRegion> capDyingAnim;
	Animation<TextureRegion> negevAnim;
	Animation<TextureRegion> tiroLaserAnim;
	Animation<TextureRegion> eyeLaserAnim;
	
	TextureRegion currentFrame;
	
	public ArrayList<TiroCap> tirosCap = new ArrayList<TiroCap>();
	
	public ArrayList<TiroTank> tiros = new ArrayList<TiroTank>();
	TiroTank tiro;
	int countTiro = 0;
	int tiroCooldown = 60;
	int quantosTiro = 0;
	int velXTiro;
	int velX2;
	float velX3;
	float walkedX;
	float walkedX2;
	boolean loopingSound = false;
	float walkedX3;
	Random rand = new Random();
	MyGdxGame game;
	
	public Boss(Rectangle rect, Player p1, MyGdxGame game) {
		this.rect = rect;
		ply = p1;
		this.game = game;
		
		//TANK SPRITES
		for(int e=0; e < 3; e++) {
			tankWalk[e] = tank[0][e];
		}
		tankRachado[0] = tank[1][0];
		for(int e=0; e < 3; e++) {
			tankDeath[e] = tank[1][e + 1];
		}
		tankDeath[3] = tank[2][0];
		tankDeath[4] = tank[2][1];
		
		tankIdle = new Animation<TextureRegion>(0.09f, tankWalk);
		tankDying = new Animation<TextureRegion>(0.1f, tankDeath);
		
		//ASADELTA SPRITES
		leavingTank[0] = cap[0][0];
		capLanding[0] = cap[6][4];
		for(int i=0; i < 3; i++) {
			capShooting[i] = cap[2][i];
			capLaser[i] = cap[3][i];
			capFlying[i] = cap[4][i];
		}
		for(int i=0; i < 4; i++) {
			transformation[i] = cap[0][i + 1];
			capFalling[i] = cap[6][i];
			capDying[i + 5] = cap[8][i];
		}
		for(int i=0; i < 5; i++) {
			transformation[i + 4] = cap[1][i];
			eyeLaser[i] = cap[5][i];
			capDying[i] = cap[7][i];
		}
		
		capLeaving = new Animation<TextureRegion>(0.3f, leavingTank);
		capTransformationAnim = new Animation<TextureRegion>(0.08f, transformation);
		capIdleAnim = new Animation<TextureRegion>(0.3f, capFlying);
		capFallingAnim = new Animation<TextureRegion>(0.06f, capFalling);
		capLandingAnim = new Animation<TextureRegion>(0.3f, capLanding);
		capDyingAnim = new Animation<TextureRegion>(0.1f, capDying);
		negevAnim = new Animation<TextureRegion>(0.1f, capShooting);
		tiroLaserAnim = new Animation<TextureRegion>(0.1f, capLaser);
		eyeLaserAnim = new Animation<TextureRegion>(0.1f, eyeLaser);
		raio = new RaioCap(rect.x + 40, rect.y, 8, 330, this, ply);
		
	}
	
	public void update() {
		if(alive) {
			if(state == "Tank") {
				if(!loopingSound) {
					flyingSound.loop(0.07f);
					loopingSound = true;
				}
				if(vulnerable) {
					if(ply.tiro.rect.overlaps(rect) && ply.tiro.isAlive) {
						ply.tiro.tiroExplosionSound.play(0.5f);
						ply.tiro.count = 0;
						ply.tiro.toDie = true;
						ply.tiro.stateTime = 0;
						vidaTank -= 10;
						vulnerable = false;
						damage.play(1f);
						if(vidaTank <= 0) {
							flyingSound.stop();
							loopingSound = false;
							alive = false;
							state = "transition";
							stateTime = 0;
							morrendoTankSound.play(0.5f);
						}
					}
				}
				if(!vulnerable) {
					vulnerableCount += 1;
					if(vulnerableCount > 50) {
						vulnerable = true;
						vulnerableCount = 0;
					}
				}
								
				rect.x += velX * Gdx.graphics.getDeltaTime();
				walked += velX * Gdx.graphics.getDeltaTime();
				if(walked >= toWalkRight) {
					velX = -vel;
				}
				else if(walked <= -toWalkLeft) {
					velX = vel;
				}
				
				if(countTiro > 60) {
					if(quantosTiro == 0) {
						//pega um valor de 100 a 399
						velXTiro = rand.nextInt(400);
						velXTiro += 101;
						velX2 = rand.nextInt(400);
						velX2 += 101;
						walkedX = (250 * velXTiro) / 200;
						walkedX2 = (250 * velX2) / 200;
						walkedX3 = walkedX + (-walkedX2);
						velX3 = (walkedX3 * 200) / 250;
					}
					/*System.out.println("velX1: " + velXTiro);
					System.out.println("velX2: " + -velX2);
					System.out.println("velX3: " + velX3);
					System.out.println("walkedX: " + walkedX);
					System.out.println("walkedX2: " + -walkedX2);
					System.out.println("walkedX3: " + walkedX3);*/
					if(quantosTiro == 0) {
						tiro = new TiroTank(rect.x + 138, rect.y + 52, 20, 20, velXTiro, -400, -300);
					}
					else if (quantosTiro == 1) {
						tiro = new TiroTank(rect.x + 138, rect.y + 52, 20, 20, -velX2, -400, -300);
					}
					else if (quantosTiro == 2) {
						tiro = new TiroTank(rect.x + 138, rect.y + 52, 20, 20, velX3, -800, -300);
						tiro.tiroType = "2";
					}
					tiroSound.play(0.2f);
					tiros.add(tiro);
					quantosTiro += 1;
					if(quantosTiro >= 3) quantosTiro = 0;
                	countTiro = 0;
                	//tiroSound.play(0.4f);
				}
				countTiro += 1;
			}
			else if(state == "AsaDelta"){ //Logica da Asa Delta
				if(!loopingSound) {
					flyingSound.loop(0.07f);
					loopingSound = true;
				}
				if(vulnerable) {
					if(ply.tiro.rect.overlaps(rect) && ply.tiro.isAlive) {
						ply.tiro.tiroExplosionSound.play(0.3f);
						ply.tiro.count = 0;
						ply.tiro.toDie = true;
						ply.tiro.stateTime = 0;
						vidaCap -= 10;
						vulnerable = false;
						capDamage.play(1f);
						if(vidaCap <= 0) {
							flyingSound.stop();
							negevSound.stop();
							tiroLaserSound.stop();
							raioSound.stop();
							morrendoCapSound.play(0.1f);
							raio.isAlive = false;
							alive = false;
							animState = "capIdle";
							state = "death";
							countTransition = 0;
							stateTime = 0;
						}
					}
				}
				if(!vulnerable) {
					vulnerableCount += 1;
					if(vulnerableCount > 50) {
						vulnerable = true;
						vulnerableCount = 0;
					}
				}

				if(animState != "negev") rect.x += velX * Gdx.graphics.getDeltaTime();
				if(animState != "negev") walked += velX * Gdx.graphics.getDeltaTime();
				if(walked >= toWalkRight) {
					velX = -vel;
				}
				else if(walked <= -toWalkLeft) {
					velX = vel;
				}
				
				if(animState == "negev") {
					if(countTiro >= 2) {
						velXTiro = rand.nextInt(200);
						velX2 = rand.nextInt(200);
						tirosCap.add(new TiroCap(rect.x, rect.y, 10, 10, velXTiro, -200, "negev"));
						tirosCap.add(new TiroCap(rect.x, rect.y, 10, 10, -velX2, -200, "negev"));
						countTiro = 0;
					}
					countTiro += 1;
				}
				else if(animState == "tiroLaser") {
					if(countTiro >= 10) {
						tirosCap.add(new TiroCap(rect.x + 50, rect.y, 10, 10, 0, -400, "laser"));
						countTiro = 0;
					}
					countTiro += 1;
				}
				else if(animState == "eyeLaser") {
					if(!raio.isAlive) {
						raio.isAlive = true;
					}
					if(raio.isAlive) raio.update();

				}
					
				if(cooldown >= maxCooldown) {
					if(animState != "capIdle") {
						negevSound.stop();
						tiroLaserSound.stop();
						raioSound.stop();
						if(animState == "eyeLaser") raio.isAlive = false;
						maxCooldown = 100;
						animState = "capIdle";
					}
					else {
						if(attackState == "eyeLaser") {
							negevSound.loop(0.3f);
							countTiro = 0;
							maxCooldown = 150;
							animState = "negev";
							attackState = "negev";
						}
						else if (attackState == "negev") {
							tiroLaserSound.loop(0.3f);
							countTiro = 0;
							maxCooldown = 150;
							animState = "tiroLaser";
							attackState = "tiroLaser";
						}
						else if(attackState == "tiroLaser") {
							raioSound.loop(0.1f);
							animState = "eyeLaser";
							attackState = "eyeLaser";
							maxCooldown = 300;
						}
					}
					cooldown = 0;
				}
				cooldown += 1;
			}
			
		}
		else if(state == "transition"){
		    if(countTransition == 0) {
				animState = "tankRachado";
			}
		    else if(countTransition <= 60) {
				rect.y -= 270 * Gdx.graphics.getDeltaTime();
			}
		    else if(countTransition <= 80) {
				animState = "tankDying";
			}
			else if(countTransition <= 150){
				if(animState != "capLeaving") jetpackSound.play(0.4f);
				if(!tankCollision) {
					tankCollision = true;
					Color color = new Color(1, 0, 0, 1);
					tPlatform = new Platform(rect.x + 78, rect.y + 90, rect.width - 150, 20, 1, color);
					rect.x = rect.x + 88;
					rect.y = rect.y + 60;
				}
				spriteLargura = 120;
				spriteAltura = 120;
				spriteAdjustmentX = -25;
				spriteAdjustmentY = -34;
				rect.width = 70;
				rect.height = 63;
				toWalkRight = -50;
				toWalkLeft = 600;
				animState = "capLeaving";
				rect.y += 230 * Gdx.graphics.getDeltaTime();
			}
			else if(countTransition <= 210){
				if(animState != "capTransformation") {
					stateTime = 0;
					transformationSound.play(0.16f);
				}
				animState = "capTransformation";
			}
			else {
				if(animState != "capIdle") stateTime = 0;
				animState = "capIdle";
				vulnerable = true;
				state = "AsaDelta";
				alive = true;
			}
			countTransition += 1;
		}
		// DEATH ANIMATION
		else if(state == "death") {
			if(deathState == "falling") {
				animState = "capFalling";
				rect.y -= 270 * Gdx.graphics.getDeltaTime();
			}
			else if(deathState == "landing") {
				if(animState != "capDying") animState = "capLanding";
				if(countTransition > 10 && animState != "capDying") {
					raioSound.play(0.1f);
					animState = "capDying";
					stateTime = 0;
				}
				else if(countTransition > 200) {
					game.transition = true;
				}
				countTransition += 1;
			}
			if(rect.y < 50 && deathState == "falling") {
				landSound.play(0.3f);
				deathState = "landing";
				stateTime = 0;
			}
		}
		
		for(int t=0; t < tiros.size(); t++) {
			TiroTank tiroT = tiros.get(t);
			tiroT.update();
			if( (tiroT.walkedY < tiroT.toDrop) && !tiro.toDie) {
				//System.out.println(tiroT.walkedX);
				tiroT.toDie = true;
				tiroT.stateTime = 0;	
			}
			if (tiroT.countDie > 10) {
				tiroExplosionSound.play(0.5f);
				tiros.remove(t);
			}

			if(tiroT.rect.overlaps(ply.rect) && !tiro.toDie) {
				ply.damageSound.play(0.2f);
				Player.vida -= 10;
				tiroT.toDie = true;
				tiroT.stateTime = 0;
			}
		}
		for(int t=0; t < tirosCap.size(); t++) {
			TiroCap tiroT = tirosCap.get(t);
			tiroT.update();
			if(tiroT.rect.y < 30) {
				tirosCap.remove(t);
			}
			else if(tiroT.rect.overlaps(ply.rect)) {
				ply.damageSound.play(0.2f);
				Player.vida -= 10;
				tirosCap.remove(t);
			}
			else if(tiroT.rect.overlaps(tPlatform.rect)) {
				tirosCap.remove(t);
			}
		}
	}
	
	public void draw(SpriteBatch sb) {
		//sb.draw(teste, rect.x, rect.y, rect.width, rect.height);
		if(animState == "tankFlying") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = tankIdle.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
		}
		else if(animState == "tankRachado") {
			sb.draw(tankRachado[0], rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
		}
		else if(animState == "tankDying") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = tankDying.getKeyFrame(stateTime, false);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
		}
		else if(animState == "capLeaving") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = capLeaving.getKeyFrame(stateTime, false);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
		}
		else if(animState == "capTransformation") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = capTransformationAnim.getKeyFrame(stateTime, false);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		else if(animState == "capIdle") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = capIdleAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		else if(animState == "negev") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = negevAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		else if(animState == "tiroLaser") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = tiroLaserAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		else if(animState == "eyeLaser") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = eyeLaserAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		if(raio.isAlive) raio.draw(sb);
		for(int t=0; t < tiros.size(); t++) {
			TiroTank tiroT = tiros.get(t);
			tiroT.draw(sb);
		}
		for(int t=0; t < tirosCap.size(); t++) {
			TiroCap tiroT = tirosCap.get(t);
			tiroT.draw(sb);
		}
		if(tankCollision) {
			//sb.draw(teste, tPlatform.rect.x, tPlatform.rect.y, tPlatform.rect.width, tPlatform.rect.height);
			sb.draw(tankPlatform, tPlatform.rect.x - 40, tPlatform.rect.y - 125, 200, 200);
		}
		if(animState == "capFalling") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = capFallingAnim.getKeyFrame(stateTime, false);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		else if(animState == "capLanding") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = capLandingAnim.getKeyFrame(stateTime, false);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		else if(animState == "capDying") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = capDyingAnim.getKeyFrame(stateTime, false);
			sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			
		}
		
		
		
	}
	
	

}
