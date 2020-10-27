package graphs;

import timing.TimeUnit;
import static timing.TimeUtils.timeAndDetail;

import java.util.*;;
/**
 * @author Sam Hooper
 *
 */
public class Matrices {
	private static final int SPLIT_POWER_THRESHOLD = 1800; //the threshold after which a matrix power operation will split
	//into multiplying smaller powers together instead of multiplying by the base sequentially
	
	public static void main(String[] args) {
		int[][] mat1 = {
				{1,6,3},
				{3,-1,4}
		};
		int[][] mat2 = {
				{3,6},
				{9,10},
				{-1,-1}
		};
		Arrays.stream(multiply(mat1,mat2)).forEach(x -> System.out.println(Arrays.toString(x)));
		
		int[][] sq = {
				{1,2},
				{3,4}
		};
		System.out.println(Arrays.deepToString(matrixPower(sq, 0)));
	}
	
	public static int[][] multiply(int[][] mat1, int[][] mat2){
		if(mat1[0].length != mat2.length)
			throw new IllegalArgumentException("Cannot multiply matrices - Invalid dimensions");
		int[][] ret = new int[mat1.length][mat2[0].length];
		for(int i = 0; i < ret.length; i++) {
			for(int j = 0; j < ret[i].length; j++) {
				int sum = 0;
				for(int k = 0; k < mat2.length; k++)
					sum += mat1[i][k] * mat2[k][j];
				ret[i][j] = sum;
			}
		}
		return ret;
	}
	
	public static int[][] identity(int size){
		int[][] mat = new int[size][size];
		for(int i = 0; i < size; i++)
			mat[i][i] = 1;
		return mat;
	}
	
	private static class MatrixPower {
		final int initPower, initMat[][];
		int highestCombinePower = 1;
		Map<Integer, int[][]> solved;
		/** {@code power} must be greater than 0.*/
		MatrixPower(int[][] mat, int power){
			this.initMat = mat;
			this.initPower = power;
		}
		
		int[][] solve() {
			solved = new HashMap<>();
			solved.put(1, initMat);
			return solve(initPower);
		}
		int[][] solve(final int power){ 
			Integer powerObj = Integer.valueOf(power);
			{
				int[][] existingSolution;
				if((existingSolution = solved.get(powerObj)) != null) return existingSolution;
			}
			int combinePower = highestCombinePower;
			while(combinePower > power)
				combinePower >>= 1;
			int extra = power - setBitNumber(power);
			while(combinePower <= power) {
				int div = power / combinePower;
				if(div == 1) {
					if(extra == 0)
						return solved.get(combinePower);
					int[][] mult = multiply(solved.get(combinePower), solve(extra));
					solved.put(powerObj, mult);
					return mult;
				}
				int[][] mult = multiply(solved.get(combinePower), solved.get(combinePower));
				combinePower <<= 1;
				solved.put(combinePower, mult);
				highestCombinePower = combinePower;
			}
			System.out.printf("Made it down to return null - combinePower=%d, extra=%d,solved=%s%n", combinePower, extra,solved);
			return null;
		}
	}
	
	/** returns 2^(most significant bit of n). For example, setBitNumber(273) returns 256. */
	static int setBitNumber(int n) 
    { 
        n |= n >> 1; 
        n |= n >> 2; 
        n |= n >> 4; 
        n |= n >> 8; 
        n |= n >> 16; 
        n = n + 1; 
        return (n >> 1); 
    } 
	
	
	public static int[][] matrixPower(int[][] mat, final int power){
		if(power == 0)
			return identity(mat.length);
		if(power >= SPLIT_POWER_THRESHOLD)
			return new MatrixPower(mat, power).solve();
		
		int[][] result = mat;
		for(int i = 0; i < power - 1; i++)
			result = multiply(result, mat);
		return result;
	}
	
	/**
	 * Returns a deep copy of the given matrix. If the number of rows in {@code mat} is zero, returns a 0 x 0 matrix.
	 */
	public static int[][] deepCopyMatrix(int[][] mat) {
		if(mat.length == 0)
			return new int[0][0];
		if(mat[0].length == 0)
			return new int[0][mat[0].length];
		int[][] newMat = new int[mat.length][mat[0].length];
		for(int i = 0; i < newMat.length; i++)
			System.arraycopy(mat[i], 0, newMat[i], 0, newMat[i].length);
		return newMat;
	}
}
