package edu.wit.cs.comp2350;

public class Edge {
	public Vertex src;
	public Vertex dst;
	public double cost;
	
	// creates an edge between two vertices
	Edge(Vertex s, Vertex d) {
		src = s;
		dst = d;
		cost = Math.sqrt(Math.pow(s.x-d.x, 2) + Math.pow(s.y-d.y, 2));
	}

	public boolean equals(Edge e){
		if(this.src.equals(e.src) && this.dst.equals(e.dst)){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return "Edge: " + this.src + " " + this.dst + " " + "Cost: " + this.cost;
	}
}
