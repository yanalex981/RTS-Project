
public class Test {
	public static void main(String[] args) {
		method(5,2,4,6,3);
	}
	
	public static void method(int... x) {
		System.out.println(x[2]);
	}
}
