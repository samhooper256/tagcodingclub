package club9_15_20;

import java.util.*;
import java.util.function.IntUnaryOperator;

import timing.*;

public class SolutionsTimer {
	private static final Random rand = new Random();
	private static final TimeUnit desiredUnit = TimeUnit.SECONDS;
	private static final long NUM_TEST_CASES = 100_000_000;
	private static class Result implements Comparable<Result> {
		private final String name;
		private final double time;
		public Result(String name, double time) {
			Objects.requireNonNull(name);
			this.name = name;
			this.time = time;
		}
		
		String getName() { return name; }
		double getTime() { return time; }
		
		@Override
		public int compareTo(Result o) {
			int c = Double.compare(time, o.time);
			if(c != 0) return c;
			return name.compareTo(o.name);
		}
	}
	public static void main(String[] args) {
		Solutions.main(args);
		
		TreeSet<Result> results = new TreeSet<>();
		int[] inputs = rand.ints(NUM_TEST_CASES, 0, Integer.MAX_VALUE).toArray();
		System.out.printf("%n--- Starting tests ---%n");
		for(Map.Entry<String, IntUnaryOperator> solution : Solutions.getSolutions()) {
			IntUnaryOperator op = solution.getValue();
			double timeValue = TimeUtils.timeAndDetail(() -> {
				for(int input : inputs)
					op.applyAsInt(input);
			}, solution.getKey(), desiredUnit);
			results.add(new Result(solution.getKey(), timeValue));
		}
		if(results.size() == 0) return;
		System.out.printf("%n%n%n");
		int maxNameLength = results.stream().map(Result::getName).max(Comparator.comparingInt(String::length)).get().length();
		String header = "-= SCOREBOARD =-";
		int spaceCount = (maxNameLength + 9 + desiredUnit.toString().length()) - header.length();
		System.out.printf("%s%n", " ".repeat(spaceCount >> 1) + header + " ".repeat((spaceCount >> 1) + (spaceCount & 1)));
		for(Result r : results)
			System.out.printf("%-" + maxNameLength + "s : %.3f %s%n", r.getName(), r.getTime(), desiredUnit.toString().toLowerCase());
		
	}
}
