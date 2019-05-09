package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import editor.MapEditor;
import soundandmusic.MusicPlayer;

public class TitleScreen extends ScreenAdapter{

    MyGdxGame game;
    Texture fundo, select;
    OrthographicCamera camera;
    FillViewport view;
    public String selected = "play";

    public TitleScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override	
    public void show(){
    	
    	//Criando a thread da musica
    	game.t1 = new MusicPlayer("TitleScreen/music.mp3"); // Crio a thread passando o caminho da musica como argumento.
        game.t1.start(); 
        
    	fundo = new Texture("TitleScreen/titlescreen.jpg");
    	select = new Texture("TitleScreen/icon.png");
    	
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	camera.update();
    	
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE || keyCode == Input.Keys.ENTER) {
                	game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
                	game.t1.interrupt();
                    if(selected == "play") game.setScreen(new Level1(game));
                    else if(selected == "editor") game.setScreen(new MapEditor(game));
                    else if(selected == "exit") System.exit(1);
                    return true;
                }
                else if (keyCode == Input.Keys.X) {
                	game.t1.stopMusic(); // Para parar a music e parar a thread quando troca de tela
                	game.t1.interrupt();
                	game.setScreen(new MapEditor(game));
                	return true;
                }
                else if(keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
                	if(selected == "play") selected = "exit";
                	else if (selected == "editor") selected = "play";
                	else if (selected == "exit") selected = "editor";
                	return true;
                }
                else if(keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
                	if(selected == "play") selected = "editor";
                	else if (selected == "editor") selected = "exit";
                	else if (selected == "exit") selected = "play";
                	return true;
                }
                else if(keyCode == Input.Keys.ESCAPE) {
                	System.exit(0);
                }
                game.t1.keysDown(keyCode); //Chama a função dos inputs da classe MusicPlayer
                return true;
            }
        });
        
    }
    
    @Override
    public void render(float delta) {
    	game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), view, camera);
    	
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
    	game.batch.draw(fundo, 0, 0, 640, 480);
    	
    	if(selected == "play") game.batch.draw(select, 130, 270, 40, 40);
        else if(selected == "editor") game.batch.draw(select, 130, 170, 40, 40);
        else if(selected == "exit") game.batch.draw(select, 130, 70, 40, 40);
    	
    	game.titlefont.draw(game.batch, "OFF-LIFE", 170, 430);
    	game.font.draw(game.batch, "Play Game", 190, 310);
    	game.font.draw(game.batch, "Map Editor", 190, 210);
    	game.font.draw(game.batch, "Exit", 190, 110);
    	
        game.batch.end();
		
    }
    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

}
