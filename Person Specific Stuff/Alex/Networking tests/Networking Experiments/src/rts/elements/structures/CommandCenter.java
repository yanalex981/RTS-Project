package rts.elements.structures;

import rts.elements.Building;

/**
 * Command center unit. Produces workers
 * 
 * @author Alex
 */
public class CommandCenter extends Building {
	public CommandCenter(int id, float x, float y) {
		super(id, x, y, 5, 5, 1000);
	}
}
