package entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;

import levels.EndScreen;
import levels.PauseScreen;
import platforms.Platform;
import projeteis.TiroPlayer;


public class Player extends Sprite {
	public Rectangle rect;
	public double gravity, velX, velY, aceX;
	public int jumpCount = 0;
	public int spriteLargura = 85;
	public int spriteAltura = 85;
	Texture vidaTXT = new Texture("Player/bateria.png");
	public int spriteAdjustmentY = -25;
	public int spriteAdjustmentX = -18;
	public boolean canTeleport = false;
	public String animState = "parado";
	public float stateTime;
	public int attackLimit = 40;
	public int widthLimit = 0;
	public int heightLimit = 0;
	public String facing = "direita";
	public String weapon = "espada";
	public boolean isColliding = false;
	public boolean isAttacking = false;
	public boolean isShooting = false;
	public int attackCount = 0;
	public int tiroCooldown = 0;
	public static float vida = 100;
	public static int score = 10000;
	public static int swordKills = 0;
	public static int cannonKills = 0;
	public static int attack1Kills = 0;
	public static int attack2Kills = 0;
	public static int attack3Kills = 0;
	public static int airAttackKills = 0;
	public float actualWidth;
	public int deathCount = 0;
	public boolean paused = false;
	public TiroPlayer tiro;
	
	Animation<TextureRegion> correndoAnim;
	Animation<TextureRegion> correndoCanhaoMAnim;
	Animation<TextureRegion> correndoCanhaoCAnim;
	Animation<TextureRegion> correndoCanhaoDAnim;
	
	Animation<TextureRegion> paradoAnim;
	Animation<TextureRegion> paradoCanhaoMAnim;
	Animation<TextureRegion> paradoCanhaoCAnim;
	Animation<TextureRegion> paradoCanhaoDAnim;
	
	Animation<TextureRegion> jumpingAnim;
	Animation<TextureRegion> jumpingCanhaoMAnim;
	Animation<TextureRegion> jumpingCanhaoCAnim;
	Animation<TextureRegion> jumpingCanhaoDAnim;
	
	Animation<TextureRegion> morrendoAnim;
	
	Animation<TextureRegion> attackingAnim;
	Animation<TextureRegion> attackingAnim2;
	Animation<TextureRegion> attackingAnim3;
	
	Animation<TextureRegion> atirandoMeioAnim;
	Animation<TextureRegion> atirandoCimaAnim;
	Animation<TextureRegion> atirandoDiagonalAnim;
	
	Animation<TextureRegion> airAttackAnim;
	
	TextureRegion currentFrame;
	
	Texture robo = new Texture(Gdx.files.internal("Player/robo.png"));
	public Texture life = new Texture(Gdx.files.internal("Player/vida.png"));
	
	TextureRegion[][] roboSheet = TextureRegion.split(robo, 80, 80);
	
	TextureRegion[] correndo = new TextureRegion[6];
	TextureRegion[] correndoCanhaoM = new TextureRegion[6];
	TextureRegion[] correndoCanhaoC = new TextureRegion[6];
	TextureRegion[] correndoCanhaoD = new TextureRegion[6];
	
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] paradoCanhaoM = new TextureRegion[1];
	TextureRegion[] paradoCanhaoC = new TextureRegion[1];
	TextureRegion[] paradoCanhaoD = new TextureRegion[1];
	
	TextureRegion[] jumping = new TextureRegion[4];
	TextureRegion[] jumpingCanhaoM = new TextureRegion[4];
	TextureRegion[] jumpingCanhaoC = new TextureRegion[4];
	TextureRegion[] jumpingCanhaoD = new TextureRegion[4];
	
	TextureRegion[] morrendo = new TextureRegion[6];
	
	TextureRegion[] attacking = new TextureRegion[3];
	TextureRegion[] attacking2 = new TextureRegion[5];
	TextureRegion[] attacking3 = new TextureRegion[4];
	
	TextureRegion[] atirandoMeio = new TextureRegion[1];
	TextureRegion[] atirandoCima = new TextureRegion[1];
	TextureRegion[] atirandoDiagonal = new TextureRegion[1];
	
	TextureRegion[] airAttack = new TextureRegion[4];

	public Player(float x, float y, float w, float h, double g, double vX, double vY, Platform[] platforms) {
		rect = new Rectangle(x, y, w, h);
		actualWidth = w;
		gravity = g;
		velX = vX;
		velY = vY;
		tiro = new TiroPlayer(x, y, 20, 20, 0, 0, platforms);
		
		for(int e=0; e < 6; e++) {
			correndo[e] = roboSheet[0][e];
			correndoCanhaoM[e] = roboSheet[7][e];
			correndoCanhaoD[e] = roboSheet[9][e];
			correndoCanhaoC[e] = roboSheet[11][e];
		}
		for(int i=0; i < 4; i++) {
			jumping[i] = roboSheet[1][i];
			jumpingCanhaoM[i] = roboSheet[8][i];
			jumpingCanhaoD[i] = roboSheet[10][i];
			jumpingCanhaoC[i] = roboSheet[12][i];
		}

		for(int j=0; j < 3; j++) {
			attacking[j] = roboSheet[2][j];
		}
		for(int j=0; j < 5; j++) {
			attacking2[j] = roboSheet[3][j];
		}
		for(int j=0; j < 4; j++) {
			attacking3[j] = roboSheet[4][j];
		}
		
		for(int a=0; a < 4; a++) {
			airAttack[a] = roboSheet[5][a];
		}
		for(int k=0; k < 6; k++) {
			morrendo[k] = roboSheet[6][k];
		}
		parado[0] = roboSheet[0][1];
		paradoCanhaoM[0] = roboSheet[7][1];
		paradoCanhaoD[0] = roboSheet[9][1];
		paradoCanhaoC[0] = roboSheet[11][1];
		atirandoMeio[0] = roboSheet[7][6];
		atirandoCima[0] = roboSheet[12][6];
		atirandoDiagonal[0] = roboSheet[10][6];
		
		correndoAnim = new Animation<TextureRegion>(0.09f, correndo);
		correndoCanhaoMAnim = new Animation<TextureRegion>(0.09f, correndoCanhaoM);
		correndoCanhaoDAnim = new Animation<TextureRegion>(0.09f, correndoCanhaoD);
		correndoCanhaoCAnim = new Animation<TextureRegion>(0.09f, correndoCanhaoC);
		
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		paradoCanhaoMAnim = new Animation<TextureRegion>(0.06f, paradoCanhaoM);
		paradoCanhaoDAnim = new Animation<TextureRegion>(0.06f, paradoCanhaoD);
		paradoCanhaoCAnim = new Animation<TextureRegion>(0.06f, paradoCanhaoC);
		
		jumpingAnim = new Animation<TextureRegion>(0.1f, jumping);
		jumpingCanhaoMAnim = new Animation<TextureRegion>(0.1f, jumpingCanhaoM);
		jumpingCanhaoDAnim = new Animation<TextureRegion>(0.1f, jumpingCanhaoD);
		jumpingCanhaoCAnim = new Animation<TextureRegion>(0.1f, jumpingCanhaoC);
		
		morrendoAnim = new Animation<TextureRegion>(0.06f, morrendo);
		
		attackingAnim = new Animation<TextureRegion>(0.06f, attacking);
		attackingAnim2 = new Animation<TextureRegion>(0.06f, attacking2);
		attackingAnim3 = new Animation<TextureRegion>(0.08f, attacking3);
		
		atirandoMeioAnim = new Animation<TextureRegion>(0.06f, atirandoMeio);
		atirandoDiagonalAnim = new Animation<TextureRegion>(0.06f, atirandoDiagonal);
		atirandoCimaAnim = new Animation<TextureRegion>(0.08f, atirandoCima);
		
		airAttackAnim = new Animation<TextureRegion>(0.05f, airAttack);
		
	}
 
	public void update(MyGdxGame game) {
		if(gravity < -100 && !isAttacking) {
			animState = "jumping";
			stateTime = 90f;
		}
		if(tiroCooldown > 10) isShooting = false;
		if(facing == "direita") tiro.fixedX = rect.x + 15;
		if(facing == "esquerda") tiro.fixedX = rect.x;
		tiro.fixedY = rect.y + 5;
		tiro.facing = facing;
		tiroCooldown += 1;
		if (vida <= 0) {
			if(deathCount == 0)stateTime = 0;
			animState = "morrendo";
			deathCount += 1;
			if(deathCount > 60) {
				deathCount = 0;
				MyGdxGame.endTime = System.currentTimeMillis();
				game.setScreen(new EndScreen(game));
			}
		}
		gravity += -2000 * Gdx.graphics.getDeltaTime();
		velX += aceX * Gdx.graphics.getDeltaTime();
		if(velX > 300) velX = 300;
		else if(velX < -300) velX = -300;
		if(isAttacking) {
			attackCount++;
			if(attackCount > attackLimit) {
				resetAttack();
			}
		}
	}
	
	
	public void draw(SpriteBatch sb) {
		tiro.update(sb);
		if(tiro.isAlive) tiro.count += 1;
		if(tiro.count > 50) {
			tiro.count = 0;
			tiro.isAlive = false;
			tiro.drawFlash = false;
		}
		if(animState == "parado") {
			if(weapon == "espada") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					currentFrame = paradoAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					currentFrame = paradoAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoM") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoMeioAnim.getKeyFrame(stateTime, false);
					else currentFrame = paradoCanhaoMAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoMeioAnim.getKeyFrame(stateTime, false);
					else currentFrame = paradoCanhaoMAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoC") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoCimaAnim.getKeyFrame(stateTime, false);
					else currentFrame = paradoCanhaoCAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoCimaAnim.getKeyFrame(stateTime, false);
					else currentFrame = paradoCanhaoCAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoD") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoDiagonalAnim.getKeyFrame(stateTime, false);
					else currentFrame = paradoCanhaoDAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoDiagonalAnim.getKeyFrame(stateTime, false);
					else currentFrame = paradoCanhaoDAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			
		}		
		else if(animState == "jumping") {
			if(weapon == "espada") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					currentFrame = jumpingAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					currentFrame = jumpingAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoM") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoMeioAnim.getKeyFrame(stateTime, false);
					else currentFrame = jumpingCanhaoMAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoMeioAnim.getKeyFrame(stateTime, false);
					else currentFrame = jumpingCanhaoMAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoC") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoCimaAnim.getKeyFrame(stateTime, false);
					else currentFrame = jumpingCanhaoCAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoCimaAnim.getKeyFrame(stateTime, false);
					else currentFrame = jumpingCanhaoCAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoD") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoDiagonalAnim.getKeyFrame(stateTime, false);
					else currentFrame = jumpingCanhaoDAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoDiagonalAnim.getKeyFrame(stateTime, false);
					else currentFrame = jumpingCanhaoDAnim.getKeyFrame(stateTime, false);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			
			
		}
		else if(animState == "running") {
			if(weapon == "espada") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					currentFrame = correndoAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else{
					stateTime += Gdx.graphics.getDeltaTime();
					currentFrame = correndoAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoM") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoMeioAnim.getKeyFrame(stateTime, false);
					else currentFrame = correndoCanhaoMAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else{
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoMeioAnim.getKeyFrame(stateTime, false);
					else currentFrame = correndoCanhaoMAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoC") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoCimaAnim.getKeyFrame(stateTime, false);
					else currentFrame = correndoCanhaoCAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else{
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoCimaAnim.getKeyFrame(stateTime, false);
					else currentFrame = correndoCanhaoCAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
			else if(weapon == "canhaoD") {
				if(facing == "direita") {
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoDiagonalAnim.getKeyFrame(stateTime, false);
					else currentFrame = correndoCanhaoDAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
				}
				else{
					stateTime += Gdx.graphics.getDeltaTime();
					if(isShooting) currentFrame = atirandoDiagonalAnim.getKeyFrame(stateTime, false);
					else currentFrame = correndoCanhaoDAnim.getKeyFrame(stateTime, true);
					sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
				}
			}
		}
		else if(animState == "attacking") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
			}
		}
		else if(animState == "attacking2") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim2.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim2.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
			}
		}
		else if(animState == "attacking3") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim3.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim3.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
			}
		}
		else if(animState == "morrendo") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = morrendoAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = morrendoAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
			}
		}
		else if(animState == "airAttack") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = airAttackAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = airAttackAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
			}
		}
		else {
			stateTime = 0;
		}
	}
	
	public void keyDown(int keyCode) {
		if((keyCode == Input.Keys.W || keyCode == Input.Keys.SPACE || keyCode == Input.Keys.UP) && jumpCount < 2) {
			//System.out.println("here");
			if(paused) {
				if(PauseScreen.selected == "return") PauseScreen.selected = "exit";
            	else if (PauseScreen.selected == "mainmenu") PauseScreen.selected = "return";
            	else if (PauseScreen.selected == "exit") PauseScreen.selected = "mainmenu";
			}else {
				stateTime = 0;
	            this.gravity = 600;
	            animState = "jumping";
	            jumpCount += 1;
	            resetAttack();
			}
        }
		else if((keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) && paused) {
			if(PauseScreen.selected == "return") PauseScreen.selected = "mainmenu";
        	else if (PauseScreen.selected == "mainmenu") PauseScreen.selected = "exit";
        	else if (PauseScreen.selected == "exit") PauseScreen.selected = "return";
		}
        else if(keyCode == Input.Keys.D || keyCode == Input.Keys.RIGHT) {
        	if(this.velX < 0) velX = 0;
        	this.aceX = 1000;
        	if(animState == "parado") animState = "running";
        	facing = "direita";
        }
        else if(keyCode == Input.Keys.A || keyCode == Input.Keys.LEFT) {
        	if(this.velX > 0) velX = 0;
        	this.aceX = -1000;
        	if(animState == "parado") animState = "running";
        	facing = "esquerda";
        }
        else if(keyCode == Input.Keys.F) {
        	if(weapon == "espada") {
        		isAttacking = true;
            	if(animState != "jumping" && animState != "airAttack") {
            		if(animState == "attacking") {
                		attackCount = 0;
                		stateTime = 0;
                		attackLimit = 25;
                		widthLimit = 15;
                		animState = "attacking2";
                	}
                	else if(animState == "attacking2") {
                		attackCount = 0;
                		attackLimit = 70;
                		stateTime = 0;
                		widthLimit = 10;
                		animState = "attacking3";
                	}
                	else{
                		attackCount = 0;
                		stateTime = 0;
                		attackLimit = 25;
                		widthLimit = 25;
                		animState = "attacking";
                	}
            	}
            	else {
            		if(animState != "airAttack") {
            			attackCount = 0;
                		attackLimit = 70;
                		widthLimit = 25;
                		heightLimit = 24;
                		stateTime = 0;
                		animState = "airAttack";
            		}
            	}
        	}
        	else weapon = "espada";
        	
        	
        }
        else if(keyCode == Input.Keys.I) {
        	if(weapon == "canhaoC") {
        		isShooting = true;
        		if(!tiro.isAlive && tiroCooldown > 60) {
            		tiro.rect.x = rect.x;
                	tiro.rect.y = rect.y + 10;
                	tiro.velX = 0;
                	tiro.velY = 400;
                	tiro.isAlive = true;
                	tiroCooldown = 0;
                	tiro.drawFlash = true;
            	}
        	}
        	else weapon = "canhaoC";
        	
        }
        else if(keyCode == Input.Keys.J) {
        	if(weapon == "canhaoD") {
        		isShooting = true;
        		if(!tiro.isAlive && tiroCooldown > 60) {
            		tiro.rect.x = rect.x;
                	tiro.rect.y = rect.y + 10;
                	if(facing == "direita") tiro.velX = 400;
                	if(facing == "esquerda") tiro.velX = -400;
                	tiro.velY = 300;
                	tiro.isAlive = true;
                	tiroCooldown = 0;
                	tiro.drawFlash = true;
            	}
        	}
        	else weapon = "canhaoD";
        	
        }
        else if(keyCode == Input.Keys.N) {
        	if(weapon == "canhaoM") {
        		isShooting = true;
        		if(!tiro.isAlive && tiroCooldown > 60) {
            		tiro.rect.x = rect.x;
                	tiro.rect.y = rect.y;
                	if(facing == "direita") tiro.velX = 400;
                	if(facing == "esquerda") tiro.velX = -400;
                	tiro.velY = 0;
                	tiro.isAlive = true;
                	tiroCooldown = 0;
                	tiro.drawFlash = true;
            	}
        	}
        	else weapon = "canhaoM";
        	
        }
        else if(keyCode == Input.Keys.M) {
        	vida = 0;
        }
	}
	
	public void keyUp(int keyCode) {
	  if(keyCode == Input.Keys.D && this.aceX > 0) {
        	this.aceX = 0;
        	this.velX = 0;
        	if(animState == "running") animState = "parado";
      }
	  else if(keyCode == Input.Keys.A && this.aceX < 0) {
        	this.aceX = 0;
        	this.velX = 0;
        	if(animState == "running") animState = "parado";
      }
	}
	
	public void resetAttack() {
		isAttacking = false;
		widthLimit = 0;
		heightLimit = 0;
		attackCount = 0;
	}
	
	public void drawVida(SpriteBatch sb, float x, float y) {
		float qntVida = (vida / 10);
		for(int v=0; v <qntVida; v++) {
			sb.draw(vidaTXT, x + (v * 23), y, vidaTXT.getWidth(), vidaTXT.getHeight());
		}
	}
}