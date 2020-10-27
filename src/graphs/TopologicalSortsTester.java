package graphs;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import static codetesting.Assertions.assertThrows;
import static graphs.TopologicalSorts.*;

/**
 * @author Sam Hooper
 *
 */
public class TopologicalSortsTester {
	public static void main(String[] args) {
		testSortingAlgorithms();
		testIsTopological();
		testGetMinimumPredecessors();
	}
	
	private static void testGetMinimumPredecessors() {
		String[] nodes = {"A"};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, new String[][] {}).equals(Collections.<String>emptySet());
		nodes = new String[] {"A", "B"};
		String[][] edges = {{"A", "B"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		nodes = new String[] {"A", "B", "C"};
		edges = new String[][] {{"A", "B"}, {"B", "C"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A", "B"));
		edges = new String[][] {{"A", "C"}, {"A", "C"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A"));
		edges = new String[][] {{"A", "C"}, {"B", "C"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A", "B"));
		nodes = new String[] {"A", "B", "C", "D"};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A", "B"));
		assert getMinimumPrecessorsInTopologicalSort("D", nodes, edges).equals(Collections.<String>emptySet());
		edges = new String[][] {{"A", "B"}, {"C", "D"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("D", nodes, edges).equals(Set.of("C"));
		edges = new String[][] {{"A", "B"}, {"A", "C"}, {"C", "D"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("D", nodes, edges).equals(Set.of("A", "C"));
		edges = new String[][] {{"A", "B"}, {"A", "C"}, {"C", "D"}, {"B", "D"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("D", nodes, edges).equals(Set.of("A", "B", "C"));
		edges = new String[][] {{"A", "B"}, {"A", "C"}, {"C", "D"}, {"B", "C"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A", "B"));
		assert getMinimumPrecessorsInTopologicalSort("D", nodes, edges).equals(Set.of("A", "B", "C"));
		edges = new String[][] {{"A", "B"}, {"A", "C"}, {"C", "D"}, {"B", "C"}, {"B", "D"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A", "B"));
		assert getMinimumPrecessorsInTopologicalSort("D", nodes, edges).equals(Set.of("A", "B", "C"));
		edges = new String[][] {{"A", "B"}, {"A", "C"}, {"C", "D"}, {"B", "C"}, {"B", "D"}, {"A", "D"}};
		assert getMinimumPrecessorsInTopologicalSort("A", nodes, edges).equals(Collections.<String>emptySet());
		assert getMinimumPrecessorsInTopologicalSort("B", nodes, edges).equals(Set.of("A"));
		assert getMinimumPrecessorsInTopologicalSort("C", nodes, edges).equals(Set.of("A", "B"));
		assert getMinimumPrecessorsInTopologicalSort("D", nodes, edges).equals(Set.of("A", "B", "C"));
		try { assert false; }
		catch(AssertionError e) { System.out.println("Successful test of getMinimumPredecessors!"); }
	}
	
	private static void testIsTopological() {
		String[] nodes = {"A", "B", "C", "D", "E", "F", "G"};
		String[][] edges = {
				{"A", "B"}, {"C", "B"}, {"A", "G"}, {"B", "F"},
				{"F", "D"}, {"D", "E"}, {"B", "E"}, {"E", "G"}
		};
		Map<List<String>, Boolean> tests = 
		Map.ofEntries(
				Map.entry(List.of("A", "C", "B", "F", "D", "E", "G"), true),
				Map.entry(List.of("A", "C", "B", "F", "D", "E", "G", "H"), false),
				Map.entry(List.of("A", "B", "C", "D", "E", "F", "G"), false),
				Map.entry(List.of("C", "A", "B", "F", "D", "E", "G"), true),
				Map.entry(List.of("A", "D", "C", "B", "E", "F", "G"), false),
				Map.entry(List.of("A", "D", "C", "B", "E", "G", "F"), false),
				Map.entry(List.of("E", "D", "C", "B", "A", "F", "G"), false),
				Map.entry(List.of("A", "C", "D", "E", "F", "B", "G"), false),
				Map.entry(List.of("A", "Q", "C", "B", "E", "F", "G"), false),
				Map.entry(List.of("A", "E", "C", "B", "F", "G", "D"), false),
				Map.entry(List.of("B", "D", "C", "A", "E", "F", "G"), false),
				Map.entry(List.of("B", "D", "C", "A", "E", "F"), false),
				Map.entry(List.of(), false)
		);
		for(var e : tests.entrySet())
			assert isTopological(e.getKey(), nodes, edges) == e.getValue(); //will perform boolean equality
		
		assertThrows(InvalidGraphException.class,
				() -> isTopological(List.of("A","B"), new String[] {"A","B"}, new String[][] {{"A","B"}, {"B","A"}}));
		assertThrows(InvalidGraphException.class,
				() -> isTopological(List.of("C","A","B"), new String[] {"A","B","C"}, new String[][] {{"C","A"}, {"A","B"}, {"B","A"}}));
		
		try { assert false; }
		catch(AssertionError e) { System.out.println("Successful test of isTopological!"); }
	}
	
	private static void testSortingAlgorithms() {
		String[] nodes = {"-A", "+A", "A","B","C","D","E","F","G","H","I","J","K","L","M"};
		String[][] edges = {
				{"-A", "A"}, {"+A", "A"},
				{"A", "B"}, {"A", "C"}, {"B", "D"}, {"C", "D"}, {"B", "E"}, {"C", "F"}, {"F", "G"}, {"E", "G"},
				{"G", "H"}, {"H", "I"}, {"I", "J"}, {"J", "K"}, {"K", "L"}, {"L", "M"}
		};
		List<String> sort1 = topologicalSort_UsingKahnsAlgorithm_AsList(nodes, edges);
		List<String> sort2 = DFSTopologicalSort.topologicalSort_UsingDFS(nodes, edges);
		assert sort1.size() == nodes.length && sort2.size() == nodes.length;
		for(String[] edge : edges)
			assert comesBefore(edge[0], edge[1], sort1) && comesBefore(edge[0], edge[1], sort2);
		
		nodes = new String[]{"A"};
		edges = new String[][]{};
		sort1 = topologicalSort_UsingKahnsAlgorithm_AsList(nodes, edges);
		sort2 = DFSTopologicalSort.topologicalSort_UsingDFS(nodes, edges);
		
		assert sort1.size() == 1 && sort2.size() == 1 && sort1.get(0).equals(nodes[0]) && sort2.get(0).equals(nodes[0]);
		
		assertThrows(InvalidGraphException.class,
				() -> topologicalSort_UsingKahnsAlgorithm_AsList(new String[] {"A","B"}, new String[][] {{"A","B"}, {"B","A"}}));
		assertThrows(InvalidGraphException.class,
				() -> topologicalSort_UsingKahnsAlgorithm_AsList(new String[] {"A","B","C"}, new String[][] {{"C","A"}, {"A","B"}, {"B","A"}}));
		assertThrows(InvalidGraphException.class,
				() -> topologicalSort_UsingKahnsAlgorithm_AsInts(new String[] {"A","B"}, new String[][] {{"A","B"}, {"B","A"}}));
		assertThrows(InvalidGraphException.class,
				() -> topologicalSort_UsingKahnsAlgorithm_AsInts(new String[] {"A","B","C"}, new String[][] {{"C","A"}, {"A","B"}, {"B","A"}}));
		assertThrows(InvalidGraphException.class,
				() -> DFSTopologicalSort.topologicalSort_UsingDFS(new String[] {"A","B"}, new String[][] {{"A","B"}, {"B","A"}}));
		assertThrows(InvalidGraphException.class,
				() -> DFSTopologicalSort.topologicalSort_UsingDFS(new String[] {"A","B","C"}, new String[][] {{"C","A"}, {"A","B"}, {"B","A"}}));
		
		try { assert false; }
		catch(AssertionError e) { System.out.println("Successful test of Kahn's and DFS!"); }
	}
	
	/** Returns {@code true} if {@code a} comes before {@code b} in {@code arr} (using {@link Objects#equals(Object)}
	 * to compare), false otherwise. Returns false if {@code a.equals(b)} or if neither item is in the {@code arr} */
	private static <T> boolean comesBefore(T a, T b, T[] arr) {
		if(a.equals(b)) return false;
		boolean foundA = false;
		for(int i = 0; i < arr.length; i++)
			if(Objects.equals(a, arr[i]))
				foundA = true;
			else if(Objects.equals(b, arr[i]))
				return foundA;
		return false;
	}
	/** Returns {@code true} if {@code a} comes before {@code b} in {@code list} (using {@link Objects#equals(Object)}
	 * to compare), false otherwise. Returns false if {@code a.equals(b)} or if neither item is in the list */
	private static <T> boolean comesBefore(T a, T b, List<T> list) {
		if(a.equals(b)) return false;
		boolean foundA = false;
		for(T item : list)
			if(Objects.equals(a, item))
				foundA = true;
			else if(Objects.equals(b, item))
				return foundA;
		return false;
	}
	
	static final String[] terms = {"Anonymous Classes", "Enums", "Lambdas", "Concurrency", "Annotations", "Exception Handling",
			"Streams", "Regex", "Bitwise Operators", "Generics", "Access Specifiers", "Number Literal Forms",
			"Other Primitive Types", "Intersection Types", "Switch Statements", "Packages", "Final Keyword",
			"Nested Types", "Exceptions", "Assert Keyword", "Wildcards", "Functional Interfaces",
			"Default Methods", "Method Reference Expressions", "Static/Instance Initializers", "Unicode",
			"Twos Complement"};
	static final String[][] edges = {
			{"Access Specifiers", "Enums"},
			{"Access Specifiers", "Nested Types"},
			{"Access Specifiers", "Anonymous Classes"},
			{"Nested Types", "Anonymous Classes"},
			{"Anonymous Classes", "Lambdas"},
			{"Anonymous Classes", "Enums"}, //Enum constant bodies implicitly declare anonymous classes (JLS 8.9.1)
			{"Default Methods", "Functional Interfaces"},
			{"Functional Interfaces", "Lambdas"},
			{"Functional Interfaces", "Method Reference Expressions"},
			{"Lambdas", "Streams"},
			{"Method Reference Expressions", "Streams"},
			{"Generics", "Intersection Types"},
			{"Generics", "Streams"},
			{"Generics", "Lambdas"},
			{"Generics", "Wildcards"},
			{"Packages", "Access Specifiers"},
			{"Enums", "Switch Statements"},
			{"Other Primitive Types", "Number Literal Forms"},
			{"Twos Complement", "Number Literal Forms"},
			{"Twos Complement", "Bitwise Operators"},
			{"Number Literal Forms", "Bitwise Operators"},
			{"Exceptions", "Exception Handling"},
			{"Final Keyword", "Enums"}, //because enum constants are final
			{"Wildcards", "Streams"},
			{"Annotations", "Functional Interfaces"},
			{"Unicode", "Regex"}
		};
	private static void testTeachingTopics() {
		final String[] testTerms = {"Anonymous Classes", "Enums", "Lambdas",
				"Streams", "Bitwise Operators", "Generics", "Number Literal Forms",
				"Other Primitive Types", "Intersection Types", "Switch Statements",
				"Nested Types", "Wildcards", "Functional Interfaces"};
		final String[][] testEdges = {
				{"Anonymous Classes", "Lambdas"},
				{"Anonymous Classes", "Enums"}, //Enum constant bodies implicitly declare anonymous classes (JLS 8.9.1)
				{"Functional Interfaces", "Lambdas"},
				{"Lambdas", "Streams"},
				{"Generics", "Intersection Types"},
				{"Generics", "Streams"},
				{"Generics", "Lambdas"},
				{"Generics", "Wildcards"},
				{"Enums", "Switch Statements"},
				{"Other Primitive Types", "Number Literal Forms"},
				{"Number Literal Forms", "Bitwise Operators"},
				{"Nested Types", "Anonymous Classes"},
				{"Wildcards", "Streams"},
			};
		System.out.println("\n-== Sorted: ==-");
		topologicalSort_UsingKahnsAlgorithm_AsList(terms, edges).forEach(System.out::println);
		timing.TimeUtils.timeAndDetail(() ->
		{
			System.out.println("\nSo many:");
			System.out.println(allTopologicalSorts(testTerms, testEdges).size());
		}, "Fat test");
	}
	
	private static void testWithUserInput() {
		Scanner in = new Scanner(System.in);
		while(true) {
			String line = in.nextLine().trim().replaceAll("\\P{ASCII}", "");
			try {
				System.out.println(getMinimumPrecessorsInTopologicalSort(line, terms, edges));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
