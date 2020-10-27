package graphs;

import java.util.*;
import java.util.function.*;

import static graphs.GraphComponents.*;
import static graphs.Utils.arrayContains;
/**
 * @author Sam Hooper
 *
 */
public class GraphComponentsTester {
	static final Consumer<Set<String>> cc = System.out::println;
	static final Supplier<Set<String>> cs = HashSet::new;
	public static void main(String[] args) {
		if(!codetesting.Assertions.areEnabled()) { System.out.println("Assertions are not enabled."); return; }
		testTarjans_EmptyGraph();
		testTarjans_SingleNode();
		testTarjans_2Nodes0Edges();
		testTarjans_DAGsize2();
		testTarjans_Length2Cycle();
		testTarjans_Length3Cycle();
		testTarjans_DAGsize3_1();
		testTarjans_DAGsize3_2();
		testTarjans_DAGsize3_3();
		testTarjans_3Components();
		testTarjans_Complex_1();
		testTarjans_NestedSCCs();
		System.out.println("Success!");
	}
	
	private static Set<Set<String>> getAsSet(String[] nodes, String[][] edges) {
		Set<Set<String>> set = new HashSet<>();
		getStronglyConnectedComponents_UsingPathBasedAlgorithm(nodes, edges)
		.find((c) -> set.add(c), cs, (c, node) -> c.add(nodes[node]));
		return set;
	}
	private static void testTarjans_2Nodes0Edges() {
		assert getAsSet(new String[] {"A","B"}, new String[][] {}).equals(Set.of(Set.of("A"), Set.of("B")));
	}
	private static void testTarjans_DAGsize2() {
		assert getAsSet(new String[] {"A","B"}, new String[][] {{"A","B"}}).equals(Set.of(Set.of("A"), Set.of("B")));
	}
	private static void testTarjans_DAGsize3_1() {
		assert getAsSet(new String[] {"A","B","C"}, new String[][] {{"A","B"},{"B","C"}}).
		equals(Set.of(Set.of("A"), Set.of("B"), Set.of("C")));
	}
	private static void testTarjans_DAGsize3_2() {
		assert getAsSet(new String[] {"A","B","C"}, new String[][] {{"A","B"},{"A","C"}}).
		equals(Set.of(Set.of("A"), Set.of("B"), Set.of("C")));
	}
	private static void testTarjans_DAGsize3_3() {
		assert getAsSet(new String[] {"A","B","C"}, new String[][] {{"A","B"},{"A","C"},{"B","C"}}).
		equals(Set.of(Set.of("A"), Set.of("B"), Set.of("C")));
	}
	private static void testTarjans_Length2Cycle() {
		assert getAsSet(new String[] {"A","B"}, new String[][] {{"A","B"},{"B","A"}}).equals(Set.of(Set.of("A","B")));
	}
	private static void testTarjans_Length3Cycle() {
		assert getAsSet(new String[] {"A","B","C"}, new String[][] {{"A","B"},{"B","C"},{"C","A"}})
		.equals(Set.of(Set.of("A","B","C")));
	}
	private static void testTarjans_SingleNode() {
		assert getAsSet(new String[] {"A"}, new String[][] {}).equals(Set.of(Set.of("A")));
	}
	private static void testTarjans_EmptyGraph() {
		assert getAsSet(new String[] {}, new String[][] {}).equals(Set.of());
	}
	private static void testTarjans_3Components() {
		assert getAsSet(new String[] {"A","B","C","D"},
		new String[][] {{"A","B"},{"A","C"},{"B","C"},{"B","D"},{"D","C"},{"D","B"}}).
		equals(Set.of(Set.of("A"), Set.of("C"), Set.of("B","D")));
	}
	private static void testTarjans_NestedSCCs() {
		assert getAsSet(new String[] {"A","B","C","D"},
		new String[][] {{"A","B"},{"A","C"},{"C","B"},{"B","D"},{"D","C"},{"D","B"}}).
		equals(Set.of(Set.of("A"), Set.of("B","C","D")));
	}
	private static void testTarjans_Complex_1() {
		assert getAsSet(new String[] {"A","B","C","D","E","F","G","H","I","J"},
		new String[][] {
			{"A","D"},{"D","B"},{"B","C"},{"C","E"},{"E","D"},{"E","F"},{"F","G"},{"G","F"},
			{"C","H"},{"H","J"},{"I","H"},{"J","I"}
		}).
		equals(Set.of(Set.of("A"), Set.of("B","C","D","E"), Set.of("F","G"), Set.of("H","I","J")));
	}
	
	private static void testKosarajusUsingArray() {
		{	String[] nodes = {"A","B","C","D","E","F","G","H","I","J"};
			String[][] edges = {
					{"A","D"},{"D","B"},{"B","C"},{"C","E"},{"E","D"},{"E","F"},{"F","G"},{"G","F"},
					{"C","H"},{"H","J"},{"I","H"},{"J","I"}
			};
			int[] group1 = {0}, group4 = {1,2,3,4}, group3 = {7,8,9}, group2 = {5,6};
			int[] result = getStronglyConnectedComponents_UsingKosarajusAlgorithm(nodes, edges).findAsArray();
			assert arrayContains(group1, result[0]);
			assert arrayContains(group4, result[1]);
			assert arrayContains(group4, result[2]);
			assert arrayContains(group4, result[3]);
			assert arrayContains(group4, result[4]);
			assert arrayContains(group3, result[7]);
			assert arrayContains(group3, result[8]);
			assert arrayContains(group3, result[9]);
			assert arrayContains(group2, result[5]);
			assert arrayContains(group2, result[6]);
			assert result[1] == result[2] && result[2] == result[3] && result[3] == result[4];
			assert result[7] == result[8] && result[8] == result[9];
			assert result[5] == result[6];	}
		{	String[] nodes = new String[] {"C", "H", "I", "J"};
			String[][] edges = new String[][] {
				{"C","H"},{"H","J"},{"J","I"},{"I","H"}
			};
			int[] result = getStronglyConnectedComponents_UsingKosarajusAlgorithm(nodes, edges).findAsArray();
			int[] group1 = new int[] {0}, group3 = new int[] {1,2,3};
			assert arrayContains(group1, result[0]);
			assert arrayContains(group3, result[1]);
			assert arrayContains(group3, result[2]);
			assert arrayContains(group3, result[3]);
			assert result[1] == result[2] && result[2] == result[3];	}
		
		
		{	String[] nodes = new String[] {"A", "B"};
			String[][] edges = new String[][] {
				{"A","B"},{"B","A"}
			};
			int[] result = getStronglyConnectedComponents_UsingKosarajusAlgorithm(nodes, edges).findAsArray();
			int[] group1 = new int[] {0};
			assert arrayContains(group1, result[0]);
			assert arrayContains(group1, result[1]);	}
		{	String[] nodes = new String[] {"A", "B"};
			String[][] edges = new String[][] {
				{"A","B"},
			};
			int[] result = getStronglyConnectedComponents_UsingKosarajusAlgorithm(nodes, edges).findAsArray();
			int[] group1 = new int[] {0}, group1_2 = new int[] {1};
			assert arrayContains(group1, result[0]);
			assert arrayContains(group1_2, result[1]);	}
		
		{	String[] nodes = new String[] {"A", "B", "C", "D", "E"};
			String[][] edges = new String[][] {
				{"A","B"},{"B","C"},{"C","D"},{"D","E"},{"E","A"}
			};
			int[] result = getStronglyConnectedComponents_UsingKosarajusAlgorithm(nodes, edges).findAsArray();
			assert result.length == nodes.length;
			int[] group5 = new int[] {0,1,2,3,4};
			assert arrayContains(group5, result[0]);
			assert arrayContains(group5, result[1]);
			assert arrayContains(group5, result[2]);
			assert arrayContains(group5, result[3]);
			assert arrayContains(group5, result[4]);
			for(int i = 0; i < result.length - 1; i++) assert result[i] == result[i+1];
		}
		
		{	String[] nodes = new String[] {"A", "B", "C", "D"};
			String[][] edges = new String[][] {
				{"A","B"},{"B","C"},{"C","D"},{"D","A"},{"B","A"}
			};
			int[] result = getStronglyConnectedComponents_UsingKosarajusAlgorithm(nodes, edges).findAsArray();
			assert result.length == nodes.length;
			int[] group4 = new int[] {0,1,2,3};
			assert arrayContains(group4, result[0]);
			assert arrayContains(group4, result[1]);
			assert arrayContains(group4, result[2]);
			assert arrayContains(group4, result[3]);
			for(int i = 0; i < result.length - 1; i++) assert result[i] == result[i+1];	}
		
		{	String[] nodes = new String[] {"A", "B", "C", "D"};
			String[][] edges = new String[][] {
				{"A","B"},{"B","A"},{"A","C"},{"C","D"},{"D","C"}
			};
			int[] result = getStronglyConnectedComponents_UsingKosarajusAlgorithm(nodes, edges).findAsArray();
			assert result.length == nodes.length;
			int[] group2_1 = new int[] {0,1}, group2_2 = new int[] {2,3};
			assert arrayContains(group2_1, result[0]);
			assert arrayContains(group2_1, result[1]);
			assert arrayContains(group2_2, result[2]);
			assert arrayContains(group2_2, result[3]);
			assert result[0] == result[1] && result[2] == result[3];	}
		
		assert getStronglyConnectedComponents_UsingKosarajusAlgorithm(new String[] {}, new String[][] {}).findAsArray().length == 0;
		
		System.out.println("Successful test of Kosaraju's (using array)!");
	}
}
