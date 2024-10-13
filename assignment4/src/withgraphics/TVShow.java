// Lynn Tao
// April 21, 2024

package withgraphics;
import java.util.*;

public class TVShow implements Comparable<TVShow> {
	private String title;
	private String genre;
	private ArrayList<Season> seasons;
	private Time showTime;
	private int numSeasons;
	private int numEps;
	
	// constructor
	public TVShow (String title, String genre) {
		this.title = title;
		this.genre = genre;
		this.seasons = new ArrayList<>();
		this.showTime = new Time();
	}
	
	// Description: adds season to current TV show
	// Parameters: Season season is season to be added
	// Return: void, but adds that season to the list of seasons in "this" tv show
	public void addSeason (Season season) {
		numSeasons++;
		this.seasons.add(season);
	}
	
	// Description: formats tv show title and genre neatly
	// Parameters: none
	// Return: properly formatted string of "this" TVShow object
	public String toString() {
		return String.format("%-25s%-15s", title, genre);
	}
	
	// Description: used by Comparable to compare two tv shows by title
	// Parameters: TVShow t is tv show to be compared against
	// Return: int representing pos/zero/neg if current tv show is before, same, or after other tv show alphabetically
	public int compareTo (TVShow t) {
		return this.title.toLowerCase().compareTo(t.title.toLowerCase());
	}

	// getters/setters
	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}
	
	public ArrayList<Season> getSeasons() {
		return seasons;
	}

	public Time getShowTime() {
		return showTime;
	}
	
	public int getNumSeasons() {
		return numSeasons;
	}
	
	public int getNumEps() {
		return numEps;
	}
	
	public void setNumSeasons(int num) {
		numSeasons = num;
	}
	
	public void setNumEps(int num) {
		numEps = num;
	}
	
	// Description: subtract time from tv show
	// Parameters: Time t is time to be subtracted
	// Return: void, but changes showTime to subtract the time
	public void subtractTime(Time t) {
		showTime.subtract(t);
	}
	
	// Description: add time from tv show
	// Parameters: Time t is time to be added
	// Return: void, but changes showTime to add the time
	public void addTime(Time t) {
		showTime.add(t);
	}

}
