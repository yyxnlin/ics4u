// Lynn Tao
// April 21, 2024

package nographics;
import java.util.Comparator;

public class SortByName implements Comparator <Episode>{

	// Description: used in Comparator to compare two episodes by title
	// Parameters: Episode e1 and Episode e2 are two episodes to be compared
	// Return: pos/zero/neg value depending on whether title of e1 is after/same/before e2
	public int compare(Episode e1, Episode e2) {
		return e1.getTitle().toLowerCase().compareTo(e2.getTitle().toLowerCase());
	}
	
}
