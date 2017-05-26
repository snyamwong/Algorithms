package edu.wit.cs.comp2350.tests;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import edu.wit.cs.comp2350.DiskLocation;
import edu.wit.cs.comp2350.LocationHolder;
import edu.wit.cs.comp2350.RBTree;

public class LAB4TestCase{
	

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
	
	private void _testInsert(LocationHolder T, DiskLocation[] vals) {
		try {
			for (int i = 0; i < vals.length; i++) {
				T.insert(vals[i]);
			}
		} catch (ExitException e) {}
	}

	private void _testNext(LocationHolder T, DiskLocation target, DiskLocation expected) {
		DiskLocation actual = new DiskLocation(-1, -1);
		try {
			actual = T.next(target);
		} catch (ExitException e) {}
		assertEquals(expected.track, actual.track);
		assertEquals(expected.sector, actual.sector);
	}
	
	private void _testPrev(LocationHolder T, DiskLocation target, DiskLocation expected) {
		DiskLocation actual = new DiskLocation(-1, -1);
		try {
			actual = T.prev(target);
		} catch (ExitException e) {}
		assertEquals(expected.track, actual.track);
		assertEquals(expected.sector, actual.sector);
	}
	
	private void _testHeight(LocationHolder T, int max) {
		int actual = -1;
		try {
			actual = T.height();
		} catch (ExitException e) {}
		assertTrue(max >= actual);
	}

	@Test
	public void testInsertSmalRBTree() {
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 2)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(1, 3)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 1), new DiskLocation(1, 2), new DiskLocation(1, 3)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 1), new DiskLocation(1, 2)});
	}

	@Test
	public void testInsertMedium() {
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 1), new DiskLocation(1, 2), new DiskLocation(1, 4)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 3), new DiskLocation(1, 1), new DiskLocation(1, 2), new DiskLocation(1, 5), new DiskLocation(1, 4)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(1, 2), new DiskLocation(2, 1), new DiskLocation(2, 3), new DiskLocation(1, 4), new DiskLocation(1, 1), new DiskLocation(2, 4)});
		_testInsert(new RBTree(), new DiskLocation[] {new DiskLocation(2, 3), new DiskLocation(1, 3), new DiskLocation(2, 2), new DiskLocation(1, 2), new DiskLocation(1, 1), new DiskLocation(2, 1)});
	}
	
	@Test
	public void testInsertSeq() {
		for (int i = 1; i < 10000; i*=10) {
			DiskLocation[] d = new DiskLocation[i];
			for (int j = 0; j < i; j++)
				d[j] = new DiskLocation(1, j);
			_testInsert(new RBTree(), d);
		}
	}
	
	@Test
	public void testInsertRand() {

		for (int track = 10; track <= 100; track += 10) {
			for (int sector = 10; sector <= 100; sector += 10) {
				ArrayList<DiskLocation> arrD = new ArrayList<DiskLocation>(track*sector);
				for (int i = 0; i < track; i++) {
					for (int j = 0; j < sector; j++) 
						arrD.add(new DiskLocation(i, j));
				}
				DiskLocation[] d = new DiskLocation[track*sector];
				Collections.shuffle(arrD);		// randomize array
				_testInsert(new RBTree(), arrD.toArray(d));
			}
		}
	}

	@Test
	public void testHeightSmall() {
		RBTree T = new RBTree();
		
		for (int i = 0; i < 1; i++)
			T.insert(new DiskLocation(i, 1));
		_testHeight(T, 1);
		
		for (int i = 1; i < 3; i++)
			T.insert(new DiskLocation(i, 1));
		_testHeight(T, 3);
		
		for (int i = 3; i < 7; i++)
			T.insert(new DiskLocation(i, 1));
		_testHeight(T, 8);
	}
	
	@Test
	public void testHeightLarge() {
		RBTree T = new RBTree();
		
		for (int i = 0; i < 100; i++)
			T.insert(new DiskLocation(i, 1));
		_testHeight(T, 14);
		
		for (int i = 100; i < 1000; i++)
			T.insert(new DiskLocation(i, 1));
		_testHeight(T, 20);
	}
	
	@Test
	public void testNextSmall() {
		RBTree T = new RBTree();
		
		for (int i = 0; i < 3; i++)
			T.insert(new DiskLocation(i, 1));
		_testNext(T, T.find(new DiskLocation(0, 1)), new DiskLocation(1, 1));
		_testNext(T, T.find(new DiskLocation(1, 1)), new DiskLocation(2, 1));
		
		for (int i = 3; i < 7; i++)
			T.insert(new DiskLocation(i, 1));
		_testNext(T, T.find(new DiskLocation(1, 1)), new DiskLocation(2, 1));
		_testNext(T, T.find(new DiskLocation(4, 1)), new DiskLocation(5, 1));
	}
	
	@Test
	public void testNextLarge() {
		RBTree T = new RBTree();
		
		for (int i = 0; i < 300; i++)
			T.insert(new DiskLocation(i, 1));
		_testNext(T, T.find(new DiskLocation(91, 1)), new DiskLocation(92, 1));
		_testNext(T, T.find(new DiskLocation(140, 1)), new DiskLocation(141, 1));
		
		for (int i = 300; i < 700; i++)
			T.insert(new DiskLocation(i, 1));
		_testNext(T, T.find(new DiskLocation(694, 1)), new DiskLocation(695, 1));
		_testNext(T, T.find(new DiskLocation(51, 1)), new DiskLocation(52, 1));
	}
	
	
	@Test
	public void testPrevSmall() {
		RBTree T = new RBTree();
		
		for (int i = 0; i < 3; i++)
			T.insert(new DiskLocation(i, 1));
		_testPrev(T, T.find(new DiskLocation(1, 1)), new DiskLocation(0, 1));
		_testPrev(T, T.find(new DiskLocation(2, 1)), new DiskLocation(1, 1));
		
		for (int i = 3; i < 7; i++)
			T.insert(new DiskLocation(i, 1));
		_testPrev(T, T.find(new DiskLocation(2, 1)), new DiskLocation(1, 1));
		_testPrev(T, T.find(new DiskLocation(5, 1)), new DiskLocation(4, 1));
	}
	
	@Test
	public void testPrevLarge() {
		RBTree T = new RBTree();
		
		for (int i = 0; i < 300; i++)
			T.insert(new DiskLocation(i, 1));
		_testPrev(T, T.find(new DiskLocation(92, 1)), new DiskLocation(91, 1));
		_testPrev(T, T.find(new DiskLocation(141, 1)), new DiskLocation(140, 1));
		
		for (int i = 300; i < 700; i++)
			T.insert(new DiskLocation(i, 1));
		_testPrev(T, T.find(new DiskLocation(696, 1)), new DiskLocation(695, 1));
		_testPrev(T, T.find(new DiskLocation(53, 1)), new DiskLocation(52, 1));
	}
}
