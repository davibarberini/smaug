package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import platforms.Platform;

public class TiroAtravessa {
	public Rectangle rect;
	public float velX, velY;
	public boolean isAlive = false;
	public int wait = 0;
	public Player ply;
	public float spriteLargura = 30;
	public float spriteAltura = 30;
	public float pCorrectY;
	public float pCorrectX;
	public int count = 0;
	public float fixedX, fixedY;
	public float stateTime = 0;
	public Platform[] platforms;
	
	Animation<TextureRegion> projetilAnim;
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Cientista/projeteis.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 20, 20);
	TextureRegion[] projetil = new TextureRegion[3];

	
	public TiroAtravessa(float x, float y, float w, float h, float velX, float velY, Player ply) {
		rect = new Rectangle(x, y, w, h);
		fixedX = x;
		fixedY = y;
		this.velX = velX;
		this.velY = velY;
		this.ply = ply;
		for(int e=0; e < 3; e++) {
			projetil[e] = spriteSheet[3][e];
		}
		
		projetilAnim = new Animation<TextureRegion>(0.06f, projetil);
	}
	
	public void update(SpriteBatch sb) {
		if(isAlive) {
			rect.x += velX * Gdx.graphics.getDeltaTime();
			rect.y += velY * Gdx.graphics.getDeltaTime();
			if(rect.overlaps(ply.rect)) {
				ply.damageSound.play(0.2f);
				isAlive = false;
				Player.vida -= 10;
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
