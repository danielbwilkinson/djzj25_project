package engineTester;

import java.util.ArrayList;

public class Config {
	public static String dtmFileName;// = "C:/Users/Daniel Wilkinson/Documents/Uni/Year 3/Project/data/LIDAR-DTM-1M-NZ24nw(durham)/nz2045_DTM_1M.asc";
	public static String dsmFileName;// = "C:/Users/Daniel Wilkinson/Documents/Uni/Year 3/Project/data/LIDAR-DSM-1M-NZ24nw(durham)/nz2045_DSM_1M.asc";
	
	public static int[] start = {50,505};
	public static int[] end = {150, 700};

	public static double dangerWeight = 1;
	public static double exertionWeight = 1;
	
	public static double routeResolution = 100;
	
	public static ArrayList<int[]> enemyLocations = new ArrayList<int[]>();
}
