
public class MyRectangle {
	private int left; // x-coordinate of left edge
	private int bottom; // y-coordinate of bottom edge
	private int width; // width of rectangle
	private int height; // height of rectangle
	
	private static int numRectangles = 0;
	
	public MyRectangle(int left, int bottom, int width, int height) {
		this.left = Math.max(0, left);
		this.bottom = Math.max(0, bottom);
		this.width = Math.max(0, width);
		this.height = Math.max(0, height);
		numRectangles++;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getNumRectangles() {
		return numRectangles;
	}
	
	public void setWidth(int width) {
		this.width = Math.max(0, width);
	}
	
	public void setHeight(int height) {
		this.height = Math.max(0, height);
	}
	
	public String toString() {
		return String.format("base:(%d,%d) w:%d h:%d", left, bottom, width, height);
	}
	
	public int area() {
		return (width*height);
	}
	
	public int compareTo(Object o) {
		MyRectangle r = (MyRectangle) o;
		
		if (this.area() > r.area()) 
			return 1;
		
		else if (this.area() < r.area()) 
			return -1;
		
		return 0;
	}

	public boolean equals(Object o) {
		MyRectangle r = (MyRectangle) o;

		if (this.compareTo(r) == 0) {
			return true;
		}
		
		return false;
	}
}
