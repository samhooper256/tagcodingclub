package graphs;

import java.util.*;
import java.util.stream.*;

/**
 * @author Sam Hooper
 *
 */
public class DFSTesting {
	private static int[][] ADJACENTS = {{-1,0},{0,1},{1,0},{0,-1}};
	public static void main(String[] args) {
		char[][] map = {
				"abcd".toCharArray(),
				"efgh".toCharArray(),
				"ijkl".toCharArray(),
				"mnop".toCharArray(),
		};
		
		final int startRow = 1, startCol = 1;
		dfsIterative(map, startRow, startCol);
		dfsRecursive(map, startRow, startCol);
//		dfs(new char[][] {{'c'},{'b'},{'a'}}, 2, 0);
	}
	private static void dfsIterative(char[][] map, final int startRow, final int startCol) {
		class StackItem<T>{
			T item; boolean needsPreprocessing;
			public StackItem(T item, boolean needsPreprocessing) {
				this.item = item;
				this.needsPreprocessing = needsPreprocessing;
			}
			
		}
		boolean[][] hasBeenOnStack = new boolean[map.length][map[0].length];
		hasBeenOnStack[startRow][startCol] = true;
		Stack<StackItem<int[]>> stack = new Stack<>();
		StringBuilder sb = new StringBuilder();
		stack.push(new StackItem<>(new int[] {startRow, startCol}, true));
		int[] currentlyProcessing = null;
		while(!stack.isEmpty()) {
			StackItem<int[]> stackItem = stack.pop();
			currentlyProcessing = stackItem.item;
			int row = currentlyProcessing[0], col = currentlyProcessing[1];
			if(stackItem.needsPreprocessing) {
				boolean addedSomethingToStack = false;
				//before stuff here:
				System.out.printf("Before recursion (%d, %d)[%c]%n", row, col, map[row][col]);
				sb.append('>').append(map[row][col]);
				//recursion:
				for(int i = ADJACENTS.length - 1; i >= 0; i--) {
					int[] adj = ADJACENTS[i];
					int nr = row + adj[0], nc = col + adj[1];
					if(nr >= 0 && nr < map.length && nc >= 0 && nc < map[nr].length && !hasBeenOnStack[nr][nc]) {
						if(!addedSomethingToStack) {
							stack.push(new StackItem<>(currentlyProcessing, false));
							addedSomethingToStack = true;
						}
						hasBeenOnStack[nr][nc] = true;
						stack.push(new StackItem<>(new int[] {nr, nc}, true));
					}
				}
				if(addedSomethingToStack)
					continue;
			}
			
			//after stuff here:
			System.out.printf("After recursion  (%d, %d)[%c]%n", row, col, map[row][col]);
			sb.append('<').append(map[row][col]);
			System.out.println(sb);
		}
	}
	
	
	private static void dfsRecursive(char[][] map, int startRow, int startCol) {
		boolean[][] vis = new boolean[map.length][map[startRow].length];
		StringBuilder sb = new StringBuilder();
		dfsRecursiveHelper(map, startRow, startCol, vis, sb);
		System.out.println(sb);
	}
	
	private static void dfsRecursiveHelper(char[][] map, int row, int col, boolean[][] vis, StringBuilder sb) {
		if(vis[row][col]) return;
		vis[row][col] = true;
		sb.append('>').append(map[row][col]);
		for(int[] adj : ADJACENTS) {
			int nr = row + adj[0], nc = col + adj[1];
			if(nr >= 0 && nr < map.length && nc >= 0 && nc < map[row].length)
				dfsRecursiveHelper(map, nr, nc, vis, sb);
		}
		sb.append('<').append(map[row][col]);
	}
}
