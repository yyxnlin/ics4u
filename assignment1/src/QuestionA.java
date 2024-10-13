// Lynn Tao
// Feb. 21, 2024
// This program is a program where I am trying to sell my Pokemon cards to Ms. Wong to see if she'll buy it, otherwise I'll sell it on Kijiji o(￣┰￣*)ゞ

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class QuestionA {

	public static void main(String[] args) throws IOException {
		boolean selling = true;
		Scanner in = new Scanner (System.in);
		String name, energyType;
		double cardAmt = 0;
		double totalWongCost = 0;
		double totalKijijiCost = 0;
		String expensiveName = "";
		double expensiveAmt = 0;
		int cardNum = 0;
		int kijijiCount = 0;
		String[] validTypes = {"grass", "fire",	"water", "poison", "fairy", 
				"psychic", "ghost",	"dark", "other"};
		

		PrintWriter outFile = new PrintWriter (new FileWriter ("receipt.txt"));
		
		// print headers
		outFile.println("Summary of Ms. Wong’s purchases:");
		outFile.printf("%-25s%-25s%-25s\n", "CARD", "TYPE", "COST");
		outFile.printf("%-25s%-25s%-25s\n", "----", "----", "----");


		System.out.println("Bye Bye My Pokemon Cards");

		while (selling) {
			System.out.println();
			cardNum++;

			// enter card number
			System.out.print("Please enter the name of card #" + cardNum + ": ");
			name = in.nextLine().trim();

			// enter energy type
			System.out.print("Please enter the energy type of this Pokemon: ");
			energyType = in.nextLine().trim();

			// check if energy type is valid
			while (!checkValidType(energyType, validTypes)) {
				System.out.print("    INVALID. Please enter the energy type of this Pokemon: ");
				energyType = in.nextLine().trim();
			}

			// enter amount
			boolean isValidAmount = false;

			System.out.print("Please enter the amount for this card: $");

			// check if amount is valid
			while (!isValidAmount) {
				try {
					cardAmt = Double.parseDouble(in.nextLine().trim());
					cardAmt = Math.round(cardAmt * 100)/100.0;
					if (cardAmt > 0 && cardAmt < Integer.MAX_VALUE)
						isValidAmount = true;
					else
						throw new NumberFormatException();
				}
				catch (NumberFormatException e) {
					System.out.print("    INVALID. Please enter the amount for this card: $");
					isValidAmount = false;
				}
			}	

			// ask if ms. wong will buy
			System.out.printf("Will Ms. Wong buy this $%.2f card? (y/n): ", cardAmt);
			String willBuy = in.nextLine().trim();

			// keep repeating if "y" or "n" is not entered
			while (!checkYN(willBuy)) {
				System.out.printf("    INVALID. Will Ms. Wong buy this $%.2f card? (y/n): ", cardAmt);
				willBuy = in.nextLine().trim();
			}

			// ms. wong buys
			if (willBuy.toLowerCase().equals("y")) { 
				totalWongCost += cardAmt;
				outFile.printf("%-25s%-25s$%-25.2f\n", name, energyType, cardAmt);
			}
			
			// sell on kijiji
			else { 
				kijijiCount++;
				totalKijijiCost += cardAmt;
			}
			
			// check if it becomes most expensive card
			if (cardAmt > expensiveAmt) {
				expensiveName = name;
				expensiveAmt = cardAmt;
			}

			// ask if you're selling more
			System.out.print("Are you selling any more cards? (y/n): ");
			String sellingMore = in.nextLine().trim();

			// keep repeating if "y" or "n" is not entered
			while (!checkYN(sellingMore)) {
				System.out.printf("    INVALID. Are you selling any more cards? (y/n): ", cardAmt);
				sellingMore = in.nextLine().trim();
			}

			// if not selling more -> print final cost and summary lines
			if (sellingMore.toLowerCase().equals("n")) {
				selling = false;
				outFile.println("------------------------------------------------------------------");
				outFile.printf("%-50s$%-25.2f\n\n", "FINAL COST", totalWongCost);
				
				// still has cards to sell on kijiji
				if (kijijiCount > 0) 
					outFile.printf("You still need to sell %d card%s for $%.2f.\n", kijijiCount, fixPlural(kijijiCount), totalKijijiCost);
				
				// no cards to sell on kijiji
				else
					outFile.println("Ms. Wong bought all your cards (yay!!!), no cards to sell on Kijiji!");

				// print most expensive card
				outFile.printf("The most expensive card you are selling is %s for $%.2f.", expensiveName, expensiveAmt);
				outFile.close();
			}
		}
		in.close();
		
	}

	// Description: checks if a type is a Ms. Wong approved pokemon type
	// Parameters: String type is the type to be checked, String[] validTypes is a String array of Ms. Wong approved types
	// Return: true if it is a valid type, false if it is not
	public static boolean checkValidType(String type, String[] validTypes) {
		for (int i = 0; i < validTypes.length; i++) {
			if (type.toLowerCase().equals(validTypes[i])) {
				return true;
			}
		}
		return false;
	}
	
	// Description: checks if a "y" or "n" is entered (case insensitive)
	// Parameters: String input is the input to be checked
	// Return: true if it is "y" or "n", false if it is not
	public static boolean checkYN(String input) {
		if (input.toLowerCase().equals("y") || input.toLowerCase().equals("n")) {
			return true;
		}
		return false;
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
