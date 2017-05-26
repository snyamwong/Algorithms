package edu.wit.cs.comp2350.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;
import java.util.Arrays;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.LAB1;

public class LAB1TestCase{
	

	@Rule
	public Timeout globalTimeout = Timeout.seconds(10);
	
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
	
	private void _test(String[] values, String result) {
		final String input = String.join(" ", values);
		
		final String output = TestSuite.stringOutput(new String[] {
			"Enter the sorting algorithm to use ([c]ounting, [r]adix, [i]nsertion, or [s]ystem): ",
			"Enter the integers that you would like sorted: ",
			"%s\n" }, new Object[] { result });
		
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		System.setOut(new PrintStream(outContent));
		
		try {
			LAB1.main(new String[] { "foo" });
		} catch (ExitException e) {}
		assert(output.equals(outContent.toString()));
		
		System.setIn(null);
		System.setOut(null);
	}

	private int[] generateRandArray(int size) {
		int[] ret = new int[size];
		
		Random r = new Random();
		for (int i = 0; i < size; i++) {
			ret[i] = r.nextInt(LAB1.MAX_INPUT+1);
		}
		return ret;
	}
	
	private void testRand(char c, int size) {
		int[] randArray = generateRandArray(size);
		int[] sortedArray = new int[size];
		String inStr = new String();
		for (int i = 0; i < size; i++) {
			inStr += " " + randArray[i];
			sortedArray[i] = randArray[i];
		}
		Arrays.sort(sortedArray);
		
		_test(new String[] {c + inStr}, Arrays.toString(sortedArray));
	}
	
	@Test
	public void testEmptyRadix() {
		_test(new String[] {"r"}, "[]");
	}

	@Test
	public void testSingleRadix() {
		_test(new String[] {"r 1"}, "[1]");
		_test(new String[] {"r 10000"}, "[10000]");
	}

	@Test
	public void testSmallRadix() {
		_test(new String[] {"r 1 2 3"}, "[1, 2, 3]");
		_test(new String[] {"r 3 2 1"}, "[1, 2, 3]");
		_test(new String[] {"r 1 2 3 4"}, "[1, 2, 3, 4]");
		_test(new String[] {"r 3 2 1 4"}, "[1, 2, 3, 4]");
		_test(new String[] {"r 1 2"}, "[1, 2]");
		_test(new String[] {"r 10000 9999"}, "[9999, 10000]");
		_test(new String[] {"r 9999 10000"}, "[9999, 10000]");
	}

	@Test
	public void testRandRadix() {
		testRand('r', 1000);
	}

	@Test
	public void testSizesRadix() {
		_test(new String[] {"r 1 10 100 1000 10000 100000"}, "[1, 10, 100, 1000, 10000, 100000]");
		_test(new String[] {"r 100000 10000 1000 100 10 1"}, "[1, 10, 100, 1000, 10000, 100000]");
		_test(new String[] {"r 10000 10 1 1000 100 100000"}, "[1, 10, 100, 1000, 10000, 100000]");
	}
	
	
	@Test
	public void testEmptyCounting() {
		_test(new String[] {"c"}, "[]");
	}

	@Test
	public void testSingleCounting() {
		_test(new String[] {"c 1"}, "[1]");	
		_test(new String[] {"c 10000"}, "[10000]");	
	}

	@Test
	public void testSmallCounting() {
		_test(new String[] {"c 1 2 3"}, "[1, 2, 3]");
		_test(new String[] {"c 3 2 1"}, "[1, 2, 3]");
		_test(new String[] {"c 1 2 3 4"}, "[1, 2, 3, 4]");
		_test(new String[] {"c 3 2 1 4"}, "[1, 2, 3, 4]");
		_test(new String[] {"c 1 2"}, "[1, 2]");
		_test(new String[] {"c 10000 9999"}, "[9999, 10000]");
		_test(new String[] {"c 9999 10000"}, "[9999, 10000]");
	}

	@Test
	public void testRandCounting() {
		testRand('c', 1000);
	}

	@Test
	public void testSizesCounting() {
		_test(new String[] {"c 1 10 100 1000 10000 100000"}, "[1, 10, 100, 1000, 10000, 100000]");
		_test(new String[] {"c 100000 10000 1000 100 10 1"}, "[1, 10, 100, 1000, 10000, 100000]");
		_test(new String[] {"c 10000 10 1 1000 100 100000"}, "[1, 10, 100, 1000, 10000, 100000]");
	}

}
