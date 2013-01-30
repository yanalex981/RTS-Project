package rts.elements;

/**
 * The base class for buildings
 * 
 * @author Alex
 */
public class Building extends ControllableObject {
	/**
	 * length of the building
	 */
	private int l;
	
	/**
	 * width of the building
	 */
	private int w;
	
	public Building(int id, float x, float y, int l, int w, int hp) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.l = l;
		this.w = w;
		this.hp = hp;
	}
	
	/**
	 * gets the length of the building
	 * 
	 * @return length
	 */
	public int getLength() {
		return l;
	}
	
	/**
	 * gets the width of the building
	 * 
	 * @return width
	 */
	public int getWidth() {
		return w;
	}
}
