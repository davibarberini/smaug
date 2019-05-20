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

import editor.MapFileReader;
import entities.Parallax;
import entities.Player;
import entities.cientistas.Cientista;
import entities.soldados.Soldado;
import entities.soldados.SoldadoAtirador;
import platforms.Platform;
import soundandmusic.MusicPlayer;


public class Level3 extends ScreenAdapter {
  public Player p1;
  public static int WIDTH;
  public static int HEIGHT;
  public Soldado[] soldados;
  
  MapFileReader mapReader;
	
  public Platform [] platforms;
  
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
	  prx1 = new Parallax("Level3/parallax1.png", 0, 8);
	  prx2 = new Parallax("Level3/parallax2.png", 50, 24);
	  prx3 = new Parallax("Level3/parallax3.png", 150, 40);
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
	  
	  Gdx.input.setInputProcessor(new InputAdapter() {
          @Override
          public boolean keyDown(int keyCode) {
              if (keyCode == Input.Keys.L) {
            	  camera.position.set(0, 0, 0);
                  game.setScreen(new TitleScreen(game));
              }
              else if(keyCode == Input.Keys.K) {
              	  camera.zoom = 2;
              }
              else if(keyCode == Input.Keys.R) {
            	  Player.vida = 100;
            	  game.setScreen(new Level3(game));
              }
              else if(keyCode == Input.Keys.ESCAPE) {
            	  game.setScreen(new TitleScreen(game));
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
	//camera.position.set(p1.rect.x + (p1.rect.width / 2), p1.rect.y  + (p1.rect.width / 2), 0);
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
	  game.batch.draw(fundo, -225 , -180);
	  for(int e=0; e < soldados.length; e++) {
		  soldados[e].update(game.batch);
	  }
	  p1.draw(game.batch);
	  game.batch.draw(p1.life, p1.rect.x - 300,  350, Player.vida, 30);
	  //game.batch.draw(idle,  p1.rect.x, p1.rect.y, 35, 35);
	  game.batch.end();
	 
	  
  }
  
  public void createEnemies() {
	  soldados = new Soldado[1];
	  soldados[0] = new SoldadoAtirador(370, 27, 25, 35, 5, 10, 0, p1, platforms);
  }
  
  public void dispose() {
	  Gdx.input.setInputProcessor(null);
	  game.shapeRenderer.setProjectionMatrix(null);
	  this.game.shapeRenderer.dispose();
	  this.game.batch.dispose();
	  
  }
  
  
}
	

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
