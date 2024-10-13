// Lynn Tao
// April 21, 2024

package nographics;

public class Time {

	private int hours;
	private int minutes;
	private int seconds;
	
	// constructor (default values for hours, minutes, seconds is 0
	public Time () {
		
	}
	
	// constructor
	public Time (String time) {
		this.hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
		this.minutes = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.lastIndexOf(":")));
		this.seconds = Integer.parseInt(time.substring(time.lastIndexOf(":") + 1));
	}
	
	// constructor
	public Time (int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	// getters/setters
	public int getSeconds() {
		return hours * 3600 + minutes * 60 + seconds;
	}

	public void setHours (int hours) {
		this.hours = hours;
	}
	
	public void setMinutes (int minutes) {
		this.minutes = minutes;
	}
	
	public void setSeconds (int seconds) {
		this.seconds = seconds;
	}
	
	// Description: adds a time to current time
	// Parameters: Time t is time to be added
	// Return: void, but modifies "this" object to add time
	public void add(Time t) {
		int totalSeconds = this.seconds + t.seconds;		
		this.seconds = totalSeconds % 60;
		
		int totalMinutes = this.minutes + t.minutes + totalSeconds/60;
		this.minutes = totalMinutes % 60;
		
		int totalHours = this.hours + t.hours + totalMinutes/60;
		this.hours = totalHours;
	}
	
	// Description: subtracts a time from current time
	// Parameters: Time t is time to be subtracted
	// Return: void, but modifies "this" object to subtracted time
	public void subtract(Time t) {
		int totalSeconds = this.seconds - t.seconds;	
		int totalMinutes = this.minutes - t.minutes;
		int totalHours = this.hours - t.hours;
		
		if (totalSeconds < 0) {
			this.seconds = totalSeconds + 60;
			totalMinutes--;
		}
		else {
			this.seconds = totalSeconds;
		}
		
		if (totalMinutes < 0) {
			this.minutes = totalMinutes + 60;
			totalHours--;
		}
		else {
			this.minutes = totalMinutes;
		}
		
		this.hours = totalHours;		
		
	}
	
	// Description: finds difference between current time and another time
	// Parameters: Time t is time to be compared against
	// Return: int representing number of seconds between current time and other time
	public int getDifference(Time t) {
		int secondsDiff = this.seconds - t.seconds;
		int minutesDiff = this.minutes - t.minutes;
		int hoursDiff = this.hours - t.hours;
		
		return (secondsDiff + minutesDiff*60 + hoursDiff*3600);
	}
	
	// Description: formats time neatly
	// Parameters: none
	// Return: properly formatted string of "this" time object, in hh:mm:ss
	public String toString () {
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	
}
