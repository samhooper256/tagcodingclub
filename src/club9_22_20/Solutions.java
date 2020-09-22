package club9_22_20;

import java.util.*;
import java.util.stream.Stream;
import timing.TimeUnit;

import static timing.TimeUtils.timeAndDetail;

public class Solutions {
	private static final Random rand = new Random();
	private static final int ALPHABET_SIZE = 128;
	private static final int MIN_CHARACTER = 0;
	private static final int MAX_CHARACTER = 127;
	private static final int MAX_LENGTH =  1_000_000_000;
	private static final String ALL_UNIQUE = new String(java.util.stream.IntStream.range(0, 128).toArray(), 0, 128);
	@FunctionalInterface
	interface UniquenessChecker {
		boolean isUnique(String s);
	}
	
	public static void main(String[] args) {
//		verifySolution();
		Scanner in = new Scanner(System.in);
		Map<String, UniquenessChecker> solutions = new LinkedHashMap<>();
		solutions.put("Pairwise", Solutions::isUnique_Pairwise);
		solutions.put("HashSet", Solutions::isUnique_HashSet);
		solutions.put("Array", Solutions::isUnique_Array);
//		solutions.put("Streams", Solutions::isUnique_Streams);
		while(true) {
			System.out.printf("Enter the number of test cases and their maximum and minimum length (inclusive) as 3 space-separated integers.%n");
			String line = in.nextLine().strip();
			String[] split = line.split("[,\\s]+");
			final String[] testCases;
			if(line.startsWith("malicious")) {
				int numCases = Integer.parseInt(split[1]);
				int maxLength = Integer.parseInt(split[2]);
				testCases = new String[numCases];
				for(int i = 0; i < numCases; i++) {
					char[] extras = new char[maxLength - ALL_UNIQUE.length()];
					Arrays.fill(extras, ALL_UNIQUE.charAt(ALL_UNIQUE.length() - 1));
					testCases[i] = ALL_UNIQUE + new String(extras);
				}
			}
			else {
				int[] ints = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
				int numCases = ints[0], minLength = ints[1], maxLength = ints[2];
				testCases = Stream.generate(() -> {
					int len = rand.nextInt(maxLength - minLength) + minLength;
					return new String(rand.ints(len, MIN_CHARACTER, MAX_CHARACTER).toArray(), 0, len);
				}).limit(numCases).toArray(String[]::new);
			}
			for(var sol : solutions.entrySet()) {
				UniquenessChecker op = sol.getValue();
				timeAndDetail(() -> {
					for(String s : testCases)
						op.isUnique(s);
				}, sol.getKey(), TimeUnit.SECONDS);
			}
		}
	}
	
	public static boolean isUnique(String s) {
		return isUnique_Pairwise(s);
	}
	
	/*
	 * Time Complexity: O(a)
	 * Space Complexity: O(a)
	 */
	public static boolean isUnique_Array(String s) {
		if(s.length() > 128) return false;
		boolean[] seen = new boolean[128];
		for(int i = 0; i < s.length(); i++)
			if(seen[s.charAt(i)])
				return false;
			else
				seen[s.charAt(i)] = true;
		return true;
	}
	
	/*
	 * Time Complexity: O(a)
	 * Space Complexity: O(a)
	 */
	public static boolean isUnique_HashSet(String s) {
		if(s.length() > 128) return false;
		HashSet<Character> set = new HashSet<>();
		// contains => O(1)
		// add => O(1)
		for(int i = 0; i < s.length(); i++) // a times
			if(set.contains(s.charAt(i))) //O(1)
				return false;
			else
				set.add(s.charAt(i)); //O(1)
		return true;
	}
	
	/*
	 * Time Complexity: O(a*n)
	 * Space Complexity: O(1)
	 */
	public static boolean isUnique_Pairwise(String s) {
		if(s.length() > 128) return false;
		for(int i = 0; i < s.length() - 1; i++) { //What's the maximum number of times this loop can iterate?
			for(int j = i + 1; j < s.length(); j++) //During a single iteration of the outer loop, how many times can this one iterate?
				if(s.charAt(i) == s.charAt(j))
					return false;
		}
		return true;
	}

	/*
	 * Time Complexity: O(n)
	 * Space Complexity: O(a)
	 */
	public static boolean isUnique_Streams(String s) {
		/* Behind the scenes what this does is: 
		 * 	- Convert each character in s to an int by zero-extending the char value
		 * 	- Convert all those ints to Integer objects
		 *  - Put all of those Integers into a HashSet
		 *  - Return whether of not the size of that HashSet is equal to the length of s.
		 *  Note that this is NOT the same as the HashSet solution presented above. Its time is O(n)
		 *  because it attempts to add ALL n characters to the HashSet before doing anything with it.
		 *  The other solution stops as soon as a character has been found that was already added to the HashSet.
		 *  
		 *  This is not the most efficient solution by any means; the algorithm isn't good, and streams require
		 *  a lot of overhead to boot. However, it sure is concise.
		 * */
		return s.chars().distinct().count() == s.length();
	}
	
	/*
	 * Only works for very small alphabets. ( <= 64 characters)
	 * Time Complexity: O(a)
	 * Space Complexity O(1)
	 */
	public static boolean isUnique_Bitwise(String s) {
		long seen = 0;
		for(int i = 0; i < s.length(); i++) {
			long shift = 1L << s.charAt(i);
			if((seen & shift) == 0)
				seen |= shift;
			else
				return false;
		}
		return true;
	}

	private static void verifySolution() {
		System.out.printf("Loading test cases (may take a while, one case has a billion characters)...");
		
		String[] inputs = {"", "a", "abc", "[]35aoekgrix09", "[]35aoekgrix03","axxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxa",
				"1234567890-_+=qwertyuiopasdfghjklzxcvbnm,<.>/?'[}~", "Ayuj is tall",
				ALL_UNIQUE, ALL_UNIQUE + "a", new String(new char[100_000]), ALL_UNIQUE + new String(new char[100_000]),
				ALL_UNIQUE + new String(new char[999_999_000])};
		boolean[] expected = {true, true, true, true, false, false, true, false, true, false, false, false, false};
		boolean success = true;
		System.out.printf("done%n%nStartings tests... (if the tests take more than a couple seconds, your code is not efficient enough)%n");
		System.out.printf("Any line breaks you see in the \"input\" part of a failed test case are part of that case's input.%n%n");
		for(int i = 0; i < inputs.length; i++) {
			boolean result = isUnique(inputs[i]);
			System.out.printf("Attempting test case %2d...", i);
			if(result != expected[i]) {
				success = false;
				System.out.printf("FAILED. returned=%b, expected=%b, input%s%n", result, expected[i],
						inputs[i].length() < 150 ? "=" + inputs[i] : " is too long to display (" + inputs[i].length() + " characters)");
			}
			else {
				System.out.printf("correct%n");
			}
		}
		if(!success) {
			System.err.printf("%nYour solution is not correct. Exiting program.%n");
			System.exit(1);
		}
		System.out.printf("%nSuccess! Your solution passed all %d test cases. Well done!", inputs.length);
	}
}
