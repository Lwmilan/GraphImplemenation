package main;

import net.datastructures.Edge;
import net.datastructures.Vertex;


public class Course {
	private AdjListGraph<String, Integer> graph;
	private HashMap<String, Vertex<String>> map;

	/**
	 * @param courses: An array of course information. Each element in the array is
	 *                 also array: starts with the course name, followed by a list
	 *                 (0 or more) of prerequisite course names.
	 * 
	 */
	public Course(String courses[][]) {
		graph = new AdjListGraph<String, Integer>(true);
		map = new HashMap<String, Vertex<String>>();

		for (int i = 0; i < courses.length; i++) {
			for (int j = 0; j < courses[i].length; j++) {
				if (map.get(courses[i][j]) == null) {
					map.put(courses[i][j], graph.insertVertex(courses[i][j]));
				}
			}
			if (courses[i].length > 1) {
				for (int j = 1; j < courses[i].length; j++) {
					graph.insertEdge(map.get(courses[i][0]), map.get(courses[i][j]), (courses[i].length - 1));
				}
			}
		}
	}

	/**
	 * @param course
	 * @return find the earliest semester that the given course could be taken by a
	 *         students after taking all the prerequisites.
	 */
	public int whichSemester(String course) {
		HashMap<String, Vertex<String>> visited = new HashMap<String, Vertex<String>>();
		HashMap<String, Integer> depth = new HashMap<String, Integer>();
		CircularArrayQueue<Vertex<String>> queue = new CircularArrayQueue<Vertex<String>>(64);
		int countSemester = 1;
		Vertex<String> v = map.get(course);
		visited.put(v.getElement(), v);
		depth.put(v.getElement(), 1);
		queue.enqueue(v);
		Vertex<String> parent = v;
		while(!queue.isEmpty()) {
			Vertex<String> check = getList(parent,visited);
			if (check!= null) {
				queue.enqueue(check);
				visited.put(check.getElement(), check);
				depth.put(check.getElement(), depth.get(parent.getElement())+1);
				parent = check;
				if(depth.get(parent.getElement())> countSemester) {
					countSemester = depth.get(parent.getElement());
				}
			}
			else parent = queue.dequeue();
		}
		return countSemester;
	}

	public Vertex<String> getList(Vertex<String> v, HashMap<String, Vertex<String>> hasVisited) {
		ArrayList<Vertex<String>> verts = new ArrayList<Vertex<String>>();
		for (Edge<Integer> e : graph.outgoingEdges(v)) {
			verts.addLast(graph.opposite(v, e));
		}
		for(int i = 0; i < verts.size(); i++) {
			if(hasVisited.get(verts.get(i).getElement()) == null) {
				return verts.get(i);
			}
		}
		return null;
	}
}
