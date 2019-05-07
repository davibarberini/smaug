package levels;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
//
import platforms.Platform;

public class MapFileWriter {
	public PrintWriter writer2;
	public File f;
	
	public MapFileWriter() {}
	
	public void writeMap(List<Platform> platforms, String mapName) {
			try {
				//System.out.println(platforms.size());
				PrintWriter writer = new PrintWriter(mapName + ".txt");
				writer.print("");
				writer.close();
				writer2 = new PrintWriter(mapName + ".txt");
				for (int i = 0; i<platforms.size(); i++){
					writer2.print(platforms.get(i).rect + String.valueOf(platforms.get(i).platformType));
				}
				writer2.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
		}
}
