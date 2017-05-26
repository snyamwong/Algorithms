package edu.wit.cs.comp2350;

import java.util.ArrayList;

// represents a graph as a list of Nodes
public class Graph {
	private ArrayList<Node> nodes;
	
	public Graph() {
		nodes = new ArrayList<Node>();
	}
	
	public void AddNode(Node n) {
		nodes.add(n);
	}
	
	public void AddEdge(int src, int dst) {
		nodes.get(src).AddEdge(dst);
	}
	
	public int size() {
		return nodes.size();
	}
	
	public Node[] GetNodes() {
		return nodes.toArray(new Node[nodes.size()]);
	}
	
	public Node[] GetEdges(Node n) {
		int[] edgeInds = n.GetEdges();
		Node[] ret = new Node[edgeInds.length];
		
		for (int i = 0; i < ret.length; i++)
			ret[i] = nodes.get(edgeInds[i]);
		
		return ret;
	}
}
