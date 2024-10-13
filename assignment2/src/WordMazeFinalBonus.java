// Lynn Tao
// March 18, 2024
// This program will output the number of points that all the words in a maze can give, using a dictionary of valid words. 
// (i actually use this program to find the words and cheat against jessica in word hunt (imessage game)!!!!)

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class WordMazeFinalBonus {

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

	public static void main(String[] args) throws IOException {
		Scanner inFile = new Scanner (new File ("maze2.txt"));
		BufferedReader br = new BufferedReader (new FileReader("wordlist.txt"));

		String[] wordList = new String[80335];

		for (int i = 0; i < 80335; i++) {
			wordList[i] = br.readLine();
		}
		br.close();

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

			// print the grid
			System.out.println("Grid #" + gridNo + ":");
			printMaze(maze);
			System.out.println();
			
			int points = 0;
			int words = 0;
			
			for (int i = 0; i < wordList.length; i++) {
				found = false;
				String word = wordList[i].toUpperCase();

				// start by searching for first letter of word going through the entire array
				for (int row = 1; row < size+1; row++) {
					for (int col = 1; col < size+1; col++) {
						wordMaze1Bonus(maze, row, col, word, 0);

						if (found) {
							break;
						}
					}
					
					if (found) {
						break;
					}
				}

				// update number of words and points if word is found
				if(found) {
					System.out.println(word);
					words++;
					points += countPoints(word);
				}

				// change maze to uppercase (to search for next word)
				mazeUppercase(maze);
			}
			
			// print total words + points
			System.out.println("Total words: " + words);
			System.out.println("Total points: " + points);
			System.out.println();
		}

		inFile.close();

	}	
	
	// Description: checks if a word is found in the maze in any direction (can be not in straight line)
	// Parameters: char[][] arr is array of letters in grid, int row is the current row that it is searching through, 
				// int col is current column that it is searching through, String word is the word to be looked for, 
				// int index is the current index in the word that it is looking for
	// Return: void, but changes the global variable "found" to true if it is found
	public static void wordMaze1Bonus(char[][] arr, int row, int col, String word, int index) {
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

			wordMaze1Bonus(arr, row, col+1, word, index+1);
			wordMaze1Bonus(arr, row+1, col, word, index+1);
			wordMaze1Bonus(arr, row-1, col, word, index+1);
			wordMaze1Bonus(arr, row, col-1, word, index+1);
			wordMaze1Bonus(arr, row-1, col-1, word, index+1);
			wordMaze1Bonus(arr, row-1, col+1, word, index+1);
			wordMaze1Bonus(arr, row+1, col-1, word, index+1);
			wordMaze1Bonus(arr, row+1, col+1, word, index+1);

			// if none of these options work, change current character in grid back to uppercase (incorrect letter chosen)
			if (!found) {
				arr[row][col] = Character.toUpperCase(arr[row][col]);
			}
		}
	}

	// Description: finds out how many points a word is worth
	// Parameters: String word is the word found
	// Return: the number of points the word is worth (int)
	public static int countPoints(String word) {
		int len = word.length();
		if (len == 3 || len == 4) {
			return 1;
		}
		else if (len == 5) {
			return 2;
		}
		else if (len == 6) {
			return 3;
		}
		else if (len == 7) {
			return 5;
		}
		else if (len >= 8) {
			return 11;
		}
		return 0;
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
