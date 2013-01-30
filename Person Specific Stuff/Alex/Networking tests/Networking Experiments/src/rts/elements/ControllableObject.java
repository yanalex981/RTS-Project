package rts.elements;

import rts.networking.Player;

/**
 * The base class for all game objects such as soldiers, workers, buildings
 * 
 * @author Alex
 */
public abstract class ControllableObject {
	/**
	 * the team that this unit is on
	 */
	protected Player owner;
	
	/**
	 * the id of the unit
	 */
	protected int id;
	
	/**
	 * x position of the unit
	 */
	protected float x;
	
	/**
	 * y position of the unit
	 */
	protected float y;
	
	/**
	 * health of the unit
	 */
	protected int hp;
	
	/**
	 * one of the most basic unit states
	 */
	public static final int DESTROYED = 0;
	
	/**
	 * the state of this unit
	 */
	protected int state;

	/**
	 * the time when this unit is destroyed
	 */
	protected long timeDestroyed;
	
	/**
	 * reduces health of this unit
	 * 
	 * @param damage	the damage to be reduced from this unit
	 */
	public void takeDamage(int damage) {
		hp -= damage;
		
		if (hp <= 0) {
			timeDestroyed = System.currentTimeMillis();
		}
	}
	
	/**
	 * get the time this unit was destroyed at
	 * 
	 * @return time of destruction
	 */
	public long getTimeDestroyed() {
		return timeDestroyed;
	}
	
	/**
	 * gets the x position of the unit
	 * 
	 * @return x of the unit
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * set the x of the unit
	 * 
	 * @param x x position of the unit
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * gets the y position of the unit
	 * 
	 * @return y position of the unit
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * sets the y position of the unit
	 * 
	 * @param y y position of the unit
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * sets the health of the unit
	 * 
	 * @param hp health of the unit
	 */
	public void setHP(int hp) {
		this.hp = hp;
	}
	
	/**
	 * gets the hp of this unit
	 * 
	 * @return hp of this unit
	 */
	public int getHP() {
		return hp;
	}
	
	/**
	 * gets the unit ID of this unit
	 * 
	 * @return ID of this unit
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * sets the ID of this unit
	 * 
	 * @param id new ID of this unit
	 */
	public void setID(int id) {
		this.id = id;
	}
}
