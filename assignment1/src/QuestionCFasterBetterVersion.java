// Lynn Tao
// Feb. 21, 2024
// This program finds the maximum sum of consecutive elements in an integer array (not using brute force and faster and works for 1000000 numbers!)

import java.io.*;
import java.util.*;

public class QuestionCFasterBetterVersion {

	public static void main(String[] args) {
		try {
			Scanner inFile = new Scanner (new File("input.txt"));

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

	// Description: finds the maximum sum of consecutive elements in an integer array
	// Parameters: int[] values is array of integers to find the maximum sum for
	// Return: maximum sum of consecutive elements in an integer array
	public static int findMaxSum(int[] values) {
		// accumulates sum of elements in order until the sum becomes negative 
		// this means it's better to restart sum (at 0) at the next index instead of accumulating negative sum (< 0)
		// keeps track of maximum sum among all these "split up" segments of the array

		int currentSum = 0; // keeps track of current sum of elements from last restarting point to current index
		int maxSum = values[0]; // keeps track of max consecutive sum 

		for (int i = 0; i < values.length; i++) {
			currentSum += values[i];
			maxSum = Math.max(maxSum, currentSum); // update maxSum if current sum is greater
			currentSum = Math.max(currentSum, 0); // if accumulated sum becomes negative, clear to restart at 0
		}

		return maxSum;
	}


}
