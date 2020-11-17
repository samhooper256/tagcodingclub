package club10_27_20;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import graphs.TopologicalSorts;
import graphs.Utils;
/**
 * @author Sam Hooper
 *
 */
public class Tester {
	
	private static Map<String[][], Set<List<String>>> testCases = new LinkedHashMap<>();
	
	public static void main(String[] args) throws Throwable {
//		writeBigTest();
		BufferedReader in = new BufferedReader(new FileReader("src/club_10_27_20/input.dat"));
		String[] lines = in.lines().toArray(String[]::new);
		for(String line : lines) {
			String[][] arr = Arrays.stream(line.split(";")).map(s -> s.split(",")).toArray(String[][]::new);
			testCases.put(arr, TopologicalSorts.allTopologicalSorts(Utils.inferNodes(arr, String[]::new), arr, HashSet::new));
		}
		
		Function<String[][], List<String>> solution = getSolutionFuncton();
		
		int num = 0;
		for(Map.Entry<String[][], Set<List<String>>> e : testCases.entrySet()) {
			List<String> ans = solution.apply(e.getKey());
			if(e.getValue().contains(ans)) {
				System.out.printf("✓ Solved Case %d%n", num);
			}
			else {
				System.out.printf("❌ FAILED CASE %d%n", num);
				System.out.printf("\tinput: %s%n", Arrays.deepToString(e.getKey()).replace('[','{').replace(']','}'));
				System.out.printf("\texpected list: %s%s%n", e.getValue().size() > 1 ? "(one of) " : "", 
						e.getValue().stream().map(Object::toString).collect(Collectors.joining(" OR ")));
				System.out.printf("\tyour list: %s%n", ans);
			}
			num++;
		}
	}
	
	private static void writeBigTest() throws Throwable {
		Files.write(Paths.get("src/club_10_27_20/input.dat"), bigTestString().getBytes(), StandardOpenOption.APPEND);
	}
	
	private static String bigTestString() {
		StringJoiner j = new StringJoiner(";");
		for(int i = 0; i < 1_000; i++)
			j.add(i + "," + (i+1));
		return j.toString();
	}
	
	private static Function<String[][], List<String>> getSolutionFuncton() {
		try {
			Method m = Solution.class.getMethod("solve", String[][].class);
			return arr -> {
				try {
					return (List<String>) m.invoke(null, (String[][]) arr);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
					throw new UnsupportedOperationException("Method Invocation failed.");
				}
			};
		}
		catch (Exception e) {
			throw new UnsupportedOperationException("solve method could not be found.");
		}
	}
}
