package rts.elements;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import rts.networking.Player;

public class Unit extends ControllableObject {
	private float radius;
	private float attackRange;
	private int damage;
	private float speed;

	public static final int STATE_IDLE = 1;
	public static final int STATE_MOVING = 2;
	public static final int STATE_ATTACKING = 3;
	public static final int STATE_COLLECTING = 4;
	public static final int STATE_UNLOADING = 5;
	
	private ArrayList<Point2D> path;
	ControllableObject target;
	
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
