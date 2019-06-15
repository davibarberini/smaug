package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;

public class Boss extends Sprite{
	Rectangle rect;
	Player ply;
	float stateTime = 0;
	int spriteAdjustmentY = -115;
	int spriteAdjustmentX = -20;
	int spriteLargura = 300;
	int spriteAltura = 300;
	public boolean alive = false;
	int count = 0;
	String animState = "tankFlying";
	float walked = 0;
	float toWalkRight = -130;
	float toWalkLeft = 500;
	float vel = 400;
	float velX = vel;
	String state = "Tank";
	Texture teste = new Texture("Player/vida.png");
	
	Texture tankSheet = new Texture(Gdx.files.internal("Boss/tank.png"));
	TextureRegion[][] tank = TextureRegion.split(tankSheet, 300, 300);
	TextureRegion[] tankWalk = new TextureRegion[3];
	TextureRegion[] tankDeath = new TextureRegion[6];
	
	Texture capSheet = new Texture(Gdx.files.internal("Boss/asadelta.png"));
	TextureRegion[][] cap = TextureRegion.split(capSheet, 120, 120);
	TextureRegion[] leavingTank = new TextureRegion[1];
	TextureRegion[] transformation = new TextureRegion[9];
	TextureRegion[] capShooting = new TextureRegion[3];
	TextureRegion[] capLaser = new TextureRegion[3];
	TextureRegion[] capFlying = new TextureRegion[3];
	TextureRegion[] eyeLaser = new TextureRegion[5];
	TextureRegion[] capFalling = new TextureRegion[4];
	TextureRegion[] capLanding = new TextureRegion[1];
	TextureRegion[] capDying = new TextureRegion[9];
	
	Animation<TextureRegion> tankIdle;
	Animation<TextureRegion> tankDying;
	TextureRegion currentFrame;
	
	public Boss(Rectangle rect, Player p1) {
		this.rect = rect;
		ply = p1;
		
		//TANK SPRITES
		for(int e=0; e < 3; e++) {
			tankWalk[e] = tank[0][e];
		}
		
		for(int e=0; e < 4; e++) {
			tankDeath[e] = tank[1][e];
		}
		tankDeath[4] = tank[2][0];
		tankDeath[5] = tank[2][1];
		
		tankIdle = new Animation<TextureRegion>(0.09f, tankWalk);
		tankDying = new Animation<TextureRegion>(0.09f, tankDeath);
		
		//ASADELTA SPRITES
		leavingTank[0] = cap[0][0];
		capLanding[0] = cap[6][4];
		for(int i=0; i < 3; i++) {
			capShooting[i] = cap[2][i];
			capLaser[i] = cap[3][i];
			capFlying[i] = cap[4][i];
		}
		for(int i=0; i < 4; i++) {
			transformation[i] = cap[0][i + 1];
			capFalling[i] = cap[6][i];
			capDying[i + 5] = cap[7][i];
		}
		for(int i=0; i < 5; i++) {
			transformation[i + 4] = cap[1][i];
			eyeLaser[i] = cap[5][i];
			capDying[i] = cap[7][i];
		}
		
	}
	
	public void draw(SpriteBatch sb) {
		//sb.draw(teste, rect.x, rect.y, rect.width, rect.height);
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = tankIdle.getKeyFrame(stateTime, true);
		sb.draw(currentFrame, rect.x + spriteAdjustmentX, rect.y + spriteAdjustmentY, spriteLargura, spriteAltura);
		
	}
	
	public void update() {
		if(alive && state == "Tank") {
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

}
