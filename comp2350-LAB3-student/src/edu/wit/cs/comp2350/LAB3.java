package edu.wit.cs.comp2350;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * @author kreimendahl
 */


public class LAB3 {

	public static final String inputFile = "text/dickens.txt";
	public static final int tableSize = 256;
	
	// opens a scanner to a text file and calls the hashtable's build method
	public static void buildTable(HashTable h, String fileName) {
		
		try (Scanner s = new Scanner(new File(fileName))){
			h.buildTable(s);
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file " + inputFile + ". Exiting.");
			System.exit(0);
		}
	}

	// prints a number of words equal to the outputLength based on the trigrams stored
	// in the hashtable.
	public static void printBabble(HashTable h, int outputLength) {
		String w1, w2, w3, randW[];
		
		randW = h.getStart();	// get the first two words to start off
		w1 = randW[0];
		w2 = randW[1];
		
		System.out.printf("%s %s", w1.substring(0, 1).toUpperCase() + w1.substring(1), w2);
		
		for (int i = 2; i < outputLength; i++) {
			w3 = h.getWord(w1, w2);
			System.out.printf(" %s", w3);
			w2 = w3;
			w1 = w2;
		}
		System.out.println(".");
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the hash function to use ([b]ad, [s]imple, [r]andtable: ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the number of words you want to ouput: ");
		int outputLength = 0;
		if (s.hasNextInt())
			outputLength = s.nextInt();
		
		s.close();

		if (outputLength <= 0) {
			System.out.println("You must enter a positive number of outputs.");
			System.exit(0);
		}
		else if (outputLength >= 10000) {
			System.out.println("That's too many words. Try fewer than 10000");
			System.exit(0);
		}
		
		HashTable h = new BadTable(1);	// to ensure java that h is initialized
		
		switch (algo) {
		case 'b':
			h = new BadTable(tableSize);
			break;
		case 's':
			h = new SimpleTable(tableSize);
			break;
		case 'r':
			h = new RandTable(tableSize);
			break;
		default:
			System.out.println("Invalid hash function");
			System.exit(0);
			break;
		}

		buildTable(h, inputFile);
		printBabble(h, outputLength);

	}

}
