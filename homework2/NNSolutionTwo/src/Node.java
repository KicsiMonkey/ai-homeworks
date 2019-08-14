import java.util.ArrayList;
import java.util.Locale;

public class Node {
	public static ArrayList<ArrayList<Node>> NODES = new ArrayList<ArrayList<Node>>();
	
	public int level;
	public ArrayList<Double> weights = new ArrayList<Double>();
	public double bias;
	
	public Node(int level, ArrayList<Double> weights, Double bias) {
		this.level = level;
		if(level >= 1) {
			this.weights = weights;
			this.bias = bias;
		}
	}
	
	public Double evaluate() {
		if (level == NODES.size()-1) {
			double weightedSum = 0;
			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
				weightedSum += NODES.get(level-1).get(j).evaluate()*weights.get(j);
			}
			return weightedSum + bias;
		} else if (level >= 1) {
			double weightedSum = 0;
			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
				weightedSum += NODES.get(level-1).get(j).evaluate()*weights.get(j);
			}
			return Math.max(weightedSum+bias, 0.0);
		}
		return bias;
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
