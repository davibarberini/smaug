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
    public Texture[] fundo;
    public String whichCutScene = "Level1";
    int rectCount = 0;
    int rectCount2 = 400;
    int countTransition = 0;
    OrthographicCamera camera;
    FillViewport view;
    int count = 0;
    int maxCount = 0;
    int e;

    public CutScene(MyGdxGame game, String wCS) {
        this.game = game;
        this.whichCutScene = wCS;
        if(whichCutScene == "Level1") {
        	fundo = new Texture[]{
        			new Texture("CutScene/CutScene1/cut1.png"),
        			new Texture("CutScene/CutScene1/cut2.png"),
        			new Texture("CutScene/CutScene1/cut3.png"),
        			new Texture("CutScene/CutScene1/cut4.png"),
        			new Texture("CutScene/CutScene1/cut5.png"),
        			new Texture("CutScene/CutScene1/cut6.png"),
        			new Texture("CutScene/CutScene1/cut7.png"),
        			new Texture("CutScene/CutScene1/cut8.png")
        			};
        	maxCount = 800;
        }
        if(whichCutScene == "Level2") {
        	fundo = new Texture[]{new Texture("CutScene/CutScene2/cut1.png")};
        	maxCount = 300;
        }
        if(whichCutScene == "Level3") {
        	fundo = new Texture[]{
        			new Texture("CutScene/CutScene3/cut1.png"),
        			new Texture("CutScene/CutScene3/cut2.png"),
        			new Texture("CutScene/CutScene3/cut3.png"),
        			new Texture("CutScene/CutScene3/cut4.png")
        			};
        	maxCount = 400;
        }
        if(whichCutScene == "Final") {
        	fundo = new Texture[]{
        			new Texture("CutScene/CutScene4/cut1.png"),
        			new Texture("CutScene/CutScene4/cut2.png"),
        			new Texture("CutScene/CutScene4/cut3.png")
        			};
        	maxCount = 540;
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
    	game.t1 = new MusicPlayer("CutScene/music.ogg"); // Crio a thread passando o caminho da musica como argumento.
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
    		if(whichCutScene == "Level1") {
    			game.transition = true;
    		}
    		else if(whichCutScene == "Level2") {
    			game.transition = true;
    		}
    		else if(whichCutScene == "Level3") {
    			game.transition = true;
    		}
    	}
    	game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
    	
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(whichCutScene != "Final") {
        	e = (int)(count / 100);
            if(e > fundo.length - 1) e = fundo.length - 1;
        }
        else {
        	e = (int)(count / 180);
            if(e > fundo.length - 1) e = fundo.length - 1;
        }
        
        
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(whichCutScene != "Final") {
        	game.shapeRenderer.rect(0, 0, 640, 100);
        }
        game.shapeRenderer.end();
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        if(whichCutScene != "Final") {
        	game.batch.draw(fundo[e], 0, 100, 640, 380);
        }
        else {
        	game.batch.draw(fundo[e], 0, 0, 640, 480);
        }
        
        if(whichCutScene == "Level1") {
        	game.scoreFont.draw(game.batch, "In a future modern world after the WW3 cientists created an A.I", 5, 90);
            game.scoreFont.draw(game.batch, "to correct all human problems, but after an update, the A.I", 5, 68);
            game.scoreFont.draw(game.batch, "concluded that the humans were the ones who caused the problems", 5, 46);
            game.scoreFont.draw(game.batch, "now he is going to anihilate all human problems: The Humans", 5, 24);
        }
        else if (whichCutScene == "Level2") {
        	game.scoreFont.draw(game.batch, "When you are leaving the laboratory you see a blueprint in the desk", 5, 90);
            game.scoreFont.draw(game.batch, "seems to be a blueprint of a cannon, interesting...", 5, 68);
        }
        else if (whichCutScene == "Level3") {
        	game.scoreFont.draw(game.batch, "Looks like someone is trying to kill you.", 5, 90);
            game.scoreFont.draw(game.batch, "guess you gotta a new target huh?", 5, 68);
        }
        
    	
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
		  if(whichCutScene == "Level1") {
			  game.setScreen(new Level1(game));
		  }
		  else if(whichCutScene == "Level2") {
			  game.setScreen(new Level2(game));
		  }
		  else if(whichCutScene == "Level3") {
			  game.setScreen(new Level3(game));
		  }
		  else if(whichCutScene == "Final") {
			  game.setScreen(new CreditsScene(game));
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
