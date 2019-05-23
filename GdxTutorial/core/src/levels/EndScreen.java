package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;

import soundandmusic.MusicPlayer;

public class EndScreen extends ScreenAdapter {

    MyGdxGame game;
    OrthographicCamera camera;
    
    Texture robo = new Texture(Gdx.files.internal("Player/robo.png"));
    TextureRegion[][] roboSheet = TextureRegion.split(robo, 80, 80);
    TextureRegion death = new TextureRegion(roboSheet[6][0]);
    
    TextureRegion currentFrame;
    TextureRegion[] fundoTXT = new TextureRegion[] {
    		new TextureRegion(new Texture("EndScreen/dead1.png")),
    		new TextureRegion(new Texture("EndScreen/dead2.png")),
    		new TextureRegion(new Texture("EndScreen/dead3.png")),
    		new TextureRegion(new Texture("EndScreen/dead4.png")),
    		new TextureRegion(new Texture("EndScreen/dead5.png"))};
    Animation<TextureRegion> fundo;
    
    float rotation = 0;
    float rotation2 = 0;
    float rotated = 0;
    float stateTime = 0;
    
    public static boolean hasPassed = false;
    

    public EndScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show(){
    	
    	fundo = new Animation<TextureRegion>(0.1f, fundoTXT);
    	
    	
    	if(!hasPassed) {
    		MyGdxGame.endTime = System.currentTimeMillis();
    		hasPassed = true;
    	} 
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
                if (keyCode == Input.Keys.F || keyCode == Input.Keys.ENTER) {
                    game.setScreen(new TitleScreen(game));
                }
                else if(keyCode == Input.Keys.ESCAPE) {
                	game.setScreen(new TitleScreen(game));
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
    	rotation += 350 * Gdx.graphics.getDeltaTime();
    	rotation2 += 100 * Gdx.graphics.getDeltaTime();
    	
    	camera.update();
    	stateTime += Gdx.graphics.getDeltaTime();
    	this.draw();
    }
    
    public void draw() {
    	Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        currentFrame = fundo.getKeyFrame(stateTime, true);
        //game.batch.draw(currentFrame, 0, 0, 640, 480);
        
        game.batch.draw(currentFrame, -100, -180, (currentFrame.getRegionWidth() + 200) / 2, (currentFrame.getRegionHeight() + 200) / 2,
        		currentFrame.getRegionWidth() + 200, currentFrame.getRegionHeight() + 200, 1, 1, rotation2);
        
        game.titlefont.draw(game.batch, "You    Died", 115, 270);
        game.batch.draw(death, (Gdx.graphics.getWidth() / 2) - (death.getRegionWidth() / 2) - 15, (Gdx.graphics.getHeight() / 2) - (death.getRegionHeight() / 2) - 16,
        		55, 55, 110, 110, 1, 1, rotation);
        game.fontSmaller.draw(game.batch, "Press Enter to Return", 170, 70);
        game.fontSmaller.draw(game.batch, "Press C to", 35, 430);
        game.fontSmaller.draw(game.batch, "see score analysis", 35, 380);
        game.batch.end();
    }
    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
  	  
    }
}