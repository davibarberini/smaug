package levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;

import bancodedados.BancoDerby;
import entities.Player;
import soundandmusic.MusicPlayer;

public class ScoreScreen extends ScreenAdapter {

    MyGdxGame game;
    OrthographicCamera camera;
    public int yourScore;
    public ArrayList<Integer> scores = new ArrayList<Integer>();
    public float[] fa;
    public float[] faAttack;
    public int[] menor;
    public int[] maior;
    int highAttackFa = 0;
    int colNum = 8;
    float linNum;
    float valorLin, valorAttackLin;
    int linAmplitude = 30;
    int valorX = 130;
    int valorY = 100;
    String nomePlayer = "Davi";
    int timesCount = 0;
    int altitude = 0;
    boolean cameraTransition = false;
    int altitudeAtual;
    int escala = 2;
    int higherScore = 0;
    int countPosition = 1;
    float frPorcento;
    float lowerScore, highestFa;
    boolean conectado = false;
    public static boolean hasPassed = false;
    BancoDerby banco = new BancoDerby();
    
    public ScoreScreen(MyGdxGame game) {
        this.game = game;
		banco.connect();
    }

    @Override
    public void show(){
    	
    	if(!hasPassed) {
    		if(Player.vida < 0) Player.vida = 10 ;
    		double timePassed = MyGdxGame.endTime - MyGdxGame.initTime;
    		//System.out.println((int)(Player.score * (Player.vida / 100)));
    		Player.score = (int)(Player.score * (Player.vida / 100));
        	Player.score -= (timePassed / 1000) * 5;
        	Player.score += Player.swordKills * 200;
        	Player.score += Player.cannonKills * 50;
    	}
    	yourScore = Player.score;
    	
    	if(!hasPassed) {
    		if(banco.isConnected()) banco.insertPlayer(nomePlayer , yourScore);
    		hasPassed = true;
    	}
    	if(banco.isConnected()) {
    		conectado = true;
			banco.listPlayers(scores);
		} else conectado = false;
    	
    	fa = new float[colNum];
    	menor = new int[colNum];
    	maior = new int[colNum];
    	for(int e=0; e < scores.size(); e++) {
    		if(scores.get(e) > higherScore) higherScore = scores.get(e);
    	}
    	
    	lowerScore = higherScore;
    	for(int e=0; e < scores.size(); e++) {
    		if(scores.get(e) < lowerScore) lowerScore = scores.get(e);
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
    		for(int i=0; i < scores.size(); i++) {
    			if(scores.get(i) > altitudeAtual && scores.get(i) <= maxAltitude) {
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
    	
    	 for(int e=0; e < colNum; e++) {
          	if(e == colNum - 1) {
          		if(yourScore > menor[e] && yourScore <= higherScore ) {
          			frPorcento = (fa[e] / scores.size()) * 100;
          		}
          	}
          	else {
          		if(yourScore > menor[e] && yourScore <= menor[e + 1]) {
          			//System.out.println(fa[e]);
          			frPorcento = (fa[e] / scores.size()) * 100;
          		} 
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
    	
    	for(int e=0; e < scores.size(); e++) {
    		if(scores.get(e) > yourScore) countPosition += 1;
    	}
    	
    	faAttack = new float[]{Player.attack1Kills, Player.attack2Kills, Player.attack3Kills, Player.airAttackKills, Player.cannonKills};
    	if(Player.attack1Kills > highAttackFa) highAttackFa = Player.attack1Kills;
    	if(Player.attack2Kills > highAttackFa) highAttackFa = Player.attack2Kills;
    	if(Player.attack3Kills > highAttackFa) highAttackFa = Player.attack3Kills;
    	if(Player.airAttackKills > highAttackFa) highAttackFa = Player.airAttackKills;
    	if(Player.cannonKills > highAttackFa) highAttackFa = Player.cannonKills;
    	if(highAttackFa % linNum != 0) valorAttackLin = ((highAttackFa + 1) / linNum);
    	else valorAttackLin = highAttackFa / linNum;
//    	System.out.println(valorLin);
    	valorAttackLin = (int)(valorAttackLin + 0.6);
    	Math.round(valorAttackLin);
    	
    	
    	
    	
    	
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
                if (keyCode == Input.Keys.F || keyCode == Input.Keys.ENTER || keyCode == Input.Keys.C) {
                    game.setScreen(new TitleScreen(game));
                }
                else if(keyCode == Input.Keys.ESCAPE) {
                	game.setScreen(new TitleScreen(game));
                }
                else if(keyCode == Input.Keys.R) {
                	banco.resetAllScores();
                }
                else if(keyCode == Input.Keys.RIGHT) {
                	cameraTransition = true;
                }
                else if(keyCode == Input.Keys.LEFT) {
                	cameraTransition = false;
                }
                game.t1.keysDown(keyCode);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
    	if(conectado) this.draw();
    	else this.noConectionDraw();
    	if(cameraTransition && camera.position.x < 1000) {
    		camera.position.x += 20;
    	}
    	else if(!cameraTransition && camera.position.x > 320) {
    		camera.position.x -= 20;
    	}
        camera.update();
        
    }
    
    public void noConectionDraw() {
    	Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.fontSmaller.draw(game.batch, "Your Score: " + String.valueOf(yourScore), 30, 430);
        game.fontSmaller.draw(game.batch, "Could'nt connect to the Database", 90, 250);
        game.batch.end();
        
        
	}

	public void draw() {
    	 Gdx.gl.glClearColor(0, 0, 0, 1);
         Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
         drawScore();
         drawAttackScore();
    }
	
	public void drawScore() {
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
        
        game.fontSmaller.draw(game.batch, "Your Score: " + String.valueOf(yourScore), 30, 430);
        
        //Posição em que o jogador ficou
        if(countPosition == 1) game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "st", 30, 400);
        else if(countPosition == 2) game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "nd", 30, 400);
        else if(countPosition == 3) game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "rd", 30, 400);
        else game.fontSmaller.draw(game.batch, "Your Rank: " + String.valueOf(countPosition) + "th", 30, 400);
        game.fontSmaller.draw(game.batch, "Fr/100: " + String.valueOf(frPorcento), 30, 370);
        
        
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
        	float y = (fa[e] * (linAmplitude * escala)) / valorLin;
        	game.shapeRenderer.rect((valorX / escala) + ((32 * escala) * e), valorY, (30 * escala), y);
        }
        //Linhas em branco para mostrar o grafico
        game.shapeRenderer.setColor(1, 1, 1, 1);
        game.shapeRenderer.rect((valorX / escala), valorY, ((32 * escala) * colNum) - 3, 1);
        game.shapeRenderer.rect((valorX / escala), valorY, 1, 200);
        game.shapeRenderer.end();
        game.batch.begin();
        for(int e=0; e < colNum; e++) {
       	 int plusX = 20;
       	 if(fa[e] / 1 < 10) plusX = 24;
       	 else if(fa[e] / 10 < 10) plusX = 20;
       	 else if(fa[e] / 100 < 10) plusX = 10;
       	 if(fa[e] > 0) game.scoreFont.draw(game.batch, String.valueOf((int)fa[e]), (valorX / escala) + ((32 * escala) * e) + plusX, valorY + ((fa[e] * (linAmplitude * escala)) / valorLin)  + 14);
        }
        game.batch.end();
   
	}
	
	public void drawAttackScore() {
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
        
		for(int e=0; e < linNum + 1; e++) {
        	game.scoreFont.draw(game.batch, String.valueOf(e * valorAttackLin), (valorX / escala) + 665, (valorY + 12) + (e * 57));
        }
		
		String attackName = "";
        //Nome dos attacks
        for(int e=0; e < 5; e++) {
        	if(e == 0) attackName = "Atk1";
        	if(e == 1) attackName = "Atk2";
        	if(e == 2) attackName = "Atk3";
        	if(e == 3) attackName = "AirAtk";
        	if(e == 4) attackName = "Canhao";
        	game.scoreFont.draw(game.batch, attackName, ((valorX - 15) / escala) + ((49 * escala) * e) + 740, valorY - 10);
        }
		
		game.batch.end();
		
		//Linhas em branco para mostrar o grafico
		game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0, 0, 1, 1);
        //Colunas vermelhas e verdes
        for(int e=0; e < 5; e++) {
        	float y = (faAttack[e] * (linAmplitude * escala)) / valorAttackLin;
        	game.shapeRenderer.rect(((valorX / escala) + ((50 * escala) * e)) + 702, valorY, (48 * escala), y);
        }
        
        game.shapeRenderer.setColor(1, 1, 1, 1);
        game.shapeRenderer.rect((valorX / escala) + 700, valorY, ((50 * escala) * 5) - 3, 1);
        game.shapeRenderer.rect((valorX / escala) + 700, valorY, 1, 200);
        game.shapeRenderer.end();
	}	
	
    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
  	  
    }
}