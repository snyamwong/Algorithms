package edu.wit.cs.comp2350;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * @author kreimendahl
 */

// Provides a solution to the 0-1 knapsack problem
public class LAB7 {

	public static class Item {
		public int weight;
		public int value;
		public int index;

		public Item(int w, int v, int i) {
			weight = w;
			value = v;
			index = i;
		}

		public String toString() {
			return "(" + weight + "#, $" + value + ")";
		}
	}

	// set by calls to Find* methods
	private static int best_value = 0;

	/**
	 * Table dimensions are working
	 * 
	 * @param table
	 * @param weight
	 * @return
	 */
	public static Item[] FindDynamic(Item[] table, int weight) {
		best_value = 0;

		int numOfItems = table.length + 1;
		int weightSize = weight + 1;
		int[][] knapsack = new int[numOfItems][weightSize];

		// Creating the Knapsack table here
		// i starts at one because the first row is always all 0's
		for (int i = 0; i < numOfItems; i++) {
			for (int j = 0; j < weightSize; j++) {
				if (i == 0 || j == 0) {
					knapsack[i][j] = 0;
				} else if (table[i - 1].weight <= j) {
					knapsack[i][j] = Integer.max(table[i - 1].value + knapsack[i - 1][j - table[i - 1].weight],
							knapsack[i - 1][j]);
				} else {
					knapsack[i][j] = knapsack[i - 1][j];
				}
			}
		}

		best_value = knapsack[table.length][weight];

		ArrayList<Item> includedItems = new ArrayList<>();

		toArray(knapsack, numOfItems, weightSize);

		includedItems = dynamic_solve(knapsack, table, numOfItems - 1, weightSize - 1, includedItems);

		Item[] includedItemsArray = new Item[includedItems.size()];
		for (int i = 0; i < includedItems.size(); i++) {
			includedItemsArray[i] = includedItems.get(i);
		}

		return includedItemsArray;
	}

	/**
	 * @param knapsack
	 * @param Table
	 * @param i
	 * @param j
	 * @param includedItems
	 * @param prev
	 * @return
	 */
	public static ArrayList<Item> dynamic_solve(int[][] knapsack, Item[] Table, int numOfItems, int weightSize,
			ArrayList<Item> includedItems) {
		if (numOfItems == 0 || weightSize == 0) {

			// I HATE DEBUGGING
//			System.out.println("END");
//			System.out.println("numOfItems: " + numOfItems);
//			System.out.println("weightSize: " + weightSize);
//			System.out.println();
			
			return includedItems;
			
		} else if (knapsack[numOfItems][weightSize] == knapsack[numOfItems - 1][weightSize]) {
			
			dynamic_solve(knapsack, Table, numOfItems - 1, weightSize, includedItems);

			// I HATE DEBUGGING
//			System.out.println("NOT ADDING ITEM");
//			System.out.println(knapsack[numOfItems][weightSize]);
//			System.out.println(knapsack[numOfItems - 1][weightSize]);
//			System.out.println("numOfItems: " + numOfItems);
//			System.out.println("weightSize: " + weightSize);
//			System.out.println();
			
		} else {
//			System.out.println(knapsack[numOfItems][weightSize]);
			dynamic_solve(knapsack, Table, numOfItems - 1, weightSize - Table[numOfItems - 1].weight, includedItems);
			includedItems.add(Table[numOfItems - 1]);

			// I HATE DEBUGGING
//			System.out.println("ADDING ITEM");
//			System.out.println(knapsack[numOfItems][weightSize]);
//			System.out.println(knapsack[numOfItems - 1][weightSize]);
//			System.out.println("numOfItems: " + numOfItems);
//			System.out.println("weightSize: " + weightSize);
//			System.out.println();
		}
		return includedItems;
	}

	/**
	 * A plaine old toArray method
	 * 
	 * @param a
	 * @param n
	 * @param w
	 */
	public static void toArray(int[][] a, int n, int w) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < w; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
	}

	// enumerates all subsets of items to find maximum value that fits in
	// knapsack
	public static Item[] FindEnumerate(Item[] table, int weight) {

		if (table.length > 63) { // bitshift fails for larger sizes
			System.err.println("Problem size too large. Exiting");
			System.exit(0);
		}

		int nCr = 1 << table.length; // bitmask for included items
		int bestSum = -1;
		boolean[] bestUsed = {};
		boolean[] used = new boolean[table.length];

		for (int i = 0; i < nCr; i++) { // test all combinations
			int temp = i;

			for (int j = 0; j < table.length; j++) {
				used[j] = (temp % 2 == 1);
				temp = temp >> 1;
			}

			if (TotalWeight(table, used) <= weight) {
				if (TotalValue(table, used) > bestSum) {
					bestUsed = Arrays.copyOf(used, used.length);
					bestSum = TotalValue(table, used);
				}
			}
		}

		int itemCount = 0; // count number of items in best result
		for (int i = 0; i < bestUsed.length; i++)
			if (bestUsed[i])
				itemCount++;

		Item[] ret = new Item[itemCount];
		int retIndex = 0;

		for (int i = 0; i < bestUsed.length; i++) { // construct item list
			if (bestUsed[i]) {
				ret[retIndex] = table[i];
				retIndex++;
			}
		}
		best_value = bestSum;
		return ret;

	}

	// returns total value of all items that are marked true in used array
	private static int TotalValue(Item[] table, boolean[] used) {
		int ret = 0;
		for (int i = 0; i < table.length; i++)
			if (used[i])
				ret += table[i].value;

		return ret;
	}

	// returns total weight of all items that are marked true in used array
	private static int TotalWeight(Item[] table, boolean[] used) {
		int ret = 0;
		for (int i = 0; i < table.length; i++) {
			if (used[i])
				ret += table[i].weight;
		}

		return ret;
	}

	// adds items to the knapsack by picking the next item with the highest
	// value:weight ratio. This could use a max-heap of ratios to run faster,
	// but
	// it runs in n^2 time wrt items because it has to scan every item each time
	// an item is added
	public static Item[] FindGreedy(Item[] table, int weight) {
		boolean[] used = new boolean[table.length];
		int itemCount = 0;

		while (weight > 0) { // while the knapsack has space
			int bestIndex = GetGreedyBest(table, used, weight);
			if (bestIndex < 0)
				break;
			weight -= table[bestIndex].weight;
			best_value += table[bestIndex].value;
			used[bestIndex] = true;
			itemCount++;
		}

		Item[] ret = new Item[itemCount];
		int retIndex = 0;

		for (int i = 0; i < used.length; i++) { // construct item list
			if (used[i]) {
				ret[retIndex] = table[i];
				retIndex++;
			}
		}

		return ret;
	}

	// finds the available item with the best value:weight ratio that fits in
	// the knapsack
	private static int GetGreedyBest(Item[] table, boolean[] used, int weight) {

		double bestVal = -1;
		int bestIndex = -1;
		for (int i = 0; i < table.length; i++) {
			double ratio = (table[i].value * 1.0) / table[i].weight;
			if (!used[i] && (ratio > bestVal) && (weight >= table[i].weight)) {
				bestVal = ratio;
				bestIndex = i;
			}
		}

		return bestIndex;
	}

	public static int getBest() {
		return best_value;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;
		int weight = 0;
		System.out.printf(
				"Enter <objects file> <knapsack weight> <algorithm>, ([d]ynamic programming, [e]numerate, [g]reedy).\n");
		System.out.printf("(e.g: objects/small 10 g)\n");
		file1 = s.next();
		weight = s.nextInt();

		ArrayList<Item> tableList = new ArrayList<Item>();

		try (Scanner f = new Scanner(new File(file1))) {
			int i = 0;
			while (f.hasNextInt())
				tableList.add(new Item(f.nextInt(), f.nextInt(), i++));
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}

		Item[] table = new Item[tableList.size()];
		for (int i = 0; i < tableList.size(); i++)
			table[i] = tableList.get(i);

		String algo = s.next();
		Item[] result = {};

		switch (algo.charAt(0)) {
		case 'd':
			result = FindDynamic(table, weight);
			break;
		case 'e':
			result = FindEnumerate(table, weight);
			break;
		case 'g':
			result = FindGreedy(table, weight);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Index of included items: ");
		for (int i = 0; i < result.length; i++)
			System.out.printf("%d ", result[i].index);
		System.out.printf("\nBest value: %d\n", best_value);
	}

}
