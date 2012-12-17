package rts.elements;

public class Building extends ControllableObject {
	private int l;
	private int w;
	
	public Building(double x, double y, int l, int w, int hp) {
		this.x = x;
		this.y = y;
		this.l = l;
		this.w = w;
		this.hp = hp;
	}
	
	public int getLength() {
		return l;
	}
	
	public int getWidth() {
		return w;
	}
}
