package rts.elements.units;

import rts.elements.Unit;
import rts.networking.Player;

public class Marine extends Unit {
	private static final int SPEED = 2;
	private static final int HP = 50;
	private static final float RANGE = 0.2f;
	private static final int DAMAGE = 5;
	
	public Marine(Player owner, int id, float x, float y) {
		super(owner, id, x, y, SPEED, HP, RANGE, DAMAGE);
	}
}
