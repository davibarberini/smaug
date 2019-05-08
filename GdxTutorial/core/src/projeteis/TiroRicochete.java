package projeteis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Player;

public class TiroRicochete extends TiroNormal{
	
	public float maxY, minY;
	public float moved = 0;
	public int bounceCount = 0;
	public float vel = 0;
	
	public TiroRicochete(float x, float y, float w, float h, float velX, float velY, Player ply, float maxY, float minY) {
		super(x, y, w, h, velX, velY, ply);
		this.maxY = maxY;
		this.minY = minY;
	}
	
	public void update(SpriteBatch sb) {
		if(isAlive) {
			rect.x += velX * Gdx.graphics.getDeltaTime();
			rect.y += velY * Gdx.graphics.getDeltaTime();
			moved += velY * Gdx.graphics.getDeltaTime();
			if(moved > maxY) {
				velY = - vel;
				bounceCount += 1;
			}
			else if(moved < minY) {
				velY = vel;
				bounceCount += 1;
			}
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
