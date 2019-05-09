package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;

public class Parallax {
	Texture image;
	public int velocity;
	public float lastX = 0;
	public float x = -1200;
	public int y;

	public Parallax(String image, int y, int velocity) {
		this.image = new Texture(image);
		this.y = y;
		this.velocity = velocity;
		
	}
	
	public void parallax(MyGdxGame game, Player ply) {
		if(lastX != ply.rect.x) {
			x -= (ply.velX  / velocity) * Gdx.graphics.getDeltaTime();
		}
		//System.out.println(image.getWidth() +  "  " + x);
		game.batch.draw(image, x , y);
		game.batch.draw(image, x + image.getWidth(), y);
		lastX = ply.rect.x;
	}
}
