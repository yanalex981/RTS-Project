package rts.elements.units;

import rts.elements.Unit;

public class Miner extends Unit {
	private static final int SPEED = 2;
	private static final int HP = 50;
	private static final float RANGE = 0.2f;
	private static final int DAMAGE = 5;
	
	public Miner(int id, float x, float y) {
		super(id, x, y, SPEED, HP, RANGE, DAMAGE);
	}
}
