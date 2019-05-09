package soundandmusic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;

public class MusicPlayer extends Thread
{
    String caminho;
    Music music;
    
    
    public MusicPlayer(String pth){
        caminho = pth;  //Recebo o caminho da musica como argumento e armazeno na variavel caminho
    }
    
    public void run(){
       music = Gdx.audio.newMusic(Gdx.files.internal(caminho)); // Carrego o arquivo na variavel music
       music.setLooping(true); //Defino que a musica ira loopar
       if(!music.isPlaying()) {
    	   music.play(); // Se a musica nao estiver tocando, toque.
       } 
    }
    public void keysDown(int keyCode) { // Inputs para pauser, aumentar o volume ou diminuir e para despausar
    	if(keyCode == Input.Keys.P) {
        	if(!music.isPlaying()) {
        		music.play();
        	}
        	else {
        		music.pause();
        	}
        }
        else if(keyCode == Input.Keys.EQUALS) {
        	music.setVolume(music.getVolume() + 0.1f);
        }
        else if(keyCode == Input.Keys.MINUS) {
        	music.setVolume(music.getVolume() - 0.1f);
        }
    }
    public void stopMusic() { //para parar a musica totalmente.
    	music.stop();
    }
}