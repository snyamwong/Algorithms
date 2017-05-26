package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

//HashTable implements a hashtable of word trigrams with counts
public abstract class HashTable {
	
	private ArrayList<HashLump>[] table;	// holds all of the word information
	protected int tableSize;
	
	// implemented in inheriting class
	public abstract int calculateHash(String word);
	
	// initialize hashtable's buckets (which are implemented as ArrayLists)
	@SuppressWarnings("unchecked")
	public HashTable(int size) {
		if (size < 0)
			size = 1;
		table = (ArrayList<HashLump>[])new ArrayList[size];
		for (int i = 0; i < size; i++) {
			table[i] = new ArrayList<HashLump>();
		}
		tableSize = size;
	}
	
	// loops through all words retrieved from scanner, strips them of non-characters,
	// and inserts them as trigrams into the hashtable
	public void buildTable(Scanner s) {
		String w1, w2, w3;
		
		//initializing for the first three words on the input
		if (s.hasNext())
			w1 = s.next().toLowerCase().replaceAll("[^a-z]","");
		else
			return;
		if (s.hasNext())
			w2 = s.next().toLowerCase().replaceAll("[^a-z]","");
		else
			return;
		if (s.hasNext())
			w3 = s.next().toLowerCase().replaceAll("[^a-z]","");
		else
			return;
		
		insertWord(w1, w2, w3);
		
		// loop over all input words
		while (s.hasNextLine()) {
			String[] line = s.nextLine().toLowerCase().replaceAll("-", " ").replaceAll("[^a-z ]","").split(" ");
			
			for (int i = 0; i < line.length; i++) {
				if (line[i].length() > 0) {
					w1 = w2; w2 = w3; w3 = line[i];
					insertWord(w1, w2, w3);
				}
			}
		}
	}
	
	// either inserts a trigram into the table or increments its count by 1 if it was already there 
	public void insertWord(String word1, String word2, String word3) {
		
		int i = calculateHash(word1 + " " + word2);
		
		boolean found = false;
		for (HashLump l: table[i]) {
			if (l.equals(word1, word2, word3)) {
				l.count++;
				found = true;
				break;
			}
		}
		if (!found)
			table[i].add(new HashLump(word1,word2,word3));
	}
	
	// gets a random next word based on the hashes of word1 and word2
	public String getWord(String word1, String word2) {
		String ret = "";
		int i = calculateHash(word1 + " " + word2);
		int tot = 0;
		
		for (HashLump l: table[i])
			tot += l.count;
		
		int randomNum = ThreadLocalRandom.current().nextInt(0, tot + 1);
		
		int accum = 0;
		for (HashLump l: table[i]) {
			if (accum + l.count >= randomNum) {
				ret = l.word3;
				break;
			}
			accum += l.count;
		}
		
		return ret;
	}
	
	// Looks in a random hash bucket to get the first two words to start the babble.
	public String[] getStart() {
		String[] ret = new String[2];
		
		int r, s;
		do {
			r = ThreadLocalRandom.current().nextInt(0, tableSize);
		} while (table[r].size() == 0);
		
		s = ThreadLocalRandom.current().nextInt(0, table[r].size());
		
		ret[0] = table[r].get(s).word1;
		ret[1] = table[r].get(s).word2;
		return ret;
	}
	
	public String toString() {
		String ret = "";
		for (int i = 0; i < tableSize; i++) {
			for (HashLump l: table[i]) {
				ret += String.format("%s %s: %s %d\n", l.word1, l.word2, l.word3, l.count);
			}
		}
		return ret;
	}
	
	// generates an array of bucket sizes in the hashtable 
	public int[] getStats() {
		int[] ret = new int[tableSize];
		for (int i = 0; i < tableSize; i++) {
			ret[i] = table[i].size();
		}
		return ret;
	}
}
