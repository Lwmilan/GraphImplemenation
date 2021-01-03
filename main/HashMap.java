package main;

import java.util.Iterator;

import net.datastructures.*;

public class HashMap<K, V> extends AbstractMap<K,V> implements Map<K, V> {
	
	/* Use Array of UnorderedMap<K,V> for the Underlying storage for the map of entries.
	 * 
	 */
	private UnorderedMap<K,V>[]  table;
	int 	size;  // number of mappings(entries) 
	int 	capacity; // The size of the hash table. 
	int     DefaultCapacity = 17; //The default hash table size
	
	/* Maintain the load factor <= 0.75.
	 * If the load factor is greater than 0.75, 
	 * then double the table, rehash the entries, and put then into new places. 
	 */
	double  loadfactor= 0.75;  
	
	/**
	 * Constructor that takes a hash size
	 * @param hashtablesize size: the number of buckets to initialize
	 */
	public HashMap(int hashtablesize) {
		//uses the hash table size to initialize all the UnorderedMaps
		table = new UnorderedMap[hashtablesize];
		for(int i = 0; i<table.length; i++) {
			table[i] = new UnorderedMap<K,V>();
		}
		capacity = hashtablesize;
	}
	
	/**
	 * Constructor that takes no argument
	 * Initialize the hash table with default hash table size: 17
	 */
	public HashMap() {
		///utilizes a size of 17 to initialize all the UnorderedMaps
		table = new UnorderedMap[17];
		for(int i = 0; i<table.length; i++) {
			table[i] = new UnorderedMap<K,V>();
		}
		capacity = 17;
	}
	
	/* This method should be called by map an integer to the index range of the hash table 
	 */
	private int hashValue(K key) {
		return Math.abs(key.hashCode()) % capacity;
	}
	
	/*
	 * The purpose of this method is for testing if the table was doubled when rehashing is needed. 
	 * Return the the size of the hash table. 
	 * It should be 17 initially, after the load factor is more than 0.75, it should be doubled.
	 */
	public int tableSize() {
		return table.length;
	}
	
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@TimeComplexity("O(n)")
	@TimeComplexityExpected("O(1)")
	@Override
	public V get(K key) {
		//gets the key inside the Unordered map contained at the hashValue
		return table[hashValue(key)].get(key);
	}

	@TimeComplexity("O(n)")
	@TimeComplexityExpected("O(1)")
	@Override
	public V put(K key, V value) {
		//checks if the table is over its load and if so doubles the size
		if(((double)size)/capacity > loadfactor) {
			UnorderedMap<K,V>[] temp = table;
			table = new UnorderedMap[temp.length*2];
			capacity = temp.length*2;
			//after doubling the table size the UnorderedMaps are re initialized and the values rehashed
			for(int i = 0; i<table.length; i++) {
				table[i] = new UnorderedMap<K,V>();
			}
			for(int i = 0; i<temp.length; i++) {
				for(Entry<K,V> entry : temp[i].entrySet()) {
					table[hashValue(entry.getKey())].put(entry.getKey(), entry.getValue());
				}
			}
		}
		//the key value pair is put into its correct UnorderedMap based on its hashvalue
		V val =table[hashValue(key)].put(key, value);
		if(val == null) size++;
		return val;
	}

	@TimeComplexity("O(n)")
	@TimeComplexityExpected("O(1)")
	@Override
	public V remove(K key) {
		size--;
		//finds the correct UnorderedMap and removes the value from it
		return table[hashValue(key)].remove(key);
	}
	
	@TimeComplexity("O(n^2)")
	@TimeComplexityExpected("O(n)")
	@Override
	public Iterable<Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return new hashIterable();
	}
	
	//nested class used to create an effective iterable for the hashmap
	private class hashIterable implements Iterable<Entry<K,V>>{

		@Override
		public Iterator<Entry<K, V>> iterator() {
			// TODO Auto-generated method stub
			return new hashIterator();
		}
		
	}
	
	//nested class used to create an effective iterator for the hashmap
	private class hashIterator implements Iterator<Entry<K,V>>{
		private int i = 0;
		private Iterator<Entry<K,V>> current = table[0].entryIterator();
		@Override
		public boolean hasNext() {
			//goes through each UnorderedMap and each maps values utlizing its iterator
			if(!current.hasNext()) {
				while(i<table.length-1) {
					i++;
					current = table[i].entryIterator();
					if(current.hasNext()) {
						return true;
					}
				}
				return false;
			}
			return true;
		}

		@Override
		public Entry<K, V> next() {
			return current.next();
		}
		
	}

}
