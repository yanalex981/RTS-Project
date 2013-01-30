package rts.elements.structures;

import rts.elements.Building;
import rts.networking.Player;

/**
 * Power generator, increases the player's electrical power, one of the requirements to build units
 * 
 * @author Alex
 */
public class Generator extends Building {
	public Generator(Player owner, int id, float x, float y) {
		super(id, x, y, 3, 4, 400);
	}
}
