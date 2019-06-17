package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import soundandmusic.MusicPlayer;

public class CreditsScene extends ScreenAdapter{

    MyGdxGame game;
    public Texture fundo = new Texture(Gdx.files.internal("Credits/fundo.png"));
    public Texture particulas = new Texture(Gdx.files.internal("Credits/particulas.png"));
    int rectCount = 0;
    int rectCount2 = 400;
    int countTransition = 0;
    OrthographicCamera camera;
    FillViewport view;
    int count = 0;
    int yFundo = 480;
    int Score = 10000;
    int yParticula = 480;
    float scale = 0.2f;
    float creditsVel = 150;
    float particulasVel = 200;
    
    public static boolean hasPassed = false;
    
    public CreditsScene(MyGdxGame game) {
        this.game = game;
        
        			
    }

    @Override	
    public void show(){
    	
		
    	if(!hasPassed) {
    		MyGdxGame.endTime = System.currentTimeMillis();
    		hasPassed = true;
    	} 
    	
    	if(Player.vida < 0) Player.vida = 10 ;
		double timePassed = MyGdxGame.endTime - MyGdxGame.initTime;
		//System.out.println((int)(Player.score * (Player.vida / 100)));
		Score = (int)(Score * (Player.vida / 100));
		Score -= (timePassed / 1000) * 5;
		Score += Player.swordKills * 200;
		Score += Player.cannonKills * 50;
    	
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
    	game.t1 = new MusicPlayer("Credits/music.ogg"); // Crio a thread passando o caminho da musica como argumento.
        game.t1.start(); 
        
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	camera.update();
    	
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keyCode) {
            	if (keyCode == Input.Keys.F || keyCode == Input.Keys.ENTER) {
                    game.setScreen(new TitleScreen(game));
                }
                else if(keyCode == Input.Keys.ESCAPE) {
                	game.setScreen(new TitleScreen(game));
                }
                else if(keyCode == Input.Keys.C) {
                	game.setScreen(new ScoreScreen(game));
                }
                else if(keyCode == Input.Keys.Z) {
                	creditsVel = 300;
                	particulasVel = 400;
                }
                game.t1.keysDown(keyCode); //Chama a função dos inputs da classe MusicPlayer
                return true;
            }
            public boolean keyUp(int keyCode) {
            	if(keyCode == Input.Keys.Z) {
                	creditsVel = 150;
                	particulasVel = 200;
                }
            	return true;
            }
        });
        
    }
    
    @Override
    public void render(float delta) {
    	if(game.untransition) {
    		draw();
			untransitionScene();
		}
    	else draw();
    }
    
    public void draw() {
    	yFundo -= creditsVel * Gdx.graphics.getDeltaTime();
    	yParticula -= particulasVel * Gdx.graphics.getDeltaTime();
    	game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        Color color = game.batch.getColor();//get current Color, you can't modify directly
        float oldAlpha = color.a;
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        if(yFundo < 0) {
        	game.fontSmaller.draw(game.batch, "Score: " + Score, 230, 240);
        	game.fontSmaller.draw(game.batch, "Press Enter to Return", 178, 70);
            game.fontSmaller.draw(game.batch, "Press C to", 35, 430);
            game.fontSmaller.draw(game.batch, "see score analysis", 35, 380);
        }
        game.batch.draw(fundo, -175, yFundo, fundo.getWidth(), fundo.getHeight());
        game.fontSmaller.draw(game.batch, "Press Z to speed up", 190, 34);
        color.a = oldAlpha*scale; //ex. scale = 0.5 will make alpha halved
		game.batch.setColor(color);
		
        game.batch.draw(particulas, 0, yParticula, 640, particulas.getHeight());
        color.a = oldAlpha;
    	game.batch.setColor(color);
        game.batch.end();
        
    }
    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
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
