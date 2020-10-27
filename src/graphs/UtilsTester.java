package graphs;

import static graphs.Utils.*;

import java.util.*;

import static java.util.Map.entry;

import static codetesting.Assertions.*;
/**
 * @author Sam Hooper
 *
 */
public class UtilsTester {
	public static void main(String[] args) {
	}
	private static void testIsDAG() {
		if(!areEnabled()) {System.out.println("Assertions are not enabled."); return;}
		Map<Object[], Boolean> expecteds = Map.ofEntries(
			entry(new Object[] {
					new String[] {"A"},
					new String[][] {}
			}, true),
			entry(new Object[] {
					new String[] {"A","B"},
					new String[][] {}
			}, true),
			entry(new Object[] {
					new String[] {"A","B"},
					new String[][] {{"A","B"}}
			}, true),
			entry(new Object[] {
					new String[] {"A","B"},
					new String[][] {{"B","A"}}
			}, true),
			entry(new Object[] {
					new String[] {"A","B"},
					new String[][] {{"B","A"},{"A","B"}}
			}, false),
			entry(new Object[] {
					new String[] {"A","B","C"},
					new String[][] {}
			}, true),
			entry(new Object[] {
					new String[] {"A","B","C"},
					new String[][] {{"A","B"}}
			}, true),
			entry(new Object[] {
					new String[] {"A","B","C"},
					new String[][] {{"A","B"},{"B","A"}}
			}, false),
			entry(new Object[] {
					new String[] {"A","B","C"},
					new String[][] {{"A","B"},{"A","C"}}
			}, true),
			entry(new Object[] {
					new String[] {"A","B","C"},
					new String[][] {{"A","B"},{"B","C"}}
			}, true),
			entry(new Object[] {
					new String[] {"A","B","C"},
					new String[][] {{"A","B"},{"B","C"},{"C","A"}}
			}, false),
			entry(new Object[] {
					new String[] {"A","B","C","D","E","F"},
					new String[][] {{"A","B"},{"B","C"},{"B","D"},{"F","C"},{"E","F"},{"D","E"},{"D","F"}}
			}, true),
			entry(new Object[] {
					new String[] {"A","B","C","D","E","F"},
					new String[][] {{"A","B"},{"B","C"},{"B","D"},{"F","C"},{"E","F"},{"D","E"},{"F","D"}}
			}, false),
			entry(new Object[] {
					new String[] {"A","B","C","D","E"},
					new String[][] {{"A","C"},{"C","E"},{"E","B"},{"B","C"},{"C","D"},{"D","A"}}
			}, false),
			entry(new Object[] {
					new String[] {"A","B","C","D","E"},
					new String[][] {{"A","C"},{"C","E"},{"E","B"},{"B","C"},{"C","D"},{"A","D"}}
			}, false),
			entry(new Object[] {
					new String[] {"A","B","C","D","E"},
					new String[][] {{"A","C"},{"C","E"},{"B","E"},{"B","C"},{"C","D"},{"A","D"}}
			}, true)
		);
		
		for(var e : expecteds.entrySet()) {
//			System.out.printf("%s : %b%n", Arrays.deepToString((Object[]) e.getKey()[1]), e.getValue());
			assert isDirectedAcyclicGraph((Object[]) e.getKey()[0], (Object[][]) e.getKey()[1]) == e.getValue();
		}
		
		System.out.println("Successful test of Utils.isDirectedAcyclicGraph");
	}
}
