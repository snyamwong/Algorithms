package edu.wit.cs.comp2350;

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * 
 * @author kreimendahl
 */

public class LAB2 {
	//used for the heap size of the float array
	private static int heapSize;
	
	/**
	 * sorts the heap
	 * 
	 * not used at all in this program***
	 * 
	 * @param a
	 * @return
	 */
	@SuppressWarnings("unused")
	private static float[] pullup(float[] a, int index) {
		float temp;

		// WORKS
		if (a[index] < a[(index - 1) / 2]) {
			temp = a[index];
			a[index] = a[(index - 1) / 2];
			a[(index - 1) / 2] = temp;
			pullup(a, (index - 1) / 2);
		}
		
		return a;
	}

	/**
	 * Push down the entry
	 * 
	 * not used at all in this program***
	 * 
	 * 
	 * @param a
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unused")
	private static float[] pushdown(float[] a, int index) {
		for(int i = a.length/2; i <= 1; i--){
			
		}
		
		return a;
	}

	/**
	 * Given an array, turns the array into a heap
	 * @param a
	 * @return
	 */
	//WORKS 100%
	private static float[] buildHeap(float[] a){
		heapSize = a.length;
		
		for(int i = (a.length - 1) / 2; i >= 0; i--){
			minHeapify(a, i);
		}
		
		return a;
	}
	
	/**
	 * Turns the array to a min heap, index is the index of the parent's node
	 * @param a
	 * @return
	 */
	//Works!
	private static float[] minHeapify(float[] a, int index){
		//Index for left and right node of the binary tree
		int left = 2 * (index) + 1;
		int right = 2 * (index) + 2;
		int smallest = index;
		float temp;
		
		//left node < heapSize
		if(left < heapSize && a[left] < a[smallest]){
			smallest = left;
		}
		else{
			smallest = index;
		}
		
		//right node < heapSize
		if(right < heapSize && a[right] < a[smallest]){
			smallest = right;
		}
		
		if(smallest != index){
			temp = a[index];
			a[index] = a[smallest];
			a[smallest] = temp;
			minHeapify(a, smallest);
		}
		
		return a;
	}
	
	/**
	 * extracts the max from a, then re-heap a
	 * @param a
	 * @return
	 */
	//WORKS!
	private static float extractMin(float[] a){
		float min = a[0];
		a[0] = a[heapSize - 1];
		heapSize = heapSize - 1;
		a = minHeapify(a, 0);
		
//		System.out.println("Min from extractMin: " + min);
		
		return min;
	}
	
	/**
	 * Insert into heap
	 * @param a
	 */
	//Works!
	private static void insertHeap(float[] a, float key){
		heapSize = heapSize + 1;
		
		int i = heapSize - 1;
		int parent = (heapSize - 1) / 2;
		float temp;
		a[heapSize - 1] = key;
		
		while(i > 0 && a[parent] > a[i]){
			temp = a[i];
			a[i] = a[parent];
			a[parent] = temp;
			
			i = parent;
			parent = (i - 1) / 2;
			
//			System.out.println("a[parent]: " + a[parent]);
//			System.out.println("a[i]: " + a[i]);
		}
		
		//In case the insertHeap method doesn't sort the insertion node
		a = minHeapify(a, 0);
	}
	
	/**
	 * toArray method
	 * 
	 * @param a
	 * @return
	 */
	@SuppressWarnings("unused")
	private static void toArray(float[] a) {
		for (int i = 0; i < heapSize; i++) {
			System.out.println(a[i]);
		}
	}
	
	/**
	 * Extracting from the heap and summing the mins
	 * @param a
	 * @return
	 */
	public static float heapAdd(float[] a) {

		// if empty array (not needed but whatever)
		if (a.length == 0)
			return 0;

		// while a.length != 1
		// means while a has more than one entry (ends when it has only the
		// root)

		a = buildHeap(a);
		
		while (heapSize > 1) {
			float minSum;

//			System.out.println("Pre addition/insertion");
//			toArray(a);
//			System.out.println("Pre heap size: " + heapSize);
			
			// Remember: Insertion: at the end of array
			// Pull up every summation
			// Figure out which index is the min number
			// instead of a second array, use A.heapsize instead

			// summing the numbers (either left | right child)
			// extract the min number twice (since we're summing two min float's)
			minSum = extractMin(a);
//			System.out.println("min Sum1 " + minSum);
			minSum += extractMin(a);
//			System.out.println("min Sum2 " + minSum);
			
//			System.out.println("Post extraction");
//			toArray(a);
			
			// inserting the sum at the end of array
			insertHeap(a, minSum);
			
//			System.out.println("Post addition/insertion");
//			toArray(a);
//			System.out.println("Post heap size: " + heapSize);
		}
		return a[0];
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// sum an array of floats sequentially
	public static float seqAdd(float[] a) {
		float ret = 0;

		for (int i = 0; i < a.length; i++)
			ret += a[i];

		return ret;
	}

	// sort an array of floats and then sum sequentially
	public static float sortAdd(float[] a) {
		Arrays.sort(a);
		return seqAdd(a);
	}

	// scan linearly through an array for two minimum values,
	// remove them, and put their sum back in the array. repeat.
	public static float min2ScanAdd(float[] a) {
		int min1, min2;
		float tmp;

		if (a.length == 0)
			return 0;

		for (int i = 0, end = a.length; i < a.length - 1; i++, end--) {

			if (a[0] < a[1]) {
				min1 = 0;
				min2 = 1;
			} // initialize
			else {
				min1 = 1;
				min2 = 0;
			}

			for (int j = 2; j < end; j++) { // find two min indices
				if (a[min1] > a[j]) {
					min2 = min1;
					min1 = j;
				} else if (a[min2] > a[j]) {
					min2 = j;
				}
			}

			tmp = a[min1] + a[min2]; // add together
			if (min1 < min2) { // put into first slot of array
				a[min1] = tmp; // fill second slot from end of array
				a[min2] = a[end - 1];
			} else {
				a[min2] = tmp;
				a[min1] = a[end - 1];
			}
		}

		return a[0];
	}

	// read floats from a Scanner
	// returns an array of the floats read
	private static float[] getFloats(Scanner s) {
		ArrayList<Float> a = new ArrayList<Float>();

		while (s.hasNextFloat()) {
			float f = s.nextFloat();
			if (f >= 0)
				a.add(f);
		}
		return toFloatArray(a);
	}

	// copies an ArrayList to an array
	private static float[] toFloatArray(ArrayList<Float> a) {
		float[] ret = new float[a.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

//		System.out.printf("Enter the adding algorithm to use ([h]eap, [m]in2scan, se[q], [s]ort): ");
//		char algo = s.next().charAt(0);

		System.out.printf("Enter the positive floats that you would like summed: ");
		float[] values = getFloats(s);
//		float sum = 0;

		values = buildHeap(values);
		values = minHeapify(values, 0); 
		
		for(int i = 0; i < values.length; i++){
			System.out.println(values[i]);
		}
		
		s.close();

//		if (values.length == 0) {
//			System.out.println("You must enter at least one value");
//			System.exit(0);
//		} else if (values.length == 1) {
//			System.out.println("Sum is " + values[0]);
//			System.exit(0);
//
//		}

//		switch (algo) {
//		case 'h':
//			sum = heapAdd(values);
//			break;
//		case 'm':
//			sum = min2ScanAdd(values);
//			break;
//		case 'q':
//			sum = seqAdd(values);
//			break;
//		case 's':
//			sum = sortAdd(values);
//			break;
//		default:
//			System.out.println("Invalid adding algorithm");
//			System.exit(0);
//			break;
//		}
//
//		System.out.printf("Sum is %f\n", sum);
	}

}
