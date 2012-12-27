package rts.networking;

public class Map {
	private int l;
	private int w;
	
	public Map() {
		
	}
	
	public int getLength() {
		return l;
	}
	
	public int getWidth() {
		return w;
	}
	
	public static Map readMap() {
		// NEED TO STANDARDIZE MAP FORMAT
		return new Map();
	}
}
