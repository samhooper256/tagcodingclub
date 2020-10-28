package club_10_27_20;

import static graphs.Utils.getDirectedAdjacencyLists;
import static graphs.Utils.getIncomingEdgeCounts;
import static graphs.Utils.mapToIndices;

import java.util.*;
import java.util.function.IntConsumer;

import graphs.IntList;
import graphs.InvalidGraphException;

/**
 * @author Sam Hooper
 *
 */
public class Solution {
	public static void main(String[] args) {
		List<String> result = solve(new String[][] {{"a","b"}});
		System.out.println(result);
		result = solve(new String[][] {{"a","b"}, {"b","c"}});
		System.out.println(result);
		result = solve(new String[][] {{"w", "y"}, {"x", "z"}, {"y", "x"}});
		System.out.println(result);
		result = solve(new String[][] {{"w", "y"}, {"x", "z"}, {"y", "z"}});
		System.out.println(result);
		result = solve(new String[][] {{"q", "m"}, {"q", "o"}});
		System.out.println(result);
	}
	
	//Here is the solution we came up with at the meeting:
	public static List<String> solve(String[][] arr) {
		List<String> resultList = new LinkedList<>(); // we use a LinkedList, because we will be removing from the front often.
		
		List<String[]> arrList = new ArrayList<>();
		for(String[] pair : arr)
			arrList.add(pair);
		
		Set<String> strings = new HashSet<>();
		for(String[] pair : arrList)
			for(String item : pair)
				strings.add(item);
		
		
		//keep removing last until there's no pairs left.
		while(arrList.size() != 0)
			removeLast(resultList, arrList, strings);
		
		//Dump the remaining strings into the resultList. The remaining strings will be the ones who were first in a pair
		//but were not second in any other pair. If we conceptualize it as a graph, these will all be nodes with no incoming
		//edges. (these nodes are called "sources").
		for(String s : strings)
			resultList.add(0, s);
		
		return resultList;
		
	}

	private static void removeLast(List<String> resultList, List<String[]> arrList, Set<String> strings) {
		outer:
		for(int i = 0; i < arrList.size(); i++) {
			String[] pair = arrList.get(i);
			String second = pair[1];
			String first = pair[0];
			for(int j = i + 1; j < arrList.size(); j++) {
				String[] other = arrList.get(j);
				if(other[0].equals(second))
					continue outer;
			}
			
			//We now know that "second" is the last one in the sequence, so we can go ahead and add it:
			resultList.add(0, second);
			strings.remove(second); //remove it from our overall list of remaining strings as well.
			
			//Does the first string in this pair appear in any other pair?
			//If YES, leave it in. If NO, add it to the result list.
			boolean contains = false;
			for(String[] duo : arrList) {
				if(duo == pair)
					continue;
				for(String item : pair)
					if(item.equals(first))
						contains = true;
			}
			
			if(!contains) {
				resultList.add(0, first);
				strings.remove(first);
			}
			
			//remove this pair - we've handled everything we need to regarding it. Either we've added both of its strings
			//to our result list, or we've added the second string and the first appears as the second string in some other pair.
			arrList.remove(i);
			
			//finally, we can remove any other pair that has the same second element as our pair variable declared above.
			//We need to remove these, because otherwise they may be added counted multiple times.
			//We are safe to do this knowing that it would mess up the index variable "i" of our outer loop, since any pair
			//that has the same second element must occur after the pair at "i".
			for(int k = arrList.size() - 1; k >= 0; k--) {
				String[] duo = arrList.get(k);
				if(duo[1].equals(second))
					arrList.remove(k);
			}
			
			//go ahead and leave the loop. We don't want to repeat. The purpose of this method is to just remove the last
			//string (and any other strings that should be removed as a consequence). It should not remove multiple last strings.
			break outer;
		}
	}
	
	//Here is a solution that employs a Topological Sort algorithm known as "Kahn' Algorithm." It is more efficient than the one
	//above, coming in at O(V + E) time where V is the number of vertices in the graph (the number of strings in the sequence) and E
	//is the number of edges in the graph (the number of pairs).
	public static List<String> solve_Kahns(String[][] edges) {
		List<String> resultList = new ArrayList<>();
		//gets all the nodes (strings) in the graph (sequence).
		String[] nodes = Arrays.stream(edges)
			.flatMap(Arrays::stream)
			.distinct()
			.toArray(String[]::new);
		
		//A map that maps each node (string) to its index in the "nodes" array.
		Map<String, Integer> map = new HashMap<>();
		for(int i = 0; i < nodes.length; i++)
			map.put(nodes[i], i);
		
		//the adjacency lists for our graph. The list at index "i" holds the indices of all nodes "n" where there is an edge from i to n.
		ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
		for(int i = 0; i < nodes.length; i++)
			adj.add(new ArrayList<>());
		for(String[] edge : edges)
			adj.get(map.get(edge[0])).add(map.get(edge[1]));
		
		//the number of incoming edges of each node. The value at index "i" is the number of edges that are incoming to the node at nodes[i].
		int[] incoming = new int[nodes.length];
		for(String[] edge : edges)
			incoming[map.get(edge[1])]++;
		
		//the nodes with no incoming edges ("sources"):
		List<Integer> sources = new ArrayList<>();
		for(int i = 0; i < incoming.length; i++)
			if(incoming[i] == 0)
				sources.add(i);
		
		while(!sources.isEmpty()) {
			int n = sources.remove(sources.size() - 1); //it doesn't matter which one we remove.
			resultList.add(nodes[n]); //this node has nothing that comes before it, so we immediately add it to the end of our result list.
			
			//now, we remove the node "n" from this graph, which entails removing all of its outgoing edges as well. We know it has no incoming
			//nodes because it's a source.
			for(int m : adj.get(n)) {
				incoming[m]--;
				if(incoming[m] == 0)
					sources.add(m); 
			}
			adj.get(n).clear();
		}

		return resultList;
	}
	
	//Here's a solution using DFS. It has the same efficiency as Kahn's algorithm. I like it better because it's more concise and
	//seems simpler (to me :)
	
	public static List<String> solve_DFS(String[][] edges) {
		//I'm going to put the code in a class, because I think that makes it easier. The meat of the code is in the "solve" method.
		return new DFS(edges).solve();
	}
	
	private static class DFS {
		
		static final byte UNVISITED = 0, CURRENTLY_BEING_VISITED = 1, VISITED = 2;
		final String[][] edges;
		LinkedList<String> resultList;
		ArrayList<ArrayList<Integer>> adj;
		String[] nodes;
		byte[] marks;
		
		DFS(String[][] edges) {
			this.edges = edges;
		}
		
		List<String> solve() {
			resultList = new LinkedList<>();
			
			//Like in Kahn's algo, we want to get the nodes, the index map, and the adjacency lists.
			nodes = Arrays.stream(edges)
				.flatMap(Arrays::stream)
				.distinct()
				.toArray(String[]::new);
			
			Map<String, Integer> map = new HashMap<>();
			for(int i = 0; i < nodes.length; i++)
				map.put(nodes[i], i);
			
			adj = new ArrayList<>();
			for(int i = 0; i < nodes.length; i++)
				adj.add(new ArrayList<>());
			for(String[] edge : edges)
				adj.get(map.get(edge[0])).add(map.get(edge[1]));
			
			//the "marks" array stores info about each node. If index "i" stores UNVISITED, then nodes[i] has not been visited by
			//the DFS (and thus has not been added to the result list).
			//If it stores CURRENTLY_BEING_VISITED, it is currently being visited by our DFS.
			//if it stores VISITED, that node has been visited by the DFS and has been added to our result list.
			marks = new byte[nodes.length];
			Arrays.fill(marks, UNVISITED);
			
			for(int i = 0; i < nodes.length; i++)
				if(marks[i] == UNVISITED)
					dfs(i);
			
			return resultList;
		}
		
		//the "n" is for node
		private void dfs(int n) {
			if(marks[n] == VISITED)
				return; //no need to do any processing if we've already visited it.
			if(marks[n] == CURRENTLY_BEING_VISITED)
				throw new IllegalArgumentException("Invalid Graph"); //uh oh - we've found our back to the same node while processing it.
				//this means our graph has a cycle (a series of nodes that connect back to each other in a cycle). If it has a cycle,
				//there is no topological sort of it.
			
			marks[n] = CURRENTLY_BEING_VISITED;
			
			for(int m : adj.get(n))
				dfs(m);
			
			marks[n] = VISITED;
			resultList.addFirst(nodes[n]); //We add it to the front, since the recursive calls get popped of the stack in the reverse
			//order they were added. (Thus, this statement gets executed in the reverse order that the nodes should actually be in).
		}
	}
}
