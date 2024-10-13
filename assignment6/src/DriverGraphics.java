// Lynn Tao
// May 26, 2024
// This program finds the top 20 most frequent words in a given text file

// ASSUMPTIONS:
// 1. put the files in the project root folder!! (this file should be in src folder)
// 2. no numbers
// 3. contractions are kept as a whole
// 4. one dash between two words is kept as a whole, but multiple dashes will be removed

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.*;


public class DriverGraphics implements ActionListener {
	JFrame frame;
	JPanel panel, rightPanel, leftPanel;
	JTextArea previewText, resultText;
	JButton addFileButton;
	JComboBox<String> fileDropdown;
	ArrayList<String> storyOptions = new ArrayList<>();
	Font NORMAL_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	Font BIGGER_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 14);

	// constructor
	public DriverGraphics() {
		frame = new JFrame ("Word Frequency");
		frame.setPreferredSize(new Dimension(1300, 500));
		frame.setLocation(200, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel ();
		panel.setLayout(new GridLayout(1, 2));
		panel.setBorder (BorderFactory.createEmptyBorder (10,10,10,10));	

		leftPanel = new JPanel ();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.setPreferredSize(new Dimension (300, 600));
		leftPanel.setBorder (BorderFactory.createEmptyBorder (10,10,10,10));

		rightPanel = new JPanel ();
		rightPanel.setLayout(new FlowLayout());
		rightPanel.setBorder (BorderFactory.createEmptyBorder (10,10,10,10));	

		panel.add(leftPanel);
		panel.add(rightPanel);
	}

	public static void main(String[] args) throws FileNotFoundException {
		DriverGraphics d = new DriverGraphics();
		d.storyOptions.add("alice.txt");
		d.storyOptions.add("moby.txt");
		d.displayContent(); 
		d.frame.add(d.panel);

	}

	// Description: displays all of the graphics on the screen
	// Parameters: none
	// Return: void, but displays the graphics
	public void displayContent() {
		// remove/repaint everything
		panel.removeAll();
		leftPanel.removeAll();
		rightPanel.removeAll();

		panel.repaint();
		leftPanel.repaint();
		rightPanel.repaint();

		// add file button
		addFileButton = new JButton("Add file");
		addFileButton.setPreferredSize(new Dimension (200, 50));
		addFileButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		addFileButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		addFileButton.addActionListener(this);
		addFileButton.setActionCommand("add file");


		// dropdown combobox
		String[] files = storyOptions.toArray(new String[storyOptions.size()]);
		fileDropdown = new JComboBox<String> (files);
		fileDropdown.setPreferredSize(new Dimension(50, 30));
		fileDropdown.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
		fileDropdown.addActionListener (this);
		fileDropdown.setActionCommand("choose story");
		fileDropdown.setSelectedItem(null);


		// preview story text box
		previewText = new JTextArea();
		previewText.setLineWrap(false);
		previewText.setEditable(false);
		previewText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		previewText.setFont(NORMAL_FONT);

		// make text box scrollable
		JScrollPane previewScroll = new JScrollPane (previewText);
		previewScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		previewScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		previewScroll.setPreferredSize(new Dimension(300, 400));


		// results text box
		resultText = new JTextArea();
		resultText.setLineWrap(false);
		resultText.setEditable(false);
		resultText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		resultText.setFont(BIGGER_FONT);

		// make results scrollable
		JScrollPane resultScroll = new JScrollPane (resultText);
		resultScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultScroll.setPreferredSize(new Dimension(400, 400));


		// add to left/right panels
		leftPanel.add(addFileButton);
		leftPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		leftPanel.add(fileDropdown);
		leftPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		leftPanel.add(previewScroll);
		rightPanel.add(resultScroll);

		// add to main panel
		panel.add(leftPanel);
		panel.add(rightPanel);
		frame.pack();
		frame.setVisible(true);

	}

	// Description: controls what happens when user clicks something
	// Parameters: ActionEvent event is the action that user performed
	// Return: void, but calls diff methods based on what user does
	public void actionPerformed(ActionEvent e) {
		String eventName = e.getActionCommand();

		// add file button
		if (eventName.equals("add file")) {
			// remove current text
			previewText.setText("");
			resultText.setText("");

			// open file choosing dialog
			FileDialog openDialog = new FileDialog (frame, "Open a new file", FileDialog.LOAD);
			openDialog.setVisible (true);
			String fileName = openDialog.getFile ();

			// file is chosen
			if (fileName != null) {

				// add the title, result, and preview to their arraylists
				storyOptions.add(fileName);

				// reload graphics
				displayContent();

			}
		}

		// story is selected from dropdown
		else if (eventName == "choose story") {
			JComboBox comboBox = (JComboBox) e.getSource();
			int option = comboBox.getSelectedIndex(); // get option index

			if (option != -1) {
				try {
					// update story preview and results text
					previewText.setText(getPreview(storyOptions.get(option)));
					resultText.setText(getResult(storyOptions.get(option)));
				}
				catch (FileNotFoundException e1) {

				}
			}


		}

	}

	// Description: gives the whole file as a String
	// Parameters: String fileName is the file to be read (must be in the project directory)
	// Return: String that contains the whole file
	public String getPreview(String fileName) throws FileNotFoundException{
		String content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
		return content;

	}

	// Description: finds the frequency of each word in a file
	// Parameters: String fileName is the file to be read (must be in the project directory)
	// Return: String that is properly formatted for the top 20 words, total time, and title
	public String getResult(String fileName) throws FileNotFoundException {
		String result = "";
		long totalTime = 0;

		// start timer
		long startTime = System.currentTimeMillis();

		Scanner inFile = new Scanner (new File (fileName));
		HashMap<String, Word> map = new HashMap<>();


		while (inFile.hasNextLine()) {
			String s = inFile.nextLine();

			// remove multiple dashes
			int i = s.indexOf('-'); // index of first dash
			boolean multiple = false; // if there are multiple dashes this will be true

			// dash exists
			if (i != -1) {
				// loop as long as there is another dash right after
				while (s.length()-1 >= i+1 && s.charAt(i+1) == '-') {
					multiple = true;
					// remove the current dash
					s = s.substring(0, i) + s.substring(i+1);
				}

				// if there were multiple, remove the final dash and replace with space
				if (multiple)
					s = s.substring(0, i) + " " + s.substring(i+1);
			}

			// tokenize the rest
			StringTokenizer st = new StringTokenizer(s, " !@#$%^&*()=_+:;\"\\`~<,>.?/1234567890");

			while (st.hasMoreTokens()) {

				String token = st.nextToken();

				// not a single dash or a single apostrophe
				if (!token.equals("-") && !token.equals("\'")) {
					token = fixWord(token);

					Word w = new Word(token);

					// already existing word -> add to frequency
					if (map.containsKey(token)) {
						map.get(token).addWord();
					}

					// not existing word -> add to map
					else {
						map.put(token, w);
					}
				}
			}
		}

		// add the words to a list and sort (will sort by highest -> lowest frequency)
		List<Word> list = new ArrayList<>(map.values());
		Collections.sort(list);

		// format the results
		int count = 1;

		// header
		result += String.format("     %-15s %s%n", "Word", "Frequency");

		// add each word and frequency to result
		for (Word w : list.subList(0, Math.min(list.size(), 20))) {
			result += String.format("%3d. %-15s %d%n", count, w.getWord(), w.getFrequency());
			count++;
		}

		// end timer + calculate time
		long endTime = System.currentTimeMillis();
		totalTime = endTime-startTime;

		// add total time and title to result
		result = "Total Time: " + totalTime + " ms\n\n" + "20 Most Frequent Words in " + fileName + "\n\n" + result;

		return result;

	}

	// Description: removes random punctuations and weird stuff from a word
	// Parameters: String token is the given word
	// Return: String that is the fixed word that can be used in the count
	public String fixWord(String token) {
		token = token.toLowerCase();

		// starts with dash
		while (token.length() >= 1 && token.charAt(0) == '-') {
			token = token.substring(1, token.length());
		}

		// ends with dash or apostrophe
		if (token.length() >= 1 && (token.charAt(token.length()-1) == '-' ||  token.charAt(token.length()-1) == '\'')) {
			token = token.substring(0, token.length()-1);
		}

		// ends with 's
		if (token.length() > 2) {
			if (token.substring(token.length()-2).equals("\'s")) {
				token = token.substring(0, token.length()-2);
			}
		}

		// starts with apostrophe
		if (token.length() >= 1 && token.charAt(0) == '\'') {
			token = token.substring(1);
		}

		return token;
	}
}
