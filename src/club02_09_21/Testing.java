package club02_09_21;

import java.util.*;

public class Testing {
	
	public Testing(int x) {
		
	}
	
	public static void main(String[] args) {
		double d = Integer.MAX_VALUE + 1000L;
		
//		int x = Math.round(4.4);
//		
		int big = 1_000_000;
		
		int y = 0b1_0__1;
		System.out.println(y);
		
		int hex = 0xfA;
		System.out.println(hex);
		
		//Regex: \d
		
		String regex = "\\d";
		System.out.println(regex);
		
		System.out.println("4".matches(regex));
		
		String str = "\n \t eaff \t \n ";
		
		System.out.println(str.strip());
		
		System.out.println("Ayuj".repeat(3));
		
		String multiline = "abc\ndef\nghi";
		
		multiline.lines().forEachOrdered(System.out::println);
		
		str.replaceAll("[a-z]*", "x");
		str.replaceFirst("", "");
		
		String rev = new StringBuilder(str).reverse().toString();
		
		int[] arr = {1,2,3,4,5};
		long index = 2;
		int first = arr[3];
		
		boolean b = true;
		b &= false;
		b = (b & false);
		
//		System.out.println(EXP1 && EXP2);
		
		System.out.println(3 & 1);
		System.out.println(-12 % 5);
		
		char[] chars = {};
		String s3 = String.valueOf(chars);
		String s2 = new String(chars);
		
	}
	
}

class Sub extends Testing {
	
	public Sub() {
		super(5);
	}
	
	
}