package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class TiroCap{
	
	public float vel = 0;
	public float spriteLargura = 20;
	public float spriteAltura = 20;
	public float pCorrectX = 12;
	public float pCorrectY = -10;
	public Rectangle rect;
	public float velX, velY;
	public boolean isAlive = false;
	public int count = 0;
	public String facing = "direita";
	public float fixedX, fixedY;
	public float stateTime = 0;
	public String tiroType = "1";
	Animation<TextureRegion> projetilAnim;
	
	Texture teste = new Texture(Gdx.files.internal("Player/vida.png"));
	
	TextureRegion currentFrame;
	Texture sprite = new Texture(Gdx.files.internal("Boss/projeteisCap.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 20, 20);
	TextureRegion[] projetil;
	
	
	public TiroCap(float x, float y, float w, float h, float velX, float velY, String whichTiro) {
		
		rect = new Rectangle(x, y, w, h);
		fixedX = x;
		fixedY = y;
		this.velX = velX;
		this.velY = velY;
		if(whichTiro == "negev") {
			projetil = new TextureRegion[4];
			for(int e=0; e < 4; e++) {
				projetil[e] = spriteSheet[0][e];
			}
		}
		else if(whichTiro == "laser") {
			projetil = new TextureRegion[1];
			projetil[0] = spriteSheet[1][0];
		}
		
		
		projetilAnim = new Animation<TextureRegion>(0.1f, projetil);
	}
	
	public void update() {
		rect.x += velX * Gdx.graphics.getDeltaTime();
		rect.y += velY * Gdx.graphics.getDeltaTime();
		//System.out.println(stateTime);
	}
	public void draw(SpriteBatch sb) {
		//sb.draw(teste, rect.x, rect.y, rect.width, rect.height);
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = projetilAnim.getKeyFrame(stateTime, true);
		sb.draw(currentFrame, rect.x + this.rect.width  + pCorrectX, rect.y + pCorrectY, -spriteLargura, spriteAltura);
	}	
}
