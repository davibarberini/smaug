package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.MyGdxGame;

import entities.Player;
import soundandmusic.MusicPlayer;

public class ScoreScreen extends ScreenAdapter {

    MyGdxGame game;
    OrthographicCamera camera;
    public int yourScore;
    public int[] scores;
    public int[] fa;
    public int[] menor;
    public int[] maior;
    int colNum = 8;
    float linNum;
    float valorLin;
    int linAmplitude = 30;
    int valorX = 130;
    int valorY = 100;
    int timesCount = 0;
    int altitude = 0;
    int altitudeAtual;
    int escala = 2;
    int higherScore = 0;
    int countPosition = 1;
    float lowerScore, highestFa;
    public static boolean hasPassed = false;
    public ScoreScreen(MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show(){
    	if(!hasPassed) {
    		double timePassed = MyGdxGame.endTime - MyGdxGame.initTime;
        	Player.score -= (timePassed / 1000) * 5;
        	Player.score += Player.swordKills * 200;
        	Player.score += Player.cannonKills * 50;
        	if(Player.vida < 0) Player.vida = 10 ;
        	Player.score = (int)(Player.score * (Player.vida / 100));
        	hasPassed = true;
    	}
    	yourScore = Player.score;
    	
    	scores = new int[]{ 330, 450, 129, 600, 2000, 900, 200, 550, 340, 770, 802, 340, 175, 400, 700, 800, yourScore};
    	fa = new int[colNum];
    	menor = new int[colNum];
    	maior = new int[colNum];
    	for(int e=0; e < scores.length; e++) {
    		if(scores[e] > higherScore) higherScore = scores[e];
    	}
    	
    	lowerScore = higherScore;
    	for(int e=0; e < scores.length; e++) {
    		if(scores[e] < lowerScore) lowerScore = scores[e];
    	}
    	
    	altitude = higherScore / colNum;
    	maior[0] = altitude;
    	menor[0] = altitudeAtual;
    	altitudeAtual = 0;
    	highestFa = 0;
    	int maxAltitude = 0;
    	for(int j=1; j < colNum + 1; j++) {
    		maxAltitude = altitude * j;
    		if(j == colNum) maxAltitude = higherScore;
    		for(int i=0; i < scores.length; i++) {
    			if(scores[i] > altitudeAtual && scores[i] <= maxAltitude) {
    				timesCount += 1;
    			}
        	}
    		if(timesCount > highestFa) highestFa = timesCount;
    		fa[(j - 1)] = timesCount;
  //  		System.out.println("Entre " + altitudeAtual + " e " + (maxAltitude) + " contém " + fa[(j - 1)]);
    		altitudeAtual = maxAltitude;
    		timesCount = 0;
    		if(j < colNum) {
    			maior[j] = maxAltitude;
            	menor[j] = altitudeAtual;
    		}
    		
    	}
    	
    	
    	linNum = 3;
//    	System.out.println(highestFa / linNum);
    	if(highestFa % linNum != 0) valorLin = ((highestFa + 1) / linNum);
    	else valorLin = highestFa / linNum;
//    	System.out.println(valorLin);
    	valorLin = (int)(valorLin + 0.6);
    	Math.round(valorLin);
//    	System.out.println(valorLin);
    	
    	camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	
    	camera.position.set(0 + (Gdx.graphics.getWidth() / 2), 0 + (Gdx.graphics.getHeight() / 2), 0);
    	camera.update();
    	
    	for(int e=0; e < scores.length; e++) {
    		if(scores[e] > yourScore) countPosition += 1;
    	}
    	
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
                else if (keyCode == Input.Keys.C) {
                    game.setScreen(new EndScreen(game));
                }
                else if(keyCode == Input.Keys.ESCAPE) {
                	System.exit(0);
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
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
        game.fontSmaller.draw(game.batch, "Your Score: " + String.valueOf(yourScore), 30, 430);
        
        //Posição em que o jogador ficou
        if(countPosition == 1) game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "st", 30, 400);
        else if(countPosition == 2) game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "nd", 30, 400);
        else if(countPosition == 3) game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "rd", 30, 400);
        else game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "th", 30, 400);
//        else if(countPosition < )
        
        //Numeros para identificação das colunas
        for(int e=0; e < colNum; e++) {
        	if(menor[e] / 100 > 0) game.scoreFont.draw(game.batch, String.valueOf(menor[e]), ((valorX - 28) / escala) + ((32 * escala) * e), valorY - 10);
        	else game.scoreFont.draw(game.batch, String.valueOf(menor[e]), ((valorX - 15) / escala) + ((32 * escala) * e), valorY - 10);
        }
        for(int e=0; e < linNum + 1; e++) {
        	game.scoreFont.draw(game.batch, String.valueOf(e * valorLin), ((valorX - 70) / escala), (valorY + 12) + (e * 57));
        }
        game.scoreFont.draw(game.batch, String.valueOf(higherScore), ((valorX - 15) / escala) + ((32 * escala) * colNum), valorY - 10);
        game.batch.end();
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        //Colunas vermelhas e verdes
        for(int e=0; e < colNum; e++) {
        	if(e == colNum - 1) {
        		if(yourScore > menor[e] && yourScore <= higherScore ) game.shapeRenderer.setColor(0, 1, 0, 1);
        		else game.shapeRenderer.setColor(1, 0, 0, 1);
        	}
        	else {
        		if(yourScore > menor[e] && yourScore <= menor[e + 1]) game.shapeRenderer.setColor(0, 1, 0, 1);
        		else game.shapeRenderer.setColor(1, 0, 0, 1);
        	}
        	game.shapeRenderer.rect((valorX / escala) + ((32 * escala) * e), valorY, (30 * escala), (fa[e] * (linAmplitude * escala)) / valorLin);
        }
        
        //Linhas em branco para mostrar o grafico
        game.shapeRenderer.setColor(1, 1, 1, 1);
        game.shapeRenderer.rect((valorX / escala), valorY, ((32 * escala) * colNum) - 3, 1);
        game.shapeRenderer.rect((valorX / escala), valorY, 1, 200);
        game.shapeRenderer.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
  	  
    }
}