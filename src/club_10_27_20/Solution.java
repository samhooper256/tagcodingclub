package club_10_27_20;

import java.util.*;

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
	
	public static List<String> solve(String[][] arr) {
//		System.out.println("\nCASE");
		List<String> resultList = new ArrayList<String>() ;
		//consolidate - {{a,b}, {b, c}}
		// {a,b,b,c}
		
		List<String[]> arrList = new ArrayList<>();
		for(String[] pair : arr)
			arrList.add(pair);
		
		Set<String> strings = new HashSet<>();
		for(String[] pair : arrList)
			for(String item : pair)
				strings.add(item);
		
		
		//keep removing last until... ?
		while(arrList.size() != 0) {
			removeLast(resultList, arrList, strings);
//			System.out.println(resultList);
		}
		
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
				if(other[0].equals(second)) {
					continue outer;
				}
			}
			//second is the last one in the sequence:
			resultList.add(0, second);
			strings.remove(second);
			//Does the first string in this pair appear in any other pair?
			//If Yes, leave it in. If no, add it to the result list.
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
			
			arrList.remove(i);
			
			for(int k = arrList.size() - 1; k >= 0; k--) {
				String[] duo = arrList.get(k);
				if(duo[1].equals(second))
					arrList.remove(k);
			}
			
			
			//keep looking or leave?
			break outer;
		}
	}
}
