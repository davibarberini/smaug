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

import soundandmusic.MusicPlayer;

public class CutScene extends ScreenAdapter{

    MyGdxGame game;
    public Texture fundo;
    public String witchCutScene = "Level1";
    int rectCount = 0;
    int rectCount2 = 400;
    int countTransition = 0;
    OrthographicCamera camera;
    FillViewport view;
    int count = 0;
    int maxCount = 0;

    public CutScene(MyGdxGame game, String wCS) {
        this.game = game;
        this.witchCutScene = wCS;
        if(witchCutScene == "Level1") {
        	fundo = new Texture("CutScene/cut1.png");
        	maxCount = 150;
        }
        if(witchCutScene == "Level2") {
        	fundo = new Texture("CutScene/cut2.png");
        	maxCount = 150;
        }
        if(witchCutScene == "Level3") {
        	fundo = new Texture("CutScene/cut3.png");
        	maxCount = 100;
        }
    }

    @Override	
    public void show(){
    	
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
        
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	camera.update();
    	
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.F || keyCode == Input.Keys.ENTER) {
                	game.transition = true;
                    return true;
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
    	count += 1;
    	if (count > maxCount) {
    		if(witchCutScene == "Level1") {
    			game.transition = true;
    		}
    		else if(witchCutScene == "Level2") {
    			game.transition = true;
    		}
    		else if(witchCutScene == "Level3") {
    			game.transition = true;
    		}
    	}
    	game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
    	
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
    	game.batch.draw(fundo, 0, 0, 640, 480);
    	
        game.batch.end();
    }
    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
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
		  if(witchCutScene == "Level1") {
			  game.setScreen(new Level1(game));
		  }
		  else if(witchCutScene == "Level2") {
			  game.setScreen(new Level2(game));
		  }
		  else if(witchCutScene == "Level3") {
			  game.setScreen(new Level3(game));
		  }
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

}
