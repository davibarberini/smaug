package levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.MyGdxGame;

import platforms.NextLevel;
import platforms.Platform;
import platforms.Teleporter;
import platforms.Teleporter2;
 
 
public class MapFileReader {
	public ArrayList<Platform> platforms = new ArrayList<Platform>();
	public Platform[] platformsReturn;
	public float[] tempRect = new float[4];
	public int count = 0;
	public FileReader fr;
	public String numToString = "";
	public boolean passed = false;
	public int platformType;
	public float xTel, yTel;
	public boolean passedOne = false;
	public Teleporter tel;
	public Teleporter2 tel2;
	NextLevel nl;
	
	public MapFileReader() {}
	
    public Platform[] readMapToLevel(String levelName, MyGdxGame game, String nextLevel) {
    	
    	read(levelName, game, nextLevel);
        
    	platformsReturn = new Platform[platforms.size()];
        for(int e=0; e < platforms.size(); e++) {
        	platformsReturn[e] = platforms.get(e);
        }
        return platformsReturn;
     
    }
    public ArrayList<Platform> readMapToEditor(String levelName, MyGdxGame game, String nextLevel) {
    	
    	read(levelName, game, nextLevel);
       
        return platforms;
    }
    
    public void read(String levelName, MyGdxGame gameF, String nextLevel) {
    	try {
            fr = new FileReader(levelName + ".txt");
            int i;
        	while ((i=fr.read()) != -1) {
        		if((char) i != '[' && (char) i != ']'&& (char) i != '[' && (char) i != ',' && passed) {
        			numToString += (char)i;
        		} else {
        			if((char) i == '[') {
        				passed = true;
        			}
        			if(!passed) {
        				String temp = ""; 
        				temp += (char) i;
        				platformType = Integer.parseInt(temp);
        				//System.out.println("Platform Type = " + platformType);
        				temp = "";
        			}
        			if(count == 4) {
        				//System.out.println("Passou : " + platformType);
        				if(platformType == 0) {
        					Color color = new Color(1, 0, 0, 1);
        					platforms.add(new Platform(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 0, color));
            				
        				}
        				else if(platformType == 4) {
        					Color color = new Color(0, 1, 0, 1);
        					tel = new Teleporter(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 4, color);
        					platforms.add(tel);
        				}
        				else if(platformType == 5) {
        					Color color = new Color(1, 1, 0, 1);
        					tel2 = new Teleporter2(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 5, color);
        					tel2.posX = tel.rect.x;
        					tel2.posY = tel.rect.y;
        					platforms.add(tel2);
        				}
        				else if(platformType == 9) {
        					Color color = new Color(0, 0, 1, 1);
        					nl = new NextLevel(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 9, color);
        					nl.game = gameF;
        					nl.nextLevel = nextLevel;
        					platforms.add(new NextLevel(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 9, color));
        				}
        				count = 0;
        				
        			}
        			if((char) i == ',') {
        				//System.out.println(numToString + "  " + count);
        				tempRect[count] = (float)Float.parseFloat(numToString);
        				count += 1;
        				numToString = "";
        				//System.out.println("Count: " + count + "Passed: " + passed );
        			}
        			else if((char) i == ']') {
        				tempRect[count] = (float)Float.parseFloat(numToString);
        				count += 1;
        				numToString = "";
        				passed = false;
        				//System.out.println("Count: " + count + "Passed: " + passed );
        			}
        		}
        	}
        } 
       
        
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}