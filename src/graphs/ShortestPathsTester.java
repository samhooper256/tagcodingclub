package graphs;

import static graphs.Utils.generateArbitrarilyDirectedWeightedGraph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import codetesting.Assertions;

import static graphs.ShortestPaths.*;
import graphs.Utils.WeightedGraphData;

import static timing.TimeUtils.timeAndDetail;
/**
 * @author Sam Hooper
 *
 */
public class ShortestPathsTester {
	static class Datas{
		ShortestPathData d1, d2;
	}
	static final Datas datas = new Datas();
	public static void main(String[] args) {
		if(!Assertions.areEnabled()) { System.err.println("Assertions are not enabled."); return;}
//		System.out.println(bellmanFord(0, new String[] {"A","B"}, new String[][] {{"A","B"}, {"A","B"}}, new double[] {2, 1}));
		testAllAlgorithms_Randomized_PositiveWeights();
	}
	
	private static void testAllAlgorithms_Randomized_PositiveWeights() {
		
		ShortestPathData dijkstras, bellmanFord, spfa = null;
//		System.out.println("Entering...");
		char[] chars = {33};
		for(int test = 0; test < 10; test++) {
			System.out.printf("test #%d%n", test );
			int nodes = (int) (Math.random() * 500);
			int edges = (int) (Math.random() * nodes * nodes);
			WeightedGraphData<String> g = generateArbitrarilyDirectedWeightedGraph(()->String.valueOf(chars[0]++), nodes, edges, 0, 100);
			for(int i = 0; i < g.nodes.length; i++) {
//				System.out.printf("i=%d%n", i);
				if(!(
						(dijkstras=dijkstras(i, g.nodes, g.edges, g.weights)).equals(bellmanFord=bellmanFord(i, g.nodes,g.edges,g.weights)) &
						dijkstras.equals(spfa=spfa(i, g.nodes, g.edges, g.weights))
					)) {
					System.err.printf("i=%d, graph: %s%n            %s%n            %s%n", i, 
							Arrays.stream(g.nodes).map(s -> String.format("%-8.8s", s)).collect(Collectors.joining(", ", "[", "]")),
							Arrays.stream(g.edges).map(s -> String.format("%-8.8s", Arrays.toString(s))).collect(Collectors.joining(", ", "[", "]")),
							Arrays.stream(g.weights).mapToObj(d -> String.format("%-8.8s", d)).collect(Collectors.joining(", ", "[", "]")));
					System.err.printf("dijkstras:  %s%n            %s%n%n",
							Arrays.stream(dijkstras.dist).mapToObj(s -> String.format("%-8.8s", s)).collect(Collectors.joining(", ", "[", "]")),
							Arrays.stream(dijkstras.prev).mapToObj(s -> String.format("%-8.8s", s+(s==-1?"":"="+g.nodes[s]))).collect(Collectors.joining(", ", "[", "]")));
					System.err.printf("bellmanFord:%s%n            %s%n%n",
							Arrays.stream(bellmanFord.dist).mapToObj(s -> String.format("%-8.8s", s)).collect(Collectors.joining(", ", "[", "]")),
							Arrays.stream(bellmanFord.prev).mapToObj(s -> String.format("%-8.8s", s+(s==-1?"":"="+g.nodes[s]))).collect(Collectors.joining(", ", "[", "]")));
					System.err.printf("spfa:       %s%n            %s%n",
							Arrays.stream(spfa.dist).mapToObj(s -> String.format("%-8.8s", s)).collect(Collectors.joining(", ", "[", "]")),
							Arrays.stream(spfa.prev).mapToObj(s -> String.format("%-8.8s", s+(s==-1?"":"="+g.nodes[s]))).collect(Collectors.joining(", ", "[", "]")));
					for(int j = 0; j < dijkstras.dist.length;j++) {
						if(dijkstras.dist[j] != bellmanFord.dist[j] || dijkstras.dist[j] != spfa.dist[j]) {
							System.err.printf("Difference in dist at index %d%n", j);
						}
					}
					for(int j = 0; j < dijkstras.prev.length;j++) {
						if(dijkstras.prev[j] != bellmanFord.prev[j] || dijkstras.prev[j] != spfa.prev[j]) {
							System.err.printf("Difference in prev at index %d%n", j);
						}
					}
					assert false;
				}
			}
		}
		System.out.println("Successful test of dijkstra/bellmanFord/spfa (randomized, positive weights)");
	}
	private static void testDijkstras() {
		System.out.println(dijkstras(0,
				new String[] {"A","B","C"},
				new String[][] {{"A","B"}, {"B","C"}},
				new double[] {1.2, 3.4}
		));
		System.out.println(dijkstras(0,
				new String[] {"A","B","C"},
				new String[][] {{"A","B"}, {"B","C"}, {"A","C"}},
				new double[] {1.2, 3.4, 1.8}
		));
		System.out.println(dijkstras(0,
				new String[] {"A","B","C","D"},
				new String[][] {{"A","B"}, {"A","C"}, {"B","D"}, {"C", "D"}},
				new double[] {8, 12, 14, 8}
		));
		System.out.println("Successful test of dijkstras!");
	}
	
	private static void testShortestPathInDAG() {
		String[] nodes = {"A", "B", "C", "D", "E", "F", "G"};
		String[][] edges = {
				{"A", "G"},
				{"A", "B"},
				{"C", "B"},
				{"B", "F"},
				{"F", "D"},
				{"D", "E"},
				{"E", "G"},
				{"B", "E"},
		};
		double[] weights = {
				2, 0.9, 1.2, 0.8, 0.7, 3, 2, 1
		};
		Map<String, int[]> expecteds = new HashMap<>();
		expecteds.put("A", new int[] {-1, 0, -1, 5, 1, 1, 0});
		expecteds.put("B", new int[] {-1, -1, -1, 5, 1, 1, 4});
		expecteds.put("C", new int[] {-1, 2, -1, 5, 1, 1, 4});
		expecteds.put("D", new int[] {-1, -1, -1, -1, 3, -1, 4});
		expecteds.put("E", new int[] {-1, -1, -1, -1, -1, -1, 4});
		expecteds.put("F", new int[] {-1, -1, -1, 5, 3, -1, 4});
		expecteds.put("G", new int[] {-1, -1, -1, -1, -1, -1, -1});
		for(var e : expecteds.entrySet()) {
			try {
				assert Arrays.equals(shortestPathsInDAG_AsInts(e.getKey(), nodes, edges, weights), e.getValue());
			}
			catch(AssertionError ex) { System.err.println(e);}
		}
		try { assert false; }
		catch(AssertionError e) { System.out.println("Successful test of shortest path in DAG!"); }
		
	}
}
