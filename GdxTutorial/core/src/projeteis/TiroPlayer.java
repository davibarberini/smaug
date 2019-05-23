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
	
	Animation<TextureRegion> flashAnim;
	Animation<TextureRegion> projetilAnim;
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Cientista/projeteis.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 20, 20);
	TextureRegion[] flash = new TextureRegion[1];
	TextureRegion[] projetil = new TextureRegion[4];
	
	public TiroPlayer(float x, float y, float w, float h, float velX, float velY, Platform[] platforms) {
		rect = new Rectangle(x, y, w, h);
		fixedX = x;
		fixedY = y;
		this.velX = velX;
		this.velY = velY;
		this.platforms = platforms;
		
		flash[0] = spriteSheet[1][0];
		for(int e=0; e < 4; e++) {
			projetil[e] = spriteSheet[1][e + 1];
		}
		
		flashAnim = new Animation<TextureRegion>(0.06f, flash);
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
		if(facing == "direita" && drawFlash) {
			currentFrame = flashAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, fixedX, fixedY + pCorrectY, spriteLargura, spriteAltura);
		}
		else if(drawFlash){
			currentFrame = flashAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, fixedX + this.rect.width, fixedY + pCorrectY, -spriteLargura, spriteAltura);
		}
	}	
}
