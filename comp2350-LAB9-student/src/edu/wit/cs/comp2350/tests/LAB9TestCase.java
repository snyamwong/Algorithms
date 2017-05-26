package edu.wit.cs.comp2350.tests;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.Random;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.Edge;
import edu.wit.cs.comp2350.Graph;
import edu.wit.cs.comp2350.LAB9;

import static org.junit.Assert.*;


public class LAB9TestCase{

	@Rule
	public Timeout globalTimeout = Timeout.seconds(15);
	
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

	private void _testMST(Graph g, double expectedWeight, double epsilon) {
		
		try {
			g.epsilon = epsilon;
			LAB9.FindMST(g);
		} catch (ExitException e) {}
		
		boolean[] contains = new boolean[g.size()];
		
		Edge[] edges = g.getEdges();
		
		assertEquals("MST must have " + (g.size() - 1) + " edges",
				g.size() - 1, edges.length);
		
		double actualWeight = 0;
		for (Edge e: edges) {
			actualWeight += e.cost;
			contains[e.src.ID] = true;
			contains[e.dst.ID] = true;
		}
		
		assertEquals(expectedWeight, actualWeight, 1E-4);
		assertEquals(expectedWeight, g.getTotalEdgeWeight(), 1E-4);
		
		// make sure all vertices are spanned
		for (int i = 0; i < contains.length; i++)
			assertTrue("missing vertex with ID " + i, contains[i]);
		
	}

	private void _testRandMST(Graph g) {
		
		try {
			g.epsilon = 1.5;
			LAB9.FindMST(g);
		} catch (ExitException e) {}
		
		boolean[] contains = new boolean[g.size()];
		
		Edge[] edges = g.getEdges();
		
		assertEquals("MST must have " + (g.size() - 1) + " edges",
				g.size() - 1, edges.length);
		
		double actualWeight = 0;
		for (Edge e: edges) {
			actualWeight += e.cost;
			contains[e.src.ID] = true;
			contains[e.dst.ID] = true;
		}
		
		assertEquals(actualWeight, g.getTotalEdgeWeight(), 1E-4);
		
		// make sure all vertices are spanned
		for (int i = 0; i < contains.length; i++)
			assertTrue("missing vertex with ID " + i, contains[i]);
	}
	
	private void _testFileMST(String file, double expectedWeight, double epsilon) {
		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file))) {
			while(f.hasNextDouble()) // each node listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}
		
		_testMST(g, expectedWeight, epsilon);
	}
	
	private Graph _genRandGraph(int size) {
		Graph g = new Graph();
		Random r = new Random();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < i; j++) {
				g.addVertex(r.nextDouble(), r.nextDouble());
			}
		}
		
		return g;
	}
	
	@Test
	public void testTiny() {
		_testFileMST("points/tiny", 2.0, 1.5);
	}
	
	@Test
	public void testSmall() {
		_testFileMST("points/small1", 2.118, 1.5);
		_testFileMST("points/small2", 1.5, 1.5);
	}
	
	@Test
	public void testPicasso() {
		_testFileMST("points/picasso", 3.2167, .3);
	}
	
	@Test
	public void testCapitals() {
		_testFileMST("points/state_capitals", 2.9449, 1.5);
	}
	
	@Test
	public void testRandSmall() {
		for (int i = 0; i < 10; i++)
			_testRandMST(_genRandGraph(5));
	}
	
	@Test
	public void testRandLarge() {
		for (int i = 0; i < 3; i++)
			_testRandMST(_genRandGraph(50));
	}
	
}