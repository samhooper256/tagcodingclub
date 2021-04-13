package club_03_30_21;

import java.util.function.*;

public class LambdaTime {
	
	public static void main(String[] args) {
		
		BiPredicate<Integer, Integer> pred = (a, b) -> a + b > 10;
		
		System.out.println(pred.test(3, 5));
		System.out.println(pred.test(13, 35));
		
		Consumer<Object> c = obj -> System.out.println(obj);
		
		c.accept("abc");
		
		Supplier<Double> s = () -> Math.random();
		
		System.out.println(s.get());
		
		Function<String, Integer> f = str -> str.length();
		
		System.out.println(f.apply("hello"));
		
	}
	
}
