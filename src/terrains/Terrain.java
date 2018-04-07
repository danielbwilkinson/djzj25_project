package terrains;

import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import engineTester.Config;
import models.RawModel;
import renderEngine.FileInterpreter;
import renderEngine.Loader;
import routeFinder.GraphGenerator;
import routeFinder.Route;
import textures.ModelTexture;

public class Terrain {
	
	private static final float SIZE = 1000000;//800;
	private static final int VERTEX_COUNT = 1000;//128;
	
	private float x;
	private float z;
	private RawModel model;
//	private ModelTexture texture;
	private float[] vertices;
	private int[] indices;
	private float[] textureCoords;
	private float[] route;
	
	private boolean useDTM;
	
	private FileInterpreter fileInterpreter;
	
	public Terrain(int gridX, int gridZ, Loader loader){//, ModelTexture texture){
		//this.texture = texture;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		useDTM = true;
		this.model = generateTerrain(loader);
	}
	
	
	
	public float getX() {
		return x;
	}



	public float getZ() {
		return z;
	}



	public RawModel getModel() {
		return model;
	}

	public float getHeightAtPos(int x, int z){
		try{
			return Math.round(vertices[(1000*z + x) * 3 + 1]);
		} catch(ArrayIndexOutOfBoundsException e){
			return 0;
		}
	}
	
	public float getMaxHeight(){
		float maxHeight = 0;
		for(int x = 1; x < vertices.length; x += 3){
			if(vertices[x] > maxHeight){
				maxHeight = vertices[x];
			}
		}
		return maxHeight;
	}
	
	public float getMinHeight(){
		float minHeight = Float.MAX_VALUE;
		for(int x = 1; x < vertices.length; x += 3){
			if(vertices[x] < minHeight && vertices[x] != -9999){
				minHeight = vertices[x];
			}
		}
		return minHeight;
	}

	public float[] getRoute(){
		FileInterpreter dtmFileInterpreter = new FileInterpreter(true);
		GraphGenerator graphGenerator = new GraphGenerator(dtmFileInterpreter);
		FileInterpreter dsmInterpreter = new FileInterpreter(false);
		GraphGenerator dsmGenerator = new GraphGenerator(dsmInterpreter);
		int[] start = Config.start;//{50,505};
		int[] end = Config.end;//{150,700};
		graphGenerator.setStartPoint(start);
		graphGenerator.setEndPoint(end);
		graphGenerator.makeRoute(dsmGenerator);
		Route route = graphGenerator.getRoute();
		System.out.printf("route number of vertices: %d \n", route.getPath().size());
		ArrayList<int[]> rawRoutePath = route.getPath();
		float[] routePath = new float[2*rawRoutePath.size()];
		for(int x = 0; x < rawRoutePath.size(); x++){
			int[] position = rawRoutePath.get(x);
			routePath[2 * x] = position[0];
			routePath[2 * x + 1] = position[1];
		}
		return routePath;
	}

	private RawModel generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		this.vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		this.textureCoords = new float[count*2];
		this.indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		this.fileInterpreter = new FileInterpreter(useDTM);
		
		vertices = fileInterpreter.getVertices();
		//float[] route = getRoute();
		float[] isInRoute = new float[vertices.length];
		for(int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex+=3){
			isInRoute[vertexIndex] = 0;
			isInRoute[vertexIndex+1] = 0;
			isInRoute[vertexIndex+2] = 0;
			if(route != null){
				for(int routeIndex = 0; routeIndex < route.length / 2; routeIndex++){
					if(vertices[vertexIndex] >= route[2 * routeIndex] - 1 && 
					vertices[vertexIndex] <= route[2 * routeIndex] + 1 &&
					vertices[vertexIndex + 2] >= route[2 * routeIndex + 1] - 1 &&
					vertices[vertexIndex + 2] <= route[2 * routeIndex + 1] + 1){
						isInRoute[vertexIndex] = 1;
						isInRoute[vertexIndex + 1] = 1;
						isInRoute[vertexIndex + 2] = 1;
						break;
					}
				}
			}
		}
		loader.cleanUp();
		return loader.loadToVAO(vertices, textureCoords, isInRoute, indices, isInRoute);
	}
	
	public RawModel makeRoute(Loader loader){
		//this.fileInterpreter = new FileInterpreter(useDTM);
		vertices = fileInterpreter.getVertices();
		route = getRoute();
		float[] isInRoute = new float[vertices.length];
		for(int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex+=3){
			isInRoute[vertexIndex] = 0;
			isInRoute[vertexIndex+1] = 0;
			isInRoute[vertexIndex+2] = 0;
			for(int routeIndex = 0; routeIndex < route.length / 2; routeIndex++){
				if(vertices[vertexIndex] >= route[2 * routeIndex] - 1 && 
				vertices[vertexIndex] <= route[2 * routeIndex] + 1 &&
				vertices[vertexIndex + 2] >= route[2 * routeIndex + 1] - 1 &&
				vertices[vertexIndex + 2] <= route[2 * routeIndex + 1] + 1){
					isInRoute[vertexIndex] = 1;
					isInRoute[vertexIndex + 1] = 1;
					isInRoute[vertexIndex + 2] = 1;
					break;
				}
			}
		}
		loader.cleanUp();
		return loader.loadToVAO(vertices, textureCoords, isInRoute, indices, isInRoute);
	}
	
	public RawModel changeModel(Loader loader){
		System.out.println("Terrain changing model");
		useDTM = !useDTM;
		return generateTerrain(loader);
	}

}
