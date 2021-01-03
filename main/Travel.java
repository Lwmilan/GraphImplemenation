package main;

import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.Vertex;


public class Travel {
	
	private AdjListGraph<String,Integer> graph;
	private HashMap<String,Vertex<String>> map;
	/**
	 * @param routes: Array of routes between cities. 
	 *                routes[i][0] and routes[i][1] represent the city names on both ends of the route. 
	 *                routes[i][2] represents the cost in string type. 
	 *                Hint: In Java, use Integer.valueOf to convert string to integer. 
	 */
	public Travel(String [][] routes) {
		graph = new AdjListGraph<String,Integer>();
		map = new HashMap<String,Vertex<String>>();
		for(int i = 0; i<routes.length; i++) {
			if(map.get(routes[i][0]) == null ) {
				map.put(routes[i][0], graph.insertVertex(routes[i][0]));
			}
			if(map.get(routes[i][1]) == null ) {
				map.put(routes[i][1], graph.insertVertex(routes[i][1]));
			}
			graph.insertEdge(map.get(routes[i][0]), map.get(routes[i][1]), Integer.valueOf(routes[i][2]));
		}
	}
	
	
	
	/**
	 * @param departure: the departure city name 
	 * @param destination: the destination city name
	 * @return Return the path from departure city to destination using Depth First Search algorithm. 
	 *         The path should be represented as ArrayList or DoublylinkedList of city names. 
	 *         The order of city names in the list should match order of the city names in the path.  
	 *         
	 * @IMPORTANT_NOTE: The outgoing edges should be traversed by the order of the city names stored in
	 *                 the opposite vertices. For example, if V has 3 outgoing edges as in the picture below,
	 *                           V
	 *                        /  |  \
	 *                       /   |    \
	 *                      B    A     F  
	 *              your algorithm below should visit the outgoing edges of V in the order of A,B,F.
	 *              This means you will need to create a helper function to sort the outgoing edges by 
	 *              the opposite city names.
	 *              	              
	 *              See the method sortedOutgoingEdges below. 
	 */
	public Iterable<String> DFSRoute(String departure, String destination ) {
		HashMap<String,Vertex<String>> visited = new HashMap<String, Vertex<String>>();
		return DFSRouteHelper(visited, departure,destination).iterable();
	}
	
	private ArrayList<String> DFSRouteHelper(HashMap<String,Vertex<String>> visited, String current, String destination) {
		ArrayList<String> list = new ArrayList<String>();
		Vertex<String> v = map.get(current);
		visited.put(current, v);
		if(graph.outDegree(v)==0 && current != destination) return list;
		list.addLast(current);
		if(current.equals(destination)) return list;
		for(Edge<Integer> e : sortedOutgoingEdges(v)) {
			if(visited.get(graph.opposite(v, e).getElement()) == null ) {
				ArrayList<String> append = DFSRouteHelper(visited,graph.opposite(v, e).getElement(),destination);
				if(append.get(append.size()-1).equals(destination))
				for(String s : append) {
					list.addLast(s);
				}
			}
		}
		
		return list;
	}
	
	
	
	/**
	 * @param departure: the departure city name 
	 * @param destination: the destination city name
     * @return Return the path from departure city to destination using Breadth First Search algorithm. 
	 *         The path should be represented as ArrayList or DoublylinkedList of city names. 
	 *         The order of city names in the list should match order of the city names in the path.  
	 *         
	 * @IMPORTANT_NOTE: The outgoing edges should be traversed by the order of the city names stored in
	 *                 the opposite vertices. For example, if V has 3 outgoing edges as in the picture below,
	 *                           V
	 *                        /  |  \
	 *                       /   |    \
	 *                      B    A     F  
	 *              your algorithm below should visit the outgoing edges of V in the order of A,B,F.
	 *              This means you will need to create a helper function to sort the outgoing edges by 
	 *              the opposite city names.
	 *              	             
	 *              See the method sortedOutgoingEdges below. 
	 */
	
	public Iterable<String> BFSRoute(String departure, String destination ) {
		ArrayList<String> list = new ArrayList<String>();
		HashMap<String,Vertex<String>> visited = new HashMap<String, Vertex<String>>();
		HashMap<String,String> childParent = new HashMap<String,String>();
		CircularArrayQueue<Vertex<String>> queue = new CircularArrayQueue<Vertex<String>>(64);
		Vertex<String> v = map.get(departure);
		queue.enqueue(v);
		visited.put(v.getElement(), v);
		Vertex<String> parent = null;
		while(!queue.isEmpty() && !v.getElement().equals(destination)) {
			parent = v;
			v = queue.dequeue();
			list.addLast(v.getElement());
			for(Edge<Integer> e : sortedOutgoingEdges(v)) {
				Vertex<String> nextVertex = graph.opposite(v,e);
				if(visited.get(nextVertex.getElement()) == null) {
					visited.put(nextVertex.getElement(), nextVertex);
					if(childParent.get(nextVertex.getElement()) == null) childParent.put(nextVertex.getElement(), v.getElement());
					queue.enqueue(nextVertex);
				}
			}
		}
		ArrayList<String> output = new ArrayList<String>();
		String current = v.getElement();
		while(childParent.get(current) != null) {
			output.addFirst(current);
			current = childParent.get(current);
		}
		output.addFirst(current);
		return output.iterable();
	}
	
	/**
	 * @param departure: the departure city name 
	 * @param destination: the destination city name
	 * @param itinerary: an empty DoublylinkedList object will be passed in to the method. 
	 * 	       When a shorted path is found, the city names in the path should be added to the list in the order. 
	 * @return return the cost of the shortest path from departure to destination. 
	 *         
	 * @IMPORTANT_NOTE: The outgoing edges should be traversed by the order of the city names stored in
	 *                 the opposite vertices. For example, if V has 3 outgoing edges as in the picture below,
	 *                           V
	 *                        /  |  \
	 *                       /   |    \
	 *                      B    A     F  
	 *              your algorithm below should visit the outgoing edges of V in the order of A,B,F.
	 *              This means you will need to create a helper function to sort the outgoing edges by 
	 *              the opposite city names.
	 *              
	 *              See the method sortedOutgoingEdges below. 
	 */

	public int DijkstraRoute(String departure, String destination, DoublyLinkedList<String> itinerary ) {
		HeapPQ<Integer,Vertex<String>> heap = new HeapPQ<Integer,Vertex<String>>();
		HashMap<Vertex<String>,Integer> distMap = new HashMap<Vertex<String>,Integer>();
		HashMap<Vertex<String>,Entry<Integer,Vertex<String>>> pqEntryMap = new HashMap<Vertex<String>,Entry<Integer,Vertex<String>>>();
		HashMap<Vertex<String>, Edge<Integer>> forest = new HashMap<Vertex<String>, Edge<Integer>>();
		for(Vertex<String> v : graph.vertices()) {
			if(v.equals(map.get(departure))) {
				pqEntryMap.put(v, heap.insert(0, v));
				distMap.put(v, 0);
			}
			else {
				pqEntryMap.put(v, heap.insert(Integer.MAX_VALUE, v));
				distMap.put(v, Integer.MAX_VALUE);
			}
		}
		while(!heap.isEmpty()) {
			Entry<Integer,Vertex<String>> entry = heap.removeMin();
			Vertex<String> v = entry.getValue();
			for(Edge<Integer> e : sortedOutgoingEdges(v)) {
				Vertex w = graph.opposite(v, e);
				int newDistance = e.getElement() + entry.getKey();
				if(newDistance<distMap.get(w)) {
					distMap.put(w, newDistance);
					heap.replaceKey(pqEntryMap.get(w), newDistance);
					forest.put(w, e);
				}
			}
		}
		Vertex<String> v = map.get(destination);
		while(!v.equals(map.get(departure))) {
			itinerary.addFirst(v.getElement());
			v= graph.opposite(v, forest.get(v));
		}
		itinerary.addFirst(v.getElement());

		
		return distMap.get(map.get(destination));
		
	}
	
	

	/**
	 * I strongly recommend you to implement this method to return sorted outgoing edges for vertex V
	 * You may use any sorting algorithms, such as insert sort, selection sort, etc.
	 * 
	 * @param v: vertex v
	 * @return a list of edges ordered by edge's name
	 */
	
	public Iterable<Edge<Integer>> sortedOutgoingEdges(Vertex<String> v)  {
		
		HeapPQ<String,Edge<Integer>> heap = new HeapPQ<String,Edge<Integer>>();
		for(Edge<Integer> e : graph.outgoingEdges(v)) {
			heap.insert(graph.opposite(v, e).getElement(), e);
		}
		ArrayList<Edge<Integer>> list = new ArrayList<Edge<Integer>>();
		while(!heap.isEmpty())list.addLast(heap.removeMin().getValue());
		return list.iterable();
	}
	
}
