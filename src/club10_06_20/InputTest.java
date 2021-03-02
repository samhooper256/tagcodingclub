package club10_06_20;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class InputTest {
	public static void main(String[] args) throws Exception {
//		Scanner in = new Scanner(new File("src/club_10_6_20/mazes.dat"));
//		in.nextLine();
		
		BufferedReader in2 = new BufferedReader(new FileReader("src/club_10_6_20/mazes.dat"));
		String[] arr = in2.lines().toArray(String[]::new);
		
		System.out.print(Arrays.toString(arr));
		
		//Breadth first Search
		//Depth first Search
	}
}
