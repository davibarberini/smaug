package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import soundandmusic.MusicPlayer;

public class SkinChange extends ScreenAdapter{

    MyGdxGame game;
    Texture fundo, seta;
    OrthographicCamera camera;
    FillViewport view;
    public String selected = "robo";
    int rectCount = 0;
    int rectCount2 = 400;
    int size = 120;
	int x1 = 325;
	int x2 = 205;
	int y = 150;
    
    float stateTime = 0;
    Texture robo = new Texture(Gdx.files.internal("Player/robo.png"));
    Texture roboSo = new Texture(Gdx.files.internal("Player/robospecialops.png"));
    Texture roboStark = new Texture(Gdx.files.internal("Player/robostark.png"));
    Texture roboLgbt = new Texture(Gdx.files.internal("Player/roboLgbt.png"));
    TextureRegion[][] roboSheet = TextureRegion.split(robo, 80, 80);
    TextureRegion[][] roboSoSheet = TextureRegion.split(roboSo, 80, 80);
    TextureRegion[][] roboStarkSheet = TextureRegion.split(roboStark, 80, 80);
    TextureRegion[][] roboLgbtSheet = TextureRegion.split(roboLgbt, 80, 80);
    Animation<TextureRegion> correndoAnim;
    Animation<TextureRegion> correndoSoAnim;
    Animation<TextureRegion> correndoStarkAnim;
    Animation<TextureRegion> correndoLgbtAnim;
    TextureRegion[] correndo = new TextureRegion[6];
    TextureRegion[] correndoSo = new TextureRegion[6];
    TextureRegion[] correndoStark = new TextureRegion[6];
    TextureRegion[] correndoLgbt = new TextureRegion[6];
    boolean rightTransition = false;
    boolean leftTransition = false;
    
    TextureRegion[] parado = new TextureRegion[1];
    TextureRegion[] paradoSo = new TextureRegion[1];
    TextureRegion[] paradoStark = new TextureRegion[1];
    TextureRegion[] paradoLgbt = new TextureRegion[1];
   
    TextureRegion currentFrame;
    float scale = 0.5f;
    
    public SkinChange(MyGdxGame game) {
        this.game = game;
        
        
        parado[0] = roboSheet[0][1];
        paradoSo[0] = roboSoSheet[0][1];
        paradoStark[0] = roboStarkSheet[0][1];
        paradoLgbt[0] = roboLgbtSheet[0][1];
        for(int e=0; e < 6; e++) {
			correndo[e] = roboSheet[0][e];
			correndoSo[e] = roboSoSheet[0][e];
			correndoStark[e] = roboStarkSheet[0][e];
			correndoLgbt[e] = roboLgbtSheet[0][e];
		}
        
        correndoAnim = new Animation<TextureRegion>(0.09f, correndo);
		correndoSoAnim = new Animation<TextureRegion>(0.09f, correndoSo);
		correndoStarkAnim = new Animation<TextureRegion>(0.09f, correndoStark);
		correndoLgbtAnim = new Animation<TextureRegion>(0.09f, correndoLgbt);

    }

    @Override	
    public void show(){
    	MyGdxGame.actualLevel = "skin";
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
    	game.t1 = new MusicPlayer("SkinChanger/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
        game.t1.start(); 
        
    	fundo = new Texture("Tutorial/fundo.png");
    	seta = new Texture("SkinChanger/seta.png");
    	
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	camera.update();
    	
    	
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.F || keyCode == Input.Keys.ENTER || keyCode == Input.Keys.ESCAPE) {
                    game.setScreen(new TitleScreen(game));
                    MyGdxGame.skinSelected = selected;
                    return true;
                }
                else if(keyCode == Input.Keys.D || keyCode == Input.Keys.RIGHT) {
                	if(selected == "robo") selected = "robospecialops";
                	else if (selected == "robospecialops") selected = "robostark";
                	else if (selected == "robostark") selected = "robolgbt";
                	else if (selected == "robolgbt") selected = "robo";
                	return true;
                }
                else if(keyCode == Input.Keys.A || keyCode == Input.Keys.LEFT) {
                	if(selected == "robo") selected = "robolgbt";
                	else if (selected == "robospecialops") selected = "robo";
                	else if (selected == "robostark") selected = "robospecialops";
                	else if (selected == "robolgbt") selected = "robostark";
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
        
        Color color = game.batch.getColor();//get current Color, you can't modify directly
        float oldAlpha = color.a;
        
    	game.batch.draw(fundo, -1702, -772, 2560, 1940);
    	
    	game.titlefont.draw(game.batch, "Skin Changer", 70, 400);
    	game.fontSmaller.draw(game.batch, "Press Enter to Choose", 180, 145);
    	game.fontSmaller.draw(game.batch, "Use WASD or Arrow Keys to Change", 105, 105);
    	
    	game.batch.draw(seta, 415, 179, 60, 60);
    	game.batch.draw(seta, 235, 179, -60, 60);
    	
    	stateTime += Gdx.graphics.getDeltaTime();
    	if(selected == "robo") currentFrame = correndoAnim.getKeyFrame(stateTime, true);
    	else if(selected == "robospecialops") currentFrame = correndoSoAnim.getKeyFrame(stateTime, true);
    	else if(selected == "robostark") currentFrame = correndoStarkAnim.getKeyFrame(stateTime, true);
    	else if(selected == "robolgbt") currentFrame = correndoLgbtAnim.getKeyFrame(stateTime, true);
		game.batch.draw(currentFrame, 245, 130, 160, 160);
		
		color.a = oldAlpha*scale; //ex. scale = 0.5 will make alpha halved
		game.batch.setColor(color);
		if(selected == "robo") {
			game.batch.draw(paradoSo[0], x1, y, size, size);
			game.batch.draw(paradoLgbt[0], x2, y, size, size);
		}
		else if(selected == "robospecialops") {
			game.batch.draw(paradoStark[0], x1, y, size, size);
			game.batch.draw(parado[0], x2, y, size, size);
		}
		else if(selected == "robostark") {
			game.batch.draw(paradoLgbt[0], x1, y, size, size);
			game.batch.draw(paradoSo[0], x2, y, size, size);
		}
		else if(selected == "robolgbt") {
			game.batch.draw(parado[0], x1, y, size, size);
			game.batch.draw(paradoStark[0], x2, y, size, size);
		}
    	color.a = oldAlpha;
    	game.batch.setColor(color);
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
