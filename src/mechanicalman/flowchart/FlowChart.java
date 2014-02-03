package mechanicalman.flowchart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import mechanicalman.Debug;
import mechanicalman.flowchart.FlowChartSymbol.Symbol;
import mechanicalman.flowchart.program.CompileError;
import mechanicalman.flowchart.program.Compiler;
import mechanicalman.flowchart.program.MMFileFilter;
import mechanicalman.flowchart.program.Program;

/**
 * 
 * @author Greg Volger, Francesco Macagno
 * 
 */
public class FlowChart extends JFrame {
	/**
	 * 
	 */

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 660;
	private static final long serialVersionUID = -2529253908475480922L;
	private JMenuItem mNew;
	private JMenuItem mOpen;
	private JMenuItem mClear;
	private JMenuItem mSave;
	private JMenuItem mSaveAs;
	private JMenuItem mCompile;
	private JMenuItem mPrint;
	private JMenuItem mExit;
	private JMenuItem mStart;
	private JMenuItem mStop;
	private JMenuItem mStandUp;
	private JMenuItem mSitDown;
	private JMenuItem mArmsUp;
	private JMenuItem mArmsDown;
	private JMenuItem mWalk;
	private JMenuItem mTurn;
	private JMenuItem mZero;
	private JMenuItem mInc;
	private JMenuItem mDec;
	private JMenuItem mWallTest;
	private JMenuItem mCtrTest;
	private File file;
	private Program program;
	private final FlowChartPanel fcp;

	public static final String VERSION = "3.1.2" + "\nOctober 2013\nGreg Volger\nFrancesco Macagno";

	public FlowChart() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setTitle("Flow Chart Creator");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		fcp = new FlowChartPanel(this);
		fcp.setBackground(Color.WHITE);
		getContentPane().add(fcp);
		setJMenuBar(buildMenu()); // Add the menu bar
		clear();
		setLocation(50, 50);
		// Debug.turnOn();
	}

	private JMenuBar buildMenu() {
		// First menu choice
		JMenu fileMenu = new JMenu("File"); // Create menu choice

		mNew = new JMenuItem("New"); // Create a menu option
		// inner anonymous class
		mNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				file = null;
				FlowChart.this.setTitle("Flow Chart Creator");
				clear();
			}
		});
		fileMenu.add(mNew);

		mOpen = new JMenuItem("Open");
		mOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new MMFileFilter());
				if (fc.showOpenDialog(FlowChart.this) == JFileChooser.APPROVE_OPTION) {
					try {
						FlowChart.this.setTitle("Flow Chart Creator - "
								+ fc.getSelectedFile().getName());
						Compiler.decompile(file = fc.getSelectedFile(), FlowChart.this);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (CompileError e1) {
						e1.printStackTrace();
					}

				}
			}
		});
		fileMenu.add(mOpen);

		mClear = new JMenuItem("Clear All Symbols");
		mClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		fileMenu.add(mClear);

		mSave = new JMenuItem("Save");
		mSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (file == null)
					showSaveDialog();

				if (file != null)
					try {

						program = Compiler.compile(fcp.getSymbols());
						file.createNewFile();
						program.save(file.getPath());
						fcp.setChanged(false);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (CompileError e1) {
						JOptionPane.showMessageDialog(null, e1);
					}
			}
		});
		fileMenu.add(mSave);

		mSaveAs = new JMenuItem("Save As...");
		mSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (showSaveDialog()) {
					try {
						program = Compiler.compile(fcp.getSymbols());
						file.createNewFile();
						program.save(file.getPath());
						fcp.setChanged(false);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (CompileError e1) {
						JOptionPane.showMessageDialog(null, e1);
					}
				}
			}
		});
		fileMenu.add(mSaveAs);

		mCompile = new JMenuItem("Check for Errors");
		mCompile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					program = Compiler.compile(fcp.getSymbols());
					JOptionPane.showMessageDialog(FlowChart.this,
							"Your program has no errors! Congratulations on your omnipotence.");
				} catch (CompileError e1) {
					JOptionPane.showMessageDialog(null, e1);
				}
			}
		});
		fileMenu.add(mCompile);

		mPrint = new JMenuItem("Print");
		// inner anonymous class
		mPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrinterJob pj = PrinterJob.getPrinterJob();
				pj.setJobName("MechanicalMan Flow Chart");

				pj.setPrintable(new Printable() {
					@Override
					public int print(Graphics pg, PageFormat pf, int pageNum) {
						if (pageNum > 0)
							return Printable.NO_SUCH_PAGE;

						Graphics2D g2 = (Graphics2D) pg;
						Debug.println("Print image offset: " + pf.getImageableX() + " "
								+ pf.getImageableY());

						g2.translate(pf.getImageableX(), pf.getImageableY());

						double sx = pf.getImageableWidth() / WINDOW_WIDTH, sy = pf
								.getImageableHeight() / WINDOW_HEIGHT;

						Debug.println("Print scalar: " + sx + " " + sy);

						g2.scale(sx, sy);

						fcp.paint(g2);
						return Printable.PAGE_EXISTS;
					}
				});
				if (pj.printDialog() == false)
					return;

				try {
					pj.print();
				} catch (PrinterException ex) {
					ex.printStackTrace();
				}

			}
		});
		fileMenu.add(mPrint);

		mExit = new JMenuItem("Exit");
		// inner anonymous class
		mExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				FlowChart.this.dispose();
			}
		});
		fileMenu.add(mExit);

		/****************************************************************/

		JMenu symbolMenu = new JMenu("Symbols"); // Create menu choice

		mStart = new JMenuItem("Start");
		// inner anonymous class
		mStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.START));
			}
		});
		symbolMenu.add(mStart);

		mStop = new JMenuItem("Stop");
		mStop.setEnabled(false);
		// inner anonymous class
		mStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.STOP));
			}
		});
		symbolMenu.add(mStop);

		mStandUp = new JMenuItem("Stand Up");
		mStandUp.setEnabled(false);
		// inner anonymous class
		mStandUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.STAND));
			}
		});
		symbolMenu.add(mStandUp);

		mSitDown = new JMenuItem("Sit Down");
		mSitDown.setEnabled(false);
		// inner anonymous class
		mSitDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.SIT));
			}
		});
		symbolMenu.add(mSitDown);

		mArmsUp = new JMenuItem("Arms Up");
		mArmsUp.setEnabled(false);
		// inner anonymous class
		mArmsUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.ARMS_UP));
			}
		});
		symbolMenu.add(mArmsUp);

		mArmsDown = new JMenuItem("Arms Down");
		mArmsDown.setEnabled(false);
		// inner anonymous class
		mArmsDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.ARMS_DOWN));
			}
		});
		symbolMenu.add(mArmsDown);

		mWalk = new JMenuItem("Walk");
		mWalk.setEnabled(false);
		// inner anonymous class
		mWalk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.WALK));
			}
		});
		symbolMenu.add(mWalk);

		mTurn = new JMenuItem("Turn");
		mTurn.setEnabled(false);
		// inner anonymous class
		mTurn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.TURN));
			}
		});
		symbolMenu.add(mTurn);

		mZero = new JMenuItem("Zero");
		mZero.setEnabled(false);
		// inner anonymous class
		mZero.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.ZERO));
			}
		});
		symbolMenu.add(mZero);

		mInc = new JMenuItem("Inc");
		mInc.setEnabled(false);
		// inner anonymous class
		mInc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.INC));
			}
		});
		symbolMenu.add(mInc);

		mDec = new JMenuItem("Dec");
		mDec.setEnabled(false);
		// inner anonymous class
		mDec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.DEC));
			}
		});
		symbolMenu.add(mDec);

		mWallTest = new JMenuItem("Wall Test");
		mWallTest.setEnabled(false);
		// inner anonymous class
		mWallTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.WALL_TEST));
			}
		});
		symbolMenu.add(mWallTest);

		mCtrTest = new JMenuItem("Ctr Test");
		mCtrTest.setEnabled(false);
		// inner anonymous class
		mCtrTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fcp.addSymbol(new FlowChartSymbol(Symbol.CTR_TEST));
			}
		});
		symbolMenu.add(mCtrTest);

		/****************************************************************/

		JMenu helpMenu = new JMenu("Help"); // Create menu choice

		JMenuItem mRefresh = new JMenuItem("Refresh Screen");
		// inner anonymous class
		mRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		helpMenu.add(mRefresh);

		JMenuItem mDirections = new JMenuItem("Directions");
		// inner anonymous class
		mDirections.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(FlowChart.this, DIRECTIONS, "Directions",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		helpMenu.add(mDirections);

		JMenuItem mVersion = new JMenuItem("Version");
		// inner anonymous class
		mVersion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Version " + VERSION, "Flow Chart",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		helpMenu.add(mVersion);

		/************************ Build the menu bar *********************/

		JMenuBar mBar = new JMenuBar();
		mBar.add(fileMenu);
		mBar.add(symbolMenu);
		mBar.add(helpMenu);

		return mBar;
	}

	/**
	 * Clears the entire FlowChart, removing all symbols and setting it as if it
	 * was just opened.
	 */
	public void clear() {

		mStart.setEnabled(true);
		mStop.setEnabled(false);
		mStandUp.setEnabled(false);
		mSitDown.setEnabled(false);
		mArmsUp.setEnabled(false);
		mArmsDown.setEnabled(false);
		mWalk.setEnabled(false);
		mTurn.setEnabled(false);
		mZero.setEnabled(false);
		mInc.setEnabled(false);
		mDec.setEnabled(false);
		mWallTest.setEnabled(false);
		mCtrTest.setEnabled(false);

		fcp.clear();
		repaint();
	}

	/**
	 * Gets the FlowChartPanel
	 * 
	 * @return the FlowChartPanel
	 */
	public FlowChartPanel getFlowChartPanel() {
		return fcp;
	}

	/**
	 * Sets all the buttons except start to on
	 */
	public void setButtonsOn() {
		mStart.setEnabled(false);
		mStop.setEnabled(true);
		mStandUp.setEnabled(true);
		mSitDown.setEnabled(true);
		mArmsUp.setEnabled(true);
		mArmsDown.setEnabled(true);
		mWalk.setEnabled(true);
		mTurn.setEnabled(true);
		mZero.setEnabled(true);
		mInc.setEnabled(true);
		mDec.setEnabled(true);
		mWallTest.setEnabled(true);
		mCtrTest.setEnabled(true);
	}

	/**
	 * Shows the save dialog box and if the user selects a file it will
	 * 
	 * @return True if a file was selected
	 */
	public boolean showSaveDialog() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new MMFileFilter());
		fc.setSelectedFile(file);
		if (fc.showSaveDialog(FlowChart.this) == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (fc.getFileFilter().getClass().equals(MMFileFilter.class)
					&& !file.getName().endsWith(".mm"))
				file = new File(fc.getSelectedFile() + ".mm");
			FlowChart.this.setTitle("Flow Chart Creator - " + file.getName());
			return true;
		}
		return false;
	}

	@Override
	public void dispose() {
		if (!fcp.changed()) {
			super.dispose();
			return;
		}
		switch (JOptionPane.showConfirmDialog(this, "Would you like to save?")) {
		case JOptionPane.YES_OPTION:
			if (file == null)
				showSaveDialog();
			if (file != null)
				try {

					program = Compiler.compile(fcp.getSymbols());
					file.createNewFile();
					program.save(file.getPath());
				} catch (IOException e1) {
					e1.printStackTrace();
					break;
				} catch (CompileError e1) {
					JOptionPane.showMessageDialog(null, e1);
					break;
				}
			else
				break;
		case JOptionPane.NO_OPTION:
			super.dispose();
			break;
		}
	}

	public static final String DIRECTIONS = "To create a program, use the menu options to select and add the modules you want. \nThen, to connect the modules click and drag a module onto another module. This will create a link between the two. \nFor conditional modules, the first time you do the drag it will set the True option, the second time the false option. \nTo delete a link, right click it. To delete a module, right click it. \n When you are ready to run your program, save it as a .mm file and then go back to the main window. \nFrom there, do Program > Load, and use the controls in the bottom corner.\n";
}
