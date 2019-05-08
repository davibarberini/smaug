package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import entities.Player;

public class TiroNormal {
	public Rectangle rect;
	public float velX, velY;
	public boolean isAlive = false;
	public int wait = 0;
	public Player ply;
	public int count = 0;
	Texture tiro = new Texture("Cientista/bullet.png");
	
	public TiroNormal(float x, float y, float w, float h, float velX, float velY, Player ply) {
		rect = new Rectangle(x, y, w, h);
		this.velX = velX;
		this.velY = velY;
		this.ply = ply;
	}
	
	public void update(SpriteBatch sb) {
		if(isAlive) {
			rect.x += velX * Gdx.graphics.getDeltaTime();
			rect.y += velY * Gdx.graphics.getDeltaTime();
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
		sb.draw(tiro, rect.x, rect.y, rect.width, rect.height);
		
	}
}
