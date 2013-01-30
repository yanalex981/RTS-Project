package rts.elements.structures;

import rts.elements.Building;

/**
 * Also called the barracks, this unit produces marines
 * 
 * @author Alex
 */
public class Factory extends Building {
	public Factory(int id, float x, float y) {
		super(id, x, y, 4, 3, 300);
	}
}
