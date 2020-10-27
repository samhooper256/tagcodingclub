package graphs;

import static graphs.Utils.*;
import static java.lang.Math.min;
import java.util.*;
import java.util.function.*;
/**
 * @author Sam Hooper
 *
 */
public class GraphComponents {
	
	public static <T> Kosaraju<T> getStronglyConnectedComponents_UsingKosarajusAlgorithm(T[] nodes, T[][] edges) {
		return new Kosaraju<>(nodes, edges);
	}
	
	public static <T> Tarjan<T> getStronglyConnectedComponents_UsingTarjansAlgorithm(T[] nodes, T[][] edges) {
		return new Tarjan<>(nodes, edges);
	}
	
	public static <T> PathBased<T> getStronglyConnectedComponents_UsingPathBasedAlgorithm(T[] nodes, T[][] edges) {
		return new PathBased<>(nodes, edges);
	}
	
	public static final class Kosaraju<T>{
		T[] nodes, edges[];
		private IntList[] adj, radj;
		private boolean[] vis;
		private LinkedList<Integer> L;
		private int[] assigns;
		Kosaraju(T[] nodes, T[][] edges) {
			this.nodes = nodes;
			this.edges = edges;
		}
		
		@SuppressWarnings("unchecked")
		public <C> void find(	Consumer<C> componentConsumer,
								Supplier<C> componentSupplier,
								ObjIntConsumer<C> componentAddFunction) {
			int[] arr = findAsArray();
			Object[] components = new Object[nodes.length]; //no generic arrays :( so we have to do some casting
			for(int i = 0; i < arr.length; i++) {
				int n = arr[i];
				if(components[n] == null)
					components[n] = componentSupplier.get();
				componentAddFunction.accept((C) components[n], i);
			}
			for(int i = 0; i < components.length; i++)
				if(components[i] != null)
					componentConsumer.accept((C) components[i]);
		}
		/** Finds the strongly connected components of the given graph, returning them as an {@code int[]} where index
		 * {@code i} in the array stores the index in {@code nodes} of the root node of the strongly connected
		 * component containing {@code nodes[i]}.*/
		public int[] findAsArray() {
			HashMap<T, Integer> map = mapToIndices(nodes);
			adj = getDirectedAdjacencyLists(nodes, edges, map);
			radj = getReverseAdjacencyLists(nodes, edges, map);
			vis = new boolean[nodes.length];
			L = new LinkedList<>();
			assigns = new int[nodes.length];
			Arrays.fill(assigns, -1);
			for(int u = 0; u < nodes.length; u++)
				visit(u);
			for(int u : L)
				assign(u, u);
			return assigns;
		}
		
		private void visit(int u) {
			if(!vis[u]) {
				vis[u] = true;
				for(int i = 0; i < adj[u].size; i++)
					visit(adj[u].get(i));
				L.addFirst(u);
			}
		}
		
		private void assign(int u, int root) {
			if(assigns[u] == -1) {
				assigns[u] = root;
				for(int i = 0; i < radj[u].size; i++)
					assign(radj[u].get(i), root);
			}
		}
	}
	
	public static final class Tarjan<T>{
		private Map<T, Integer> map;
		private final T[] nodes, edges[];
		private int index, indexes[], lowlinks[];
		private boolean[] onStack;
		private IntList stack, adj[];
		Tarjan(T[] nodes, T[][] edges) {
			this.nodes = nodes;
			this.edges = edges;
		}
		
		public <C> void find(	Consumer<C> componentConsumer,
								Supplier<C> componentSupplier,
								ObjIntConsumer<C> componentAddFunction) {
			new SCCsFinder<>(componentConsumer, componentSupplier, componentAddFunction).findSCCs();
		}
		
		private class SCCsFinder<C> {
			final Consumer<C> componentConsumer;
			final Supplier<C> componentSupplier;
			final ObjIntConsumer<C> componentAddFunction;
			SCCsFinder(Consumer<C> componentConsumer,
					Supplier<C> componentSupplier, ObjIntConsumer<C> componentAddFunction) {
				this.componentConsumer = componentConsumer;
				this.componentSupplier = componentSupplier;
				this.componentAddFunction = componentAddFunction;
			}
			
			void findSCCs() {
				index = 1;
				map = mapToIndices(nodes);
				stack = new IntList();
				indexes = new int[nodes.length];
				lowlinks = new int[nodes.length];
				onStack = new boolean[nodes.length];
				adj = getDirectedAdjacencyLists(nodes, edges, map);
				for(int v = 0; v < nodes.length; v++)
					if(indexes[v] == 0)
						strongconnect(v);
			}
			
			void strongconnect(int v) {
				indexes[v] = lowlinks[v] = index;
				index++;
				stack.add(v);
				onStack[v] = true;
				
				for(int i = 0; i < adj[v].size(); i++) {
					int w = adj[v].get(i);
					if(indexes[w] == 0) {
						strongconnect(w);
						lowlinks[v] = min(lowlinks[v], lowlinks[w]);
					}
					else if(onStack[w]) {
						lowlinks[v] = min(lowlinks[v], indexes[w]);
					}
				}
				
				if(lowlinks[v] == indexes[v]) {
					C component = componentSupplier.get();
					int w;
					do {
						w = stack.pop();
						onStack[w] = false;
						componentAddFunction.accept(component, w);
					}
					while(w != v);
					componentConsumer.accept(component);
				}
			}
		}
	}
	
	public static final class PathBased<T>{
		private final T[] nodes, edges[];
		private int C, preorderNumbers[];
		private IntList S, P, adj[];
		private Map<T, Integer> map;
		private boolean[] onS;
		PathBased(T[] nodes, T[][] edges){
			this.nodes = nodes;
			this.edges = edges;
		}
		
		public <C> void find(	Consumer<C> componentConsumer,
								Supplier<C> componentSupplier,
								ObjIntConsumer<C> componentAddFunction) {
			new SCCsFinder<>(componentConsumer, componentSupplier, componentAddFunction).findSCCs();
		}
		
		private class SCCsFinder<C>{
			final Consumer<C> componentConsumer;
			final Supplier<C> componentSupplier;
			final ObjIntConsumer<C> componentAddFunction;
			SCCsFinder(Consumer<C> componentConsumer,
					Supplier<C> componentSupplier, ObjIntConsumer<C> componentAddFunction) {
				this.componentConsumer = componentConsumer;
				this.componentSupplier = componentSupplier;
				this.componentAddFunction = componentAddFunction;
			}
			void findSCCs() {
				map = mapToIndices(nodes);
				adj = getDirectedAdjacencyLists(nodes, edges, map);
				C = 1;
				S = new IntList();
				P = new IntList();
				preorderNumbers = new int[nodes.length];
				onS = new boolean[nodes.length];
				for(int v = 0; v < nodes.length; v++)
					if(preorderNumbers[v] == 0)
						solveHelper(v);
			}
			
			void solveHelper(int v) {
				preorderNumbers[v] = C;
				C++;
				S.add(v);
				P.add(v);
				onS[v] = true;
				for(int i = 0; i < adj[v].size(); i++) {
					int w = adj[v].get(i);
					if(preorderNumbers[w] == 0)
						solveHelper(w);
					else if(onS[v]) 
						while(preorderNumbers[P.peekSafe()] > preorderNumbers[w])
							P.popSafe();
				}
				if(P.peekSafe() == v) {
					int popped;
					C component = componentSupplier.get();
					do {
						popped = S.pop();
						onS[popped] = false;
						componentAddFunction.accept(component, popped);
					}
					while(popped != v);
					P.popSafe();
					componentConsumer.accept(component);
				}
			}
		}
	}
	
	
	private static void testCountComponents() {
		String[] nodes = {"A", "B", "C", "D", "E", "F", "G", "H"};
		String[][] edges = {
				{"A","C"},
				{"D","E"},
				{"F","H"},
				{"G","H"},
				{"E","H"}
		};
		assert countComponents(nodes, edges) == 3;
		edges = new String[][] {
				{"A","C"},
				{"D","E"},
				{"F","H"},
				{"G","H"}
		};
		assert countComponents(nodes, edges) == 4;
		edges = new String[][] {
			{"A","C"},
			{"C","D"},
			{"D","E"},
			{"F","H"},
			{"G","H"}
		};
		assert countComponents(nodes, edges) == 3;
		edges = new String[][] {
			{"A","C"},
			{"C","D"},
			{"B","C"},
			{"D","E"},
			{"F","H"},
			{"G","H"}
		};
		assert countComponents(nodes, edges) == 2;
		assert countComponents(new String[] {}, new String[][] {}) == 0;
		assert countComponents(new String[] {"A"}, new String[][] {}) == 1;
		assert countComponents(new String[] {"A"}, new String[][] {}, Map.of("A", 0)) == 1;
		assert countComponents(new String[] {"A", "B"}, new String[][] {{"A", "B"}}, Map.of("A", 0, "B", 1)) == 1;
		assert countComponents(new String[] {"A", "B"}, new String[][] {{"A", "B"}, {"A", "B"}, {"A", "B"}}, Map.of("A", 0, "B", 1)) == 1;
		assert countComponents(new String[] {"A", "B"}, new String[][] {}, Map.of("A", 0, "B", 1)) == 2;
		assert countComponents(new IntList[] {new IntList(1, 2), new IntList(0), new IntList(0)}) == 1;
		assert countComponents(new IntList[] {new IntList(new int[] {1}), new IntList(0), new IntList(0)}) == 2;
		
		try { assert false; }
		catch(AssertionError ex) { System.out.println("Successful test of countComponents!");}
	}
	
	/** Returns the number of components in an undirected graph. This method creates an map to the indices in {@code nodes} and then delegates to
	 * {@link #countComponents(Object[], Object[][], Map)}.
	 */
	public static <T> int countComponents(T[] nodes, T[][] edges) {
		return countComponents(nodes, edges, mapToIndices(nodes));
	}
	
	/** Returns the number of components in an undirected graph. This method creates the adjacency lists for the graph and then delegates to
	 * {@link #countComponents(IntList[])}.
	 */
	public static <T> int countComponents(T[] nodes, T[][] edges, final Map<T, Integer> map) {
		return countComponents(getUndirectedAdjacencyLists(nodes, edges, map));
	
	}
	/** Returns the number of components in an undirected graph given the adjacency lists. The adjacency lists must represent an undirected graph.
	 * The graph may be a multigraph. This method assumes the number of nodes in the graph is equal to the length of the array of {@code IntList}s. */
	public static int countComponents(IntList[] adjacencyLists) {
		boolean[] vis = new boolean[adjacencyLists.length];
		int count = 0;
		for(int i = 0; i < vis.length; i++) {
			if(!vis[i]) {
				count++;
				countComponentsHelper(i, adjacencyLists, vis);
			}
		}
		return count;
	}
	
	
	private static void countComponentsHelper(final int index, IntList[] adj, boolean[] vis) {
		if(vis[index]) return;
		vis[index] = true;
		for(int i = 0; i < adj[index].size; i++)
			countComponentsHelper(adj[index].get(i), adj, vis);
	}
}
