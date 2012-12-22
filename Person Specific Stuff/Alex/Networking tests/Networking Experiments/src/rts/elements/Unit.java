package rts.elements;

public class Unit extends ControllableObject {
	private double radius;
	private double attackRange;
	private int damage;
	private double speed;
	
	private boolean moving;
	private boolean attacking;
	private boolean idling;
	
	public Unit(int id, double x, double y, int speed, int hp, double attackRange, int damage) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.hp = hp;
		this.attackRange = attackRange;
		this.damage = damage;
		idling = true;
	}
	
	public void moveTo(int x, int y) {
		
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

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isIdling() {
		return idling;
	}

	public void setIdling(boolean idling) {
		this.idling = idling;
	}

	public double getSpeed() {
		return speed;
	}
}
