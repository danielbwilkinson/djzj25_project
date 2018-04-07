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
	private volatile boolean routeRequested;
	private volatile boolean changeModelRequested;
	private Terrain terrain;
	private Loader loader;
	private Light light;
	private float maxHeight;
	private float minHeight;
	private Camera camera;
	private MasterRenderer renderer;
	private InputHandler inputHandler;
	
	//public static void main(String[] args) {
	public MainGameLoop(Canvas canvas){
		DisplayManager.createDisplay(canvas);
		loader = new Loader();
		isRunning = true;
		routeRequested = false;
		changeModelRequested = false;
		
		light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		terrain = new Terrain(0,0,loader);
		
		maxHeight = terrain.getMaxHeight();
		minHeight = terrain.getMinHeight();
		
		camera = new Camera();	
		renderer = new MasterRenderer(maxHeight, minHeight);
		inputHandler = new InputHandler(camera, renderer);
		
//		while(!Display.isCloseRequested() && isRunning){
//			camera.move(terrain);
//			
//			renderer.processTerrain(terrain);
//			renderer.render(light, camera);
//			inputHandler.pollInput();
//			DisplayManager.updateDisplay();
//		}
//
//		renderer.cleanUp();
//		loader.cleanUp();
//		DisplayManager.closeDisplay();

	}
	
	public void run(){
		while(!Display.isCloseRequested() && isRunning){
			if(routeRequested){
				routeRequested = false;
				terrain.makeRoute(loader);
			}
			if(changeModelRequested){
				changeModelRequested = false;
				terrain.changeModel(loader);
			}
			
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
	
	public void makeRoute(){
		routeRequested = true;
	}
	
	public void changeModel(){
		changeModelRequested = true;
	}

}
