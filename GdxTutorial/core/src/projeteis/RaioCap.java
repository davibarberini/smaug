package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Boss;
import entities.Player;

public class RaioCap{
	
	public float spriteLargura = 10;
	public float spriteAltura = -330;
	public float pCorrectX = 0;
	public float pCorrectY = -250;
	float lessY = -210;
	public Rectangle rect;
	public boolean isAlive = false;
	public float fixedX, fixedY;
	public float stateTime = 0;
	int dmgCooldown = 10;
	Boss boss;
	Player p1;
	
	Animation<TextureRegion> projetilAnim;
	
	Texture teste = new Texture(Gdx.files.internal("Player/vida.png"));
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Boss/eyeLaser.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 10, 100);
	TextureRegion[] projetil = new TextureRegion[6];
	
	
	public RaioCap(float x, float y, float w, float h, Boss boss, Player p1) {
		
		rect = new Rectangle(x, y, w, h);
		fixedX = x;
		fixedY = y;
		
		for(int e=0; e < 6; e++) {
			projetil[e] = spriteSheet[0][e];
		}
		this.boss = boss;
		this.p1 = p1;
		
		projetilAnim = new Animation<TextureRegion>(0.1f, projetil);
	}
	
	public void update() {
		spriteLargura = 8;
		rect.x = boss.rect.x + 33;
		rect.y = boss.rect.y + lessY;
		if(rect.overlaps(boss.tPlatform.rect)) {
			lessY = -230;
			pCorrectY = 270;
			spriteAltura = -270;
			rect.height = 270;
		}
		else {
			lessY = -325;
			pCorrectY = 365;
			spriteAltura = -365;
			rect.height = 365;
		}
		if(rect.overlaps(p1.rect) && dmgCooldown > 20) {
			p1.damageSound.play(0.2f);
			Player.vida -= 10;
			dmgCooldown = 0;
		}
		dmgCooldown += 1;
	}
	public void draw(SpriteBatch sb) {
		//sb.draw(teste, rect.x, rect.y, rect.width, rect.height);
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = projetilAnim.getKeyFrame(stateTime, true);
		sb.draw(currentFrame, rect.x + this.rect.width  + pCorrectX, rect.y + pCorrectY, -spriteLargura, spriteAltura);
	}	
}
