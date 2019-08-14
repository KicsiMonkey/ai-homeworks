
public class Sized {
	public int height, width;
	
	public Sized(int height, int width) {
		this.height = height;
		this.width = width;	
	}
	
	public void rotate() {
		int x = height;
		height = width;
		width = x;
	}
	
	public int size() {
		return height*width;
	}
	
	public Boolean isHorizontal() {
		if (height <= width) 
			return true;
		return false;
	}
}
