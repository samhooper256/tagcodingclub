package club_04_06_21;

import java.util.*;

public class RawTypes<T extends Number> {

	public static void main(String[] args) throws Throwable {
		
		RawTypes<Integer> obj = new RawTypes<>();
		
//		RawTypes<String> obj2 = new RawTypes<>();
		
		
		ArrayList<String> strs = new ArrayList<>();
		
		String s = strs.get(0);
		
		
		ArrayList raw = new ArrayList();
		
		Object o = raw.get(0);
		
		Iterator itr = raw.iterator();
		
	
		Holder h = new Holder();
		
		Object val = h.value;
		Number n = h.value;
		
		
	}
	
}

class Holder<T extends Number> {
	T value;
}