package edu.wit.cs.comp2350;

import java.util.ArrayList;

public class Trie extends Speller {
	//Root with no particular wordCharacter (uses ' ')
	private TrieNode root = new TrieNode();

	@Override
	public void insertWord(String s) {
		// TODO Implement this method
		root.insertWord(s, root);
	}

	@Override
	public boolean contains(String s) {
		// TODO Implement this method
		return root.lookWord(s, root);
	}

	/**
	 * Copied from BinTree.class/Hash.class
	 */
	@Override
	public String[] getSugg(String s) {
		// TODO Implement this method
		ArrayList<String> suggestions = new ArrayList<String>();
		int editDistance = 2;

		root.findWords(s, suggestions, 0, editDistance, root);

		return suggestions.toArray(new String[0]);
	}

	/**
	 * Inner class for class Node used in Trie
	 * @author wongt1
	 *
	 */
	private class TrieNode {
		private TrieNode[] wordArray;
		private boolean wordFound;
		@SuppressWarnings("unused")
		private char wordCharacter;

		/**
		 * Used for the root only
		 */
		private TrieNode() {
			this(' ');
		}

		private TrieNode(char wc) {
			this.wordArray = new TrieNode[26];
			this.wordFound = false;
			this.wordCharacter = wc;
		}

		private void insertWord(String s, TrieNode n) {
			// ASCII Value convert
			int index = s.charAt(0) - 'a';

//			System.out.println("Insert word: " + s);

			// If the wordArray is null means it's path has not been created
			// Create path here
			if (n.wordArray[index] == null) {
				// System.out.println("NULL");
				n.wordArray[index] = new TrieNode(s.charAt(0));
			}

			// If the String is bigger than 1
			if (s.length() > 1) {
				n.wordArray[index].insertWord(s.substring(1), n.wordArray[index]);
			}
			// Eventually the String will be reduced to an empty String
			else {
				n.wordArray[index].wordFound = true;
//				System.out.println(n.wordArray[index].wordFound);
			}
		}

		private boolean lookWord(String s, TrieNode n) {
			int index = 0;
			
			//Recursion is too hard ahh
			while(s.length() > 1){
				index = s.charAt(0) - 'a';
				
//				System.out.println("Look Word: " + s);
				
				// If it's null means no path, therefore does not contain
				if (n.wordArray[index] == null) {
					return false;
				}
				
				else if (s.length() > 1) {
					s = s.substring(1);
				}
				
				n = n.wordArray[index];
			}
			
			index = s.charAt(0) - 'a';
			//Gotta check for NullPointer again 
			if(n.wordArray[index] == null){
				return false;
			}
			return n.wordArray[index].wordFound;
		}

		/**
		 * Also copied from BinTree.java/Hash.java
		 * @param s
		 * @param suggestions
		 * @param strPos
		 * @param editDistance
		 * @param n
		 */
		private void findWords(String s, ArrayList<String> suggestions, int strPos, int editDistance, TrieNode n) {
			if (strPos == s.length()) {
				if (n.lookWord(s, n))
					if (!suggestions.contains(s))
						suggestions.add(s);
				return;
			}

			for (int i = 0; i < 26; i++) {
				if (s.charAt(strPos) == 'a' + i)
					findWords(s, suggestions, strPos + 1, editDistance, n);
				else if (editDistance > 0)
					findWords(s.substring(0, strPos) + (char) ('a' + i) + s.substring(strPos + 1, s.length()), suggestions,
							strPos + 1, editDistance - 1, n);
			}
		}
		
		@SuppressWarnings("unused")
		/**
		 * Used for debugging, not really useful tbh
		 * @param n
		 */
		public void printNode(TrieNode n) {
			String array = "";

			for (int i = 0; i < n.wordArray.length; i++) {
				if (wordArray[i] != null) {
					array += "Word Found for " + i + " " + n.wordArray[i].wordFound + "\n";
				}
			}

			System.out.println(array);
		}
	}

//	public static void main(String[] args) {
//		Trie trie = new Trie();
//
//		trie.insertWord("same");
//		System.out.println(trie.contains("same"));
//	}
}
