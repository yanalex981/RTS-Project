package rts.elements;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import rts.networking.Player;

public class Unit extends ControllableObject {
	/**
	 * radius that the unit will occupy
	 */
	private float radius;
	
	/**
	 * the radius of this unit's attack range
	 */
	private float attackRange;
	
	/**
	 * the damage this unit will do
	 */
	private int damage;
	
	/**
	 * how fast this unit can move
	 */
	private float speed;

	/**
	 * Idle state
	 */
	public static final int STATE_IDLE = 1;
	
	/**
	 * Moving state
	 */
	public static final int STATE_MOVING = 2;
	
	/**
	 * Attacking state
	 */
	public static final int STATE_ATTACKING = 3;
	
	/**
	 * Collecting state
	 */
	public static final int STATE_COLLECTING = 4;
	
	/**
	 * Unloading resources state
	 */
	public static final int STATE_UNLOADING = 5;
	
	/**
	 * The path this unit will follow when it moves to a new point
	 */
	private ArrayList<Point2D> path;
	
	/**
	 * The the object this unit is trying to attack
	 */
	private ControllableObject target;
	
	public Unit(Player owner, int id, float x, float y, int speed, int hp, float attackRange, int damage) {
		this.owner = owner;
		this.id = id;
		this.x = x;
		this.y = y;
		this.state = STATE_IDLE;
		this.speed = speed;
		this.hp = hp;
		this.attackRange = attackRange;
		this.damage = damage;
	}
	
	public void collect() {
		state = STATE_COLLECTING;
	}
	
	public void unload() {
		state = STATE_UNLOADING;
	}
	
	public boolean isInRange(ControllableObject target) {
		float dX = target.getX() - getX();
		float dY = target.getY() * getY();
		
		if (Math.sqrt(dX + dY) < attackRange)
			return false;
		
		return true;
	}
	
	public void moveTo(ArrayList<Point2D> pathToTarget) {
		this.state = STATE_MOVING;
		path = pathToTarget;
	}
	
	public void attack(ControllableObject target) {
		this.state = STATE_ATTACKING;
		this.target = target;
	}
	
	public double getRadius() {
		return radius;
	}

	public double getAttackRange() {
		return attackRange;
	}

	public int getDamage() {
		return damage;
	}

	public float getSpeed() {
		return speed;
	}
}
