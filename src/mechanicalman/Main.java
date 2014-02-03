package mechanicalman;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mechanicalman.flowchart.FlowChart;
import mechanicalman.flowchart.program.MMFileFilter;
import mechanicalman.flowchart.program.Program;

/**
 * 
 * @author Greg Volger, Francesco Macagno
 * 
 */
public class Main extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final MechanicalMan robot;
	private final BoundedGrid env;
	private final int mainWindowWidth;
	private final int mainWindowHeight;
	private final String version = "v4.0.1\nSeptember 2013\nGreg Volger\nFrancesco Macagno";
	private static int NUM_ROWS = 20;
	private static int NUM_COLS = 20;
	private Program program;
	private final CommandPanelController cpController;
	private static Random generator = new Random();
	protected FlowChart flow;
	private final CommandPanel cp;

	public Main() {
		// Debug.turnOn();
		Debug.println("*************** Main.constructor() - Beginning **************"); // debug
		env = new BoundedGrid(NUM_ROWS, NUM_COLS);
		robot = new MechanicalMan(env);

		mainWindowWidth = 800;
		mainWindowHeight = 730;
		setSize(mainWindowWidth, mainWindowHeight);
		setResizable(false); // Disable for JWindow, Enable for JFrame
		setTitle("Mechanical Man"); // Disable for JWindow, Enable for JFrame

		cpController = new CommandPanelController(robot);

		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.X_AXIS));
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension(600, mainWindowHeight));
		leftPanel.setBackground(Color.CYAN);
		cpController.getGridPanelView().setLocation(0, 0);
		leftPanel.add(cpController.getGridPanelView());
		leftPanel.add(cpController.getDataPanelView());
		leftPanel.add(cpController.getStatusPanelView());

		JPanel rightPanel = new JPanel();
		rightPanel.setMaximumSize(new Dimension(40, mainWindowHeight));
		rightPanel.setBackground(Color.RED);
		cp = new CommandPanel(robot);
		rightPanel.add(cp);

		c.add(leftPanel);
		c.add(rightPanel);

		// Disable for JWindow, Enable for JFrame
		setJMenuBar(addMenu()); // Add the menu bar

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		Debug.println("*************** Main.constructor() - Ending **************"); // debug
	}

	/**
	 * Adds the 3 menu choices (Mechanical Man, Flow Chart, About) and all their
	 * sub-choices
	 */
	private JMenuBar addMenu() {
		// First menu choice
		JMenu theMenu = new JMenu("Mechanical Man"); // Create menu choice

		JMenuItem m;
		m = new JMenuItem("Start over"); // Create a menu option
		// inner anonymous class
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				robot.reset();
			}
		});
		theMenu.add(m);

		m = new JMenuItem("Start at new location"); // Create a menu option
		// inner anonymous class
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startNewLocation();
			}
		});
		theMenu.add(m);

		m = new JMenuItem("Start at a random location"); // Create a menu option
		// inner anonymous class
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startRandomLocation();
			}
		});
		theMenu.add(m);

		m = new JMenuItem("Add a door"); // Create a menu option
		// inner anonymous class
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addDoor();
			}
		});
		theMenu.add(m);

		m = new JMenuItem("Add a door at a random location"); // Create a menu
																// option
		// inner anonymous class
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRandomDoor();
			}
		});
		theMenu.add(m);

		m = new JMenuItem("Quit");
		// inner anonymous class
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		theMenu.add(m);

		JMenuBar mBar = new JMenuBar();
		mBar.add(theMenu);

		// *** Second menu choice - Flow Chart
		theMenu = new JMenu("Flow Chart");

		m = new JMenuItem("Open Flow Chart Editor");
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (flow != null) {
					if (!flow.isDisplayable()) {
						flow = new FlowChart();
						flow.setVisible(true);
					} else
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								flow.setState(Frame.NORMAL);
								flow.toFront();
								flow.repaint();
							}
						});
				} else {
					flow = new FlowChart();
					flow.setVisible(true);
				}
			}
		});
		theMenu.add(m);

		JMenuItem mOpen = new JMenuItem("Load Program");
		mOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new MMFileFilter());
				if (fc.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
					try {
						program = new Program(new FileInputStream(fc.getSelectedFile()));
						cp.setProgram(program);
						Main.this.setTitle("MechanicalMan - " + fc.getSelectedFile().getName());
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});
		theMenu.add(mOpen);

		mBar.add(theMenu);

		theMenu = new JMenu("Help");

		m = new JMenuItem("About");
		m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, version, "Mechanical Man",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		theMenu.add(m);

		mBar.add(theMenu);

		return mBar;
	}

	private void startNewLocation() {
		Location loc = null;
		boolean error;
		do {
			error = false;
			try {
				String input = JOptionPane.showInputDialog("Location (row,column)");
				if (input == null)
					return;
				loc = new Location(input);
				if (!env.isValidLocation(loc))
					throw new Exception("Invalid Location!");
			} catch (Exception e) {
				error = true;
				JOptionPane.showMessageDialog(null, e.getMessage());
			}

		} while (error);

		Object[] possibleValues = { "North", "East", "South", "West" };
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose a Direction: ",
				"Direction:", JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
				possibleValues[0]);

		if (selectedValue == null)
			return;

		robot.reset(loc, Direction.getDirection((String) selectedValue));
	}

	private void startRandomLocation() {
		Location loc = new Location(generator.nextInt(NUM_COLS), generator.nextInt(NUM_ROWS));
		Direction dir = Direction.getDirection(generator.nextInt(4) * 90);
		robot.reset(loc, dir);
	}

	private void addDoor() {
		Location loc = null;
		boolean error;
		do {
			error = false;
			try {
				String input = JOptionPane.showInputDialog("Location (x,y)");
				if (input == null)
					return;
				loc = new Location(input);
				if (env.atEdge(loc))
					env.addDoor(loc);
				else {
					JOptionPane.showMessageDialog(null, "Pick an edge location");
					error = true; // Location of door must be an edge
				}
			} catch (Exception e) {
				error = true;
			}
		} while (error);
		robot.repaint();
	}

	private void addRandomDoor() {
		Location loc = null;
		int choice = generator.nextInt(4); // Choose one of the four walls for
											// the door
		if (choice == 0)
			loc = new Location(0, generator.nextInt(NUM_ROWS));
		else if (choice == 1)
			loc = new Location(generator.nextInt(NUM_COLS), 0);
		else if (choice == 2)
			loc = new Location(generator.nextInt(NUM_COLS), NUM_ROWS - 1);
		else
			loc = new Location(NUM_COLS - 1, generator.nextInt(NUM_ROWS));
		env.addDoor(loc);
		robot.repaint();
	}

	public static void main(String[] args) {
		new Main();
	}
}