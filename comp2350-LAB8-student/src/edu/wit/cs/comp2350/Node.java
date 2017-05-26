package edu.wit.cs.comp2350;

import java.util.ArrayList;

public class Node {
	
	private int ID;
	private String name;
	private ArrayList<Integer> edges;
	public int marker = 0;
	
	public Node(int ID, String name) {
		this.ID = ID;
		this.name = name;
		this.edges = new ArrayList<Integer>();
	}
	
	public void AddEdge(int dst) {
		edges.add(dst);
	}
	
	public String toString() {
		String ret = name;
		
		return ret;
	}
	
	public int[] GetEdges() {
		int[] ret = new int[edges.size()];
		for (int i = 0; i < edges.size(); i++)
			ret[i] = edges.get(i);
		
		return ret;
	}
	
	public int GetID() {
		return ID;
	}
}
