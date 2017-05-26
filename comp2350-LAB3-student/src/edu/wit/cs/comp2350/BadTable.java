package edu.wit.cs.comp2350;

public class BadTable extends HashTable {

	public BadTable(int size) {
		super(size);
	}

	@Override
	public int calculateHash(String word) {
		return word.charAt(0) % tableSize;
	}

}
