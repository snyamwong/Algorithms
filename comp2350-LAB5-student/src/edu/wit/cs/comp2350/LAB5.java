package edu.wit.cs.comp2350;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * @author kreimendahl
 */

// Builds a dictionary from a hard-coded file location and checks for a word in the dictionary.
// Supplies edit suggestions if the word is not found
public class LAB5 {

	private static void populateSpeller(Speller l, String fileName) {
		
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

	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Speller l = new Linear();
		
		System.out.printf("Enter the data structure to use ([l]ist, [b]inary tree, [h]ashtable, [t]rie): ");
		char algo = s.next().charAt(0);

		switch (algo) {
		case 'l':
			l = new Linear();
			break;
		case 'h':
			l = new Hash();
			break;
		case 'b':
			l = new BinTree();
			break;
		case 't':
			l = new Trie();
			break;
		default:
			System.out.println("Invalid data structure");
			System.exit(0);
			break;
		}
		
		populateSpeller(l, "text/10000.txt");
		
		System.out.printf("Enter a word to spell check: ");
		
		String inString = "";
		if (s.hasNext())
			inString = s.next();
		
		s.close();
		
		if (l.contains(inString))
			System.out.println(inString + " is in the dictionary!");
		else {
			String[] results = l.getSugg(inString);
			if (results.length == 0)
				System.out.println("No results");
			else
				for (String str: results)
					System.out.println(str);
		}
		
		
	}

}
