package levelTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Platform;
 
 
public class MapFileReaderTest {
	public static List <Platform> platforms = new ArrayList<Platform>();
	public static Platform[] platformsReturn;
	public float[] tempRect = new float[4];
	public int count = 0;
	public FileReader fr;
	public String numToString = "";
	
	public MapFileReaderTest() {}
	
    public Platform[] readMapToLevel(String levelName) {
    	try {
            fr = new FileReader(levelName + ".txt");
            int i;
        	while ((i=fr.read()) != -1) {
        		if((char) i != 'x' && (char) i != '[' && (char) i != ']'&& (char) i != '[' && (char) i != ',') {
        			numToString += (char)i;
        		} else {
        			if((char) i == ',') {
        				System.out.println(numToString + "  " + count);
        				tempRect[count] = (float)Float.parseFloat(numToString);
        				count += 1;
        				numToString = "";
        			}
        			else if((char) i == ']') {
        				tempRect[count] = (float)Float.parseFloat(numToString);
        				count += 1;
        				numToString = "";
        			}
        			if(count == 4) {
        				Color color = new Color(1, 0, 0, 1);
        				platforms.add(new Platform(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 0, color));
        				count = 0;
        			}
        		}
        	}
        } 
       
        
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    	platformsReturn = new Platform[platforms.size()];
        for(int e=0; e < platforms.size(); e++) {
        	platformsReturn[e] = platforms.get(e);
        }
        return platformsReturn;
     
    }
    public List<Platform> readMapToEditor(String levelName) {
    	try {
            fr = new FileReader(levelName + ".txt");
            int i;
        	while ((i=fr.read()) != -1) {
        		if((char) i != 'x' && (char) i != '[' && (char) i != ']'&& (char) i != '[' && (char) i != ',') {
        			numToString += (char)i;
        		} else {
        			if((char) i == ',') {
        				System.out.println(numToString + "  " + count);
        				tempRect[count] = (float)Float.parseFloat(numToString);
        				count += 1;
        				numToString = "";
        			}
        			else if((char) i == ']') {
        				tempRect[count] = (float)Float.parseFloat(numToString);
        				count += 1;
        				numToString = "";
        			}
        			if(count == 4) {
        				Color color = new Color(1, 0, 0, 1);
        				platforms.add(new Platform(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 0, color));
        				count = 0;
        			}
        		}
        	}
        } 
       
        
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
       
        return platforms;
    }
}