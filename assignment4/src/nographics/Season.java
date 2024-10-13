// Lynn Tao
// April 21, 2024

package nographics;
import java.util.*;

public class Season implements Comparable <Season>{

	private int seasonNo;
	private ArrayList<Episode> episodes;
	private Time seasonTime;
	private int unwatchedEps;
	private int numEps;
	private boolean isWatched;

	// constructor
	public Season (int seasonNo) {
		this.seasonNo = seasonNo;
		this.episodes = new ArrayList<>();
		this.seasonTime = new Time();
	}

	// Description: adds an episode to current season
	// Parameters: Episode episode is the episode to add
	// Return: void, but adds an episode to "this" season's list of episodes
	public void addEpisode (Episode episode) {
		numEps++;
		unwatchedEps++;
		episodes.add(episode);

	}

	// Description: adds an episode to current season
	// Parameters: String title, int epNo, Time time is the title, episode number, and time of episode to be added
	// Return: void, but adds an episode to "this" season's list of episodes
	public void addEpisode (String title, int epNo, Time time) {
		numEps++;
		unwatchedEps++;
		updateWatchedSeason();

		episodes.add(new Episode(title, epNo, time, this.seasonNo));
	}
	
	// Description: adds an episode to current season
	// Parameters: String title, int epNo, Time time is the title, episode number, and time of episode to be added, TVShow show is the show that this episode is under
	// Return: void, but adds an episode to "this" season's list of episodes
	public void addEpisode (String title, int epNo, Time time, TVShow show) {
		numEps++;
		unwatchedEps++;
		updateWatchedSeason();

		// update number of episodes and total show time
		show.setNumEps(show.getNumEps()+1);
		show.addTime(time);
				
		episodes.add(new Episode(title, epNo, time, this.seasonNo));

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
		return seasonTime;
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

	// Description: update total time in season
	// Parameters: none
	// Return: void, but changes seasonTime to be total time in season
	public void updateTime() {
		for (Episode episode : episodes) {
			seasonTime.add(episode.getTime());
		}
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

	// Description: prints out episodes in a season neatly
	// Parameters: int sortOption is the sorting order (1 = ep #, 2 = title, 3 = time)
	// Return: void, but prints out the episodes in console
	public void displayEpisodes(int sortOption) {
		this.sortEpisodes(sortOption);
		
		if (episodes.size() == 0) {
			return;
		}

		System.out.println("  ===================== Episodes =====================");
		String watchStatus = "";

		for (Episode episode : episodes) {
			if (episode.getWatched())
				watchStatus = "WATCHED";
			else
				watchStatus = "UNWATCHED";

			System.out.println(String.format("  Ep. %-4d %-34s%-30s", episode.getEpisodeNo(), episode.getTitle(), watchStatus));
		}
		System.out.println("  ====================================================");

	}
	
	// Description: prints out episodes in a season neatly with the time of episode
	// Parameters: int sortOption is the sorting order (1 = ep #, 2 = title, 3 = time)
	// Return: void, but prints out the episodes with the time in console
	public void displayEpisodesWithTime(int sortOption) {
		this.sortEpisodes(sortOption);
		System.out.println("  ===================== Episodes =====================");

		for (Episode episode : episodes) {			
			System.out.println(String.format("  Ep. %-4d %-34s%-30s", episode.getEpisodeNo(), episode.getTitle(), episode.getTime()));
		}
		System.out.println("  ====================================================");

	}

	// Description: prints out information about a specific tvShow neatly
	// Parameters: int epPos is the index of episode in list to be displayed
	// Return: void, but prints out the information in console
	public void episodeInfo(int epPos) {
		Episode episode = episodes.get(epPos); 
		System.out.println("\n  ===== EPISODE INFO =====");
		System.out.print(episode);
		System.out.println("  ========================");
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
	// Parameters: int epPos is the index of episode in list to be removed, TVShow show is the show that the episode is in
	// Return: void, but removes the episode and updates related variables
	public void removeEpisode (int epPos, TVShow show) {
		numEps--;
		
		// update number of episodes and total show time
		show.setNumEps(show.getNumEps()-1);
		show.subtractTime(episodes.get(epPos).getTime());
				
		
		if (!episodes.get(epPos).getWatched()) {
			unwatchedEps--;
		}
		episodes.remove(epPos);
		updateWatchedSeason();
		
	}

	// Description: removes watched episode from season
	// Parameters: TVShow show is the show that the episodes are in
	// Return: void, but removes the episodes and updates related variables
	public void removeWatchedEpisodes (TVShow show) {
		// remove all watched
		for (int i = 0; i < episodes.size(); i++) {
			if (episodes.get(i).getWatched()) {
				
				// update number of episodes and total show time
				show.setNumEps(show.getNumEps()-1);
				show.subtractTime(episodes.get(i).getTime());
				
				
				numEps--;
				episodes.remove(i);
				i--;
			}
		}
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
