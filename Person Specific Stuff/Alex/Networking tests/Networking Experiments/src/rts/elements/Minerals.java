package rts.elements;

import java.util.Random;

public class Minerals {
	private int minerals;
	
	public Minerals() {
		Random r = new Random();
		minerals = r.nextInt(3000) + 300;
	}
	
	public void mine() {
		minerals -= 30;
	}
	
	public int getMinerals() {
		return minerals;
	}
}
