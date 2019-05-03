package levelWithMatrix;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MapFileWriterMatrix {
	
	public int[][] mapObjects;
	public PrintWriter writer;
	
	public MapFileWriterMatrix(int mapLin, int mapCol) {
		mapObjects = new int[mapLin][mapCol];
	}
	
	public void writeMap(int[][] levelMap, String mapName) {
		
		 
		try
		{
			writer = new PrintWriter(mapName + ".txt");
			for (int i = 0; i<levelMap.length; i++){
	        	for(int j = 0; j<levelMap[i].length; j++){
	        	writer.print(levelMap[i][j] + ",");
	        	      }
	        	writer.println();
			}	
		}
		catch (FileNotFoundException e){
			System.out.println("Error: " + e.getMessage());			
		} finally {
			try{if (writer!=null) writer.close(); } 
            catch (Exception e) {System.out.println("Could not close writer");}
	    }
	}
}
