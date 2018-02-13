package terrains;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import models.RawModel;
import renderEngine.FileInterpreter;
import renderEngine.Loader;
import routeFinder.GraphGenerator;
import textures.ModelTexture;

public class Terrain {
	
	private static final float SIZE = 1000000;//800;
	private static final int VERTEX_COUNT = 1000;//128;
	
	private float x;
	private float z;
	private RawModel model;
	private ModelTexture texture;
	private float[] vertices;
	
	public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture){
		this.texture = texture;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
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

	public ModelTexture getTexture() {
		return texture;
	}

	private RawModel generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		this.vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
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
		FileInterpreter fileInterpreter = new FileInterpreter();
		vertices = fileInterpreter.getVertices();
		GraphGenerator graphGenerator = new GraphGenerator(fileInterpreter);
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

}
