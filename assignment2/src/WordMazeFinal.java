// Lynn Tao
// March 18, 2024
// This program will find if a word exists in a grid by connecting adjacent letters, either in one direction or in multiple directions

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordMazeFinal {

	// found keeps track of whether a word exists or not in the grid
	static boolean found = false;
	
	// directions...
	final static int LEFT = 1;
	final static int RIGHT = 2;
	final static int UP = 3;
	final static int DOWN = 4;
	final static int UP_RIGHT = 5;
	final static int UP_LEFT = 6;
	final static int DOWN_RIGHT = 7;
	final static int DOWN_LEFT = 8;
	final static int NONE = 0;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner (System.in);
		Scanner inFile = new Scanner (new File ("maze.txt"));

		// get maze option
		System.out.print("Do you want maze option 1 or 2? ");
		int option = Integer.parseInt(in.nextLine());
		in.close();
		
		// get number of grids
		int grids = Integer.parseInt(inFile.nextLine());

		// read each grid
		for (int gridNo = 1; gridNo <= grids; gridNo++) {
			int size = Integer.parseInt(inFile.nextLine());
			
			// maze stores the maze, but the first + last rows and columns are empty (helps with searching without going out of bounds)
			char[][] maze = new char[size+2][size+2];

			// store characters starting from index 1 and up to second last index
			for (int i = 1; i < size+1; i++) {
				String line = inFile.nextLine();
				for (int j = 1; j < size+1; j++) {
					maze[i][j] = line.charAt(j-1);
				}
			}

			// get number of words to find
			int words = Integer.parseInt(inFile.nextLine());

			// print the grid
			System.out.println("\nGrid #" + gridNo + ":");
			printMaze(maze);
			System.out.println();

			// maze option 1
			if (option == 1) {
				for (int i = 0; i < words; i++) {
					found = false;
					String word = inFile.nextLine();

					// start by searching for first letter of word going through the entire array
					for (int row = 1; row < size+1; row++) {
						for (int col = 1; col < size+1; col++) {
							wordMaze1(maze, row, col, word, 0);

							if (found) {
								break;
							}
						}
						if (found) {
							break;
						}
					}

					if(found) {
						System.out.println(word + " is found.");
					}
					else {
						System.out.println(word + " is NOT found.");
					}
					
					// change maze to uppercase (to search for next word)
					mazeUppercase(maze);
				}

			}

			else if (option == 2) {
				for (int i = 0; i < words; i++) {
					found = false;
					String word = inFile.nextLine();

					// start by searching for first letter of word going through the entire array
					for (int row = 1; row < size+1; row++) {
						for (int col = 1; col < size+1; col++) {
							wordMaze2(maze, row, col, word, 0, NONE);

							if (found) {
								break;
							}
						}
						if (found) {
							break;
						}
					}

					if(found) {
						System.out.println(word + " is found.");
					}
					else {
						System.out.println(word + " is NOT found.");
					}
					
					// change maze to uppercase (to search for next word)
					mazeUppercase(maze);
					
				}

			}

		}
		inFile.close();

	}

	// Description: checks if a word is found in the maze in any direction (can be not in straight line)
	// Parameters: char[][] arr is array of letters in grid, int row is the current row that it is searching through, 
				// int col is current column that it is searching through, String word is the word to be looked for, 
				// int index is the current index in the word that it is looking for
	// Return: void, but changes the global variable "found" to true if it is found
	public static void wordMaze1(char[][] arr, int row, int col, String word, int index) {
		// stop searching if whole word is found
		if (found) {
			return;
		}

		// end of path if current index is past the last character in word
		if (index > word.length()-1) {
			found = true;
			return;
		}

		// if character at current index is found in the grid
		if (word.charAt(index) == arr[row][col]) {
			// change to lowercase
			arr[row][col] = Character.toLowerCase(arr[row][col]);

			// check all other directions for the next character in word
			wordMaze1(arr, row, col+1, word, index+1);
			wordMaze1(arr, row+1, col, word, index+1);
			wordMaze1(arr, row-1, col, word, index+1);
			wordMaze1(arr, row, col-1, word, index+1);
			wordMaze1(arr, row-1, col-1, word, index+1);
			wordMaze1(arr, row-1, col+1, word, index+1);
			wordMaze1(arr, row+1, col-1, word, index+1);
			wordMaze1(arr, row+1, col+1, word, index+1);

			// if none of these options work, change current character in grid back to uppercase (incorrect letter chosen)
			if (!found) {
				arr[row][col] = Character.toUpperCase(arr[row][col]);
			}
		}

	}

	// Description: checks if a word is found in the maze in any direction (must be in straight line)
	// Parameters: char[][] arr is array of letters in grid, int row is the current row that it is searching through, 
				// int col is current column that it is searching through, String word is the word to be looked for, 
				// int index is the current index in the word that it is looking for
				// int direction is the direction that word is going in
	// Return: void, but changes the global variable "found" to true if it is found
	public static void wordMaze2(char[][] arr, int row, int col, String word, int index, int direction) {
		// stop searching if whole word is found
		if (found) {
			return;
		}

		// end of path if current index is past the last character in word
		if (index > word.length()-1) {
			found = true;
			return;
		}

		// if character at current index is found in the grid
		if (word.charAt(index) == arr[row][col]) {
			arr[row][col] = Character.toLowerCase(arr[row][col]);


			// check all possible directions if word does not already have a direction
			if (direction == NONE) {
				wordMaze2(arr, row, col+1, word, index+1, RIGHT);
				wordMaze2(arr, row, col-1, word, index+1, LEFT);
				wordMaze2(arr, row-1, col, word, index+1, UP);
				wordMaze2(arr, row+1, col, word, index+1, DOWN);
				wordMaze2(arr, row-1, col-1, word, index+1, UP_LEFT);
				wordMaze2(arr, row-1, col+1, word, index+1, UP_RIGHT);
				wordMaze2(arr, row+1, col-1, word, index+1, DOWN_LEFT);
				wordMaze2(arr, row+1, col+1, word, index+1, DOWN_RIGHT);
			}

			// continue checking in that direction if word already has a direction

			else if (direction == RIGHT) {
				wordMaze2(arr, row, col+1, word, index+1, direction);
			}

			else if (direction == LEFT) {
				wordMaze2(arr, row, col-1, word, index+1, direction);
			}

			else if (direction == UP) {
				wordMaze2(arr, row-1, col, word, index+1, direction);
			}

			else if (direction == DOWN) {
				wordMaze2(arr, row+1, col, word, index+1, direction);
			}

			else if (direction == UP_RIGHT) {
				wordMaze2(arr, row-1, col+1, word, index+1, direction);
			}

			else if (direction == UP_LEFT) {
				wordMaze2(arr, row-1, col-1, word, index+1, direction);
			}

			else if (direction == DOWN_RIGHT) {
				wordMaze2(arr, row+1, col+1, word, index+1, direction);
			}

			else if (direction == DOWN_LEFT) {
				wordMaze2(arr, row+1, col-1, word, index+1, direction);
			}

			// if none of these options work, change current character in grid back to uppercase (incorrect letter chosen)
			if (!found) {
				arr[row][col] = Character.toUpperCase(arr[row][col]);
				return;
			}
		}

	}
	
	// Description: makes all elements in the maze (2d array) uppercase
	// Parameters: char[][] maze is 2d array of elements of maze, with first and last rows & columns empty
	// Return: void, but changes maze into uppercase
	public static void mazeUppercase(char[][] maze) {
		for (int k = 1; k < maze.length-1; k++) {
			for (int j = 1; j < maze.length-1; j++) {
				maze[k][j] = Character.toUpperCase(maze[k][j]);
			}
		}
	}

	// Description: prints a 2d array in rows
	// Parameters: char[][] maze is 2d array of elements of maze, with first and last rows & columns empty
	// Return: void, but prints the maze with each row as a new line (no spaces in between)
	public static void printMaze(char[][] maze) {
		for (int k = 1; k < maze.length-1; k++) {
			for (int j = 1; j < maze.length-1; j++) {
				System.out.print(maze[k][j]);
			}
			System.out.println();
		}
	}


}
