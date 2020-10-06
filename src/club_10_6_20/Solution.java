package club_10_6_20;

import java.io.*;
import java.util.Arrays;

/**
 * @author Sam Hooper
 *
 */
public class Solution {
	
	private static int[][] moves = {{-1,0}, {0,1}, {1,0}, {0,-1}};
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("src/club_10_6_20/mazes.dat"));
		String[] lines = in.lines().toArray(String[]::new);
		for(int i = 0; i < lines.length;) {
			int rows = Integer.parseInt(lines[i].split(" ")[0]);
			char[][] maze = Arrays.stream(lines, i + 1, i + 1 + rows).map(String::toCharArray).toArray(char[][]::new);
			solveAndPrint(maze);
			i += 1 + rows;
		}
	}
	
	static void solveAndPrint(char[][] maze) {
		solve(maze);
		for(char[] row : maze)
			System.out.println(new String(row));
		System.out.println();
	}
	static void solve(char[][] maze) {
		boolean[][] visited = new boolean[maze.length][maze[0].length];
		int srow = 0, scol = 0;
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				if(maze[i][j] == 'S') {
					srow = i; scol = j;
					break;
				}
			}
		}
		solveHelper(maze, visited, srow, scol);
	}
	static boolean solveHelper(char[][] maze, boolean[][] visited, int row, int col) {
		if(row < 0 || row >= maze.length || col < 0 || col >= maze[row].length || visited[row][col] || maze[row][col] == '#')
			return false;
		if(maze[row][col] == 'E')
			return true;
		boolean found = false;
		visited[row][col] = true;
		for(int[] move : moves) {
			int dx = move[0], dy = move[1];
			if(solveHelper(maze, visited, row + dx, col + dy)) {
				found = true;
				break;
			}
		}
		if(found && maze[row][col] != 'S')
			maze[row][col] = '.';
		return found;
	}
}
