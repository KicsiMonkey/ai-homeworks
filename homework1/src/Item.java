
public class Item extends Sized implements Comparable<Item> {
	static public Boolean isLuggageHorizontal = true;
	public int index;
	Position position;
	public Boolean isSet;
	
	public Item(int index, int height, int width) {
		super(height, width);
		this.index = index;
		position = new Position(0, 0, 0, 0);
		isSet = false;
	}

	public void setPosition(int startRow, int startColumn, int endRow, int endColumn) {
		position.startRow = startRow;
		position.startColumn = startColumn;
		position.endRow = endRow;
		position.endColumn = endColumn;
	}

	public void print() {
		System.out.println("I" + index + ": " + height + "x" + width + "[" + this.size() + "]");
	}

	@Override
	public int compareTo(Item o) {
		if (isLuggageHorizontal) {
			if (this.width < o.width)
				return -1;
			if (this.width > o.width)
				return 1;
			if (this.width == o.width) {
				if (this.size() < o.size())
					return -1;
				if (this.size() > o.size())
					return 1;
				return 0;
			}
		} else {
			if (this.height < o.height)
				return -1;
			if (this.height > o.height)
				return 1;
			if (this.height == o.height) {
				if (this.size() < o.size())
					return -1;
				if (this.size() > o.size())
					return 1;
				return 0;
			}
		}
		return 0;
	}
}
