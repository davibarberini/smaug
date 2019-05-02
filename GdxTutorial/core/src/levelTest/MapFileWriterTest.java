package levelTest;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.mygdx.game.Platform;

public class MapFileWriterTest {
	public PrintWriter writer;
	
	public MapFileWriterTest() {}
	
	public void writeMap(List<Platform> platforms, String mapName) {
		
		 
		try
		{
			writer = new PrintWriter(mapName + ".txt");
			for (int i = 0; i<platforms.size(); i++){
	        	writer.print(platforms.get(i).rect + "x");
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
