package levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
 
public class MapFileReader {
	public int[][] mapLevel;
	public FileReader fr;
	public String numToString = "";
	public int mapL, mapC;
	
	public MapFileReader(int mapLin, int mapCol) {
		 mapLevel = new int[mapLin][mapCol];
		 mapL = mapLin;
		 mapC = mapCol;
	}
    public int[][] readMap(String mapName) {
       
        try {
            fr = new FileReader(mapName + ".txt");
            int i, countL = 0, countC = 0;
        	while ((i=fr.read()) != -1) {
        		if((char) i == '0' || (char) i == '1' || (char) i == ',') {
        			//System.out.println((char) i);
        			//System.out.println(numToString);
            		if((char) i == ',') {
            			mapLevel[countL][countC] = Integer.parseInt(numToString);
            			numToString = "";
            			countC += 1;
                		if(countC == mapC) {
                			countL += 1;
                			countC = 0;
                		}
            		} else {
            			if((char) i == '0') {
            				numToString += "0";
            			}
            			else {
            				numToString += (char) i;
            			}
            		}
           	
        		}
        		
        	} 
        		
        } 
       
        
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapLevel;
 
    }
}