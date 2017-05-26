package edu.wit.cs.comp2350;

import java.util.ArrayList;

import java.util.HashSet;
//implements a spell-checker class that holds all words in a hash table
public class Hash extends Speller {

	private HashSet<String> dict;
	
	public Hash() {
		dict = new HashSet<String>();
	}
	
	@Override
	public void insertWord(String s) {
		dict.add(s);
	}

	@Override
	public boolean contains(String s) {
		return dict.contains(s);
	}

	@Override
	public String[] getSugg(String s) {
		ArrayList<String> suggestions = new ArrayList<String>();
		int editDistance = 2;
		
		findWords(s, suggestions, 0, editDistance);
		
		return suggestions.toArray(new String[0]);
	}

	private void findWords(String s, ArrayList<String> suggestions, int strPos, int editDistance) {
		if (strPos == s.length()) {
			if (this.contains(s))
				if (!suggestions.contains(s))
				suggestions.add(s);
			return;
		}
		
		for (int i = 0; i < 26; i++) {
			if (s.charAt(strPos) == 'a' + i)
				findWords(s, suggestions, strPos + 1, editDistance);
			else if (editDistance > 0)
				findWords(s.substring(0, strPos) + (char)('a' + i) + s.substring(strPos + 1, s.length()), suggestions, strPos + 1, editDistance - 1);
		}
	}

}
