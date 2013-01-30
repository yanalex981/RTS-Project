package rts.elements;

import java.util.Random;

/**
 * A class for an object that allows worker units of the game to gather resources from
 * @author Alex
 */
public class Minerals {
	/**
	 * number of minerals that can be gathered/mined before this resource is depleted
	 */
	private int minerals;
	
	public Minerals() {
		Random r = new Random();
		minerals = r.nextInt(3000) + 300; // generates a random number of resources from 300 to 3300
	}
	
	/**
	 * reduces the number of resources in this object as if resources were gathered
	 */
	public void mine() {
		minerals -= 30;
	}
	
	/**
	 * gets the amount of minerals left in this rock that can be mined
	 * 
	 * @return
	 */
	public int getMinerals() {
		return minerals;
	}
}
