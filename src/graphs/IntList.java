package graphs;

import java.util.*;
import java.util.function.IntConsumer;

/**
 * @author Sam Hooper
 *
 */
public class IntList implements Iterable<Integer>{
	/** stores 0 in unused indices. The used indices are those in the range 0 (inclusive) to size (exclusive). */
	int[] data; 
	/** The number of elements in this {@code IntList}. It is always true that {@code size <= data.length}.*/
	int size;
	
	/** Returns a freshly created {@code IntList} containing exactly {@code numElements} elements, all of which are 0.
	 * The capacity of the returned list is {@code numElements}. Runs in O(1).*/
	public static IntList zeroList(int numElements) {
		IntList l = new IntList(numElements);
		l.size = numElements;
		return l;
	}
	
	/** Returns a freshly created {@code IntList} containing exactly {@code numElements} elements, all of which are 0.
	 * The capacity of the returned list is {@code capacity}. Runs in O(1).*/
	public static IntList zeroList(int numElements, int capacity) {
		if(capacity < numElements) throw new IllegalArgumentException("capacity < numElements");
		IntList l = new IntList(capacity);
		l.size = numElements;
		return l;
	}
	
	public IntList() { this(10); }
	
	/** Be sure that you don't accidentally try to use this constructor to make a single-element {@code IntList} */
	public IntList(int capacity) {
		data = new int[capacity];
	}
	
	/** Creates an {@code IntList} containing the values in the array. The capacity is set to the length of the array. */
	public IntList(int... copyFrom) {
		this(copyFrom, copyFrom.length);
	}
	
	/** Creates an {@code IntList} containing the first {@code capacity} values of {@code copyFrom} with
	 * capacity {@code capacity}. If {@code capacity > copyFrom.length}, then all of the elements of {@code copyFrom}
	 * are added and the size of the list is the number of elements in {@code copyFrom}.
	 */
	public IntList(int[] copyFrom, int capacity) {
		data = Arrays.copyOf(copyFrom, capacity);
		size = Math.min(capacity, copyFrom.length);
	}
	
	public IntList(IntList copyFrom) {
		data = Arrays.copyOf(copyFrom.data, copyFrom.data.length);
		size = copyFrom.size;
	}
	
	public boolean isEmpty() { return size == 0; }
	
	/** Adds the given item to this list without checking if the internal array has enough capacity. May throw an
	 * {@link IndexOutOfBoundsException}.
	 */
	public void addSafe(int item) {
		data[size++] = item;
	}
	/** Adds the given item to this list, automatically expanding this list's capacity if needed. */
	public void add(int item) {
		if(size == data.length)
			doubleCapacity();
		addSafe(item);
	}
	
	/** Adds the given item to this list at the given index (such that, after this call, {@code get(index) == item}),
	 * automatically expanding this list's capacity if needed. */
	public void add(int index, int item) {
		if(index > size)
			throw new IllegalArgumentException("index > size");
		if(size == data.length)
			doubleCapacity();
		pushForwardSafeKeepingValue(index);
		data[index] = item;
		size++;
	}
	
	/** Shifts all elements starting from index (inclusive) to size (exclusive) one index to the right. Does not check for capacity.
	 * After this method is called, {@code data[index]} will still hold the same value as before. THIS METHOD DOES NOT
	 * INCREASE SIZE.*/
	private void pushForwardSafeKeepingValue(int index) {
		for(int i = size; i > index;)
			data[index] = data[--index];
	}
	
	public void set(int index, int item) {
		if(index >= size) throw new IllegalArgumentException("index >= size");
		setSafe(index, item);
	}
	
	/** Sets the element at given index to the given item, without performing bounds checks. This method allows the
	 * caller to set the value of any index in {@code data}, even if it that index is greater than or equal to {@code size}.
	 * Does not change {@code size}.*/
	public void setSafe(int index, int item) {
		data[index] = item;
	}
	
	/** Removes the first (lowest index) occurrence of {@code item}. Returns false if {@code item} was not present,
	 * true otherwise. */
	public boolean remove(int item) {
		for(int i = 0; i < size; i++) {
			if(data[i] == item) {
				removeByIndexSafe(i);
				return true;
			}
		}
		return false;
	}
	
	/** throws {@link ArrayIndexOutOfBoundsException} if {@code index} is negative or greater than or equal to the
	 * size of this {@code IntList}. */
	public int removeByIndex(int index) {
		if(index >= size) throw new ArrayIndexOutOfBoundsException("index >= size");
		return removeByIndexSafe(index);
	}
	
	public int removeByIndexSafe(int index) {
		int value = data[index]; 
		for(int j = index; j < size - 1; j++)
			data[j] = data[j + 1];
		size--;
		data[size] = 0;
		return value;
	}
	
	/** Removes and returns the last (highest index/rightmost) element in this list. Throws {@link NoSuchElementException} if
	 * the list is empty.*/
	public int pop() {
		if(size == 0) throw new NoSuchElementException();
		return popSafe();
	}
	
	/** Removes and returns the last (highest index/rightmost) element in this list. Assumes the list is not empty. */
	public int popSafe() {
		int value = data[--size];
		data[size] = 0;
		return value;
	}
	
	/** Returns the last (highest index/rightmost) element in this list. Throws {@link NoSuchElementException} if the list is empty */
	public int peek() {
		if(size == 0) throw new NoSuchElementException();
		return peekSafe();
	}
	
	/** Returns the last (highest index/rightmost) element in this list. Assumes the list is not empty.*/
	public int peekSafe() {
		return data[size - 1];
	}
	
	public boolean contains(int item) {
		for(int i = 0; i < size; i++)
			if(data[i] == item)
				return true;
		return false;
	}
	
	/** Returns the item at the given index. Throws {@link ArrayIndexOutOfBoundsException} if {@code index} is negative
	 * or greater than or equal to {@code size}.*/
	public int get(int index) {
		return data[index];
	}
	
	public int size() { return size; }
	
	public void clear() {
		Arrays.fill(data, 0, size, 0);
		size = 0;
	}
	
	public void doubleCapacity() {
		data = Arrays.copyOf(data, data.length << 1);
	}
	
	public int getCapcity() { return data.length; }
	
	/** Sets this {@code IntList}'s capacity to {@code minCapacity} if {@code minCapacity} is greater than its current capacity.
	 * Has no effect otherwise.*/
	public void ensureCapacity(int minCapacity) {
		if(minCapacity > data.length)
			data = Arrays.copyOf(data, minCapacity);
	}
	
	public void forEach(IntConsumer consumer) {
		for(int i = 0; i < size; i++)
			consumer.accept(data[i]);
	}

	/**
	 * Note that it will likely be more efficient to iterate over the elements of this {@code IntList} without using
	 * the iterator, as it will not require boxing to {@link Integer}. The returned iterator makes no guarantees about
	 * its behavior if elements are added or removed from this list after it has been created, except by its on
	 * remove method.
	 */
	@Override
	public java.util.Iterator<Integer> iterator() {
		return new Itr();
	}
	
	private class Itr implements Iterator<Integer> {
		int index; //index of the NEXT element to be returned by next(). Zero by default.
		boolean canRemove;
		@Override
		public boolean hasNext() {
			return index != size;
		}

		@Override
		public Integer next() {
			if(index >= size) throw new NoSuchElementException();
			canRemove = true;
			return data[index++];
		}

		@Override
		public void remove() {
			if(!canRemove) throw new IllegalStateException();
			canRemove = false;
			removeByIndex(--index);
		}
		
	}
	
	@Override
	public String toString() {
		StringJoiner j = new StringJoiner(", ", "[", "]");
		for(int i = 0; i < size; i++)
			j.add(Integer.toString(data[i]));
		return j.toString();
	}
}
