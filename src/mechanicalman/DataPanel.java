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

/**
 * 
 * @author Greg Volger, Francesco Macagno
 *
 */
public class DataPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8226060561417760202L;
	private JTextField motor;
	private JTextField location;
	private JTextField posture;
	private JTextField arms;
	private JTextField facing;
	private JTextField wallTest;
	private JTextField ctrTest;
	private JTextField counter;

	public DataPanel() {
		// System.out.println("*************** DataPanel.constructor() - Beginning **************");
		setBackground(Color.red);
		Dimension panelDim = new Dimension(600, 60);
		setPreferredSize(panelDim);
		setMaximumSize(panelDim);
		setLayout(new GridLayout(2, 4)); // 2 rows, 6 columns, separate by 5
											// pixels in both directions

		Dimension textBoxDim = new Dimension(20, 10);

		JLabel motorLabel = new JLabel("Motor", SwingConstants.RIGHT);
		motorLabel.setMaximumSize(textBoxDim);
		motorLabel.setMinimumSize(textBoxDim);
		add(motorLabel);
		motor = new JTextField(5);
		motor.setMaximumSize(textBoxDim);
		motor.setMinimumSize(textBoxDim);
		motor.setEditable(false);
		// motor.setText("Off");
		add(motor);

		JLabel postureLabel = new JLabel("Posture", SwingConstants.RIGHT);
		postureLabel.setMaximumSize(textBoxDim);
		postureLabel.setMinimumSize(textBoxDim);
		add(postureLabel);
		posture = new JTextField(5);
		posture.setMaximumSize(textBoxDim);
		posture.setMinimumSize(textBoxDim);
		posture.setEditable(false);
		// posture.setText("Sitting");
		add(posture);

		JLabel facingLabel = new JLabel("Facing", SwingConstants.RIGHT);
		facingLabel.setMaximumSize(textBoxDim);
		facingLabel.setMinimumSize(textBoxDim);
		add(facingLabel);
		facing = new JTextField(5);
		facing.setMaximumSize(textBoxDim);
		facing.setMinimumSize(textBoxDim);
		facing.setEditable(false);
		// facing.setText("North");
		add(facing);

		JLabel wallTestLabel = new JLabel("Wall Test", SwingConstants.RIGHT);
		wallTestLabel.setMaximumSize(textBoxDim);
		wallTestLabel.setMinimumSize(textBoxDim);
		add(wallTestLabel);
		wallTest = new JTextField(5);
		wallTest.setMaximumSize(textBoxDim);
		wallTest.setMinimumSize(textBoxDim);
		wallTest.setEditable(false);
		add(wallTest);

		JLabel locationLabel = new JLabel("Location", SwingConstants.RIGHT);
		locationLabel.setMaximumSize(textBoxDim);
		locationLabel.setMinimumSize(textBoxDim);
		add(locationLabel);
		location = new JTextField(5);
		location.setMaximumSize(textBoxDim);
		location.setMinimumSize(textBoxDim);
		location.setEditable(false);
		// location.setText("(0, 0)"); // Not correct value!
		add(location);

		JLabel armsLabel = new JLabel("Arms", SwingConstants.RIGHT);
		armsLabel.setMaximumSize(textBoxDim);
		armsLabel.setMinimumSize(textBoxDim);
		add(armsLabel);
		arms = new JTextField(5);
		arms.setMaximumSize(textBoxDim);
		arms.setMinimumSize(textBoxDim);
		arms.setEditable(false);
		// counter.setText("0"); // Not correct value!
		add(arms);

		JLabel counterLabel = new JLabel("Counter", SwingConstants.RIGHT);
		counterLabel.setMaximumSize(textBoxDim);
		counterLabel.setMinimumSize(textBoxDim);
		add(counterLabel);
		counter = new JTextField(5);
		counter.setMaximumSize(textBoxDim);
		counter.setMinimumSize(textBoxDim);
		counter.setEditable(false);
		// counter.setText("0"); // Not correct value!
		add(counter);

		JLabel ctrTestLabel = new JLabel("Ctr Test", SwingConstants.RIGHT);
		ctrTestLabel.setMaximumSize(textBoxDim);
		ctrTestLabel.setMinimumSize(textBoxDim);
		add(ctrTestLabel);
		ctrTest = new JTextField(5);
		ctrTest.setMaximumSize(textBoxDim);
		ctrTest.setMinimumSize(textBoxDim);
		ctrTest.setEditable(false);
		// facing.setText("North");
		add(ctrTest);
		// System.out.println("*************** DataPanel.constructor() - Ending **************");
	}

	@Override
	public void update(Observable o, Object arg) {
		MechanicalMan m = (MechanicalMan) o;
		// System.out.println("DataPanel.update() - Robot is " + m);

		ctrTest.setText("Not tested");
		wallTest.setText("Not tested");

		if (m.motorOn()) {
			// System.out.println("DataPanel.update() - Setting Motor turned on");
			motor.setText("On");
		} else {
			// System.out.println("DataPanel.update() - Setting Motor turned off");
			motor.setText("Off");
		}

		if (m.standingUp()) {
			// System.out.println("DataPanel.update() - Setting Standing up");
			posture.setText("Standing");
		} else {
			// System.out.println("DataPanel.update() - Setting Sitting down");
			posture.setText("Sitting");
		}

		location.setText(m.location().toString());

		if (m.areArmsUp()) {
			// System.out.println("DataPanel.update() - Setting Arms up");
			arms.setText("Up");
		} else {
			// System.out.println("DataPanel.update() - Setting Arms down");
			arms.setText("Down");
		}

		facing.setText(m.dir().toString());

		if (m.foundWall())
			wallTest.setText("Yes");
		else
			wallTest.setText("No");

		if (m.ctrZero())
			ctrTest.setText("Yes");
		else
			ctrTest.setText("No");

		// System.out.println("DataPanel.update() - Setting counter");
		counter.setText("" + m.counter());
	}
}