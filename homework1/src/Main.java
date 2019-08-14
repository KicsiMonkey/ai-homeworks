import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			/////////////////////////////////////////////////////////////
			// BEOLVASAS
			// Poggyasz
			String[] luggageInfo = reader.readLine().split("\\s");
			Luggage luggage = new Luggage( Integer.parseInt(luggageInfo[0]), Integer.parseInt(luggageInfo[1]) );
			
			// Targyak listaja
			String[] itemCount = reader.readLine().split("\\s");
			int n = Integer.parseInt( itemCount[0] );
			ArrayList<Item> items = new ArrayList<Item>();
			for (int i = 0; i < n; ++i) {
				String[] itemInfo = reader.readLine().split("\\s");
				items.add( new Item( i+1, Integer.parseInt(itemInfo[0]), Integer.parseInt(itemInfo[1]) ) );
			}
			// BEOLVASAS VEGE
			/////////////////////////////////////////////////////////////
			
			luggage.addItems(items);
			
			/*
			luggage.printInfo();
			luggage.printInside();
			System.out.println();
			System.out.println(">>>>>>>>>>");
			*/
			Boolean result = luggage.fitItems();
			
			luggage.printInside();
			/*System.out.println();
			if(result) {
				System.out.println("SUCCESSSSSSSSSSSSSSSSSSSS");
			} else {
				System.out.println("FAILUREEEEEEEEEEEEEEEEE");
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
