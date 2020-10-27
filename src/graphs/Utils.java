package graphs;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Sam Hooper
 *
 */
public final class Utils {
	@FunctionalInterface public static interface IntBiConsumer{ void accept(int a, int b); }
	@FunctionalInterface public static interface IntIntDoubleConsumer{ void accept(int a, int b, double c); }
	public static boolean arrayContains(int[] arr, int item) {
		for(int i : arr) if(i == item) return true;
		return false;
	}
	
	static class GraphData<T> {
		final T[] nodes, edges[];
		public GraphData(T[] nodes, T[][] edges) {
			this.nodes = nodes;
			this.edges = edges;
		}
	}
	
	static class WeightedGraphData<T> extends GraphData<T> {
		final double[] weights;
		public WeightedGraphData(T[] nodes, T[][] edges, double[] weights) {
			super(nodes, edges);
			this.weights = weights;
		}
		@Override
		public String toString() {
			return String.format("WeightedGraphData[nodes=%s, edges=%s, weights=%s]", Arrays.toString(nodes), Arrays.deepToString(edges),
					Arrays.toString(weights));
		}
	}
	
	/**
	 * Generates an arbitrarily directed weighted graph using the given parameters. The generated graph may have loops.
	 * {@code weightUpperBound} must be greater than or equal to {@code weightLowerBound}. {@code nodeCount} must be positive (and not 0).
	 */
	public static <T> WeightedGraphData<T> generateArbitrarilyDirectedWeightedGraph(Supplier<T> nodeSupplier,
			int nodeCount, int edgeCount, double weightLowerBound, double weightUpperBound) {
		if(weightLowerBound > weightUpperBound)
			throw new IllegalArgumentException("Upper bound on weights must be >= lower bound.");
		if(nodeCount <= 0)
			throw new IllegalArgumentException("node count must be > 0");
		
		T first = nodeSupplier.get();
		Class<?> clazz = first.getClass();
		T[] nodes = (T[]) Array.newInstance(clazz, nodeCount);
		nodes[0] = first;
		for(int i = 1; i < nodes.length; i++)
			nodes[i] = nodeSupplier.get();
		T[][] edges = (T[][]) Array.newInstance(clazz.arrayType(), edgeCount);
		for(int i = 0; i < edges.length; i++) {
			T[] edge = (T[]) Array.newInstance(clazz, 2);
			edge[0] = nodes[(int) (Math.random() * nodes.length)];
			edge[1] = nodes[(int) (Math.random() * nodes.length)];
			edges[i] = edge;
		}
		double[] weights = new double[edges.length];
		for(int i = 0; i < weights.length; i++)
			weights[i] = Math.random() * (weightUpperBound - weightLowerBound) + weightLowerBound;
		
		
		return new WeightedGraphData<>(nodes, edges, weights);
	}
	
	public static boolean arrayContains(boolean[] arr, boolean item) {
		for(boolean b : arr) if(b == item) return true;
		return false;
	}
	
	public static class DoubleWeightedEdge {
		final int dest;
		final double weight;
		public DoubleWeightedEdge(int dest, double weight) {
			this.dest = dest;
			this.weight = weight;
		}
	}
	
	@SuppressWarnings("serial")
	public static class DoubleWeightedEdgeList extends ArrayList<DoubleWeightedEdge> {}
	
	/** Throws an {@link IllegalArgumentException} if {@code nodes.length == 0}. */
	public static <T> boolean isDirectedAcyclicGraph(T[] nodes, T[][] edges) {
		if(nodes.length == 0) throw new IllegalArgumentException("The graph cannot be empty");
		Map<T, Integer> map = mapToIndices(nodes);
		int[] outEdges = getOutgoingEdgeCounts(nodes, edges, map);
		IntList[] radj = getReverseAdjacencyLists(nodes, edges, map);
		int nodesProcessed = 0;
		IntList leaves = new IntList();
		for(int i = 0; i < nodes.length; i++)
			if(outEdges[i] == 0)
				leaves.add(i);
		while(!leaves.isEmpty()) {
			int leaf = leaves.popSafe();
			nodesProcessed++;
			for(int i = 0; i < radj[leaf].size(); i++)
				if(--outEdges[radj[leaf].get(i)] == 0)
					leaves.add(radj[leaf].get(i));
			//we don't need to clear radj[leaf], as it will never be looked at again.
		}
		return nodesProcessed == nodes.length;
	}
	
	public static <T> HashMap<T, Integer> mapToIndices(T[] arr) {
		HashMap<T, Integer> map = new HashMap<>(arr.length);
		for(int i = 0; i < arr.length; i++)
			map.put(arr[i], i);
		return map;
	}
	
	public static <T> boolean isCyclic(T[] nodes, T[][] edges) {
		return new CyclicFinder<>(nodes, edges).isCyclic();
	}
	private static class CyclicFinder<T> {
		boolean[] recStack, visited;
		T[] nodes, edges[];
		IntList[] adj;
		public CyclicFinder(T[] nodes, T[][] edges) {
			if(nodes.length == 0) throw new IllegalArgumentException("The graph cannot be empty");
			this.nodes = nodes; this.edges = edges;
		}
		
		public boolean isCyclic() {
			adj = getDirectedAdjacencyLists(nodes, edges, mapToIndices(nodes));
			visited = new boolean[nodes.length];
			recStack = new boolean[nodes.length];
			for(int i = 0; i < nodes.length; i++)
				if(isCyclicHelper(i))
					return true;
			return false;
		}
		
		private boolean isCyclicHelper(int index) {
			if(recStack[index])
				return true;
			if(visited[index])
				return false;
			visited[index] = true;
			recStack[index] = true;
			for(int i = 0; i < adj[index].size(); i++)
				if(isCyclicHelper(adj[index].get(i)))
					return true;
			recStack[index] = false;
			return false;
		}
	}
	
	
	public static <T> int[] getIncomingEdgeCounts(T[] nodes, T[][] edges, Map<T, Integer> map) {
		int[] incomingEdgeCounts = new int[nodes.length];
		forEachEdge(edges, map, (index1, index2) -> incomingEdgeCounts[index2]++);
		return incomingEdgeCounts;
	}
	
	public static <T> int[] getOutgoingEdgeCounts(T[] nodes, T[][] edges, Map<T, Integer> map) {
		int[] outgoingEdgeCounts = new int[nodes.length];
		forEachEdge(edges, map, (index1, index2) -> outgoingEdgeCounts[index1]++);
		return outgoingEdgeCounts;
	}
	
	public static <T> int[][] edgesToInts(T[][] edges, Map<T, Integer> map) {
		int[][] edgeInts = new int[edges.length][2];
		for(int i = 0; i < edgeInts.length; i++) {
			edgeInts[i][0] = map.get(edges[i][0]);
			edgeInts[i][1] = map.get(edges[i][1]);
		}
		return edgeInts;
	}
	
	public static <T> void forEachEdge(T[][] edges, double[] edgeWeights, Map<T, Integer> map, IntIntDoubleConsumer consumer) {
		int i = 0;
		try {
			for(; i < edges.length; i++)
				consumer.accept(map.get(edges[i][0]), map.get(edges[i][1]), edgeWeights[i]);
		}
		catch(NullPointerException e) { //a NPE can only occur when one of the map.get(...) calls returns null
			throw new InvalidGraphException(String.format("The edge %s"
					+ " connects nodes that are not in the graph.", Arrays.toString(edges[i])));
		}
	}
	
	public static <T> void forEachEdge(T[][] edges, Map<T, Integer> map, IntBiConsumer consumer) {
		int i = 0;
		try {
			for(; i < edges.length; i++)
				consumer.accept(map.get(edges[i][0]), map.get(edges[i][1]));
		}
		catch(NullPointerException e) { //a NPE can only occur when one of the map.get(...) calls returns null
			throw new InvalidGraphException(String.format("The edge %s"
					+ " connects nodes that are not in the graph.", Arrays.toString(edges[i])));
		}
	}
	
	public static <T> IntList[] getDirectedAdjacencyLists(T[] nodes, T[][] edges, Map<T, Integer> map) {
		IntList[] adj = new IntList[nodes.length];
		for(int i = 0; i < adj.length; i++)
			adj[i] = new IntList();
		forEachEdge(edges, map, (index1, index2) ->  adj[index1].add(index2));
		return adj;
	}
	
	/** Gets the reverse adjacency list for the given directed graph. */
	public static <T> IntList[] getReverseAdjacencyLists(T[] nodes, T[][] edges, Map<T, Integer> map) {
		IntList[] radj = new IntList[nodes.length];
		for(int i = 0; i < radj.length; i++)
			radj[i] = new IntList();
		forEachEdge(edges, map, (i1, i2) -> radj[i2].add(i1));
		return radj;
	}
	
	public static <T> IntList[] getUndirectedAdjacencyLists(T[] nodes, T[][] edges, Map<T, Integer> map) {
		IntList[] adj = new IntList[nodes.length];
		for(int i = 0; i < adj.length; i++)
			adj[i] = new IntList();
		forEachEdge(edges, map, (index1, index2) ->  { adj[index1].add(index2); adj[index2].add(index1); } );
		return adj;
	}
	
	public static <T>
	DoubleWeightedEdgeList[] getDirectedAdjacencyLists(T[] nodes, T[][] edges, double[] edgeWeights, Map<T, Integer> map) {
		DoubleWeightedEdgeList[] adj = new DoubleWeightedEdgeList[nodes.length];
		for(int i = 0; i < adj.length; i++)
			adj[i] = new DoubleWeightedEdgeList();
		forEachEdge(edges, edgeWeights, map, (index1, index2, weight) ->  adj[index1].add(new DoubleWeightedEdge(index2, weight)));
		return adj;
	}
	
	public static <T> int indexOf(T[] arr, T item) {
		for(int i = 0; i < arr.length; i++)
			if(Objects.equals(arr[i], item))
				return i;
		return -1;
	}
	
	public static int min(Collection<Integer> c) {
		if(c.size() == 0) throw new IllegalArgumentException("Cannot find minimum in empty Collection");
		int min = Integer.MAX_VALUE;
		for(int i : c)
			if(i < min)
				min = i;
		return min;
	}
	
	public static <T> T[] inferNodes(T[][] edges, IntFunction<T[]> arraySupplier) {
		return Arrays.stream(edges)
				.flatMap(Arrays::stream)
				.distinct()
				.toArray(arraySupplier);
	}
}
