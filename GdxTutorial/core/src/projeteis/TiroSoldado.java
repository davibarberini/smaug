package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import platforms.Platform;

public class TiroSoldado {
	public Rectangle rect;
	public float velX, velY;
	public int wait = 0;
	public Player ply;
	public float spriteLargura = 20;
	public float spriteAltura = 20;
	public float pCorrectY;
	public float pCorrectX;
	public float fixedX, fixedY;
	public float stateTime = 0;
	public int count = 0;
	public Platform[] platforms;
	
	Animation<TextureRegion> projetilAnim;
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Cientista/projeteis.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 20, 20);
	TextureRegion[] projetil = new TextureRegion[1];

	
	public TiroSoldado(float x, float y, float w, float h, float velX, float velY, Player ply) {
		rect = new Rectangle(x, y, w, h);
		fixedX = x;
		fixedY = y;
		this.velX = velX;
		this.velY = velY;
		this.ply = ply;
		projetil[0] = spriteSheet[5][0];
		
		projetilAnim = new Animation<TextureRegion>(0.06f, projetil);
	}
	
	public void update(SpriteBatch sb) {
		rect.x += velX * Gdx.graphics.getDeltaTime();
		rect.y += velY * Gdx.graphics.getDeltaTime();
		this.draw(sb);
	}
	
	public void draw(SpriteBatch sb) {
		if(velX > 0) {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = projetilAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, this.rect.x  + pCorrectX, this.rect.y + pCorrectY + 5, spriteLargura, spriteAltura);
			
		}
		else{
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = projetilAnim.getKeyFrame(stateTime, true);
			sb.draw(currentFrame, rect.x + this.rect.width, rect.y + pCorrectY + 5, -spriteLargura, spriteAltura);
			
		}
	}
}
