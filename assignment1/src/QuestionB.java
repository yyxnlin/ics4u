// Lynn Tao
// Feb. 21, 2024
// This program repeatedly adds an integer with its reverse until a palindrome is reached, in which the program outputs how many steps it took.

import java.util.Scanner;

public class QuestionB {

	public static void main(String[] args) {
		Scanner in = new Scanner (System.in);

		while(true) { // loops forever (until 0 is entered)
			int count = 0;
			long num = 0;
			boolean validInput = false;
			boolean found = false;
			String input = "";

			System.out.print("Please input a starting number (0 to quit): ");

			// loops until valid long is entered
			while (!validInput) {
				try {
					input = in.nextLine().trim();
					num = Long.parseLong(input);
					if (num < 0) {
						throw new NumberFormatException();
					}
					validInput = true;

				}
				catch (NumberFormatException e) {
					System.out.print("   INVALID. Please input a starting number (0 to quit): ");
					validInput = false;				
				}
			}	

			if (num == 0) { // if 0 is entered, exit
				System.out.println("Program is complete");
				in.close();
				System.exit(0);
			}

			// if number is already a palindrome, output that
			if (checkPalindrome(num)) {
				System.out.println(num + " is already a palindrome!");
			}

			// if number is not already a palindrome, find number of steps
			else {
				// loop as long as sum is not a palindrome
				while(!found) {
					num = findSum(num); // update num to the newest sum
					found = checkPalindrome(num); // check if this number is a palindrome
					count++; // increase number of steps by 1

					// if max value of long is reached, then palindrome cannot be attained
					if (num == Long.MAX_VALUE) {
						break;
					}
				}

				if (found) // if palindrome is reached, output number of steps
					System.out.printf("%s becomes the palindrome %d after %d step%s.\n", input, num, count, fixPlural(count));
				else // if palindrome is not reached, output impossible
					System.out.printf("%s does not become a palindrome.\n", input);
			}
		}
	}

	// Description: checks if a long is a palindrome
	// Parameters: long num is the number to be checked
	// Return: true if it is a palindrome, false if it is not
	public static boolean checkPalindrome(long num) {
		String text = Long.toString(num);
		for (int i = 0; i <= text.length()/2; i++) {
			if (text.charAt(i) != text.charAt(text.length()-1-i)) {
				return false;
			}
		}
		return true;
	}

	// Description: finds the sum of a number and its reverse
	// Parameters: long num is the number to be added with its reverse
	// Return: sum of the number and its reverse
	public static long findSum(long num) {
		int len = Long.toString(num).length();
		long sum = 0;
		
		// the digit in the 10^i place value in original number becomes the 10^(len-1-i) place value in its reverse
		// num divided by its highest place value (10^i) truncates the answer to give the first digit
		// num mod the highest place value (10^i) gives the rest of the number excluding first digit -> repeat the process
		for (int i = 0; i < len; i++) {
			sum += (Math.pow(10, len-1-i) + Math.pow(10, i)) * (num/((long)(Math.pow(10, len-1-i))));
			num %= Math.pow(10, len-1-i);
		}
		return sum;
	}

	// Description: returns "s" or "" depending on whether a number needs a plural noun or not
	// Parameters: int num is the number to be checked against
	// Return: "s" if plural form is needed, "" if not needed
	public static String fixPlural(int num) {
		if (num > 1 || num == 0) {
			return "s";
		}
		return "";
	}


}
