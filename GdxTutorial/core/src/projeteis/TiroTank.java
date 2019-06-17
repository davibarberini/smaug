package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import platforms.Platform;

public class TiroTank{
	
	public float vel = 0;
	public float spriteLargura = 40;
	public float spriteAltura = 40;
	public float pCorrectX = 12;
	public float pCorrectY = -10;
	public Rectangle rect;
	public float velX, velY;
	public boolean isAlive = false;
	public int count = 0;
	public String facing = "direita";
	public float fixedX, fixedY;
	public float stateTime = 0;
	public boolean toDie = false;
	public int countDie = 0;
	public float toDrop = 0;
	public float walkedY = 0;
	public float walkedX = 0;
	public String tiroType = "1";
	Animation<TextureRegion> projetilAnim;
	Animation<TextureRegion> explosionAnim;
	Animation<TextureRegion> projetil2Anim;
	Animation<TextureRegion> explosion2Anim;
	
	Texture teste = new Texture(Gdx.files.internal("Player/vida.png"));
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Boss/projeteisTank.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 20, 20);
	TextureRegion[] projetil = new TextureRegion[4];
	TextureRegion[] explosion = new TextureRegion[5];
	TextureRegion[] projetil2 = new TextureRegion[4];
	TextureRegion[] explosion2 = new TextureRegion[5];
	
	
	public TiroTank(float x, float y, float w, float h, float velX, float velY, float howMuchY) {
		rect = new Rectangle(x, y, w, h);
		fixedX = x;
		fixedY = y;
		toDrop = howMuchY;
		this.velX = velX;
		this.velY = velY;
		
		for(int e=0; e < 4; e++) {
			projetil[e] = spriteSheet[0][e];
		}
		for(int e=0; e < 5; e++) {
			explosion[e] = spriteSheet[1][e];
		}
		
		for(int e=0; e < 4; e++) {
			projetil2[e] = spriteSheet[2][e];
		}
		for(int e=0; e < 5; e++) {
			explosion2[e] = spriteSheet[3][e];
		}
		
		projetilAnim = new Animation<TextureRegion>(0.1f, projetil);
		explosionAnim = new Animation<TextureRegion>(0.04f, explosion);
		projetil2Anim = new Animation<TextureRegion>(0.1f, projetil2);
		explosion2Anim = new Animation<TextureRegion>(0.04f, explosion2);
	}
	
	public void update() {
		if(!toDie) {
			rect.x += velX * Gdx.graphics.getDeltaTime();
			walkedX += velX * Gdx.graphics.getDeltaTime();
		}
		if(!toDie) {
			rect.y += velY * Gdx.graphics.getDeltaTime();
			walkedY += velY * Gdx.graphics.getDeltaTime();
		}
			
		if(toDie) {
			countDie += 1;
		}
		if(rect.y < 0 && !toDie) {
			toDie = true;
			stateTime = 0;	
		}
		//System.out.println(stateTime);
	}
	public void draw(SpriteBatch sb) {
		//sb.draw(teste, rect.x, rect.y, rect.width, rect.height);
		if(tiroType == "1") {
			if(!toDie) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = projetilAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, rect.x + this.rect.width  + pCorrectX, rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = explosionAnim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, rect.x + this.rect.width  + pCorrectX, rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}
		} 
		else {
			if(!toDie) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = projetil2Anim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, rect.x + this.rect.width  + pCorrectX, rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = explosion2Anim.getKeyFrame(stateTime, false);
				sb.draw(currentFrame, rect.x + this.rect.width  + pCorrectX, rect.y + pCorrectY, -spriteLargura, spriteAltura);
			}
		}
		
		
	}	
}
