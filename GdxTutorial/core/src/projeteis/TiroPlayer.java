package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import platforms.Platform;

public class TiroPlayer{
	
	public float vel = 0;
	public float spriteLargura = 40;
	public float spriteAltura = 40;
	public float pCorrectX = 12;
	public float pCorrectY = -10;
	public Rectangle rect;
	public float velX, velY;
	public boolean isAlive = false, drawFlash = false;
	public int count = 0;
	public String facing = "direita";
	public float fixedX, fixedY;
	public float stateTime = 0;
	public Platform[] platforms;
	
	Animation<TextureRegion> projetilAnim;
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Player/tiro.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 30, 30);
	TextureRegion[] projetil = new TextureRegion[4];
	
	public TiroPlayer(float x, float y, float w, float h, float velX, float velY, Platform[] platforms) {
		rect = new Rectangle(x, y, w, h);
		fixedX = x;
		fixedY = y;
		this.velX = velX;
		this.velY = velY;
		this.platforms = platforms;
		
		for(int e=0; e < 4; e++) {
			projetil[e] = spriteSheet[0][e];
		}
		
		projetilAnim = new Animation<TextureRegion>(0.1f, projetil);
	}
	
	public void update(SpriteBatch sb) {
		if(isAlive) {
			rect.x += velX * Gdx.graphics.getDeltaTime();
			for(int e=0; e < platforms.length; e++) {
				if(platforms[e].isPlatform() || platforms[e].isEscudo()) platforms[e].playerBulletCollision(this);
			}
			rect.y += velY * Gdx.graphics.getDeltaTime();
			
			this.draw(sb);
		}
	}
	public void draw(SpriteBatch sb) {
		if(velX > 0) {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = projetilAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, this.rect.x, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			
		}
		else{
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = projetilAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + this.rect.width  + pCorrectX, rect.y + pCorrectY, -spriteLargura, spriteAltura);
			
		}
	}	
}
