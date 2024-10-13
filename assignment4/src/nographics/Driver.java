// Lynn Tao
// April 21, 2024
// This NO GRAPHICS program allows you to view and modify your TV show list by show, season, and episode

package nographics;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class Driver {
	public static void main (String[] args) throws IOException {
		ArrayList<TVShow> tvShows = new ArrayList<>();

		addTVShow(tvShows, "bossbaby.txt"); // add bossbaby by default

		BufferedReader stdIn = new BufferedReader (new InputStreamReader (System.in));
		int mainMenuChoice, subMenuChoice;
		int episodeSortOption = 1; // 1 = ep #, 2 = title, 3 = time

		// main menu
		while(true) {
			mainMenuChoice = displayMenu (0, stdIn);

			// submenu #1
			if (mainMenuChoice == 1) {
				while(true) {
					try {
						subMenuChoice = displayMenu (1, stdIn);
						boolean valid = false;

						// display list of tv shows
						if (subMenuChoice == 1) {
							displayTVShows(tvShows);
						}

						// info on particular tv show
						else if (subMenuChoice == 2) {
							displayTVShows(tvShows);
							int showNo = askShowNo(stdIn, tvShows);

							if (showNo != 0)
								displayInfo(tvShows, showNo);
						}

						// add a tv show
						else if (subMenuChoice == 3) {

							System.out.print("Enter the file to read (no .txt, blank line to go back): ");

							while (!valid) {
								try {
									String input = stdIn.readLine();

									// if blank line -> go back
									if (input.length() == 0) {
										break;
									}
									String file = input + ".txt";
									addTVShow(tvShows, file);
									System.out.println(String.format("   Added %s!", tvShows.get(tvShows.size()-1).getTitle()));

									// sort tvShows
									Collections.sort(tvShows);

									valid = true;
								}
								catch (FileNotFoundException e) {
									System.out.print("   Invalid. Enter the file to read (no .txt, blank line to go back): ");
								}
							}
						}

						// remove a tv show
						else if (subMenuChoice == 4) {
							displayTVShows(tvShows);
							int showNo = askShowNo(stdIn, tvShows);

							// blank line -> go back to previous menu
							if (showNo != 0) {
								TVShow show = tvShows.get(showNo - 1);
								show.displaySeasons();
								int seasonPos = askSeasonNoRemove(stdIn, show);

								// blank line = remove all seasons
								if (seasonPos == -1) {
									System.out.println(String.format("   Removed %s!", tvShows.get(showNo-1).getTitle()));
									remove(tvShows, showNo);
								}

								// remove specific season
								else {
									System.out.println(String.format("   Removed season %d of %s!", tvShows.get(showNo-1).getSeasons().get(seasonPos).getSeasonNo(), tvShows.get(showNo-1).getTitle()));
									remove(tvShows, showNo, seasonPos);

									// if you removed the last season of the show, then remove the show entirely
									if (tvShows.get(showNo-1).getNumSeasons() == 0) {
										System.out.println("   No seasons left! Show has been removed completely.");
										remove(tvShows, showNo);
									}
								}

							}
						}

						// display show status
						else if (subMenuChoice == 5) {
							displayTVShows(tvShows);
							int showNo = askShowNo(stdIn, tvShows);


							// blank line -> go back to previous menu
							showStatus(tvShows, showNo);
						}

						// return to main menu
						else if (subMenuChoice == 6) {
							break;
						}
					}

					catch (NumberFormatException e) {
					}
				}
			}

			// submenu #2
			else if (mainMenuChoice == 2) {
				// no tv shows in list -> don' allow user to go into show-specific menu
				if (tvShows.size() == 0) {
					System.out.println("Add TV shows first!");
				}

				// at least one tv show exists
				else {
					displayTVShows(tvShows); // show list of tv shows
					int showNo = askShowNo(stdIn, tvShows); // chosen show

					Season season = new Season(0); // chosen season

					// blank line -> exit to previous menu
					if (showNo != -1) {
						TVShow show = tvShows.get(showNo - 1);

						// get a season number from user
						show.displaySeasons();
						Collections.sort(show.getSeasons());

						int seasonNo = askSeasonNo(stdIn, show);
						int seasonPos = Collections.binarySearch(show.getSeasons(), new Season(seasonNo));

						// season does not exist -> add new season
						if (seasonPos < 0) {
							System.out.println("Added " + seasonNo + " as new season!");
							season = new Season(seasonNo);
							show.addSeason(season);
							seasonPos = show.getNumSeasons()-1;
						}

						// if not blank line (blank line will exit to previous menu)
						else if (seasonNo != -1) {
							season = show.getSeasons().get(seasonPos);
						}

						while(true) {
							try {
								// display submenu #2
								subMenuChoice = displayMenu (2, stdIn);

								// display episodes
								if (subMenuChoice == 1) {

									if (season.getNumEps() == 0) 
										System.out.println("No episodes. Add episodes first!");

									else
										season.displayEpisodes(episodeSortOption);
								}

								// display info on specific episode
								else if (subMenuChoice == 2) {
									if (season.getNumEps() == 0) 
										System.out.println("No episodes. Add episodes first!");
									else {
										season.displayEpisodes(episodeSortOption);
										int epPos = askEpisodeNo(stdIn, season, "Enter an episode number (blank line to exit): ");

										// blank line -> exit to previous menu
										if (epPos != -1) {
											season.episodeInfo(epPos);
										}
									}

								}

								// watch an episode
								else if (subMenuChoice == 3) {

									if (season.getNumEps() == 0) 
										System.out.println("Episode list is empty. Add episodes first!");

									else {
										season.displayEpisodes(episodeSortOption);
										int epPos = askEpisodeNo(stdIn, season, "Enter an episode number (blank line to exit): ");

										// blank line -> exit to previous menu
										if (epPos != -1) {
											int epNo = season.getEpisodes().get(epPos).getEpisodeNo();
											// episode already watched
											if (season.getEpisodes().get(epPos).getWatched()){
												System.out.println(String.format("   Already watched episode %d!", epNo));
											}

											else {
												season.watchEpisode(epPos);
												System.out.println(String.format("   Episode %d set to watched!", epNo));
											}

										}
									}

								}

								// add an episode
								else if (subMenuChoice == 4) {
									season.displayEpisodes(episodeSortOption);
									String epTitle = "";

									// position where new episode is inserted
									int epNo = askEpisodeNoAdd(stdIn, season);
									

									if (epNo != -1) {
										System.out.print("Enter episode title to add (enter to return): ");
										epTitle = stdIn.readLine();

										if (epTitle.length() != 0) {
											Time time = askEpisodeTime(stdIn);

											if (time != null) {
												season.addEpisode(epTitle, epNo, time, show);
												System.out.println(String.format("Added Ep. %d: %s!", epNo, epTitle));
											}
										}
									}
								}

								// remove episode
								else if (subMenuChoice == 5) {
									if (season.getNumEps() == 0) 
										System.out.println("No episodes. Add episodes first!");

									else {
										boolean validOption = false;
										int removeOption = 0;
										System.out.print("Choose a remove option:\n   1. By episode #\n   2. By title\n   3. By range\n   4. By watched\n(1/2/3/4): ");

										while (!validOption) {
											try {
												String input = stdIn.readLine();

												if (input.length() == 0) {
													break;
												}

												removeOption = Integer.parseInt(input);

												if (removeOption < 1 || removeOption > 4) {
													throw new NumberFormatException();
												}
												validOption = true;
											}

											catch (NumberFormatException e) {
												System.out.print("   Invalid. (1/2/3/4): ");
											}
										}



										// by episode number
										if (removeOption == 1) {
											season.displayEpisodes(episodeSortOption);

											int epPos = askEpisodeNo(stdIn, season, "Enter an episode number (blank line to exit): ");
											season.removeEpisode(epPos, show);

											System.out.println("Removed episode!");
										}

										// by title
										else if (removeOption == 2) {
											season.displayEpisodes(episodeSortOption);

											boolean valid = false;
											int epPos = -1;

											System.out.print("Enter the title: ");

											while (!valid) {
												String title = stdIn.readLine().trim();

												if (title.length() == 0) {
													break;
												}

												Collections.sort(season.getEpisodes(), new SortByName());
												epPos = Collections.binarySearch(season.getEpisodes(), new Episode (title, 0, new Time(), 0), new SortByName());

												if (epPos >= 0) {
													valid = true;
												}
												else {
													System.out.print("  Invalid title. Enter the title: ");
												}
											}


											if (valid) {
												season.removeEpisode(epPos, show);												
												System.out.println("Removed episode!");
											}

										}

										// by range
										else if (removeOption == 3) {
											season.displayEpisodes(1); // sort by episode number

											int start = askEpisodeNo(stdIn, season, "Enter starting episode: ");

											if (start != -1) {
												int end = askEpisodeNo(stdIn, season, "Enter ending episode: ");

												if (end != -1 && start <= end) {					
													for (int i = start; i <= end; i++) {
														season.removeEpisode(start, show);
													}

													System.out.println("Removed episodes!");
												}
												else {
													System.out.println("   Invalid range.");
												}
											}

										}

										// by watched
										else if (removeOption == 4) {
											season.removeWatchedEpisodes(show);
											System.out.println("Removed watched episodes!");
										}

									}

								}

								// sort episodes
								else if (subMenuChoice == 6) {
									if (season.getNumEps() == 0) 
										System.out.println("No episodes. Add episodes first!");

									else {
										boolean validOption = false;
										int choice = 0;
										System.out.print("Choose a sort option:\n   1. By episode #\n   2. By title\n   3. By time\n(1/2/3):");

										while (!validOption) {
											try {
												String input = stdIn.readLine();

												if (input.length() == 0) {
													break;
												}

												choice = Integer.parseInt(input);

												if (choice < 1 || choice > 3) {
													throw new NumberFormatException();
												}

												validOption = true;
											}

											catch (NumberFormatException e) {
												System.out.println("   Invalid. (1/2/3): ");

											}
										}

										if (choice != 0) {
											episodeSortOption = choice;
											season.displayEpisodesWithTime(episodeSortOption);
										}
									}
								}

								// back to previous menu
								else if (subMenuChoice == 7) {
									if (season.getNumEps() == 0) {
										System.out.println("   No episodes in season. Season removed!");
										show.getSeasons().remove(seasonPos);
										show.setNumSeasons(show.getNumSeasons()-1);
										
										if (show.getNumSeasons() == 0) {
											System.out.println("   No seasons in show. Show removed!");
											tvShows.remove(showNo);
										}
									}
									break;
								}

								else {
									throw new NumberFormatException();
								}
							}
							catch (NumberFormatException e) {
							}
						}
					}

				}
			}

			// exit
			else if (mainMenuChoice == 3) {
				System.exit(0);
			}

			System.out.println();
			System.out.println();

		}
	}

	// Description: displays the main menu or submenu and allows user to enter their choice
	// Parameters: int menuNum represents main menu or submenu, stdIn is the BufferedReader to read in user's input
	// Return: int representing user's option choice
	public static int displayMenu (int menuNum, BufferedReader stdIn) throws IOException {

		if (menuNum == 0) {
			System.out.println ("----------  MAIN MENU  -----------");
			System.out.println ("1) Accessing your TV shows list");
			System.out.println ("2) Accessing within a particular TV show");
			System.out.println ("3) Exit");
			System.out.println ("----------------------------------");
		}
		else if (menuNum == 1) {
			System.out.println ("\n---------  SUB-MENU #1  ----------");
			System.out.println ("1) Display a list of all your TV shows"); // Just the show titles, numbered in order
			System.out.println ("2) Display info on a particular TV show"); 
			System.out.println ("3) Add a TV show"); // Adds a show by reading from input file
			System.out.println ("4) Remove (show or season)");
			System.out.println ("5) Show status");
			System.out.println ("6) Return back to main menu.");
			System.out.println ("----------------------------------");
		}
		else {
			System.out.println ("\n---------  SUB-MENU #2  ----------");
			System.out.println ("1) Display all episodes (in the last sorted order) ");
			System.out.println ("2) Display info on a particular episode ");
			System.out.println ("3) Watch an episode");
			System.out.println ("4) Add an episode");
			System.out.println ("5) Remove episode(s) (4 options)");
			System.out.println ("6) Sort episodes (3 options)");
			System.out.println ("7) Return back to main menu");
			System.out.println ("----------------------------------");
		}

		System.out.print ("\n\nPlease enter your choice:  ");

		boolean valid = false;
		int choice = 0;

		while (!valid) {
			try {
				choice = Integer.parseInt (stdIn.readLine ());

				// check if choice is valid
				if ((menuNum == 0 && choice <= 3 && choice >= 1) 
						|| (menuNum == 1 && choice <= 6 && choice >= 1) 
						|| (menuNum == 2 && choice <= 7 && choice >= 1))
					valid = true;

				// if invalid -> throw exception to re-ask
				else
					throw new NumberFormatException();
			}

			// invalid choice -> re-ask
			catch (NumberFormatException e) {
				System.out.print("   Invalid. Please enter your choice: ");
			}

		}
		return choice;
	}

	// Description: prints out all tv shows neatly
	// Parameters: ArrayList<TVShow> tvShows is the list of tv shows
	// Return: void, but prints out the shows in console
	public static void displayTVShows (ArrayList<TVShow> tvShows) {
		System.out.println("\n  ============ TV Show List ============");

		// no tv shows -> say so
		if(tvShows.size() == 0) {
			System.out.println("  TV show list is empty.");
		}

		// has tv shows -> number each show and display name
		else {
			int count = 1;
			for (TVShow show : tvShows) {
				System.out.println("  " + count + ".    " + show);
				count++;
			}
		}
		System.out.println("  ======================================");

	}

	// Description: prints out information about a specific tvShow neatly
	// Parameters: ArrayList<TVShow> tvShows is the list of tv shows, int showNo is the tv show # in list to be displayed
	// Return: void, but prints out the information in console
	public static void displayInfo (ArrayList<TVShow> tvShows, int showNo) {
		TVShow show = tvShows.get(showNo - 1);
		System.out.println("\n  ====== SHOW INFO =======");
		System.out.println(String.format("  Title: %s", show.getTitle()));
		System.out.println(String.format("  Genre: %s", show.getGenre()));
		System.out.println(String.format("  Seasons: %d", show.getNumSeasons()));
		System.out.println(String.format("  Episodes: %d", show.getNumEps()));
		System.out.println(String.format("  Total Time: %s", show.getShowTime()));
		System.out.println("  ========================");
	}

	// Description: allows user to add a new tv show to the list
	// Parameters: ArrayList<TVShow> tvShows is the existing list of tv shows, String fileName is the file containing information on new show
	// Return: void, but modifies the tvShows list to add new show
	public static void addTVShow (ArrayList<TVShow> tvShows, String fileName) throws IOException {
		BufferedReader inFile = new BufferedReader(new FileReader (fileName));
		String title = inFile.readLine();
		String genre = inFile.readLine();	
		TVShow show = new TVShow (title, genre);
		tvShows.add(show);

		String line;

		// loop season by season
		while ((line = inFile.readLine()) != null) {
			// get season number and add to show
			int seasonNo = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
			Season season = new Season(seasonNo);
			show.addSeason(season);

			// get number of episodes
			int numEpisodes = Integer.parseInt(inFile.readLine());

			// loop episode by episode
			for (int i = 0; i < numEpisodes; i++) {
				line = inFile.readLine();

				// get episode number, title, and time and add to season
				int episodeNo = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
				String episodeTitle = inFile.readLine();
				Time episodeTime = new Time(inFile.readLine());
				season.addEpisode(new Episode(episodeTitle, episodeNo, episodeTime, season.getSeasonNo()));
			}

			// sort episodes
			Collections.sort(season.getEpisodes());

			// update total time in season
			season.updateTime();
		}

		inFile.close();

		// sort seasons in tv show
		Collections.sort(show.getSeasons());

		// update number of episodes and total time in show
		show.updateNumEps();
		show.updateTime();
	}

	// Description: removes an entire tv show from list
	// Parameters: ArrayList<TVShow> tvShows is the existing list of tv shows, int showNo is the position of show to be removed
	// Return: void, but modifies tvShows to remove that tv show
	public static void remove (ArrayList<TVShow> tvShows, int showNo) {		
		// remove show from tv shows list
		tvShows.remove(showNo-1);
	}

	// Description: removes a specific season of a tv show from list
	// Parameters: ArrayList<TVShow> tvShows is the existing list of tv shows, int showNo is the position of show to be removed, int seasonPos is position of season to be removed
	// Return: void, but modifies the specific show in tvShows list to remove that season
	public static void remove (ArrayList<TVShow> tvShows, int showNo, int seasonPos) {
		TVShow show = tvShows.get(showNo-1);
		Time t = show.getSeasons().get(seasonPos).getSeasonTime();
		show.getSeasons().remove(seasonPos);

		// update number of episodes and total show time
		show.updateNumEps();
		show.subtractTime(t);
		show.setNumSeasons(show.getNumSeasons()-1);
	}

	// Description: display show status
	// Parameters: ArrayList<TVShow> tvShows is the existing list of tv shows, int showNo is the position of show to view
	// Return: void, but prints out description of show
	public static void showStatus (ArrayList<TVShow> tvShows, int showNo) {
		if (showNo != 0) {
			System.out.println("\n  ======= SHOW INFO =======");

			TVShow show = tvShows.get(showNo - 1);
			int totalUnwatched = 0;
			int totalWatchedSeasons = 0;

			for (Season s : show.getSeasons()) {
				if (s.getIsWatched()) {
					totalWatchedSeasons++;
				}
				totalUnwatched += s.getUnwatchedEps();
				System.out.println(String.format("   Season %d: %d/%d watched", s.getSeasonNo(), s.getNumEps() - s.getUnwatchedEps(), s.getNumEps()));
			}

			// FIX THIS
			System.out.println(String.format("\n   %d watched seasons in total", totalWatchedSeasons));
			System.out.println(String.format("   %d unwatched episodes in total", totalUnwatched));
			System.out.println("\n  =========================");
		}
	}

	// Description: asks user for a show number and makes sure input is valid
	// Parameters: BufferedReader stdIn gets user's input, ArrayList<TVShow> tvShows is the list of shows
	// Return: int representing user's show choice
	public static int askShowNo(BufferedReader stdIn, ArrayList<TVShow> tvShows) throws IOException {
		boolean valid = false;
		System.out.print("Enter the show number (blank line to exit): ");
		int showNo = 0;

		while (!valid) {
			try {
				// get user's input
				String input = stdIn.readLine();

				// blank line -> return -1
				if (input.length() == 0) {
					return -1;
				}

				// convert input to integer
				showNo = Integer.parseInt(input);

				// invalid show number -> throw exception
				if (showNo > tvShows.size() || showNo <= 0) {
					showNo = 0;
					throw new NumberFormatException();
				}
				valid = true;
			}

			// re-ask if invalid
			catch (NumberFormatException e) {
				System.out.print("   Invalid. Enter the show number (blank line to exit): ");

			}

		}

		return showNo;
	}

	// Description: asks user for a season number and makes sure input is valid
	// Parameters: BufferedReader stdIn gets user's input, TVShow show is the tv show that season is from
	// Return: int representing user's season choice
	public static int askSeasonNo(BufferedReader stdIn, TVShow show) throws IOException {
		boolean valid = false;
		System.out.print("Enter the season number (blank line to exit): ");
		int seasonNo = 0;

		while (!valid) {
			try {
				// get user's input
				String input = stdIn.readLine();

				// blank line -> return -1
				if (input.length() == 0) {
					return -1;
				}

				// convert season number to integer
				seasonNo = Integer.parseInt(input);

				if (seasonNo <= 0) {
					throw new NumberFormatException();
				}

				valid = true;
			}

			// invalid input -> re-ask
			catch (NumberFormatException e) {
				System.out.print("   Invalid. Enter the season number (blank line to exit): ");
			}

		}

		return seasonNo;
	}


	// Description: asks user for a season number and makes sure input is valid, called specifically when you want to remove a season (same method as above but output is different)
	// Parameters: BufferedReader stdIn gets user's input, TVShow show is the tv show that season is from
	// Return: int representing user's season choice
	public static int askSeasonNoRemove(BufferedReader stdIn, TVShow show) throws IOException {
		boolean valid = false;
		System.out.print("Enter the season number (blank line to remove whole show): ");
		int seasonPos = 0;
		Collections.sort(show.getSeasons());

		while (!valid) {
			try {
				// get user's input
				String input = stdIn.readLine();

				// blank line -> return 0
				if (input.length() == 0) {
					return -1;
				}

				// convert season number to integer
				int seasonNo = Integer.parseInt(input);

				seasonPos = Collections.binarySearch(show.getSeasons(), new Season(seasonNo));
				// season does not exist -> throw exception to re-ask
				if (seasonPos < 0) {
					seasonPos = -1;
					throw new NumberFormatException();
				}
				valid = true;
			}

			// invalid input -> re-ask
			catch (NumberFormatException e) {
				System.out.print("   Invalid. Enter the season number (blank line to remove all): ");

			}

		}

		return seasonPos;
	}

	// Description: asks user for a episode number and makes sure input is valid
	// Parameters: BufferedReader stdIn gets user's input, Season season is the season that episode is from
	// Return: int representing position of user's episode choice in the list of episodes (-1 if go back)
	public static int askEpisodeNo(BufferedReader stdIn, Season season, String prompt) throws IOException {
		boolean valid = false;
		int epPos = -1;
		Collections.sort(season.getEpisodes());
		
		System.out.print(prompt);

		while (!valid) {
			try {
				// ask for user's input
				String input = stdIn.readLine();

				// blank line -> return -1
				if (input.length() == 0) {
					return -1;
				}

				// convert input to integer
				int epNo = Integer.parseInt(input);

				// if episode does not exist -> throw exception to re-ask
				epPos = Collections.binarySearch(season.getEpisodes(), new Episode("", epNo, new Time(), 0));
				if (epPos < 0) {
					epPos = -1;
					throw new NumberFormatException();
				}
				valid = true;
			}

			// invalid input -> re-ask
			catch (NumberFormatException e) {
				System.out.print("   Invalid. " + prompt);
			}
		}

		return epPos;
	}

	// Description: asks user for a episode number to add
	// Parameters: BufferedReader stdIn gets user's input, Season season is the season that episode should be added in
	// Return: int representing episode number that user wants to add
	public static int askEpisodeNoAdd(BufferedReader stdIn, Season season) throws IOException {
		boolean valid = false;
		int epNo = 0;
		int epPos = 0;
		Collections.sort(season.getEpisodes());

		System.out.print("Enter an episode number (blank line to exit): ");

		while (!valid) {
			try {
				// ask for user's input
				String input = stdIn.readLine();

				// blank line -> return -1
				if (input.length() == 0) {
					return -1;
				}

				// convert input to integer
				epNo = Integer.parseInt(input);

				if (epNo <= 0) {
					throw new NumberFormatException();
				}

				// if episode already exists -> throw exception to re-ask
				epPos = Collections.binarySearch(season.getEpisodes(), new Episode("", epNo, new Time(), 0));
				if (epPos >= 0) {
					throw new NumberFormatException();
				}
				
				valid = true;
			}

			// invalid input -> re-ask
			catch (NumberFormatException e) {
				System.out.print("   Invalid. Enter an episode number (blank line to exit): ");
			}
		}

		return epNo;
	}


	// Description: asks user for a Time of episode
	// Parameters: BufferedReader stdIn gets user's input
	// Return: Time object representing the time that user entered
	public static Time askEpisodeTime(BufferedReader stdIn) throws IOException {
		boolean valid = false;
		Time t = new Time();
		System.out.print("Enter the time in hh:mm:ss format (blank line to exit): ");

		while (!valid) {
			try {
				// ask for user's input
				String time = stdIn.readLine();

				// blank line -> return -1
				if (time.length() == 0) {
					return null;
				}

				if (time.indexOf(":") == time.lastIndexOf(":"))
					throw new NumberFormatException();

				int hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
				int minutes = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.lastIndexOf(":")));
				int seconds = Integer.parseInt(time.substring(time.lastIndexOf(":") + 1));

				if (hours < 0) {
					throw new NumberFormatException();
				}

				if (minutes < 0 || minutes > 59) {
					throw new NumberFormatException();
				}

				if (seconds < 0 || seconds > 59) {
					throw new NumberFormatException();
				}

				t.setHours(hours);
				t.setMinutes(minutes);
				t.setSeconds(seconds);

				valid = true;
			}

			// invalid input -> re-ask
			catch (NumberFormatException e) {
				System.out.print("   Invalid. Enter the time in hh:mm:ss format (blank line to exit): ");
			}
		}

		return t;
	}
}