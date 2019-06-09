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

import editor.MapEditor;
import entities.Player;
import soundandmusic.MusicPlayer;

public class TitleScreen extends ScreenAdapter{

    MyGdxGame game;
    Texture fundo, select, title;
    OrthographicCamera camera;
    FillViewport view;
    public String selected = "play";
    int rectCount = 0;
    int rectCount2 = 400;

    public TitleScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override	
    public void show(){
    	Player.swordKills = 0;
    	Player.cannonKills = 0;
    	Player.score = 10000;
    	EndScreen.hasPassed = false;
    	ScoreScreen.hasPassed = false;
    	game.untransition = true;
    	game.transition = false;
    	game.paused = false;
    	
    	//Parando a thread anterior
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
    	
    	
    	//Criando a thread da musica
    	game.t1 = new MusicPlayer("TitleScreen/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
        game.t1.start(); 
        
    	fundo = new Texture("TitleScreen/fundo.png");
    	select = new Texture("TitleScreen/icon.png");
    	title = new Texture("TitleScreen/offlife.png");
    	
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	camera.update();
    	
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.F || keyCode == Input.Keys.ENTER) {
                    if(selected == "play") {
                    	Player.score = 10000;
                    	Player.vida = 100;
                    	game.transition = true;
                    } 
                    else if(selected == "tutorial") game.setScreen(new Tutorial(game));
                    else if(selected == "exit") System.exit(1);
                    return true;
                }
                else if (keyCode == Input.Keys.X) {
                	game.setScreen(new MapEditor(game));
                	return true;
                }
                else if(keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
                	if(selected == "play") selected = "exit";
                	else if (selected == "tutorial") selected = "play";
                	else if (selected == "exit") selected = "tutorial";
                	return true;
                }
                else if(keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
                	if(selected == "play") selected = "tutorial";
                	else if (selected == "tutorial") selected = "exit";
                	else if (selected == "exit") selected = "play";
                	return true;
                }
                else if(keyCode == Input.Keys.ESCAPE) {
                	Gdx.app.exit();
                }
                game.t1.keysDown(keyCode); //Chama a função dos inputs da classe MusicPlayer
                return true;
            }
        });
        
    }
    
    @Override
    public void render(float delta) {
    	if(!game.transition) {
	    	if(game.untransition) {
	    		draw();
				untransitionScene();
			}
	    	else draw();
		}
	    else {
			draw();
			transitionScene();
		}
    	
    }
    
    public void draw() {
    	game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
    	
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
    	game.batch.draw(fundo, 0, 0, 640, 480);
    	
    	if(selected == "play") game.batch.draw(select, 130, 270, 40, 40);
        else if(selected == "tutorial") game.batch.draw(select, 130, 170, 40, 40);
        else if(selected == "exit") game.batch.draw(select, 130, 70, 40, 40);
    	
    	game.titlefont.draw(game.batch, "OFF-LIFE", 170, 430);
    	//game.batch.draw(title, 155, 345);
    	game.font.draw(game.batch, "Play Game", 190, 310);
    	game.font.draw(game.batch, "Tutorial", 190, 210);
    	game.font.draw(game.batch, "Exit", 190, 110);
    	
        game.batch.end();
    }
    
    public void transitionScene() {
    	  game.shapeRenderer.setProjectionMatrix(camera.combined);
    	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    	  game.shapeRenderer.setColor(0, 0, 0, 1);
    	  game.shapeRenderer.rect(0, 0, rectCount, rectCount);
    	  game.shapeRenderer.rect(0, 510, rectCount, -rectCount);
    	  game.shapeRenderer.rect(640, 510, -rectCount, -rectCount);
    	  game.shapeRenderer.rect(640, 0, -rectCount, rectCount);
    	  rectCount += 10;
    	  if(rectCount > 400) {
    		  camera.position.set(0, 0, 0);
    		  game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
    		  game.t1.interrupt();
    		  game.untransition = true;
    		  game.transition = false;
  			  game.setScreen(new CutScene(game, "Level3"));
    	  }
    	  game.shapeRenderer.end();
    	  
    }
    public void untransitionScene() {
    	  game.shapeRenderer.setProjectionMatrix(camera.combined);
    	  game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    	  game.shapeRenderer.setColor(0, 0, 0, 1);
    	  game.shapeRenderer.rect(0, 0, rectCount2, rectCount2);
    	  game.shapeRenderer.rect(0, 510, rectCount2, -rectCount2);
    	  game.shapeRenderer.rect(640, 510, -rectCount2, -rectCount2);
    	  game.shapeRenderer.rect(640, 0, -rectCount2, rectCount2);
	  	  rectCount2 -= 10;
	  	  if(rectCount2 < 0) {
	  		  game.untransition = false;
	  	  }
	  	  game.shapeRenderer.end();
	}
    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

}
