package edu.wit.cs.comp2350;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * @author kreimendahl
 */

// Provides a solution to the minimal spanning tree problem

/**
 * Problems still: Edges repeat themselves
 * 
 * Problems solved: Heaps actually work now
 * 
 * @author wongt1
 *
 */
public class LAB9 {
	private static int heapSize;
	// private static double minWeight = Integer.MAX_VALUE;
	// private static Vertex v1;
	// private static Vertex v2;

	public static void FindMST(Graph g) {
		// TODO implement this method
		// All methods from Lab 2 with modifications, creating heap here
		heapSize = 0;
		Vertex[] min_heap = new Vertex[g.size()];
		Vertex[] vertexToEdge = new Vertex[g.size()];

		for (Vertex v : g.getVertices()) {
			v.c = Double.MAX_VALUE;
			insertHeap(min_heap, v);
		}

		min_heap[0].c = 0;
		vertexToEdge[0] = min_heap[0];
		min_heap = buildHeap(min_heap);

//		System.out.println("pre min_heap");
//		for (Vertex x : min_heap) {
//			System.out.println(x);
//		}

		System.out.println();

		// growing tree here
		while (heapSize != 0) {
			min_heap = minHeapify(min_heap, 0);

//			System.out.println("Min heap before extracting: ");
//			for (int i = 0; i < heapSize; i++) {
//				System.out.println(min_heap[i]);
//			}
//			System.out.println();

			Vertex temp = extractMin(min_heap);
//			System.out.println("Temp: " + temp + "\n");

			if(vertexToEdge[temp.ID] == null){
				vertexToEdge[temp.ID] = temp;
			}
			
			// Reprinting the min_heap
//			System.out.println("Min_heap after extracting:");
//			for (int i = 0; i < heapSize; i++) {
//				System.out.println(min_heap[i]);
//			}
//			System.out.println();

			// To fix, first you build the graph with all the edges attached.
			// Maybe use a Map to record all the edges and its parents(!!!)
			for (int i = 0; i < heapSize; i++) {
				Vertex v = min_heap[i];

				// System.out.println("Vertex v: " + v);

				Double weight = Math.sqrt(Math.pow(temp.x - v.x, 2) + Math.pow(temp.y - v.y, 2));
				// System.out.println("Weight: " + weight);
				// System.out.println("v.c: " + v.c);

				if (vertexToEdge[v.ID] == null) {
					vertexToEdge[v.ID] = v;
				}

				if (weight < v.c) {
					// System.out.println("weight < v.c\n");
					v.c = weight;
					v.parent = temp;
					vertexToEdge[v.ID] = v;

					// Edge e = new Edge(v, v.parent);
					// Need to know how to replace edge
					// if (minWeight > e.cost) {
					// minWeight = e.cost;
					// v1 = v;
					// v2 = v.parent;
					// }
					//
					// System.out.println(e);
					// System.out.println();
				}
			}

//			System.out.println("Vertex to edge: ");
//			for (int i = 0; i < vertexToEdge.length; i++) {
//				System.out.println(vertexToEdge[i]);
//			}
//			System.out.println();

			min_heap = rebuildHeap(min_heap);

			// Error here is that the last edge isn't being added
			// Instead, it's adding an old edge where it's increased by
			// 1.118 instead of .5
			// if(heapSize >= 1){
			// Edge e = new Edge(v1, v2);
			// System.out.println("Edge being added: ");
			// System.out.println(e);
			// g.addEdge(v1, v2);
			// minWeight = Integer.MAX_VALUE;
			// }
			//
			// System.out.println();
		}

		for (int i = 0; i < vertexToEdge.length; i++) {
			if (vertexToEdge[i] != null) {
				Vertex v1 = vertexToEdge[i];
				Vertex v2 = vertexToEdge[i].parent;

				if (vertexToEdge[i].parent != null) {
//					Edge e = new Edge(v1, v2);
//					System.out.println(e);
//					System.out.println();

					g.addEdge(v1, v2);
				}
			}
		}

//		System.out.println(g.getTotalEdgeWeight());
	}

	// public static boolean contains(Vertex[] a, Vertex key) {
	// for (Vertex v : a) {
	// if (key.ID == v.ID) {
	// return true;
	// }
	// }
	// return false;
	// }

	private static Vertex[] buildHeap(Vertex[] a) {
		heapSize = a.length;

		for (int i = (a.length - 1) / 2; i >= 0; i--) {
			minHeapify(a, i);
		}

		return a;
	}

	private static Vertex[] rebuildHeap(Vertex[] a) {
		for (int i = (heapSize - 1) / 2; i >= 0; i--) {
			minHeapify(a, i);
		}

		return a;
	}

	private static void insertHeap(Vertex[] a, Vertex key) {
		heapSize = heapSize + 1;

		int i = heapSize - 1;
		int parent = (heapSize - 1) / 2;
		Vertex temp;
		a[heapSize - 1] = key;

		while (i > 0 && a[parent].c > a[i].c) {
			temp = a[i];
			a[i] = a[parent];
			a[parent] = temp;

			i = parent;
			parent = (i - 1) / 2;

			// System.out.println("a[parent]: " + a[parent]);
			// System.out.println("a[i]: " + a[i]);
		}

		// In case the insertHeap method doesn't sort the insertion node
		a = minHeapify(a, 0);
	}

	private static Vertex extractMin(Vertex[] a) {
		Vertex min = a[0];
		a[0] = a[heapSize - 1];
		heapSize = heapSize - 1;
		a = minHeapify(a, 0);

		// System.out.println("Min from extractMin: " + min);

		return min;
	}

	private static Vertex[] minHeapify(Vertex[] a, int index) {
		// Index for left and right node of the binary tree
		int left = 2 * (index) + 1;
		int right = 2 * (index) + 2;
		int smallest = index;
		Vertex temp;

		// left node < heapSize
		if (left < heapSize && a[left].c < a[smallest].c) {
			smallest = left;
		} else {
			smallest = index;
		}

		// right node < heapSize
		if (right < heapSize && a[right].c < a[smallest].c) {
			smallest = right;
		}

		if (smallest != index) {
			temp = a[index];
			a[index] = a[smallest];
			a[smallest] = temp;
			minHeapify(a, smallest);
		}

		return a;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;

		System.out.printf("Enter <points file> <edge neighborhood>\n");
		System.out.printf("(e.g: points/small .5)\n");
		file1 = s.next();

		// read in vertices
		Graph g = InputGraph(file1);
		g.epsilon = s.nextDouble();

		FindMST(g);

		s.close();

		System.out.printf("Weight of tree: %f\n", g.getTotalEdgeWeight());
	}

	// reads in an undirected graph from a specific file formatted with one
	// x/y node coordinate per line:
	private static Graph InputGraph(String file1) {

		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file1))) {
			while (f.hasNextDouble()) // each vertex listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}

		return g;
	}

}
