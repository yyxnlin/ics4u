// Lynn Tao
// May 10, 2024
// This program finds the number of fractions within a range with less than a certain denominator

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class Problem2 {

	public static void main(String[] args) {

		Scanner in = new Scanner (System.in);

		while (true) {

			int maxDenom = 0;
			boolean valid = false;

			// enter max denominator
			while (!valid) {
				try {
					System.out.print("Enter the maximum denominator: ");
					maxDenom = Integer.parseInt(in.nextLine());

					// negative/zero entered
					if (maxDenom <= 0) {
						throw new NumberFormatException();
					}
					valid = true; // valid number entered -> break
				}

				catch (NumberFormatException e) {
					System.out.print("Invalid. ");
				}
			}


			// lower and upper limits
			Fraction lower = new Fraction (0, 1);
			Fraction upper = new Fraction (1, 1);

			valid = false;

			// enter lower limit
			while (!valid) {
				try {
					System.out.print("Enter the lower limit: ");
					String f = in.nextLine();

					// make sure / exists
					if (f.indexOf("/") == -1) {
						throw new NumberFormatException();
					}

					// get numerator and denominator
					int num = Integer.parseInt(f.substring(0, f.indexOf("/")));
					int denom = Integer.parseInt(f.substring(f.indexOf("/")+1));

					// invalid fraction
					if (num > denom || num < 0 || denom <= 0) {
						throw new NumberFormatException();
					}

					lower = new Fraction(num, denom);
					valid = true; // valid fraction entered -> break
				}

				catch (NumberFormatException e) {
					System.out.print("Invalid fraction. ");
				}
			}

			valid = false;

			// enter upper limit
			while (!valid) {
				try {
					System.out.print("Enter the upper limit: ");
					String f = in.nextLine();

					// make sure / exists
					if (f.indexOf("/") == -1) {
						throw new NumberFormatException();
					}

					// get numerator and denominator
					int num = Integer.parseInt(f.substring(0, f.indexOf("/")));
					int denom = Integer.parseInt(f.substring(f.indexOf("/")+1));

					// invalid fraction
					if (num > denom || num < 0 || denom <= 0) {
						throw new NumberFormatException();
					}

					upper = new Fraction(num, denom);

					// make sure upper fraction is not less than lower fraction
					if (upper.toDecimal() < lower.toDecimal()) {
						throw new NumberFormatException();
					}

					valid = true; // valid fraction entered -> break
				}

				catch (NumberFormatException e) {
					System.out.print("Invalid fraction. ");
				}
			}

			TreeSet<Fraction> set = new TreeSet<>();

			// fractions within the range of max denom
			for (int denom = 1; denom <= maxDenom; denom++) { // denom ranges from 1 to maxDenom
				for (int num = 0; num <= denom; num++) { // num ranges from 0 to maxDenom
					set.add(new Fraction(num, denom)); // add fraction to set
				}
			}

			System.out.println("Total number of fractions: " + set.size());


			// number of fractions within the lower/upper bound range
			int count = 0;

			// iterate through set of fractions			
			for (Fraction f : set) {
				// add to counter if fraction is between lower and upper bounds
				if (f.toDecimal() <= upper.toDecimal() && f.toDecimal() >= lower.toDecimal()) { 
					count++;

				}
			}

			System.out.println(String.format("Number of Fractions between %s and %s inclusive: %d%n", lower, upper, count));

		}	

	}

}
