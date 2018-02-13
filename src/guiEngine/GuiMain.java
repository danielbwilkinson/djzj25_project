package guiEngine;

import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GuiMain {

	//frame, container panel and card layout that all other interfaces will be inside
	private JFrame frame;
	private JPanel container;
	private GridBagLayout gbl;
	private GridBagConstraints gc;
	
	//individual interface declaration
	private ToolPanel toolPanel;
	private IntelPanel intelPanel;
	private RenderPanel renderPanel;
	private RouteInfoPanel routeInfoPanel;
	//individual button declaration
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiMain window = new GuiMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//Initialise frame, container panel and card layout as well as dimensions for objects
		final int frameWidth = 1024;
		final int frameHeight = 720;
		frame = new JFrame("LiDAR Route Planner");
		frame.setBounds(100, 100, frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gbl = new GridBagLayout();
		container = new JPanel();
		container.setLayout(gbl);
		container.setBackground(Color.BLACK);
		
		gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.BOTH;
		
		
		//Initialise homepage and add elements to it
		toolPanel = new ToolPanel();
		intelPanel = new IntelPanel();
		renderPanel = new RenderPanel();
		routeInfoPanel = new RouteInfoPanel();
		
		//add initialised pages to container panel*/
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridwidth = 2;
		gc.gridheight = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		container.add(toolPanel, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 1;
		gc.gridheight = 2;
		gc.weightx = 1;
		gc.weighty = 1;
		container.add(intelPanel, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.weightx = 2;
		gc.weighty = 2;
		container.add(renderPanel, gc);
		
		gc.gridy = 2;
		gc.weighty = 1;
		container.add(routeInfoPanel, gc);
		
		
		frame.getContentPane().add(container);
	}
}

