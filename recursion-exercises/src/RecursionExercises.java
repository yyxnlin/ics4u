// Lynn Tao
// March 1, 2024
// Recursion Exercises
import java.util.Scanner;

public class RecursionExercises {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		int question = 0;

		while (question != -1) {
			boolean valid = false;
			System.out.print("Enter an exercise # (1-7, -1 to quit): ");

			while (!valid) {

				try {
					question= Integer.parseInt(in.nextLine());
					
					if (question == -1) {
						System.out.println("Program terminated");
						System.exit(0);
					}
					else if (question < 1 || question > 7) {
						throw new NumberFormatException();
					}
					valid = true;
				}
				
				catch (NumberFormatException e) {
					System.out.print("     Invalid input. Enter an exercise # (1-7, -1 to quit): ");
					valid = false;
				}
			}
			
			if (question == 1) {
				System.out.println("FINDING HOW MANY CANDIES WE HAVE TO BRIBE MS. WONG'S KIDS");
				int num = 0;
				boolean validNum = false;
				System.out.print("   Enter the number of students in line: ");

				while (!validNum) {

					try {
						num= Integer.parseInt(in.nextLine());
						
						if (num < 0) {
							throw new NumberFormatException();
						}
						validNum = true;
					}
					
					catch (NumberFormatException e) {
						System.out.print("     Invalid input. Enter the number of students in line: ");
						validNum = false;
					}
				}
				System.out.println("   We have " + candies(num) + " candies to bribe Ms. Wong's kids.");
			}
			
			else if (question == 2) {
				System.out.println("CALCULATING POWERS");

				int base = 0;
				int exp = 0;
				
				boolean validNum = false;
				System.out.print("   Enter the base: ");

				while (!validNum) {

					try {
						base = Integer.parseInt(in.nextLine());
						validNum = true;
					}
					
					catch (NumberFormatException e) {
						System.out.print("     Invalid input. Enter the base: ");
						validNum = false;
					}
				}
				
				validNum = false;
				System.out.print("   Enter the exponent: ");

				while (!validNum) {

					try {
						exp = Integer.parseInt(in.nextLine());
						validNum = true;
					}
					
					catch (NumberFormatException e) {
						System.out.print("     Invalid input. Enter the exponent: ");
						validNum = false;
					}
				}
				
				System.out.println(power(base, exp));

			}
			
			else if (question == 3) {
				System.out.println("COUNTING NUMBER OF NON-NUMERICAL DIGITS");
				String expression = "";
				System.out.print("   Enter the MATHEMATICAL expression (no spaces please >:(((): ");
				expression = in.nextLine();
				System.out.printf("   Your expression has %d non-numerical digit(s).\n", find(expression));

			}
			
			else if (question == 4){
				System.out.println("CONVERTING FROM BASE N -> BASE 10");
				String number = "";
				int base = 0;
				System.out.print("   Enter the number in YOUR BASE: ");
				
				valid = false;
				
				while (!valid) {
					try {
						base = Integer.parseInt(in.nextLine());
						
						if (base <= 1) {
							throw new NumberFormatException();
						}
						
						valid = true;
					}
					
					catch(NumberFormatException e) {
						System.out.print("     INVALID. Enter your base: ");
						valid = false;
					}
				}
				number = in.nextLine();
				System.out.print("   Enter your base: ");
				
				valid = false;
				
				while (!valid) {
					try {
						base = Integer.parseInt(in.nextLine());
						
						if (base <= 1) {
							throw new NumberFormatException();
						}
						
						valid = true;
					}
					
					catch(NumberFormatException e) {
						System.out.print("     INVALID. Enter your base: ");
						valid = false;
					}
				}
				
				System.out.printf("%s in base %d is %d in base 10.\n", number, base, convert(number, base));
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
				int[] arr = {3, 0, 4, -2, 10, -7};
				System.out.println(funnySum(arr, 0));
			}
			System.out.println();
		}
	}

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

	public static double power(int num, int exp) {

		if (exp > 0) {
			return (num * power(num, exp-1));
		}

		else if (exp < 0){
			return (1.0/num * power(num, exp+1));
		}

		return 1;


	}

	public static int find(String s) {
		if (s.length() == 0) {
			return 0;
		}

		if (!Character.isDigit(s.charAt(0)) && s.charAt(0) != '(' && s.charAt(0) != ')') {
			return (find(s.substring(1)) + 1);
		}
		return (find(s.substring(1)));
	}

	public static int convert (String s, int base) {
		if (s.length() == 0) {
			return 0;
		}

		return(Integer.parseInt(s.substring(0, 1)) * (int) Math.pow(base, (s.length()-1))) + convert(s.substring(1), base);
	}

	public static String removeDup (String s) {
		if (s.length() <= 1) {
			return s;
		}

		if (Character.toUpperCase(s.charAt(0)) == Character.toUpperCase(s.charAt(1))) {
			return removeDup(s.substring(1));
		}
		return s.charAt(0) + (removeDup(s.substring(1)));

	}

	public static String commas(int num) {

		if (num == 0) {
			return ("000");
		}
		if (num/1000 == 0) {
			String zeros = "";
			if (num/10 == 0) {
				zeros = "00";
			}
			else if (num/100 == 0) {
				zeros = "0";
			}

			if (num > 0) {
				return ("+" + zeros + num);
			}
			else if (num < 0){
				return ("-" + zeros + Math.abs(num));
			}
		}

		return (commas(num/1000) + "," + Math.abs(num) % 1000);
	}

	public static int funnySum(int[] arr, int index) {
		
		if (index == arr.length-1) {
			return index*Math.abs(arr[index]);
		}
		return (index*Math.abs(arr[index]) + funnySum(arr, index+1));

		
	}
}
