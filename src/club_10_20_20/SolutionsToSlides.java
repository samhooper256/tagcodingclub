package club_10_20_20;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sam Hooper
 *
 */
public class SolutionsToSlides {
	public static void main(String[] args) {
		LinkedList<ArrayList<String>> strs = new LinkedList<>();
		List<CharSequence> list = flatten(strs);
		System.out.println(find(new Double[] {2.0}, 2.0));
		
		addToList(new ArrayList<Number>(), 1);
		addToList(new LinkedList<>(), "");
		addToList(new ArrayList<List<?>>(), List.of("hey", "there"));
		
		List<Object> a = flatten(List.of(List.of(1,"A",3)));
		List<Number> b = flatten(List.of(List.of(1,2,3), List.of(5.5,6.4), List.of(-88.0)));
		List<List<?>> c = flatten(List.of(List.of(List.of(1)), List.of(List.of(2,3), List.of(4))));
		List<Object> d = flatten(new ArrayList<>(Arrays.asList(new LinkedList<>(), new ArrayList<>())));
		System.out.println(a + "\n" + b + "\n" + c + "\n" + d);
		/*
		HashMap<CharSequence, Number> m1 = new HashMap<>();
		addToMap(m1, "A", 1);
		addToMap(m1, new StringBuilder("B"), 2.3);
		TreeMap<Object, Object> m = new TreeMap<>();
		addToMap(m, "A", m1.get("A")); 
		System.out.println(m1);
		System.out.println(m);
		Map<List<? super String>, Number> m4 = null;
		List<? super CharSequence> csqs = new ArrayList<>();
		addToMap(m4, csqs, Integer.valueOf(4));
		
		
		SolutionsToSlides.<String, String>addToMap(m, "", ""); //would not work if Q6 did not use wildcards!
		*/
		Map<String, Integer> m1 = new HashMap<>();
		m1.put("A", 1);
		Map<StringBuilder, Double> m2 = new HashMap<>();
		m2.put(new StringBuilder("B"), 2.0);
		Map<CharSequence, Number> m3 = mergeMaps(m1, m2);
		System.out.println(m3);

	}
	interface Goofy{}
	interface Lammy extends Goofy{
		void doStuff();
	}
	private static <T extends Collection<? extends Number>, S extends U, U> void test(T t, S s, U u) {
		Map<Goofy, Object> m = null;
		Map<Collection<? extends Object>, Object> m2 = null;
		Map<Object, Object> objs = null;
		addToMap(m, (Lammy) System::gc, null);
		addToMap(objs, true, 1);
	}
	
	// Q1: Write the method "find" which takes an array and an object and determines whether the array contains an object that is equal (by the equals(...) method)
	//to the given one. If it does, it should return that object, otherwise it should return null.
	
	public static <T> T find(T[] array, T item) {
		for(T obj : array)
			if(Objects.equals(obj, item)) //accounts for the possibility that item and/or obj may be null.
				//if we did "item.equals(...)" or "obj.equals(...)" we may get NullPointerException.
				return obj;
		return null;
		
		//a one-liner using streams:
		//return Arrays.stream(array).filter(x -> Objects.equals(item, x)).findFirst().orElse(null);
		//or even shorter, if we assume item is non-null:
		//return Arrays.stream(array).filter(item::equals).findFirst().orElse(null);
	}
	
	// Q2: Write the method "addToList" which takes a list and an item and adds that item to the list. It should be as generic as possible while remaining type-safe.
	
	public static <T> void addToList(List<? super T> list, T item) {
		list.add(item);
	}
	
	
	// Q3: Write the method "mean" which takes finds the mean value of a collection of numbers.
	
	public static double mean(Collection<? extends Number> nums) {
		double total = 0;
		for(Number num : nums)
			total += num.doubleValue(); //don't forget the convenient doubleValue() method on all Number objects!
		return total / nums.size();
		
		//and the customary streams one-liner:
		//return nums.stream().mapToDouble(Number::doubleValue).sum() / nums.size();
	}
	
	// Q4: Write the method "merge" which takes two lists and returns a single containing all the elements of the first followed by all the elements of the second.
	
	public static <T> List<T> merge(List<? extends T> list1, List<? extends T> list2) {
		List<T> result = new ArrayList<>();
		result.addAll(list1); //again, don't forget about the convenient addAll() method
		//If you did it manually, it would be: "for(T item: list1) result.add(item);"
		result.addAll(list2);
		return result;
	}
	
	/*
	public static <T1 extends R, T2 extends R, R> List<R> merge(List<T1> list1, List<T2> list2) {
		
//		list1.addAll(list2);
		return null;
	}
	*/
		
	// Q5: Write the method "flatten", which takes a list of lists and flattens it into single list containing all of the items (in order) from all of the nested
	// lists. For example, if the argument is a list containing three lists: ("A","B","C"), ("D","E","F"), and ("G","H","I"), the returned value should be a 9-element
	//list of strings containing ("A","B","C","D","E","F","G","H","I"). As always, it should be as generic as possible while remaining type-safe.
	
	public static <T> List<T> flatten(List<? extends List<? extends T>> lists) {
		List<T> result = new ArrayList<>();
		for(List<? extends T> list : lists)
			result.addAll(list);
		return result;
		
		//and, of course, the stream:
		//return lists.stream().flatMap(List::stream).collect(Collectors.toList());
	}
	
	// Q6: Write the method "addToMap" which takes a map, a key, and value. The method should add an entry to the map whose key is the given key and whose value is the 
	// given value.
	
	public static <K, V> void addToMap(Map<? super K, ? super V> map, K key, V value) {
		
		map.put(key, value);
		
	}
	
	// Q7: Write the method "mergeMaps," which takes two maps and returns a single map containing all of the entries from both maps. You can assume the that the two
	// maps have no keys in common (that is, all the keys across both maps are unique - you don't have to handle the case where both maps have an entry with the same key).
	
	public static <K, V> Map<K, V> mergeMaps(Map<? extends K, ? extends V> map1, Map<? extends K, ? extends V> map2) {
		Map<K, V> result = new HashMap<>();
		result.putAll(map1); //another handy method to be aware of
		result.putAll(map2);
		return result;
	}
}
