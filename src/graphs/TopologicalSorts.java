package graphs;

import java.util.*;
import java.util.function.*;
import static graphs.Utils.*;
import static codetesting.Assertions.assertThrows;
/**
 * @author Sam Hooper
 *
 */
public class TopologicalSorts {
	
	/** Runs in O(|V| + |E|) time where V is the set of vertices and E the set of edges in the graph. The graph must be
	 * directed and acyclic, but it need not be connected. It may be a multigraph. */
	public static <T> Set<T> getMinimumPrecessorsInTopologicalSort(T item, T[] nodes, T[][] edges) {
		Map<T, Integer> map = mapToIndices(nodes);
		if(!map.containsKey(item)) throw new IllegalArgumentException(item + "is not in the graph");
		IntList[] radj = getReverseAdjacencyLists(nodes, edges, map);
		Set<T> results = new HashSet<>();
		findPredecessors(results, nodes, radj, new boolean[nodes.length], map.get(item));
		return results;
	}
	
	private static <T> 
	void findPredecessors(Set<T> addResultsTo, T[] nodes, IntList[] radj, boolean[] vis, int currentNode) {
		vis[currentNode] = true;
		IntList list = radj[currentNode];
		for(int i = 0; i < list.size; i++) {
			int n = list.get(i);
			if(!vis[n]) {
				addResultsTo.add(nodes[n]);
				findPredecessors(addResultsTo, nodes, radj, vis, n);
			}
		}
	}
	/** Determines if the given sort is a topological sort of the given graph. Returns {@code false} if length of the
	 * sort does not equal the length of {@code nodes}. This method throws an {@link IllegalArgumentException} if the
	 * graph is not directed, connected, and acyclic; otherwise, if passed a sort containing items not in {@code nodes}, it simply
	 * returns {@code false}.
	 */
	public static <T> boolean isTopological(List<T> sort, T[] nodes, T[][] edges) {
//		System.out.printf("isTopological(sort=%s, nodes=%s, edges=%s%n", sort, Arrays.toString(nodes), Arrays.deepToString(edges));
		if(sort.size() != nodes.length) return false;
		Map<T, Integer> map = mapToIndices(nodes);
		int[] incomingEdgeCounts = getIncomingEdgeCounts(nodes, edges, map);
		IntList[] adj = getDirectedAdjacencyLists(nodes, edges, map);
		Set<Integer> sources = getSources(HashSet::new, incomingEdgeCounts);
		if(sources.isEmpty()) throw new InvalidGraphException("The input graph is not connected, directed and acyclic");
		Iterator<T> itr = sort.iterator();
//		System.out.printf("Initial sources=%s%n", sources);
		Integer nextAsInteger = map.get(itr.next());
		while(!sources.isEmpty()) {
//			System.out.printf("\t(start)nextAsInteger=%d, itr.hasNext()=%b, sources=%s%n", nextAsInteger, itr.hasNext(), sources);
			if(!sources.remove(nextAsInteger))
				return false;
			int n = nextAsInteger;
			for(int i = 0; i < adj[n].size(); i++) {
				int m = adj[n].get(i);
				incomingEdgeCounts[m]--;
				if(incomingEdgeCounts[m] == 0)
					sources.add(m);
			}
			adj[n].clear();
			if(itr.hasNext())
				nextAsInteger = map.get(itr.next());
			else
				return true;
//			System.out.printf("\t(end)nextAsInteger=%d, itr.hasNext()=%b, sources=%s%n", nextAsInteger, itr.hasNext(), sources);
		}
		if(itr.hasNext()) throw new InvalidGraphException("The input graph is not connected, directed and acyclic");
		return true;
	}
	public static <T> List<List<T>> allTopologicalSorts(T[] nodes, T[][] edges) {
		return allTopologicalSorts(nodes, edges, ArrayList::new);
	}
	
	public static <T, C extends Collection<List<T>>> C allTopologicalSorts(T[] nodes, T[][] edges, Supplier<C> collectionFactory) {
		Map<T, Integer> map = mapToIndices(nodes);
		C results = collectionFactory.get();
		allTopologicalSorts(
				results, new ArrayList<>(nodes.length), nodes,
				new boolean[nodes.length], getDirectedAdjacencyLists(nodes, edges, map), getIncomingEdgeCounts(nodes, edges, map));
		return results;
	}
	
	private static <T, C extends Collection<List<T>>> void allTopologicalSorts(C addResultsTo, List<T> addTo, T[] nodes, boolean[] vis,
			IntList[] adj, int[] incomingEdgeCounts) {
		List<Integer> sources = new ArrayList<>();
		int leftToVisit = 0;
		for(int i = 0; i < incomingEdgeCounts.length; i++)
			if(!vis[i]) {
				leftToVisit++;
				if(incomingEdgeCounts[i] == 0)
					sources.add(i);
			}
		if(sources.size() == 0){
			if(leftToVisit != 0) 
				throw new InvalidGraphException("Input graph is not a directed acyclic graph");
			addResultsTo.add(new ArrayList<>(addTo)); return;
		}
//		System.out.printf("\tsources=%s%n", sources);
		for(final int n : sources) {
			IntList oldAdjN = new IntList(adj[n]);
			addTo.add(nodes[n]);
			vis[n] = true;
			for(int i = 0; i < adj[n].size(); i++)
				incomingEdgeCounts[adj[n].get(i)]--;
			adj[n].clear();
			allTopologicalSorts(addResultsTo, addTo, nodes, vis, adj, incomingEdgeCounts);
			vis[n] = false;
			adj[n] = oldAdjN;
			for(int i = 0; i < adj[n].size(); i++)
				incomingEdgeCounts[adj[n].get(i)]++;
			addTo.remove(addTo.size() - 1);
		}
	}
	
	
	
	public static class DFSTopologicalSort<T> {
		public static <T> List<T> topologicalSort_UsingDFS(T[] nodes, T[][] edges) {
			return new DFSTopologicalSort<>(nodes, edges).solve();
		}
		
		public static <T, C extends List<? super T>> 
		C topologicalSort_UsingDFS(Supplier<C> listFactory, T[] nodes, T[][] edges) {
			C list = listFactory.get();
			list.addAll(new DFSTopologicalSort<>(nodes, edges).solve());
			return list;
		}
		
		private static final int TEMPORARY_MARK = 1, PERMANENT_MARK = 2;
		private final T[] nodes;
		private final T[][] edges;
		private IntList[] adj;
		private byte[] marks;
		private LinkedList<T> sorted;
		private DFSTopologicalSort(T[] nodes, T[][] edges) {
			this.nodes = nodes;
			this.edges = edges;
		}
		
		private List<T> solve() {
			marks = new byte[nodes.length];
			Map<T, Integer> map = mapToIndices(nodes);
			adj = getDirectedAdjacencyLists(nodes, edges, map);
			sorted = new LinkedList<>();
			for(int i = 0; i < marks.length; i++)
				if(marks[i] != PERMANENT_MARK)
					visit(i);
			return sorted;
		}
		
		private void visit(int node) {
			if(marks[node] == PERMANENT_MARK)
				return;
			if(marks[node] == TEMPORARY_MARK)
				throw new InvalidGraphException("Input graph is not a directed acyclic graph");
			marks[node] = TEMPORARY_MARK;
			for(int m : adj[node])
				visit(m);
			marks[node] = PERMANENT_MARK;
			sorted.addFirst(nodes[node]);
		}
	}
	
	public static <T> int[] topologicalSort_UsingKahnsAlgorithm_AsInts(T[] nodes, T[][] edges) {
		return topologicalSort_UsingKahnsAlgorithm_AsInts(nodes, edges, mapToIndices(nodes));
	}
	
	public static <T> int[] topologicalSort_UsingKahnsAlgorithm_AsInts(T[] nodes, T[][] edges, Map<T, Integer> map) {
		return topologicalSort_UsingKahnsAlgorithm_AsInts(nodes, getDirectedAdjacencyLists(nodes, edges, map), getIncomingEdgeCounts(nodes, edges, map));
	}
	
	public static <T> List<T> topologicalSort_UsingKahnsAlgorithm_AsList(T[] nodes, T[][] edges) {
		Map<T, Integer> map = mapToIndices(nodes);
		return topologicalSort_UsingKahnsAlgorithm_AsList(nodes, getDirectedAdjacencyLists(nodes, edges, map), getIncomingEdgeCounts(nodes, edges, map));
	}

	/** The results are passed to {@code consumer} as {@code int}s in the order of the topological sort. The {@code int}
	 * value is the index in {@code nodes} of the corresponding node.
	 * <b>This method modifies the contents of {@code adj}.</b>
	 */
	private static <T> void topologicalSort_UsingKahnsAlgorithm(T[] nodes, IntList[] adj, int[] incomingEdgeCounts,
			IntConsumer consumer) {
		List<Integer> sources = getSources(ArrayList::new, incomingEdgeCounts);
		
		int acceptedCount = 0;
		while(!sources.isEmpty()) {
			int n = sources.remove(sources.size() - 1);
			consumer.accept(n);
			acceptedCount++;
			for(int i = 0; i < adj[n].size(); i++) {
				int m = adj[n].get(i);
				incomingEdgeCounts[m]--;
				if(incomingEdgeCounts[m] == 0)
					sources.add(m);
			}
			adj[n].clear();
		}
		if(acceptedCount != nodes.length)
			throw new InvalidGraphException("Input graph is not a directed acyclic graph");
		for(int i : incomingEdgeCounts)
			if(i != 0)
				throw new InvalidGraphException("Input graph is not a directed acyclic graph");
	}

	private static <C extends Collection<? super Integer>> 
	C getSources(Supplier<C> collectionFactory, int[] incomingEdgeCounts) {
		C sources = collectionFactory.get();
		for(int i = 0; i < incomingEdgeCounts.length; i++)
			if(incomingEdgeCounts[i] == 0)
				sources.add(i);
		return sources;
	}

	private static <T> List<T> topologicalSort_UsingKahnsAlgorithm_AsList(T[] nodes, IntList[] adj, int[] incomingEdgeCounts) {
		List<T> sorted = new ArrayList<>(nodes.length);
		topologicalSort_UsingKahnsAlgorithm(nodes, adj, incomingEdgeCounts, i -> sorted.add(nodes[i]));
		return sorted;
	}
	
	private static <T> int[] topologicalSort_UsingKahnsAlgorithm_AsInts(T[] nodes, IntList[] adj, int[] incomingEdgeCounts) {
		int[] sorted = new int[nodes.length];
		topologicalSort_UsingKahnsAlgorithm(nodes, adj, incomingEdgeCounts, new IntConsumer() {
			int index = 0;
			@Override public void accept(int value) { sorted[index++] = value; }
		});
		return sorted;
	}
	
	public static <T> List<Node<T>> topologicalSort_UsingKahnsAlgorithm_Nondestructive(Graph<T> g) {
		Graph.GraphWithMap<T> duplicate = g.deepCopy();
		List<Node<T>> sorted = topologicalSort_UsingKahnsAlgorithm_Destructive(duplicate.graph);
		for(ListIterator<Node<T>> itr = sorted.listIterator(); itr.hasNext();)
			itr.set(duplicate.copyToOriginal.get(itr.next()));
		return sorted;
	}
	
	//this algorithm destroys the given graph (specifically, it removes edges)
	public static <T> List<Node<T>> topologicalSort_UsingKahnsAlgorithm_Destructive(Graph<T> g) {
//		System.out.printf("\tdestructive recieved: %s%n", g.nodesUnmodifiable());
		List<Node<T>> sortedList = new ArrayList<>();
		List<Node<T>> sources = new ArrayList<>(); //a "source" is a node with in-degree zero
		for(Node<T> node : g.nodesUnmodifiable())
			if(node.incomingEdgeCount() == 0)
				sources.add(node);
		while(!sources.isEmpty()) {
			Node<T> node = sources.remove(sources.size() - 1);
			sortedList.add(node);
			for(Iterator<DirectedEdge<T>> itr = node.outgoingEdgesUnmodifiable().iterator(); itr.hasNext();) {
				DirectedEdge<T> e = itr.next();
				Node<T> m = e.end();
				if(m.incomingEdgeCount() == 1) //m has no incoming edges other than from n
					sources.add(m);
			}
			node.removeAllOutgoingEdges();
		}
		for(Node<T> node : g.nodesUnmodifiable())
			if(node.outgoingEdgeCount() != 0)
				throw new InvalidGraphException("The input graph was not a directed acyclic graph.");
		return sortedList;
	}
	
	static class Graph<T> {
		public static class GraphWithMap<S> {
			private GraphWithMap(Graph<S> graph, IdentityHashMap<Node<S>, Node<S>> originalToCopy){
				this.graph = graph;
				copyToOriginal = new IdentityHashMap<>(originalToCopy.size());
				for(Map.Entry<Node<S>, Node<S>> entry : originalToCopy.entrySet()) {
					Object prev = copyToOriginal.put(entry.getValue(), entry.getKey());
					if(prev != null) throw new IllegalArgumentException("originalToCopy contains duplicate values");
				}
			}
			public final Graph<S> graph;
			public final IdentityHashMap<Node<S>, Node<S>> copyToOriginal;
		}
		
		private Set<Node<T>> nodes;
		public Graph() {
			nodes = new HashSet<>();
		}
		
		private Graph(Set<Node<T>> nodes) {
			this.nodes = nodes;
		}
		public Set<Node<T>> nodesUnmodifiable(){
			return Collections.unmodifiableSet(nodes);
		}
		
		GraphWithMap<T> deepCopy() {
			IdentityHashMap<Node<T>, Node<T>> nodeCopies = new IdentityHashMap<>();
			for(Node<T> n : nodes)
				nodeCopies.put(n, new Node<>(n.data()));
//			System.out.printf("nodeCopies = %s%n", nodeCopies);
			HashSet<Node<T>> newNodes = new HashSet<>(nodes.size());
			for(Map.Entry<Node<T>, Node<T>> entry : nodeCopies.entrySet()) {
				Node<T> newNode = entry.getValue();
				for(DirectedEdge<T> e : entry.getKey().outgoingEdgesUnmodifiable())
					newNode.addOutgoingEdge(nodeCopies.get(e.end()));
				newNodes.add(newNode);
			}
			return new GraphWithMap<>(new Graph<>(newNodes), nodeCopies);
		}
		
		@SuppressWarnings("unchecked")
		public void addNodes(Node<T>... nodes) {
			for(Node<T> n : nodes)
				addNode(n);
		}
		
		public boolean addNode(Node<T> node) {
			return nodes.add(node);
		}
		
		public boolean removeNode(Node<T> node) {
			return nodes.remove(node);
		}
		
		public int nodeCount() { return nodes.size(); }
	}
	
	static class Node<T> {

		private T data;
		private Set<DirectedEdge<T>> outgoingEdges;
		private Set<DirectedEdge<T>> incomingEdges;
		public Node(T data) {
			this.data = data;
			outgoingEdges = new LinkedHashSet<>();
			incomingEdges = new LinkedHashSet<>();
		}
		
		public void removeAllOutgoingEdges() {
			for(DirectedEdge<T> outgoingEdge : outgoingEdges) {
				outgoingEdge.end().removeIncomingEdge(outgoingEdge);
			}
			outgoingEdges.clear();
		}
		
		public boolean removeIncomingEdge(DirectedEdge<T> edge) {
			return incomingEdges.remove(edge);
		}

		public boolean addOutgoingEdge(Node<T> to) {
			DirectedEdge<T> edge = new DirectedEdge<>(this, to); //TODO to is null
			return	outgoingEdges.add(edge) & to.incomingEdges.add(edge);
		}
		
		public boolean addIncomingEdge(Node<T> from) {
			DirectedEdge<T> edge = new DirectedEdge<>(from, this);
			return incomingEdges.add(edge) & from.outgoingEdges.add(edge);
		}
		
		public Set<DirectedEdge<T>> outgoingEdgesUnmodifiable(){
			return Collections.unmodifiableSet(outgoingEdges);
		}
		
		public Set<DirectedEdge<T>> incomingEdgesUnmodifiable(){
			return Collections.unmodifiableSet(incomingEdges);
		}
		
		/** returns the sum of this node's outgoing and incoming edges */
		public int totalEdgeCount() {
			return outgoingEdgeCount() + incomingEdgeCount();
		}
		public int outgoingEdgeCount() { return outgoingEdges.size(); }
		
		public int incomingEdgeCount() { return incomingEdges.size(); }
		
		public T data() { return data; }
		
		@Override
		public String toString() {
			return String.format("Node[%s, %d in, %d out]@%x",
					data, incomingEdgeCount(), outgoingEdgeCount(), System.identityHashCode(this));
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(data, outgoingEdges);
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
			Node<?> other = (Node<?>) obj;
			return Objects.equals(data, other.data) && Objects.equals(outgoingEdges, other.outgoingEdges);
		}
	}
	
	static class DirectedEdge<T> {
		 private Node<T> start, end;
		 public DirectedEdge(Node<T> start, Node<T> end) {
			 this.start = start;
			 this.end = end;
		 }
		 
		 public Node<T> start() { return start; }
		 public Node<T> end() { return end; }
	}
}
