// Lynn Tao
// May 10, 2024
// This program displays how many distinct substrings there are in a string

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;

public class Problem1 {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner inFile = new Scanner (new FileReader ("file.txt"));

		// first line is number of lines to be read
		int numLines = Integer.parseInt(inFile.nextLine());

		System.out.println("Finding the number of Substrings");
		
		// loop through each line
		for (int t = 0; t < numLines; t++) {
			// read string
			String s = inFile.nextLine().toLowerCase().trim();

			// hashset of substrings
			HashSet<String> set = new HashSet<>();
			set.add(""); // add empty string

			// i keeps track of starting index
			for (int i = 0; i < s.length(); i++) {
				// j keeps track of ending index
				for (int j = i+1; j <= s.length(); j++) {
					String sub = s.substring(i, j);
					set.add(sub); // add substring to set
				}
			}

			// print results
			System.out.println("String: " + s);
			System.out.println("No. of substrings: " + set.size());
		}
	}

}
