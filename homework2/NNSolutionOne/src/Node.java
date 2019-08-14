import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Node {
	public static ArrayList<ArrayList<Node>> NODES = new ArrayList<ArrayList<Node>>();
	
	public int level;
	public ArrayList<Double> weights = new ArrayList<Double>();
	public double bias;
	
	public Node(int level) {
		this.level = level;
		if(level >= 1) {
			for (int i = 0; i < NODES.get(level-1).size(); ++i) {
				weights.add(new Random().nextGaussian()*0.1);
			}
			bias = 0;
		}
	}
	
	public void printNode() {
		if(level >= 1) {
			for (int i = 0; i < weights.size(); ++i) {
				System.out.print(String.format(Locale.ROOT, "%.9f", weights.get(i)));
				System.out.print(',');
			}
			System.out.println(bias);
		}
	}
	
	public static void printArchitecture() {
		for (int i = 0; i < NODES.size(); ++i) {
			System.out.print(NODES.get(i).size());
			if (i < NODES.size()-1) System.out.print(',');
		}
		System.out.println();
	}
}
