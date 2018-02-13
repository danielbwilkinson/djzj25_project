package routeFinder;

import java.util.ArrayList;

import renderEngine.FileInterpreter;

public class GraphGenerator {
	private FileInterpreter fileInterpreter;
	private float[] vertices;
	private int NCOLS;
	private int NROWS;
	
	public GraphGenerator(FileInterpreter fileInterpreter){
		this.fileInterpreter = fileInterpreter;
		this.vertices = fileInterpreter.getVertices();
		this.NCOLS = fileInterpreter.getNCOLS();
		this.NROWS = fileInterpreter.getNROWS();
		
		int[] point1 = {1,1};
		int[] point2 = {4,5};
		Route route = new Route(point1, point2, this);
		boolean routeFound = route.dijkstra();
		if(routeFound){
			System.out.println(route.getExertion());
		}
		//testExertion();
	}
	
	public double asTheCrowFlies(int[] start, int[] end){
		return Math.sqrt(Math.pow((end[0] - start[0]),2) + Math.pow((end[1] - start[1]),2));
	}
	
	private float testExertion(){
		ArrayList<int[]> path = new ArrayList<int[]>();
		int[] a = {1,1};
		int[] b = {2,2};
		int[] c = {3,3};
		int[] d = {4,4};
		int[] e = {4,5};
		
		float totalExertion = exertionBetweenNeighbours(a,b) + 
				exertionBetweenNeighbours(b,c) +
				exertionBetweenNeighbours(c,d) +
				exertionBetweenNeighbours(d,e);
		System.out.println(totalExertion);
		
				
		return 1;
	}
	
	public float exertionBetweenNeighbours(int[] start, int[] end){
		return (float)(Math.abs(heightOfPos(end) - heightOfPos(start)) + 1);
	}
	
	public double heightOfPos(int[] pos){
		return this.vertices[(pos[1] * this.NCOLS + pos[0]) * 3 + 1];
	}
	
	public ArrayList<int[]> getNeighbours(int[] pos){
		ArrayList<int[]> neighbours = new ArrayList<int[]>();
		
		// add top left
		if(pos[0] > 0 && pos[1] > 0){
			int[] neighbour = {pos[0] - 1, pos[1] - 1};
			neighbours.add(neighbour);
		};
		
		// add top centre
		if(pos[1] > 0){
			int[] neighbour = {pos[0], pos[1] - 1};
			neighbours.add(neighbour);
		};
		
		// add top right
		if(pos[0] < this.NCOLS-1 && pos[1] > 0){
			int[] neighbour = {pos[0] + 1, pos[1] - 1};
			neighbours.add(neighbour);
		};
		
		// add middle left
		if(pos[0] > 0){
			int[] neighbour = {pos[0] - 1, pos[1]};
			neighbours.add(neighbour);
		};
		
		// add middle right
		if(pos[0] < this.NCOLS){
			int[] neighbour = {pos[0] + 1, pos[1]};
			neighbours.add(neighbour);
		};
		
		// add bottom left
		if(pos[0] > 0 && pos[1] < this.NROWS){
			int[] neighbour = {pos[0] - 1, pos[1] + 1};
			neighbours.add(neighbour);
		};
		
		// add bottom centre
		if(pos[1] < this.NROWS){
			int[] neighbour = {pos[0], pos[1] + 1};
			neighbours.add(neighbour);
		};
		
		// add bottom right
		if(pos[0] < this.NCOLS && pos[1] < this.NROWS){
			int[] neighbour = {pos[0] + 1, pos[1] + 1};
			neighbours.add(neighbour);
		};
		
		return neighbours;
	}
}
