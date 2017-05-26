package edu.wit.cs.comp2350;

import java.util.Scanner;

/**
 * 
 * @author kreimendahl
 */

public class LAB4 {

	// read pairs of ints from scanner
	// returns an array of DiskLocations read
	private static DiskLocation getLoc(Scanner s) {

		DiskLocation d = new DiskLocation(-1, -1);
		d.track = s.nextInt();
		if (s.hasNextInt())
			d.sector = s.nextInt();
		else {
			System.err.println("track/sector mismatch on input");
			System.exit(0);
		}

		if (d.track < 0 || d.sector < 0) {
			System.err.println("track and sector values must be non-negative");
			System.exit(0);
		}
		return d;
	}

	// prints the next/prev n items after a specific location
	// the location must be a valid location in l
	private static void printIter(LocationHolder l, Scanner s, char direction) {
		int track = -1;
		int sector = -1;
		int number = -1;
		if (s.hasNextInt())
			track = s.nextInt();
		if (s.hasNextInt())
			sector = s.nextInt();
		if (s.hasNextInt())
			number = s.nextInt();
		else {
			System.err.println("couldn't read track/sector and number");
			System.exit(0);
		}
		if (direction == 'n') {
			DiskLocation temp = l.find(new DiskLocation(track, sector));
			for (int i = 0; i < number; i++) {
				temp = l.next(temp);
				if (temp.toString().length() == 0)
					break;
				System.out.println(temp.toString());
			}
		}
		else if (direction == 'p') {
			DiskLocation temp = l.find(new DiskLocation(track, sector));
				for (int i = 0; i < number; i++) {
					temp = l.prev(temp);
					if (temp.toString().length() == 0)
						break;
					System.out.println(temp.toString());
			}			
		}
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		LocationHolder l = new BST();
		
		System.out.printf("Enter the data structure to use ([l]ist, [b]inary tree, [r]ed-black tree): ");
		char algo = s.next().charAt(0);

		switch (algo) {
		case 'l':
			l = new L();
			break;
		case 'b':
			l = new BST();
			break;
		case 'r':
			l = new RBTree();
			break;
		default:
			System.out.println("Invalid data structure");
			System.exit(0);
			break;
		}
		
		System.out.printf("Enter non-negative track/sector pairs separated by spaces.\nTerminate the list with one of the following options:\n");
		System.out.printf("Enter [n] <track> <sector> <number> to print the next \"number\" values after the location (must be valid location).\n");
		System.out.printf("Enter [p] <track> <sector> <number> to print the next \"number\" values before the location (must be valid location).\n");
		System.out.printf("Enter [h] to print the height of the data structure\n");
		System.out.printf("Enter [q] to quit\n");
		
		while (s.hasNextInt())
			l.insert(getLoc(s));
		
		char nextAction = s.next().charAt(0);
		
		switch (nextAction) {
		case 'n':
			printIter(l, s, nextAction);
			break;
		case 'p':
			printIter(l, s, nextAction);
			break;
		case 'h':
			System.out.printf("height: %d\n", l.height());
			break;
		case 'q':
			System.exit(0);
		default:
			System.out.println("Invalid action");
			System.exit(0);
			break;
		}
		s.close();
	}

}
