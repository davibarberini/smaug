package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class CientistaDePancada {
	Texture sacoTxt = new Texture("Tutorial/saco.png");
	Polygon polygon, plypolygon;
	Rectangle rect;
	Player ply;
	int adjustmentX = 40;
	String animState = "dano";
	boolean attacked = false;
	float rotation = 0;
	float rotated = 0;
	float rotationSpeed = 0;
	int dano = 0;
	int countRegen = 0;
	float stateTime = 0;
	
	TextureRegion currentFrame;
	
	Animation<TextureRegion> regen;
	
	TextureRegion[][] sacoSheet = TextureRegion.split(sacoTxt, 80, 80);
	TextureRegion[] sacoDano = new TextureRegion[4];
	TextureRegion[] sacoRegen = new TextureRegion[3];
	
	
	public CientistaDePancada(float x, float y, float w, float h, Player ply) {
		rect = new Rectangle(x, y, w, h);
		this.ply = ply;
		
		for(int e=0; e < 4; e++) {
			sacoDano[e] = sacoSheet[0][e];
		}
		for(int e=0; e < 2; e ++) {
			sacoRegen[e] = sacoSheet[0][e + 4];
		}
		sacoRegen[2] = sacoSheet[0][0];
		
		regen = new Animation<TextureRegion>(0.5f, sacoRegen);
		
	}
	
	public void update() {
		if(rect.overlaps(ply.rect) && ply.isAttacking) {
			if(ply.facing == "direita") {
				if(!attacked) {
					attacked = true;
					dano += 1;
					countRegen = 0;
					animState = "dano";
				}
					
			}
			else {
				if(!attacked) {
					attacked = true;
					dano += 1;
					countRegen = 0;
					animState = "dano";
					
				}
			}
		}
		if(rect.overlaps(ply.tiro.rect) && ply.tiro.isAlive) {
			dano += 1;
			ply.tiro.isAlive = false;
			ply.tiro.count = 0;
			countRegen = 0;
			animState = "dano";
		}
		if(!ply.isAttacking) attacked = false;
		if(!rect.overlaps(ply.rect)) attacked = false;
		if(dano > sacoDano.length - 1) dano = sacoDano.length -1;
		
		if(dano > 0) countRegen += 1;
		if(countRegen >= 100) {
			stateTime = 0;
			animState = "regen";
			countRegen = 0;
			dano = 0;
		}
		
	}
	
	public void draw(SpriteBatch sb) {
		if(animState == "dano") sb.draw(sacoDano[dano], rect.x - (adjustmentX / 2), rect.y, rect.width + adjustmentX, rect.height);
		else if(animState == "regen") {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = regen.getKeyFrame(stateTime);
			sb.draw(currentFrame, rect.x - (adjustmentX / 2), rect.y, rect.width + adjustmentX, rect.height);
		}
	}
	
	public void drawTest(ShapeRenderer sr, OrthographicCamera camera) {
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.rect(rect.x, rect.y, rect.width, rect.height);
		sr.end();
	}
}
