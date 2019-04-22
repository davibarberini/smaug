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
  public static Player p1;
  public static int WIDTH;  
  public static int HEIGHT;
  public static int mapCol = 12;
  public static int mapLin = 8;
  public static int[][] map = new int[][]{
	  { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
	  { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	  { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	  { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	  { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};
	
  public static Platform [][] platforms;
  
  OrthographicCamera camera;
  MyGdxGame game;
  
  Texture fundo, idle;

  public Level1(MyGdxGame game) {
	  this.game = game;

  }
  
  @Override
  public void show() {
	  p1 = new Player(0, 0, 35, 35, 0.0, 0.0, 0.0);
	  fundo = new Texture("lab.png");
	  idle = new Texture("sprite.png");
	  
	  platforms = new Platform[mapLin][mapCol];
	  for(int e=0; e < mapLin; e++) {
		  for(int i=0; i < mapCol; i++) {
			  if(map[e][i] == 1) {
				  platforms[e][i] = new Platform(i * 50, (e * -20) + (20 * mapLin), 50, 20);
			  }
		  }
	  }
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  p1.rect.x = 0;
	  p1.rect.y = 0;
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.position.set(p1.rect.x, p1.rect.y, 0);
	  camera.update();
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.SPACE) {
                  game.setScreen(new EndScreen(game));
              }
              else if(keyCode == Input.Keys.W) {
                  p1.gravity = 500;
              }
              else if(keyCode == Input.Keys.D) {
              	p1.velX = 500;
              }
              else if(keyCode == Input.Keys.A) {
              	p1.velX = -500;
              }
              else if(keyCode == Input.Keys.K) {
              	camera.zoom = 2;
              }
              return true;
          }
          public boolean keyUp(int keyCode) {
        	  if(keyCode == Input.Keys.D && p1.velX > 0) {
                	p1.velX = 0;
              }
        	  else if(keyCode == Input.Keys.A && p1.velX < 0) {
                	p1.velX = 0;
              }
        	  else if(keyCode == Input.Keys.K) {
                	camera.zoom = 1;
              }
        	  return true;
          }
      });
	  
	 
  }
  
  @Override
  public void render(float delta) {
    
	p1.rect.y += p1.gravity * delta;
	for(int k=0; k < mapLin; k++) {     //Colisao após a movimentação Y
		  for(int j=0; j < mapCol; j++)
		  if(platforms[k][j] != null) {
			  Platform plat = platforms[k][j];
			  if(p1.rect.overlaps(plat.rect)) plat.platCollision(0.0, p1.gravity, p1);
		  }  
	  }
	p1.rect.x += p1.velX * delta;
	for(int k=0; k < mapLin; k++) {   //Colisao após a movimentação X
		  for(int j=0; j < mapCol; j++)
		  if(platforms[k][j] != null) {
			  Platform plat = platforms[k][j];
			  if(p1.rect.overlaps(plat.rect)) plat.platCollision(p1.velX, 0.0, p1);
		  }
		  
	  }
	p1.update();
	
    camera.position.set(p1.rect.x, p1.rect.y, 0);
	camera.update();
	
	this.draw();
  
  }
  
  public void draw() {
	  Gdx.gl.glClearColor(0, 0, 1, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  
	  
	 
	  game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  game.shapeRenderer.setColor(0, 1, 0, 1);
	  game.shapeRenderer.rect(p1.rect.x, p1.rect.y, p1.rect.width, p1.rect.height);
	  game.shapeRenderer.setColor(1, 0, 0, 1);
	  
	  for(int k=0; k < mapLin; k++) {
		  for(int j=0; j < mapCol; j++)
		  if(platforms[k][j] != null) {
			  Platform plat = platforms[k][j];
			  game.shapeRenderer.rect(plat.rect.x, plat.rect.y, plat.rect.width, plat.rect.height);  
		  }
		  
	  }
	  game.shapeRenderer.end();
	  
	  game.batch.setProjectionMatrix(camera.combined);
	  game.batch.begin();
	  game.batch.draw(fundo, 3 , 20);
	  game.batch.draw(idle,  p1.rect.x, p1.rect.y, 35, 35);
	  game.batch.end();
	  
  }
  public void dispose() {
	  game.shapeRenderer.setProjectionMatrix(null);
  }
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
