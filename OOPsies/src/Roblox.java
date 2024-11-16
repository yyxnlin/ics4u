
public class Roblox implements Comparable<Roblox>{

	// instance variables
	private int robux;
	private String name;
	private boolean isUnder13;
	
	// class variable
	private static String creator = "David Baszucki";
	
	public Roblox(String name, boolean isUnder13) {
		this.name = name;
		this.isUnder13 = isUnder13;
	}
	
	public Roblox(String name, boolean isUnder13, int robux) {
		this.name = name;
		this.isUnder13 = isUnder13;
		this.robux = robux;
	}
	
	// getter methods
	public String getName() {
		return name;
	}
	
	public boolean getUnder13() {
		return isUnder13;
	}
	
	public int getRobux() {
		return robux;
	}
	
	// setter methods
	public void setRobux(int robux) {
		this.robux = robux;
	}
	
	
	// instance method
	public void birthdayRobux(int amount) {
		robux += amount;
	}
	
	// class method
	public static void changeName(String newName) {
		creator = newName;
	}
	
	public String toString() {
//		return String.format("Name: %s%nRobux: $%d%nIs under 13: %b%n%n", name, robux, isUnder13);
		return name;
	}
	
	public boolean equals(Object o) {
		Roblox r = (Roblox) o;
		
		return this.name.equals(r.name);
	}

	@Override
	public int compareTo(Roblox r) {
//		return this.name.compareTo(r.name);
		
		return this.robux - r.robux;
	}
	
}