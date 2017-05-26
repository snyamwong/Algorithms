package edu.wit.cs.comp2350.tests;

import java.security.Permission;
import java.util.Arrays;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import edu.wit.cs.comp2350.LAB2;

public class LAB2TestCase{
	

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
	
	private void _testHeap(float[] values, float expected) {

		float result = 0; 
		try {
			result = LAB2.heapAdd(values);
		} catch (ExitException e) {}
		assertEquals(expected, result, 0);
	}

	private float[] _generateRandIntArray(int size) {
		float[] ret = new float[size];
		
		Random r = new Random();
		for (int i = 0; i < size; i++) {
			ret[i] = r.nextInt(10000000);
		}
		return ret;
	}	

	private float[] _generateRandFloatArray(int size) {
		float[] ret = new float[size];
		
		Random r = new Random();
		for (int i = 0; i < size; i++) {
			ret[i] = r.nextFloat();
		}
		return ret;
	}	

	@Test
	public void testSmall() {
		_testHeap(new float[] {1, 2}, 3);
		_testHeap(new float[] {1, 2, 3}, 6);
		_testHeap(new float[] {100, 200, 300}, 600);
		_testHeap(new float[] {(float) .1, (float) .2, (float) .3}, (float) .6);
		_testHeap(new float[] {(float) .0001, (float) .0002, (float) .0003}, (float) .0006);
		_testHeap(new float[] {1000000000, 2000000000, (float) 3000000000.0}, (float) 6000000000.0);
	}

	@Test
	public void testRandInts() {
		for (int k = 0; k < 100; k++) {
			float[] randArray = _generateRandIntArray(1000);
			float[] dup = new float[1000];
			for (int i = 0; i < 1000; i++)
				dup[i] = randArray[i];

			_testHeap(randArray, LAB2.min2ScanAdd(dup));
		}
	}
	
	@Test
	public void testRandFloats() {
		for (int k = 0; k < 100; k++) {
			float[] randArray = _generateRandFloatArray(1000);
			float[] dup = new float[1000];
			for (int i = 0; i < 1000; i++)
				dup[i] = randArray[i];

			_testHeap(randArray, LAB2.min2ScanAdd(dup));
		}
	}
	
	private void _testSizeFront(int size, float exp) {
		float[]f = new float[size];
		Arrays.fill(f,  (float) 1E-12);
		f[0] = (float) 1;
		
		_testHeap(f, exp);
	}
	
	private void _testSizeBack(int size, float exp) {
		float[]f = new float[size];
		Arrays.fill(f,  (float) 1E-12);
		f[size-1] = (float) 1;
		
		_testHeap(f, exp);
	}
	@Test
	public void testBig() {
		_testSizeFront(10000000, (float) 1.00001);
		_testSizeFront(1000000, (float) 1.000001);
		_testSizeFront(100000, (float) 1.0000001);
		_testSizeFront(10000, (float) 1);
		_testSizeFront(1000, (float) 1);
		
		_testSizeBack(10000000, (float) 1.00001);
		_testSizeBack(1000000, (float) 1.000001);
		_testSizeBack(100000, (float) 1.0000001);
		_testSizeBack(10000, (float) 1);
		_testSizeBack(1000, (float) 1);
	}


}
