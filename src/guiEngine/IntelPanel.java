package guiEngine;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import engineTester.Config;

public class IntelPanel extends JPanel{
	private GridBagConstraints gc;
	
	private SpinnerNumberModel startSpinnerXModel;
	private SpinnerNumberModel startSpinnerYModel;
	
	private SpinnerNumberModel endSpinnerXModel;
	private SpinnerNumberModel endSpinnerYModel;
	
	private SpinnerNumberModel enemySpinnerXModel;
	private SpinnerNumberModel enemySpinnerYModel;
	
	private SpinnerNumberModel exertionWeightModel;
	private SpinnerNumberModel dangerWeightModel;
	
	private SpinnerNumberModel routeResolutionModel;
	
	private JLabel startLabel;
	private JSpinner startXInput;
	private JSpinner startYInput;
	
	private JLabel endLabel;
	private JSpinner endXInput;
	private JSpinner endYInput;
	
	private JLabel exertionWeightLabel;
	private JSpinner exertionWeight;
	private JLabel dangerWeightLabel;
	private JSpinner dangerWeight;
	
	private JLabel routeResolutionLabel;
	private JSpinner routeResolution;
	
	private JLabel enemyLocLabel;
	private JSpinner enemyLocationX;
	private JSpinner enemyLocationY;
	private JButton addEnemy;
	
	private JPanel fillerPanel;
	
	public IntelPanel(){
		startSpinnerXModel = new SpinnerNumberModel(	0,		// initial value
														0,		// min value
														1000,	// max value
														1);		// step
		startSpinnerYModel = new SpinnerNumberModel(0, 0, 1000, 1);
		
		endSpinnerXModel = new SpinnerNumberModel(0, 0, 999, 1);
		endSpinnerYModel = new SpinnerNumberModel(0, 0, 999, 1);
		
		enemySpinnerXModel = new SpinnerNumberModel(0, 0, 999, 1);
		enemySpinnerYModel = new SpinnerNumberModel(0, 0, 999, 1);
		
		exertionWeightModel = new SpinnerNumberModel(1, 0, 999, 1);
		dangerWeightModel = new SpinnerNumberModel(1, 0, 999, 1);
		
		routeResolutionModel = new SpinnerNumberModel(100, 10, 1000, 1);
		
		gc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		startLabel = new JLabel("Start");
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		add(startLabel, gc);
		
		startXInput = new JSpinner(startSpinnerXModel);
		gc.gridx = 1;
		add(startXInput,gc);
		
		startYInput = new JSpinner(startSpinnerYModel);
		gc.gridx = 2;
		add(startYInput, gc);
		
		endLabel = new JLabel("End");
		gc.gridx = 0;
		gc.gridy = 1;
		add(endLabel, gc);
		
		endXInput = new JSpinner(endSpinnerXModel);
		gc.gridx = 1;
		add(endXInput, gc);
		
		endYInput = new JSpinner(endSpinnerYModel);
		gc.gridx = 2;
		add(endYInput, gc);
		
		exertionWeightLabel = new JLabel("Exertion importance");
		gc.gridy = 2;
		gc.gridx = 0;
		add(exertionWeightLabel, gc);
		
		exertionWeight = new JSpinner(exertionWeightModel);
		gc.gridx = 1;
		gc.gridwidth = 2;
		add(exertionWeight, gc);
		
		dangerWeightLabel = new JLabel("Danger importance");
		gc.gridy = 3;
		gc.gridx = 0;
		gc.gridwidth = 1;
		add(dangerWeightLabel, gc);
		
		dangerWeight = new JSpinner(dangerWeightModel);
		gc.gridx = 1;
		gc.gridwidth = 2;
		add(dangerWeight, gc);
		
		routeResolutionLabel = new JLabel("Route resolution");
		gc.gridy = 4;
		gc.gridx = 0;
		gc.gridwidth = 1;
		add(routeResolutionLabel, gc);
		
		routeResolution = new JSpinner(routeResolutionModel);
		gc.gridx = 1;
		gc.gridwidth = 2;
		add(routeResolution, gc);
		
		enemyLocLabel = new JLabel("Known enemy location");
		gc.gridy = 5;
		gc.gridx = 0;
		gc.gridwidth = 1;
		add(enemyLocLabel, gc);
		
		enemyLocationX = new JSpinner(enemySpinnerXModel);
		gc.gridx = 1;
		add(enemyLocationX, gc);
		
		enemyLocationY = new JSpinner(enemySpinnerYModel);
		gc.gridx = 2;
		add(enemyLocationY, gc);
		
		addEnemy = new JButton("Add");
		gc.gridx = 3;
		add(addEnemy, gc);
		addEnemy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				int[] newEnemyLocation = {(int)enemyLocationX.getValue(),(int) enemyLocationY.getValue()};
				Config.enemyLocations.add(newEnemyLocation);
				GuiMain.getRouteInfoPanel().update();
			}
		});
		
	}
	
	public void setRouteParams(){
		int[] newStart = {(int) startXInput.getValue(), (int) startYInput.getValue()};
		Config.start = newStart;
		
		int[] newEnd = {(int) endXInput.getValue(), (int) endYInput.getValue()};
		Config.end = newEnd;
		
		double newExertionWeight = (int) exertionWeight.getValue();
		Config.exertionWeight = newExertionWeight;
		
		double newDangerWeight = (int) dangerWeight.getValue();
		Config.dangerWeight = newDangerWeight;
		
		double newRouteResolution = (int) routeResolution.getValue();
		Config.routeResolution = newRouteResolution;
	}
}
