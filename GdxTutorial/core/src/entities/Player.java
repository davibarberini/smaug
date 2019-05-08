package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Player extends Sprite {
	public Rectangle rect;
	public double gravity, velX, velY, aceX;
	public int numColunas = 5;
	public int numLinhas = 2;
	public int spriteLargura = 35;
	public int spriteAltura = 40;
	public String animState = "parado";
	public float stateTime;
	public String facing = "direita";
	public boolean isColliding = false;
	public boolean isAttacking = false;
	public int attackCount = 0;
	
	Animation<TextureRegion> correndoAnim;
	Animation<TextureRegion> paradoAnim;
	Animation<TextureRegion> jumpingAnim;
	Animation<TextureRegion> attackingAnim;
	
	TextureRegion currentFrame;
	Texture ninja = new Texture(Gdx.files.internal("Player/ninja.png"));
	
	TextureRegion[][] correndoTmp = TextureRegion.split(ninja, ninja.getWidth() / numColunas, ninja.getHeight()/ numLinhas);
	TextureRegion[] correndo = new TextureRegion[numColunas * numLinhas];
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] jumping = new TextureRegion[1];
	TextureRegion[] attacking = new TextureRegion[2];

	public Player(float x, float y, float w, float h, double g, double vX, double vY) {
		rect = new Rectangle(x, y, w, h);
		gravity = g;
		velX = vX;
		velY = vY;
		int count = 0;
		for(int i=0; i < numLinhas; i++) {
			for(int e=0; e < numColunas; e++) {
				correndo[count ++] = correndoTmp[i][e];

			}
		}
		//System.out.println(correndo.length + "--" + correndoTmp.length);

		parado[0] = correndoTmp[0][1];
		jumping[0] = correndoTmp[0][0];
		attacking[0] = correndoTmp[1][2];
		attacking[1] = correndoTmp[0][0];
		
		correndoAnim = new Animation<TextureRegion>(0.06f, correndo);
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		jumpingAnim = new Animation<TextureRegion>(0.06f, jumping);
		attackingAnim = new Animation<TextureRegion>(0.5f, attacking);
		
	}
 
	public void update() {
		gravity += -1000 * Gdx.graphics.getDeltaTime();
		velX += aceX * Gdx.graphics.getDeltaTime();
		if(velX > 500) velX = 500;
		else if(velX < -500) velX = -500;
		if(gravity < 50) {
			if(velX == 0) {
				animState = "parado";
			}
			else {
				animState = "running";
			}
		}
		if(isAttacking) {
			animState = "attacking";
			attackCount++;
			if(attackCount > 50) {
				isAttacking = false;
				attackCount = 0;
			}
		}
		
	}
	
	
	public void draw(SpriteBatch sb) {
		if(animState == "parado") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = paradoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y, -spriteLargura, spriteAltura);
			}
			
		}		
		else if(animState == "jumping") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = jumpingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = jumpingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y, -spriteLargura, spriteAltura);
			}
			
		}
		else if(animState == "running") {
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
		else if(animState == "attacking") {
			if(facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = attackingAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y, -spriteLargura, spriteAltura);
			}
		}
		else {
			stateTime = 0;
		}
	}
	
	public void keyDown(int keyCode) {
		if(keyCode == Input.Keys.W) {
            this.gravity = 500;
            animState = "jumping";
        }
        else if(keyCode == Input.Keys.D) {
        	if(this.velX < 0) velX = 0;
        	this.aceX = 2000;
        	if(animState != "jumping") animState = "running";
        	facing = "direita";
        }
        else if(keyCode == Input.Keys.A) {
        	if(this.velX > 0) velX = 0;
        	this.aceX = -2000;
        	if(animState != "jumping") animState = "running";
        	facing = "esquerda";
        }
        else if(keyCode == Input.Keys.F) {
        	this.stateTime = 0;
        	this.isAttacking = true;
        }
	}
	
	public void keyUp(int keyCode) {
	  if(keyCode == Input.Keys.D && this.aceX > 0) {
        	this.aceX = 0;
        	this.velX = 0;
        	if(animState != "jumping") animState = "parado";
      }
	  else if(keyCode == Input.Keys.A && this.aceX < 0) {
        	this.aceX = 0;
        	this.velX = 0;
        	if(animState != "jumping") animState = "parado";
      }
	}
}