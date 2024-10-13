// Lynn Tao
// March 3, 2024
// Recursion Exercises: 7 problems to solve recursively

import java.util.Scanner;

public class RecursionExercisesFinal {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		int question = -1;

		while (question != 0) {
			boolean valid = false;

			while (!valid) {

				try {
					System.out.print("Enter an exercise # (0 to quit): ");
					question= Integer.parseInt(in.nextLine());
					
					if (question == 0) {
						System.out.println("Program terminated.");
						System.exit(0);
					}
					else if (question < 1 || question > 7) {
						throw new NumberFormatException();
					}
					valid = true;
				}
				
				catch (NumberFormatException e) {
					valid = false;
				}
			}
			
			if (question == 1) {
				System.out.println("FINDING HOW MANY CANDIES WE HAVE TO BRIBE MS. WONG'S KIDS");

				System.out.print("   Enter the number of students in line: ");
				int num = Integer.parseInt(in.nextLine());
				
				if (num < 0) 
					System.out.println("Invalid input!");
				
				else
					System.out.println("   We have " + candies(num) + " candies to bribe Ms. Wong's kids >:)");
			}
			
			else if (question == 2) {
				System.out.println("CALCULATING POWERS");
				
				System.out.print("   Enter the base: ");
				int base = Integer.parseInt(in.nextLine());
				
				System.out.print("   Enter the exponent: ");
				int exp = Integer.parseInt(in.nextLine());
				
				System.out.println(power(base, exp));

			}
			
			else if (question == 3) {
				System.out.println("COUNTING NUMBER OF NON-NUMERICAL DIGITS");

				System.out.print("   Enter the MATHEMATICAL expression (no spaces please >:(): ");
				String expression = in.nextLine();
				
				System.out.printf("   Your expression has %d non-numerical digit(s).\n", find(expression));

			}
			
			else if (question == 4){
				System.out.println("CONVERTING FROM BASE N -> BASE 10");
				
				System.out.print("   Enter the number in YOUR BASE: ");
				String number = in.nextLine();
				
				System.out.print("   Enter your base: ");
				int base = Integer.parseInt(in.nextLine());
				
				// invalid base (<1 or > 10) or value (negative)
				if (base > 10 || base <= 1 || Integer.parseInt(number) < 0) {
					System.out.println("Invalid input.");
				}
				
				// valid base or value
				else {
					int result = convert(number, base);
					System.out.printf("%s in base %d is %d in base 10.\n", number, base, result);	
				}
				
			}
			
			else if (question == 5) {
				System.out.println("REMOVING CONSECUTIVE DUPLICATE LETTERS");
				System.out.print("   Enter a string: ");
				String s = in.nextLine();
				System.out.println(removeDup(s));
			}
			
			else if (question == 6) {
				System.out.println("PADDING 0'S AND COMMAS TO A NUMBER");
				System.out.print("   Enter a number: ");
				int number = Integer.parseInt(in.nextLine());
				System.out.println(commas(number));
			}
			
			else if (question == 7) {
				int[] arr = {3, 0, 4, -2, 10, -7, 2, 5};
				System.out.println(funnySum(arr, 0));
			}
			
			System.out.println();
		}
	}

	// problem 1
	public static int candies(int n) {
		if (n == 0) {
			return 0;
		}

		if (n % 2 == 0) {
			return (candies (n-1) + 3);
		}
		else {
			return (candies(n-1) + 5);
		}

	}

	// problem 2
	public static double power(int num, int exp) {
		
		if (exp > 0) {
			return (num * power(num, exp-1));
		}

		else if (exp < 0){
			return (1.0/num * power(num, exp+1));
		}

		return 1;

	}

	// problem 3
	public static int find(String s) {
		if (s.length() == 0) {
			return 0;
		}

		if (!Character.isDigit(s.charAt(0)) && s.charAt(0) != '(' && s.charAt(0) != ')') {
			return (find(s.substring(1)) + 1);
		}
		return (find(s.substring(1)));
	}

	// problem 4
	public static int convert (String s, int base) {
		if (s.length() == 0 || Integer.parseInt(s) == 0) {
			return 0;
		}

		return(Integer.parseInt(s.substring(0, 1)) * (int) Math.pow(base, (s.length()-1))) + convert(s.substring(1), base);
	}

	// problem 5
	public static String removeDup (String s) {
		if (s.length() <= 1) {
			return s;
		}

		if (Character.toUpperCase(s.charAt(0)) == Character.toUpperCase(s.charAt(1))) {
			return removeDup(s.substring(1));
		}
		return s.charAt(0) + (removeDup(s.substring(1)));

	}

	// problem 6
	public static String commas(int num) {

		// edge case if the number itself is 0
		if (num == 0) {
			return ("000");
		}
		
		// stopping case
		if (num/1000 == 0) {
			// pad with zeros
			String zeros = "";
			if (num/10 == 0) {
				zeros = "00";
			}
			else if (num/100 == 0) {
				zeros = "0";
			}

			// add + or - sign
			if (num > 0) {
				return ("+" + zeros + num);
			}
			else if (num < 0){
				return ("-" + zeros + Math.abs(num));
			}
		} 

		// if last three digits is 000, add three 0's manually
		if (Math.abs(num) % 1000 == 0) {
			return (commas(num/1000) + ",000");
		}
		
		// if last three digits is 00x, add two 0's manually
		else if (Math.abs(num) % 1000 < 10) {
			return (commas(num/1000) + ",00" + Math.abs(num) % 1000);
		}
		
		// if last three digits is 0xx, add a 0 manually
		else if (Math.abs(num) % 1000 < 100) {
			return (commas(num/1000) + ",0" + Math.abs(num) % 1000);
		}
		
		// if last three digits does not start with 0, display these digits normally
		return (commas(num/1000) + "," + Math.abs(num) % 1000);

	}

	// problem 7
	// parameters: int[] arr is the integer array, int index is the index to start adding from (leave it as 0 when called in main method)
	public static int funnySum(int[] arr, int index) {
		
		if (index == arr.length-1) {
			return index*Math.abs(arr[index]);
		}
		return (index*Math.abs(arr[index]) + funnySum(arr, index+1));

		
	}
}
