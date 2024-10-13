// Lynn Tao
// Feb. 21, 2024
// This program finds the maximum sum of consecutive elements in an integer array (using brute force for no bonus if the other faster one doesn't work)

import java.io.*;
import java.util.*;

public class QuestionCNoBonus {

	public static void main(String[] args) {
		try {
			Scanner inFile = new Scanner (new File("input.txt"));; 

			// 2d array with each line and each number
			int numLines = inFile.nextInt();
			int[][] values = new int[numLines][];

			// add each value to 2d array
			for (int row = 0; row < numLines; row++) {
				int numValues = inFile.nextInt();
				values[row] = new int [numValues]; // array with size of # of elements in that row

				for (int col = 0; col < numValues; col++) { // add each value in each row to array
					values[row][col] = inFile.nextInt();
				}
			}

			// print max sum
			for (int row = 0; row < values.length; row++) {
				System.out.printf("Case #%d: %d\n", (row+1), findMaxSum(values[row]));
			}
		}


		catch (FileNotFoundException e) {
			System.out.println("File does not exist");
		}

		catch (NoSuchElementException e) {
			System.out.println("Element does not exist - incorrect number of values entered or invalid number entered?");
		}

	}

	// Description: finds the maximum sum of consecutive integers in an array
	// Parameters: int[] values is array of integers to find the maximum sum for
	// Return: maximum sum of consecutive integers
	public static int findMaxSum(int[] values) {
		int maxSum = 0; // keeps track of maximum sum of consecutive integers

		// starting index can range from first to last element of array
		for (int start = 0; start < values.length; start++) {
			int sum = 0; // keeps track of current sum

			// current index goes from starting index to end of array
			for (int cur = start; cur < values.length; cur++) {
				sum += values[cur]; // update current sum with the next value
				maxSum = Math.max(maxSum, sum); // maxSum takes the larger sum between original + current sum
			}
		}
		return maxSum;


	}

}
