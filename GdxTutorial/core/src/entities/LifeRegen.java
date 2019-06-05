package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class LifeRegen extends Sprite{
	
	int count = 0;
	public boolean alive = false;
	public float stateTime = 0;
	
	Rectangle rect = new Rectangle(0, 0, 60, 60);
	
	Animation<TextureRegion> regenAnim;
	
	TextureRegion currentFrame;
	
	Texture sprite = new Texture(Gdx.files.internal("Player/heal.png"));
	
	TextureRegion[][] spriteSheet = TextureRegion.split(sprite, 60, 60);
	
	TextureRegion[] healing = new TextureRegion[3];
	
	Player ply;
	
	
	public LifeRegen(Player ply) {
		this.ply = ply;
		
		for(int i=0; i < 3; i++) {
			healing[i] = spriteSheet[0][i];
		}
		
		regenAnim = new Animation<TextureRegion>(0.06f, healing);
	}
	
	public void update() {
		if(alive) {
			count += 1;
			rect.x = ply.rect.x;
			rect.y = ply.rect.y;
			if(count > 50) {
				alive = false;
				count = 0;
			}
			
		}
	}
	public void draw(SpriteBatch sb) {
		if(alive) {
			if(ply.facing == "direita") {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = regenAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, rect.x - 7, rect.y - 17, rect.width, rect.height);
			}
			else {
				stateTime += Gdx.graphics.getDeltaTime();
				currentFrame = regenAnim.getKeyFrame(stateTime, true);
				sb.draw(currentFrame, (rect.x + (rect.width / 2)) + 12, rect.y - 17, -rect.width, rect.height);
			}
			
		}
		else {
			stateTime = 0;
		}
		
	}
	

}
