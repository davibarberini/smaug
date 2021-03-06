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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import editor.MapFileReader;
import entities.Boss;
import entities.Parallax;
import entities.Player;
import entities.soldados.Soldado;
import entities.soldados.Policial;
import platforms.Platform;
import soundandmusic.MusicPlayer;


public class Level3 extends ScreenAdapter {
  public Player p1;
  public static int WIDTH;
  public static int HEIGHT;
  public Soldado[] soldados;
  
  
  public Boss boss;
  boolean bossFight = false;
  boolean bossTransition = false;
  int countBoss = 0;
  
  int rectCount = 0;
  int rectCount2 = 400;
  int countTransition = 0;
  
  MapFileReader mapReader;
  
  PauseScreen pause;
	
  public Platform [] platforms;
  
  Sound selectSound = Gdx.audio.newSound(Gdx.files.internal("TitleScreen/select.wav"));
  
  
  OrthographicCamera camera;
  FillViewport view;
  MyGdxGame game;
  
  Texture fundo, fundo2;
  Parallax prx1;
  Parallax prx2;
  Parallax prx3;

  public Level3(MyGdxGame game) {
	  this.game = game;
	  mapReader = new MapFileReader();
	  try {
		  System.out.println("Tamanho atual" + platforms.length);
		  for(int e=0; e < platforms.length; e++) {
			  platforms[e] = null;
			  
			
		  }
	  } catch(Exception e){
		  
	  }
	  platforms = mapReader.readMapToLevel("Level3/Level3", game, "TitleScreen");
	  

  }
  
  @Override
  public void show() {
	  MyGdxGame.actualLevel = "Level3";
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
	  game.t1 = new MusicPlayer("Level3/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
      game.t1.start(); 
	  
	  p1 = new Player(0, 0, 35, 35, 0, 0, 0, platforms);
	  prx1 = new Parallax("Parallax/parallax1.png", 24, 8);
	  prx2 = new Parallax("Parallax/parallax2.png", 50, 24);
	  prx3 = new Parallax("Parallax/parallax3.png", 150, 40);
	  fundo = new Texture("Level3/city.png");
	  fundo2 = new Texture("Level3/fundo.png");
	  
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
            	  game.transition = true;
              }
              else if(keyCode == Input.Keys.K) {
              	  camera.zoom = 2;
              }
              else if(keyCode == Input.Keys.R) {
            	  Boss.flyingSound.stop();
            	  Boss.negevSound.stop();
            	  Boss.raioSound.stop();
            	  Boss.tiroLaserSound.stop();
            	  Player.vida = 100;
            	  game.setScreen(new Level3(game));
              }
              else if (keyCode == Input.Keys.ENTER) {
            	  if(game.paused) {
            		  selectSound.play(0.2f);
            		  if(PauseScreen.selected == "return") {
            			  game.paused = false;
            			  p1.paused = false;
            			  if(bossFight) {
                			  camera.position.x = 2095;
                			  camera.position.y = 220;
                		  }
            		  } 
                	  else if(PauseScreen.selected == "mainmenu") {
                		  Boss.flyingSound.stop();
                    	  Boss.negevSound.stop();
                    	  Boss.raioSound.stop();
                    	  Boss.tiroLaserSound.stop();
                		  game.setScreen(new TitleScreen(game));  
                	  }
                	  else if(PauseScreen.selected == "exit") Gdx.app.exit();
            		  
            	  }
              }
              else if(keyCode == Input.Keys.ESCAPE) {
            	  if(game.paused) {
            		  Boss.flyingSound.stop();
                	  Boss.negevSound.stop();
                	  Boss.raioSound.stop();
                	  Boss.tiroLaserSound.stop();
            		  game.paused = false;
            		  p1.paused = false;
            		  if(bossFight) {
            			  camera.position.x = 2095;
            			  camera.position.y = 220;
            		  }
            	  }
            	  else {
            		  p1.paused = true;
            		  game.paused = true;
            	  }
              }
              else if(keyCode == Input.Keys.O) {
            	  p1.rect.x = 2000;
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
		if(!bossTransition) p1.rect.y += p1.gravity * delta;
		if(boss.tankCollision) {
			boss.tPlatform.platCollisionY(p1.gravity, p1);
		}
		for(int k=0; k < platforms.length; k++) {     //Colisao ap�s a movimenta��o Y
			  if(platforms[k] != null) {
				  Platform plat = platforms[k];
				  plat.platCollisionY(p1.gravity, p1);
			  }  
		}
		
		if(!bossTransition) {
			p1.rect.x += p1.velX * delta;
			
		} else p1.animState = "parado";
		if(boss.tankCollision) {
			boss.tPlatform.platCollisionX(p1.velX, p1);
			if(p1.tiro.rect.overlaps(boss.tPlatform.rect) && !p1.tiro.toDie && p1.tiro.isAlive) {
				p1.tiro.tiroExplosionSound.play(0.5f);
				p1.tiro.count = 0;
				p1.tiro.toDie = true;
				p1.tiro.stateTime = 0;
			}
		}
		for(int k=0; k < platforms.length; k++) {   //Colisao ap�s a movimenta��o X
			  if(platforms[k] != null) {
				  Platform plat = platforms[k];
				  plat.platCollisionX(p1.velX, p1);
			  }
			  
		}
		p1.update(game);
		boss.update();
	    if(camera.position.x > 2095 && !bossFight) {
	    	bossFight = true;
	    	bossTransition = true;
	    	camera.position.x = 2095;
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
	    	game.t1 = new MusicPlayer("Level3/bossMusic.mp3"); // Crio a thread passando o caminho da musica como argumento.
	        game.t1.start(); 
	    }
	    if(bossFight) {
	    	if(p1.rect.x < 1770) {
	    		p1.rect.x = 1770;
	    	}
	    }
	    else camera.position.set(p1.rect.x + (p1.rect.width / 2) + 100 ,  220, 0);
	    
	    if(bossTransition) {
	    	if(countBoss < 30) {
	    		camera.zoom -= 0.01f;
	    		camera.position.x += 10;
	    		camera.position.y += 5;
	    	}
	    	else if (countBoss > 90 && countBoss < 120){
	    		camera.zoom += 0.01f;
	    		camera.position.x -= 10;
	    		camera.position.y -= 5;
	    	}
	    	else if(countBoss > 120) {
	    		camera.zoom = 1;
	    		boss.alive = true;
	    		bossTransition = false;
	    		camera.position.x = 2095;
	    		camera.position.y = 220;
	    		p1.gravity = 0;
	    	}
	    	countBoss += 1;
	    	
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
	  //for(int e=0; e < soldados.length; e++) {
		  //soldados[e].update(game.batch);
	 // }
	  boss.draw(game.batch);
	  p1.draw(game.batch);
	  //game.batch.draw(p1.life, p1.rect.x - 300,  350, Player.vida, 30);
	  if(bossFight) {
		  p1.drawVida(game.batch, 1785, 410);
	  }else {
		  p1.drawVida(game.batch, p1.rect.x - 190, 410);
	  }
	  //game.batch.draw(idle,  p1.rect.x, p1.rect.y, 35, 35);
	  game.batch.end();
	 
	
  }
  
  public void createEnemies() {
	  soldados = new Soldado[1];
	  boss = new Boss(new Rectangle(2300, 300, 270, 100), p1, game);
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
	  game.shapeRenderer.rect(1760, -20, rectCount, rectCount);
	  game.shapeRenderer.rect(1760, 490, rectCount, -rectCount);
	  game.shapeRenderer.rect(2430, 490, -rectCount, -rectCount);
	  game.shapeRenderer.rect(2430, -20, -rectCount, rectCount);
	  rectCount += 10;
	  if(rectCount > 400) {
		  camera.position.set(0, 0, 0);
		  game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
		  game.t1.interrupt();
		  game.untransition = true;
		  game.transition = false;
		  game.setScreen(new CutScene(game, "Final"));
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
		  game.font.draw(game.batch, "Level 3", p1.rect.x + 20, 250);
		  game.batch.end();
	  }
  }
  
  
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
