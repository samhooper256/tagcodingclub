package club10_06_20;

import java.io.*;
import java.util.Arrays;

public class SolutionLiveDemo {
	static int[][] moves = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("src/club_10_6_20/mazes.dat"));
		String[] arr = in.lines().toArray(String[]::new);
		for(int i = 0; i < arr.length;) {
			int rows = Integer.parseInt(arr[i].split(" ")[0]);
			char[][] maze = Arrays.stream(arr, i + 1, i + rows + 1).map(String::toCharArray).toArray(char[][]::new);
			solveAndPrint(maze);
			i += rows + 1;
		}
	}
	
	private static void solveAndPrint(char[][] maze) {
		int srow = 0, scol = 0;
		outer:
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				if(maze[i][j] == 'S') {
					srow = i;
					scol = j;
					break outer;
				}
			}
		}
		boolean[][] visited = new boolean[maze.length][maze[0].length];
		solve(maze, srow, scol, visited);
		for(char[] row : maze)
			System.out.println(new String(row));
		System.out.println();
	}
	
	private static boolean solve(char[][] maze, int r, int c, boolean[][] vis) {
		if(!(r >= 0 && r < maze.length && c >= 0 && c < maze[r].length))
			return false;
		if(vis[r][c])
			return false;
		if(maze[r][c] == '#')
			return false;
		if(maze[r][c] == 'E')
			return true;
		vis[r][c] = true;
		boolean solvedOnThisPath = false;
		for(int[] move : moves) {
			int nr = r + move[0], nc = c + move[1];
			if(solve(maze, nr, nc, vis)) {
				solvedOnThisPath = true;
				break;
			}
		}
		
		if(solvedOnThisPath && maze[r][c] != 'S') {
			maze[r][c] = '.';
		}
		
		return solvedOnThisPath;
	}
}
