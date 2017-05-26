package edu.wit.cs.comp2350.tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import edu.wit.cs.comp2350.HashTable;
import edu.wit.cs.comp2350.LAB3;
import edu.wit.cs.comp2350.RandTable;
import edu.wit.cs.comp2350.SimpleTable;

public class LAB3TestCase{
	

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
	
	private void _testHash(HashTable h, int wordcount) {

		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		try {
			LAB3.printBabble(h, wordcount);
		} catch (ExitException e) {}
		assertEquals(outContent.toString().split(" ").length, wordcount);
	}

	@Test
	public void testSimple() {
		
		HashTable h = new SimpleTable(256);
		LAB3.buildTable(h, "text/dickens.txt");
		LAB3.printBabble(h, 5);
		_testHash(h, 5);
		
		h = new SimpleTable(256);
		LAB3.buildTable(h, "text/dickens.txt");
		LAB3.printBabble(h, 100);
		_testHash(h, 100);
		
		h = new SimpleTable(256);
		LAB3.buildTable(h, "text/dickens.txt");
		LAB3.printBabble(h, 4000);
		_testHash(h, 4000);
	}

	@Test
	public void testRand() {
		
		HashTable h = new RandTable(256);
		LAB3.buildTable(h, "text/dickens.txt");
		LAB3.printBabble(h, 5);
		_testHash(h, 5);
		
		h = new SimpleTable(256);
		LAB3.buildTable(h, "text/dickens.txt");
		LAB3.printBabble(h, 100);
		_testHash(h, 100);
		
		h = new SimpleTable(256);
		LAB3.buildTable(h, "text/dickens.txt");
		LAB3.printBabble(h, 4000);
		_testHash(h, 4000);
	}


}
