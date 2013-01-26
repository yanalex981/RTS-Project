
public class Test extends Thread {
	public Test() {}
	
	public void run() {
		for (int i = 0; i < 1000; ++i) {
			System.out.println(i);
		}
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		test.run();
	}
}
