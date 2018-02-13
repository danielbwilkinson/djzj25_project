package guiEngine;

import java.awt.Canvas;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import engineTester.MainGameLoop;

public class RenderPanel extends JPanel{
	private Canvas canvas;
	private Thread renderThread;
	private volatile boolean isRunning;
	private MainGameLoop mainGameLoop;
	
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
}
