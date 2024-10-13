// Lynn Tao
// April 21, 2024

package withgraphics;

public class Episode implements Comparable<Episode> {

	private String title;
	private int seasonNo;
	private int episodeNo;
	private boolean watched;
	private Time episodeTime;
	
	// constructor
	public Episode (String title, int episodeNo, Time episodeTime, int seasonNo) {
		this.title = title;
		this.episodeNo = episodeNo;
		this.seasonNo = seasonNo;
		this.watched = false;
		this.episodeTime = episodeTime;
	}
	
	// Description: used by Comparable to compare two episodes by episode number
	// Parameters: Episode e is episode to be compared against
	// Return: int representing pos/zero/neg if current episode number is larger, equal, or less than other episode number
	public int compareTo(Episode e) {
		return this.episodeNo - e.episodeNo;
	}
	
	// Description: formats episode information neatly
	// Parameters: none
	// Return: properly formatted string of "this" episode object
	public String toString() {
		String watchStatus;
		
		if (watched)
			watchStatus = "Watched";
		else
			watchStatus = "Unwatched";
		return String.format("Title: %s%nSeason: %d%nEpisode: %d%nStatus: %s%nLength: %s%n", title, seasonNo, episodeNo, watchStatus, episodeTime);
	}
	
	// getters/setters
	public String getTitle() {
		return title;
	}

	public Time getTime() {
		return episodeTime;
	}
	
	public int getEpisodeNo() {
		return episodeNo;
	}
	
	public boolean getWatched() {
		return watched;
	}
	
	public void setWatched(boolean b) {
		watched = b;
	}
	
	public void setTitle(String t) {
		title = t;
	}
	
	public void setEpNo(int epNo) {
		episodeNo = epNo;
	}
	
	public void setTime(Time t) {
		episodeTime = t;
	}
}
