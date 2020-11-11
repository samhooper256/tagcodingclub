package club11_10_20;

import java.util.*;
import java.util.stream.*;

public class StreamsMain {
	
	public static void main(String[] args) {
		Stream<Integer> stream = null;
		
		String[] strArr = {"a","eafe","beafea","be","afeafea"};
		
		Stream<String> strStream = Arrays.stream(strArr);
		List<String> strList = List.of("a","b","c");
		strList.stream();
		
		strStream
			.sorted()
			.distinct()
			.filter(x -> x.startsWith("b"))
			.map(x -> x.toUpperCase())
			.forEach(x -> System.out.println(x));
		
//		strStream.map(x -> x).findFirst(); //IllegalStateException: only one terminal operation per stream!
		System.out.println(strList.stream().allMatch(x -> x.length() > 0));
		
		strList.stream().collect(Collectors.toList());
		strList.stream().collect(Collectors.toSet());
		
		strList.stream().collect(Collectors.toCollection(ArrayList::new));
		
		String[] listAsArr = strList.toArray(String[]::new);
		
		String longest = Arrays.stream(strArr).max((s1, s2) -> {
			if(s1.length() > s2.length())
				return 1;
			if(s1.length() == s2.length())
				return 0;
			return -1;
		}).get();
		System.out.println("longest = " + longest);
		
		int[] ints = {1,2,3,5,7};
		
		Set<Integer> set = new HashSet<>();
		
	}
	
}
