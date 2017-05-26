package edu.wit.cs.comp2350;

// provides an interface for a spell checker
public abstract class Speller {
	
	public abstract void insertWord(String s);
	public abstract boolean contains(String s);
	public abstract String[] getSugg(String s);
	
}
