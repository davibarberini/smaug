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
	public String facing = "direita";
	public boolean isColliding = false;
	public boolean isAttacking = false;
	public int attackCount = 0;
	public static int vida = 100;
	public float actualWidth;
	public int deathCount = 0;
	
	Animation<TextureRegion> correndoAnim;
	Animation<TextureRegion> paradoAnim;
	Animation<TextureRegion> jumpingAnim;
	Animation<TextureRegion> morrendoAnim;
	Animation<TextureRegion> attackingAnim;
	Animation<TextureRegion> attackingAnim2;
	Animation<TextureRegion> attackingAnim3;
	
	TextureRegion currentFrame;
	Texture robo = new Texture(Gdx.files.internal("Player/robo.png"));
	Texture life = new Texture(Gdx.files.internal("Player/vida.png"));
	TextureRegion[][] roboSheet = TextureRegion.split(robo, 80, 80);
	TextureRegion[] correndo = new TextureRegion[6];
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] jumping = new TextureRegion[4];
	TextureRegion[] morrendo = new TextureRegion[3];
	TextureRegion[] attacking = new TextureRegion[3];
	TextureRegion[] attacking2 = new TextureRegion[5];
	TextureRegion[] attacking3 = new TextureRegion[4];

	public Player(float x, float y, float w, float h, double g, double vX, double vY) {
		rect = new Rectangle(x, y, w, h);
		actualWidth = w;
		gravity = g;
		velX = vX;
		velY = vY;
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
		parado[0] = roboSheet[0][1];
		
		correndoAnim = new Animation<TextureRegion>(0.09f, correndo);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		jumpingAnim = new Animation<TextureRegion>(0.1f, jumping);
		morrendoAnim = new Animation<TextureRegion>(0.06f, morrendo);
		attackingAnim = new Animation<TextureRegion>(0.1f, attacking);
		attackingAnim2 = new Animation<TextureRegion>(0.1f, attacking2);
		attackingAnim3 = new Animation<TextureRegion>(0.1f, attacking3);
		
	}
 
	public void update(MyGdxGame game) {
		if (vida <= 0) {
			animState = "morrendo";
			deathCount += 1;
			if(deathCount > 60) {
				deathCount = 0;
				game.setScreen(new EndScreen(game));
			}
		}
		gravity += -2000 * Gdx.graphics.getDeltaTime();
		velX += aceX * Gdx.graphics.getDeltaTime();
		if(velX > 300) velX = 300;
		else if(velX < -300) velX = -300;
		if(isAttacking) {
			attackCount++;
			if(attackCount > 40) {
				isAttacking = false;
				attackCount = 0;
			}
		}
	}
	
	
	public void draw(SpriteBatch sb) {
		sb.draw(life, rect.x - 300, rect.y + 200, vida, 30);
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
				currentFrame = attackingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width - spriteAdjustmentX, this.rect.y + spriteAdjustmentY, -spriteLargura, spriteAltura);
			}
		}
		else if(animState == "attacking2") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim2.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + spriteAdjustmentX, this.rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim2.getKeyFrame(stateTime, true);
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
        	this.isAttacking = true;
        	if(animState == "attacking") {
        		attackCount = 0;
        		stateTime = 0;
        		animState = "attacking2";
        	}
        	else if(animState == "attacking2") {
        		attackCount = 0;
        		stateTime = 0;
        		animState = "attacking3";
        	}
        	else{
        		attackCount = 0;
        		stateTime = 0;
        		animState = "attacking";
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
}