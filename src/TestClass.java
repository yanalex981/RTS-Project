import java.util.Scanner;

public class TestClass {
	public static void main(String[] args) {
		final int NUMTRIALS = 10000;
		
		Map map = new Map(10, 10);
		int width = map.getWidth();
		int height = map.getHeight();
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++)
				System.out.print(map.getNode(x, y).getNeighbours().length + "\t");
			System.out.println("");
		}
		System.out.println("\n\n");
		
		while(true) {
			Scanner scanner = new Scanner(System.in);
			
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++)
					System.out.print(map.getNode(x, y).getMaxUnitRadius() + "\t");
				System.out.println("");
			}
			System.out.println("Height: " + height);
			System.out.println("Width: " + width);
			
			String command = scanner.nextLine();
			
			if(command.equals("testspeed"))
				break;
			
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			
			if(command.equals("true"))
				map.setUnblocked(x, y);
			else
				map.setBlocked(x, y);
		}
		
		width /= 2;
		height /= 2;
		
		long time = System.nanoTime();
		for(int i = 0; i < NUMTRIALS; i++) {
			map.setBlocked(width, height);
			map.setUnblocked(width, height);
		}
		System.out.println(NUMTRIALS * 1000000000.0 / (System.nanoTime() - time));
	}
}
