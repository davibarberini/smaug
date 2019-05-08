package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;


public class Cientista extends Sprite {
	public Rectangle rect;
	public int numColunas = 5;
	public int numLinhas = 2;
	public int spriteLargura = 35;
	public int spriteAltura = 40;
	public String animState = "parado";
	public float stateTime;
	public boolean isAlive = true;
	public Player ply;
	
	Animation<TextureRegion> paradoAnim;
	
	TextureRegion currentFrame;
	Texture idle = new Texture(Gdx.files.internal("Cientista/idle.png"));
	
	//TextureRegion[][] correndoTmp = TextureRegion.split(ninja, ninja.getWidth() / numColunas, ninja.getHeight()/ numLinhas);
	//TextureRegion[] parado = new TextureRegion[1];

	public Cientista(float x, float y, float w, float h, Player ply) {
		rect = new Rectangle(x, y, w, h);
		this.ply = ply;
		//parado[0] = correndoTmp[0][1];
		
		//paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		
	}
 
	public void update(SpriteBatch sb) {
		if(ply.isAttacking) {
			if(rect.overlaps(ply.rect)) {
				isAlive = false;
			}
		}
		if(isAlive) {
			this.draw(sb);
		}
		
		
		
	}
	public void draw(SpriteBatch sb) {
		if(animState == "parado") {
			sb.draw(idle, rect.x, rect.y);
		}
		else {
			stateTime = 0;
		}
	}
}