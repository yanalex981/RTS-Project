import java.util.Scanner;

public class TestClass {
	public static void main(String[] args) {
		Map map = new Map(10, 10);
		Scanner scanner = new Scanner(System.in);
		int width = map.getWidth();
		int height = map.getHeight();
		
		while(true) {
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++)
					System.out.println(map.getNode(x, y).getMaxUnitRadius() + '\t');
				System.out.println("");
			}
			System.out.println("Height: " + height);
			System.out.println("Width: " + width);
			
			String command = scanner.nextLine();
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			
			if(command.equals("true"))
				map.setUnblocked(x, y);
			else
				map.setBlocked(x, y);
		}
	}
}
