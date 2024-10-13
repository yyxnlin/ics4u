// Fraction class that is used in problem 2

public class Fraction implements Comparable<Fraction>{

	private int numerator;
	private int denominator;
	
	// constructor
	public Fraction(int num, int den) {
		numerator = num;
		denominator = den;
	}
	
	// Description: gives decimal value of fraction
	// Parameters: none
	// Return: double of value of fraction
	public double toDecimal() {
		return 1.0*numerator/denominator;
	}
	
	// Description: compares this fraction against another fraction
	// Parameters: Fraction f is fraction to be compared to
	// Return: 0 if equal, -1 if this is less than f, 1 if this is greater than f
	public int compareTo (Fraction f) {
		if (this.toDecimal() == f.toDecimal()) {
			return 0;
		}
		else if (this.toDecimal() < f.toDecimal()) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
	// Description: gives fraction formatted as string
	// Parameters: none
	// Return: String of fraction formatted
	public String toString() {
		return(numerator + "/" + denominator);
	}
}
