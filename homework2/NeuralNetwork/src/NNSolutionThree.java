import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NNSolutionThree {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String[] archParams = reader.readLine().split(",");
			int notInputNodesSize = 0;
			for (int i = 1; i < archParams.length; ++i) {
				notInputNodesSize += Integer.parseInt(archParams[i]);
			}
			
			ArrayList<Double> weightParams = new ArrayList<Double>();
			for (int j = 0; j < notInputNodesSize; ++j) {
				String[] weightParamsTokens = reader.readLine().split(",");
				for (int i = 0; i < weightParamsTokens.length; ++i) {
					weightParams.add(Double.parseDouble(weightParamsTokens[i]));					
				}
			}
			
			int exampleInputsSize = Integer.parseInt(reader.readLine());
			ArrayList<ArrayList<Double>> exampleInputs = new ArrayList<ArrayList<Double>>();
			for (int j = 0; j < exampleInputsSize; ++j) {
				String[] exampleInputTokens = reader.readLine().split(",");
				exampleInputs.add(new ArrayList<Double>());
				for (int i = 0; i < exampleInputTokens.length; ++i) {
					exampleInputs.get(j).add(Double.parseDouble(exampleInputTokens[i]));
				}
			}
			
			ArrayList<Node> inputNodes = new ArrayList<Node>();
			for (int i = 0; i < Integer.parseInt(archParams[0]); ++i) {
				inputNodes.add(new Node(0, new ArrayList<Double>(), 0.0));
			}
			Node.NODES.add(inputNodes);
			
			int innerNodeLevelsSize = archParams.length-2; //osszes minusz bemenet es kimenet szint
			for (int j = 0; j < innerNodeLevelsSize; ++j) {
				ArrayList<Node> innerNodes = new ArrayList<Node>();
				int prevLevelSize = Integer.parseInt(archParams[j]);
				for (int i = 0; i < Integer.parseInt(archParams[1+j]); ++i) {
					ArrayList<Double> weightParamsSubList = new ArrayList<Double>(weightParams.subList(0, prevLevelSize));
					innerNodes.add(new Node(1+j,
							weightParamsSubList,
							weightParams.get(prevLevelSize)));
					for (int k = 0; k < weightParamsSubList.size(); ++k) {
						weightParams.remove(0); // remove used weights
					}
					weightParams.remove(0); // remove bias
				}
				Node.NODES.add(innerNodes);
			}
			
			// elozo utolso ciklusa lehetne
			int prevLevelSize = Integer.parseInt(archParams[archParams.length-2]); //meret-1-1
			ArrayList<Node> outputNodes = new ArrayList<Node>();
			for (int i = 0; i < Integer.parseInt(archParams[archParams.length-1]); ++i) {
				ArrayList<Double> weightParamsSubList = new ArrayList<Double>(weightParams.subList(0, prevLevelSize));
				outputNodes.add(new Node(archParams.length-1,
						weightParamsSubList,
						weightParams.get(prevLevelSize)));
				for (int k = 0; k < weightParamsSubList.size(); ++k) {
					weightParams.remove(0); // remove used weights
				}
				weightParams.remove(0); // remove bias
			}
			Node.NODES.add(outputNodes);
			
			Node.printArchitecture();
			
//			for(int k = 0; k < exampleInputs.size(); ++k) {
//				for (int i = 0; i < Node.NODES.get(0).size(); ++i) {
//					Node.NODES.get(0).get(i).bias = exampleInputs.get(k).get(i);
//				}
//				
//				for (int j = 1; j < Node.NODES.size(); ++j) {
//					for (int o = 0; o < Node.NODES.get(j).size(); ++o) {
//						for (int l = 0; l < Node.NODES.get(j).get(o).weights.size(); ++l) {
//							Double w = Node.NODES.get(j).get(o).partialDerivateWeight(l);
//							System.out.print(w);
//							System.out.print(',');							
//						}
//						Double b = Node.NODES.get(j).get(o).partialDerivateBias();
//						System.out.print(b);
//						System.out.println();
//						//System.out.println("Node:");
//						//Node.NODES.get(j).get(o).printNode();
//					}
//				}
//			}
			
			for(int k = 0; k < exampleInputs.size(); ++k) {
				for (int i = 0; i < Node.NODES.get(0).size(); ++i) {
					Node.NODES.get(0).get(i).bias = exampleInputs.get(k).get(i);
				}
				
				Node outputNode = Node.NODES.get(Node.NODES.size()-1).get(0);
				for (int j = 1; j < Node.NODES.size(); ++j) {
					for (int o = 0; o < Node.NODES.get(j).size(); ++o) {
						Node currentNode = Node.NODES.get(j).get(o);
						for (int l = 0; l < Node.NODES.get(j).get(o).weights.size(); ++l) {
							Double w = outputNode.partialDerivateWeight(currentNode, l);
							System.out.print(w);
							System.out.print(',');							
						}
						Double b = outputNode.partialDerivateBias(currentNode);
						System.out.print(b);
						System.out.println();
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

