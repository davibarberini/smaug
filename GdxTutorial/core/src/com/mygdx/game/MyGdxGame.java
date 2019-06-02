package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;

import levels.PauseScreen;
import levels.TitleScreen;
import soundandmusic.MusicPlayer;


public class MyGdxGame extends Game {
  public ShapeRenderer shapeRenderer;
  public SpriteBatch batch;
  public BitmapFont font, titlefont, fontSmaller, scoreFont;
  public MusicPlayer t1;
  public static double initTime, endTime;
  public boolean paused = false;
  public static String actualLevel = "Level1";
  public boolean transition = false;
  public boolean untransition = true;

  @Override
  public void create () {
	  batch = new SpriteBatch();
	  shapeRenderer = new ShapeRenderer();
	  font = new BitmapFont(Gdx.files.internal("Fonts/pixelfont.fnt"));
	  fontSmaller = new BitmapFont(Gdx.files.internal("Fonts/pixelfont.fnt"));
	  titlefont = new BitmapFont(Gdx.files.internal("Fonts/pixelfontgradient.fnt"));
	  scoreFont = new BitmapFont(Gdx.files.internal("Fonts/pixelfont.fnt"));
	  titlefont.getData().setScale(1.5f, 1.5f);
	  fontSmaller.getData().setScale(0.5f, 0.5f);
	  scoreFont.getData().setScale(0.3f, 0.3f);
	  setScreen(new TitleScreen(this));
  }
  
  public void resize(int width, int height, FillViewport viewport, OrthographicCamera camera) {
	  //viewport.update(width, height);
      //camera.update();
  }
  
 
  @Override
  public void dispose () {
    shapeRenderer.dispose();
    batch.dispose();
    font.dispose();
  }
    
}

//camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
//