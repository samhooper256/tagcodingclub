package club9_29_20;

import java.io.*;
import java.util.Arrays;

public class ExceptionTesting {
	public static void main(String[] args) throws Throwable {
		File file = new File("");
		BufferedReader in = new BufferedReader(new FileReader("src/club9_29_20/input.dat"));
		String[] lines = in.lines().toArray(String[]::new);
		System.out.println(Arrays.toString(lines));
	}
	
	static int getInt() {
		try {
			return 5;
		}
		catch(NullPointerException e) {
			
		}
		finally {
			System.out.println("Finally!");
		}
		return 66;
	}
	
	static void getFile() throws FileNotFoundException {
		throw new FileNotFoundException();
	}
	static int min(int[] arr) {
		if(arr.length == 0)
			throw new IllegalArgumentException("Array size is zero");
		return 0;
	}
}
