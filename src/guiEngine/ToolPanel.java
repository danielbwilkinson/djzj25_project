package guiEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ToolPanel extends JPanel {
	private JButton openButton;
	private JButton saveButton;
	private JButton waypointButton;
	private JButton enemyButton;
	private JButton friendlyButton;
	private JButton findRouteButton;
	private JButton routeParamsButton;
	private JButton changeModelButton;
	private JPanel fillerPanel;
	private JFileChooser fc;
	private GridBagConstraints gc;
	
	
	public ToolPanel() {
		//Create all objects to appear on page
		//super(cl, container, pageName);
		gc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		
		fc = new JFileChooser();
		
		//// create buttons for tool panel
		openButton = new JButton("Open");
		Font buttonFont = new Font("Ariel", Font.PLAIN, 12);
		openButton.setFont(buttonFont);
		openButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(ToolPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					System.out.println(file.getName());
				} else {
					System.out.println("User cancelled open");
				}
			}
		});
		
		saveButton = new JButton("Save");
		saveButton.setFont(buttonFont);
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(ToolPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					System.out.println(file.getName());
				} else {
					System.out.println("User cancelled open");
				}
			}
		});
		
		waypointButton = new JButton("Waypoint");
		waypointButton.setFont(buttonFont);
		
		enemyButton = new JButton("Enemy");
		enemyButton.setFont(buttonFont);
		
		friendlyButton = new JButton("Friendly");
		friendlyButton.setFont(buttonFont);
		
		findRouteButton = new JButton("Find Route");
		findRouteButton.setFont(buttonFont);
		findRouteButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				GuiMain.getRenderPanel().makeRoute();
			}
		});
		
		routeParamsButton = new JButton("Route Parameters");
		routeParamsButton.setFont(buttonFont);
		
		changeModelButton = new JButton("Change Model");
		changeModelButton.setFont(buttonFont);
		changeModelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				GuiMain.getRenderPanel().changeModel();
			}
		});
		
		// Create page layout
		
		////Add 'go to current medications' button
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.NONE;
		
		gc.weightx = 0;
		gc.weighty = 1;
		
		gc.insets = new Insets(3,3,3,3);
		
		gc.gridx = 0;
		gc.gridy = 0;
		add(openButton, gc);
		
		//// Add 'go to create new medication' button		
		gc.gridx = 1;
		//gc.insets = new Insets(10,10,10,10);
		add(saveButton, gc);
		
		//gc.weighty = 0.1;
		gc.gridx = 2;
		add(waypointButton, gc);
		
		gc.gridx = 3;
		add(enemyButton, gc);
		
		gc.gridx = 4;
		add(friendlyButton, gc);
		
		gc.gridx = 5;
		add(findRouteButton, gc);
		
		gc.gridx = 6;
		add(routeParamsButton, gc);	
		
		gc.gridx = 7;
		add(changeModelButton, gc);
		
		//filler panel to push options to left
		gc.gridx = 8;
		gc.weightx = 1;
		fillerPanel = new JPanel();
		fillerPanel.setBackground(this.getBackground());
		add(fillerPanel, gc);
	}
}

