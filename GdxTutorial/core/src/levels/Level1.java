package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Player;

import levelWithMatrix.Level2Matrix;
import platforms.Platform;


public class Level1 extends ScreenAdapter {
  public Player p1;
  public static int WIDTH;
  public static int HEIGHT;
  
  MapFileWriter mapWriter;
  MapFileReader mapReader;
	
  public static Platform [] platforms;
  
  OrthographicCamera camera;
  MyGdxGame game;
  
  Texture fundo, idle;

  public Level1(MyGdxGame game) {
	  this.game = game;
	  //mapWriter = new MapFileWriter(mapLin, mapCol);
	  //mapWriter.writeMap(map, "Level1");
	  mapReader = new MapFileReader();
	  platforms = mapReader.readMapToLevel("Level1");
	  

  }
  
  @Override
  public void show() {
	  p1 = new Player(0, 0, 35, 35, 0.0, 0.0, 0.0);
	  fundo = new Texture("lab.png");
	  idle = new Texture("sprite.png");
	  
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  p1.rect.x = 50;
	  p1.rect.y = 20;
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.position.set(p1.rect.x, p1.rect.y, 0);
	  camera.update();
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.SPACE) {
                  game.setScreen(new Level2Matrix(game));
              }
              else if(keyCode == Input.Keys.K) {
              	camera.zoom = 2;
              }
              p1.keyDown(keyCode);
              return true;
          }
          public boolean keyUp(int keyCode) {
        	  if(keyCode == Input.Keys.K) {
                	camera.zoom = 1;
              }
        	  p1.keyUp(keyCode);
        	  return true;
          }
      });
	  
	 
  }
  
  @Override
  public void render(float delta) {
    
	p1.rect.y += p1.gravity * delta;
	for(int k=0; k < platforms.length; k++) {     //Colisao ap�s a movimenta��o Y
		  if(platforms[k] != null) {
			  Platform plat = platforms[k];
			  if(p1.rect.overlaps(plat.rect)) plat.platCollision(0.0, p1.gravity, p1);
		  }  
	}
	
	p1.rect.x += p1.velX * delta;
	for(int k=0; k < platforms.length; k++) {   //Colisao ap�s a movimenta��o X
		  if(platforms[k] != null) {
			  Platform plat = platforms[k];
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
	  
	  /*for(int k=0; k < platforms.length; k++) {
		  if(platforms[k] != null) {
			  game.shapeRenderer.setColor(platforms[k].color);
			  Platform plat = platforms[k];
			  game.shapeRenderer.rect(plat.rect.x, plat.rect.y, plat.rect.width, plat.rect.height);  
		  }
		  
	  }*/
	  game.shapeRenderer.end();
	  
	  game.batch.setProjectionMatrix(camera.combined);
	  game.batch.begin();
	  game.batch.draw(fundo, 3 , 20);
	  p1.draw(game.batch);
	  //game.batch.draw(idle,  p1.rect.x, p1.rect.y, 35, 35);
	  game.batch.end();
	 
	  
  }
  
  public void hide() {
	  Gdx.input.setInputProcessor(null);
	  
  }
  
  
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
