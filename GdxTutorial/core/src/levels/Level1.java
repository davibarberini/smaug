package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Platform;
import com.mygdx.game.Player;


public class Level1 extends ScreenAdapter {
  public static Player p1 = new Player(0, 0, 50, 50, 0.0, 0.0, 0.0);
  public static int WIDTH;  
  public static int HEIGHT;
  public static boolean isOverLaping;
  public static Platform plat1 = new Platform(0, 0, 1000, 200);
  public static Platform [] platforms;
  Texture texture;
  OrthographicCamera camera;
  MyGdxGame game;

  public Level1(MyGdxGame game) {
	  this.game = game;

  }
  
  @Override
  public void show() {
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.SPACE) {
                  game.setScreen(new EndScreen(game));
              }
              return true;
          }
      });
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	    
	  p1.rect.x = WIDTH / 2;
	  p1.rect.y = HEIGHT / 2;
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.position.set(p1.rect.x, p1.rect.y, 0);
	  camera.update();
	  texture = new Texture(Gdx.files.internal("sprite.png"));
  }
  
  @Override
  public void render(float delta) {
    
	p1.rect.y += p1.gravity * delta;
	if(p1.rect.overlaps(plat1.rect)) plat1.platCollision(0.0, p1.gravity, p1);
	p1.rect.x += p1.velX * delta;
	if(p1.rect.overlaps(plat1.rect)) plat1.platCollision(p1.velX, 0.0, p1);
	p1.update();
	
    camera.position.set(p1.rect.x, p1.rect.y, 0);
	camera.update();
	
	this.draw();
  
    eventKey();
  }
  
  public void draw() {
	  Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  game.shapeRenderer.setColor(0, 1, 0, 1);
	  game.shapeRenderer.rect(p1.rect.x, p1.rect.y, p1.rect.width, p1.rect.height);
	  game.shapeRenderer.setColor(1, 0, 0, 1);
	  game.shapeRenderer.rect(plat1.rect.x, plat1.rect.y, plat1.rect.width, plat1.rect.height);
	  game.shapeRenderer.end();
	  game.batch.begin();
	  game.batch.draw(texture, WIDTH / 2, HEIGHT / 2);
	  game.batch.end();
  }

  public void eventKey() {
    if(Gdx.input.isKeyPressed(Input.Keys.W)) {
      p1.gravity = 500;
    }
    
    else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
     p1.velX = 500;
    }
    
    else if(!Gdx.input.isKeyPressed(Input.Keys.D) && p1.velX > 0) {
     p1.velX = 0;
    }
    
    else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
     p1.velX = -500;
    }
    
    else if(!Gdx.input.isKeyPressed(Input.Keys.A) && p1.velX < 0) {
     p1.velX = 0;
    }
    else if(Gdx.input.isKeyPressed(Input.Keys.K)) {
    	camera.zoom = 2;
    }
    else if(!Gdx.input.isKeyPressed(Input.Keys.K)) {
    	camera.zoom = 1;
    }
  }
  
}

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
//
