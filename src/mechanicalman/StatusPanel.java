package mechanicalman;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StatusPanel extends JPanel implements Observer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6667405126765010748L;
	private JTextField status;

	public StatusPanel()
	{
		//System.out.println("*************** StatusPanel.constructor() - Beginning **************");
		setBackground(Color.yellow);
		Dimension panelDim = new Dimension(600, 30);
		setPreferredSize(panelDim);
		setMaximumSize(panelDim);
		setMinimumSize(panelDim);
		
		JLabel statusLabel = new JLabel("Robot's Status", SwingConstants.RIGHT);
		add(statusLabel);
		status = new JTextField(30);
		status.setEditable(false);
		add(status);
		//System.out.println("*************** StatusPanel.constructor() - Ending **************");
	}
		
	public void update(Observable o, Object arg)
	{
		MechanicalMan robot = (MechanicalMan) o;
		//System.out.println("StatusPanel.update() new status is " + robot.getStatus());
		status.setText(robot.getStatus());
	}
}