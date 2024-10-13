// Lynn Tao
// March 29, 2024
// This class is a template for the Player object, which stores one player's data

public class Player implements Comparable<Player> {

	private String name;
	private String power;
	private int score;
	
	// Description: constructor that takes in score, name, and power to create new object
	// Parameters: int score is the player's score, String name is the player's name, String power is the player's power
	// Return: nothing (constructor)
	public Player(int score, String name, String power) {
		this.score = score;
		this.name = name;
		this.power = power;
	}

	// getters
	public String getName() {
		return name;
	}

	public String getPower() {
		return power;
	}
	
	public int getScore() {
		return score;
	}
	
	// Description: finds whether the current player's score is greater or another player's
	// Parameters: Player p is the second player to compare to
	// Return: difference between current player's and second player's scores
	public int compareTo(Player p) {
		return this.score - p.score;
	}
	
	// Description: in case you want to print out the array of players
	// Parameters: none
	// Return: properly formatted line with player's score, name, and power
	public String toString() {
		return String.format("\n%6d%25s%35s", score, name, power);
	}


}
