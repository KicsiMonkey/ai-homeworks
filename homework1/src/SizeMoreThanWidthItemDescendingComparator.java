import java.util.Comparator;

public class SizeMoreThanWidthItemDescendingComparator implements Comparator<Item> {

	@Override
	public int compare(Item o1, Item o2) {
		if (Item.isLuggageHorizontal) {
			if (o1.size() < o2.size())
				return 1;
			if (o1.size() > o2.size())
				return -1;
			if (o1.size() == o2.size()) {
				if (o1.width < o2.width)
					return 1;
				if (o1.width > o2.width)
					return -1;
				return 0;
			}
		} else {
			if (o1.size() < o2.size())
				return 1;
			if (o1.size() > o2.size())
				return -1;
			if (o1.size() == o2.size()) {
				if (o1.height < o2.height)
					return 1;
				if (o1.height > o2.height)
					return -1;
				return 0;
			}
		}
		return 0;
	}

}
