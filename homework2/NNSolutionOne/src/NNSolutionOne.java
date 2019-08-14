import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NNSolutionOne {
	
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String[] archParams = reader.readLine().split(",");
			
			ArrayList<Node> inputNodes = new ArrayList<Node>();
			for (int i = 0; i < Integer.parseInt(archParams[0]); ++i) {
				inputNodes.add(new Node(0));
			}
			Node.NODES.add(inputNodes);
			
			int innerNodeLevelsSize = archParams.length-2;
			for (int j = 0; j < innerNodeLevelsSize; ++j) {
				ArrayList<Node> innerNodes = new ArrayList<Node>();
				for (int i = 0; i < Integer.parseInt(archParams[1+j]); ++i) {
					innerNodes.add(new Node(1+j));
				}
				Node.NODES.add(innerNodes);
			}
			
			ArrayList<Node> outputNodes = new ArrayList<Node>();
			for (int i = 0; i < Integer.parseInt(archParams[archParams.length-1]); ++i) {
				outputNodes.add(new Node(archParams.length-1));
			}
			Node.NODES.add(outputNodes);
			
			Node.printArchitecture();
			for (int j = 0; j < Node.NODES.size(); ++j) {
				for (int i = 0; i < Node.NODES.get(j).size(); ++i) {
					Node.NODES.get(j).get(i).printNode();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
