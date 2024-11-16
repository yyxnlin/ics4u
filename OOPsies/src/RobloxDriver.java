import java.util.*;

public class RobloxDriver {

	public static void main(String[] args) {		
		ArrayList<Roblox> myBestFriends = new ArrayList<>();		
		
		Roblox r1 = new Roblox("young k", true);
		myBestFriends.add(r1);
		myBestFriends.add(new Roblox("jungkook", false));
		
		System.out.println(myBestFriends.size());
		System.out.println(myBestFriends.get(1));
		System.out.println(myBestFriends.contains(new Roblox ("jungkook", false)));
	}

}
