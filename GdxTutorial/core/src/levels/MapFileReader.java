package levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

import platforms.Platform;
import platforms.Teleporter;
 
 
public class MapFileReader {
	public static List <Platform> platforms = new ArrayList<Platform>();
	public static Platform[] platformsReturn;
	public float[] tempRect = new float[4];
	public int count = 0;
	public FileReader fr;
	public String numToString = "";
	public boolean passed = false;
	public int platformType;
	
	public MapFileReader() {}
	
    public Platform[] readMapToLevel(String levelName) {
    	
    	read(levelName);
        
    	platformsReturn = new Platform[platforms.size()];
        for(int e=0; e < platforms.size(); e++) {
        	platformsReturn[e] = platforms.get(e);
        }
        return platformsReturn;
     
    }
    public List<Platform> readMapToEditor(String levelName) {
    	
        read(levelName);
       
        return platforms;
    }
    
    public void read(String levelName) {
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
        				System.out.println(platformType);
        				temp = "";
        			}
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
        				passed = false;
        			}
        			if(count == 4) {
        				if(platformType == 0) {
        					Color color = new Color(1, 0, 0, 1);
        					platforms.add(new Platform(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 0, color));
            				
        				}
        				else if(platformType == 4) {
        					Color color = new Color(0, 1, 0, 1);
        					platforms.add(new Teleporter(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 4, color));
        				}
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
    }
}