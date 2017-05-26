package edu.wit.cs.comp2350.tests;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.wit.cs.comp2350.Graph;
import edu.wit.cs.comp2350.LAB9;

public class GraphMaker extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	public GraphMaker()
	{
		super("Minimal spanning tree");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try
		{

			Graph g = InputGraph("points/picasso");
			g.epsilon = 2;
			
			LAB9.FindMST(g);
			Object[] verts = new Object[g.size()];
			
			for (edu.wit.cs.comp2350.Vertex v: g.getVertices()) {
				verts[v.ID] = graph.insertVertex(parent, null, "",  v.x*550, v.y*550, 3,
					3, "strokeColor=black;fillColor=black;shape=ellipse");
			}
			
			for (edu.wit.cs.comp2350.Edge e: g.getEdges())
				graph.insertEdge(parent, null, "", verts[e.src.ID], verts[e.dst.ID],
						"endArrow=none");
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	public static void main(String[] args)
	{
		GraphMaker frame = new GraphMaker();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}
	
	// reads in an undirected graph from a specific file formatted with one
	// x/y node coordinate per line:
	private static Graph InputGraph(String file1) {
		
		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file1))) {
			while(f.hasNextDouble()) // each node listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		return g;
	}

}
