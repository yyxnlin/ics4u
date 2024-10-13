// Lynn Tao
// April 21, 2024

package nographics;
import java.util.Comparator;

public class SortByTime implements Comparator <Episode>{
	
	// Description: used in Comparator to compare two episodes by time
	// Parameters: Episode e1 and Episode e2 are two episodes to be compared
	// Return: time different in seconds between e1 and e2
	public int compare(Episode e1, Episode e2) {
		return (e1.getTime().getDifference(e2.getTime()));
	}
	

}
