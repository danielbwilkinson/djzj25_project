package guiEngine;

import java.awt.Canvas;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import engineTester.MainGameLoop;

public class RenderPanel extends JPanel{
	private Canvas canvas;
	private Thread renderThread;
	private volatile boolean isRunning;
	private MainGameLoop mainGameLoop;
	private ArrayList<int[]> enemyLocations;
	
	public RenderPanel(){
		canvas = new Canvas(){
			@Override
			public void addNotify(){
				super.addNotify();
				startRender();
			}
			
			@Override
			public void removeNotify(){
				stopRender();
				super.removeNotify();
			}
		};
		add(canvas);
	}
	
	public void startRender(){
		renderThread = new Thread(new Runnable() {
			@Override
			public void run(){
				isRunning = true;
				mainGameLoop = new MainGameLoop(canvas);
				mainGameLoop.run();
			}
		});
		renderThread.start();
	}
	
	public void stopRender(){
		isRunning = false;
		try{
			mainGameLoop.requestClose();
			renderThread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void makeRoute(){
		mainGameLoop.makeRoute();
	}
	
	public void changeModel(){
		mainGameLoop.changeModel();
	}
	
	public void resetRender(){
		mainGameLoop.resetRender();
	}
	
	public void addEnemyLocation(int xCoord, int yCoord){
		mainGameLoop.addEnemy(xCoord, yCoord);
		GuiMain.getRouteInfoPanel().update();
	}
	
	public ArrayList<int[]> getEnemyLocations(){
		return mainGameLoop.getEnemyLocations();
	}
	
	public void clearEnemies(){
		mainGameLoop.clearEnemies();
	}
}
