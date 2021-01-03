package main;

import java.util.Iterator;
import net.datastructures.Position;
import net.datastructures.PositionalList;


public class DoublyLinkedList<E> implements PositionalList<E> {

	private int size;
	private NodePosition<E> first;
	private NodePosition<E> last;
	public DoublyLinkedList() {
		this.size = 0;
		
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public Position<E> first() {
		//gets the position of the first element in the list
		return first;
	}

	@Override
	public Position<E> last() {
		//gets the position of the last element in the list
		return last;
	}

	@Override
	public Position<E> before(Position<E> p) throws IllegalArgumentException {
		//checks to see if the position is the correct type of position
		if(!(p instanceof NodePosition)) throw new IllegalArgumentException("Not a valid Position");
		
		//casts to NodePosition in order to find the previous/next positions
		NodePosition<E> pos = (NodePosition<E>)p;
		
		//checks to see if the position is in the list
		if(pos.list != this) throw new IllegalArgumentException("Position not in list");
		
		//returns the position before pos
		return pos.previous;
	}

	@Override
	public Position<E> after(Position<E> p) throws IllegalArgumentException {
		//checks to see if the position is the correct type of position
		if(!(p instanceof NodePosition)) throw new IllegalArgumentException("Not a valid Position");
				
		//casts to NodePosition in order to find the previous/next positions
		NodePosition<E> pos = (NodePosition<E>)p;
				
		//checks to see if the position is in the list
		if(pos.list != this) throw new IllegalArgumentException("Position not in list");

		//returns the position after pos
		return pos.next;
	}

	@Override
	public Position<E> addFirst(E e) {
		//creates new position and sets the value to the input
		NodePosition<E> p = new NodePosition<E>(e,this);
		
		//sets up the first element if necessary if not it adds it to the beginning of the list
		if(size ==0) {
			first = p;
			last = p;
		}
		else {
			p.next = first;
			first.previous = p;
			first = p;
		}
		size++;
		
		//returns the new position
		return first;
	}

	@Override
	public Position<E> addLast(E e) {
		//creates new position and sets the value to the input
		NodePosition<E> p = new NodePosition<E>(e,this);
		
		//sets up the first element if necessary if not it adds it to the end of the list
		if(size == 0) {
			first = p;
			last = p;
		}
		else {
			p.previous = last;
			last.next = p;
			last = p;
		}
		size++;
		return last;
	}

	@Override
	public Position<E> addBefore(Position<E> p, E e)
			throws IllegalArgumentException {
		//checks to see if the position is the correct type of position
		if(!(p instanceof NodePosition)) throw new IllegalArgumentException("Not a valid Position");
				
		//casts to NodePosition in order to find the previous/next positions
		NodePosition<E> pos = (NodePosition<E>)p;
				
		//checks to see if the position is in the list
		if(pos.list != this) throw new IllegalArgumentException("Position not in list");
		
		size++;
		
		//creates new position and sets the value to the input
		NodePosition<E> newPosition = new NodePosition<E>(e,this);
		
		//sets up the previous and next nodes of the positions before and after the new node
		newPosition.previous = pos.previous;
		pos.previous.next = newPosition;
		pos.previous = newPosition;
		newPosition.next = pos;
		
		//returns the created position
		return newPosition;
	}

	@Override
	public Position<E> addAfter(Position<E> p, E e)
			throws IllegalArgumentException {
		//checks to see if the position is the correct type of position
		if(!(p instanceof NodePosition)) throw new IllegalArgumentException("Not a valid Position");
						
		//casts to NodePosition in order to find the previous/next positions
		NodePosition<E> pos = (NodePosition<E>)p;
						
		//checks to see if the position is in the list
		if(pos.list != this) throw new IllegalArgumentException("Position not in list");
				
		size++;
		
		//creates new position and sets the value to the input
		NodePosition<E> newPosition = new NodePosition<E>(e,this);
		
		//sets up the previous and next nodes of the positions before and after the new node
		newPosition.next = pos.next;
		pos.next.previous = newPosition;
		pos.next = newPosition;
		newPosition.previous = pos;
		
		//returns the created position
		return newPosition;
	}

	@Override
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		//checks to see if the position is the correct type of position
		if(!(p instanceof NodePosition)) throw new IllegalArgumentException("Not a valid Position");
						
		//casts to NodePosition in order to find the previous/next positions
		NodePosition<E> pos = (NodePosition<E>)p;
						
		//checks to see if the position is in the list
		if(pos.list != this) throw new IllegalArgumentException("Position not in list");
		
		//gets the currently stored value of the position
		E output = pos.getElement();
		
		//changes the value of the position
		pos.value = e;
		
		//returns the saved value
		return output;
	}

	@Override
	public E remove(Position<E> p) throws IllegalArgumentException {
		//checks to see if the position is the correct type of position
		if(!(p instanceof NodePosition)) throw new IllegalArgumentException("Not a valid Position");
						
		//casts to NodePosition in order to find the previous/next positions
		NodePosition<E> pos = (NodePosition<E>)p;
					
		//checks to see if the position is in the list
		if(pos.list != this) throw new IllegalArgumentException("Position not in list");
				
		size--;
		
		//checks if the node is the last node and if not it sets the next nodes references
		if(pos.next != null) pos.next.previous = pos.previous;
		else last = pos.previous;
		
		//checks if the node is the first node and if it is not it sets the previous nodes references
		if(pos.previous != null) pos.previous.next = pos.next;
		else first = pos.next;
		
		//returns the removed element
		return pos.getElement();
	}

	@Override
	public Iterator<E> iterator() {
		return new DoublyLinkedListIterator();
	}

	@Override
	public Iterable<Position<E>> positions() {
		return new DLLIterable();
	}
	public Iterable<E> elements(){
		return new DLLElementIterable();
	}
	
	public E removeFirst() throws IllegalArgumentException {
		
		return remove(first);
	}
	
	public E removeLast() throws IllegalArgumentException {
		return remove(last);
	}
	
	public E remove(E element) {
		for(Position<E> pos : this.positions()) {
			if(pos.getElement().equals(element)) {
				remove(pos);
				return element;
			}
		}
		return null;
	}
	
	//class describing the position that is used for the list
	private class NodePosition<E> implements Position<E>{
		
		private E value;
		private NodePosition<E> next;
		private NodePosition<E> previous;
		private DoublyLinkedList list;
		
		//sets the value and the reference to the list that the position is in
		NodePosition(E value, DoublyLinkedList l){
			this.value = value;
			this.next = null;
			this.previous = null;
			this.list = l;
		}
		
		//gets the actual element stored the position
		@Override
		public E getElement() throws IllegalStateException {
			return value;
		}
	}
	
	private class DLLElementIterable implements Iterable<E>{

		Iterator<E> iterator;
		DLLElementIterable(){
			iterator = new DoublyLinkedListIterator<E>();
		}
		@Override
		public Iterator<E> iterator() {
			return iterator;
		}
		
	}
	
	private class DoublyLinkedListIterator<E> implements Iterator<E>{
		
		//the current position of the iterator
		NodePosition<E> head;
		
		DoublyLinkedListIterator(){
			//sets the position of the iterator to the start of the list
			head = (NodePosition<E>) first;
		}
		@Override
		public boolean hasNext() {
			//checks to see if there the current element is null
			return head != null;
		}

		@Override
		public E next() {
			//gets the current node's element then increments the position of the iterator
			E output = head.getElement();
			head = head.next;
			return output;
		}
		
	}
	
	//private class defining an Iterable for the data structure
	private class DLLIterable<E> implements Iterable<Position<E>>{
		
		private Iterator<Position<E>> iterator;
		//sets up a new instance of the iterator used to get the positions
		DLLIterable(){
			iterator = new DLLIterableIterator<E>();
		}
		
		//returns the stored iterator
		@Override
		public Iterator<Position<E>> iterator() {
			return (Iterator<Position<E>>) this.iterator;
		}
		
		//doubly nested class describing the iterator used only for the iterable because it iterates through the positions and not the values
		private class DLLIterableIterator<E> implements Iterator<Position<E>>{

			//the current node
			NodePosition<E> head;
			
			DLLIterableIterator(){
				//sets the current node to the first node in the list
				head = (NodePosition<E>) first;
			}
			@Override
			public boolean hasNext() {
				//checks if the current node is empty
				return head != null;
			}

			@Override
			public Position<E> next() {
				//gets the current node and then increments through the list
				Position<E> output = head;
				head = head.next;
				return output;
			}
			
		}
	}

}
