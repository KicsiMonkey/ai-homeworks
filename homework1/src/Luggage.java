import java.util.ArrayList;
import java.util.Collections;

public class Luggage extends Sized {
	private int step = 0;
	private ArrayList<Item> items;
	Integer inside[][];
	
	public Luggage(int height, int width) {
		super(height, width);
		this.inside = new Integer[this.height][this.width];
		for (int i = 0; i < this.height; ++i) {
			for (int j = 0; j < this.width; ++j) {
				this.inside[i][j] = 0;
			}
		}
		Item.isLuggageHorizontal = isHorizontal();
	}
	
	public void addItems(ArrayList<Item> items) {
		if(isHorizontal()) {
			for (Item item : items) {
				if (item.height > item.width) {
					item.rotate();
				}
			}			
		} else {
			for (Item item : items) {
				if (item.height < item.width) {
					item.rotate();
				}
			}
		}
		this.items = items;
	}
	
	public boolean fitItems() {
		if (this.size() < itemsSizeSum()) return false;
		Collections.sort(items, Collections.reverseOrder());
		if ( tryToFitItemsStartingWithIndex(0) == false ) {
			Collections.sort(items, new SizeMoreThanWidthItemDescendingComparator());
			/*printItems();*/
			step = 0;
			return tryToFitItemsStartingWithIndex(0);
		}
		return true;
	}
	
	public boolean tryToFitItemsStartingWithIndex(int index) {
		//// Hanyadik dolgot teszem be, hanyadik globalis lepest teszem, mi van a taskaban, varjunk 0.1 sec-et
		/*System.out.println("id: " + index + " | step: " + step);
		printInside();
		System.out.println();
		System.out.println(">>>>>>>>>>");*/
		/*try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		if (++step >= 1000) {
			return false;
		}
		Boolean foundASpot = false;		
		/* A kerdojel operatorok celja:
		 * fektetett (vizszintes) taska eseten
		 * a kulso ciklus tol fuggolegesen, a belso vizszintesen,
		 * tehat eloszor vegignezzuk az legfelso sort, majd tovabb,
		 * allo (fuggoleges) taska eseten
		 * a kulso ciklus tol vizszintesen, a belso fuggolegesen,
		 * tehat eloszor vegignezzuk a legbaloldali oszlopot, majd tovabb. */
		for (int i = 0; i < (isHorizontal() ? (height-(items.get(index).height-1)) : (width-(items.get(index).width-1)) ) && foundASpot == false; ++i) {
			for (int j = 0; j < (isHorizontal() ? (width-(items.get(index).width-1)) : (height-(items.get(index).height-1)) ) && foundASpot == false; ++j) {
				Position positionForItem = (isHorizontal()) ?
						new Position(i, j, i+items.get(index).height-1, j+items.get(index).width-1) :
						new Position(j, i, j+items.get(index).height-1, i+items.get(index).width-1);
				if (isPositionFree(positionForItem)) {
					
					items.get(index).position = positionForItem;
					placeItem(items.get(index));
					foundASpot = true;
					
					//// Lassuk mi van bent
					/*printInside();
					System.out.println();
					System.out.println(">>>>>>>>>>");*/
					
					if (index+1 < items.size()) {
						foundASpot = tryToFitItemsStartingWithIndex(index+1);						
					}
					if (foundASpot == false) {
						removeItem(items.get(index));
					}
				}
			}
		}
		if (foundASpot == false) {
			foundASpot = tryToFitItemsStartingWithIndexRotated(index);
		}
		return foundASpot;
	}
	
	public boolean tryToFitItemsStartingWithIndexRotated(int index) {
		/*System.out.println("id: " + index + " | step: " + step);
		printInside();
		System.out.println();
		System.out.println(">>>>>>>>>>");*/
		
		if (++step >= 1000) {
			return false;
		}
		items.get(index).rotate();
		Boolean foundASpot = false;
		for (int i = 0; i < (isHorizontal() ? (height-(items.get(index).height-1)) : (width-(items.get(index).width-1)) ) && foundASpot == false; ++i) {
			for (int j = 0; j < (isHorizontal() ? (width-(items.get(index).width-1)) : (height-(items.get(index).height-1)) ) && foundASpot == false; ++j) {
				Position positionForItem = (isHorizontal()) ?
						new Position(i, j, i+items.get(index).height-1, j+items.get(index).width-1) :
						new Position(j, i, j+items.get(index).width-1, i+items.get(index).height-1);
				if (isPositionFree(positionForItem)) {
					
					items.get(index).position = positionForItem;
					placeItem(items.get(index));
					foundASpot = true;
					
					//// Lassuk mi van bent
					/*printInside();
					System.out.println();
					System.out.println("ROT>>>>>>>>>>");*/
					
					if (index+1 < items.size()) {
						foundASpot = tryToFitItemsStartingWithIndex(index+1);						
					}
					if (foundASpot == false) {
						removeItem(items.get(index));
					}
				}
			}
		}
		if (foundASpot == false) {
			items.get(index).rotate();
		}
		return foundASpot;
	}
	
	
	
	public boolean isPositionFree(Position position) {
		for (int i = position.startRow; i <= position.endRow; ++i) {
			for (int j = position.startColumn; j <= position.endColumn; ++j) {
				if ( inside[i][j] != 0 ) return false; 
			}
		}
		return true;
	}
	
	public void placeItem(Item item) {
		for (int i = item.position.startRow; i <= item.position.endRow; ++i) {
			for (int j = item.position.startColumn; j <= item.position.endColumn; ++j) {
				inside[i][j] = item.index;
			}
		}
	}
	
	public void removeItem(Item item) {
		for (int i = item.position.startRow; i <= item.position.endRow; ++i) {
			for (int j = item.position.startColumn; j <= item.position.endColumn; ++j) {
				inside[i][j] = 0;
			}
		}
	}
	
	public int itemsSizeSum() {
		int sum = 0;
		for(Item item : this.items) sum += item.size();
		return sum;
	}
	
	public void printInfo() {
		printLuggage();
		System.out.println("==========");
		printItems();
	}
	
	public void printLuggage() {
		System.out.println("L : " + height + "x" + width + "[" + this.size() + "]");
	}
	
	public void printItems() {
		for (Item item : items) item.print();
	}
	
	public void printInside() {
		for (int i = 0; i < this.height; ++i) {
			for (int j = 0; j < this.width; ++j) {
				System.out.print(this.inside[i][j] + (j<(this.width-1)?"\t":""));
				//kenyelmesebb kiiras a szemnek ez az utobbi
				//System.out.format("%2d" + (j<(this.width-1)?" ":""), this.inside[i][j]);
			}
			System.out.print(i<(this.height-1)?"\n":"");	// utolso sorra ellenorzes
		}
	}
}
