package guiEngine;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engineTester.Config;

public class RouteInfoPanel extends JPanel{
	private GridBagConstraints gc;
	private JLabel routeInfoLabel;
	
	private JButton clearEnemiesButton;
	
	public RouteInfoPanel(){
		gc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		routeInfoLabel = new JLabel("Route info");
		add(routeInfoLabel, gc);
		
		clearEnemiesButton = new JButton("Clear Enemies");
		clearEnemiesButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Config.enemyLocations = new ArrayList<int[]>();
				update();
			}
		});
	}
	
	public void update(){
		gc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		removeAll();
		
		ArrayList<JLabel> enemyLocationLabels = new ArrayList<JLabel>();
		for(int[] enemy : Config.enemyLocations){
			String caption = "Enemy location: " + Integer.toString(enemy[0]) + " " + Integer.toString(enemy[1]);
			System.out.println(caption);
			JLabel enemyLocationLabel = new JLabel(caption);
			enemyLocationLabels.add(enemyLocationLabel);
		}
		
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.gridy = 0;
		for(JLabel label : enemyLocationLabels){
			System.out.println("adding enemy");
			add(label, gc);
			gc.gridy++;
		}
		add(clearEnemiesButton, gc);
		revalidate();
		repaint();
	}
}
