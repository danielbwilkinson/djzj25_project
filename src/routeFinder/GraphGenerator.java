package routeFinder;

import java.util.ArrayList;

import renderEngine.FileInterpreter;

public class GraphGenerator {
	private FileInterpreter fileInterpreter;
	private float[] vertices;
	private int NCOLS;
	private int NROWS;
	private int[] startPoint;
	private int[] endPoint;
	private Route route;
	private GraphGenerator dsmGraph;
	private final static int GROUND_TYPE_BUILDING = 0;
	private final static int GROUND_TYPE_OPEN = 1;
	private final static int GROUND_TYPE_FOREST = 2;
	private final static int GROUND_TYPE_OTHER = 3;
	
	public GraphGenerator(FileInterpreter fileInterpreter){
		this.fileInterpreter = fileInterpreter;
		this.vertices = fileInterpreter.getVertices();
		this.NCOLS = fileInterpreter.getNCOLS();
		this.NROWS = fileInterpreter.getNROWS();
	}
	
	public void setStartPoint(int[] start){
		this.startPoint = start;
	}
	
	public void setEndPoint(int[] end){
		this.endPoint = end;
	}
	
	public void makeRoute(GraphGenerator dsmGraph){
		this.dsmGraph = dsmGraph;
		this.route = new Route(startPoint, endPoint, this);
		boolean routeFound = route.dijkstra();
		if(routeFound){
			System.out.printf("Path weight: %f\n", route.getCost());
		} else {
			System.out.println("Route not found");
		}
	}
	
	public Route getRoute(){
		return route;
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
		
		double totalExertion = exertionBetweenNeighbours(a,b,1) + 
				exertionBetweenNeighbours(b,c,1) +
				exertionBetweenNeighbours(c,d,1) +
				exertionBetweenNeighbours(d,e,1);
		System.out.println(totalExertion);
		
				
		return 1;
	}
	
	public double exertionBetweenNeighbours(int[] start, int[] end, int graphScale){
		double distance;
		if(start[0] == end[0] || start[1] == end[1]){
			distance = graphScale;
		} else {
			distance = (float) 1.414 * graphScale;
		}
		double heightDifference = heightOfPos(end) - heightOfPos(start);
		
		double exertion = energyOverGradient(heightDifference, distance);
		return exertion;
	}
	
	public double energyOverGradient(double height, double distance){
		double energy = 0;
		double gradient = height / distance;
		if(gradient >= 0){
			energy = (100 * gradient) + 10;
		} else if (gradient > -0.1) {
			energy = (50 * gradient) + 10;
		} else {
			energy = (-50) * gradient + 5;
		}
		energy *= 0.1 * distance;
		//System.out.printf("Height diff: %f Grad energy: %f distance: %f\n", height, energy, distance);
		return energy;
	}
	
	public double heightOfPos(int[] pos){
		double height = this.vertices[(pos[1] * this.NCOLS + pos[0]) * 3 + 1];
		double null_value = fileInterpreter.getNODATA_VALUE();
		int graphScale = 1;
		while(height == null_value){
			ArrayList<int[]> neighbours = getNeighbours(pos, graphScale);
			double sumHeights = 0;
			int numberRealNeighbours = 0;
			for(int[] neighbour : neighbours){
				double neighbourHeight = this.vertices[(neighbour[1] * this.NCOLS + neighbour[0]) * 3 + 1];
				if(neighbourHeight != null_value){
					sumHeights += neighbourHeight;
					numberRealNeighbours++;
				}
			}
			if(numberRealNeighbours != 0){
				height = sumHeights / numberRealNeighbours;
			} else {
				graphScale++;
			}
		}
		return height;
	}
	
	public ArrayList<int[]> getNeighbours(int[] pos, int graphScale){
		ArrayList<int[]> neighbours = new ArrayList<int[]>();
		
		// add top left
		if(pos[0] > graphScale - 1 && pos[1] > graphScale - 1){
			int[] neighbour = {pos[0] - graphScale, pos[1] - graphScale};
			neighbours.add(neighbour);
		};
		
		// add top centre
		if(pos[1] > graphScale - 1){
			int[] neighbour = {pos[0], pos[1] - graphScale};
			neighbours.add(neighbour);
		};
		
		// add top right
		if(pos[0] < this.NCOLS-graphScale && pos[1] > graphScale - 1){
			int[] neighbour = {pos[0] + graphScale, pos[1] - graphScale};
			neighbours.add(neighbour);
		};
		
		// add middle left
		if(pos[0] > graphScale - 1){
			int[] neighbour = {pos[0] - graphScale, pos[1]};
			neighbours.add(neighbour);
		};
		
		// add middle right
		if(pos[0] < this.NCOLS - graphScale){
			int[] neighbour = {pos[0] + graphScale, pos[1]};
			neighbours.add(neighbour);
		};
		
		// add bottom left
		if(pos[0] > graphScale - 1 && pos[1] < this.NROWS - graphScale){
			int[] neighbour = {pos[0] - graphScale, pos[1] + graphScale};
			neighbours.add(neighbour);
		};
		
		// add bottom centre
		if(pos[1] < this.NROWS - graphScale){
			int[] neighbour = {pos[0], pos[1] + graphScale};
			neighbours.add(neighbour);
		};
		
		// add bottom right
		if(pos[0] < this.NCOLS - graphScale && pos[1] < this.NROWS - graphScale){
			int[] neighbour = {pos[0] + graphScale, pos[1] + graphScale};
			neighbours.add(neighbour);
		};
		
		return neighbours;
	}
	
	public double dangerCost(int[] point1, int[] point2, int graphScale){
		double danger = 0;
		danger += elevationAdvantageDifference(point1, point2);
		
		return danger;
	}
	
	public double elevationAdvantageDifference(int[] point1, int[] point2){
		ArrayList<int[]> point1Neighbours = new ArrayList<int[]>();
		ArrayList<int[]> point2Neighbours = new ArrayList<int[]>();
		for(int neighbourDistance = 0; neighbourDistance < 5; neighbourDistance+=2){
			point1Neighbours.addAll(getNeighbours(point1, neighbourDistance));
			point2Neighbours.addAll(getNeighbours(point2, neighbourDistance));
		}
		
		double point1HeightProportion;
		double point2HeightProportion;
		double maxHeight = Double.MIN_VALUE;
		double minHeight = Double.MAX_VALUE;
		double point1HeightRange;
		double point2HeightRange;
		
		// calculate point1 height proportion
		for(int[] neighbour : point1Neighbours){
			double neighbourHeight = heightOfPos(neighbour);
			if(neighbourHeight > maxHeight){
				maxHeight = neighbourHeight;
			}
			if(neighbourHeight < minHeight){
				minHeight = neighbourHeight;
			}
		}
		point1HeightRange = maxHeight - minHeight;
		point1HeightProportion = (heightOfPos(point1) - minHeight) / point1HeightRange;
		
		// calculate point2 height proportion
		minHeight = Double.MAX_VALUE;
		maxHeight = Double.MIN_VALUE;
		for(int[] neighbour : point2Neighbours){
			double neighbourHeight = heightOfPos(neighbour);
			if(neighbourHeight > maxHeight){
				maxHeight = neighbourHeight;
			}
			if(neighbourHeight < minHeight){
				minHeight = neighbourHeight;
			}
		}
		point2HeightRange = maxHeight - minHeight;
		point2HeightProportion = (heightOfPos(point2) - minHeight) / point2HeightRange;
		
		// rating out of 10 for elevation advantage
		double point1ElevAdv;
		double point2ElevAdv;
		
		// if on flat ground, no tactical disadvantage but no advantage
		if(point1HeightRange < 1){
			point1ElevAdv = 3;
		}
		// if on top of hill, then tactical advantage but silhouetted
		else if (point1HeightProportion > 0.9){
			point1ElevAdv = 4;
		}
		// if on side of hill, we want to be near the top
		else if (point1HeightProportion > 0.1){
			point1ElevAdv = 2 + 10*point1HeightProportion;
		}
		// if in bottom of valley, no tactical advantage but easily hidden
		else {
			point1ElevAdv = 7;
		}
		
		//repeat for point 2
		// if on flat ground, no tactical disadvantage but no advantage
		if(point2HeightRange < 1){
			point2ElevAdv = 3;
		}
		// if on top of hill, then tactical advantage but silhouetted
		else if (point2HeightProportion > 0.9){
			point2ElevAdv = 4;
		}
		// if on side of hill, we want to be near the top
		else if (point2HeightProportion > 0.1){
			point2ElevAdv = 2 + 10*point1HeightProportion;
		}
		// if in bottom of valley, no tactical advantage but easily hidden
		else {
			point2ElevAdv = 5;
		}
		
		return Math.max(0,point2ElevAdv - point1ElevAdv);		
	}
	
	public double groundTypeCoeff(int[] point1, int[] point2, int graphScale){
		double point1DTM = heightOfPos(point1);
		double point1DSM = dsmGraph.heightOfPos(point1);
		double point2DTM = heightOfPos(point2);
		double point2DSM = dsmGraph.heightOfPos(point2);
		ArrayList<int[]> point1Neighbours = new ArrayList<int[]>();
		ArrayList<int[]> point2Neighbours = new ArrayList<int[]>();
		
		for(int x = 0; x < 3; x++){
			point1Neighbours.addAll(getNeighbours(point1, x));
			point2Neighbours.addAll(getNeighbours(point2, x));
		}
		
		ArrayList<Double> point1DtmNeighbourHeights = new ArrayList<Double>();
		ArrayList<Double> point1DsmNeighbourHeights = new ArrayList<Double>();
		ArrayList<Double> point2DtmNeighbourHeights = new ArrayList<Double>();
		ArrayList<Double> point2DsmNeighbourHeights = new ArrayList<Double>();
		
		for(int[] neighbour: point1Neighbours){
			point1DtmNeighbourHeights.add(heightOfPos(neighbour));
			point1DsmNeighbourHeights.add(dsmGraph.heightOfPos(neighbour));
		}
		for(int[] neighbour: point2Neighbours){
			point2DtmNeighbourHeights.add(heightOfPos(neighbour));
			point2DsmNeighbourHeights.add(dsmGraph.heightOfPos(neighbour));
		}
		
		double point1SumDsm = 0;
		for(double height: point1DsmNeighbourHeights){point1SumDsm += height;}
		double point1AvgDsm = point1SumDsm / point1DsmNeighbourHeights.size();
		
		double point1SumDtm = 0;
		for(double height: point1DtmNeighbourHeights){point1SumDtm += height;}
		double point1AvgDtm = point1SumDtm / point1DtmNeighbourHeights.size();
		
		double point1DsmSumDiffSquared = 0;
		for(double height : point1DsmNeighbourHeights){
			double diff = height - point1AvgDsm;
			diff *= diff;
			point1DsmSumDiffSquared += diff;
		}
		point1DsmSumDiffSquared /= point1DsmNeighbourHeights.size();
		
		int point1GroundType;
		// if less than 1 metre between terrain and surface, ground type is open
		if(point1AvgDtm > point1AvgDsm - 1){
			point1GroundType = GROUND_TYPE_OPEN;
		}
		// if variance is small, assume area is building
		else if(point1DsmSumDiffSquared < 0.001){
			point1GroundType = GROUND_TYPE_BUILDING;
		}
		// if variance is large and surface higher than terrain, ground type is forest
		else if(point1DsmSumDiffSquared > 1){
			point1GroundType = GROUND_TYPE_FOREST;
		}
		else{
			point1GroundType = GROUND_TYPE_OTHER;
		}


		
		double point2SumDsm = 0;
		for(double height: point2DsmNeighbourHeights){point2SumDsm += height;}
		double point2AvgDsm = point2SumDsm / point2DsmNeighbourHeights.size();
		
		double point2SumDtm = 0;
		for(double height: point2DtmNeighbourHeights){point2SumDtm += height;}
		double point2AvgDtm = point2SumDtm / point2DtmNeighbourHeights.size();
		
		double point2DsmSumDiffSquared = 0;
		for(double height : point2DsmNeighbourHeights){
			double diff = height - point2AvgDsm;
			diff *= diff;
			point2DsmSumDiffSquared += diff;
		}
		point2DsmSumDiffSquared /= point2DsmNeighbourHeights.size();
		
		int point2GroundType;
		// if less than 1 metre between terrain and surface, ground type is open
		if(point2AvgDtm > point2AvgDsm - 1){
			point2GroundType = GROUND_TYPE_OPEN;
		}
		// if variance is small, assume area is building
		else if(point2DsmSumDiffSquared < 0.001){
			point2GroundType = GROUND_TYPE_BUILDING;
		}
		// if variance is large and surface higher than terrain, ground type is forest
		else if(point2DsmSumDiffSquared > 1){
			point2GroundType = GROUND_TYPE_FOREST;
		}
		else{
			point2GroundType = GROUND_TYPE_OTHER;
		}
		
		double coeff;
		if(point1GroundType == GROUND_TYPE_BUILDING || point2GroundType == GROUND_TYPE_BUILDING){
			coeff = 5;
		}
		else if(point1GroundType == GROUND_TYPE_FOREST || point2GroundType == GROUND_TYPE_FOREST){
			coeff = 2.5;
		}
		else if(point1GroundType == GROUND_TYPE_OTHER || point2GroundType == GROUND_TYPE_OTHER){
			coeff = 1.5;
		}
		else {
			coeff = 1;
		}
		
		return coeff;
	}
}
