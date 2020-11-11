package club10_11_20;

import java.util.Arrays;

interface Inter {
	int convert(int arg);
}

interface ObjectMaker {
	Object make();
}

interface ArrayMaker {
	String[] make(int sizeOfArray);
}

public class Main {

	public static void main(String[] args) {
		Inter lambda = (int x) -> {
			return x * 7;
		};
		Inter lambda2 = (x) -> x * 7;
		Inter lambda3 = Main::takeInt;
		
		ObjectMaker maker1 = () -> new Object();
		ObjectMaker maker2 = Object::new;
		
		ArrayMaker arrMaker = String[]::new;
		
		
		
		System.out.println(lambda.convert(4));
		System.out.println(lambda3.convert(6));
		
		String[] strs = arrMaker.make(12);
		System.out.println(Arrays.toString(strs));
		
	}
	
	public static int takeInt(int x) {
		return x + 3 + x * x;
	}
	
}
