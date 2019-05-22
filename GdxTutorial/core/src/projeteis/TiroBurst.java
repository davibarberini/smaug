package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import platforms.Platform;

public class TiroBurst extends TiroNormal{
	
	public float spriteLargura = 30;
	public float spriteAltura = 30;
	public float pCorrectY = -10;
	public float pCorrectX = -10;
	TextureRegion[] projetil = new TextureRegion[3];
	
	public TiroBurst(float x, float y, float w, float h, float velX, float velY, Player ply, Platform[] platforms) {
		super(x, y, w, h, velX, velY, ply, platforms);
		
		for(int e=0; e < 3; e++) {
			projetil[e] = spriteSheet[2][e + 1];
		}
		
		projetilAnim = new Animation<TextureRegion>(0.06f, projetil);
	}
	
	public void update(SpriteBatch sb) {
		if(isAlive) {
			rect.x += velX * Gdx.graphics.getDeltaTime();
			rect.y += velY * Gdx.graphics.getDeltaTime();
			for(int e=0; e < platforms.length; e++) {
				if(platforms[e].isPlatform()) platforms[e].normalBulletCollision(this);
			}
			if(rect.overlaps(ply.rect)) {
				isAlive = false;
				ply.vida -= 10;
				count = 0;
			}
			if(count > wait) {
				isAlive = false;
				count = 0;
				stateTime = 0;
			}
			this.draw(sb);
		}
	}
	
	
	public void draw(SpriteBatch sb) {
		if(velX > 0) {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = projetilAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, this.rect.x  + pCorrectX, this.rect.y + pCorrectY, spriteLargura, spriteAltura);
			
		}
		else{
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = projetilAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + this.rect.width, rect.y + pCorrectY, -spriteLargura, spriteAltura);
			
		}
	}

	
	
	
}
