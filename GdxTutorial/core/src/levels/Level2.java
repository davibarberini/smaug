package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import editor.MapFileReader;
import entities.Parallax;
import entities.Player;
import entities.soldados.Soldado;
import entities.soldados.Policial;
import platforms.Platform;
import soundandmusic.MusicPlayer;


public class Level2 extends ScreenAdapter {
  public Player p1;
  public static int WIDTH;
  public static int HEIGHT;
  public boolean fixCamera = false;
  public Soldado[] soldados;
  int rectCount = 0;
  int rectCount2 = 400;
  int countTransition = 0;
  
  TextureRegion[] aguaTXT = new TextureRegion[] {
  		new TextureRegion(new Texture("Level2/AGUA1.png")),
  		new TextureRegion(new Texture("Level2/AGUA2.png")),
  		new TextureRegion(new Texture("Level2/AGUA3.png")),
  		new TextureRegion(new Texture("Level2/AGUA4.png"))};
  
  MapFileReader mapReader;
  
  PauseScreen pause;
	
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
	  mapReader = new MapFileReader();
	  try {
		  System.out.println("Tamanho atual" + platforms.length);
		  for(int e=0; e < platforms.length; e++) {
			  platforms[e] = null;
			  
			
		  }
	  } catch(Exception e){
		  
	  }
	  platforms = mapReader.readMapToLevel("Level2/Level2", game, "Level3");
	  
	  

  }
  
  @Override
  public void show() {
	  MyGdxGame.actualLevel = "Level2";
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
	  
	  //Iniciando a nova thread da musica.
	  game.t1 = new MusicPlayer("Level2/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
      game.t1.start(); 
	  
	  p1 = new Player(0, 0, 35, 35, 0, 0, 0, platforms);
	  prx1 = new Parallax("Parallax/parallax1.png", 24, 8);
	  prx2 = new Parallax("Parallax/parallax2.png", 50, 24);
	  prx3 = new Parallax("Parallax/parallax3.png", 150, 40);
	  fundo = new Texture("Level2/city.png");
	  fundo2 = new Texture("Level2/fundo.png");
	  
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  p1.rect.x = 50;
	  p1.rect.y = 50;
	  createEnemies();
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
	  view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	  camera.update();
	  pause = new PauseScreen(game, camera);
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.L) {
            	  camera.position.set(0, 0, 0);
                  game.setScreen(new Level3(game));
              }
              else if(keyCode == Input.Keys.K) {
              	  camera.zoom = 2;
              }
              else if(keyCode == Input.Keys.R) {
            	  Player.vida = 100;
            	  game.setScreen(new Level2(game));
              }
              else if (keyCode == Input.Keys.ENTER) {
            	  if(PauseScreen.selected == "return") game.paused = false;
            	  else if(PauseScreen.selected == "mainmenu") game.setScreen(new TitleScreen(game));
            	  else if(PauseScreen.selected == "exit") Gdx.app.exit();
              }
              else if(keyCode == Input.Keys.ESCAPE) {
            	  if(game.paused) {
            		  game.paused = false;
            		  p1.paused = false;
            	  }
            	  else {
            		  p1.paused = true;
            		  game.paused = true;
            	  }
              }
              else if(keyCode == Input.Keys.Q) {
            	  game.setScreen(new EndScreen(game));
              }
              p1.keyDown(keyCode);
              game.t1.keysDown(keyCode);
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
	  if(!game.transition) {
			if(!game.paused) {
				if(game.untransition) {
					updateUnpaused(delta);
					untransitionScene();
				} 
				else updateUnpaused(delta);
			}
			else {
				pause.update();
			}
			
	}else {
		this.drawUnpaused();
		transitionScene();
		
	}
	
  }
  
  public void updateUnpaused(float delta) {
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
		
	    camera.position.set(p1.rect.x + (p1.rect.width / 2) + 100 ,  220, 0);
	    if(camera.position.x > 2300) {
	    	camera.position.x = 2300;		
	    }
		//camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
		camera.update();
		
		this.drawUnpaused();
	  
  }
  public void drawUnpaused() {
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
	  game.batch.draw(fundo, -225 , -180);
	  for(int e=0; e < soldados.length; e++) {
		  soldados[e].update(game.batch);
	  }
	  p1.draw(game.batch);
	  //game.batch.draw(p1.life, p1.rect.x - 200,  400, Player.vida, 30);
	  if(camera.position.x == 2300) {
		  p1.drawVida(game.batch, 1990, 410);
	  }else {
		  p1.drawVida(game.batch, p1.rect.x - 190, 410);
	  }
	  //game.batch.draw(idle,  p1.rect.x, p1.rect.y, 35, 35);
	  game.batch.end();
	 
	
  }
  
  public void createEnemies() {
	  soldados = new Soldado[1];
	  soldados[0] = new Policial(370, 27, 25, 35, 5, 10, 0, p1, platforms);
  }
  
  public void dispose() {
	  Gdx.input.setInputProcessor(null);
	  game.shapeRenderer.setProjectionMatrix(null);
	  this.game.shapeRenderer.dispose();
	  this.game.batch.dispose();
	  
  }
  
  public void transitionScene() {
	  game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  game.shapeRenderer.setColor(0, 0, 0, 1);
	  game.shapeRenderer.rect(p1.rect.x - 320, p1.rect.y - 230, rectCount, rectCount);
	  game.shapeRenderer.rect(p1.rect.x - 320, p1.rect.y + 280, rectCount, -rectCount);
	  game.shapeRenderer.rect(p1.rect.x + 350, p1.rect.y + 280, -rectCount, -rectCount);
	  game.shapeRenderer.rect(p1.rect.x + 350, p1.rect.y - 230, -rectCount, rectCount);
	  rectCount += 10;
	  if(rectCount > 400) {
		  camera.position.set(0, 0, 0);
		  game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
		  game.t1.interrupt();
		  game.untransition = true;
		  game.transition = false;
		  game.setScreen(new Level3(game));
	  }
	  game.shapeRenderer.end();
  }
  public void untransitionScene() {
	  game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  game.shapeRenderer.setColor(0, 0, 0, 1);
	  game.shapeRenderer.rect(p1.rect.x - 230, -20, rectCount2, rectCount2);
	  game.shapeRenderer.rect(p1.rect.x - 230, 490, rectCount2, -rectCount2);
	  game.shapeRenderer.rect(p1.rect.x + 440, 490, -rectCount2, -rectCount2);
	  game.shapeRenderer.rect(p1.rect.x + 440, -20, -rectCount2, rectCount2);
	  if(countTransition > 100) {
		  rectCount2 -= 10;
		  if(rectCount2 < 0) {
			  game.untransition = false;
		  }
		  game.shapeRenderer.end();
	  }
	  else {
		  game.shapeRenderer.end();
		  game.batch.begin();
		  countTransition += 1;
		  game.font.draw(game.batch, "Level 2", p1.rect.x + 20, 250);
		  game.batch.end();
	  }
  }
  
  
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
