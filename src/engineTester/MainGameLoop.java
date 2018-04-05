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
import renderEngine.InputHandler;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
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
		
		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,0,loader);
		
		float maxHeight = terrain.getMaxHeight();
		float minHeight = terrain.getMinHeight();
		//float[] route = terrain.getRoute();
		
		Camera camera = new Camera();	
		MasterRenderer renderer = new MasterRenderer(maxHeight, minHeight);
		InputHandler inputHandler = new InputHandler(camera, renderer);
		
		while(!Display.isCloseRequested() && isRunning){
			camera.move(terrain);
			
			renderer.processTerrain(terrain);
			renderer.render(light, camera);
			inputHandler.pollInput();
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
