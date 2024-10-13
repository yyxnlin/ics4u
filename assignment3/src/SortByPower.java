// Lynn Tao
// March 29, 2024
// This class implements Comparator to compare Player objects by power

import java.util.*;

public class SortByPower implements Comparator<Player> {
	
	// Description: compares two strings representing the powers of two players
	// Parameters: Player o1 is first player, Player o2 is second player
	// Return: difference between o1's power and o2's power based on alphabetical order
	public int compare(Player o1, Player o2) {
		return o1.getPower().toLowerCase().compareTo(o2.getPower().toLowerCase());
	}
	
}
