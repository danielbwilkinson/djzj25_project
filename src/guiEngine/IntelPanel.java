package guiEngine;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class IntelPanel extends JPanel{
	private GridBagConstraints gc;
	private JLabel intelLabel;
	
	public IntelPanel(){
		gc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		intelLabel = new JLabel("intel");
		add(intelLabel, gc);
	}
}
