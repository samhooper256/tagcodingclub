package club12_1_20;

import java.util.Arrays;

public class Testing {
	
	public static void main(String[] args) {
		// Sorts
		// Selection Sort (n^2)
		// Insertion Sort (n^2)
		// Merge Sort (nlogn)
		
		// Counting Sort O(n)
		
		final int max = 1000, min = -1500;
		//All the values are between 0 and 1000
		int[] arr = {0, -3, 10, 25, 938, 452, 3, -323, 24, 53, -999, 823, 432};
		
		System.out.println("Before: " + Arrays.toString(arr));
		
		int[] temp = new int[2501];
			
		// O(n):
		for(int item : arr)
			temp[item - min]++;
		
		int[] sorted = new int[arr.length];
		
		int sortedIndex = 0;
		
		// O(n):
		for(int i = 0; i < temp.length; i++) {
			for(int j = 0; j < temp[i]; j++) {
				sorted[sortedIndex] = i + min;
				sortedIndex++;
			}
		}
		
		System.out.println("After: " + Arrays.toString(sorted));
		
		// O(n)
		
		
	}
	
}
