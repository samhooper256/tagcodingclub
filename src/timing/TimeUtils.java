package timing;

import java.util.*;

public class TimeUtils {
	
	/** Returns the time that the given action took to run in nanoseconds. */
	public static double time(Runnable action) {
		return time(action, TimeUnit.NANOSECONDS);
	}
	
	/** Returns the time that the given action took to run in the desired units */
	public static double time(Runnable action, TimeUnit resultUnit) {
		return time(action, new TimeUnit[] {resultUnit})[0];
	}
	
	/** Returns the time that the given action took to run in the desired units, where index
	 * {@code i} in the result array corresponds to {@code resultUnits[i]}. */
	public static double[] time(Runnable action, TimeUnit... resultUnits) {
		long start = System.nanoTime();
		action.run();
		long nanoTime = System.nanoTime() - start;
		double[] results = new double[resultUnits.length];
		for(int i = 0; i < resultUnits.length; i++)
			results[i] = resultUnits[i].convert(nanoTime, TimeUnit.NANOSECONDS);
		return results;
	}
	
	/** Prints to stdout and returns the duration that the given action took to complete */
	public static double timeAndDetail(Runnable action) {
		return timeAndDetail(action, TimeUnit.NANOSECONDS);
	}
	
	/** Prints to stdout and returns the duration that the given action took to complete.
	 * The printed statement contains {@code name}, the name of the test. */
	public static double timeAndDetail(Runnable action, String name) {
		return timeAndDetail(action, name, TimeUnit.NANOSECONDS);
	}
	
	/** Prints to stdout and returns the duration that the given action took to complete, in
	 * the desired units. */
	public static double timeAndDetail(Runnable action, TimeUnit unit) {
		return timeAndDetail(action, "Untitled", unit);
	}
	
	/** Prints to stdout and returns the duration that the given action took to complete, in
	 * the desired units. The printed statement contains {@code name}, the name of the test. */
	public static double timeAndDetail(Runnable action, String name, TimeUnit unit) {
		return timeAndDetail(action, name, new TimeUnit[] {unit})[0];
	}
	
	/** Prints to stdout and returns the durations that the given action took to complete, in
	 * the desired units. The printed statement contains {@code name}, the name of the test. */
	public static double[] timeAndDetail(Runnable action, String name, TimeUnit... units) {
		if(units.length == 0) throw new IllegalArgumentException("units.length == 0");
		System.out.printf("Testing \"%s\"...", name);
		double[] durations = time(action, units);
		StringJoiner j = new StringJoiner(", ");
		for(int i = 0; i < durations.length; i++)
			j.add(String.format("%.3f %s", durations[i], units[i].toString().toLowerCase()));
		System.out.println(String.format("Test \"%s\" took %s", name, j));
		return durations;
	}
}
