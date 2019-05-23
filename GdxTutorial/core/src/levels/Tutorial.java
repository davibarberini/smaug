package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import editor.MapFileReader;
import entities.CientistaDePancada;
import entities.Player;
import entities.cientistas.Cientista;
import platforms.Platform;
import soundandmusic.MusicPlayer;

public class Tutorial extends ScreenAdapter {
	  public Player p1;
	  public static int WIDTH;
	  public static int HEIGHT;
	  
	  public int reviveCount = 0;
	  int cameraX = 138;
	  boolean cameraTransition = false;
	  
	  public Cientista cientista;
	  CientistaDePancada saco;
	  
	  FillViewport view;
	  
	  MapFileReader mapReader;
		
	  public Platform [] platforms;
	  
	  OrthographicCamera camera;
	  MyGdxGame game;
	  
	  Texture fundo;
	  
	  public Tutorial(MyGdxGame game) {
		  this.game = game;
		  mapReader = new MapFileReader();
		  try {
			  System.out.println("Tamanho atual" + platforms.length);
			  for(int e=0; e < platforms.length; e++) {
				  platforms[e] = null;
				  
				
			  }
		  } catch(Exception e){
			  
		  }
		  
		  platforms = mapReader.readMapToLevel("Tutorial/Tutorial", game, "TitleScreen");
		  

	  }
	  
	  public void show() {
		  //Parando a thread anterior se existir.
		  if(game.t1 != null && game.t1.isAlive()) {
	  		game.t1.toStop = true;
	  		try {
				game.t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  	  }
		  
		  
		  //Iniciando a thread da musica
		  game.t1 = new MusicPlayer("Level1/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
	      game.t1.start(); 
	      
		  p1 = new Player(0, 0, 35, 35, 0.0, 0.0, 0.0, platforms);
		  saco = new CientistaDePancada(830, 45f, 40, 80, p1);
		  //cientista = new Cientista(270, 55, 32, 32, 50, 0, 100, p1);
		  fundo = new Texture("Tutorial/fundo.png");
		  
		  WIDTH = Gdx.graphics.getWidth();
		  HEIGHT = Gdx.graphics.getHeight();
		  
		  p1.rect.x = 50;
		  p1.rect.y = 50;
		   
		  camera = new OrthographicCamera(WIDTH, HEIGHT);
		  camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
		  view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		  camera.update();
		  
		  Gdx.input.setInputProcessor(new InputAdapter() {
	          @Override
	          public boolean keyDown(int keyCode) {
	              if(keyCode == Input.Keys.L) {
	              }
	              else if(keyCode == Input.Keys.R) {
	            	  Player.vida = 100;
	            	  game.setScreen(new Tutorial(game));
	              }
	              else if(keyCode == Input.Keys.ESCAPE) {
	            	  game.setScreen(new TitleScreen(game));
	              }
	              p1.keyDown(keyCode);
	              game.t1.keysDown(keyCode);
	              return true;
	          }
	          public boolean keyUp(int keyCode) {
	        	  p1.keyUp(keyCode);
	        	  return true;
	          }
	      });
		  
	  }
	  
	  public void render(float delta) {
			game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
			
			p1.rect.y += p1.gravity * delta;
			for(int k=0; k < platforms.length; k++) {     //Colisao após a movimentação Y
				  if(platforms[k] != null) {
					  Platform plat = platforms[k];
					  plat.platCollisionY(p1.gravity, p1);
				  }  
			}
			
			
			p1.rect.x += p1.velX * delta;
			for(int k=0; k < platforms.length; k++) {   //Colisao após a movimentação X
				  if(platforms[k] != null) {
					  Platform plat = platforms[k];
					  plat.platCollisionX(p1.velX, p1);
				  }
				  
			}
			p1.update(game);
			saco.update();
			
			if(cientista != null) {
				if(!cientista.isAlive) {
					reviveCount += 1;
					if(reviveCount > 20) {
						cientista = new Cientista(270, 55, 25, 35, 50, 0, 100, p1);
						reviveCount = 0;
					}
				}
			}
			
			camera.position.set(cameraX, 150, 0);
			camera.update();
			if(p1.rect.x > 450) {
				cameraTransition = true;
			}
			else if(p1.rect.x < 450) {
				cameraTransition = false;
			}
			if(cameraTransition && cameraX < 600) {
				cameraX += 10;
			}
			else if(!cameraTransition && cameraX > 138) {
				cameraX -= 10;
			}
			this.draw();
		  
		  }
	  
	  public void draw() {
		  Gdx.gl.glClearColor(0, 0, 1, 1);
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		  
		  //saco.drawTest(game.shapeRenderer, camera);
		  
		  game.batch.setProjectionMatrix(camera.combined);
		  game.batch.begin();
		  game.batch.draw(fundo, -259, -329, fundo.getWidth() - 235, fundo.getHeight() - 235);
		  //if(cientista != null) cientista.update(game.batch);
		  saco.draw(game.batch);
		  p1.draw(game.batch);
		  game.batch.end();
		  
	  }
	  
	  
}
