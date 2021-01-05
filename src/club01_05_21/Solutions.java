package club01_05_21;

import java.util.Random;

import timing.*;

/**
 * @author Sam Hooper
 *
 */
public class Solutions {
	
	private static final Random RANDOM = new Random();

	private static final int[][] TESTS = {{1,2,3}, {7,3,-4,13}, {9,3,1,30,12,132,5,0,3,356,2,3,4}, {12872,9090,44891,123456,939308123}};
	private static final long[] ANSWERS = {20, 74, 23108, 4698069362L};
	
	private static final int BIG1_SIZE = 3_000;
	private static final int BIG2_SIZE = 100_000;
	
	private static final int[] BIG1_TEST = RANDOM.ints(BIG1_SIZE, 0, 10).toArray();
	private static final int[] BIG2_TEST = RANDOM.ints(BIG2_SIZE, 0, 10).toArray();
	
	public static void main(String[] args) {
		requireAssertions();
		for(int i = 0; i < TESTS.length; i++) {
			assert sumSums_N(TESTS[i]) == ANSWERS[i];
			assert sumSums_N2(TESTS[i]) == ANSWERS[i];
			assert sumSums_N3(TESTS[i]) == ANSWERS[i];
		}
		System.out.println("All test cases passed.\n");
		
		System.out.printf("=== Testing input with %d elements ===%n", BIG1_SIZE);
		TimeUtils.timeAndDetail(() -> sumSums_N3(BIG1_TEST), "N^3 Algorithm", TimeUnit.SECONDS);
		TimeUtils.timeAndDetail(() -> sumSums_N2(BIG1_TEST), "N^2 Algorithm", TimeUnit.SECONDS);
		TimeUtils.timeAndDetail(() -> sumSums_N(BIG1_TEST), "N Algorithm", TimeUnit.SECONDS);
		
		System.out.println();
		
		System.out.printf("=== Testing input with %d elements ===%n", BIG2_SIZE);
		TimeUtils.timeAndDetail(() -> sumSums_N2(BIG2_TEST), "N^2 Algorithm", TimeUnit.SECONDS);
		TimeUtils.timeAndDetail(() -> sumSums_N(BIG2_TEST), "N Algorithm", TimeUnit.SECONDS);
		
	}
	
	/** <p>Throws an {@link AssertionError} if assertions are not enabled; otherwise, does nothing.</p>*/
	private static void requireAssertions() {
		boolean assertionsEnabled = false;
		assert assertionsEnabled = true;
		if(!assertionsEnabled)
			throw new AssertionError("Assertions are not enabled");
	}
	
	/** <p>Runs in O(n<sup>3</sup>).</p>*/
	private static long sumSums_N3(int[] arr) {
		long total = 0;
		for(int i = 0; i < arr.length; i++)
			for(int j = i + 1; j <= arr.length; j++)
				total += sumContiguousSubsequence(arr, i, j);
		return total;
	}
	
	/** <p>Returns the sum of the contiguous subsequence in {@code arr} that starts at {@code startInclusive} and ends at
	 * {@code endExclusive}.</p>*/
	private static long sumContiguousSubsequence(int[] arr, int startInclusive, int endExclusive) {
		long sum = 0;
		for(int i = startInclusive; i < endExclusive; i++)
			sum += arr[i];
		return sum;
	}
	
	/** <p>Runs in O(n<sup>2</sup>).</p>*/
	private static long sumSums_N2(int[] arr) {
		long total = 0;
		for(int i = 0; i < arr.length; i++) {
			long subsequenceSum = 0;
			for(int j = i; j < arr.length; j++) {
				subsequenceSum += arr[j];
				total += subsequenceSum;
			}
		}
		return total;
	}	
	
	/** <p>Runs in O(n).</p>*/
	private static long sumSums_N(int[] arr) {
		long total = 0;
		int L = arr.length;
		for(int i = 0; i < arr.length; i++) {
			int times = (i + 1) * (L - i);
			total += ((long) arr[i]) * times;
		}
		return total;
	}
	
}
