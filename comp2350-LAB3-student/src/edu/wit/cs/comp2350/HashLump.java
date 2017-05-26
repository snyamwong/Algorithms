package edu.wit.cs.comp2350;

// stores information about a single trigram for usage in a HashTable object
public class HashLump {
	public String word1;
	public String word2;
	public String word3;
	public int count;
	
	public HashLump(String w1, String w2, String w3) {
		word1 = w1;
		word2 = w2;
		word3 = w3;
		count = 1;
	}
	
	public boolean equals(String w1, String w2, String w3) {
		return w1.equals(word1) && w2.equals(word2) && w3.equals(word3);
	}

}
