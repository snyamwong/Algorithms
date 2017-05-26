package edu.wit.cs.comp2350;

import java.util.ArrayList;

// represents a graph as a list of vertices and edges
public class Graph implements GraphInterface {
	private ArrayList<Vertex> vs;
	private ArrayList<Edge> edges;
	public double epsilon = 0; // set to maximum edge distance
	private int nextVertexID = 0;	// unique ID of each vertex
	
	public Graph() {
		vs = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	
	public Graph(ArrayList<Vertex> x, ArrayList<Edge> e){
		vs = x;
		edges = e;
	}
	
	public void addVertex(double x, double y) {
		Vertex v = new Vertex();
		v.x = x; v.y = y; v.ID = nextVertexID++;
		vs.add(v);
	}
	
	// adds an edge to graph if it is within epsilon limit
	public void addEdge(Vertex src, Vertex dst) {
		if (dist(src, dst) < epsilon)
			edges.add(new Edge(src, dst));
	}
	
	// finds the cartesian distance between two vertices
	private static double dist(Vertex s, Vertex d) {
		return Math.sqrt(Math.pow(s.x-d.x, 2) + Math.pow(s.y-d.y, 2));
	}
	
	public int size() {
		return vs.size();
	}
	
	public Vertex[] getVertices() {
		return vs.toArray(new Vertex[vs.size()]);
	}
	
	public Edge[] getEdges() {
		return edges.toArray(new Edge[edges.size()]);
	}
	
	// sums up the costs of all edges in the graph
	public double getTotalEdgeWeight() {
		double ret = 0;
		for (Edge e: edges)
			ret += e.cost;
		return ret;
	}
	
	public void removeMaxWeight(Edge e){
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).equals(e)){
				this.edges.remove(e);
			}
		}
	}
	
	public void printArray(){
		for(int i = 0; i < edges.size(); i++){
			System.out.println(edges.get(i));
		}
	}
}
