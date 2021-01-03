package main;

import java.util.Iterator;
import net.datastructures.List;

public class ArrayList<E> implements List<E> {
	private int size;
	private E[] array;
	public ArrayList() {
		//Sets size to 0 and creates a new array with length 16 as the starting capacity of the ArrayList
		this.size = 0;
		array = (E[]) new Object[16];
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return size ==0;
	}

	@Override
	public E get(int i) throws IndexOutOfBoundsException {
		//Checks to see if the index is less than 0 or greater than the size-1
		if(i>=size || i<0) throw new IndexOutOfBoundsException();
		
		//returns the value at the index
		return array[i];
	}

	@Override
	public E set(int i, E e) throws IndexOutOfBoundsException {
		//Checks to see if the index is less than 0 or greater than the size-1
		if(i>=size|| i<0) throw new IndexOutOfBoundsException();
		
		//Saves the current value then sets the new value
		E temp = array[i];
		array[i] = e;
		
		//returns the saved value
		return temp;
	}

	@Override
	public void add(int i, E e) throws IndexOutOfBoundsException {
		//Checks to see if the index is less than 0 or greater than the size
		if(i>size|| i<0) throw new IndexOutOfBoundsException();
		//creates a temporary array to store what will be the new arraylist
		E[] tempArray;
		
		//checks to see if the arraylist has reached the capacity of the array if so the capacity is doubled
		if(size<array.length) tempArray = (E[]) new Object[array.length];
		else tempArray = (E[]) new Object[array.length*2];
		
		//Inserts all the values before the i value into the array
		for(int j = 0; j<i; j++) {
			tempArray[j] = array[j];
		}
		
		//adds the new value into the array
		tempArray[i] = e;
		size++;
		
		//adds the rest of the values into the array
		for(int j = i+1; j<size; j++) {
			tempArray[j] = array[j-1];
		}
		
		//sets the reference to the new array
		array = tempArray;
	}
	
	@Override
	public E remove(int i) throws IndexOutOfBoundsException {
		//Checks to see if the index is less than 0 or greater than the size-1
		if(i>=size || i<0) throw new IndexOutOfBoundsException();
		
		//saves off value at the desired index in order to return it later
		E output = array[i];
		
		//checks to see if the array size is 2x the size of the current arraylist if it is then the capacity is halfed
		size--;
		if(size>0 && array.length/(size) > 1) {
			
			//recreates the array with a smaller capacity in order to facilitate more efficent storage
			E[] tempArray = (E[]) new Object[size];
			for(int j=0; j<i; j++) {
				tempArray[j] = array[j];
			}
			for(int j = i; j<size; j++) {
				tempArray[j] = array[j+1];
			}
			array = tempArray;
		}
		else {
			//writes over the array from i to the end of the arraylist
			for(int j = i; j<size; j++) {
				array[j] = array[j+1];
			}
			
		}
		return output;
	}
	
	public Iterable<E> iterable(){
		return new ArrayListIterable<E>();
	}
	private class ArrayListIterable<E> implements Iterable<E>{

		@Override
		public Iterator<E> iterator() {
			// TODO Auto-generated method stub
			return new ArrayListIterator<E>();
		}
		
	}

	//private class describing the iterator for the ArrayList class being implemented
	private class ArrayListIterator<E> implements Iterator<E>{
		
		//index that the iterator is currently on
		private int index = 0;
		
		@Override
		public boolean hasNext() {
			return index<size;
		}

		//returns the value of the array at the current index then increments
		@Override
		public E next() {
			E output = (E) get(index);
			index++;
			return output;
		}
		
	}
	
	@Override
	public Iterator<E> iterator() {
		return new ArrayListIterator();
	}

	//The next four methods utilize the add() and remove() methods created above at their respective indexes
	public void addFirst(E e)  {
		add(0,e);
	}
	
	public void addLast(E e)  {
		add(size,e);
	}
	
	public E removeFirst() throws IndexOutOfBoundsException {
		return remove(0);
	}
	
	public E removeLast() throws IndexOutOfBoundsException {
		return remove(size-1);
	}
	
	// Return the capacity of array, not the number of elements.
	// Notes: The initial capacity is 16. When the array is full, the array should be doubled. 
	public int capacity() {
		//returns the current length of the array which is equivalent to the maximum capacity 
		return array.length;
	}
	
}
