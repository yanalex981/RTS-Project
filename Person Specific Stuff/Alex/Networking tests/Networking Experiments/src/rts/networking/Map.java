package rts.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Map {
	/**
	 * Unblocked tile
	 */
	public static final byte UNBLOCKED = 0;
	
	/**
	 * Blocked tile
	 */
	public static final byte BLOCKED = 1;
	
	/**
	 * Resource tile
	 */
	public static final byte MINERAL = 2;
	
	/**
	 * Spawn tile
	 */
	public static final byte SPAWN = 3;
	
	/**
	 * Number of spawn sites, players that can play on this map
	 */
	private int spawnSites;
	
	/**
	 * length of the map
	 */
	private int l;
	
	/**
	 * width of the map
	 */
	private int w;
	
	/**
	 * Map grid. Byte[][] for easy writing
	 */
	private byte grid[][] = new byte[l][w];
	
	private Map(byte[][] grid, int spawnSites) {
		this.grid = grid;
		this.spawnSites = spawnSites;
		l = grid[0].length;
		w = grid.length;
	}
	
	/**
	 * gets the length of the map
	 * 
	 * @return	length of the map
	 */
	public int getLength() {
		return l;
	}
	
	/**
	 * gets the width of the map
	 * 
	 * @return	width of the map
	 */
	public int getWidth() {
		return w;
	}
	
	/**
	 * gets the spawn sites
	 * 
	 * @return	number of spawan sites
	 */
	public int getSpawnSites() {
		return spawnSites;
	}
	
	/**
	 * gets the type of tile at (x, y)
	 * 
	 * @param x	xth row on the map
	 * @param y	yth row on the map
	 * @return	
	 */
	public int checkPosition(int x, int y) {
		return grid[y][x];
	}
	
	/**
	 * Checks whether a map file is valid
	 * 
	 * @param in			Map file to be read
	 * @param checkSpawn	whether or not to check if the number of spawn points in the file matches up
	 * @return				true if file matches, false if not
	 * @throws IOException
	 */
	// TODO number of spawn points needs to be greater than 1
	public static boolean validateMapFile(File in, boolean checkSpawn) throws IOException {
		DataInputStream data = new DataInputStream(new FileInputStream(in));
		
		int w = data.readInt();			// width written in the file
		int h = data.readInt();			// height written in the file
		int spawn = data.readByte();	// spawn sites written to file
		int actualSpawns = 0;			// spawn sites in the grid
		
		// minimum length check
		if (in.length() < 9) {
			data.close();
			return false;
		}
		
		// dimension check
		if (w * h != data.available()) {
			data.close();
			return false;
		}
		
		if (checkSpawn) {
			while (data.available() > 0) {
				if (data.readByte() == Map.SPAWN) {
					++actualSpawns;
				}
			}
			
			if (actualSpawns != spawn) {
				data.close();
				return false;
			}
		}
		
		data.close();
		return true;
	}
	
	/**
	 * reads a map from a file
	 * 
	 * @param in	file to read
	 * @return		byte[][] of map
	 * @throws IOException
	 */
	public static byte[][] readMap(File in) throws IOException {
		DataInputStream dataIn = new DataInputStream(new FileInputStream(in));
		
		int w = dataIn.readInt();
		int h = dataIn.readInt();
		dataIn.skip(Integer.SIZE / 8);
		
		byte[][] grid = new byte[h][w];
		
		for (int i = 0; i < grid.length; ++i) {
			dataIn.read(grid[i]);
		}
		
		dataIn.close();
		
		return grid;
	}
	
	/**
	 * writes out a map as a binary file
	 * 
	 * @param grid		byte[][] of the map
	 * @param out		file to write to
	 * @param spawns	number of spawn points
	 * @throws IOException
	 */
	public static void writeMap(byte[][] grid, File out, int spawns) throws IOException {
		out.createNewFile();
		DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(out));
		
		fileOut.writeInt(grid[0].length);
		fileOut.writeInt(grid.length);
		fileOut.writeByte(spawns);
		
		for (int i = 0; i < grid.length; ++i) {
			fileOut.write(grid[i]);
		}
		
		fileOut.close();
	}
	
	/**
	 * Factory method to create a map from a file
	 * 
	 * @param in	file to read from
	 * @return		a Map object
	 * @throws IOException
	 */
	public static Map createMap(File in) throws IOException {
		DataInputStream fileIn = new DataInputStream(new FileInputStream(in));
		
		int w = fileIn.readInt();
		int h = fileIn.readInt();
		int spawnSites = fileIn.readByte();
		byte[][] file = new byte[h][w];
		
		for (int i = 0; i < h; ++i) {
			fileIn.read(file[i]);
		}
		
		fileIn.close();
		
		Map map = new Map(file, spawnSites);
		
		return map;
	}
}