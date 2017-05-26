package edu.wit.cs.comp2350;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 
 * @author kreimendahl
 */

// Finds the longest common substring in two text files
public class LAB6 {

	private static int best_cost = 0;
	private static String sol1 = "", sol2 = "";
	private static int strChar1 = 0, strChar2 = 0;

	/**
	 * This method works 99%
	 * 
	 * @param text1
	 * @param text2
	 * @return
	 */
	public static String[] FindSubstrDYN(String text1, String text2) {
		// static variables are mean
		clearStatic();

		int m = text1.length() + 1;
		int n = text2.length() + 1;

		int b[][] = new int[m][n];
		int c[][] = new int[m][n];

		// fill in [i][0] or the top row
		for (int i = 0; i < m; i++) {
			c[i][0] = 0;
			b[i][0] = 'u';
		}

		// fill in [0][i] or the top column
		for (int i = 0; i < n; i++) {
			c[0][i] = 0;
			b[0][i] = 'l';
		}

		// doubly loop to fill in table c and b
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
					c[i][j] = c[i - 1][j - 1] + 1;
					b[i][j] = 'd';
					// System.out.print(c[i][j] + " ");
					// System.out.print(b[i][j] + " ");
				} else if (c[i - 1][j] >= c[i][j - 1]) {
					c[i][j] = c[i - 1][j];
					b[i][j] = 'u';
					// System.out.print(c[i][j] + " ");
					// System.out.print(b[i][j] + " ");
				} else {
					c[i][j] = c[i][j - 1];
					b[i][j] = 'l';
					// System.out.print(c[i][j] + " ");
					// System.out.print(b[i][j] + " ");
				}
			}
			// System.out.println();
		}

//		print2D(b, m, n);
//		print2D(c, m, n);

		// Reset to the last entry of table b (rather than +1)
		m = text1.length();
		n = text2.length();

		//return new String[]{"", ""};
		return dyn_solve(b, c, text1, text2, m, n);
	}

	// @SuppressWarnings("unused")
	private static String[] dyn_solve(int[][] b, int[][] c, String x, String y, int i, int j) {
		// Fix Index error, everything else works (including table)
		// Index error mostly fixed, figure out how to fix [1][0] error or
		// [0][1] error (off by one)
		if (i == 0 && j == 0) {
//			System.out.println("i: " + i);
//			System.out.println("j: " + j);
//			System.out.println(strChar1);
//			System.out.println(strChar2);

			return new String[] { sol1, sol2 };
		} else if (i == 0) {
			dyn_solve(b, c, x, y, i, j - 1);

			sol1 += '-';
			if (!(strChar2 >= y.length())) {
				sol2 += y.charAt(strChar2);
			} else {
				sol2 += '-';
			}

			// Debugging
//			System.out.println("i: " + i);
//			System.out.println("j: " + j);
//			System.out.println(b[i][j]);
//			System.out.println("strChar1: " + strChar1 + " strChar2: " + strChar2);
//			System.out.println("Sol1 : " + sol1 + " Sol2: " + sol2);
//			System.out.println();

			strChar2++;
		} else if (j == 0) {
			dyn_solve(b, c, x, y, i - 1, j);

			if (!(strChar1 >= x.length())) {
				sol1 += x.charAt(strChar1);
			} else {
				sol1 += '-';
			}
			sol2 += '-';

			// Debugging
//			System.out.println("i: " + i);
//			System.out.println("j: " + j);
//			System.out.println(b[i][j]);
//			System.out.println("strChar1: " + strChar1 + " strChar2: " + strChar2);
//			System.out.println("Sol1 : " + sol1 + " Sol2: " + sol2);
//			System.out.println();

			strChar1++;
		} else {
			if (b[i][j] == 'd') {
				dyn_solve(b, c, x, y, i - 1, j - 1);
				if (!(strChar1 >= x.length())) {
					sol1 += x.charAt(strChar1);
				} else {
					sol1 += '-';
				}
				if (!(strChar2 >= y.length())) {
					sol2 += y.charAt(strChar2);
				} else {
					sol2 += '-';
				}

				// Debugging
//				System.out.println("i: " + i);
//				System.out.println("j: " + j);
//				System.out.println(b[i][j]);
//				System.out.println("strChar1: " + strChar1 + " strChar2: " + strChar2);
//				System.out.println("Sol1 : " + sol1 + " Sol2: " + sol2);
//				System.out.println();

				best_cost++;
				strChar1++;
				strChar2++;
			} else if (b[i][j] == 'u') {
				dyn_solve(b, c, x, y, i - 1, j);

				if (!(strChar1 >= x.length())) {
					sol1 += x.charAt(strChar1);
				} else {
					sol1 += '-';
				}
				sol2 += '-';

				// Debugging
//				System.out.println("i: " + i);
//				System.out.println("j: " + j);
//				System.out.println(b[i][j]);
//				System.out.println("strChar1: " + strChar1 + " strChar2: " + strChar2);
//				System.out.println("Sol1 : " + sol1 + " Sol2: " + sol2);
//				System.out.println();

				strChar1++;
			} else {
				dyn_solve(b, c, x, y, i, j - 1);

				sol1 += '-';
				if (!(strChar2 >= y.length())) {
					sol2 += y.charAt(strChar2);
				} else {
					sol2 += '-';
				}

				// Debugging
//				System.out.println("i: " + i);
//				System.out.println("j: " + j);
//				System.out.println(b[i][j]);
//				System.out.println("strChar1: " + strChar1 + " strChar2: " + strChar2);
//				System.out.println("Sol1 : " + sol1 + " Sol2: " + sol2);
//				System.out.println();

				strChar2++;
			}
		}

		return new String[] { sol1, sol2 };
	}

	@SuppressWarnings("unused")
	private static void print2D(int[][] b, int m, int n) {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(b[i][j] + " ");
			}
			System.out.println();
		}
	}

	private static void clearStatic() {
		best_cost = 0;
		sol1 = "";
		sol2 = "";
		strChar1 = 0;
		strChar2 = 0;
	}

	private static void dfs_solve(int i1, int i2, String s1, String s2, char[] out1, char[] out2, int score,
			int index) {

		if ((i1 >= s1.length()) && (i2 >= s2.length())) {
			if (score > best_cost) {
				out1[index] = '\0';
				out2[index] = '\0';
				best_cost = score;
				sol1 = String.valueOf(out1).substring(0, String.valueOf(out1).indexOf('\0'));
				sol2 = String.valueOf(out2).substring(0, String.valueOf(out2).indexOf('\0'));
			}
		} else if ((i1 >= s1.length()) && (i2 < s2.length())) {
			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);
		} else if ((i1 < s1.length()) && (i2 >= s2.length())) {
			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
		} else {
			if (s1.charAt(i1) == s2.charAt(i2)) {
				out1[index] = s1.charAt(i1);
				out2[index] = s2.charAt(i2);
				dfs_solve(i1 + 1, i2 + 1, s1, s2, out1, out2, score + 1, index + 1);
			}
			out1[index] = '-';
			out2[index] = s2.charAt(i2);
			dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);

			out1[index] = s1.charAt(i1);
			out2[index] = '-';
			dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
		}

	}

	public static String[] FindSubstrDFS(String text1, String text2) {
		int max_len = text1.length() + text2.length() + 1;
		char[] out1 = new char[max_len];
		char[] out2 = new char[max_len];

		dfs_solve(0, 0, text1, text2, out1, out2, 0, 0);

		String[] ret = new String[2];
		ret[0] = sol1;
		ret[1] = sol2;
		return ret;
	}

	public static int getBest() {
		return best_cost;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1, file2, text1 = "", text2 = "";
		System.out.printf(
				"Enter <text1> <text2> <algorithm>, ([dfs] - depth first search, [dyn] - dynamic programming): ");
		file1 = s.next();
		file2 = s.next();

		try {
			text1 = new String(Files.readAllBytes(Paths.get(file1)));
			text2 = new String(Files.readAllBytes(Paths.get(file2)));
		} catch (IOException e) {
			System.err.println("Cannot open files " + file1 + " and " + file2 + ". Exiting.");
			System.exit(0);
		}

		String algo = s.next();
		String[] result = { "" };

		switch (algo) {
		case "dfs":
			result = FindSubstrDFS(text1, text2);
			break;
		case "dyn":
			result = FindSubstrDYN(text1, text2);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Best string alignment:\n%s\n%s\nBest cost: %d\n", result[0], result[1], best_cost);
	}

}
