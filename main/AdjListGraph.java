package main;

import net.datastructures.*;

/*
 * Implement Graph interface. A graph can be declared as either directed or undirected.
 * In the case of an undirected graph, methods outgoingEdges and incomingEdges return the same collection,
 * and outDegree and inDegree return the same value.
 *
 */
public class AdjListGraph<V, E> implements Graph<V, E> {

	private boolean directed = false;
	private DoublyLinkedList<AdjListEdge<E>> edges;
	private DoublyLinkedList<AdjListVertex<V>> vertices;
	
	/**
	 * calls the general constructor and sets the directed variable
	 * @param directed whether the graph is directed or not
	 */
	public AdjListGraph(boolean directed) {
		this();
		this.directed = directed;
	}

	public AdjListGraph() {
		edges = new DoublyLinkedList<AdjListEdge<E>>();
		vertices = new DoublyLinkedList<AdjListVertex<V>>();
	}


	@TimeComplexity("O(m)")
	/* (non-Javadoc)
	 * puts edges into comprenendable list then returns the iterator for that list
	 * @see net.datastructures.Graph#edges()
	 */
	public Iterable<Edge<E>> edges() {
		DoublyLinkedList<Edge<E>> output = new DoublyLinkedList<Edge<E>>();
		for(Edge<E> e : edges.elements()) {
			output.addFirst(e);
		}
		return output.elements();
	}

	@TimeComplexity("O(1)")
	/* (non-Javadoc)
	 * 
	 * @see net.datastructures.Graph#endVertices(net.datastructures.Edge)
	 */
	public Vertex[] endVertices(Edge<E> e) throws IllegalArgumentException {
		if(!(e instanceof AdjListEdge)) throw new IllegalArgumentException("Invalid Edge");
		//returns the vertices contained within the AdjListEdge object
		AdjListEdge<E> edge = (AdjListEdge)e;
		return new Vertex[] {edge.outgoingVertex,edge.incomingVertex};
	}

	@TimeComplexity("O(1)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#insertEdge(net.datastructures.Vertex, net.datastructures.Vertex, java.lang.Object)
	 */
	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E o)
			throws IllegalArgumentException {
		//checks for type
		if(!(u instanceof AdjListVertex || v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Vertex");
		//casts input variables to the correct type
		AdjListVertex<V> out = (AdjListVertex<V>)u;
		AdjListVertex<V> in = (AdjListVertex<V>)v;
		//creates the edge from the out vertex to the in vertex and creates the opposite edge if it is not directed
		AdjListEdge<E> output = out.createOutgoingEdge(in, o);
		if(!directed)in.createOutgoingInteriorEdge(out, o);

		return output;
	}

	@TimeComplexity("O(1)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#insertVertex(java.lang.Object)
	 */
	public Vertex<V> insertVertex(V o) {
		AdjListVertex<V> vertex = new AdjListVertex<V>(o);
		vertices.addFirst(vertex);
		return vertex;
	}

	@TimeComplexity("O(1)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#numEdges()
	 */
	public int numEdges() {
		return edges.size();
	}

	@TimeComplexity("O(1)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#numVertices()
	 */
	public int numVertices() {
		// TODO Auto-generated method stub
		return vertices.size();
	}

	
	@TimeComplexity("O(1)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#opposite(net.datastructures.Vertex, net.datastructures.Edge)
	 */
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e)
			throws IllegalArgumentException {
		//checks for the correct type and casts
		if(!(e instanceof AdjListEdge)) throw new IllegalArgumentException("Invalid Edge");
		AdjListEdge<E> edge = (AdjListEdge<E>)e;
		
		//check which side of the edge is the vertex and returns the other
		if(edge.incomingVertex.equals(v)) return edge.outgoingVertex;
		else if(edge.outgoingVertex.equals(v)) return edge.incomingVertex;
		return null;
	}

	@TimeComplexity("O(m)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#removeEdge(net.datastructures.Edge)
	 */
	public void removeEdge(Edge<E> e) throws IllegalArgumentException {
		if(!(e instanceof AdjListEdge)) throw new IllegalArgumentException("Invalid Edge");
		AdjListEdge<E> edge = (AdjListEdge<E>)e;
		edges.remove(edge);
		edge.incomingVertex.removeEdge(edge);
		edge.outgoingVertex.removeEdge(edge);
		if(!directed) {
			edge = (AdjListEdge<E>)getEdge(edge.incomingVertex, edge.outgoingVertex);
			edge.incomingVertex.removeEdge(edge);
			edge.outgoingVertex.removeEdge(edge);
		}
	}

	@TimeComplexity("O(m)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#removeVertex(net.datastructures.Vertex)
	 */
	public void removeVertex(Vertex<V> v) throws IllegalArgumentException {
		if(!(v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Vertex");
		AdjListVertex<E> vertex = (AdjListVertex<E>) v;
		for(AdjListEdge<E> e : vertex.incomingEdges) {
			edges.remove(e);
		}
		for(AdjListEdge<E> e : vertex.outgoingEdges) {
			edges.remove(e);
		}
	}

	@TimeComplexity("O(1)")
	/* 
     * replace the element in edge object, return the old element
     */
	public E replace(Edge<E> e, E o) throws IllegalArgumentException {
		E old = e.getElement();
		if(!(e instanceof AdjListEdge)) throw new IllegalArgumentException("Invalid Edge");
		((AdjListEdge<E>)e).element = o;
		return old;
	}

	@TimeComplexity("O(1)")
    /* 
     * replace the element in vertex object, return the old element
     */
	public V replace(Vertex<V> v, V o) throws IllegalArgumentException {
		V old = v.getElement();
		if(!(v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Edge");
		((AdjListVertex<V>)v).element = o;
		return old;
	}

	@TimeComplexity("O(n)")
	/* (non-Javadoc)
	 * @see net.datastructures.Graph#vertices()
	 */
	public Iterable<Vertex<V>> vertices() {
		DoublyLinkedList<Vertex<V>> output = new DoublyLinkedList<Vertex<V>>();
		for(Vertex<V> e : vertices.elements()) {
			output.addFirst(e);
		}
		return output.elements();
	}

	@TimeComplexity("O(1)")
	@Override
	public int outDegree(Vertex<V> v) throws IllegalArgumentException {
		if(!(v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Edge");
		AdjListVertex<V> vertex = (AdjListVertex<V>)v;
		return vertex.outgoingEdges.size();
	}

	@TimeComplexity("O(1)")
	@Override
	public int inDegree(Vertex<V> v) throws IllegalArgumentException {
		if(!(v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Edge");
		AdjListVertex<V> vertex = (AdjListVertex<V>)v;
		return vertex.incomingEdges.size();
	}

	@TimeComplexity("O(n)")
	@Override
	public Iterable<Edge<E>> outgoingEdges(Vertex<V> v)
			throws IllegalArgumentException {
		if(!(v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Edge");
		AdjListVertex<V> vertex = (AdjListVertex<V>)v;
		DoublyLinkedList<Edge<E>> output = new DoublyLinkedList<Edge<E>>();
		for(Edge<E> e : vertex.outgoingEdges.elements()) {
			output.addFirst(e);
		}
		return output.elements();
	}

	@TimeComplexity("O(n)")
	@Override
	public Iterable<Edge<E>> incomingEdges(Vertex<V> v)
			throws IllegalArgumentException {
		if(!(v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Edge");
		AdjListVertex<V> vertex = (AdjListVertex<V>)v;
		DoublyLinkedList<Edge<E>> output = new DoublyLinkedList<Edge<E>>();
		for(Edge<E> e : vertex.incomingEdges.elements()) {
			output.addFirst(e);
		}
		return output.elements();
	}

	@TimeComplexity("O(m)")
	@Override
	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v)
			throws IllegalArgumentException {
		if(!(u instanceof AdjListVertex || v instanceof AdjListVertex)) throw new IllegalArgumentException("Invalid Vertex");
		AdjListVertex<V> vertex = (AdjListVertex<V>)u;
		for(AdjListEdge<E> e : vertex.outgoingEdges.elements()) {
			if(e.incomingVertex.equals(v)) return e;
		}
		return null;
	}

	
	private class AdjListEdge<E> implements Edge<E>{

		AdjListVertex<V> incomingVertex;
		AdjListVertex<V> outgoingVertex;
		E element;
		
		public AdjListEdge(E e, AdjListVertex in, AdjListVertex out){
			incomingVertex = in;
			outgoingVertex = out;
			element = e;
		}
		
		@Override
		public E getElement() {
			return element;
		}
	}
	
	private class AdjListVertex<V> implements Vertex<V>{

		V element;
		DoublyLinkedList<AdjListEdge<E>> outgoingEdges;
		DoublyLinkedList<AdjListEdge<E>> incomingEdges;
		
		
		public AdjListVertex(V e) {
			element = e;
			outgoingEdges = new DoublyLinkedList<AdjListEdge<E>>();
			incomingEdges = new DoublyLinkedList<AdjListEdge<E>>();
		}
		public AdjListEdge<E> createOutgoingEdge(AdjListVertex<V> v, E element) {
			AdjListEdge output = new AdjListEdge<E>(element,v,this);
			edges.addFirst(output);
			outgoingEdges.addFirst(output);
			v.incomingEdges.addFirst(output);
			return output;
		}
		public AdjListEdge<E> createOutgoingInteriorEdge(AdjListVertex<V> v,E element){
			AdjListEdge output = new AdjListEdge<E>(element,v,this);
			outgoingEdges.addFirst(output);
			v.incomingEdges.addFirst(output);
			return output;
		}
		public void removeEdge(AdjListEdge<E> e) {
			outgoingEdges.remove(e);
			incomingEdges.remove(e);
		}
		
		
		@Override
		public V getElement() {
			return element;
		}
		
	}
	
	
}
