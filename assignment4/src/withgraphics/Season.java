// Lynn Tao
// April 21, 2024

package withgraphics;
import java.util.*;

public class Season implements Comparable <Season>{

	private int seasonNo;
	private ArrayList<Episode> episodes;
	private int unwatchedEps;
	private int numEps;
	private boolean isWatched;

	// constructor
	public Season (int seasonNo) {
		this.seasonNo = seasonNo;
		this.episodes = new ArrayList<>();
	}

	// Description: adds an episode to current season
	// Parameters: Episode episode is the episode to add
	// Return: void, but adds an episode to "this" season's list of episodes
	public void addEpisode (Episode episode) {
		numEps++;
		unwatchedEps++;
		updateWatchedSeason();

		episodes.add(episode);

	}

	// Description: used by Comparable to compare two seasons by season number
	// Parameters: Season s is season to be compared against
	// Return: int representing pos/zero/neg if current season number is larger, equal, or less than other season number
	public int compareTo(Season s) {
		return this.seasonNo - s.seasonNo;
	}

	// Description: formats season number
	// Parameters: none
	// Return: properly formatted string of "this" Season object
	public String toString() {
		return String.format("Season %d", seasonNo);
	}

	// getters
	public int getNumEps() {
		return numEps;
	}

	public Time getSeasonTime() {
		Time t = new Time();
		for (Episode e : episodes) {
			t.add(e.getTime());
		}
		return t;
	}

	public ArrayList<Episode> getEpisodes(){
		return episodes;
	}

	public int getSeasonNo() {
		return seasonNo;
	}

	public int getUnwatchedEps() {
		return unwatchedEps;
	}

	public boolean getIsWatched() {
		return isWatched;
	}


	// Description: updates isWatched variable
	// Parameters: none
	// Return: void, but changes isWatched to true/false depending on how many episodes are left
	public void updateWatchedSeason() {
		if (unwatchedEps == 0) {
			isWatched = true;
		}
		else
			isWatched = false;
	}


	// Description: formats episodes in a season with watch status neatly
	// Parameters: int sortOption is the sort option (1/2/3) to display episodes in
	// Return: String with formatted episodes
	public String displayEpisodes(int sortOption) {
		this.sortEpisodes(sortOption);

		String result = "";
		String watchStatus = "";

		for (Episode episode : episodes) {
			if (episode.getWatched())
				watchStatus = "WATCHED";
			else
				watchStatus = "UNWATCHED";

			result += String.format("Ep. %-4d %-50s%-12s%-10s\n", episode.getEpisodeNo(), episode.getTitle(), episode.getTime(), watchStatus);
		}
		return result;

	}

	// Description: formats information about a specific episode neatly
	// Parameters: int epPos is the index of episode in list to be displayed
	// Return: String with formatted episode info
	public String episodeInfo(int epPos) {
		Episode episode = episodes.get(epPos); 
		return "" + episode;
	}

	// Description: sets an episode to watched
	// Parameters: int epPos is the index of episode in list to be watched
	// Return: void, but sets the episode to watched and updates related variables
	public void watchEpisode (int epPos) {
		Episode episode = episodes.get(epPos); 
		unwatchedEps--;
		episode.setWatched(true);

		updateWatchedSeason();
	}


	// Description: removes an episode from season
	// Parameters: int epPos is the index of episode in list to be removed
	// Return: void, but removes the episode and updates related variables
	public void removeEpisode (int epPos) {
		numEps--;

		if (!episodes.get(epPos).getWatched()) {
			unwatchedEps--;
		}
		episodes.remove(epPos);
		updateWatchedSeason();
	}

	// Description: removes watched episode from season
	// Parameters: TVShow show is the show that the episodes are in
	// Return: void, but removes the episodes and updates related variables
	public void removeWatchedEpisodes () {
		// remove all watched
		for (int i = 0; i < episodes.size(); i++) {
			if (episodes.get(i).getWatched()) {
				numEps--;
				episodes.remove(i);
				i--;
			}
		}
	}

	// Description: calculates total watched time in a season
	// Parameters: none
	// Return: Time representing total time of all watched episodes
	public Time getWatchedTime() {
		Time t = new Time();
		for (int i = 0; i < episodes.size(); i++) {
			if (episodes.get(i).getWatched()) {
				t.add(episodes.get(i).getTime());
			}
		}
		return t;
	}

	// Description: sorts episodes
	// Parameters: int sortOption is the sorting order (1 = ep #, 2 = title, 3 = time)
	// Return: void, but sorts the ArrayList of episodes based on the sort option
	public void sortEpisodes (int sortOption) {
		if (sortOption == 1) {
			Collections.sort (episodes);
		}

		else if (sortOption == 2) {
			Collections.sort (episodes, new SortByName());
		}

		else {
			Collections.sort (episodes, new SortByTime());
		}
	}

}
