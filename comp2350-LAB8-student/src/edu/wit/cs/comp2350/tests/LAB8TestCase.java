package edu.wit.cs.comp2350.tests;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.Graph;
import edu.wit.cs.comp2350.LAB8;
import edu.wit.cs.comp2350.Node;

import static org.junit.Assert.*;


public class LAB8TestCase{

	@Rule
	public Timeout globalTimeout = Timeout.seconds(1500);
	
	@SuppressWarnings("serial")
	private static class ExitException extends SecurityException {}
	
	private static class NoExitSecurityManager extends SecurityManager 
    {
        @Override
        public void checkPermission(Permission perm) {}
        
        @Override
        public void checkPermission(Permission perm, Object context) {}
        
        @Override
        public void checkExit(int status) { super.checkExit(status); throw new ExitException(); }
    }
	
	@Before
    public void setUp() throws Exception 
    {
        System.setSecurityManager(new NoExitSecurityManager());
    }
	
	@After
    public void tearDown() throws Exception 
    {
        System.setSecurityManager(null);
    }

	private void _testTopo(Graph g) {
		Node[] actual = new Node[0];
		HashSet<Integer> s = new HashSet<Integer>();
		
		try {
			actual = LAB8.FindTopo(g);
		} catch (ExitException e) {}
		boolean[] contains = new boolean[actual.length];
		
		assertEquals("Incorrect number of nodes output", g.size(), actual.length);
		
		for (int i = 0; i < actual.length; i++) {
			contains[actual[i].GetID()] = true;
			for (int j: actual[i].GetEdges())
				assertFalse(s.contains(j)); // j depends on i, so it should come after 
			s.add(actual[i].GetID());
		}
		
		// make sure all nodes are output
		for (int i = 0; i < actual.length; i++)
			assertTrue(contains[i]);
		
	}
	
	private void _testFileTopo(String file) {
		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file))) {
			int i = 0;
			while(f.hasNextLine()) { // each file listing
				String line = f.nextLine();
				Scanner lineScan = new Scanner(line);
				Node n = new Node(i, lineScan.next());
				lineScan.close();
				g.AddNode(n);
				i++;
			}
		} catch (IOException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}
		
		try (Scanner f = new Scanner(new File(file))) {
			int i = 0;
			while(f.hasNextLine()) { // each file listing
				String line = f.nextLine();
				Scanner lineScan = new Scanner(line);
				lineScan.next();	// skip over file name
				while (lineScan.hasNextInt())	// for each dependency
					g.GetNodes()[lineScan.nextInt()].AddEdge(i);
				lineScan.close();
				i++;
			}
		} catch (IOException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}
		
		_testTopo(g);
	}
	
	private Graph _genRandDAG(int size) {
		Graph g = new Graph();
		boolean[][] adj = new boolean[size][size];
		Random r = new Random();
		
		for (int i = 0; i < size; i++) {	// random lower triangular adjacency matrix
			for (int j = 0; j < i; j++) {
				adj[i][j] = r.nextBoolean();
			}
		}
		
		for (int i = size - 1; i > 0; i--) {	// shuffle rows
			int swapWith = r.nextInt(i);
			
			for (int j = 0; j < size; j++) {
				boolean temp = adj[i][j];
				adj[i][j] = adj[swapWith][j];
				adj[swapWith][j] = temp;
			}
		}
		
		for (int i = 0; i < size; i++) {	//  convert matrix to Graph
			g.AddNode(new Node(i, "file" + Integer.toString(i)));
			for (int j = 0; j < i; j++) {
				if (adj[i][j])
					g.AddEdge(j, i);
			}
		}
		
		return g;
	}
	
	@Test
	public void testTiny() {
		_testFileTopo("deps/tiny");
	}
	
	@Test
	public void testSmall() {
		_testFileTopo("deps/small1");
		_testFileTopo("deps/small2");
	}
	
	@Test
	public void testRandSmall() {
		for (int i = 0; i < 10; i++)
			_testTopo(_genRandDAG(5));
	}
	
	@Test
	public void testRandLarge() {
		for (int i = 0; i < 10; i++)
			_testTopo(_genRandDAG(50));
	}
	
}