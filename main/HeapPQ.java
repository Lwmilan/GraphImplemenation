package main;

import java.util.Comparator;

import net.datastructures.*;
/**
 * An Adaptable PriorityQueue based on an heap.
 */

public class HeapPQ<K,V> implements AdaptablePriorityQueue<K,V> {
	
	private ArrayList<Entry<K,V>> primaryArray;
	private Comparator<K> comparator;
	
	
	/**
	 * Sets up the ArrayList used to store the heap
	 * also utilizes the Default comparator class to assign the comparator to use
	 */
	@TimeComplexity("O(1)")
	public HeapPQ() {
		primaryArray = new ArrayList<Entry<K,V>>();
		comparator = new DefaultComparator<K>();
	}
	
	/**
	 * Sets up the ArrayList used to store the heap
	 * @param c the Comparator used during the opperation of the heap
	 */
	@TimeComplexity("O(1)")
	public HeapPQ(Comparator<K> c) {
		comparator = c;
	}
	
	
	/**
	 * 
	 * @return the data inside the HeapPQ as array form of the tree structure
	 */
	@TimeComplexity("O(n)")
	Object[] data() {
		Object[] outputArray = new Object[primaryArray.size()];
		for(int i = 0; i<outputArray.length; i++) {
			outputArray[i] = primaryArray.get(i);
		}
		return  outputArray;
	}
	
	/**
	 * The entry should be bubbled up to its appropriate position 
	 * @param int move the entry at index j higher if necessary, to restore the heap property
	 */
	@TimeComplexity("O(lg n)")
	/*
	 * The time complexity is O(lg n) due to the need to only traverse one branch of the tree after each subsequent call
	 * this is most simply shown through the recursive calls utilizing a 2j 
	 */
	public void upheap(int j){
		if(j==0) return;
		int comp = comparator.compare(primaryArray.get(j).getKey(), primaryArray.get((j-1)/2).getKey());
		if(comp<0) {
			swap(j,(j-1)/2);
			upheap((j-1)/2);
		}
	}
	
	/**
	 * The entry should be bubbled down to its appropriate position 
	 * @param int move the entry at index j lower if necessary, to restore the heap property
	 */
	@TimeComplexity("O(lg n)")
	/*
	 * The time complexity is O(lg n) due to the need to only follow one branch of the tree after each subsequent call
	 * this is most simply shown through the recursive calls utilizing a 2j 
	 */
	public void downheap(int j){
		//if there is no left node return because its the end of the tree
		if(2*j+1>size()-1) return;
		
		//if there is no right node compare only to the left node
		if(2*j+2>size()-1) {
			if(comparator.compare(primaryArray.get(j).getKey(), primaryArray.get(2*j+1).getKey())>0) {
				swap(j,2*j+1);
				downheap(2*j+1);
			}
			return;
		}
		
		//compare the two child nodes and select the lower node
		int comp = comparator.compare(primaryArray.get(2*j+1).getKey(), primaryArray.get(2*j+2).getKey());
		if(comp<=0) {
			//compare the left node to the primary node and if it is smaller swap them then downheap the new child node
			if(comparator.compare(primaryArray.get(j).getKey(), primaryArray.get(2*j+1).getKey())>0) {
				swap(j,2*j+1);
				downheap(2*j+1);
			}
		}
		else {
			//compare the right node to the primary node and if it is smaller swap them then downheap the new child node
			if(comparator.compare(primaryArray.get(j).getKey(), primaryArray.get(2*j+2).getKey())>0) {
				swap(j,2*j+2);
				downheap(2*j+2);
			}
		}
	}

	@Override
	@TimeComplexity("O(1)")
	public int size() {
		return primaryArray.size();
	}

	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		return primaryArray.isEmpty();
	}

	@Override
	@TimeComplexity("O(n)")
	@TimeComplexityAmortized("O(lg n)")
	/*
	 * The time complexity is O(n) because in the case where the ArrayList needs to be expanded
	 * the time complexity is O(n). Due to this being infrequent the Amortized Time Complexity is
	 * lower being O(lg n) due to the upheap function which is utilized to restore heap order
	 */
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		//creates a new Entry then upheaps
		Entry<K,V> entry = new HeapEntry<K,V>(key,value);
		primaryArray.addLast(entry);
		upheap(size()-1);
		
		return entry;
	}

	
	@Override
	@TimeComplexity("O(1)")
	/*
	 * The time complexity is O(1) because there is no need to loop through the datastructure in order to find the
	 * smallest value
	 */
	public Entry<K, V> min() {
		return primaryArray.get(0);
	}

	@Override
	@TimeComplexity("O(lg n)")
	/*
	 * The time complexity is O(lg n) because the function calls downheap which
	 * itself has a time complexity of O(lg n)
	 */
	public Entry<K, V> removeMin() {
		//Swaps the top entry with the last entry
		Entry<K,V> entry = primaryArray.get(0);
		swap(0,size()-1);
		
		//removes the last entry then downheaps the new top entry
		primaryArray.remove(size()-1);
		downheap(0);
		return entry;
	}

	@Override
	@TimeComplexity("O(n)")
	/*
	 * The time complexity is O(n) because in order to find the correct entry the entire
	 * ArrayList might need to be traversed. The other functions utilized in the loop will only run once
	 * when the Entry is found limiting the complexity to O(n).
	 */
	public void remove(Entry<K, V> entry) throws IllegalArgumentException {
		
		//Searches for the Entry inside of the HeapPQ
		for(int i =0 ; i<size(); i++) {
			if(primaryArray.get(i).equals(entry)) {
				//Once found it swaps it with the last node
				swap(i,size()-1);
				
				//removes the last node then downheaps the original location of the node to fix the order
				primaryArray.remove(size()-1);
				downheap(i);
				return;
			}
		}
		throw new IllegalArgumentException("Entry Not in Queue");
	}

	@Override
	@TimeComplexity("O(n)")
	/*
	 * Similar to the remove method the entire array may need to be traversed to find the correct Entry
	 * Therefore the time complexity is still O(n)
	 */
	public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
		
		//Searches for the Entry inside of the HeapPQ
		for(int i =0; i<size(); i++) {
			if(primaryArray.get(i).equals(entry)) {
				//Casts to HeapEntry then modifies the key
				HeapEntry<K,V> temp = (HeapEntry) primaryArray.get(i);
				temp.key = key;
				//Checks to see if the order needs to be modified
				upheap(i);
				downheap(i);
				return;
			}
		}
		throw new IllegalArgumentException("Entry Not in Queue");
		
	}

	@Override
	@TimeComplexity("O(n)")
	/*
	 * Exact same rational as the replaceKey function except there is no need to downheap/upheap due to the value not
	 * impacting the heap order. This however is irrelivent to the time complexity as it is still
	 * O(n)
	 */
	public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
		
		//Searches for the Entry inside of the HeapPQ
		for(Entry<K,V> i: primaryArray) {
			if(i.equals(entry)) {
				//Casts to HeapEntry then modifies the value
				HeapEntry<K,V> temp = (HeapEntry<K,V>)i;
				temp.value = value;
				return;
			}
		}
		throw new IllegalArgumentException("Entry Not in Queue");
	}
	
	/**
	 * Helper method used to swap to elements in the ArrayList
	 * @param indexOne the first index to swap
	 * @param indexTwo the second index to swap
	 */
	@TimeComplexity("O(1)")
	private void swap(int indexOne, int indexTwo) {
		Entry<K,V> temp = primaryArray.get(indexOne);
		primaryArray.set(indexOne, primaryArray.get(indexTwo));
		primaryArray.set(indexTwo, temp);
	}
	
	/**
	 * 
	 * @author jbrou
	 *
	 * class used in order to access and change the keys and values in a HeapPQ
	 *
	 * @param <K> The key used for the HeapPQ
	 * @param <V> The value used for the HeapPQ
	 */
	private class HeapEntry<K,V> implements Entry<K,V>{
		private K key;
		private V value;
		
		/**
		 * Creates a Entry with a specific key value pair
		 * @param k The key of the new Entry
		 * @param v The value of the new Entry
		 */
		@TimeComplexity("O(1)")
		HeapEntry(K k, V v){
			key = k;
			value = v;
		}
		
		@TimeComplexity("O(1)")
		public K getKey() {
			return key;
		}
		
		@TimeComplexity("O(1)")
		public V getValue() {
			return value;
		}
	}
}
