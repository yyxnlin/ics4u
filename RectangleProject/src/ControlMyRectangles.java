import java.util.ArrayList;
import java.util.Scanner;

public class ControlMyRectangles {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		System.out.print("Enter the number of squares: ");
		int numSquares = Integer.parseInt(in.nextLine());
		System.out.print("Enter the max side length: ");
		int maxLen = Integer.parseInt(in.nextLine());
		
		ArrayList<MyRectangle> squares = new ArrayList<>();
		for (int i = 0; i < numSquares; i++) {
			int side = (int) (Math.random() * maxLen) + 1;
			squares.add(new MyRectangle(0, 0, side, side));
		}
		
		for (MyRectangle i : squares) {
			System.out.println(i);
		}
		while (true) {
			System.out.print("Enter the area of the square to search for: ");
			int area = Integer.parseInt(in.nextLine());
			double side = Math.sqrt(area);
			int count = 0;
			
			MyRectangle search1 = new MyRectangle(0, 0, (int) side, (int) side);

			while (squares.contains(search1)) {
				squares.remove(squares.indexOf(search1));
				count++;
			}
			
			if (side  % 1.0 != 0) {
				MyRectangle search2 = new MyRectangle(0, 0, (int) side + 1, (int) side + 1);
				
				while (squares.contains(search2)) {
					squares.remove(squares.indexOf(search2));
					count++;
				}

			}
			
			System.out.printf("There are %d rectangles with area of %d sq. units.%n", count, area);
			
			
		}

	}

}
