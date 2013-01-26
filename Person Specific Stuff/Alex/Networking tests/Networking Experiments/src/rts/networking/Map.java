package rts.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Map {
	public static final byte UNBLOCKED = 0;
	public static final byte BLOCKED = 1;
	public static final byte MINERAL = 2;
	public static final byte SPAWN = 3;
	
	private int spawnSites;
	private int l;
	private int w;
	
	private byte grid[][] = new byte[l][w];
	
	private Map(byte[][] grid, int spawnSites) {
		this.grid = grid;
		this.spawnSites = spawnSites;
		l = grid[0].length;
		w = grid.length;
	}
	
	public int getLength() {
		return l;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getSpawnSites() {
		return spawnSites;
	}
	
	public int checkPosition(int x, int y) {
		return grid[y][x];
	}
	
	// TODO number of spawn points needs to be greater than 1
	public static boolean validateMapFile(File in, boolean checkSpawn) throws IOException {
		DataInputStream data = new DataInputStream(new FileInputStream(in));
		
		int w = data.readInt();
		int h = data.readInt();
		int spawn = data.readByte();
		int actualSpawns = 0;
		
		// minimum length check
		if (in.length() < 9) {
			data.close();
			return false;
		}
		
		// dimension check
		if (w * h != data.available()) {
			// TODO fails here?
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