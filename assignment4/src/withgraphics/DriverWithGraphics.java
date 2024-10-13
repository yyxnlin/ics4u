// Lynn Tao
// April 21, 2024
// This program contains the graphics that allows you to view and modify your TV show list by show, season, and episode

package withgraphics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class DriverWithGraphics implements ActionListener {
	static ArrayList<TVShow> tvShows;
	JFrame frame;
	JPanel panel;

	JComboBox <String> submenu1Option, submenu2Option;
	JLabel optionLabel, latinName;
	JTextArea mainText;
	JButton backToMain;
	JButton goToSubmenu1Button, goToSubmenu2Button, exitButton;

	Season currentSeason;
	TVShow currentShow;
	int showPos, seasonPos, episodeSortOption;
	Font NORMAL_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);
	Font SMALL_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 14);

	// constructor
	public DriverWithGraphics() {
		frame = new JFrame ("TV Shows!");
		frame.setPreferredSize(new Dimension(900, 500));
		frame.setLocation(200, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel ();
		panel.setLayout(new FlowLayout());
		panel.setBorder (BorderFactory.createEmptyBorder (10,10,10,10));	
	}

	// Description: controls what happens when user clicks something
	// Parameters: ActionEvent event is the action that user performed
	// Return: void, but calls diff methods based on what user does
	public void actionPerformed(ActionEvent event) {
		String eventName = event.getActionCommand();

		// show submenu 1
		if (eventName == "submenu 1") {
			showSubmenu1();
		}

		// selected option in submenu
		else if (eventName == "submenu 1 option") {
			JComboBox comboBox = (JComboBox) event.getSource ();
			String submenu1Option = (String)comboBox.getSelectedItem ();

			showSubmenu1Options(submenu1Option);
		}

		// show submenu 2
		else if (eventName == "submenu 2") {
			// must have at least one tv show
			if (tvShows.size() != 0) {
				int temp = getShowNo();
				Season season = new Season(0); // chosen season

				// valid show is inputed
				if (temp != -1) {
					showPos = temp;
					currentShow = tvShows.get(showPos);

					Collections.sort(currentShow.getSeasons());

					int seasonNo = enterSeason(showPos);

					// if not blank line (blank line will exit to previous menu)
					if (seasonNo != -1) {
						seasonPos = Collections.binarySearch(currentShow.getSeasons(), new Season(seasonNo));

						// season exists
						if (seasonPos >= 0)
							currentSeason = currentShow.getSeasons().get(seasonPos);

						// season does not exist -> add new season
						else {
							JOptionPane.showMessageDialog(frame, "Added season " + seasonNo + " as new season!");
							season = new Season(seasonNo);
							currentShow.addSeason(season);
							seasonPos = currentShow.getNumSeasons()-1;
							currentSeason = season;
						}

						showSubmenu2();

					}
				}
			}

			// no tv shows -> tell user to add
			else {
				JOptionPane.showMessageDialog(frame, "No TV shows! Add a TV show first.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		// option in submenu 2 selected
		else if (eventName == "submenu 2 option") {
			JComboBox comboBox = (JComboBox) event.getSource();
			String submenu2Option = (String)comboBox.getSelectedItem ();

			showSubmenu2Options(submenu2Option);
		}

		// back button clicked from submenu 1
		else if (eventName == "back to main from submenu 1") {
			showMainMenu();
		}

		// back button clicked from submenu 2
		else if (eventName == "back to main from submenu 2") {
			// no episodes left in season -> remove season
			if(currentSeason.getNumEps() == 0) {
				JOptionPane.showMessageDialog(frame, "No episodes in season. Season removed!");
				currentShow.getSeasons().remove(seasonPos);
				currentShow.setNumSeasons(currentShow.getNumSeasons()-1);

				// no season left in show -> remove show
				if (currentShow.getNumSeasons() == 0) {
					JOptionPane.showMessageDialog(frame, "No seasons in show. Show removed!");
					tvShows.remove(showPos);
				}
			}
			showMainMenu();

		}

		// exit button clicked
		else if (eventName == "exit") {
			System.exit(0);
		}

	}

	public static void main (String[] args) throws IOException {
		DriverWithGraphics d = new DriverWithGraphics();

		// make graphics prettier
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} 

		catch (Exception e) {

		}

		tvShows = new ArrayList<>();
		d.showMainMenu(); // main menu on start
		d.episodeSortOption = 1; // default sort option by episode number
		d.frame.add(d.panel);

		//		addTVShow("bossbaby.txt"); // add bossbaby by default

	}

	// Description: displays graphics for main menu
	public void showMainMenu() {
		panel.removeAll();
		panel.repaint();

		JLabel label = new JLabel("Select an option!");
		label.setPreferredSize(new Dimension (600, 100));
		label.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));

		// three buttons
		goToSubmenu1Button = new JButton("Submenu 1: Accessing \nyour TV Shows list");
		goToSubmenu2Button = new JButton("Submenu 2: Accessing within particular TV show");
		exitButton = new JButton("Exit");

		goToSubmenu1Button.setPreferredSize(new Dimension (600, 100));
		goToSubmenu1Button.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		goToSubmenu2Button.setPreferredSize(new Dimension (600, 100));
		goToSubmenu2Button.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		exitButton.setPreferredSize(new Dimension (600, 100));
		exitButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));

		goToSubmenu1Button.addActionListener(this);
		goToSubmenu1Button.setActionCommand("submenu 1");
		goToSubmenu2Button.addActionListener(this);
		goToSubmenu2Button.setActionCommand("submenu 2");
		exitButton.addActionListener(this);
		exitButton.setActionCommand("exit");

		// add to panel
		panel.add(label);
		panel.add(goToSubmenu1Button);
		panel.add(goToSubmenu2Button);
		panel.add(exitButton);

		frame.pack();
		frame.setVisible(true);
	}

	// Description: displays graphics for submenu 1
	// Parameters: none
	// Return: none (shows graphics)
	public void showSubmenu1() {
		panel.removeAll();
		panel.repaint();

		// dropdown options
		optionLabel = new JLabel ("Select an option:");
		optionLabel.setAlignmentX (JLabel.LEFT_ALIGNMENT);
		String[] submenu1Options = {
				"Display list of TV shows", 
				"Display information on a particular TV show", 
				"Add a TV show", 
				"Remove (2 options)", 
				"Show status"
		};

		// dropdown combobox
		submenu1Option = new JComboBox<String> (submenu1Options);
		submenu1Option.setSize(new Dimension(400, 100));
		submenu1Option.setAlignmentX (JComboBox.LEFT_ALIGNMENT);
		submenu1Option.setSelectedIndex (0);
		submenu1Option.addActionListener (this);
		submenu1Option.setActionCommand("submenu 1 option");
		submenu1Option.setSelectedItem(null);

		// back button
		backToMain = new JButton("Back");
		backToMain.addActionListener(this);
		backToMain.setActionCommand("back to main from submenu 1");

		// main text box
		mainText = new JTextArea();
		mainText.setLineWrap(false);
		mainText.setEditable(false);
		mainText.setFont(NORMAL_FONT);

		mainText.setText("Select an option to continue.");

		// text box scrollable
		JScrollPane scroll = new JScrollPane (mainText);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(600, 400));

		// add to panel and frame
		panel.add (backToMain);
		panel.add (submenu1Option);
		panel.add(scroll);


		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	// Description: displays graphics when an option in submenu 1 is selected
	// Parameters: String option is the option that is selected
	// Return: none (displays graphics)
	public void showSubmenu1Options(String option) {
		if (option == "Display list of TV shows") {
			mainText.setText(displayTVShows());
		}

		else if (option == "Display information on a particular TV show") {
			if (tvShows.size() == 0) 
				JOptionPane.showMessageDialog(frame, "No TV shows! Add TV shows first.");

			else {
				int showNo = getShowNo();

				// not cancel/x button
				if (showNo != -1)
					mainText.setText(displayInfo(showNo));
			}
		}

		else if (option == "Add a TV show") {
			mainText.setText("Select an option to continue.");

			String fileName = showAddShow();

			// not cancel/x button
			if (fileName != null) {
				try {
					addTVShow(fileName + ".txt");
				} 
				catch (IOException e) {
				}
			}
		}


		else if (option == "Remove (2 options)") {
			if (tvShows.size() == 0) 
				JOptionPane.showMessageDialog(frame, "No TV shows! Add TV shows first.");

			else {
				mainText.setText("Select an option to continue.");

				// first number is selected show, second is selected season 
				int[] removeOptions = showRemoveShow();

				// not cancel/x button
				if (removeOptions != null) {
					int showPos = removeOptions[0];
					int seasonPos = removeOptions[1];

					// no season selected -> remove whole show
					if (seasonPos == -1) {
						remove(showPos);
					}

					// season selected -> remove season
					else {
						remove(showPos, seasonPos);

						// if no seasons remaining, remove whole show
						if (tvShows.get(showPos).getNumSeasons() == 0) {
							JOptionPane.showMessageDialog(frame, "No seasons left. Removed TV show!");
							remove(showPos);
						}
					}
				}
			}
		}


		else if (option == "Show status") {
			if (tvShows.size() == 0) 
				JOptionPane.showMessageDialog(frame, "No TV shows! Add TV shows first.");

			else {
				int showNo = getShowNo();

				// not cancel/x -> display the show status
				if (showNo != -1)
					mainText.setText(showStatus(showNo));
			}
		}
	}

	// Description: displays graphics for submenu 2
	// Parameters: none
	// Return: none (shows graphics)
	public void showSubmenu2() {
		panel.removeAll();
		panel.repaint();

		// dropdown options
		optionLabel = new JLabel ("Select an option:");
		optionLabel.setAlignmentX (JLabel.LEFT_ALIGNMENT);
		String[] submenu2Options = {
				"Display all episodes", 
				"Display information on particular episode",
				"Watch an episode", 
				"Add an episode", 
				"Remove episode (4 options)", 
				"Sort episodes (3 options)"
		};

		// dropdown combobox
		submenu2Option = new JComboBox<String> (submenu2Options);
		submenu2Option.setSize(new Dimension(400, 100));
		submenu2Option.setAlignmentX (JComboBox.LEFT_ALIGNMENT);
		submenu2Option.setSelectedIndex (0);
		submenu2Option.addActionListener (this);
		submenu2Option.setActionCommand("submenu 2 option");
		submenu2Option.setSelectedItem(null);

		// back button
		backToMain = new JButton("Back");
		backToMain.addActionListener(this);
		backToMain.setActionCommand("back to main from submenu 2");

		// main text box
		mainText = new JTextArea();
		mainText.setLineWrap(false);
		mainText.setEditable(false);
		mainText.setFont(NORMAL_FONT);

		mainText.setText("Select an option to continue.");

		// make text box scrollable
		JScrollPane scroll = new JScrollPane (mainText);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(600, 400));

		// add to panel
		panel.add (backToMain);
		panel.add (submenu2Option);
		panel.add(scroll);
	}

	// Description: displays graphics when an option in submenu 2 is selected
	// Parameters: String option is the option that is selected
	// Return: none (displays graphics)
	public void showSubmenu2Options(String option) {
		if (option == "Display all episodes") {
			// no episodes -> tell them to add
			if (currentSeason.getNumEps() == 0) 
				JOptionPane.showMessageDialog(frame, "No episodes. Add episodes first!");

			// display episode info in last sorted order
			else {
				mainText.setText(currentSeason.displayEpisodes(episodeSortOption));
			}
		}

		else if (option == "Display information on particular episode") {
			// no episodes -> tell them to add
			if (currentSeason.getNumEps() == 0) 
				JOptionPane.showMessageDialog(frame, "No episodes. Add episodes first!");

			// display information on episode
			else {
				int epPos = askEpisodeNo(); // user chooses an episode

				// blank line -> exit to previous menu
				if (epPos != -1) {
					mainText.setText(currentSeason.episodeInfo(epPos));
				}
			}

		}

		else if (option == "Watch an episode") {
			// no episodes -> tell them to add
			if (currentSeason.getNumEps() == 0) 
				JOptionPane.showMessageDialog(frame, "No episodes. Add episodes first!");

			else {
				mainText.setText("Select an option to continue.");

				int epPos = askEpisodeNo(); // user chooses an episode

				// blank line -> exit to previous menu
				if (epPos != -1) {
					int epNo = currentSeason.getEpisodes().get(epPos).getEpisodeNo();

					// episode already watched
					if (currentSeason.getEpisodes().get(epPos).getWatched()){
						JOptionPane.showMessageDialog(frame, String.format("Already watched episode %d!", epNo));
					}

					// episode not watched -> set to watched
					else {
						currentSeason.watchEpisode(epPos);
						JOptionPane.showMessageDialog(frame, String.format("Episode %d set to watched!", epNo));
					}
				}
			}
		}


		else if (option == "Add an episode") {
			mainText.setText("Select an option to continue.");

			Episode episode = enterAddEpisode(); // user enters an episode

			// not cancel/x
			if (episode != null) {
				// add episode to seasom
				currentSeason.addEpisode(episode);

				// update show time/episodes
				currentShow.addTime(episode.getTime());
				currentShow.setNumEps(currentShow.getNumEps()+1);

				JOptionPane.showMessageDialog(frame, String.format("Added Ep. %d: %s!", episode.getEpisodeNo(), episode.getTitle()));
			}

		}


		else if (option == "Remove episode (4 options)") {
			// no episodes -> tell them to add
			if (currentSeason.getNumEps() == 0) 
				JOptionPane.showMessageDialog(frame, "No episodes. Add episodes first!");

			else {
				mainText.setText("Select an option to continue.");

				int removeOption = askRemoveOption(); // how user wants to remove

				// by episode number
				if (removeOption == 1) {
					Collections.sort(currentSeason.getEpisodes()); // sort by ep #

					int epPos = enterEpisodeNo(); // position of episode to be deleted

					// not cancel/x
					if (epPos != -1) {
						// update number of episodes and total show time
						currentShow.setNumEps(currentShow.getNumEps()-1);
						currentShow.subtractTime(currentSeason.getEpisodes().get(epPos).getTime());

						// remove episode
						currentSeason.removeEpisode(epPos);						

						JOptionPane.showMessageDialog(frame, "Removed episode!");
					}

				}

				// by title
				else if (removeOption == 2) {
					Collections.sort(currentSeason.getEpisodes(), new SortByName()); // sort by title

					int epPos = enterEpisodeTitle(); // position of episode to be deleted

					// not cancel/x
					if (epPos != -1) {
						// update number of episodes and total show time
						currentShow.setNumEps(currentShow.getNumEps()-1);
						currentShow.subtractTime(currentSeason.getEpisodes().get(epPos).getTime());

						// remove episode
						currentSeason.removeEpisode(epPos);

						JOptionPane.showMessageDialog(frame, "Removed episode!");
					}

				}

				// by range
				else if (removeOption == 3) {
					Collections.sort(currentSeason.getEpisodes()); // sort by ep #

					int[] epPos = enterTwoEpisodeNo(); // start and end bounds of episodes to delete

					// not cancel/x
					if (epPos != null) {
						int start = epPos[0];
						int end = epPos[1];

						for (int i = start; i <= end; i++) {
							// update number of episodes and total show time
							currentShow.setNumEps(currentShow.getNumEps()-1);
							currentShow.subtractTime(currentSeason.getEpisodes().get(start).getTime());

							// remove episode
							currentSeason.removeEpisode(start);
						}

						JOptionPane.showMessageDialog(frame, "Removed episodes!");
					}

				}

				// by watched
				else if (removeOption == 4) {
					Collections.sort(currentSeason.getEpisodes()); // sort by ep #

					Time watchedTime = currentSeason.getWatchedTime(); // total time of season
					int numEps = currentSeason.getNumEps() - currentSeason.getUnwatchedEps(); // total episodes in season

					// update number of episodes and total show time
					currentShow.setNumEps(currentShow.getNumEps() - numEps);
					currentShow.subtractTime(watchedTime);

					// remove episode
					currentSeason.removeWatchedEpisodes();

					JOptionPane.showMessageDialog(frame, "Removed watched episodes!");
				}

			}
		}

		else if (option == "Sort episodes (3 options)") {
			// no episodes -> tell them to add
			if (currentSeason.getNumEps() == 0) 
				JOptionPane.showMessageDialog(frame, "No episodes. Add episodes first!");

			else {
				int temp = askSortOption(); // how user wants to sort

				// not cancel/x
				if (temp != -1) {
					episodeSortOption = temp;
					mainText.setText(currentSeason.displayEpisodes(episodeSortOption)); // re-display list
				}
			}
		}
	}

	// Description: displays graphics for adding a new show
	// Parameters: none
	// Return: String of a valid filename without .txt, or null if cancelled
	public String showAddShow() {
		String fileName;

		while (true) {
			try {
				// input box for file name
				fileName = JOptionPane.showInputDialog (panel, "Type in the file name (no .txt)");

				// cancel
				if (fileName == null) {
					return null;
				}

				// no cancel
				else {
					fileName = fileName.trim();
				}

				// try reading file
				BufferedReader inFile = new BufferedReader(new FileReader (fileName + ".txt"));
				inFile.close();

				// able to read -> success message
				JOptionPane.showMessageDialog (panel, "Added!", "Success", JOptionPane.INFORMATION_MESSAGE);
				return fileName;

			} 

			// unable to read -> error
			catch (IOException e) {
				JOptionPane.showMessageDialog (panel, "File does not exist!", "Error", JOptionPane.WARNING_MESSAGE);
			}

		}

	}

	// Description: allows user to select a show
	// Parameters: none
	// Return: int representing position of show
	public int getShowNo() {
		int noShows = tvShows.size();

		JPanel radioPanel = new JPanel (); 
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		radioPanel.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Choose a TV show"));
		radioPanel.setLayout (new GridLayout (Math.max(8, noShows), 1));

		// Create a group of radio buttons to add to the Panel
		ButtonGroup buttonGroup = new ButtonGroup ();
		JRadioButton[] buttonList = new JRadioButton [noShows];

		// Create and add each radio button to the panel
		for (int i = 0 ; i < noShows; i++) {
			buttonList[i] = new JRadioButton (tvShows.get(i).getTitle());
			buttonGroup.add (buttonList [i]);
			radioPanel.add (buttonList [i]);
		}

		// make radiopanel scrollable
		JScrollPane scroll = new JScrollPane (radioPanel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(300, 200));

		// if ok/cancel button is selected
		// loop until option is selected
		while (true) {
			// Show a dialog with the panel attached
			int choice = JOptionPane.showConfirmDialog (panel, scroll,
					"Show Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			// ok button pressed
			if (choice == JOptionPane.OK_OPTION) {
				for (int index = 0 ; index < buttonList.length ; index++) {
					// option is selected -> break loop
					if (buttonList[index].isSelected ()) {
						return index;
					}
				}			
			}

			// cancel button pressed -> return -1
			else {
				return -1;
			}

		}
	}

	// Description: allows user to enter a season number
	// Parameters: none
	// Return: int representing season number
	public int enterSeason(int selectedShow) {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		mainPanel.setPreferredSize(new Dimension (400, 300));
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);


		String text = "";
		for (int i = 0; i < tvShows.get(selectedShow).getNumSeasons(); i++) {
			text += "Season " + tvShows.get(selectedShow).getSeasons().get(i).getSeasonNo() + "\n";
		}

		// list of existing seasons
		JTextArea listField = new JTextArea(text);
		listField.setFont(SMALL_FONT);
		listField.setEditable(false);

		// make list scrollable
		JScrollPane scrollPanel = new JScrollPane (listField);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));
		listField.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Existing seasons:"));

		// label for entering season number
		JLabel label = new JLabel ("Enter season number: ");

		// text field for entering season number
		JTextField seasonField = new JTextField(4);

		mainPanel.add(scrollPanel);
		mainPanel.add(label);
		mainPanel.add(seasonField);


		while (true) {
			int choice = JOptionPane.showConfirmDialog (panel, mainPanel,
					"Season Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			// ok option -> return input as int
			if (choice == JOptionPane.OK_OPTION) {	
				try {
					int result = Integer.parseInt(seasonField.getText().trim());
					return result;
				}
				catch (NumberFormatException e) {

				}
			}

			// cancel/x button -> return -1
			else {	
				return -1;
			}
		}
	}

	// Description: allows user to select show/season to remove
	// Parameters: none
	// Return: int[] with first value representing position of show, second value representing position of season (-1 if remove all seasons)
	public int[] showRemoveShow() {
		int selectedShow = 0;
		int selectedSeason = 0;
		int noShows = tvShows.size();


		// set up jpanel
		JPanel radioPanel = new JPanel (); 
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		radioPanel.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Choose a TV show"));
		radioPanel.setLayout (new GridLayout (Math.max(8, noShows), 1));


		// SELECTING A SHOW
		// Create a group of radio buttons to add to the panel
		ButtonGroup showsGroup = new ButtonGroup ();
		JRadioButton[] showsList = new JRadioButton [noShows];

		// Create and add each radio button (tv show) to the panel
		for (int i = 0 ; i < tvShows.size(); i++) {
			showsList [i] = new JRadioButton (tvShows.get(i).getTitle());
			showsGroup.add (showsList [i]);
			radioPanel.add (showsList [i]);
		}

		// make radio buttons scrollable
		JScrollPane scrollPanel = new JScrollPane (radioPanel);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));

		// Show a dialog with the panel attached
		int choice = JOptionPane.showConfirmDialog (panel, scrollPanel,
				"Show Options",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.DEFAULT_OPTION);


		// if ok/cancel button is selected
		// loop until option is selected
		boolean found = false;

		while (!found) {
			// ok button pressed
			if (choice == JOptionPane.OK_OPTION) {
				for (int index = 0 ; index < showsList.length ; index++) {
					// option is selected -> break loop
					if (showsList[index].isSelected ()) {
						selectedShow = index;
						found = true;
						break;
					}
				}	
			}

			// cancel button pressed -> return null
			else {
				return null;
			}

		}


		// SELECTING A SEASON
		// reset panel
		radioPanel.removeAll();
		radioPanel.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Choose a season"));

		int noOptions = tvShows.get(selectedShow).getNumSeasons()+1;
		ButtonGroup seasonsGroup = new ButtonGroup ();
		JRadioButton[] seasonsList = new JRadioButton [noOptions];
		radioPanel.setLayout (new GridLayout (Math.max(8, noOptions), 1));

		// first option is to remove all seasons
		seasonsList [0] = new JRadioButton ("Remove all seasons");
		seasonsGroup.add (seasonsList [0]);
		radioPanel.add(seasonsList[0]);

		// Create and add each radio button (season) to the panel
		for (int i = 0 ; i < tvShows.get(selectedShow).getNumSeasons(); i++) {
			seasonsList [i+1] = new JRadioButton ("Season " + tvShows.get(selectedShow).getSeasons().get(i).getSeasonNo());
			seasonsGroup.add (seasonsList [i+1]);
			radioPanel.add (seasonsList [i+1]);
		}

		// make radiopanel scrollable
		scrollPanel = new JScrollPane (radioPanel);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));

		// Show a dialog with the panel attached
		scrollPanel = new JScrollPane (radioPanel);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));

		// if ok/cancel button is selected
		// loop until option is selected
		found = false;

		while (!found) {
			choice = JOptionPane.showConfirmDialog (panel, scrollPanel,
					"Season Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			// ok button pressed
			if (choice == JOptionPane.OK_OPTION) {
				for (int index = 0 ; index < seasonsList.length ; index++) {
					// if option is selected -> break loop
					if (seasonsList[index].isSelected ()) {
						selectedSeason = index-1;
						found = true;

						// success message
						JOptionPane.showMessageDialog (panel, "Removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}

			// cancel button pressed -> return null
			else {
				return null;
			}
		}


		int[] result = {selectedShow, selectedSeason};
		return result;
	}


	// submenu 2 methods


	// Description: asks user to select episode number
	// Parameters: none
	// Return: int representing position of episode selected
	public int askEpisodeNo() {
		int noEps = currentSeason.getNumEps();
		currentSeason.sortEpisodes(episodeSortOption);

		JPanel radioPanel = new JPanel (); 
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		radioPanel.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Choose an episode"));
		radioPanel.setLayout (new GridLayout (Math.max(8, noEps), 1));

		// Create a group of radio buttons to add to the Panel
		ButtonGroup buttonGroup = new ButtonGroup ();
		JRadioButton[] buttonList = new JRadioButton [noEps];

		// Create and add each radio button to the panel
		int epPos = 0;
		for (int i = 0 ; i < noEps; i++) {
			String title = currentSeason.getEpisodes().get(i).getTitle();
			int epNo = currentSeason.getEpisodes().get(i).getEpisodeNo();
			buttonList[i] = new JRadioButton (String.format("  Ep. %d: %s", epNo, title));
			buttonGroup.add(buttonList [i]);
			radioPanel.add(buttonList [i]);
		}

		// make radiobuttons scrollable
		JScrollPane scroll = new JScrollPane (radioPanel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(300, 200));


		// if ok/cancel button is selected
		// loop until option is selected

		while (true) {
			// Show a dialog with the panel attached
			int choice = JOptionPane.showConfirmDialog (panel, scroll,
					"Show Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			// ok button pressed
			if (choice == JOptionPane.OK_OPTION) {
				for (int index = 0 ; index < buttonList.length ; index++) {
					// option is selected -> break loop
					if (buttonList[index].isSelected ()) {
						return index;
					}
				}			
			}

			// cancel button pressed -> return -1
			else {
				return -1;
			}

		}

	}

	// Description: asks user to select which option they want to remove episode by (ep#, title, range, watched)
	// Parameters: none
	// Return: int representing the option (1/2/3/4, -1 for exit)
	public int askRemoveOption() {
		JPanel radioPanel = new JPanel (); 
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		radioPanel.setBorder (BorderFactory.createTitledBorder (
				lowerEtched, "Choose a remove option"));
		radioPanel.setLayout (new GridLayout (4, 1));

		// Create a group of radio buttons to add to the Panel
		ButtonGroup buttonGroup = new ButtonGroup ();
		JRadioButton[] buttonList = new JRadioButton [4];

		// Create and add each radio button to the panel
		buttonList[0] = new JRadioButton ("Remove by episode #");
		buttonList[1] = new JRadioButton ("Remove by title");
		buttonList[2] = new JRadioButton ("Remove by range");
		buttonList[3] = new JRadioButton ("Removed watched episodes");

		for (int i = 0 ; i < 4; i++) {
			buttonGroup.add (buttonList [i]);
			radioPanel.add (buttonList [i]);
		}

		// if ok/cancel button is selected
		// loop until option is selected

		while (true) {
			// Show a dialog with the panel attached
			int choice = JOptionPane.showConfirmDialog (panel, radioPanel,
					"Remove Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			// ok button pressed
			if (choice == JOptionPane.OK_OPTION) {
				for (int index = 0 ; index < buttonList.length ; index++) {
					// option is selected -> break loop
					if (buttonList[index].isSelected ()) {
						return (index+1);
					}
				}			

			}

			// cancel button pressed -> return -1
			else {
				return -1;
			}

		}

	}

	// Description: asks user to select which option they want to sort episodes by (ep#, title, time)
	// Parameters: none
	// Return: int representing the option (1/2/3, -1 for exit)
	public int askSortOption() {
		JPanel radioPanel = new JPanel (); 
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		radioPanel.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Choose a remove option"));
		radioPanel.setLayout (new GridLayout (3, 1));

		// Create a group of radio buttons to add to the Panel
		ButtonGroup buttonGroup = new ButtonGroup ();
		JRadioButton[] buttonList = new JRadioButton [3];

		// Create and add each radio button to the panel
		buttonList[0] = new JRadioButton ("Sort by episode #");
		buttonList[1] = new JRadioButton ("Sort by title");
		buttonList[2] = new JRadioButton ("Sort by time");

		for (int i = 0 ; i < 3; i++) {
			buttonGroup.add (buttonList [i]);
			radioPanel.add (buttonList [i]);
		}

		// Show a dialog with the panel attached
		int choice = JOptionPane.showConfirmDialog (panel, radioPanel,
				"Sort Options",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.DEFAULT_OPTION);


		// if ok/cancel button is selected
		// loop until option is selected
		boolean found = false;

		while (!found) {
			// ok button pressed
			if (choice == JOptionPane.OK_OPTION) {
				for (int index = 0 ; index < buttonList.length ; index++) {
					// option is selected -> break loop
					if (buttonList[index].isSelected ()) {
						return (index+1);
					}
				}			
			}

			// cancel button pressed -> return -1
			else {
				return -1;
			}

		}

		return -1;
	}

	// Description: asks user to enter an episode number
	// Parameters: none
	// Return: int representing the position of episode
	public int enterEpisodeNo() {
		JPanel mainPanel = new JPanel();
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		mainPanel.setLayout(new FlowLayout());
		mainPanel.setPreferredSize(new Dimension (400, 300));

		String text = "";
		for (int i = 0; i < currentSeason.getNumEps(); i++) {
			text += String.format("Ep. %-4d%s%n", currentSeason.getEpisodes().get(i).getEpisodeNo(), currentSeason.getEpisodes().get(i).getTitle());
		}

		// list of episodes
		JTextArea listField = new JTextArea(text);
		listField.setFont(SMALL_FONT);
		listField.setEditable(false);
		listField.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Current episodes:"));

		// make scrollable
		JScrollPane scrollPanel = new JScrollPane (listField);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));

		// label and field for entering episode number
		JLabel label = new JLabel ("Enter episode number: ");
		JTextField episodeField = new JTextField(4);

		mainPanel.add(scrollPanel);
		mainPanel.add(label);
		mainPanel.add(episodeField);


		while (true) {
			int choice = JOptionPane.showConfirmDialog (panel, mainPanel,
					"Episode Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			if (choice == JOptionPane.OK_OPTION) {	
				try {
					// convert input to integer
					int epNo = Integer.parseInt(episodeField.getText().trim());

					// if episode does not exist -> throw exception to re-ask
					int epPos = Collections.binarySearch(currentSeason.getEpisodes(), new Episode("", epNo, new Time(), 0));
					if (epPos < 0) {
						JOptionPane.showMessageDialog(frame, "Invalid episode number.", "Error", JOptionPane.ERROR_MESSAGE);
					}

					// episode exists -> return the position
					else {
						return epPos;
					}
				}
				catch (NumberFormatException e) {

				}
			}
			else {	
				return -1;
			}
		}


	}

	// Description: asks user to enter two episode numbers (starting/ending bound for range)
	// Parameters: none
	// Return: int[] representing the positions of two episodes
	public int[] enterTwoEpisodeNo() {
		JPanel mainPanel = new JPanel();
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		mainPanel.setLayout(new FlowLayout());
		mainPanel.setPreferredSize(new Dimension (400, 300));

		String text = "";
		for (int i = 0; i < currentSeason.getNumEps(); i++) {
			text += String.format("Ep. %-4d%s%n", currentSeason.getEpisodes().get(i).getEpisodeNo(), currentSeason.getEpisodes().get(i).getTitle());
		}

		// list of existing episodes
		JTextArea listField = new JTextArea(text);
		listField.setFont(SMALL_FONT);
		listField.setEditable(false);
		listField.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Current episodes:"));

		// make scrollable
		JScrollPane scrollPanel = new JScrollPane (listField);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));

		// labels and fields for two episodes
		JLabel label1 = new JLabel ("Enter starting episode: ");
		JLabel label2 = new JLabel ("Enter ending episode: ");
		JTextField epOneField = new JTextField(4);
		JTextField epTwoField = new JTextField(4);

		mainPanel.add(scrollPanel);
		mainPanel.add(label1);
		mainPanel.add(epOneField);
		mainPanel.add(label2);
		mainPanel.add(epTwoField);


		while (true) {
			int choice = JOptionPane.showConfirmDialog (panel, mainPanel,
					"Season Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			if (choice == JOptionPane.OK_OPTION) {	
				try {
					// convert input to integer
					int epOneNo = Integer.parseInt(epOneField.getText().trim());

					// if episode does not exist -> throw exception to re-ask
					int epOnePos = Collections.binarySearch(currentSeason.getEpisodes(), new Episode("", epOneNo, new Time(), 0));

					// no such episode exists -> invalid
					if (epOnePos < 0) {
						JOptionPane.showMessageDialog(frame, "Invalid starting episode number.", "Error", JOptionPane.ERROR_MESSAGE);
					}

					// starting episode exists -> check ending
					else {
						int epTwoNo = Integer.parseInt(epTwoField.getText().trim());

						// ending bound is less than starting bound -> invalid
						if (epTwoNo < epOneNo) {
							JOptionPane.showMessageDialog(frame, "Invalid range.", "Error", JOptionPane.ERROR_MESSAGE);
						}

						// ending bound not less than starting bound
						else {
							// if episode does not exist -> throw exception to re-ask
							int epTwoPos = Collections.binarySearch(currentSeason.getEpisodes(), new Episode("", epTwoNo, new Time(), 0));

							// no such episode exists -> invalid
							if (epTwoPos < 0) {
								JOptionPane.showMessageDialog(frame, "Invalid ending episode number.", "Error", JOptionPane.ERROR_MESSAGE);
							}

							// ending episode exists -> return positions of two episodes
							else {
								int[] res = {epOnePos, epTwoPos};
								return res;
							}
						}

					}
				}
				catch (NumberFormatException e) {

				}
			}
			else {	
				return null;
			}
		}
	}

	// Description: asks user to enter an episode title
	// Parameters: none
	// Return: int representing the position of episode
	public int enterEpisodeTitle() {
		JPanel mainPanel = new JPanel();
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		mainPanel.setLayout(new FlowLayout());
		mainPanel.setPreferredSize(new Dimension (400, 300));

		String text = "";
		for (int i = 0; i < currentSeason.getNumEps(); i++) {
			text += String.format("Ep. %-4d %s%n", currentSeason.getEpisodes().get(i).getEpisodeNo(), currentSeason.getEpisodes().get(i).getTitle());
		}

		// list of episodes
		JTextArea listField = new JTextArea(text);
		listField.setFont(SMALL_FONT);
		listField.setEditable(false);
		listField.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Current episodes:"));

		// make scrollable
		JScrollPane scrollPanel = new JScrollPane (listField);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));

		// label and field for entering title
		JLabel label = new JLabel ("Enter episode title: ");
		JTextField episodeField = new JTextField(20);

		mainPanel.add(scrollPanel);
		mainPanel.add(label);
		mainPanel.add(episodeField);


		while (true) {
			int choice = JOptionPane.showConfirmDialog (panel, mainPanel,
					"Episode Options",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			if (choice == JOptionPane.OK_OPTION) {	
				try {
					// convert input to integer
					String input = episodeField.getText().trim();

					// if episode does not exist -> throw exception to re-ask
					int epPos = Collections.binarySearch(currentSeason.getEpisodes(), new Episode(input, 0, new Time(), 0), new SortByName());
					if (epPos < 0) {
						JOptionPane.showMessageDialog(frame, "Invalid episode title.", "Error", JOptionPane.ERROR_MESSAGE);
					}

					// episode exists -> return position
					else {
						return epPos;
					}
				}
				catch (NumberFormatException e) {

				}
			}
			else {	
				return -1;
			}
		}

	}

	// Description: asks user to enter information for a new episode
	// Parameters: none
	// Return: Episode object of newly created episode
	public Episode enterAddEpisode() {
		JPanel mainPanel = new JPanel();
		Border lowerEtched = BorderFactory.createEtchedBorder (EtchedBorder.RAISED);

		mainPanel.setLayout(new FlowLayout());
		mainPanel.setPreferredSize(new Dimension (400, 300));

		String text = "";
		for (int i = 0; i < currentSeason.getNumEps(); i++) {
			text += String.format("Ep. %-4d %s%n", currentSeason.getEpisodes().get(i).getEpisodeNo(), currentSeason.getEpisodes().get(i).getTitle());
		}

		// list of existing episodes
		JTextArea listField = new JTextArea(text);
		listField.setBorder (BorderFactory.createTitledBorder (lowerEtched, "Current episodes:"));
		listField.setFont(SMALL_FONT);
		listField.setEditable(false);

		JScrollPane scrollPanel = new JScrollPane (listField);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(300, 200));

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3, 2));
		inputPanel.setPreferredSize(new Dimension (400, 80));

		JLabel label = new JLabel ("Enter episode title: ");
		JTextField titleField = new JTextField(12);

		JLabel label2 = new JLabel ("Enter episode #: ");
		JTextField numberField = new JTextField(4);

		JLabel label3 = new JLabel ("Enter episode length (hh:mm:ss): ");
		JTextField timeField = new JTextField(12);


		inputPanel.add(label);
		inputPanel.add(titleField);
		inputPanel.add(label2);
		inputPanel.add(numberField);
		inputPanel.add(label3);
		inputPanel.add(timeField);

		mainPanel.add(scrollPanel);
		mainPanel.add(inputPanel);


		while (true) {
			int choice = JOptionPane.showConfirmDialog (panel, mainPanel,
					"Add episode",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.DEFAULT_OPTION);

			if (choice == JOptionPane.OK_OPTION) {	
				// EPISODE NUMBER
				try {
					int epNo = Integer.parseInt(numberField.getText().trim());

					if (epNo <= 0) {
						throw new NumberFormatException();
					}

					Collections.sort(currentSeason.getEpisodes());
					int epPos = Collections.binarySearch(currentSeason.getEpisodes(), new Episode("", epNo, new Time(), 0));

					// episode # can't already exit
					if (epPos >= 0) {
						throw new NumberFormatException();
					}

					// TIME
					try {
						String timeInput = timeField.getText().trim();

						// blank line -> return -1
						if (timeInput.length() == 0) {
							throw new NumberFormatException();
						}

						if (timeInput.indexOf(":") == timeInput.lastIndexOf(":"))
							throw new NumberFormatException();

						int hours = Integer.parseInt(timeInput.substring(0, timeInput.indexOf(":")));
						int minutes = Integer.parseInt(timeInput.substring(timeInput.indexOf(":") + 1, timeInput.lastIndexOf(":")));
						int seconds = Integer.parseInt(timeInput.substring(timeInput.lastIndexOf(":") + 1));

						if (hours < 0) {
							throw new NumberFormatException();
						}

						if (minutes < 0 || minutes > 59) {
							throw new NumberFormatException();
						}

						if (seconds < 0 || seconds > 59) {
							throw new NumberFormatException();
						}

						Time time = new Time(hours, minutes, seconds);

						// EPISODE TITLE
						String title = titleField.getText().trim();

						if (title.length() == 0) {
							JOptionPane.showMessageDialog(frame, "Invalid title.", "Error", JOptionPane.ERROR_MESSAGE);
							continue;
						}

						return (new Episode(title, epNo, time, currentSeason.getSeasonNo()));

					}
					catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(frame, "Invalid time format.", "Error", JOptionPane.ERROR_MESSAGE);
					}

				}

				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(frame, "Invalid episode number.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			}

			else {
				return null;
			}


		}


	}

	// Description: formats list of all tv shows neatly
	// Parameters: none
	// Return: String of list of tv shows
	public static String displayTVShows () {
		String result = "";

		// no tv shows -> say so
		if(tvShows.size() == 0) {
			result += "  TV show list is empty.\n";
		}

		// has tv shows -> number each show and display name
		else {
			int count = 1;
			for (TVShow show : tvShows) {
				result += String.format("  %-5s %s\n", count + ".", show);
				count++;
			}
		}

		return result;

	}

	// Description: formats information about a specific tvShow neatly
	// Parameters: int showNo is the tv show # in list to be displayed
	// Return: String of tv show information
	public static String displayInfo (int showNo) {
		String result = "";
		TVShow show = tvShows.get(showNo);
		result += String.format("  %-25s%-10s\n", "Title:", show.getTitle());
		result += String.format("  %-25s%-10s\n", "Genre:", show.getGenre());
		result += String.format("  %-25s%-10s\n", "Seasons:", show.getNumSeasons());
		result += String.format("  %-25s%-10d\n", "Episodes:", show.getNumEps());
		result += String.format("  %-25s%-10s\n", "Total Time:", show.getShowTime());

		return result;
	}

	// Description: allows user to add a new tv show to the list
	// Parameters: String fileName is the file containing information on new show
	// Return: void, but modifies the tvShows list to add new show
	public static void addTVShow (String fileName) throws IOException {
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

				// update show time and number of episdoes
				show.addTime(episodeTime);
				show.setNumEps(show.getNumEps()+1);
			}

			// sort episodes
			Collections.sort(season.getEpisodes());
		}

		inFile.close();

		// sort seasons in tv show
		Collections.sort(show.getSeasons());

	}

	// Description: removes an entire tv show from list
	// Parameters: int showNo is the position of show to be removed
	// Return: void, but modifies tvShows to remove that tv show
	public static void remove (int showNo) {		
		// remove show from tv shows list
		tvShows.remove(showNo);
	}

	// Description: removes a specific season of a tv show from list
	// Parameters: int showNo is the position of show to be removed, int seasonPos is position of season to be removed
	// Return: void, but modifies the specific show in tvShows list to remove that season
	public static void remove (int showNo, int seasonPos) {
		TVShow show = tvShows.get(showNo);
		Time t = show.getSeasons().get(seasonPos).getSeasonTime();
		int numEps = show.getSeasons().get(seasonPos).getNumEps();

		// update number of episodes and total show time
		show.setNumEps(show.getNumEps() - numEps);
		show.subtractTime(t);
		show.setNumSeasons(show.getNumSeasons() - 1);

		// remove
		show.getSeasons().remove(seasonPos);

	}

	// Description: formats show status
	// Parameters: int showNo is the position of show to view
	// Return: String with formatted description of show
	public static String showStatus (int showNo) {
		String result = "";

		TVShow show = tvShows.get(showNo);
		int totalUnwatched = 0;
		int totalWatchedSeasons = 0;

		result += "   " + show.getTitle() + "\n\n";
		for (Season s : show.getSeasons()) {
			if (s.getIsWatched()) {
				totalWatchedSeasons++;
			}
			totalUnwatched += s.getUnwatchedEps();
			result += String.format("   Season %d: %d/%d watched\n", s.getSeasonNo(), s.getNumEps() - s.getUnwatchedEps(), s.getNumEps());
		}

		result += String.format("\n   %d watched seasons in total\n", totalWatchedSeasons);
		result += String.format("   %d unwatched episodes in total\n", totalUnwatched);

		return result;
	}

	
}