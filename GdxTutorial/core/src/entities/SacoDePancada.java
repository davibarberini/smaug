package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class SacoDePancada {
	Texture sacoTxt = new Texture("Tutorial/saco.png");
	TextureRegion saco = new TextureRegion(sacoTxt);
	Polygon polygon, plypolygon;
	Rectangle rect;
	Player ply;
	boolean attacked = false;
	float rotation = 0;
	float rotated = 0;
	float rotationSpeed = 0;
	int dano = 0;
	
	
	public SacoDePancada(float x, float y, float w, float h, Player ply) {
		rect = new Rectangle(x, y, w, h);
		
		polygon = new Polygon(new float[]{rect.x, rect.y, rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + 
				rect.height, rect.x + rect.width, rect.y});
		polygon.setOrigin(rect.x + (rect.width / 2), rect.y + rect.height);
		
		plypolygon = new Polygon(new float[] {ply.rect.x, ply.rect.y, ply.rect.x, ply.rect.y + ply.rect.height,
				ply.rect.x + ply.rect.width, ply.rect.y + 
				ply.rect.height, ply.rect.x + ply.rect.width, ply.rect.y});
		
		this.ply = ply;
		
		
	}
	
	public void update() {
		plypolygon = new Polygon(new float[] {ply.rect.x, ply.rect.y, ply.rect.x, ply.rect.y + ply.rect.height,
				ply.rect.x + ply.rect.width, ply.rect.y + 
				ply.rect.height, ply.rect.x + ply.rect.width, ply.rect.y});
		if(Intersector.overlapConvexPolygons(polygon, plypolygon) && ply.isAttacking) {
			if(ply.facing == "direita") {
				if(!attacked) {
					rotationSpeed = 80;
					attacked = true;
				}
					
			}
			else {
				if(!attacked) {
					rotationSpeed = -80;
					attacked = true;
				}
			}
		}
		else attacked = false;
		rotation += rotationSpeed * Gdx.graphics.getDeltaTime();
		rotated += rotationSpeed * Gdx.graphics.getDeltaTime();
		polygon.rotate(rotationSpeed * Gdx.graphics.getDeltaTime());
		if(rotated > 0) rotationSpeed -= 80 * Gdx.graphics.getDeltaTime();
		else if(rotated < 0) rotationSpeed += 80 * Gdx.graphics.getDeltaTime();
		
	}
	
	public void draw(SpriteBatch sb) {
		sb.draw(saco, rect.x, rect.y, rect.width / 2, rect.height, rect.width, rect.height, 1, 1, rotation);
	}
	public void drawRect(ShapeRenderer sr, OrthographicCamera camera) {
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.setColor(0, 1, 0, 1);
		sr.polygon(polygon.getTransformedVertices());
		sr.polygon(plypolygon.getTransformedVertices());
		sr.end();
	}
}
