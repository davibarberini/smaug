package editor;

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

import levels.TitleScreen;
import platforms.Hole;
import platforms.NextLevel;
import platforms.Platform;
import platforms.Teleporter;
import platforms.Teleporter2;
import soundandmusic.MusicPlayer;

import java.util.ArrayList;


public class MapEditor extends ScreenAdapter {
  public static double velX, velY;
  public static int WIDTH;
  public static int HEIGHT; 
  public static int selected = 1;
  public static int mouseX, mouseY;
  public float platW, platH;
  public double vel = 200;
  public double pVelX, pVelY;
  public static boolean isClicking;
  public static float colr, linr;
  public ArrayList <Platform> platforms = new ArrayList<Platform>();
  public int x, y;
  public String levelToEdit = "Level3/Level3";
  MapFileWriter mapWriter;
  MapFileReader mapReader;
	
  
  OrthographicCamera camera;
  MyGdxGame game;
  
  Texture fundo;

  public MapEditor(MyGdxGame game) {
	  this.game = game; 
	  mapReader = new MapFileReader();
	  platforms = mapReader.readMapToEditor(levelToEdit, game, "TitleScreen");
	  System.out.println(platforms.size());
	  

  }
  
  @Override
  public void show() {
	//Parando a thread anterior se existir.
	  if(game.t1 != null && game.t1.isAlive()) {
  		game.t1.toStop = true;
  		try {
  			//System.out.println("Join");
			game.t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  }
	  
	  
	  game.t1 = new MusicPlayer("MapEditor/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
      game.t1.start(); 
	  
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  if(levelToEdit == "Level1/Level1") fundo = new Texture("Level1/lab.png");
	  else if(levelToEdit == "Level2/Level2") fundo = new Texture("Level2/city.png");
	  else if(levelToEdit == "Level3/Level3") fundo = new Texture("Level3/city.png");
	  else if(levelToEdit == "Tutorial/Tutorial") fundo = new Texture("Tutorial/fundo.png");
	  
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
            	  mapWriter = new MapFileWriter();
            	  mapWriter.writeMap(platforms, levelToEdit);
            	  platforms.clear();
                  game.setScreen(new TitleScreen(game));
                  return true;
              }
              else if(keyCode == Input.Keys.UP) {
            	  if(camera.zoom == 1) velY = 500;
            	  else if(camera.zoom == 2) velY = 1000;
            	  return true;
                  
            	  
              }
              else if(keyCode == Input.Keys.DOWN) {
            	  if(camera.zoom == 1) velY = -500;
            	  else if(camera.zoom == 2) velY =- 1000;
            	  return true;
              }
              else if(keyCode == Input.Keys.RIGHT) {
            	  if(camera.zoom == 1) velX = 500;
            	  else if(camera.zoom == 2) velX = 1000;
            	  return true;
              }
              else if(keyCode == Input.Keys.LEFT) {
            	  if(camera.zoom == 1) velX = -500;
            	  else if(camera.zoom == 2) velX = -1000;
            	  return true;
              }
              else if(keyCode == Input.Keys.R) {
            	  float temp = platW;
            	  platW = platH;
            	  platH = temp;
            	  return true;
              }
              else if(keyCode == Input.Keys.K) {
            	  if(camera.zoom == 1) camera.zoom = 2;
            	  else if (camera.zoom == 2) camera.zoom = 1;
            	  return true;
              }
              else if(keyCode == Input.Keys.W) {
            	  pVelY = vel;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.S) {
            	  pVelY = -vel;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.D) {
            	  pVelX = vel;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.A) {
            	  pVelX = -vel;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.Z) {
            	  vel = 10;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.X) {
            	  vel = 200;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.C) {
            	  vel = 500;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.NUM_1) {
            	  selected = 1;
            	  return true;
                  
              }
              else if(keyCode == Input.Keys.NUM_4) {
            	  selected = 4;
            	  return true;
              }
              else if(keyCode == Input.Keys.NUM_5) {
            	  selected = 5;
            	  return true;
              }
              else if(keyCode == Input.Keys.NUM_9) {
            	  selected = 9;
            	  return true;
              }
              else if(keyCode == Input.Keys.NUM_2) {
            	  selected = 2;
            	  return true;
              }
              else if(keyCode == Input.Keys.ESCAPE) {
            	  game.setScreen(new TitleScreen(game));
              }
              
              return false;
              
          }
          public boolean keyUp(int keyCode) {
        	  if(keyCode == Input.Keys.RIGHT && velX > 0) {
                	velX = 0;
                	return true;
              }
        	  else if(keyCode == Input.Keys.LEFT && velX < 0) {
                	velX = 0;
                	return true;
              }
        	  if(keyCode == Input.Keys.UP && velY > 0) {
              		velY = 0;
              		return true;
              }
        	  else if(keyCode == Input.Keys.DOWN && velY < 0) {
              		velY = 0;
              		return true;
              }
        	  if(keyCode == Input.Keys.D && pVelX > 0) {
              		pVelX = 0;
              		return true;
	          }
	      	  else if(keyCode == Input.Keys.A && pVelX < 0) {
	              	pVelX = 0;
	              	return true;
	          }
	      	  if(keyCode == Input.Keys.W && pVelY > 0) {
	            	pVelY = 0;
	            	return true;
	          }
	      	  else if(keyCode == Input.Keys.S && pVelY < 0) {
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
        	  System.out.println("X:" + mouseX + "Y" + mouseY);
              if (button == Input.Buttons.LEFT) {
            	  if(selected == 999) {
            		  for(int e=0; e < platforms.size(); e++) {
            			  if(pointInRectangle(platforms.get(e).rect, mouseX, mouseY)){
            				  platforms.remove(e);
            			  }
            		  }
            	  }
            	  else if(selected == 1) {
            		  Color color = new Color(1, 0, 0, 1);
            		  platforms.add(new Platform(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH, 0, color));  
            	  }
            	  else if(selected == 2) {
            		  Color color = new Color(1, 1, 1, 1);
            		  platforms.add(new Hole(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH, 2, color));  
            	  }
            	  else if(selected == 4) {
            		  Color color = new Color(0, 1, 0, 1);
            		  platforms.add(new Teleporter(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH, 4, color));  
            	  }
            	  else if(selected == 5) {
            		  Color color = new Color(1, 1, 0, 1);
            		  platforms.add(new Teleporter2(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH, 5, color));  
            	  }
            	  else if(selected == 9) {
            		  Color color = new Color(0, 0, 1, 1);
            		  platforms.add(new NextLevel(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH, 9, color));  
            	  }
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
	  Vector3 vec = new Vector3((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
	  vec = camera.unproject(vec);
	  mouseX = (int) vec.x;
	  mouseY =(int) vec.y;
	  
	  game.batch.setProjectionMatrix(camera.combined);
	  game.batch.begin();
	  if(levelToEdit == "Level1/Level1") game.batch.draw(fundo, -288, -210);
	  else if(levelToEdit == "Level2/Level2") game.batch.draw(fundo, -225 , -180);
	  else if(levelToEdit == "Level3/Level3") game.batch.draw(fundo, -225 , -180);
	  else if(levelToEdit == "Tutorial/Tutorial") game.batch.draw(fundo, -259, -329, fundo.getWidth() - 235, fundo.getHeight() - 235);
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
	  
	  if(selected != 999) {
		  if(selected == 1) game.shapeRenderer.setColor(1, 0, 0, 1);
		  else if(selected == 2) game.shapeRenderer.setColor(1, 1, 1, 1);
		  else if(selected == 4) game.shapeRenderer.setColor(0, 1, 0, 1);
		  else if(selected == 5) game.shapeRenderer.setColor(1, 1, 0, 1);
		  else if(selected == 9) game.shapeRenderer.setColor(0, 0, 1, 1);
    	  game.shapeRenderer.rect(mouseX - (platW / 2), mouseY - (platH / 2), platW, platH);  
	  }
	  else {
		  game.shapeRenderer.setColor(0, 0, 0, 1);
		  game.shapeRenderer.circle(mouseX, mouseY, 5);
	  }
	  game.shapeRenderer.end();
	  
	 
	  
  }
  
  public static boolean pointInRectangle (Rectangle r, float x, float y) {
      return r.x <= x && r.x + r.width >= x && r.y <= y && r.y + r.height >= y;
  }
  public void hide() {
	  Gdx.input.setInputProcessor(null);
  }
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
