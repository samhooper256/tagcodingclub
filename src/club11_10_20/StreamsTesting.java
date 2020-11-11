package club11_10_20;

import java.util.*;
import java.util.stream.*;

public class StreamsTesting {
	
	public static void main(String[] args) {
		int[] arr = {1,5,9,14,7,82};
		Map<Integer, Integer> map = IntStream.range(0, arr.length)
				.boxed()
				.collect(Collectors.toMap(i -> arr[i], i -> i));
		String[] strs = {"a", "bceade", "bc", "eafafeeaf", "aebe"};
		System.out.println(
				Arrays.stream(strs).collect(Collectors.partitioningBy(s -> s.length() < 3, Collectors.toCollection(ArrayList::new)))
		);
		
		System.out.println(Arrays.stream(strs).collect(Collectors.groupingBy(s -> s.substring(0,1))));
		
	}
	
}
