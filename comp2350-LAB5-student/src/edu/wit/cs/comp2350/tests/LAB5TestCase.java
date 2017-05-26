package edu.wit.cs.comp2350.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Permission;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.Speller;
import edu.wit.cs.comp2350.Trie;

import static org.junit.Assert.*;


public class LAB5TestCase{
	

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
	
	private void _testContains(Speller S, String word) {
		boolean result = false;
		try {
			result = S.contains(word);
		} catch (ExitException e) {}
		assert(result);
	}

	private void _testSuggestions(Speller S, String word, String[] expected) {
		String[] actual = new String[0];
		boolean result = false;
		try {
			actual = S.getSugg(word);
			result = S.contains(word);
		} catch (ExitException e) {}
		
		assert(!result);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++)
			assertEquals(expected[i], actual[i]);
	}

	private void populateSpeller(Speller l, String fileName) {
		
		try (Scanner s = new Scanner(new File(fileName))) {
			// loop over all input words
			while (s.hasNext()) {
				String w = s.next().toLowerCase().replaceAll("[^a-z ]","");
				l.insertWord(w);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file " + fileName + ". Exiting.");
			System.exit(0);
		}
	}
	
	@Test
	public void testSmallContains() {
		Speller T = new Trie();
		populateSpeller(T, "text/small.txt");
		
		_testContains(T, "they");
		_testContains(T, "people");
		_testContains(T, "a");
		_testContains(T, "we");
	}

	@Test
	public void testSmallSuggestions() {
		Speller T = new Trie();
		populateSpeller(T, "text/small.txt");
		
		String[] expected = {"than", "that", "them", "then", "they", "this", "what", "when"};
		_testSuggestions(T, "thet", expected);
		String[] expected2 = {"find", "like", "long"};
		_testSuggestions(T, "lint", expected2);
		String[] expected3 = {"can", "day", "for", "had", "has", "her", "may", "was", "way"};
		_testSuggestions(T, "par", expected3);
		
	}
	
	@Test
	public void test10000Contains() {
		Speller T = new Trie();
		populateSpeller(T, "text/10000.txt");
		
		_testContains(T, "level");
		_testContains(T, "accurately");
		_testContains(T, "likelihood");
		_testContains(T, "mon");
	}
	
	@Test
	public void test10000Suggestions() {
		Speller T = new Trie();
		populateSpeller(T, "text/10000.txt");
		
		String[] expected = {"cakes", "danny", "dates", "davis", "gains", "lakes", "makes", "takes"};
		_testSuggestions(T, "dakns", expected);
		String[] expected2 = {"adams", "beads", "beans", "bears", "beats", "deals", "exams", "fears", "grams", "heads", "heard", "heart", "heath", "heavy", "helps", "herbs", "jeans", "leads", "meals", "means", "reads", "seats", "seems", "teams", "tears", "terms", "years"};
		_testSuggestions(T, "heams", expected2);
		String[] expected3 = {};
		_testSuggestions(T, "bengan", expected3);
		String[] expected4 = {"alpine", "claire", "empire"};
		_testSuggestions(T, "alpire", expected4);
	}

}