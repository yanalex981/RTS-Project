package rts.elements;

import rts.networking.Player;

public abstract class ControllableObject {
	protected Player owner;
	protected int id;
	protected float x;
	protected float y;
	protected int hp;
	
	public static final int DESTROYED = 0;
	
	protected int state;
	protected long timeDestroyed;
	
	public void takeDamage(int damage) {
		hp -= damage;
		
		if (hp <= 0) {
			timeDestroyed = System.currentTimeMillis();
		}
	}
	
	public long getTimeDestroyed() {
		return timeDestroyed;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public int getHP() {
		return hp;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
}
