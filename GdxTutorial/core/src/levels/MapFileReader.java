package levels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
 
public class MapFileReader {
	public int[][] mapLevel;
	public FileReader fr;
	public String numToString;
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
            String e;
        	while ((i=fr.read()) != -1) {
        		System.out.print((char) i);
        		if((char) i == ',') {
        			//if((char) i == null)
        			mapLevel[countL][countC] = Integer.parseInt(numToString);
        			countC += 1;
            		if(countC == mapC) {
            			countL += 1;
            		}
        		} else {
        			numToString += (char) i;
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