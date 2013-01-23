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
	
	private int playerSize;
	private int l;
	private int w;
	
	private byte grid[][] = new byte[l][w];
	
	private Map(byte[][] grid, int playerSize) {
		this.grid = grid;
		this.playerSize = playerSize;
		l = grid[0].length;
		w = grid.length;
	}
	
	public int getLength() {
		return l;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getPlayerSize() {
		return playerSize;
	}
	
	public int checkPosition(int x, int y) {
		return grid[y][x];
	}
	
	// TODO number of spawn points needs to be greater than 1
	public static boolean validateMapFile(File in, boolean checkSpawn) throws IOException {
		DataInputStream data = new DataInputStream(new FileInputStream(in));
		
		int w, h, s, sInGrid = 0;
		
		// minimum length check
		if (in.length() < 9) {
			data.close();
			return false;
		}
		
		w = data.readInt();
		h = data.readInt();
		
		// dimension check
		if (w * h != data.available()) {
			data.close();
			return false;
		}
		
		if (checkSpawn) {
			s = data.readInt();
			
			while (data.available() > 0) {
				if (data.readByte() == Map.SPAWN) {
					++sInGrid;
				}
			}
			
			if (sInGrid != s) {
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
	
	public static void writeMap(byte[][] grid, File out) throws IOException {
		out.createNewFile();
		DataOutputStream fileOut = new DataOutputStream(new FileOutputStream(out));
		
		fileOut.writeInt(grid[0].length);
		fileOut.writeInt(grid.length);
		
		for (int i = 0; i < grid.length; ++i) {
			fileOut.write(grid[i]);
		}
		
		fileOut.close();
	}
	
	public static Map createMap(File in) throws IOException {
		DataInputStream fileIn = new DataInputStream(new FileInputStream(in));
		
		int w = fileIn.readInt();
		int h = fileIn.readInt();
		int playerSize = fileIn.readInt();
		byte[][] file = new byte[h][w];
		
		for (int i = 0; i < h; ++i) {
			fileIn.read(file[i]);
		}
		
		fileIn.close();
		
		Map map = new Map(file, playerSize);
		
		return map;
	}
}