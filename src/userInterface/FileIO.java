package userInterface;

import gameEngine.Data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Nemo Li
 * 
 */
public class FileIO {

	BufferedReader in;
	
	/**
	 * @return array of terrain data
	 */
	public int[] readTerrainFile(){
		try {
			FileInputStream fstream = new FileInputStream(Data.FILE_TERRAIN);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine = null;
			int count = 0;
			int[] terrain = new int[Data.P_COUNT];
			
			while ((strLine = br.readLine()) != null){
				fillTerrainGrid(strLine, terrain, count);
				count = count + Data.DIMENSION;
			}
			
			br.close();
			
			return terrain;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println("terrain file not found");
		}
		return null;
	}
	
	

	/**
	 * @param line		- current string
	 * @param terrain	- destination array
	 * @param count		- offset to where stopped last line
	 * 
	 * converts each line into individual integers and save them from count
	 * in array terrain
	 */
	private void fillTerrainGrid(String line, int[] terrain, int count) {
		for (int i = 0; i < Data.DIMENSION; i++){
			terrain[i+count] = Integer.parseInt(line.substring(i, i+1));
		}
	}

	
	
	/**
	 * @param terrain - array to be saves
	 * 
	 * saves an array of terrain data to file
	 */
	public void saveTerrainToFile(int[] terrain) {
		FileWriter outFile;
		try {
			outFile = new FileWriter(Data.FILE_TERRAIN);
		
			PrintWriter out = new PrintWriter(outFile);
			for (int i = 0; i < terrain.length; i++) {
				out.print(terrain[i]);
				if (i%Data.DIMENSION == Data.DIMENSION-1)
					out.print("\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
