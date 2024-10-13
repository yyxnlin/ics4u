// Lynn Tao
// March 29, 2024
// This class implements Comparator to compare Player objects by name

import java.util.*;

public class SortByName implements Comparator<Player> {
	
	// Description: compares two strings representing the names of two players
	// Parameters: Player o1 is first player, Player o2 is second player
	// Return: difference between o1's name and o2's name based on alphabetical order
	public int compare(Player o1, Player o2) {
		return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
	}
	
}
