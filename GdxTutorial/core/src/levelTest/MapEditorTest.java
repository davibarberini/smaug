package levelTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Platform;

import levels.TitleScreen;

import java.util.ArrayList;
import java.util.List;


public class MapEditorTest extends ScreenAdapter {
  public static double velX, velY;
  public static int WIDTH;
  public static int HEIGHT; 
  public static int selected = 1;
  public static int mouseX, mouseY;
  public float platW, platH;
  public double pVelX, pVelY;
  public static boolean isClicking;
  public static float colr, linr;
  public static List <Platform> platforms = new ArrayList<Platform>();
  public int x, y;
  MapFileWriterTest mapWriter;
  MapFileReaderTest mapReader;
	
  
  OrthographicCamera camera;
  MyGdxGame game;
  
  Texture fundo, idle;

  public MapEditorTest(MyGdxGame game) {
	  this.game = game; 
	  mapReader = new MapFileReaderTest();
	  platforms = mapReader.readMapToEditor("Level1PlatTest");
	  

  }
  public void RestartMap(String mapName) {
	  mapReader = new MapFileReaderTest();
	  platforms = mapReader.readMapToEditor(mapName);
  }
  
  @Override
  public void show() {
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  fundo = new Texture("lab.png");
	  idle = new Texture("sprite.png");
	  
	  x = 0;
	  y = 900;
	  platW = 10;
	  platH = 10;
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.zoom = 2;
	  camera.position.set(0, 0, 0);
	  camera.update();
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.SPACE) {
            	  mapWriter = new MapFileWriterTest();
            	  mapWriter.writeMap(platforms, "Level1PlatTest");
                  game.setScreen(new TitleScreen(game));
                  return true;
              }
              else if(keyCode == Input.Keys.W) {
            	  if(camera.zoom == 1) velY = 500;
            	  else if(camera.zoom == 2) velY = 1000;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.S) {
            	  if(camera.zoom == 1) velY = -500;
            	  else if(camera.zoom == 2) velY =- 1000;
            	  return true;
              }
              else if(keyCode == Input.Keys.D) {
            	  if(camera.zoom == 1) velX = 500;
            	  else if(camera.zoom == 2) velX = 1000;
            	  return true;
              }
              else if(keyCode == Input.Keys.A) {
            	  if(camera.zoom == 1) velX = -500;
            	  else if(camera.zoom == 2) velX = -1000;
            	  return true;
              }
              else if(keyCode == Input.Keys.R) {
            	  //reset positions
            	  return true;
              }
              else if(keyCode == Input.Keys.K) {
            	  if(camera.zoom == 1) camera.zoom = 2;
            	  else if (camera.zoom == 2) camera.zoom = 1;
            	  return true;
              }
              else if(keyCode == Input.Keys.UP) {
            	  pVelY = 200;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.DOWN) {
            	  pVelY = -200;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.RIGHT) {
            	  pVelX = 200;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.LEFT) {
            	  pVelX = -200;
            	  return true;
                  
              }
              return false;
          }
          public boolean keyUp(int keyCode) {
        	  if(keyCode == Input.Keys.D && velX > 0) {
                	velX = 0;
                	return true;
              }
        	  else if(keyCode == Input.Keys.A && velX < 0) {
                	velX = 0;
                	return true;
              }
        	  if(keyCode == Input.Keys.W && velY > 0) {
              		velY = 0;
              		return true;
              }
        	  else if(keyCode == Input.Keys.S && velY < 0) {
              		velY = 0;
              		return true;
              }
        	  if(keyCode == Input.Keys.RIGHT && pVelX > 0) {
              		pVelX = 0;
              		return true;
	          }
	      	  else if(keyCode == Input.Keys.LEFT && pVelX < 0) {
	              	pVelX = 0;
	              	return true;
	          }
	      	  if(keyCode == Input.Keys.UP && pVelY > 0) {
	            	pVelY = 0;
	            	return true;
	          }
	      	  else if(keyCode == Input.Keys.DOWN && pVelY < 0) {
	            	pVelY = 0;
	            	return true;
	          }
        	  return false;
          }
          public boolean touchDown (int x, int y, int pointer, int button) {
        	  Vector3 vec = new Vector3((float)x, (float)y, 0);
        	  vec = camera.unproject(vec);
        	  mouseX = (int) vec.x;
        	  mouseY =(int) vec.y;
        	  Color color = new Color(1, 0, 0, 1);
        	  System.out.println("X:" + mouseX + "Y" + mouseY);
              if (button == Input.Buttons.LEFT) {
                  platforms.add(new Platform(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH, 0, color));
                  return true;     
              }
              if (button == Input.Buttons.RIGHT) {
            	  selected = 999;
              }
              return false;
           }
          public boolean touchUp (int x, int y, int pointer, int button) {
        	  if(button == Input.Buttons.LEFT) {
        		 isClicking = false;
        	  }
        	  
          return false;
          }
      });
	  
	 
  }
  
  @Override
  public void render(float delta) {
	
    camera.position.set(x + WIDTH - 20, y - HEIGHT + 20, 0);
	camera.update();
	
	x += velX * Gdx.graphics.getDeltaTime();
	y += velY * Gdx.graphics.getDeltaTime();
	platW += pVelX * Gdx.graphics.getDeltaTime();
	platH += pVelY * Gdx.graphics.getDeltaTime();
	if(platW <= 1) {
		platW = 1;
	}
	if(platH <= 1) {
		platH = 1;
	}
	
	colr = (mouseX / 50);
	linr = (900 - mouseY) / 20;
	
	//System.out.println("Col: " + colr +"Lin: " + linr);
	
	this.draw();
  
  }
  
  public void draw() {
	  Gdx.gl.glClearColor(0, 0, 0, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  
	  
	  game.batch.setProjectionMatrix(camera.combined);
	  game.batch.begin();
	  game.batch.draw(fundo, 3 , 20);
	  game.batch.end();
	  
	  game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  
	  for(int k=0; k < platforms.size(); k++) {
		  if(platforms.get(k) != null) {
			  game.shapeRenderer.setColor(platforms.get(k).color);
			  Platform plat = platforms.get(k);
			  game.shapeRenderer.rect(plat.rect.x, plat.rect.y, plat.rect.width, plat.rect.height);  
		  }
		  
	  }
	  
	  if(selected != 99999) {
		  Vector3 vec = new Vector3((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
    	  vec = camera.unproject(vec);
    	  mouseX = (int) vec.x;
    	  mouseY =(int) vec.y;
    	  game.shapeRenderer.rect(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH);  
	  }
	  game.shapeRenderer.end();
	  
	 
	  
  }
  
  public static boolean pointInRectangle (Rectangle r, float x, float y) {
      return r.x <= x && r.x + r.width >= x && r.y <= y && r.y + r.height >= y;
  }
  public void dispose() {
	  game.shapeRenderer.setProjectionMatrix(null);
  }
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
