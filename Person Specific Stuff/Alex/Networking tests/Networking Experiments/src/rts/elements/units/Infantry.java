package rts.elements.units;

import rts.elements.Unit;

public class Infantry extends Unit {
	private static final int SPEED = 2;
	private static final int HP = 50;
	private static final double RANGE = 0.2;
	private static final int DAMAGE = 5;
	
	public Infantry(int id, double x, double y) {
		super(id, x, y, SPEED, HP, RANGE, DAMAGE);
	}
}
