package club9_15_20;

import java.util.*;
import java.util.function.IntUnaryOperator;

public class Solutions {
	public static void main(String[] args) {
		checkSolutionsForValidity();
		System.out.printf("All %d solutions are valid!%n", solutions.size());
	}
	
	private static final LinkedHashMap<String, IntUnaryOperator> solutions;
	private static final HashMap<Integer, Integer> testCases;
	
	static {
		testCases = new HashMap<>();
		testCases.put(0, 1);
		testCases.put(1, 1);
		testCases.put(9, 1);
		testCases.put(10, 2);
		testCases.put(11, 2);
		testCases.put(37, 2);
		testCases.put(99, 2);
		testCases.put(100, 3);
		testCases.put(101, 3);
		testCases.put(573, 3);
		testCases.put(999, 3);
		testCases.put(1_000, 4);
		testCases.put(1_001, 4);
		testCases.put(9_999, 4);
		testCases.put(10_000, 5);
		testCases.put(10_001, 5);
		testCases.put(99_999, 5);
		testCases.put(100_000, 6);
		testCases.put(100_001, 6);
		testCases.put(999_999, 6);
		testCases.put(1_000_000, 7);
		testCases.put(1_000_001, 7);
		testCases.put(9_999_999, 7);
		testCases.put(10_000_000, 8);
		testCases.put(10_000_001, 8);
		testCases.put(99_999_999, 8);
		testCases.put(100_000_000, 9);
		testCases.put(100_000_001, 9);
		testCases.put(999_999_999, 9);
		testCases.put(1_000_000_000, 10);
		testCases.put(1_525_512_789, 10);
		testCases.put(Integer.MAX_VALUE, 10);
		
		
		solutions = new LinkedHashMap<>();
		
		solutions.put("aastha", Solutions::aastha);
		solutions.put("anjali", Solutions::anjali);
		solutions.put("ayuj", Solutions::ayuj);
		solutions.put("betsegaw", Solutions::betsegaw);
		solutions.put("dhruv", Solutions::dhruv);
		solutions.put("jessica", Solutions::jessica);
		solutions.put("victoria", Solutions::victoria);
		solutions.put("fiona", Solutions::fiona);
		solutions.put("akshee", Solutions::akshee);
		solutions.put("zach", Solutions::zach);
//		solutions.put("yasmeen", Solutions::yasmeen);
		
		
		solutions.put("optimal", Solutions::optimal);
//		solutions.put("string based", Solutions::stringBased);
//		solutions.put("log based", Solutions::logBased);
//		solutions.put("loop based", Solutions::loopBased);
		
	}
	
	public static int akshee(int num) {
		String str = Integer.toString(num);
		int len = str.length();
		return len;
	}

//	public static int yasmeen(int n)
//	{
//		int count = 0;
//	while(n >= 0) {n/=10; count++;}
//		return count;
//	}
	
	public static int aastha(int n) {
		if(n==0)return 1;
		int length = (int)(Math.log10(n)+1);
		return length;
	}

	public static int victoria(int n)
	{
		int count = 0;
		while( n != 0)
		{
			n = n/10;
			++count;
		}
		return count;
	}
	
	public static int anjali(int n)
	{	
		return Integer.toString(n).length();
	}
	
	public static int fiona(int n){ int count=0; while (n!=0) { n= n/10; count++;} return count; } 


	public static int dhruv(int n)
    {
        int count = 0; 
        while (n != 0)
        { 
            n = n / 10; 
            ++count; 
        } 
        return count;
    }
	
	public static int jessica(int n) {return String.valueOf(n).length();}

	public static int betsegaw(int n)
	{
		int num = n;
		int count = 0;
		if(num/10 == 0)
			return 1;
		while( num > 0)
		{
			num = num / 10;
			count++;
		}
		return count;
	}
	
	public static int ayuj(int n)
    {
        int temp = 0;
            while(n != 0)
            {
                n /= 10;
                temp++;
            }

            return temp;
    }

	public static int zach(int n) {
		int digits = 0;     
		while (n > .99) {          
			n /= 10;             
			digits++;
		}
		return digits;	
	}
	
	static int stringBased(int n) {
		return Integer.toString(n).length();
	}

	static int logBased(int n) {
		if(n == 0) return 1;
		return (int) (Math.log10(n)) + 1;
	}

	static int loopBased(int n) {
		if(n == 0) return 0;
		int count = 0;
		while(n > 0) {
			count++;
			n /= 10;
		}
		return count;
	}

	static int optimal(int n) {
		if(n >= 1_000_000_000) 	return 10;
		if(n >= 100_000_000) 	return 9;
		if(n >= 10_000_000)		return 8;
		if(n >= 1_000_000)		return 7;
		if(n >= 100_000)		return 6;
		if(n >= 10_000)			return 5;
		if(n >= 1_000)			return 4;
		if(n >= 100)			return 3;
		if(n >= 10)				return 2;
								return 1;
	}

	public static void checkSolutionsForValidity() {
		boolean result = true;
		for(Map.Entry<String, IntUnaryOperator> solution : solutions.entrySet())
			if(!isValid(solution))
				result = false;
		if(!result) {
			System.err.printf("Solutions are not valid. Exiting program.%n");
			System.exit(1);
		}
	}
	
	private static boolean isValid(Map.Entry<String, IntUnaryOperator> solution) {
		boolean result = true;
		IntUnaryOperator op = solution.getValue();
		for(Map.Entry<Integer, Integer> testCase : testCases.entrySet()) {
			int returned = op.applyAsInt(testCase.getKey());
			if(returned != testCase.getValue()) {
				System.err.printf("Solution \"%s\" failed for input=%d. Expected: %d, Returned: %d%n",
						solution.getKey(), testCase.getKey(), testCase.getValue(), returned);
				result = false;
			}
		}
		return result;
	}
	
	public static Set<Map.Entry<String, IntUnaryOperator>> getSolutions() {
		return solutions.entrySet();
	}
}
