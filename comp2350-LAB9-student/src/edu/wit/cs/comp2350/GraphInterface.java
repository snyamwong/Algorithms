package edu.wit.cs.comp2350;

// provides an interface for any graph
public interface GraphInterface {
	
	void addVertex(double x, double y);
	
	Vertex[] getVertices();
	Edge[] getEdges();
	
	double getTotalEdgeWeight();
}
