package entities.cientistas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;
import projeteis.Escudo;


public class Cientista extends Sprite {
	public Rectangle rect;
	public float velX, vel;
	public int numColunas = 8;
	public int numLinhas = 1;
	public float spriteLargura, spriteAltura;
	public String animState = "running";
	public float stateTime;
	public int toWalkLeft, toWalkRight;
	public float walked = 0;
	public boolean isAlive = true;
	public Player ply;
	
	Animation<TextureRegion> paradoAnim;
	Animation<TextureRegion> correndoAnim;
	
	TextureRegion currentFrame;
	Texture correndoTxt = new Texture(Gdx.files.internal("Cientista/running.png"));
	
	TextureRegion[][] correndoTmp = TextureRegion.split(correndoTxt, correndoTxt.getWidth() / numColunas, correndoTxt.getHeight()/ numLinhas);
	TextureRegion[] parado = new TextureRegion[1];
	TextureRegion[] correndo = new TextureRegion[numColunas * numLinhas];

	public Cientista(float x, float y, float w, float h, float vel, int pixelsToWalkRight, int pixelsToWalkLeft, Player ply) {
		rect = new Rectangle(x, y, w, h);
		this.ply = ply;
		this.vel = vel;
		velX = vel;
		toWalkRight = pixelsToWalkRight;
		toWalkLeft = pixelsToWalkLeft;
		spriteLargura = w;
		spriteAltura = h;
		
		int count = 0;
		parado[0] = correndoTmp[0][0];
		for(int e=0; e < numLinhas; e++) {
			for(int i=0; i < numColunas; i++ ) {
				correndo[count++] = correndoTmp[e][i];
			}
		}
		
		paradoAnim = new Animation<TextureRegion>(0.06f, parado);
		correndoAnim = new Animation<TextureRegion>(0.06f, correndo);
		
	}
 
	public void update(SpriteBatch sb) {
		if(ply.isAttacking) {
			if(rect.overlaps(ply.rect)) {
				isAlive = false;
			}
		}
		if(isAlive) {
			this.draw(sb);
			rect.x += velX * Gdx.graphics.getDeltaTime();
			walked += velX * Gdx.graphics.getDeltaTime();
			if(walked >= toWalkRight) {
				velX = -vel;
			}
			else if(walked <= -toWalkLeft) {
				velX = vel;
			}
		}
		
		
		
	}
	public void draw(SpriteBatch sb) {
		if(animState == "running") {
			if(velX > 0) {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x, this.rect.y, spriteLargura, spriteAltura);
			}
			else{
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = correndoAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, this.rect.x + this.rect.width, this.rect.y, -spriteLargura, spriteAltura);
		}	}
		else {
			stateTime = 0;
		}
		
	}
	public Escudo getEscudo() {
		Color color = new Color(1, 1, 1, 1);
		return new Escudo(0, 0, 0, 0, 0, color, ply);
	}
	
}