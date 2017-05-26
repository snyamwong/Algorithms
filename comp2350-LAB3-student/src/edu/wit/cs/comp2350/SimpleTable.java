package edu.wit.cs.comp2350;

public class SimpleTable extends HashTable {
		
	public SimpleTable(int size) {
		super(size);
	}

	@Override
	public int calculateHash(String word) {
		// TODO: implement hash from slides that uses multiplier
		int hash = 0;

		for(int i = 0; i < word.length(); i++){
			//Picked 127 as the multiplier 
			hash = ((hash * 31) + word.charAt(i)) % this.tableSize;
		}
		
		return hash;
	}

}
