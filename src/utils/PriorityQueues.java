package utils;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import utils.PriorityQueues.DoublePQ.DoublePQEntry;

/**
 * @author Sam Hooper
 *
 */
public class PriorityQueues {
	public static void main(String[] args) {
		DoublePQ<String> q = new DoublePQ<>();
		System.out.println(Arrays.toString(q.queue));
		q.insert("B1", 2.0);
		System.out.println(Arrays.toString(q.queue));
		q.insert("C", 3.0);
		System.out.println(Arrays.toString(q.queue));
		q.insert("A", 1.0);
		System.out.println(Arrays.toString(q.queue));
		q.insert("A+", 0.0);
		System.out.println(Arrays.toString(q.queue));
		q.insert("B2", 2.0);
		System.out.println(Arrays.toString(q.queue));
		q.insert("D", 4.0);
		System.out.println(Arrays.toString(q.queue));
		q.insert("S", -1);
		System.out.println(Arrays.toString(q.queue));
		q.insert("B-", 2.1);
		System.out.println(Arrays.toString(q.queue));
		q.insert("D+", 3.9);
		System.out.println(Arrays.toString(q.queue));
		q.insert("A-", 1.1);
		System.out.println(Arrays.toString(q.queue));
		q.insert("A--", 1.2);
		System.out.println(Arrays.toString(q.queue));
		q.insert("A---", 1.3);
		System.out.println(Arrays.toString(q.queue));
		q.changePriority("D", 1.5);
		System.out.println(Arrays.toString(q.queue));
		q.changePriority("A---", 7.7);
		System.out.println(Arrays.toString(q.queue));
		q.changePriority("D+", -2);
		System.out.println(Arrays.toString(q.queue));
		System.out.println(q.indexMap);
		System.out.println(q);
		while(!q.isEmpty()) {
			DoublePQEntry<String> item = q.extractEntry();
			System.out.println("extracted: " + item);
//			System.out.println(Arrays.toString(q.queue));
			System.out.println(q.indexMap);
		}
	}
	
	public static interface PQEntry<E> {
		E getItem();
	}
	public abstract static class AbstractArrayPQ<E> extends AbstractCollection<E>{
		protected static final int DEFAULT_CAPACITY = 10;
		
		protected int size;
		
		protected Object[] queue;
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public boolean isEmpty() {
			return size == 0;
		}
		
		/**
		 * Doubles the capacity of {@link #queue}.
		 */
		protected void doubleCapacity() {
			Object[] newQueue = new Object[queue.length << 1];
			System.arraycopy(queue, 0, newQueue, 0, queue.length);
			queue = newQueue;
		}
	}
	public static class DoublePQ<E> extends AbstractArrayPQ<E> {
		public static final class DoublePQEntry<T> implements PQEntry<T>{
			private final T item;
			private double priority;
			private DoublePQEntry(final T item, final double priority){
				this.item = item;
				this.priority = priority;
			}
			
			@Override public String toString() { return String.format("(%s, %.2f)", item, priority); }
			
			/**
			 * @return the item in this {@code DoublePQEntry}.
			 */
			@Override
			public T getItem() {
				return item;
			}
			
			/**
			 * @return the priority of the item in this {@code DoublePQEntry}.
			 */
			public double getPriority(){
				return priority;
			}
			
			private void setPriortity(double newPriority) {
				this.priority = newPriority;
			}

			@Override
			public int hashCode() {
				return Objects.hashCode(item);
			}
			
			/**
			 * This method tests for equality of the item using <b>reference equality</b>.
			 */
			@Override
			public boolean equals(Object obj) {
				if(this == obj) {
					return true;
				}
				if(obj == null) {
					return false;
				}
				if(getClass() != obj.getClass()) {
					return false;
				}
				@SuppressWarnings("rawtypes")
				DoublePQEntry other = (DoublePQEntry) obj;
				return 	item == other.item;
			}
		}
		
		private final Map<DoublePQEntry<E>, Integer> indexMap;
		
		public DoublePQ() {
			queue = new Object[DEFAULT_CAPACITY + 1];
			indexMap = new HashMap<>();
			size = 0;
		}
		
		public void insert(final E item, final double priority) {
			insert(new DoublePQEntry<>(item, priority));
		}
		
		private void insert(final DoublePQEntry<E> entry) {
			if(size == queue.length - 1) doubleCapacity();
			size++;
			queue[size] = entry;
			indexMap.put(entry, size);
			siftUp(size);
		}
		
		private void siftUp(int index) {
			@SuppressWarnings("unchecked")
			final DoublePQEntry<E> entry = (DoublePQEntry<E>) queue[index];
			while(index > 1) {
				@SuppressWarnings("unchecked")
				final DoublePQEntry<E> parent = (DoublePQEntry<E>) queue[index >> 1];
				if(parent.priority > entry.priority) {
					queue[index >> 1] = entry;
					indexMap.put(entry, index >> 1);
					queue[index] = parent;
					indexMap.put(parent, index);
					index >>= 1;
				}
				else {
					break;
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		public DoublePQEntry<E> extractEntry() {
			final DoublePQEntry<E> min = (DoublePQEntry<E>) queue[1];
			queue[1] = queue[size];
			indexMap.remove(min);
			queue[size] = null;
			
			size--;
			if(size > 0) {
				indexMap.put((DoublePQEntry<E>) queue[1], 1);
				siftDown(1);
			}
			return min;
		}
		/**
		 * Assumes {@link #indexMap} and {@link #size} is correct, and that {@code index} is in bounds.
		 * @param index
		 */
		@SuppressWarnings("unchecked")
		private void siftDown(int index) {
			final DoublePQEntry<E> focusEntry = Objects.requireNonNull((DoublePQEntry<E>) queue[index]);
			while(index < size) {
				final int leftIndex = index << 1;
				final int rightIndex = leftIndex + 1;
				if(rightIndex <= size) { //check rightIndex first, because if right is in bounds then left certainly is.
					final DoublePQEntry<E> leftEntry = (DoublePQEntry<E>) queue[leftIndex];
					final DoublePQEntry<E> rightEntry = (DoublePQEntry<E>) queue[rightIndex];
					if(rightEntry.priority < leftEntry.priority) {
						if(rightEntry.priority < focusEntry.priority) {
							queue[index] = rightEntry;
							indexMap.put(rightEntry, index);
							queue[rightIndex] = focusEntry;
							indexMap.put(focusEntry, rightIndex);
							index = rightIndex;
							continue;
						} //loop will exit
					}
					else if(leftEntry.priority < focusEntry.priority) {
						queue[index] = leftEntry;
						indexMap.put(leftEntry, index);
						queue[leftIndex] = focusEntry;
						indexMap.put(focusEntry, leftIndex);
						index = leftIndex;
						continue;
					} // at this point, neither child has a lower priority - heap property is satisfied, so we break.
				}
				else if(leftIndex <= size) {
					final DoublePQEntry<E> leftEntry = (DoublePQEntry<E>) queue[leftIndex];
					if(leftEntry.priority < focusEntry.priority) {
						queue[index] = leftEntry;
						indexMap.put(leftEntry, index);
						queue[leftIndex] = focusEntry;
						indexMap.put(focusEntry, leftIndex);
						index = leftIndex;
						continue;
					} // neither child has a lower priority - heap property is satisfied, so we break.
				}
				break;
			}
		}
		
		public E extract() {
			return extractEntry().getItem();
		}
		
		public void changePriority(final E item, final double newPriority) {
			DoublePQEntry<E> entry = new DoublePQEntry<>(item, newPriority);
			if(!indexMap.containsKey(entry)) {
				throw new IllegalArgumentException(item + " is not in this DoublePQ, so its priority cannot be "
						+ "changed.");
			}
			final int index = indexMap.get(entry);
			changePriority(entry, index);
		}
		
		/**
		 * Changes the priority of the item of {@code entry} to the priority of {@code entry}. {@code index} <b>must</b>
		 * be the index of the {@code DoublePQEntry} with same item (by reference equality) as {@code entry}, as
		 * given by {@link #indexMap}.
		 * @param entry
		 * @param index
		 */
		
		private void changePriority(final DoublePQEntry<E> entry, final int index) {
			final double newPriority = entry.priority;
			@SuppressWarnings("unchecked")
			DoublePQEntry<E> realEntry = (DoublePQEntry<E>) queue[index];
			if(newPriority > realEntry.priority) {
				realEntry.setPriortity(newPriority);
				siftDown(index);
			}
			else {
				realEntry.setPriortity(newPriority);
				siftUp(index);
			}
		}
		
		/**
		 * If {@code item} is not present by <b>reference equality</b>, adds {@code item} to this {@code DoublePQ} with
		 * the given priority. Otherwise, it changes the priority of {@code item} to {@code priority}
		 * @param item
		 * @param priority
		 */
		public void setWithPriority(final E item, final double priority) {
			DoublePQEntry<E> entry = new DoublePQEntry<>(item, priority);
			if(indexMap.containsKey(entry)) {
				insert(entry);
			}
			else {
				changePriority(entry, indexMap.get(entry));
			}
			
		}

		/**
		 * Returns an {@link Iterator} over this {@code DoublePQ}'s elements. If elements are added or removed from this
		 * {@code Collection} after the {@code Iterator} has been returned, the {@code Iterator}'s future behavior is
		 * undefined.
		 * @return an {@link Iterator} of this {@code DoublePQ}'s elements.
		 */
		@Override
		public Iterator<E> iterator() {
			return new Itr();
		}
		
		private class Itr implements Iterator<E>{
			int cursor; //the index (in queue) of the next value to be returned by next()
			Itr() { cursor = 1; }
			@Override public boolean hasNext() { return cursor <= size; }
			@SuppressWarnings("unchecked")
			@Override public E next() { return ((DoublePQEntry<E>) queue[cursor++]).getItem(); }
		}
	}
	
	public static class IntDoublePQ {
		protected static final int DEFAULT_CAPACITY = 10;
		
		protected int size;
		
		protected IntDoublePQEntry[] queue;
		
		public int size() {
			return size;
		}
		
		public boolean isEmpty() {
			return size == 0;
		}
		
		/**
		 * Doubles the capacity of {@link #queue}.
		 */
		protected void doubleCapacity() {
			IntDoublePQEntry[] newQueue = new IntDoublePQEntry[queue.length << 1];
			System.arraycopy(queue, 0, newQueue, 0, queue.length);
			queue = newQueue;
		};
		
		public static final class IntDoublePQEntry{
			private final int item;
			private double priority;
			private IntDoublePQEntry(final int item, final double priority){
				this.item = item;
				this.priority = priority;
			}
			
			@Override public String toString() { return String.format("(%s, %.4f)", item, priority); }
			
			/**
			 * @return the item in this {@code IntDoublePQEntry}.
			 */
			public int getItem() {
				return item;
			}
			
			/**
			 * @return the priority of the item in this {@code IntDoublePQEntry}.
			 */
			public double getPriority() {
				return priority;
			}
			
			private void setPriortity(double newPriority) {
				this.priority = newPriority;
			}

			@Override
			public int hashCode() {
				return Objects.hashCode(item);
			}
			
			/**
			 * This method tests for equality of the item using <b>reference equality</b>.
			 */
			@Override
			public boolean equals(Object obj) {
				if(this == obj) {
					return true;
				}
				if(obj == null) {
					return false;
				}
				if(getClass() != obj.getClass()) {
					return false;
				}
				@SuppressWarnings("rawtypes")
				IntDoublePQEntry other = (IntDoublePQEntry) obj;
				return 	item == other.item;
			}
		}
		
		private final Map<IntDoublePQEntry, Integer> indexMap;
		
		public IntDoublePQ() {
			queue = new IntDoublePQEntry[DEFAULT_CAPACITY + 1];
			indexMap = new HashMap<>();
			size = 0;
		}
		
		public void insert(final int item, final double priority) {
			insert(new IntDoublePQEntry(item, priority));
		}
		
		private void insert(final IntDoublePQEntry entry) {
			if(size == queue.length - 1) doubleCapacity();
			size++;
			queue[size] = entry;
			indexMap.put(entry, size);
			siftUp(size);
		}
		
		private void siftUp(int index) {
			final IntDoublePQEntry entry = queue[index];
			while(index > 1) {
				final IntDoublePQEntry parent = queue[index >> 1];
				if(parent.priority > entry.priority) {
					queue[index >> 1] = entry;
					indexMap.put(entry, index >> 1);
					queue[index] = parent;
					indexMap.put(parent, index);
					index >>= 1;
				}
				else {
					break;
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		public IntDoublePQEntry extractEntry() {
			final IntDoublePQEntry min = queue[1];
			queue[1] = queue[size];
			indexMap.remove(min);
			queue[size] = null;
			
			size--;
			if(size > 0) {
				indexMap.put(queue[1], 1);
				siftDown(1);
			}
			return min;
		}
		/**
		 * Assumes {@link #indexMap} and {@link #size} is correct, and that {@code index} is in bounds.
		 * @param index
		 */
		@SuppressWarnings("unchecked")
		private void siftDown(int index) {
			final IntDoublePQEntry focusEntry = Objects.requireNonNull(queue[index]);
			while(index < size) {
				final int leftIndex = index << 1;
				final int rightIndex = leftIndex + 1;
				if(rightIndex <= size) { //check rightIndex first, because if right is in bounds then left certainly is.
					final IntDoublePQEntry leftEntry = queue[leftIndex];
					final IntDoublePQEntry rightEntry = queue[rightIndex];
					if(rightEntry.priority < leftEntry.priority) {
						if(rightEntry.priority < focusEntry.priority) {
							queue[index] = rightEntry;
							indexMap.put(rightEntry, index);
							queue[rightIndex] = focusEntry;
							indexMap.put(focusEntry, rightIndex);
							index = rightIndex;
							continue;
						} //loop will exit
					}
					else if(leftEntry.priority < focusEntry.priority) {
						queue[index] = leftEntry;
						indexMap.put(leftEntry, index);
						queue[leftIndex] = focusEntry;
						indexMap.put(focusEntry, leftIndex);
						index = leftIndex;
						continue;
					} // at this point, neither child has a lower priority - heap property is satisfied, so we break.
				}
				else if(leftIndex <= size) {
					final IntDoublePQEntry leftEntry = queue[leftIndex];
					if(leftEntry.priority < focusEntry.priority) {
						queue[index] = leftEntry;
						indexMap.put(leftEntry, index);
						queue[leftIndex] = focusEntry;
						indexMap.put(focusEntry, leftIndex);
						index = leftIndex;
						continue;
					} // neither child has a lower priority - heap property is satisfied, so we break.
				}
				break;
			}
		}
		
		public int extract() {
			return extractEntry().getItem();
		}
		
		public void changePriority(final int item, final double newPriority) {
			IntDoublePQEntry entry = new IntDoublePQEntry(item, newPriority);
			if(!indexMap.containsKey(entry)) {
				throw new IllegalArgumentException(item + " is not in this DoublePQ, so its priority cannot be "
						+ "changed.");
			}
			final int index = indexMap.get(entry);
			changePriority(entry, index);
		}
		
		/**
		 * Changes the priority of the item of {@code entry} to the priority of {@code entry}. {@code index} <b>must</b>
		 * be the index of the {@code DoublePQEntry} with same item (by reference equality) as {@code entry}, as
		 * given by {@link #indexMap}.
		 * @param entry
		 * @param index
		 */
		
		private void changePriority(final IntDoublePQEntry entry, final int index) {
			final double newPriority = entry.priority;
			IntDoublePQEntry realEntry = queue[index];
			if(newPriority > realEntry.priority) {
				realEntry.setPriortity(newPriority);
				siftDown(index);
			}
			else {
				realEntry.setPriortity(newPriority);
				siftUp(index);
			}
		}
		
		/**
		 * If {@code item} is not present by <b>reference equality</b>, adds {@code item} to this {@code DoublePQ} with
		 * the given priority. Otherwise, it changes the priority of {@code item} to {@code priority}
		 * @param item
		 * @param priority
		 */
		public void setWithPriority(final int item, final double priority) {
			IntDoublePQEntry entry = new IntDoublePQEntry(item, priority);
			if(indexMap.containsKey(entry)) {
				insert(entry);
			}
			else {
				changePriority(entry, indexMap.get(entry));
			}
		}
		
		@Override
		public String toString() {
			return String.format("IntDoublePQ[queue=%s]", Arrays.deepToString(queue));
		}
	}
}
