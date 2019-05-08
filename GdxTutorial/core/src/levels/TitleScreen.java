package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

public class TitleScreen extends ScreenAdapter {

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
    	fundo = new Texture("TitleScreen/titlescreen.jpg");
    	select = new Texture("TitleScreen/badlogic.jpg");
    	
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	view = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	camera.update();
    	
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    if(selected == "play") game.setScreen(new Level1(game));
                    else if(selected == "editor") game.setScreen(new MapEditor(game));
                    else if(selected == "exit") game.setScreen(new EndScreen(game));
                }
                else if (keyCode == Input.Keys.X) {
                	game.setScreen(new MapEditor(game));
                }
                else if(keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
                	if(selected == "play") selected = "exit";
                	else if (selected == "editor") selected = "play";
                	else if (selected == "exit") selected = "editor";
                }
                else if(keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
                	if(selected == "play") selected = "editor";
                	else if (selected == "editor") selected = "exit";
                	else if (selected == "exit") selected = "play";
                }
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
        if(Gdx.graphics.getWidth() != 640) {
        	game.batch.draw(fundo, 0, 60, 640, 350);
        	if(selected == "play") game.batch.draw(select, 175, 260, 40, 40);
            else if(selected == "editor") game.batch.draw(select, 175, 180, 40, 40);
            else if(selected == "exit") game.batch.draw(select, 175, 110, 40, 40);
        }
        else {
        	game.batch.draw(fundo, 0, 0);
        	if(selected == "play") game.batch.draw(select, 175, 280, 40, 40);
            else if(selected == "editor") game.batch.draw(select, 175, 180, 40, 40);
            else if(selected == "exit") game.batch.draw(select, 175, 80, 40, 40);
        }
        
        game.batch.end();
        
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
