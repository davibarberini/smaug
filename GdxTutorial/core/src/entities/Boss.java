package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Boss extends Sprite{
	Rectangle rect;
	Player ply;
	float stateTime = 0;
	public boolean alive = false;
	int count = 0;
	
	TextureRegion tank1 = new TextureRegion(new Texture("Boss/tank1.png"));
	TextureRegion tank2 = new TextureRegion(new Texture("Boss/tank2.png"));
	TextureRegion tank3 = new TextureRegion(new Texture("Boss/tank3.png"));
	TextureRegion[] tankTxt = new TextureRegion[] {tank1, tank2, tank3};
	
	Animation<TextureRegion> tankIdle;
	TextureRegion currentFrame;
	
	public Boss(Rectangle rect, Player p1) {
		this.rect = rect;
		ply = p1;
		tankIdle = new Animation<TextureRegion>(0.09f, tankTxt);
	}
	
	public void draw(SpriteBatch sb) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = tankIdle.getKeyFrame(stateTime, true);
		sb.draw(currentFrame, rect.x, rect.y, rect.width, rect.height);
	}
	
	public void update() {
		if(alive) {
			count += 1;
			if(count < 60) {
				rect.x -= 10;
			}
			else if(count < 119) {
				rect.x += 10;
			}
			else count = 0;
		}
	}

}
