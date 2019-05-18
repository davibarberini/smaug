package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import soundandmusic.MusicPlayer;

public class ScoreScreen extends ScreenAdapter {

    MyGdxGame game;
    Texture robo = new Texture(Gdx.files.internal("Player/robo.png"));
    TextureRegion[][] roboSheet = TextureRegion.split(robo, 80, 80);
    TextureRegion death = new TextureRegion(roboSheet[1][3]);
    OrthographicCamera camera;
    float rotation = 0;
    float rotated = 0;

    public ScoreScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show(){
    	
    	
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	camera.update();
    	
    	
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
        
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    game.setScreen(new TitleScreen(game));
                }
                else if(keyCode == Input.Keys.ESCAPE) {
                	System.exit(0);
                }
                else if(keyCode == Input.Keys.C) {
                	game.setScreen(new ScoreScreen(game));
                }
                game.t1.keysDown(keyCode);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        rotation += 350 * Gdx.graphics.getDeltaTime();
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.titlefont.draw(game.batch, "Your Score", 129, 270);
        game.fontSmaller.draw(game.batch, "Press Space to go to the TitleScreen", 170, 70);
        game.batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
  	  
    }
}