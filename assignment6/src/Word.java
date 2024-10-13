// Lynn Tao
// May 26, 2024
// Word class for the word frequency assignment

public class Word implements Comparable <Word>{

	private String word;
	private int frequency = 0;
	
	// constructor
	public Word (String w) {
		word = w;
		frequency = 1;
	}
	
	// getters
	public String getWord() {
		return word;
	}
	
	public int getFrequency() {
		return frequency;
	}

	// Description: increases the frequency by 1
	// Parameters: none
	// Return: none
	public void addWord() {
		frequency++;
	}
	
	// Description: checks if two words are equal
	// Parameters: Object o is the object of the word to be compared against
	// Return: true if equal, false if unequal
	public boolean equals(Object o) {
		Word w = (Word) o;

		if (this.word.equals(w.word)) {
			return true;
		}
		return false;
	}

	// Description: used to determine which bucket hashtable uses - adds unicode of each character in word
	// Parameters: none
	// Return: int representing hash code
	public int hashCode() {
		int res = 0;

		for (int i = 0; i < word.length(); i++) {
			res += word.charAt(i);
		}

		return res;
	}


	// Description: used by Comparable to compare two words by frequency
	// Parameters: Word w is word to be compared against
	// Return: difference in frequency between other word and this word
	public int compareTo(Word w) {
		return w.frequency - this.frequency;
	}
}
