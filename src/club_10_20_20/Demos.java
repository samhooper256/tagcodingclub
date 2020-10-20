package club_10_20_20;

import java.util.*;

//union, sum
public class Demos {
	
	public static void main(String[] args) {
		ArrayList<Integer> integers = new ArrayList<Integer>();
		
		ArrayList<Number> numbers = new ArrayList<Number>();
		
		double intsum = sum(integers);
		double numsum = sum(numbers);
	}
	
	public static double sum(Collection<? extends Number> coll) {
		double sum = 0;
		for(Number n : coll)
			sum += n.doubleValue();
		return sum;
	}
	
}