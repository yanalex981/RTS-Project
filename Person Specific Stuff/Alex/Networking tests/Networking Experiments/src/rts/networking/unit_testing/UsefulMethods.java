package rts.networking.unit_testing;

public class UsefulMethods {
	private UsefulMethods() {}
	
	public static int[] generateRandomNumbers(int size, int min, int max) {
		int[] array = new int[size];
		
		for (int i = 0; i < array.length; ++i) {
			i = (int)(Math.random() * ((max - min) + 1)) + min;
		}
		
		return array;
	}
}
