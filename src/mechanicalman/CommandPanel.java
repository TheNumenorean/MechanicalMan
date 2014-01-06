package mechanicalman;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mechanicalman.flowchart.program.Program;
import mechanicalman.flowchart.program.ProgramRunner;

public class CommandPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 100, HEIGHT = 680;
	private static final int INTERVAL_MIN = 0;
	private static final int INTERVAL_MAX = 1000; 
	private Program program;
	private JButton prgmRun;
	private MechanicalMan robot;
	private ProgramRunner pRunner;
	private AbstractButton prgmPause;
	private AbstractButton prgmStep;
	private AbstractButton prgmReset;
	private ArrayList<JButton> stdButtons;
	private JSlider slider;

	public CommandPanel(MechanicalMan robot) {
		
		stdButtons = new ArrayList<JButton>();
		
		this.robot = robot;
		
		//System.out.println("CommandPanel.CommandPanel() starting constructor");
		setBackground(Color.green);
		Dimension panelDim = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(panelDim);
		setMaximumSize(panelDim);
		setMinimumSize(panelDim);
		
		CommandPanelController cpc = new CommandPanelController(robot);
		
		JButton start = new JButton("Start");
		start.addActionListener(cpc);
		add(start);
		stdButtons.add(start);
		Debug.println("CommandPanel.CommandPanel() - Added start button");

		JButton stop = new JButton("Stop");
		stop.addActionListener(cpc);
		add(stop);
		stdButtons.add(stop);
		Debug.println("CommandPanel.CommandPanel() - Added stop button");

		JButton standUp = new JButton("Stand Up");
		standUp.addActionListener(cpc);
		add(standUp);
		stdButtons.add(standUp);
		Debug.println("CommandPanel.CommandPanel() - Added stand up button");

		JButton sitDown = new JButton("Sit Down");
		sitDown.addActionListener(cpc);
		add(sitDown);
		stdButtons.add(sitDown);
		Debug.println("CommandPanel.CommandPanel() - Added sit down button");

		JButton armsUp = new JButton("Arms Up");
		armsUp.addActionListener(cpc);
		add(armsUp);
		stdButtons.add(armsUp);
		Debug.println("CommandPanel.CommandPanel() - Added arms up button");

		JButton armsDown = new JButton("Arms Down");
		armsDown.addActionListener(cpc);
		add(armsDown);
		stdButtons.add(armsDown);
		Debug.println("CommandPanel.CommandPanel() - Added arms down button");

		JButton walk = new JButton("Walk");
		walk.addActionListener(cpc);
		add(walk);
		stdButtons.add(walk);
		Debug.println("CommandPanel.CommandPanel() - Added walk button");

		JButton turn = new JButton("Turn");
		turn.addActionListener(cpc);
		add(turn);
		stdButtons.add(turn);
		Debug.println("CommandPanel.CommandPanel() - Added inc button");

		JButton inc = new JButton("Inc");
		inc.addActionListener(cpc);
		add(inc);
		stdButtons.add(inc);
		Debug.println("CommandPanel.CommandPanel() - Added inc button");

		JButton dec = new JButton("Dec");
		dec.addActionListener(cpc);
		add(dec);
		stdButtons.add(dec);
		Debug.println("CommandPanel.CommandPanel() - Added dec button");

		JButton zero = new JButton("Zero");
		zero.addActionListener(cpc);
		add(zero);
		stdButtons.add(zero);
		Debug.println("CommandPanel.CommandPanel() - Added zero button");

		JButton atWall = new JButton("Wall Test");
		atWall.addActionListener(cpc);
		add(atWall);
		stdButtons.add(atWall);
		Debug.println("CommandPanel.CommandPanel() - Added wall test button");

		JButton ctrZero = new JButton("Ctr Test");
		ctrZero.addActionListener(cpc);
		add(ctrZero);
		stdButtons.add(ctrZero);
		Debug.println("CommandPanel.CommandPanel() - Added ctr test button");
		
		prgmRun = new JButton("Run Program");
		prgmRun.setEnabled(false);
		prgmRun.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				pRunner.start(new Runnable(){

					@Override
					public void run() {
						prgmRun.setEnabled(true);
						prgmPause.setEnabled(false);
						prgmStep.setEnabled(true);
						enableButtons(true);
						
						pRunner = program.getProgramRunner(CommandPanel.this.robot);
					}
					
				});
				prgmRun.setEnabled(false);
				prgmPause.setEnabled(true);
				prgmStep.setEnabled(false);
				enableButtons(false);
			}
			
		});
		add(prgmRun);
		
		prgmPause = new JButton("Pause Program");
		prgmPause.setEnabled(false);
		prgmPause.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				pRunner.stop();
				prgmRun.setEnabled(true);
				prgmPause.setEnabled(false);
				prgmStep.setEnabled(true);
				enableButtons(true);
			}
			
		});
		add(prgmPause);
		
		prgmStep = new JButton("Step Program");
		prgmStep.setEnabled(false);
		prgmStep.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				pRunner.step();
			}
			
		});
		add(prgmStep);
		
		prgmReset = new JButton("Reset Program");
		prgmReset.setEnabled(false);
		prgmReset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				pRunner.stop();
				pRunner = program.getProgramRunner(CommandPanel.this.robot);
				pRunner.setInterval(INTERVAL_MAX - slider.getValue());
				prgmRun.setEnabled(true);
				prgmPause.setEnabled(false);
				prgmStep.setEnabled(true);
				enableButtons(true);
			}
			
		});
		add(prgmReset);
		
		slider = new JSlider(JSlider.HORIZONTAL, INTERVAL_MIN, INTERVAL_MAX, INTERVAL_MAX - ProgramRunner.INTERVAL_DEFAULT);
		slider.setEnabled(false);
		slider.setPreferredSize(new Dimension(WIDTH, 20));
		slider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				pRunner.setInterval(INTERVAL_MAX - ((JSlider)e.getSource()).getValue());
			}
			
		});
		add(slider);
		
	}

	/**
	 * Sets the program that the buttons will be acting upon
	 * @param program Program to use
	 */
	public void setProgram(Program program) {
		
		if(pRunner != null)
			pRunner.stop();
		
		this.program = program;
		pRunner = program.getProgramRunner(robot);
		pRunner.setInterval(INTERVAL_MAX - slider.getValue());
		prgmRun.setEnabled(true);
		prgmPause.setEnabled(false);
		prgmStep.setEnabled(true);
		prgmReset.setEnabled(true);
		slider.setEnabled(true);
	}
	
	private void enableButtons(boolean enable){
		for(JButton b : stdButtons)
			b.setEnabled(enable);
	}
}