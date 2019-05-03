package levelWithMatrix;

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
import platforms.Platform;

import java.util.ArrayList;
import java.util.List;


public class MapEditorMatrix extends ScreenAdapter {
  public static double velX, velY;
  public static int WIDTH;
  public static int HEIGHT; 
  public static int selected = 99999;
  public static int mouseX, mouseY;
  public static boolean isClicking;
  public static float colr, linr;
  public static int mapCol = 25;
  public static int mapLin = 44;
  public static int[][] map = new int[mapLin][mapCol];
  public static List <Platform> blocks = new ArrayList<Platform>();
  
  MapFileWriterMatrix mapWriter;
  MapFileReaderMatrix mapReader;
	
  public static Platform [][] platforms;
  
  OrthographicCamera camera;
  MyGdxGame game;
  
  Texture fundo, idle;

  public MapEditorMatrix(MyGdxGame game) {
	  this.game = game; 
	  mapReader = new MapFileReaderMatrix(mapLin, mapCol);
	  map = mapReader.readMap("Level1");
	  

  }
  public void RestartMap(String mapName) {
	  mapReader = new MapFileReaderMatrix(mapLin, mapCol);
	  map = mapReader.readMap(mapName);
  }
  
  @Override
  public void show() {
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  Platform platEditor1 = new Platform(0, 920, 50, 20, 1, new Color(1, 0, 0, 1));
	  Platform platEditor2 = new Platform(70, 920, 25, 10, 2, new Color(1, 0.8f, 0, 1));
	  Platform platEditor3 = new Platform(115, 920, 25, 10, 3, new Color(1, 0, 0.8f, 1));
	  Platform platEditor4 = new Platform(150, 920, 20, 50, 4, new Color(0, 1, 0, 1));
	  blocks.add(platEditor1);
	  blocks.add(platEditor2);
	  blocks.add(platEditor3);
	  blocks.add(platEditor4);
	  fundo = new Texture("lab.png");
	  idle = new Texture("sprite.png");
	  
	  platforms = new Platform[mapLin][mapCol];
	  for(int e=0; e < mapLin; e++) {
		  for(int i=0; i < mapCol; i++) {
			  if(map[e][i] == 1) {
				  platforms[e][i] = new Platform(i * 50, (e * -20) + (20 * mapLin), 50, 20, 1, new Color(1, 0, 0, 1));
			  }
			  else if(map[e][i] == 2) {
				  platforms[e][i] = new Platform(i * 50, (e * -20) + (20 * mapLin), 25, 10, 2, new Color(1, 0.8f, 0, 1));
			  }
			  else if(map[e][i] == 3) {
				  platforms[e][i] = new Platform(i * 50, (e * -20) + (20 * mapLin) + 10, 25, 10, 3, new Color(1, 0, 0.8f, 1));
			  }
			  else if(map[e][i] == 4) {
				  platforms[e][i] = new Platform(i * 50, (e * -20) + (20 * mapLin), 20, 50, 4, new Color(0, 1, 0, 1));
			  }
		  }
	  }
	  
	  
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.zoom = 2;
	  camera.position.set(0, 0, 0);
	  camera.update();
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.SPACE) {
            	  for(int lin=0; lin < mapLin; lin++) {
            		  for(int col=0; col < mapCol; col++) {
            			  if(platforms[lin][col] != null) {
            				  map[lin][col] = platforms[lin][col].platformType;
            			  }
            			  else {
            				  map[lin][col] = 0;
            			  }
            			  
            		  }
            	  }
            	  mapWriter = new MapFileWriterMatrix(mapLin, mapCol);
            	  mapWriter.writeMap(map, "Level1");
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
            	  blocks.get(0).rect.x = 0;
            	  blocks.get(0).rect.y = 920;
            	  return true;
              }
              else if(keyCode == Input.Keys.K) {
            	  if(camera.zoom == 1) camera.zoom = 2;
            	  else if (camera.zoom == 2) camera.zoom = 1;
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
        	  return false;
          }
          public boolean touchDown (int x, int y, int pointer, int button) {
        	  Vector3 vec = new Vector3((float)x, (float)y, 0);
        	  vec = camera.unproject(vec);
        	  mouseX = (int) vec.x;
        	  mouseY =(int) vec.y;
        	  System.out.println("X:" + mouseX + "Y" + mouseY);
              if (button == Input.Buttons.LEFT) {
            	  isClicking = true;
            	  for(int b=0; b < blocks.size(); b++) {
            		  if(camera.zoom == 2) {
            			  if(pointInRectangle(blocks.get(b).rect, mouseX, mouseY)) {
                			  System.out.println(b);
                			  selected = b;
                		  }  
            		  }
            		  else if(camera.zoom == 1) {
            			  Rectangle block = blocks.get(b).rect;
            			  Rectangle block2 = new Rectangle(block.x + 310, block.y - 230, 25, 10);
            			  if(pointInRectangle(block2, mouseX, mouseY)) {
                			  System.out.println(b);
                			  selected = b;
                		  } 
            		  }
            		  
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
	Rectangle mainPosition = blocks.get(0).rect;
    
	mainPosition.y += velY * delta;
	mainPosition.x += velX * delta;
	
    camera.position.set(mainPosition.x + WIDTH - 20, mainPosition.y - HEIGHT + 20, 0);
	camera.update();
	if(isClicking) {
		if(mouseX > 0 && mouseX < 1240 && mouseY < 900 && mouseY > 20) {
			  if(selected != 99999) {
				  if(selected == 0) {
					  platforms[(int)linr][(int)colr] = new Platform(colr * 50, ((linr * -20) + (20 * mapLin)), 50, 20, 1, new Color(1, 0, 0, 1));
				  }
				  else if(selected == 1) {
					  platforms[(int)linr][(int)colr] = new Platform(colr * 50, ((linr * -20) + (20 * mapLin)), 25, 10, 2, new Color(1, 0.8f, 0, 1));
				  }
				  else if(selected == 2) {
					  platforms[(int)linr][(int)colr] = new Platform(colr * 50, ((linr * -20) + (20 * mapLin) + 10), 25, 10, 3, new Color(1, 0, 0.8f, 1));
				  }
				  else if (selected == 999) {
					  platforms[(int)linr][(int)colr] = null;
				  }
				  
			  }
		  }
	}
	
	colr = (mouseX / 50);
	linr = (900 - mouseY) / 20;
	
	if(linr > mapLin - 1) {
		linr = mapLin - 1;
	}
	if(colr > mapCol - 1) {
		colr = mapCol - 1;
	}
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
	  
	  for(int k=0; k < mapLin; k++) {
		  for(int j=0; j < mapCol; j++)
		  if(platforms[k][j] != null) {
			  game.shapeRenderer.setColor(platforms[k][j].color);
			  Platform plat = platforms[k][j];
			  game.shapeRenderer.rect(plat.rect.x, plat.rect.y, plat.rect.width, plat.rect.height);  
		  }
		  
	  }
	  for(int e=0; e < blocks.size(); e++) {
		  if(camera.zoom == 2) {
			  game.shapeRenderer.setColor(blocks.get(e).color);
			  Rectangle block = blocks.get(e).rect;
			  game.shapeRenderer.rect(block.x, block.y, block.width, block.height);
		  }
		  else if(camera.zoom == 1) {
			  game.shapeRenderer.setColor(blocks.get(e).color);
			  Rectangle block = blocks.get(e).rect;
			  game.shapeRenderer.rect(block.x + 310, block.y - 230, block.width / 2, block.height / 2);
		  }
		  
	  }
	  
	  if(selected != 99999) {
		  Vector3 vec = new Vector3((float)Gdx.input.getX(), (float)Gdx.input.getY(), 0);
    	  vec = camera.unproject(vec);
    	  mouseX = (int) vec.x;
    	  mouseY =(int) vec.y;
    	  if(mouseX > 0 && mouseX < 1240 && mouseY < 900 && mouseY > 20) {
    		  if(selected == 0) {
    			  game.shapeRenderer.setColor(1, 0, 0, 1);
        		  game.shapeRenderer.rect((colr * 50), 880 -(linr * 20), 50, 20);  
    		  }
    		  else if(selected == 1) {
    			  game.shapeRenderer.setColor(1, 0.8f, 0, 1);
        		  game.shapeRenderer.rect((colr * 50), 880 -(linr * 20), 25, 10);  
    		  }
    		  else if(selected == 2) {
    			  game.shapeRenderer.setColor(1, 0, 0.8f, 1);
        		  game.shapeRenderer.rect((colr * 50), 890 -(linr * 20), 25, 10);  
    		  }
    		  else if(selected == 999) {
    			  game.shapeRenderer.setColor(1, 1, 1, 0);
        		  game.shapeRenderer.rect((colr * 50), 880 -(linr * 20), 50, 20);
    		  }
    		  
    	  }
    	  else {
    		  if(selected == 0) {
    			  game.shapeRenderer.setColor(1, 0, 0, 1);
        		  game.shapeRenderer.rect(mouseX - 25, mouseY - 10 , 50, 20);
    		  }
    		  else if(selected == 1) {
    			  game.shapeRenderer.setColor(1, 0.8f, 0, 1);
        		  game.shapeRenderer.rect((colr * 50), 880 -(linr * 20), 25, 10);  
    		  }
    		  else if(selected == 2) {
    			  game.shapeRenderer.setColor(1, 0, 0.8f, 1);
        		  game.shapeRenderer.rect((colr * 50), 890 -(linr * 20), 25, 10);  
    		  }
    		  else if(selected == 999) {
    			  game.shapeRenderer.setColor(1, 1, 1, 10);
        		  game.shapeRenderer.rect(mouseX - 25, mouseY - 10 , 50, 20);
    		  }
    		  
    	  }
		  
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
