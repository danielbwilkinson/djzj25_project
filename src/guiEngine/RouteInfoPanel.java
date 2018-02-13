package guiEngine;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RouteInfoPanel extends JPanel{
	private GridBagConstraints gc;
	private JLabel routeInfoLabel;
	
	public RouteInfoPanel(){
		gc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		routeInfoLabel = new JLabel("Route info");
		add(routeInfoLabel, gc);
	}
}
