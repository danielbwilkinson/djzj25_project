package routeFinder;

import java.util.ArrayList;
import java.lang.Math;

public class Route {
	private int[] start;
	private int[] end;
	private double cost;
	private ArrayList<int[]> path;
	private GraphGenerator graphGenerator;

	public Route(int[] start, int[] end, GraphGenerator graphGenerator){
		this.setStart(start);
		this.setEnd(end);
		this.graphGenerator = graphGenerator;
	}
	
	public double heuristicFunction(int[] point1, int[] point2){
		double h = graphGenerator.asTheCrowFlies(point1, point2);
		
		return h;
	}
	
	public double costFunction(int[] point1, int[] point2, int graphScale){
		double c =  graphGenerator.exertionBetweenNeighbours(point1, point2, graphScale);
		c += graphGenerator.dangerCost(point1, point2, graphScale);
		c *= graphGenerator.groundTypeCoeff(point1, point2, graphScale);
		return c;
	}
	
	public boolean dijkstra(){
		int graphScale = (int) Math.round(graphGenerator.asTheCrowFlies(start, end) / 50);
		if(graphScale == 0){
			graphScale = 1;
		}
		int[] scaledEnd = new int[2];
		scaledEnd[0] = (int) Math.max(1, (graphScale * Math.floor(this.end[0] / graphScale)));
		scaledEnd[1] = (int) Math.max(1, (graphScale * Math.floor(this.end[1] / graphScale)));
		this.end = scaledEnd;
		
		int[] scaledStart = new int[2];
		scaledStart[0] = (int) Math.max(1, (graphScale * Math.floor(this.start[0] / graphScale)));
		scaledStart[1] = (int) Math.max(1, (graphScale * Math.floor(this.start[1] / graphScale)));
		this.start = scaledStart;
		
		//Initialise lists to keep track of needed variables for algorithm
		
		//list of positions that are neighbours of visited nodes and visited nodes
		ArrayList<int[]> discovered = new ArrayList<int[]>();
		
		// list of indices in discovered list that have been finalised
		ArrayList<Integer> visited = new ArrayList<Integer>();
		
		// previous[x] is the previous node on the path to discovered[x]
		ArrayList<Integer> previous = new ArrayList<Integer>();
		
		// totalPathWeight[x] is the total length of the path to discovered[x]
		ArrayList<Double> totalPathWeight = new ArrayList<Double>();
		
		discovered.add(start);
		previous.add(null);
		totalPathWeight.add((double)0);
		visited.add(0);
		
		float minWeight;
		int minNode = 0;
		int nextNotify = 100;
		
		// while we have not discovered the the end point or we have not visited it
		while(arrayListContainsArray(discovered,end) == -1 || !visited.contains(arrayListContainsArray(discovered,end))){
			// maintain node discovery
			int numberDiscovered = discovered.size();
			if(numberDiscovered >= nextNotify){
				System.out.printf("Discovered: %d Visited: %d\n", numberDiscovered, visited.size());
				nextNotify += 200;
			}
			for(int x = 0; x < numberDiscovered; x++){
				if(visited.contains(x) && discovered.get(x) != start){
					continue;
				}
				ArrayList<int[]> neighbours = graphGenerator.getNeighbours(discovered.get(minNode), graphScale);
				for(int[] neighbour: neighbours){
					// check for any undiscovered neighbours of current minNode and initialise them if they exist
					if(arrayListContainsArray(discovered, neighbour) == -1){
						discovered.add(neighbour);
						previous.add(minNode);
						double newPathWeight = (totalPathWeight.get(minNode) + costFunction(discovered.get(minNode), neighbour, graphScale));
						totalPathWeight.add(newPathWeight);
					}
				}
			}
			
			// find next nearest node
			minWeight = Float.MAX_VALUE;
			int prevMinNode = minNode;
			for(int x = 0; x < discovered.size(); x++){
				if((!visited.contains(x)) && totalPathWeight.get(x)  + heuristicFunction(discovered.get(x), end)<= minWeight){
					minWeight = (float) (totalPathWeight.get(x) + heuristicFunction(discovered.get(x), end));
					minNode = x;
				}
			}
			if(minNode == prevMinNode){
				System.out.println("Could not find route");
				return false;
			}
			
			// check for improvements on any neighbour's path weight
			visited.add(minNode);
			ArrayList<int[]> neighbours = graphGenerator.getNeighbours(discovered.get(minNode), graphScale);
			for(int[] neighbour: neighbours){
				int neighbourIndex = arrayListContainsArray(discovered, neighbour);
				if(neighbourIndex == -1){
					continue;
				}
				double newWeight = totalPathWeight.get(neighbourIndex) + costFunction(discovered.get(minNode), neighbour, graphScale);
				if(newWeight < totalPathWeight.get(neighbourIndex)){
					totalPathWeight.set(neighbourIndex, newWeight);
					previous.set(neighbourIndex, minNode);
				}
			}
		}
		
		// find path
		path = new ArrayList<int[]>();
		cost = 0;
		int prevIndex = arrayListContainsArray(discovered,end);
		if(prevIndex == -1){
			System.out.println("path not complete");
			return false;
		}
		path.add(0, end);
		while(path.get(0) != start){
			int[] newStart = discovered.get(previous.get(prevIndex));
			prevIndex = previous.get(prevIndex);
			cost += costFunction(newStart, path.get(0), graphScale);
			path.add(0, newStart);
		}
		
		return true;
	}
	
	// returns index of array in arrayList, if it exists. Otherwise, returns -1
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
			if(inArray && (arrayList.get(listItem).length == array.length)){
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

	public double getCost() {
		return cost;
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
