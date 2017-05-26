package edu.wit.cs.comp2350;

/* Sorts integers from command line using various algorithms 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Programming Assignment 0
 * 
 */

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * @author wongt1
 *
 */
public class LAB1 {

	public final static int MAX_INPUT = 524287;
	public final static int MIN_INPUT = 0;

	/**
	 * Counting sort
	 * @param a
	 * @return
	 */
	public static int[] countingSort(int[] a) {
		// first test if array is empty
		if (a.length == 0) {
			return a;
		}

		// first find the range of the numbers
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (max < a[i]) {
				max = a[i];
			}
		}

		// then create a new array, where it counts the numbers (also sorting
		// it)
		int[] temp = new int[max];
		int elements = 0;
		for (int i = 0; i < a.length; i++) {
			int num = a[i];
			temp[num - 1]++;
		}

		// converting the temp array into a sorted array
		int[] sortedArray = new int[a.length];
		int position = 0;

		for (int i = 0; i < temp.length; i++) {
			while (temp[i] != 0) {
				sortedArray[position] = i + 1;
				position++;
				temp[i]--;
			}
		}

		return sortedArray;
	}

	/**
	 * Radix sort (no buckets, uses count and pos array)
	 * @param a
	 * @return
	 */
	public static int[] radixSort(int[] a) {
		// first check if the array is empty
		if (a.length == 0) {
			return a;
		}

		// if not empty, find the range of the numbers
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (max < a[i]) {
				max = a[i];
			}
		}

		// remember to increase by 10 every iteration
		int bucketMod = 1;

		// temp array(s)
		int[] count = new int[10];
		int[] pos = new int[10];
		int[] output = new int[a.length];
		// position variable used for pos array
		int position = 0;

		// position and count array both work perfect!!!
		while (bucketMod <= max) {
			// loop to count the base of the number ( = number )
			for (int i = 0; i < a.length; i++) {
				// mod 10 because base 10
				if (bucketMod <= a[i]) {
					int digit = (a[i] / bucketMod) % 10;
					count[digit]++;
				}
				// this is important because if sorting numbers with different length
				// this is needed for that
				else {
					count[0]++;
				}
			}

//			System.out.println("Count array: ");
//			toArray(count);

			// for numbers < count[i]
			for (int i = 0; i < count.length - 1; i++) {
				position += count[i];
				pos[i + 1] = position;
			}

//			System.out.println("Pos array: ");
//			toArray(pos);

			// then move to the output array
			for (int i = 0; i < a.length; i++) {
				if (bucketMod > a[i]) {
					output[pos[0]] = a[i];
					pos[0] = pos[0] + 1;
				} 
				else {
					int digit = (a[i] / bucketMod) % 10;
					output[pos[digit]] = a[i];
					pos[digit] = pos[digit] + 1;
				}
			}

//			System.out.println("Output array: ");
//			toArray(output);

			// reset, increment bucketMod
			for(int i = 0; i < a.length; i++){
				a[i] = output[i];
			}
			bucketMod *= 10;
			position = 0;
			count = new int[10];
			pos = new int[10];
		}

		return output;
	}

	/**
	 * A method that prints out an array 
	 * @param a
	 */
	public static void toArray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// example sorting algorithm
	public static int[] insertionSort(int[] a) {

		for (int i = 1; i < a.length; i++) {
			int tmp = a[i];
			int j;
			for (j = i - 1; j >= 0 && tmp < a[j]; j--)
				a[j + 1] = a[j];
			a[j + 1] = tmp;
		}

		return a;
	}

	/*
	 * Implementation note: The sorting algorithm is a Dual-Pivot Quicksort by
	 * Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm
	 * offers O(n log(n)) performance on many data sets that cause other
	 * quicksorts to degrade to quadratic performance, and is typically faster
	 * than traditional (one-pivot) Quicksort implementations.
	 */
	public static int[] systemSort(int[] a) {
		Arrays.sort(a);
		return a;
	}

	// read ints from a Scanner
	// returns an array of the ints read
	private static int[] getInts(Scanner s) {
		ArrayList<Integer> a = new ArrayList<Integer>();

		while (s.hasNextInt()) {
			int i = s.nextInt();
			if ((i <= MAX_INPUT) && (i >= MIN_INPUT))
				a.add(i);
		}

		return toIntArray(a);
	}

	// copies an ArrayList to an array
	private static int[] toIntArray(ArrayList<Integer> a) {
		int[] ret = new int[a.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the sorting algorithm to use ([c]ounting, [r]adix, [i]nsertion, or [s]ystem): ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the integers that you would like sorted: ");
		int[] unsorted_values = getInts(s);
		int[] sorted_values = {};

		s.close();

		switch (algo) {
		case 'c':
			sorted_values = countingSort(unsorted_values);
			break;
		case 'r':
			sorted_values = radixSort(unsorted_values);
			break;
		case 'i':
			sorted_values = insertionSort(unsorted_values);
			break;
		case 's':
			sorted_values = systemSort(unsorted_values);
			break;
		default:
			System.out.println("Invalid sorting algorithm");
			System.exit(0);
			break;
		}

		System.out.println(Arrays.toString(sorted_values));

	}

}
