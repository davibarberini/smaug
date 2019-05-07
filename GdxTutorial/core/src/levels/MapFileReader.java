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
	public ArrayList<Platform> platforms = new ArrayList<Platform>();
	public Platform[] platformsReturn;
	public float[] tempRect = new float[4];
	public int count = 0;
	public FileReader fr;
	public String numToString = "";
	public boolean passed = false;
	public int platformType;
	public Teleporter lastTeleporter, teleporterAtual;
	public boolean passedOne = false;
	
	public MapFileReader() {}
	
    public Platform[] readMapToLevel(String levelName) {
    	
    	read(levelName);
        
    	platformsReturn = new Platform[platforms.size()];
        for(int e=0; e < platforms.size(); e++) {
        	platformsReturn[e] = platforms.get(e);
        }
        return platformsReturn;
     
    }
    public ArrayList<Platform> readMapToEditor(String levelName) {
    	
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
        					teleporterAtual = new Teleporter(tempRect[0], tempRect[1], tempRect[2], tempRect[3], 4, color);
        					if(passedOne) {
        						//System.out.println("Passou");
        						teleporterAtual.posX = lastTeleporter.rect.x;
        						teleporterAtual.posX = lastTeleporter.rect.y;
        					}
        					else passedOne = true;
        					platforms.add(teleporterAtual);
        					lastTeleporter = teleporterAtual;
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