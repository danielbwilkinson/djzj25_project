package engineTester;

import java.util.ArrayList;

import routeFinder.GraphGenerator;
import routeFinder.Route;

public class Evaluator {
	private long startTime;
	private long endTime;
	private Route route;
	private GraphGenerator graphGenerator;
	
	public Evaluator(GraphGenerator graphGenerator){
		this.graphGenerator = graphGenerator;
	}
	
	public void startTimer(){
		startTime = System.nanoTime();
	}
	
	public void endTimer(){
		endTime = System.nanoTime();
		
		System.out.printf("Time to compute: %d\n", endTime - startTime);
	}
	
	public void analyseRoute(Route newRoute){
		this.route = newRoute;
		ArrayList<int[]> routePath = route.getPath();
		double totalHeightChange = 0;
		double totalDistance = 0;
		for(int x = 0; x < routePath.size() - 1; x++){
			int[] point1 = routePath.get(x);
			int[] point2 = routePath.get(x+1);
			totalHeightChange += Math.abs(graphGenerator.heightOfPos(point2) - graphGenerator.heightOfPos(point1));
			totalDistance += graphGenerator.asTheCrowFlies(point1, point2);
		}
		System.out.printf("Total height change: %f\n", totalHeightChange);
		System.out.printf("Total distance covered: %f\n", totalDistance);
	}
	
}
