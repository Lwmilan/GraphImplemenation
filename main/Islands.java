package main;

import net.datastructures.Edge;
import net.datastructures.Vertex;

/**
 * @author Ruihong Zhang
 * Reference: Textbook R-14.27 on page 679
 *
 */
public class Islands  {

	private AdjListGraph<Integer,Integer> graph;
	private HashMap<Integer,Vertex<Integer>> map;
	/**
	 * @param numOfIslands: total number of islands. It will be numbered as 0,1,2,...
	 * @param distance: distance[i][j] represents the distance between island[i] and island[j]. 
	 * 					-1 means there is no edge between island[i] and island[j]. 
	 */
	public Islands(int numOfIslands, int distance[][]) {
		graph = new AdjListGraph<Integer,Integer>();
		map = new HashMap<Integer,Vertex<Integer>>();
		for(int i = 0; i< numOfIslands; i++) {
			map.put(i, graph.insertVertex(i));
		}
		for(int i = 0; i<distance.length; i++) {
			for(int j = 0; j<distance[i].length; j++) {
				if(distance[i][j]!= -1) graph.insertEdge(map.get(i), map.get(j), distance[i][j]);
			}
		}
		
	}


	/**
	 * @return the cost of minimum spanning tree using Kruskal's algorithm. 
	 */
	public int Kruskal() {
		int numOfEdges = graph.numVertices()-1;
		HeapPQ<Integer,Edge<Integer>> sortedEdges = new HeapPQ<Integer,Edge<Integer>>();
		HashMap<Vertex<Integer>, Integer> clouds = new HashMap<Vertex<Integer>, Integer>();
		HashMap<Integer,Integer> cloudSpan = new HashMap<Integer,Integer>();
		int count = 0;
		for(Vertex<Integer> v : graph.vertices()) {
			clouds.put(v, count);
			cloudSpan.put(count, 0);
			count++;
		}
		for(Edge<Integer> e : graph.edges()) {
			sortedEdges.insert(e.getElement(), e);
		}
		for(int i = 0; i<numOfEdges; i++) {
			Edge<Integer> e = sortedEdges.removeMin().getValue();
			Vertex<Integer>[] vertices = graph.endVertices(e);
			if(clouds.get(vertices[0]) != clouds.get(vertices[1])){
				cloudSpan.put(clouds.get(vertices[0]), cloudSpan.get(clouds.get(vertices[0]))+e.getElement()+ cloudSpan.get(clouds.get(vertices[1])));
				cloudSpan.put(clouds.get(vertices[1]), 0);
				int oldCloud = clouds.get(vertices[1]);
				for(Vertex<Integer> v: clouds.keySet()) {
					if(clouds.get(v) == oldCloud) {
						clouds.put(v, clouds.get(vertices[0]));
					}
				}
			}
			else {
				i--;
			}
		}
		int output = 0;
		for(Integer i : cloudSpan.values()) {
			output+= i;
		}
		return output;
		
	}
}
