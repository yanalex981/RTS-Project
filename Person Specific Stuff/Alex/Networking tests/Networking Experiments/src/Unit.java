
public class Unit {
	private int attackRange;
	private int damage;
	private int health;
	private int speed;
	private int x, y;
	
	private boolean moving;
	private boolean attacking;
	private boolean idling;
	
	public Unit(int x, int y, int speed, int health, int attackRange, int damage) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.health = health;
		this.attackRange = attackRange;
		this.damage = damage;
		idling = true;
	}
	
	public void takeDamage(int damage) {
		health -= damage;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
