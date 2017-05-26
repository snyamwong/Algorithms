package edu.wit.cs.comp2350;

// represents a vertex in a graph, including a unique ID to keep track of vertex
public class Vertex {
	public double x;
	public double y;
	public int ID;
	public double c;
	public Vertex parent;
	public boolean transpose;
	
	public String toString(){
		String s = "ID: " + this.ID +
				" X: " + this.x +
				" Y: " + this.y +
				" C: " + this.c;
		return s;
		
	}
	
	public boolean equals(Vertex x){
		if(this.x == x.x && this.y == x.y && this.ID == x.ID){
			return true;
		}
		return false;
	}
}
