package club12_15_20;

import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * <p>Solution code for the coding problem from the meeting on 12/15/20. The coding problem for this club is the Advent of Code
 * Day 1 problem, parts 1 and 2.<p>
 * 
 * <p>Given the input in {@code input.txt}, the correct answer to <b>part 1 is <code>921504</code></b> and the correct
 * answer to <b>part 2 is <code>195700142</code></b>.</p>
 * @author Sam Hooper
 *
 */
public class Solution {

	private static void gettingInput() throws Exception { //make sure to add the throws clause if working with BufferedReader or Files.lines(...)
		
		//In Eclipse, if you put the input file in the same package as your class (like I have - see my package explorer on the left),
		//the file path (where it says "FILEPATH_HERE" below) will be:
		// 		"src/packagename/filename.extension"
		//So, for example, mine would be: "src/club12_15_20/input.txt"
		
		//The Files and Path types are in the java.nio.file package. (use "import java.nio.file.*").
		
		//To get the input as an array of Strings, one String per line:
		String[] stringArray = Files.lines(Path.of("FILEPATH_HERE")).toArray(String[]::new);
		
		//To get the input as one big String:
		String bigString = Files.lines(Path.of("FILEPATH_HERE")).collect(Collectors.joining("\n")); // <-- whatever String you put here will
		//separate each line in the file. I put the newline character ('\n').
		
		//To get the input simply as a Stream<String> (for those who don't know what that is, don't worry about it):
		Stream<String> stream = Files.lines(Path.of("FILEPATH_HERE"));
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
		int[] nums = Files.lines(Path.of("src/club12_15_20/input.txt")).mapToInt(Integer::parseInt).toArray();
		
//		Scanner in = new Scanner(new File("filepath"));
//		while(in.hasNextLine()) {
//			String line = in.nextLine();
//		}
		
		solvePart1_Version2(nums);
		solvePart2_Version2(nums);
		
	}
	
	private static void solvePart1_Version2(int[] nums) {
		System.out.println(findProduct(nums, 2020, 0, nums.length));
	}
	
	private static void solvePart2_Version2(int[] nums) {
		
		// O(n^2)
		for(int i = 0; i < nums.length; i++) {
			int needed = 2020 - nums[i];
			int product = findProduct(nums, needed, i + 1, nums.length);
			if(product > 0)
				System.out.println(nums[i] * product);
		}
		
	}

	private static int findProduct(int[] nums, int target, int startInclusive, int endExclusive) {
		// O(n)
		HashSet<Integer> set = new HashSet<>(nums.length);
		for(int i = startInclusive; i < endExclusive; i++)
			set.add(nums[i]);
		for(int i = startInclusive; i < endExclusive; i++)
			if(set.contains(target - nums[i]))
				return nums[i] * (target - nums[i]);
		return -1;
	}
	
	
	private static void solvePart1(int[] nums) {
		
		// O(n^2)
		for(int i = 0; i < nums.length; i++)
			for(int j = i + 1; j < nums.length; j++)
				if(nums[i] + nums[j] == 2020)
					System.out.println(((long) nums[i]) * ((long) nums[j]));
		
	}
	
	private static void solvePart2(int[] nums) {
		
		// O(n^3)
		for(int i = 0; i < nums.length; i++)
			for(int j = i + 1; j < nums.length; j++)
				for(int k = j + 1; k < nums.length; k++)
					if(nums[i] + nums[j] + nums[k] == 2020)
						System.out.println(((long) nums[i]) * ((long) nums[j]) * nums[k]);
		
	}
	
}
