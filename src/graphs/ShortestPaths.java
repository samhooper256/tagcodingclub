package graphs;

import static graphs.Utils.*;
import static utils.PriorityQueues.IntDoublePQ;

import java.util.*;

import graphs.Utils.DoubleWeightedEdgeList;
import graphs.Utils.IntBiConsumer;
/**
 * @author Sam Hooper
 *
 */
public class ShortestPaths {
	public static void main(String[] args) {
		
	}
	
	/** prev[i] will be -1 if i was the initial node or there is not path from the initial node to i;
	 * dist will hold 0 for the iniital node and infinity for nodes to which there is no path from i. */
	public static final class ShortestPathData {
		final double[] dist;
		final int[] prev;
		ShortestPathData(double[] dist, int[] prev) {
			this.dist = dist;
			this.prev = prev;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(dist);
			result = prime * result + Arrays.hashCode(prev);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			if(obj == null) {
				return false;
			}
			if(getClass() != obj.getClass()) {
				return false;
			}
			ShortestPathData other = (ShortestPathData) obj;
			return Arrays.equals(dist, other.dist) && Arrays.equals(prev, other.prev);
		}
		@Override
		public String toString() {
			return String.format("ShortestPathData[dist=%s, prev=%s]", Arrays.toString(dist), Arrays.toString(prev));
		}
	}
	
	public static <T> ShortestPathData dijkstras(T initial, Utils.WeightedGraphData<T> data) {
		return dijkstras(initial, data.nodes, data.edges, data.weights);
	}
	
	public static <T> ShortestPathData dijkstras(int initialIndex, Utils.WeightedGraphData<T> data) {
		return dijkstras(initialIndex, data.nodes, data.edges, data.weights);
	}
	/** Computes shortest path to all other nodes starting from {@code initial}. Loops in the graph will not be considered
	 * by this algorithm. Weights must be nonnegative. Multiple edges between two nodes in the same direction are permitted, but note
	 * that this algorithm returns the nodes in each shortest path, not the edges.
	 */
	public static <T> ShortestPathData dijkstras(T initial, T[] nodes, T[][] edges, double[] edgeWeights) {
		int index = indexOf(nodes, initial);
		if(index == -1)
			throw new IllegalArgumentException("The starting node is not in the graph");
		return dijkstras(index, nodes, edges, edgeWeights);
	}
	/** {@code initialIndex} must be between 0 (inclusive) and {@code nodes.length} (exclusive). Weights must be nonnegative.
	 * Multiple edges between two nodes in the same direction are permitted, but note that this algorithm returns the nodes in
	 * each shortest path, not the edges. Runs in O((|V| + |E|)log(V)) time.*/
	public static <T> ShortestPathData dijkstras(int initialIndex, T[] nodes, T[][] edges, double[] edgeWeights) {
		if(initialIndex < 0 || initialIndex >= nodes.length)
			throw new IllegalArgumentException("initialIndex must be between 0 (inclusive) and nodes.length (exclusive)");
		if(edges.length != edgeWeights.length)
			throw new IllegalArgumentException("edges.length must equal edgeWeights.length");
//		System.out.printf("entered dijkstra(initialIndex=%d, nodes=%s)%n", initialIndex, Arrays.toString(nodes));
		Map<T, Integer> map = mapToIndices(nodes);
		DoubleWeightedEdgeList[] adj = getDirectedAdjacencyLists(nodes, edges, edgeWeights, map);
		
		boolean[] vis = new boolean[nodes.length];
		double[] dist = new double[nodes.length];
		int[] prev = new int[nodes.length];
		Arrays.fill(dist, Double.POSITIVE_INFINITY); // O(V)
		Arrays.fill(prev, -1);
		IntDoublePQ unvisitedSet = new IntDoublePQ();
		dist[initialIndex] = 0;
		for(int i = 0; i < nodes.length; i++)
			unvisitedSet.insert(i, dist[i]); //O(log(V))
		while(!unvisitedSet.isEmpty()) { //loops for each vertex, so O(V)
//			System.out.printf("\tentered loop, unvisitedSet=%s, dist=%s, prev=%s%n", unvisitedSet, Arrays.toString(dist), Arrays.toString(prev));
			int current = unvisitedSet.extract(); // O(log(V))
			if(dist[current] == Double.POSITIVE_INFINITY)
				break;
			vis[current] = true;
			for(int i = 0; i < adj[current].size(); i++) { //this loop will do a total of O(E) throughout the entire algo
				DoubleWeightedEdge edge = adj[current].get(i);
				int n = edge.dest;
				if(!vis[n]) {
					double tentative = dist[current] + edge.weight;
					if(dist[n] > tentative) {
						dist[n] = tentative;
						prev[n] = current;
						unvisitedSet.changePriority(n, tentative); // O(log(V))
					}
				}
			}
		}
		return new ShortestPathData(dist, prev);
	}
	
	/** <p>Computes the shortest path from {@code nodes[initialIndex]} to all other nodes in the given weighted digraph, which may contain cycles
	 * and negative weight edges (but no negative-weight cycles). The graph may have loops; the loops count as cycles of length 1
	 * (and thus the length of the edge in the loop must not be negative). The graph may also have multiple edges connecting the same pair of nodes.
	 * This method throws an {@link IllegalArgumentException} if {@code intialIndex} is not between 0 (inclusive) and {@code nodes.length} (exclusive),
	 * {@code edges.length != edgeWeights.length}, or the graph contains a negative-weight cycle that is reachable from the initial node.
	 * </p><p>This method uses the Bellman-Ford algorithm (also known as the Bellman-Ford-Moore algorithm) and runs in O(|V||E|) time.</p>
	 */
	public static <T> ShortestPathData bellmanFord(int initialIndex, T[] nodes, T[][] edges, double[] edgeWeights) {
		if(initialIndex < 0 || initialIndex >= nodes.length)
			throw new IllegalArgumentException("initialIndex must be between 0 (inclusive) and nodes.length (exclusive)");
		if(edges.length != edgeWeights.length)
			throw new IllegalArgumentException("edges.length must equal edgeWeights.length");
		
		//preparation:
		Map<T, Integer> map = mapToIndices(nodes);
		int[][] edgeInts = edgesToInts(edges, map);
		double[] dist = new double[nodes.length];
		int[] prev = new int[nodes.length];
		Arrays.fill(dist, Double.POSITIVE_INFINITY);
		dist[initialIndex] = 0;
		Arrays.fill(prev, -1);
		
		//relax edges |V| - 1  times:
		for(int i = 1; i < nodes.length; i++) { //Runs O(V) times
			boolean changed = false;
			for(int j = 0; j < edgeInts.length; j++) { //Runs O(E) times for each iteration of outer loop
				double w = edgeWeights[j];
				int u = edgeInts[j][0], v = edgeInts[j][1];
				if(dist[u] + w < dist[v]) {
					changed = true;
					dist[v] = dist[u] + w;
					prev[v] = u;
				}
			}
			if(!changed) break;
		}
		
		//check for negative-weight cycles:
		for(int j = 0; j < edgeInts.length; j++) { //Runs O(E) times
			double w = edgeWeights[j];
			int u = edgeInts[j][0], v = edgeInts[j][1];
			if(dist[u] + w < dist[v])
				throw new IllegalArgumentException("Graph contains a negative-weight cycle.");
		}
		
		return new ShortestPathData(dist, prev);
	}
	
	/**
	 * Computes the shortest path from {@code node[initialIndex]} to all other nodes using the SPFA (Shortest Path Faster Algorithm)
	 * first published by Edward F. Moore in O(|V||E|) time. This algorithm is believed to be better for sparse graphs than the 
	 * Bellman-Ford algorithm.
	 */
	public static <T> ShortestPathData spfa(int initialIndex, T[] nodes, T[][] edges, double[] edgeWeights) {
		if(initialIndex < 0 || initialIndex >= nodes.length)
			throw new IllegalArgumentException("initialIndex must be between 0 (inclusive) and nodes.length (exclusive)");
		if(edges.length != edgeWeights.length)
			throw new IllegalArgumentException("edges.length must equal edgeWeights.length");
//		System.out.printf("enteredspfa(initialIndex=%d)%n", initialIndex);
//		System.out.printf("entered spfa(initialIndex=%d, nodes=%s, edges=%s, edgeWeights=%s)%n", initialIndex, Arrays.toString(nodes), Arrays.deepToString(edges), Arrays.toString(edgeWeights));
		double[] dist = new double[nodes.length];
		Arrays.fill(dist, Double.POSITIVE_INFINITY);
		dist[initialIndex] = 0;
		int[] prev = new int[nodes.length];
		Arrays.fill(prev, -1);
		DoubleWeightedEdgeList[] adj = getDirectedAdjacencyLists(nodes, edges, edgeWeights, mapToIndices(nodes));
		LinkedList<Integer> queue = new LinkedList<>();
		boolean[] inQueue = new boolean[nodes.length];
		queue.add(initialIndex);
		inQueue[initialIndex] = true;
		while(!queue.isEmpty()) {
			int u = queue.poll();
			inQueue[u] = false;
			for(int i = 0; i < adj[u].size(); i++) {
				int v = adj[u].get(i).dest;
				double newDist = dist[u] + adj[u].get(i).weight;
				if(newDist < dist[v]) {
					dist[v] = newDist;
					prev[v] = u;
					if(!inQueue[v]) {
						queue.add(v);
						inQueue[v] = true;
					}
				}
			}
		}
		return new ShortestPathData(dist, prev);
	}
	/** Calculates the shortest path from {@code source} to all other vertices in the graph. Runs in O(|E|+|V|) time
	 * (linear). Passes the results to {@code consumer} as a node and its predecessor in the shortest path from
	 * {@code source} (in that order). That passed {@code int}s are the indices of the corresponding nodes
	 * in {@code nodes}. <b>Passes {@code -1} as the predecessor index if a node has no predecessor</b>
	 */
	public static
	<T> void shortestPathsInDAG(T source, T[] nodes, T[][] edges, double[] edgeWeights, IntBiConsumer consumer) {
		int[] prev = shortestPathsInDAG_AsInts(source, nodes, edges, edgeWeights);
		for(int i = 0; i < prev.length; i++)
			consumer.accept(i, prev[i]);
	}
	
	/** Calculates the shortest path from {@code source} to all other vertices in the graph. Runs in O(|E|+|V|) time
	 * (linear). Returns an array where the value at index {@code i} is the index in {@code nodes} of the node
	 * immediately before {@code nodes[i]} in the shortest path, or {@code -1} if no node comes before {@code node[i]}.
	 */
	public static <T> int[] shortestPathsInDAG_AsInts(T source, T[] nodes, T[][] edges, double[] edgeWeights) {
		Map<T, Integer> map = mapToIndices(nodes);
		if(!map.containsKey(source))
			throw new IllegalArgumentException(source + "is not in the graph");
		double[] d = new double[nodes.length];
		Arrays.fill(d, Double.POSITIVE_INFINITY);
		d[map.get(source)] = 0;
		int[] prev = new int[nodes.length];
		Arrays.fill(prev, -1);
		
		class WEdge{
			final int nodeIndex;
			final double weight;
			WEdge(int nodeIndex, double weight) {
				this.nodeIndex = nodeIndex;
				this.weight = weight;
			}
		}
		@SuppressWarnings("serial")
		class WEdgeList extends ArrayList<WEdge> {}
		
		WEdgeList[] adj = new WEdgeList[nodes.length];
		for(int i = 0; i < adj.length; i++)
			adj[i] = new WEdgeList();
		forEachEdge(edges, edgeWeights, map, (from, to, w) -> adj[from].add(new WEdge(to, w)));
		
		int[] topologicalSortInts = TopologicalSorts.topologicalSort_UsingKahnsAlgorithm_AsInts(nodes, edges, map);
		for(int i = 0; i < topologicalSortInts.length; i++) {
			int u = topologicalSortInts[i];
			for(int j = 0; j < adj[u].size(); j++) {
				WEdge edge = adj[u].get(j);
				int v = edge.nodeIndex;
				double w = edge.weight;
				if(d[v] > d[u] + w) {
					d[v] = d[u] + w;
					prev[v] = u;
				}
			}
		}
		return prev;
	}
}
