package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import platforms.Platform;

public class TiroRicochete extends TiroNormal{
	
	public float moved = 0;
	public int bounceCount = 0;
	public float fixedVelX;
	public float vel = 0;
	public float spriteLargura = 40;
	public float spriteAltura = 40;
	public float pCorrectX = 12;
	public float pCorrectY = -10;
	
	public TiroRicochete(float x, float y, float w, float h, float velX, float velY, Player ply, Platform[] platforms) {
		super(x, y, w, h, velX, velY, ply, platforms);
		for(int e=0; e < 4; e++) {
			projetil[e] = spriteSheet[1][e + 1];
		}
		
		projetilAnim = new Animation<TextureRegion>(0.1f, projetil);
	}
	
	public void update(SpriteBatch sb) {
		if(isAlive) {
			rect.x += velX * Gdx.graphics.getDeltaTime();
			for(int e=0; e < platforms.length; e++) {
				platforms[e].platCollisionBulletX(this);
			}
			rect.y += velY * Gdx.graphics.getDeltaTime();
			for(int i=0; i < platforms.length; i++) {
				platforms[i].platCollisionBulletY(this);
			}
			moved += velY * Gdx.graphics.getDeltaTime();
			if(rect.overlaps(ply.rect)) {
				isAlive = false;
				ply.vida -= 10;
				count = 0;
			}
			if(count > wait) {
				isAlive = false;
				count = 0;
			}
		
			
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
