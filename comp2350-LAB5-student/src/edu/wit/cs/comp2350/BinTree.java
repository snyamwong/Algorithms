package edu.wit.cs.comp2350;

import java.util.ArrayList;
import java.util.TreeMap;

//implements a spell-checker class that holds all words in a balanced binary tree
public class BinTree extends Speller {
	private TreeMap<String, String> dict;
	
	public BinTree() {
		dict = new TreeMap<String, String>();
	}
	
	@Override
	public void insertWord(String s) {
		dict.put(s, s);
	}

	@Override
	public boolean contains(String s) {
		return dict.containsKey(s);
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
