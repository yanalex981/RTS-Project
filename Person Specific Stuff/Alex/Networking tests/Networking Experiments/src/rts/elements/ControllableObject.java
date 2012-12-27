package rts.elements;

public abstract class ControllableObject {
	protected int id;
	protected double x;
	protected double y;
	protected int hp;
	protected boolean underAttack = false;
	protected boolean destroyed = false;
	protected long timeDestroyed;
	
	public void takeDamage(int damage) {
		hp -= damage;
		
		if (hp <= 0) {
			destroyed = true;
			timeDestroyed = System.currentTimeMillis();
		}
	}
	
	public long getTimeDestroyed() {
		return timeDestroyed;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getHP() {
		return hp;
	}
	
	public boolean isUnderAttack() {
		return underAttack;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
}
