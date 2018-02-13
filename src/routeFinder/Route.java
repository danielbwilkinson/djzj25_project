package routeFinder;

import java.util.ArrayList;

public class Route {
	private int[] start;
	private int[] end;
	private double exertion;
	private ArrayList<int[]> path;
	private GraphGenerator graphGenerator;

	public Route(int[] start, int[] end, GraphGenerator graphGenerator){
		this.setStart(start);
		this.setEnd(end);
		this.graphGenerator = graphGenerator;
	}
	
	public boolean dijkstra(){
		//Initialise lists to keep track of needed variables for algorithm
		
		//list of positions that are neighbours of visited nodes and visited nodes
		ArrayList<int[]> discovered = new ArrayList<int[]>();
		
		// list of indices in discovered list that have been finalised
		ArrayList<Integer> visited = new ArrayList<Integer>();
		
		// previous[x] is the previous node on the path to discovered[x]
		ArrayList<Integer> previous = new ArrayList<Integer>();
		
		// totalPathWeight[x] is the total length of the path to discovered[x]
		ArrayList<Float> totalPathWeight = new ArrayList<Float>();
		
		discovered.add(start);
		previous.add(null);
		totalPathWeight.add((float)0);
		visited.add(0);
		
		float minWeight;
		int minNode = 0;
		
		while(!(arrayListContainsArray(discovered,end) == -1 && visited.contains(arrayListContainsArray(discovered,end)))){
			// maintain node discovery
			int numberDiscovered = discovered.size();
			for(int x = 0; x < numberDiscovered; x++){
				if(visited.contains(x) && discovered.get(x) != start){
					continue;
				}
				ArrayList<int[]> neighbours = graphGenerator.getNeighbours(discovered.get(minNode));
				for(int[] neighbour: neighbours){
					// check for any undiscovered neighbours of current minNode and initialise them if they exist
					if(arrayListContainsArray(discovered, neighbour) == -1){
						discovered.add(neighbour);
						previous.add(minNode);
						float newPathWeight = (float) (totalPathWeight.get(minNode) + graphGenerator.exertionBetweenNeighbours(discovered.get(minNode), neighbour));
						totalPathWeight.add(newPathWeight);
					}
				}
			}
			
			// find next nearest node
			minWeight = Float.MAX_VALUE;
			int prevMinNode = minNode;
			for(int x = 0; x < discovered.size(); x++){
				if((!visited.contains(x)) && totalPathWeight.get(x) <= minWeight){
					minWeight = totalPathWeight.get(x);
					minNode = x;
				}
			}
			if(minNode == prevMinNode){
				System.out.println("Could not find route");
				return false;
			}
			
			// check for improvements on any neighbour's path weight
			visited.add(minNode);
			ArrayList<int[]> neighbours = graphGenerator.getNeighbours(discovered.get(minNode));
			for(int[] neighbour: neighbours){
				//System.out.printf("%d %d\n", neighbour[0], neighbour[1]);
				int neighbourIndex = arrayListContainsArray(discovered, neighbour);
				if(neighbourIndex == -1){
					continue;
				}
				float newWeight = totalPathWeight.get(neighbourIndex) + graphGenerator.exertionBetweenNeighbours(discovered.get(minNode), neighbour);
				//System.out.println(neighbourIndex);
				if(newWeight < totalPathWeight.get(neighbourIndex)){
					totalPathWeight.set(neighbourIndex, newWeight);
					previous.set(neighbourIndex, minNode);
				}
			}
			System.out.printf("Min Node: %d %d\n", discovered.get(minNode)[0], discovered.get(minNode)[1]);
			System.out.printf("Min Weight: %f\n", minWeight);
			if(minWeight > 6){
				break;
			}
		}
		
		for(float weight: totalPathWeight){
			System.out.println(weight);
		}
		
		// find path
		path = new ArrayList<int[]>();
		exertion = 0;
		int prevIndex = arrayListContainsArray(discovered,end);
		if(prevIndex == -1){
			System.out.println("path not complete");
			return false;
		}
		path.add(0, end);
		while(path.get(0) != start){
			int[] newStart = discovered.get(previous.get(prevIndex));
			prevIndex = previous.get(prevIndex);
			exertion += graphGenerator.exertionBetweenNeighbours(newStart, path.get(0));
			path.add(0, newStart);
		}
		
		return true;
	}
	
	private int arrayListContainsArray(ArrayList<int[]> arrayList, int[] array){
		boolean inArray = true;
		for (int listItem = 0; listItem < arrayList.size(); listItem++){
			inArray = true;
			for(int arrayItem = 0; arrayItem < array.length; arrayItem++){
				if(arrayList.get(listItem)[arrayItem] != array[arrayItem]){
					inArray = false;
					break;
				}
			}
			if(inArray && arrayList.get(listItem).length == array.length){
				return listItem;
			}
		}
		return -1;
	}
	
	public GraphGenerator getGraphGenerator() {
		return graphGenerator;
	}

	public void setGraphGenerator(GraphGenerator graphGenerator) {
		this.graphGenerator = graphGenerator;
	}

	public double getExertion() {
		return exertion;
	}

	public ArrayList<int[]> getPath() {
		return path;
	}

	public int[] getStart() {
		return start;
	}

	public void setStart(int[] start) {
		this.start = start;
	}

	public int[] getEnd() {
		return end;
	}

	public void setEnd(int[] end) {
		this.end = end;
	}
}
