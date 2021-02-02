package club02_02_21;

import java.util.*;
import java.io.*;
import java.util.stream.*;

public class prob09 {

	public static void main(String[] args) throws Throwable {
		//Get input:
		BufferedReader in = new BufferedReader(new FileReader("src/club02_02_21/prob09.txt"));
		
		String[] lines = in.lines().toArray(String[]::new);
		
		
		int[][] data = new int[lines.length - 1][2];
		
		for(int i = 0; i < data.length; i++) {
			String[] split = lines[i].split(" ");
			data[i][0] = Integer.parseInt(split[0]);
			data[i][1] = Integer.parseInt(split[1]);
		}
		
		solveNoStreams(data);
	}

	private static void solveUsingStreams(int[][] data) {
		//Process input
		Map<Integer, Integer> scores = new HashMap<>();
		for(int[] problem : data)
			scores.put(problem[0], scores.getOrDefault(problem[0], 0) + problem[1]);
	
		List<Map.Entry<Integer, Integer>> entries = scores.entrySet().stream()
			.sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
		
		for(int i = 1; i <= 5; i++) {
			Map.Entry<Integer, Integer> entry = entries.get(entries.size() - i);
			System.out.printf("%d %d %d%n", i, entry.getKey(), entry.getValue());
		}
	}
	 
	//Here's a simpler (to understand) solution that does not involve streams.
	private static void solveNoStreams(int[][] data) {
		
		Map<Integer, Integer> scores = new HashMap<>(); //this map will map a team's ID to their total (cummulative) score.	
		for(int[] problem : data) {
			if(!scores.containsKey(problem[0]))
				scores.put(problem[0], 0);
			scores.put(problem[0], scores.get(problem[0]) + problem[1]);
		}
		
		//Sort the entries based on their score, in ascending order:
		List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(scores.entrySet());
		Collections.sort(entries, (e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()));
		
		//Print the last five entries in the list, in reverse order (this will be the top 5 teams):
		for(int i = 1; i <= 5; i++) {
			Map.Entry<Integer, Integer> entry = entries.get(entries.size() - i);
			System.out.printf("%d %d %d%n", i, entry.getKey(), entry.getValue());
		}
		
	}
	
}
