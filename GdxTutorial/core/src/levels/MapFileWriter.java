package levels;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import platforms.Platform;

public class MapFileWriter {
	public PrintWriter writer;
	
	public MapFileWriter() {}
	
	public void writeMap(List<Platform> platforms, String mapName) {
		
		 
		try
		{
			writer = new PrintWriter(mapName + ".txt");
			for (int i = 0; i<platforms.size(); i++){
	        	writer.print(platforms.get(i).rect + String.valueOf(platforms.get(i).platformType));
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
