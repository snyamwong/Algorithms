package edu.wit.cs.comp2350.tests;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.LAB7;
import edu.wit.cs.comp2350.LAB7.Item;

import static org.junit.Assert.*;


public class LAB7TestCase{

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

	private void _testDyn(LAB7.Item[] items, int weight, int expectedBest) {
		LAB7.Item[] actual = new LAB7.Item[0];
		int actualBest = 0;
		
		try {
			actual = LAB7.FindDynamic(items, weight);
			actualBest = LAB7.getBest();
		} catch (ExitException e) {}
		
		int actualValues = 0;
		int actualWeight = 0;
		
		for (int i = 0; i < actual.length; i++) {
			actualValues += items[actual[i].index].value;
			actualWeight += items[actual[i].index].weight;
		}
		
		assertEquals(expectedBest, actualBest);
		assertEquals(actualValues, actualBest);
		assertTrue(weight >= actualWeight);
	}
	
	private void _testFileDyn(String file, int weight, int expectedBest) {
		ArrayList<Item> tableList = new ArrayList<Item>();

		try (Scanner f = new Scanner(new File(file))) {
			int i = 0;
			while(f.hasNextInt())
				tableList.add(new LAB7.Item(f.nextInt(), f.nextInt(), i++));
		} catch (IOException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}

		Item[] table = new Item[tableList.size()];
		for (int i = 0; i < tableList.size(); i++)
			table[i] = tableList.get(i);
		
		_testDyn(table, weight, expectedBest);
	}
	
	@Test
	public void testTiny() {
		_testFileDyn("objects/tiny", 2, 5);
		_testFileDyn("objects/tiny", 3, 8);
		_testFileDyn("objects/tiny", 4, 10);
	}
	
	@Test
	public void testSmall() {
		_testFileDyn("objects/small1", 26, 51);
		_testFileDyn("objects/small1", 39, 76);
		_testFileDyn("objects/small1", 40, 78);
		_testFileDyn("objects/small2", 5, 25);
		_testFileDyn("objects/small2", 6, 29);
		_testFileDyn("objects/small2", 7, 35);
		
	}
	
	@Test
	public void testLarge() {
		_testFileDyn("objects/large1", 5000, 7647);
		_testFileDyn("objects/large1", 6000, 8263);
		_testFileDyn("objects/large2", 35, 444);
		_testFileDyn("objects/large2", 60, 612);
	}

}