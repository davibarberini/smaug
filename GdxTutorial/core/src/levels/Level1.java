package levels;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import editor.MapFileReader;
import entities.Player;
import entities.cientistas.Cientista;
import entities.cientistas.CientistaAtirador;
import entities.cientistas.CientistaBurstFire;
import entities.cientistas.CientistaEscudo;
import entities.cientistas.CientistaRicochete;
import platforms.Platform;
import soundandmusic.MusicPlayer;


public class Level1 extends ScreenAdapter{
  public Player p1;
  public static int WIDTH;
  public static int HEIGHT;
  boolean runningThread = false;
  
  public Cientista[] cientistas;
  float passedTime = 0;
  int rodouNoMain = 1;
  int countMain = 0;
  int countThread = 0;
  int rectCount = 0;
  int rectCount2 = 400;
  int countTransition = 0;

  Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("TitleScreen/select.wav"));
  
  FillViewport view;
  
  MapFileReader mapReader;
  
  PauseScreen pause;
	
  public Platform [] platforms;
  
  OrthographicCamera camera;
  MyGdxGame game;
  
  Texture fundo, portaElevador, vidro;

  public Level1(MyGdxGame game) {
	  this.game = game;
	  mapReader = new MapFileReader();
	  try {
		  System.out.println("Tamanho atual" + platforms.length);
		  for(int e=0; e < platforms.length; e++) {
			  platforms[e] = null;
			  
			
		  }
	  } catch(Exception e){
		  
	  }
	  
	  platforms = mapReader.readMapToLevel("Level1/Level1", game, "Level2");
	  

  }
  
  @Override
  public void show() {
	  MyGdxGame.initTime = System.currentTimeMillis();
	  MyGdxGame.actualLevel = "Level1";
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
	  
	  
	  //Iniciando a thread da musica
	  game.t1 = new MusicPlayer("Level1/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
      game.t1.start(); 
      
	  p1 = new Player(0, 0, 35, 35, 0.0, 0.0, 0.0, platforms);
	  fundo = new Texture("Level1/lab.png");
	  portaElevador = new Texture("Level1/portaelevador.png");
	  vidro = new Texture("Level1/vidro.png");
	  
	  createEnemies();
	  WIDTH = Gdx.graphics.getWidth();
	  HEIGHT = Gdx.graphics.getHeight();
	  
	  p1.rect.x = 50;
	  p1.rect.y = 50;
	    
	  camera = new OrthographicCamera(WIDTH, HEIGHT);
	  camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
	  view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	  camera.update();
	  pause = new PauseScreen(game, camera);
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.L) {
            	  game.transition = true;
              }
              else if(keyCode == Input.Keys.K) {
              	camera.zoom = 2;
              }
              else if(keyCode == Input.Keys.R) {
            	  Player.vida = 100;
            	  game.setScreen(new Level1(game));
              }
              else if (keyCode == Input.Keys.ENTER) {
            	  if(game.paused) {
            		  selectSound.play(0.2f);
            		  if(PauseScreen.selected == "return") {
            			  game.paused = false;
            			  p1.paused = false;
            		  }
                	  else if(PauseScreen.selected == "mainmenu") {
                		  dispose();
                		  game.setScreen(new TitleScreen(game));  
                	  }
                	  else if(PauseScreen.selected == "exit") Gdx.app.exit();
            		  
            	  }
            	  
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
		//System.out.println(Gdx.graphics.getDeltaTime() + "  :  " + valor);
		p1.rect.y += (p1.gravity * Gdx.graphics.getDeltaTime());
		if(p1.rect.overlaps(cientistas[3].getEscudo().rect) && cientistas[3].getEscudo().isAlive ) {
			cientistas[3].getEscudo().platCollisionY(p1.gravity, p1);
		}
		for(int k=0; k < platforms.length; k++) {     //Colisao ap�s a movimenta��o Y
			  if(platforms[k] != null) {
				  Platform plat = platforms[k];
				  plat.platCollisionY(p1.gravity, p1);
			  }  
		}
		p1.rect.x += (p1.velX * Gdx.graphics.getDeltaTime());
		
		if(cientistas[3].getEscudo().isAlive) {
			cientistas[3].getEscudo().platCollisionX(p1.velX, p1);
		}
		for(int k=0; k < platforms.length; k++) {   //Colisao ap�s a movimenta��o X
			  if(platforms[k] != null) {
				  Platform plat = platforms[k];
				  plat.platCollisionX(p1.velX, p1);
			  }
			  
		}

	  	game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
		
		//if(p1.rect.overlaps()
		p1.update(game);
		
		camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
		camera.update();
		
		this.drawUnpaused();
	  
  }
  public void drawUnpaused() {
	  Gdx.gl.glClearColor(0, 0, 1, 1);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	  /*
	  game.shapeRenderer.setProjectionMatrix(camera.combined);
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
	  game.batch.draw(fundo, -288 , -210);
	  //game.batch.draw(portaElevador, -129 , 100);
	  for(int e=0; e < cientistas.length; e++) {
		  cientistas[e].update(game.batch);
	  }
	  p1.draw(game.batch);
	  game.batch.draw(vidro, 52 , 718);
	  //game.batch.draw(p1.life, p1.rect.x - 300, p1.rect.y + 200, Player.vida, 30);
	  p1.drawVida(game.batch, p1.rect.x - 290, p1.rect.y + 210);
	  //game.batch.draw(idle,  p1.rect.x, p1.rect.y, 35, 35);
	  game.batch.end();
	  /*game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  game.shapeRenderer.setColor(0, 1, 0, 1);
	  //game.shapeRenderer.rect(p1.rect.x, p1.rect.y, p1.rect.width + p1.widthLimit, p1.rect.height);
	  game.shapeRenderer.end();
	  game.batch.begin();
	  game.batch.end()*/;
  }
  
  public void createEnemies() {
	  cientistas = new Cientista[5];
	  cientistas[0] = new Cientista(170, 40, 25, 35, 100, 100, 0, p1);
	  cientistas[1] = new CientistaAtirador(450, 180, 25, 35, 50, 0, 200, p1, platforms);
	  cientistas[2] = new CientistaRicochete(900, 585, 25, 35, 30, 0, 100, p1, platforms);
	  cientistas[3] = new CientistaEscudo(600, 320, 25, 35, 70, 0, 200, p1);
	  cientistas[4] = new CientistaBurstFire(800, 320, 25, 35, 70, 0, 200, p1, platforms);
  }
  
  public void dispose() {
	  //Gdx.input.setInputProcessor(null);
	  //this.game.shapeRenderer.dispose();
	  //this.game.batch.dispose();
	  p1.runningThread = false;
	  p1.thread.interrupt();
	  /*for(int e=0; e < cientistas.length; e++) {
		  cientistas[e].runningThread = false;
		  cientistas[e].thread.interrupt();
	  }*/
	  
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
		  game.setScreen(new CutScene(game, "Level2"));
	  }
	  game.shapeRenderer.end();
  }
  public void untransitionScene() {
	  game.shapeRenderer.setProjectionMatrix(camera.combined);
	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	  game.shapeRenderer.setColor(0, 0, 0, 1);
	  game.shapeRenderer.rect(p1.rect.x - 320, p1.rect.y - 230, rectCount2, rectCount2);
	  game.shapeRenderer.rect(p1.rect.x - 320, p1.rect.y + 280, rectCount2, -rectCount2);
	  game.shapeRenderer.rect(p1.rect.x + 350, p1.rect.y + 280, -rectCount2, -rectCount2);
	  game.shapeRenderer.rect(p1.rect.x + 350, p1.rect.y - 230, -rectCount2, rectCount2);
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
		  game.font.draw(game.batch, "Level 1", p1.rect.x - 75, p1.rect.y + 45);
		  game.batch.end();
	  }
	  
  }
  
  
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
