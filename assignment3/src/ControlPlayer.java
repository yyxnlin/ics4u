// Lynn Tao
// March 29, 2024
// This program will allow you to search for name or power of a set of players and display the rankings of each

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ControlPlayer {

	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<Player> playersByScore = new ArrayList<>();

		Scanner inFile = new Scanner (new File ("players.txt"));
		Scanner in = new Scanner (System.in);

		while (inFile.hasNextLine()) {
			String line = inFile.nextLine().trim();

			try {
				// remove multiple spaces in input
				while(line.indexOf("  ") != -1) {
					line = line.substring(0, line.indexOf("  ")) + line.substring(line.indexOf("  ")+1);
				}
				
				// invalid # of elements (you need at least 2 spaces)
					// -> if no space, indexOf(" ") and lastIndexOf(" ") is -1
					// -> if only one space, indexOf(" ") and lastIndexOf(" ") are same
				if (line.indexOf(" ") == line.lastIndexOf(" ") || line.lastIndexOf(" ") == -1) {
					throw new NumberFormatException();
				}
				
				// score is number up until first space
				int score = Integer.parseInt(line.substring(0, line.indexOf(" ")));
				if (score < 0) { // can't have negative score
					throw new NumberFormatException();
				}
				
				// name is between first and last space
				String name = line.substring(line.indexOf(" ") + 1, line.lastIndexOf(" "));
								
				// power is after last space
				String power = line.substring(line.lastIndexOf(" ")+1);

				// create new Player object inside array
				playersByScore.add(new Player (score, name, power));

			}

			catch (NumberFormatException e) { 
				// invalid score
			}
			
		}
		inFile.close();

		int numPlayers = playersByScore.size();
		
		// copy items inside playersByScore
		ArrayList<Player> playersByPower = new ArrayList<> (playersByScore);
		ArrayList<Player> playersByName = new ArrayList<> (playersByScore);

		// sort name and power arraylists in alphabetical order using Comparator
		Collections.sort(playersByName, new SortByName());
		Collections.sort(playersByPower, new SortByPower());

		// natural sort order is score in ascending order (Comparable)
		Collections.sort(playersByScore); 

		// printing out players to test
//		System.out.println(playersByScore);

		while(true) {
			System.out.print("Name or power (N/P) (\"exit\" to exit): ");
			String option = in.nextLine().trim();

			ArrayList<Player> results = new ArrayList<>(); // stores players that are searched for
			
			// order to list results in
			boolean orderByPower = false;
			boolean orderByScore = false;
			boolean orderByName = false;

			// search by name
			if (option.toLowerCase().equals("n")) {
				System.out.print("Enter a name: ");
				String name = in.nextLine();
				
				// search for the name
				int pos = Collections.binarySearch(playersByName, new Player(0, name, ""), new SortByName());

				// name is found
				if (pos >= 0) {
					// add to results, and check if there are other duplicate names
					results.add(playersByName.get(pos));
					findAllName(playersByName, results, pos, name);

					// only one result -> no need to order
					if (results.size() == 1) {
						orderByPower = true;
					}

					// searching name, so you can order by power or score if there's duplicates
					else {
						orderByPower = checkPowersUnequal(results);
						orderByScore = checkScoresUnequal(results);

						// if scores (rankings) and powers are both not all equal, ask user what they want to order by
						if (orderByPower && orderByScore) {
							boolean validInput = false;

							while (!validInput) {
								System.out.print("Sort by power or ranking? (P/R): ");
								String sortOption = in.nextLine().toLowerCase().trim();

								// sort by power
								if (sortOption.equals("p")) {
									orderByScore = false;
									break;
								}

								// sort by ranking
								else if (sortOption.equals("r")) {
									orderByPower = false;
									break;
								}

								System.out.print("    Invalid input. ");
							}
						}
					}
				}
				
				// name is not found
				else {
					System.out.println(String.format("No player with name \"%s\" found.\n", name));
					continue;
				}
			}

			
			// search by power
			else if (option.toLowerCase().equals("p")) {
				System.out.print("Enter a power: ");
				String power = in.nextLine().trim();
				
				// search for the power
				int pos = Collections.binarySearch(playersByPower, new Player(0, "", power), new SortByPower());

				// power is found
				if(pos >= 0) {
					// add to results, and find if there are duplicates
					results.add(playersByPower.get(pos));
					findAllPower(playersByPower, results, pos, power);

					// only one result -> no need to order
					if (results.size() == 1) {
						orderByPower = true;
					}

					// searching power, so you can order by score or name if there's duplicates
					else {
						orderByScore = checkScoresUnequal(results);
						orderByName = checkNamesUnequal(results);

						// if scores (rankings) and powers are both not all equal, ask user what they want to order by
						if (orderByScore && orderByName) {
							boolean validInput = false;

							while (!validInput) {
								System.out.print("Sort by ranking or name? (R/N): ");
								String sortOption = in.nextLine().toLowerCase().trim();

								// sort by power
								if (sortOption.equals("r")) {
									orderByName = false;
									break;
								}

								// sort by ranking
								else if (sortOption.equals("n")) {
									orderByScore = false;
									break;
								}

								System.out.print("    Invalid input. ");
							}
						}	
					}
				}
				
				// power is not found
				else {
					System.out.println(String.format("No player with power \"%s\" found.\n", power));
					continue;
				}
			}
			
			else if (option.toLowerCase().equals("exit")) {
				in.close();
				System.exit(0);
			}

			
			
			// display the results
			if (orderByPower) {
				Collections.sort(results, new SortByPower());
				displayResults(results, playersByScore, numPlayers, false);
			}

			else if (orderByScore) {
				Collections.sort(results); // automatically sorts by score (natural sort order)
				displayResults(results, playersByScore, numPlayers, true); // need to "flip" results when displaying since ranking is reverse order of score ascending order
			}

			else if (orderByName) {
				Collections.sort(results, new SortByName());
				displayResults(results, playersByScore, numPlayers, false);
			}

		}

	}


	// Description: displays the player's information in proper format
	// Parameters: ArrayList<Player> of players needed to be displayed and playersByScore of all the players ordered by score, and int numPlayers for number of players, boolean reverse for whether order needs to be flipped (will need if sort by ranking, true if need to be flipped, false if not)
	// Return: void but displays the output in console
	public static void displayResults(ArrayList<Player> players, ArrayList<Player> playersByScore, int numPlayers, boolean reverse) {
		System.out.println();
		// order by score -> must go from highest score to lowest score which is descending order
		if (reverse) {
			for (int i = players.size()-1; i >= 0; i--) {
				Player player = players.get(i);
				System.out.println(String.format("Name: %s%nPower: %s%nScore: %d%nRanking: %d out of %d%n", player.getName(), player.getPower(), player.getScore(), findRank(playersByScore, player.getScore(), numPlayers), numPlayers));
			}
		}

		// order by name or power -> alphabetical, ascending order
		else {
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				System.out.println(String.format("Name: %s%nPower: %s%nScore: %d%nRanking: %d out of %d%n", player.getName(), player.getPower(), player.getScore(), findRank(playersByScore, player.getScore(), numPlayers), numPlayers));
			}
		}
	}

	// Description: finds the ranking of a score within the list of players' scores
	// Parameters: ArrayList<Player> playersByScore of all the players ordered by score, int score for score to be searched, int numPlayers for number of players in total
	// Return: int of the rank of that score
	public static int findRank(ArrayList<Player> playersByScore, int score, int numPlayers) {
		int pos = Collections.binarySearch(playersByScore, new Player(score, "", ""));

		for (int i = pos+1; i < numPlayers; i++) {
			if (playersByScore.get(i).getScore() != score) {
				return (numPlayers-i+1);
			}
		}
		return (numPlayers-(pos+1)+1);
	}

	// Description: gets ALL the players with same name after given a position of one of the players with that name
	// Parameters: ArrayList<Player> playersByName of all the players ordered by name alpha order, ArrayList for results is the list of all players with same name, int pos is one of the positions of the players with that name, String name is the name to be searched for
	// Return: void, but updates the ArrayList<Player> results to add ALL the players with same name
	public static void findAllName(ArrayList<Player> playersByName, ArrayList<Player> results, int pos, String name) {
		// find before current index
		for (int i = pos-1; i >= 0; i--) {
			if (playersByName.get(i).getName().toLowerCase().equals(name.toLowerCase())) 
				results.add(playersByName.get(i));
			else
				break;
		}

		// find after current index
		for (int i = pos+1; i < playersByName.size(); i++) {
			if (playersByName.get(i).getName().toLowerCase().equals(name.toLowerCase())) 
				results.add(playersByName.get(i));
			else
				break;
		}
	}

	// Description: gets ALL the players with same power after given a position of one of the players with that power
	// Parameters: ArrayList<Player> playersByPower of all the players ordered by powers, ArrayList for results is the list of all players with same power, int pos is one of the positions of the players with that power, String power is the power to be searched for
	// Return: void, but updates the ArrayList<Player> results to add ALL the players with same power
	public static void findAllPower(ArrayList<Player> playersByPower, ArrayList<Player> results, int pos, String power) {
		// find before current index
		for (int i = pos-1; i >= 0; i--) {
			if (playersByPower.get(i).getPower().toLowerCase().equals(power.toLowerCase())) 
				results.add(playersByPower.get(i));
			else
				break;
		}

		// find after current index
		for (int i = pos+1; i < playersByPower.size(); i++) {
			if (playersByPower.get(i).getPower().toLowerCase().equals(power.toLowerCase())) 
				results.add(playersByPower.get(i));
			else
				break;
		}
	}

	// Description: check if NOT all the players in a list has the same name
	// Parameters: ArrayList<Player> arr is the list of players to check names for
	// Return: boolean for whether all the names are NOT equal (true) or equal (false)
	public static boolean checkNamesUnequal(ArrayList<Player> arr) {
		for (int i = 0; i < arr.size()-1; i++) {
			// check if two adjacent players have different names
			if (!arr.get(i).getName().toLowerCase().equals(arr.get(i+1).getName().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	// Description: check if NOT all the players in a list has the same score
	// Parameters: ArrayList<Player> arr is the list of players to check scores for
	// Return: boolean for whether all the scores are NOT equal (true) or equal (false)
	public static boolean checkScoresUnequal(ArrayList<Player> arr) {
		for (int i = 0; i < arr.size()-1; i++) {
			// check if two adjacent players have different scores
			if (arr.get(i).getScore() != arr.get(i+1).getScore()) {
				return true;
			}
		}
		return false;
	}

	// Description: check if NOT all the players in a list has the same power
	// Parameters: ArrayList<Player> arr is the list of players to check powers for
	// Return: boolean for whether all the powers are NOT equal (true) or equal (false)
	public static boolean checkPowersUnequal(ArrayList<Player> arr) {
		for (int i = 0; i < arr.size()-1; i++) {
			// check if two adjacent players have different powers
			if (!arr.get(i).getPower().toLowerCase().equals(arr.get(i+1).getPower().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
