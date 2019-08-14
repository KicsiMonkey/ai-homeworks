import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Node {
	public static ArrayList<ArrayList<Node>> NODES = new ArrayList<ArrayList<Node>>();
	
	public int level;
	public ArrayList<Double> weights = new ArrayList<Double>();
	public double bias; // this is the input value in case of input nodes
	
	public Node(int level) {
		this.level = level;
		if(level >= 1) {
			for (int i = 0; i < NODES.get(level-1).size(); ++i) {
				weights.add(new Random().nextGaussian()*0.1);
			}
			bias = 0;
		}
	}
	
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
	
//	public Double partialDerivateWeight(int index) {
//		if (level == NODES.size()-1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum += NODES.get(level-1).get(j).evaluate()*((j==index)? 1.0 : 0.0);
//				
//			}
//			return weightedSum + bias;
//		} else if (level >= 1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum += NODES.get(level-1).get(j).evaluate()*((j==index)? 1.0 : 0.0);
//			}
//			return Math.max(weightedSum+bias, 0.0) == 0.0 ? 0.0 : weightedSum;
//		}
//		return bias;
//	}
//	
//	public Double partialDerivateBias() {
//		if (level == NODES.size()-1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum += NODES.get(level-1).get(j).evaluate()*weights.get(j);
//			}
//			return 1.0;
//		} else if (level >= 1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum += NODES.get(level-1).get(j).evaluate()*weights.get(j);
//			}
//			return Math.max(weightedSum+bias, 0.0) == 0.0 ? 0.0 : 1.0;
//		}
//		return bias;
//	}
//	
//	public Double partialDerivateWeight(Node node, int index) {
//		if (level == NODES.size()-1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum +=
//					( this == node ? NODES.get(level-1).get(j).evaluate() :
//						NODES.get(level-1).get(j).partialDerivateWeight(node, index) ) *
//					( this == node ? ((j==index)? 1.0 : 0.0) : (weights.get(j)) );
//			}
//			return weightedSum;
//		} else if (level >= 1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum += 
//						( this == node ? NODES.get(level-1).get(j).evaluate() :
//							NODES.get(level-1).get(j).partialDerivateWeight(node, index) ) *
//						( this == node ? ((j==index)? 1.0 : 0.0) : (weights.get(j)) );
//			}
//			return Math.max(weightedSum, 0.0);
//		}
//		return 0.0;
//	}
	
	public Double partialDerivateWeight(Node node, int index) {
		if (level != 0) {
			double weightedSum = 0;
			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
				weightedSum +=
					(this == node) ?				
					( j==index ? 1.0 : 0.0) * NODES.get(level-1).get(j).evaluate()
					:
					weights.get(j) * NODES.get(level-1).get(j).partialDerivateWeight(node, index);
			}
			if (level == NODES.size()-1) {
				return weightedSum;
			} else {
				return this.evaluate() > 0.0 ? weightedSum : 0.0;
			}			
		}
		return 0.0;
	}
	
//	public Double partialDerivateBias(Node node) {
//		if (level == NODES.size()-1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum += NODES.get(level-1).get(j).evaluate()*weights.get(j);
//			}
//			return 1.0;
//		} else if (level >= 1) {
//			double weightedSum = 0;
//			for(int j = 0; j < NODES.get(level-1).size(); ++j) {
//				weightedSum += NODES.get(level-1).get(j).evaluate()*weights.get(j);
//			}
//			return Math.max(weightedSum+bias, 0.0) == 0.0 ? 0.0 : 1.0;
//		}
//		return bias;
//	}
	
	public Double partialDerivateBias(Node node) {
		if (level != 0) {
			double weightedSum = 0;
			if (this == node) {
				weightedSum = 1;
			} else {
				for(int j = 0; j < NODES.get(level-1).size(); ++j) {
					weightedSum += weights.get(j) * NODES.get(level-1).get(j).partialDerivateBias(node);
				}				
			}
			if (level == NODES.size()-1) {
				return weightedSum;
			} else {
				return this.evaluate() > 0.0 ? weightedSum : 0.0;
			}			
		}
		return 0.0;
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
