package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Parallax;

import entities.Player;
import platforms.Platform;


public class Level2 extends ScreenAdapter {
  public Player p1;
  public static int WIDTH;
  public static int HEIGHT;
  
  //MapFileWriter mapWriter;
  MapFileReader mapReader;
	
  public Platform [] platforms;
  
  OrthographicCamera camera;
  FillViewport view;
  MyGdxGame game;
  
  Texture fundo, fundo2;
  Parallax prx1;
  Parallax prx2;
  Parallax prx3;

  public Level2(MyGdxGame game) {
	  this.game = game;
	  //mapWriter = new MapFileWriter(mapLin, mapCol);
	  //mapWriter.writeMap(map, "Level1");
	  mapReader = new MapFileReader();
	  platforms = mapReader.readMapToLevel("Level2/Level2", game, "TitleScreen");
	  

  }
  
  @Override
  public void show() {
	  p1 = new Player(0, 0, 35, 35, 0.0, 0.0, 0.0);
	  prx1 = new Parallax("Level2/parallax1.png", 0, 4);
	  prx2 = new Parallax("Level2/parallax2.png", 50, 12);
	  prx3 = new Parallax("Level2/parallax3.png", 150, 40);
	  fundo = new Texture("Level2/city.png");
	  fundo2 = new Texture("Level2/fundo.png");
	  
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  p1.rect.x = 50;
	  p1.rect.y = 20;
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
	  view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	  camera.update();
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.SPACE) {
                  game.setScreen(new TitleScreen(game));
              }
              else if(keyCode == Input.Keys.K) {
              	camera.zoom = 2;
              }
              else if(keyCode == Input.Keys.R) {
            	  game.setScreen(new Level2(game));
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
    
	game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
	p1.rect.y += p1.gravity * delta;
	for(int k=0; k < platforms.length; k++) {     //Colisao após a movimentação Y
		  if(platforms[k] != null) {
			  Platform plat = platforms[k];
			  if(p1.rect.overlaps(plat.rect)) plat.platCollision(0.0, p1.gravity, p1);
		  }  
	}
	
	p1.rect.x += p1.velX * delta;
	for(int k=0; k < platforms.length; k++) {   //Colisao após a movimentação X
		  if(platforms[k] != null) {
			  Platform plat = platforms[k];
			  if(p1.rect.overlaps(plat.rect)) plat.platCollision(p1.velX, 0.0, p1);
		  }
		  
	}
	p1.update();
	
    camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
	camera.update();
	
	this.draw();
  
  }
  
  public void draw() {
	  Gdx.gl.glClearColor(0, 0, 1, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  
	 
	  
	  /*game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  game.shapeRenderer.setColor(0, 1, 0, 1);
	  game.shapeRenderer.rect(p1.rect.x, p1.rect.y, p1.rect.width, p1.rect.height);
	  
	  for(int k=0; k < platforms.length; k++) {
		  if(platforms[k] != null) {
			  game.shapeRenderer.setColor(platforms[k].color);
			  Platform plat = platforms[k];
			  game.shapeRenderer.rect(plat.rect.x, plat.rect.y, plat.rect.width, plat.rect.height);  
		  }
		  
	  }
	  game.shapeRenderer.end();*/
	  
	  game.batch.setProjectionMatrix(camera.combined);
	  game.batch.begin();
	  game.batch.draw(fundo2, -900, -400, 4000, 1300);
	  prx3.parallax(game, p1);
	  prx2.parallax(game, p1);
	  prx1.parallax(game, p1);
	  game.batch.draw(fundo, 3 , 20);
	  p1.draw(game.batch);
	  //game.batch.draw(idle,  p1.rect.x, p1.rect.y, 35, 35);
	  game.batch.end();
	 
	  
  }
  
  public void dispose() {
	  Gdx.input.setInputProcessor(null);
	  game.shapeRenderer.setProjectionMatrix(null);
	  this.game.shapeRenderer.dispose();
	  
  }
  
  
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
