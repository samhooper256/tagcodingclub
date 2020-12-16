package club09_29_20;

import java.util.ArrayList;

public class Testing {
	public static void main(String[] args) {
		int abc = 44;
		System.out.println(abc);
		int y = 3 + abc;
		System.out.println(method(abc));
		System.out.println("y = " + y);
		
		System.out.println("(x, y) = (" + abc + ", " + y + ")");
		System.out.println("(x, y) = (" + abc + ", " + y + ")");
		System.out.println("(x, y) = (" + abc + ", " + y + ")");
		
		ArrayList<String> a = new ArrayList<>();
		fillList(a);
	}

	private static void fillList(ArrayList<String> a) {
		for(int i = 0; i < 100; i++) {
			a.add(String.valueOf(i));
		}
	}
	
	public static int method(int num) {
		return num * num * num;
	}
}
