package mechanicalman;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DataViewerPanel extends JPanel implements Observer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6046252071588805892L;
	private JTextField motor;
	private JTextField location;
	private JTextField facing;
	private JTextField counter;

	public DataViewerPanel()
	{
		System.out.println("*************** DataViewerPanel.constructor() - Beginning **************");
		setBackground(Color.RED);
		Dimension panelDim = new Dimension(600, 60);
		setPreferredSize(panelDim);
		setMaximumSize(panelDim);
		setLayout(new GridLayout(2, 4));	// 2 rows, 6 columns, separate by 5 pixels in both directions
		
		new Dimension(30, 10);
		Dimension textBoxDim = new Dimension(20, 10);
		
		JLabel motorLabel = new JLabel("Motor", SwingConstants.RIGHT);
		motorLabel.setMaximumSize(textBoxDim);
		motorLabel.setMinimumSize(textBoxDim);
		add(motorLabel);
		JTextField motor = new JTextField(5);
		motor.setMaximumSize(textBoxDim);
		motor.setMinimumSize(textBoxDim);
		motor.setEditable(false);
		//motor.setText("Off");
		add(motor);
		
		JLabel postureLabel = new JLabel("Posture", SwingConstants.RIGHT);
		postureLabel.setMaximumSize(textBoxDim);
		postureLabel.setMinimumSize(textBoxDim);
		add(postureLabel);
		JTextField posture = new JTextField(5);
		posture.setMaximumSize(textBoxDim);
		posture.setMinimumSize(textBoxDim);
		posture.setEditable(false);
		//posture.setText("Sitting");
		add(posture);
		
		JLabel facingLabel = new JLabel("Facing", SwingConstants.RIGHT);
		facingLabel.setMaximumSize(textBoxDim);
		facingLabel.setMinimumSize(textBoxDim);
		add(facingLabel);
		JTextField facing = new JTextField(5);
		facing.setMaximumSize(textBoxDim);
		facing.setMinimumSize(textBoxDim);
		facing.setEditable(false);
		//facing.setText("North");
		add(facing);
		
		JLabel wallTestLabel = new JLabel("Wall?", SwingConstants.RIGHT);
		wallTestLabel.setMaximumSize(textBoxDim);
		wallTestLabel.setMinimumSize(textBoxDim);
		add(wallTestLabel);
		JTextField wallTest = new JTextField(5);
		wallTest.setMaximumSize(textBoxDim);
		wallTest.setMinimumSize(textBoxDim);
		wallTest.setEditable(false);
		//facing.setText("North");
		add(wallTest);
		
		JLabel locationLabel = new JLabel("Location", SwingConstants.RIGHT);
		locationLabel.setMaximumSize(textBoxDim);
		locationLabel.setMinimumSize(textBoxDim);
		add(locationLabel);
		JTextField location = new JTextField(5);
		location.setMaximumSize(textBoxDim);
		location.setMinimumSize(textBoxDim);
		location.setEditable(false);
		//location.setText("(0, 0)");	// Not correct value!
		add(location);
		
		JLabel armsLabel = new JLabel("Arms", SwingConstants.RIGHT);
		armsLabel.setMaximumSize(textBoxDim);
		armsLabel.setMinimumSize(textBoxDim);
		add(armsLabel);
		JTextField arms = new JTextField(5);
		arms.setMaximumSize(textBoxDim);
		arms.setMinimumSize(textBoxDim);
		arms.setEditable(false);
		//counter.setText("0");	// Not correct value!
		add(arms);

		JLabel counterLabel = new JLabel("Counter", SwingConstants.RIGHT);
		counterLabel.setMaximumSize(textBoxDim);
		counterLabel.setMinimumSize(textBoxDim);
		add(counterLabel);
		JTextField counter = new JTextField(5);
		counter.setMaximumSize(textBoxDim);
		counter.setMinimumSize(textBoxDim);
		counter.setEditable(false);
		//counter.setText("0");	// Not correct value!
		add(counter);

		JLabel ctrTestLabel = new JLabel("Counter?", SwingConstants.RIGHT);
		ctrTestLabel.setMaximumSize(textBoxDim);
		ctrTestLabel.setMinimumSize(textBoxDim);
		add(ctrTestLabel);
		JTextField ctrTest = new JTextField(5);
		ctrTest.setMaximumSize(textBoxDim);
		ctrTest.setMinimumSize(textBoxDim);
		ctrTest.setEditable(false);
		//facing.setText("North");
		add(ctrTest);
		System.out.println("*************** DataViewerPanel.constructor() - Ending **************");
	}
		
	public void update(Observable o, Object arg)
	{
		MechanicalMan m = (MechanicalMan) o;
		if (m.motorOn())
			motor.setText("On");
		else
			motor.setText("Off");
		location.setText(m.location().toString());
		if (m.areArmsUp())
			motor.setText("Up");
		else
			motor.setText("Down");
		facing.setText(m.dir().toString());
		counter.setText(""+m.counter());
	}
}