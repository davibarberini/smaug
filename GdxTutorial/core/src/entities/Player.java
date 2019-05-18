package entities;

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
import platforms.Platform;
import projeteis.TiroPlayer;


public class Player extends Sprite {
	public Rectangle rect;
	public double gravity, velX, velY, aceX;
	public int jumpCount = 0;
	public int spriteLargura = 85;
	public int spriteAltura = 85;
	public int spriteAdjustmentY = -25;
	public int spriteAdjustmentX = -18;
	public String animState = "parado";
	public float stateTime;
	public int attackLimit = 40;
	public int widthLimit = 0;
	public int heightLimit = 0;
	public String facing = "direita";
	public boolean isColliding = false;
	public boolean isAttacking = false;
	public int attackCount = 0;
	public int tiroCooldown = 0;
	public static int vida = 100;
	public float actualWidth;
	public int deathCount = 0;
	public TiroPlayer tiro;
	
	Animation<TextureRegion> correndoAnim;
	Animation<TextureRegion> paradoAnim;
	Animation<TextureRegion> jumpingAnim;
	Animation<TextureRegion> morrendoAnim;
	Animation<TextureRegion> attackingAnim;
	Animation<TextureRegion> attackingAnim2;
	Animation<TextureRegion> attackingAnim3;
	Animation<TextureRegion> airAttackAnim;
	
	TextureRegion currentFrame;
	Texture robo = new Texture(Gdx.files.internal("Player/robo.png"));
	public Texture life = new Texture(Gdx.files.internal("Player/vida.png"));
	TextureRegion[][] roboSheet = TextureRegion.split(robo, 80, 80);
	TextureRegion[] correndo = new TextureRegion[6];
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] jumping = new TextureRegion[4];
	TextureRegion[] morrendo = new TextureRegion[3];
	TextureRegion[] attacking = new TextureRegion[3];
	TextureRegion[] attacking2 = new TextureRegion[5];
	TextureRegion[] attacking3 = new TextureRegion[4];
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

		}
		for(int i=0; i < 4; i++) {
			jumping[i] = roboSheet[1][i];
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
		
		for(int k=0; k < 3; k++) {
			morrendo[k] = roboSheet[1][k + 2];
		}
		for(int a=0; a < 4; a++) {
			airAttack[a] = roboSheet[5][a];
		}
		parado[0] = roboSheet[0][1];
		
		correndoAnim = new Animation<TextureRegion>(0.09f, correndo);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		jumpingAnim = new Animation<TextureRegion>(0.1f, jumping);
		morrendoAnim = new Animation<TextureRegion>(0.06f, morrendo);
		attackingAnim = new Animation<TextureRegion>(0.06f, attacking);
		attackingAnim2 = new Animation<TextureRegion>(0.06f, attacking2);
		attackingAnim3 = new Animation<TextureRegion>(0.08f, attacking3);
		airAttackAnim = new Animation<TextureRegion>(0.05f, airAttack);
		
	}
 
	public void update(MyGdxGame game) {
		if(gravity < -100 && !isAttacking) {
			animState = "jumping";
			stateTime = 90f;
		}
		if(facing == "direita") tiro.fixedX = rect.x + 15;
		if(facing == "esquerda") tiro.fixedX = rect.x;
		tiro.fixedY = rect.y + 5;
		tiro.facing = facing;
		tiroCooldown += 1;
		if (vida <= 0) {
			animState = "morrendo";
			deathCount += 1;
			if(deathCount > 60) {
				deathCount = 0;
				vida = 100;
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
		else if(animState == "jumping") {
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
		else if(animState == "running") {
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
				currentFrame = morrendoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = morrendoAnim.getKeyFrame(stateTime, true);
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
		if(keyCode == Input.Keys.W && jumpCount < 2) {
			//System.out.println("here");
			stateTime = 0;
            this.gravity = 600;
            animState = "jumping";
            jumpCount += 1;
            resetAttack();
        }
        else if(keyCode == Input.Keys.D) {
        	if(this.velX < 0) velX = 0;
        	this.aceX = 1000;
        	if(animState == "parado") animState = "running";
        	facing = "direita";
        }
        else if(keyCode == Input.Keys.A) {
        	if(this.velX > 0) velX = 0;
        	this.aceX = -1000;
        	if(animState == "parado") animState = "running";
        	facing = "esquerda";
        }
        else if(keyCode == Input.Keys.F) {
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
        else if(keyCode == Input.Keys.I) {
        	if(!tiro.isAlive && tiroCooldown > 60) {
        		tiro.rect.x = rect.x;
            	tiro.rect.y = rect.y + 10;
            	if(facing == "direita") tiro.velX = 400;
            	if(facing == "esquerda") tiro.velX = -400;
            	tiro.velY = 200;
            	tiro.isAlive = true;
            	tiroCooldown = 0;
            	tiro.drawFlash = true;
        	}
        }
        else if(keyCode == Input.Keys.J) {
        	if(!tiro.isAlive && tiroCooldown > 60) {
        		tiro.rect.x = rect.x;
            	tiro.rect.y = rect.y + 10;
            	if(facing == "direita") tiro.velX = 400;
            	if(facing == "esquerda") tiro.velX = -400;
            	tiro.velY = 0;
            	tiro.isAlive = true;
            	tiroCooldown = 0;
            	tiro.drawFlash = true;
        	}
        }
        else if(keyCode == Input.Keys.N) {
        	if(!tiro.isAlive && tiroCooldown > 60) {
        		tiro.rect.x = rect.x;
            	tiro.rect.y = rect.y + 10;
            	if(facing == "direita") tiro.velX = 400;
            	if(facing == "esquerda") tiro.velX = -400;
            	tiro.velY = -200;
            	tiro.isAlive = true;
            	tiroCooldown = 0;
            	tiro.drawFlash = true;
        	}
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
}