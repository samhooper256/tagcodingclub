package club04_13_21;

import java.util.*;

public class Main {

	/* *
	 * "A" -> 1
	 * "B" -> 2
	 * "C" -> 3
	 * etc. 
	 */
	
	static final Map<String, Integer> map = new HashMap<>();
	
	static {
		for(char c = 'A'; c <= 'Z'; c++) {
			map.put(String.valueOf(c), c - 'A' + 1);
		}
		
		for(Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.printf("%s : %s%n", entry.getKey(), entry.getValue());
		}
	}
	
	
	static {
		System.out.println("Static Init");
	}
	
	public static void main(String[] args) {
		System.out.println("Main method");
		
		final int k = 3;
		byte b = 66 + k;
		
		
		
		ArrayList<String> list = new ArrayList<>() {{
			add("E");
			add("E");
			add("A");
		}};
		
		System.out.println(list);
	}
	
}
