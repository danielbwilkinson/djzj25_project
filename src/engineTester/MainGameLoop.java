package engineTester;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

	private volatile boolean isRunning;
	
	//public static void main(String[] args) {
	public MainGameLoop(Canvas canvas){
		DisplayManager.createDisplay(canvas);
		Loader loader = new Loader();
		isRunning = true;
		
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
		
		/*List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<500;i++){
			entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
		}*/
		
		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
		//Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")));
		
		float maxHeight = terrain.getMaxHeight();
		float minHeight = terrain.getMinHeight();
		
		Camera camera = new Camera();	
		MasterRenderer renderer = new MasterRenderer(maxHeight, minHeight);
		
		while(!Display.isCloseRequested() && isRunning){
			camera.move(terrain);
			
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			/*for(Entity entity:entities){
				renderer.processEntity(entity);
			}*/
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	public void requestClose(){
		isRunning = false;
	}

}
