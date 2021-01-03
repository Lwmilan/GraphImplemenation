package main;

import java.util.Iterator;
import net.datastructures.Entry;

public class UnorderedMap<K,V> extends AbstractMap<K,V> {
	
	private ArrayList<Entry<K,V>> table; 
	 
	
	public UnorderedMap() {
		table = new ArrayList<Entry<K,V>>();
	}
		

	@Override
	public int size() {
		return table.size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@TimeComplexity("O(n)")
	@Override
	public V get(K key) {
		//searches all values for the key
		for(int i = 0; i<table.size(); i++) {
			if(table.get(i).getKey().equals(key)) return table.get(i).getValue();
		}
		return null;
	}

	@TimeComplexity("O(n)")
	@Override
	public V put(K key, V value) {
		//searches for if the key exists and replaces it or adds to the list 
		Entry<K,V> newEntry = new mapEntry<K,V>(key,value);
		for(int i = 0; i<table.size(); i++) {
			if(table.get(i).getKey().equals(key)) {
				V oldVal = table.get(i).getValue();
				table.set(i, newEntry);
				return oldVal; 
			}
		}
		table.addLast(newEntry);
		return null;
	}

	@TimeComplexity("O(n)")
	@Override
	public V remove(K key) {
		//searches for the key and removes it from the list
		for(int i = 0; i<table.size(); i++) {
			if(table.get(i).getKey().equals(key)) {
				V oldVal = table.get(i).getValue();
				table.remove(i);
				return oldVal; 
			}
		}
		return null;
	}


	@TimeComplexity("O(n)")
	@Override
	public Iterable<Entry<K, V>> entrySet() {
		return new entryIterable();
	}

	private class entryIterable implements Iterable<Entry<K,V>>{
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return table.iterator();
		}
	}
	public Iterator<Entry<K,V>> entryIterator(){
		return table.iterator();
	}

}
