package mechanicalman.flowchart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mechanicalman.Debug;
import mechanicalman.flowchart.FlowChartSymbol.Symbol;

/**
 * 
 * @author Greg Volger, Francesco Macagno
 * 
 */
public class FlowChartPanel extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7669588397630223723L;
	public static final int GRID_WIDTH = 100;
	public static final int GRID_HEIGHT = 100;
	public static final int MAX_ROWS = 6;
	public static final int MAX_COLS = 8;
	private static final int OVAL_X_OFFSET = 30;
	private static final int OVAL_Y_OFFSET = 30;
	private static final int OVAL_WIDTH = 40;
	private static final int OVAL_HEIGHT = 40;
	private static final int RECT_X_OFFSET = 10;
	private static final int RECT_Y_OFFSET = 30;
	private static final int RECT_WIDTH = 80;
	private static final int RECT_HEIGHT = 40;
	private static final int DIAMOND_X_OFFSET = 25;
	private static final int DIAMOND_Y_OFFSET = 25;
	private static final int DIAMOND_DIAGONAL = 50;

	private static Coord bottomCoord(FlowChartSymbol s) {
		Coord c = new Coord();
		if (s.getType().equals(Symbol.START) || s.getType().equals(Symbol.STOP)) {
			c.setX(GRID_WIDTH / 2); // 50
			c.setY(GRID_HEIGHT - OVAL_Y_OFFSET); // 70
		} else if (s.getType().equals(Symbol.WALL_TEST) || s.getType().equals(Symbol.CTR_TEST)) {
			c.setX(GRID_WIDTH / 2); // 50
			c.setY(GRID_HEIGHT - DIAMOND_Y_OFFSET); // 75
		} else {// Rectangle
			c.setX(GRID_WIDTH / 2); // 50
			c.setY(GRID_HEIGHT - RECT_Y_OFFSET); // 70
		}
		return c;
	}

	/**
	 * from symbol has at least one link coming out of it to symbol exists
	 * Returns the coodinates for the arrow relative to it's grid c[0] -> (x,y)
	 * coordinates of the from symbol c[1] -> (x,y) coordinates of the to symbol
	 * 
	 * @param from
	 * @param to
	 * @param out
	 * @return
	 */
	public static Coord[] getCoord(FlowChartSymbol from, FlowChartSymbol to, int out) {
		Coord[] c = new Coord[2];
		c[0] = new Coord();
		c[1] = new Coord();

		int r1 = from.getRow();
		int c1 = from.getCol();
		int r2 = to.getRow();
		int c2 = to.getCol();

		if (r1 == r2) // Same row
		{
			if (c1 > c2) // Pointing left to right
			{
				c[0] = leftCoord(from);
				c[1] = rightCoord(to);
			} else // Pointing right to left
			{
				c[0] = rightCoord(from);
				c[1] = leftCoord(to);
			}
		} else // differnet rows
		{
			if (r1 > r2) // Pointing top to bottom
			{
				c[0] = topCoord(from);
				c[1] = bottomCoord(to);
			} else // pointing bottom to top
			{
				c[0] = bottomCoord(from);
				c[1] = topCoord(to);
			}
		}

		if (from.getType().equals(Symbol.WALL_TEST) || from.getType().equals(Symbol.CTR_TEST)) {
			Debug.println("from.getTo().size() = " + from.getTo().size()); // debug
			if (out == 0)
				c[0] = rightCoord(from); // Yes arrow always comes out of right
											// side.
			else
				c[0] = bottomCoord(from); // No arrow always comes out of the
											// bottom
		}

		c[0].setX(c[0].getX() + c1 * GRID_HEIGHT);
		c[0].setY(c[0].getY() + r1 * GRID_WIDTH);

		c[1].setX(c[1].getX() + c2 * GRID_HEIGHT);
		c[1].setY(c[1].getY() + r2 * GRID_WIDTH);

		return c;
	}

	private static Coord leftCoord(FlowChartSymbol s) {
		Coord c = new Coord();
		if (s.getType().equals(Symbol.START) || s.getType().equals(Symbol.STOP)) {
			c.setX(OVAL_X_OFFSET); // 40
			c.setY(GRID_WIDTH / 2); // 50
		} else if (s.getType().equals(Symbol.WALL_TEST) || s.getType().equals(Symbol.CTR_TEST)) {
			c.setX(DIAMOND_X_OFFSET); // 25
			c.setY(GRID_WIDTH / 2); // 50
		} else // Rectangle
		{
			c.setX(RECT_X_OFFSET); // 10
			c.setY(GRID_WIDTH / 2); // 50
		}
		return c;
	}

	private static Coord rightCoord(FlowChartSymbol s) {
		Coord c = new Coord();
		if (s.getType().equals(Symbol.START) || s.getType().equals(Symbol.STOP)) {
			c.setX(GRID_WIDTH - OVAL_X_OFFSET); // 70
			c.setY(GRID_WIDTH / 2); // 50
		} else if (s.getType().equals(Symbol.WALL_TEST) || s.getType().equals(Symbol.CTR_TEST)) {
			c.setX(GRID_WIDTH - DIAMOND_X_OFFSET); // 75
			c.setY(GRID_WIDTH / 2); // 50
		} else // Rectangle
		{
			c.setX(GRID_WIDTH - RECT_X_OFFSET); // 90
			c.setY(GRID_WIDTH / 2); // 50
		}
		return c;
	}

	private static Coord topCoord(FlowChartSymbol s) {
		Coord c = new Coord();
		if (s.getType().equals(Symbol.START) || s.getType().equals(Symbol.STOP)) {
			c.setX(GRID_WIDTH / 2); // 50
			c.setY(OVAL_Y_OFFSET); // 30
		} else if (s.getType().equals(Symbol.WALL_TEST) || s.getType().equals(Symbol.CTR_TEST)) {
			c.setX(GRID_WIDTH / 2); // 50
			c.setY(DIAMOND_Y_OFFSET); // 25
		} else // Rectangle
		{
			c.setX(GRID_WIDTH / 2); // 50
			c.setY(RECT_Y_OFFSET); // 10
		}
		return c;
	}

	private final FlowChart fc;

	private final FlowChartSymbol[][] symbols;

	private int numSymbols;

	private FlowChartSymbol symbolToBeMoved;

	private final ArrayList<Link> links;

	private boolean changed;

	public FlowChartPanel(FlowChart fc) {
		this.fc = fc;
		symbols = new FlowChartSymbol[MAX_ROWS][MAX_COLS];

		changed = false;
		numSymbols = 0;
		symbolToBeMoved = null;
		links = new ArrayList<Link>();

		addMouseListener(this); // Track the mouse
		addMouseMotionListener(this); // Track dragging the mouse
	}

	// Add a symbol to the grid
	public boolean addSymbol(FlowChartSymbol symbol) {
		if (numSymbols >= MAX_ROWS * MAX_COLS) {
			JOptionPane.showMessageDialog(null, "Flow chart is full!");
			return false;
		}
		int r = numSymbols / MAX_COLS;
		int c = numSymbols % MAX_COLS;
		if (symbols[r][c] != null) {
			Coord co = findFirstEmptyLocation();
			r = co.getX();
			c = co.getY();
		}
		symbol.setCoordinates(r, c);
		symbols[r][c] = symbol;
		numSymbols++;

		if (symbol.getType().equals(Symbol.START)) {
			fc.setButtonsOn();
		}

		repaint();
		setChanged();
		return true;
	}

	/**
	 * Gets whether the panel has been changed since the last setChanged(false);
	 * 
	 * @return True if it has been changed, false otherwise.
	 */
	public boolean changed() {
		return changed;
	}

	public void clear() {
		for (int r = 0; r < MAX_ROWS; r++)
			for (int c = 0; c < MAX_COLS; c++)
				symbols[r][c] = null;

		numSymbols = 0;

		setChanged(false);

	}

	private void clearAllSelectedSymbols() {
		for (int r = 0; r < MAX_ROWS; r++)
			for (int c = 0; c < MAX_COLS; c++)
				if (symbols[r][c] != null)
					symbols[r][c].setSelected(false);
	}

	private Link clickedOnLink(int x, int y) {
		int xoffset = x;
		int yoffset = y;
		Coord c = new Coord(xoffset, yoffset);
		System.out.println("Clicked link: " + c + " " + x + " " + y);
		for (Link l : links) {
			Debug.println("Checking link " + l);
			if (l.onLine(c))
				return l;
		}
		return null;
	}

	/*
	 * Determines with more prescision if the user click on a symbol not just
	 * the grid location a symbol is located in.
	 */
	private boolean clickedOnSymbol(FlowChartSymbol fcs, int xloc, int yloc) {
		final int HALF_WIDTH = GRID_WIDTH / 2;
		final int HALF_HEIGHT = GRID_HEIGHT / 2;
		if (fcs == null)
			return false;
		int xoffset = xloc % GRID_WIDTH;
		int yoffset = yloc % GRID_HEIGHT;
		if (fcs.getType().equals(Symbol.START) || fcs.getType().equals(Symbol.STOP)) {
			if (Math.sqrt((HALF_WIDTH - xoffset) * (HALF_WIDTH - xoffset) + (HALF_HEIGHT - yoffset)
					* (HALF_HEIGHT - yoffset)) <= OVAL_WIDTH / 2) {
				// Got a circle exactly
				Debug.println("Got a CIRCLE");
				return true;
			} else
				return false;
		} else if (fcs.getType().equals(Symbol.WALL_TEST) || fcs.getType().equals(Symbol.CTR_TEST)) {
			// Got a diamond exactly
			final int BOTTOM = DIAMOND_Y_OFFSET + DIAMOND_DIAGONAL;
			if ((xoffset >= HALF_WIDTH && xoffset <= BOTTOM)
					&& (yoffset >= DIAMOND_Y_OFFSET && yoffset <= HALF_HEIGHT))
				return (yoffset >= xoffset - DIAMOND_X_OFFSET);
			else if ((xoffset >= DIAMOND_X_OFFSET && xoffset <= HALF_WIDTH)
					&& (yoffset >= DIAMOND_Y_OFFSET && yoffset <= HALF_HEIGHT))
				return (yoffset >= -xoffset + BOTTOM);
			else if ((xoffset >= DIAMOND_X_OFFSET && xoffset <= HALF_WIDTH)
					&& (yoffset >= HALF_HEIGHT && yoffset <= BOTTOM))
				return (yoffset <= xoffset + DIAMOND_X_OFFSET);
			else if ((xoffset >= HALF_WIDTH && xoffset <= BOTTOM)
					&& (yoffset >= HALF_HEIGHT && yoffset <= BOTTOM))
				return (yoffset <= -xoffset + BOTTOM + DIAMOND_DIAGONAL);
			else
				return false;
		} else // Must be a rectangle
		{
			if (RECT_X_OFFSET <= xoffset && xoffset <= (GRID_WIDTH - RECT_X_OFFSET)
					&& RECT_Y_OFFSET <= yoffset && yoffset <= (GRID_WIDTH - RECT_Y_OFFSET)) {
				// Got a rectangle exactly
				Debug.println("Got a RECTANGLE");
				return true;
			} else
				return false;
		}
	}

	// Create the arrow points for a pologon to draw the arrow
	// The arrow is draw at the end of the line segment
	// Starting points are (x1,y1)
	// Ending points are (x2,y2)
	private void createArrowPoints(int x1, int y1, int x2, int y2, int[] xPoints, int[] yPoints) {
		// System.out.println("Testing points (" + x1 + " , " + y1 + ") and (" +
		// x2 + " , " + y2 + ")");

		// Determine the two points from 2 points and distance from end point
		double[] points = { -1, -1, -1, -1 };
		if (x1 != x2) {
			points = MathFormulas.findPoints(x1, y1, x2, y2, 10);
			// System.out.println("Point 1 is = (" + points[0] + " , " +
			// points[1] + ")");
			// System.out.println("Point 2 is = (" + points[2] + " , " +
			// points[3] + ")");
			// which set of points?
			if (x2 > x1) {
				// Arrow is going to the right
				double[] realPoints = { -1, -1, -1, -1 };
				// System.out.println("Choose this point = (" + points[0] +
				// " , " + points[1] + ")");
				if (y1 != y2)
					try {
						realPoints = MathFormulas.findPoints(points[0], points[1], -1.0
								/ MathFormulas.slope(x1, y1, x2, y2), 5);
					} catch (UndefinedSlopeException e) {
						JOptionPane.showMessageDialog(null,
								"Undefined Slope - This error should never occur");
					}
				else {
					// System.out.println("Pointing directly to the Right");
					realPoints[0] = points[0];
					realPoints[1] = points[1] - 5;
					realPoints[2] = points[0];
					realPoints[3] = points[1] + 5;
				}
				xPoints[0] = (int) Math.round(realPoints[0]);
				yPoints[0] = (int) Math.round(realPoints[1]);
				xPoints[1] = (int) Math.round(realPoints[2]);
				yPoints[1] = (int) Math.round(realPoints[3]);
				// System.out.println("Point 1 is = (" + realPoints[0] + " , " +
				// realPoints[1] + ")");
				// System.out.println("Point 2 is = (" + realPoints[2] + " , " +
				// realPoints[3] + ")");
			} else {
				// Arrow is going to the left
				// System.out.println("Choose this point = (" + points[2] +
				// " , " + points[3] + ")");
				double[] realPoints = { -1, -1, -1, -1 };
				if (y1 != y2)
					try {
						realPoints = MathFormulas.findPoints(points[2], points[3], -1.0
								/ MathFormulas.slope(x1, y1, x2, y2), 5);
					} catch (UndefinedSlopeException e) {
						JOptionPane.showMessageDialog(null,
								"Undefined Slope - This error should never occur");
					}
				else {
					// System.out.println("Pointing directly to the Left");
					realPoints[0] = points[2];
					realPoints[1] = points[3] - 5;
					realPoints[2] = points[2];
					realPoints[3] = points[3] + 5;
				}
				xPoints[0] = (int) Math.round(realPoints[0]);
				yPoints[0] = (int) Math.round(realPoints[1]);
				xPoints[1] = (int) Math.round(realPoints[2]);
				yPoints[1] = (int) Math.round(realPoints[3]);
				// System.out.println("Point 1 is = (" + realPoints[0] + " , " +
				// realPoints[1] + ")");
				// System.out.println("Point 2 is = (" + realPoints[2] + " , " +
				// realPoints[3] + ")");
			}
		} else // x1 == x2
		{
			if (y1 > y2) // Pointing directly Up
			{
				// System.out.println("Pointing directly up");
				points[0] = x2 - 5;
				points[1] = y2 + 5;
				points[2] = x2 + 5;
				points[3] = y2 + 5;
			} else // Point directly down
			{
				// System.out.println("Pointing directly down");
				points[0] = x1 - 5;
				points[1] = y2 - 5;
				points[2] = x1 + 5;
				points[3] = y2 - 5;
			}
			xPoints[0] = (int) Math.round(points[0]);
			yPoints[0] = (int) Math.round(points[1]);
			xPoints[1] = (int) Math.round(points[2]);
			yPoints[1] = (int) Math.round(points[3]);
			// System.out.println("Point 1 is = (" + points[0] + " , " +
			// points[1] + ")");
			// System.out.println("Point 2 is = (" + points[2] + " , " +
			// points[3] + ")");
		}
	}

	/**
	 * Returns the first empty location in the view grid
	 */
	private Coord findFirstEmptyLocation() {
		for (int r = 0; r < symbols.length; r++) {
			for (int c = 0; c < symbols[r].length; c++) {
				if (symbols[r][c] == null)
					return new Coord(r, c);
			}
		}
		return null;
	}

	// Given pixel location return symbol in that cell
	private FlowChartSymbol getSymbol(int xloc, int yloc) {
		int row = (yloc) / GRID_HEIGHT;
		int col = xloc / GRID_WIDTH;
		Debug.println(yloc + " getSymbol -> " + row);
		Debug.println(xloc + " getSymbol -> " + col);
		if (row == -1 || col == -1)
			return null;
		return symbols[row][col];
	}

	public FlowChartSymbol[][] getSymbols() {
		return symbols;
	}

	/*************** MouseListener event handlers *******************/

	@Override
	public void mouseClicked(MouseEvent event) {
		int xloc = event.getX();
		int yloc = event.getY();
		if (event.getButton() == MouseEvent.BUTTON3) // Check for a Right-Click
		{
			// Make sure it is on a symbol
			FlowChartSymbol fcs = getSymbol(xloc, yloc);
			if (clickedOnSymbol(fcs, xloc, yloc)) {
				// can't delete the Start button
				if (fcs.getType().equals(Symbol.START)) {
					JOptionPane.showMessageDialog(null, "You can't delete the Start symbol");
					return;
				}
				fcs.setSelected(true);
				repaint();
				// Prompt to only allow Yes or No buttons
				// Give the name of the symbol to delete in the prompt
				int choice = JOptionPane.showConfirmDialog(this, "Delete " + fcs);
				if (choice == JOptionPane.YES_OPTION) {
					// Delete the symbol
					fcs.delete();
					removeSymbol(fcs);
					repaint();
					setChanged();
				} else {
					fcs.setSelected(false);
					repaint();
				}
			} else {
				Link l = clickedOnLink(xloc, yloc);
				if (l != null) {
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
							"Delete link involving " + l.getFrom() + " and " + l.getTo() + "?")) {
						links.remove(l);
						l.getFrom().removeTo(l.getTo());
						l.getTo().removeFrom(l.getFrom());
						repaint();
					}
				} else
					Debug.println("Missed the link");
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	// MouseMotionListener methods

	@Override
	public void mouseMoved(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		clearAllSelectedSymbols();
		// Determine which symbol has been selected.
		int xloc = event.getX();
		int yloc = event.getY();
		int row = yloc / GRID_HEIGHT;
		int col = xloc / GRID_WIDTH;
		Debug.println(yloc + " mousePressed -> " + row);
		Debug.println(xloc + " mousePressed -> " + col);
		symbolToBeMoved = symbols[row][col];
		// Determine if it really cicked on the symbol if there is on
		if (!clickedOnSymbol(symbols[row][col], xloc, yloc)) {
			symbolToBeMoved = null;
		} else {
			symbolToBeMoved.setSelected(true);
		}
		repaint();
		Debug.println("Pressed mouse down " + xloc + " " + yloc);
		Debug.println("row = " + row + " col = " + col);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// Determine where to place a symbol
		int xloc = event.getX();
		int yloc = event.getY();
		int row = yloc / GRID_HEIGHT;
		int col = xloc / GRID_WIDTH;
		if (symbolToBeMoved != null && symbols[row][col] == null) // Move a
																	// symbol
		{
			symbols[symbolToBeMoved.getRow()][symbolToBeMoved.getCol()] = null;
			symbolToBeMoved.setCoordinates(row, col);
			symbols[row][col] = symbolToBeMoved;
			Debug.println("Released mouse after drag " + xloc + " " + yloc);
			Debug.println("row = " + row + " col = " + col);

			ArrayList<Link> tmp = new ArrayList<Link>();
			for (Link l : links) {
				if (l.getTo().equals(symbolToBeMoved) || l.getFrom().equals(symbolToBeMoved))
					tmp.add(l);
			}
			links.removeAll(tmp);

			for (Link l : tmp) {
				Coord[] c = getCoord(l.getFrom(), l.getTo(),
						l.getFrom().getTo().indexOf(symbolToBeMoved));
				links.add(new Link(c[0], c[1], l.getFrom(), l.getTo()));
			}

			repaint();
		} else if (symbolToBeMoved != null && symbols[row][col] != null) {

			if (symbolToBeMoved.setTo(symbols[row][col])) {

				ArrayList<Link> tmp = new ArrayList<Link>();
				for (Link l : links) {
					if (l.getFrom().equals(symbolToBeMoved) && l.getTo().equals(symbols[row][col]))
						tmp.add(l);
				}
				links.removeAll(tmp);

				Debug.println("Index: " + symbolToBeMoved.getTo().indexOf(symbolToBeMoved));
				Coord[] c = getCoord(symbolToBeMoved, symbols[row][col], symbolToBeMoved.getTo()
						.indexOf(symbols[row][col]));
				Link link = new Link(c[0], c[1], symbolToBeMoved, symbols[row][col]);
				links.add(link);
				Debug.println(links.toString());
				repaint();
				setChanged();
			}
		} else
			symbolToBeMoved = null;
	}

	/**
	 * Total area is WINDOW_HEIGHT (800) pixels wide by WINDOW_WIDTH (660)
	 * pixels high Each grid location is GRID_WIDHT (100) pixles wide by
	 * GRID_HEIGHT (100) pixels high. The circle symbol is 40 pixles wide and 40
	 * pixles high The rectangle symbol is 80 pixels wide and 40 pixles high The
	 * test symbol is 50 pixles hig and 50 pixels high
	 * 
	 * 0 - Is an empty cell 1 - Is a START symbol 2 - Is a STOP symbol 3 - Is a
	 * SATND UP symbol 4 - Is a SIT DOWN symbol 5 - Is a ARMS UP symbol 6 - Is a
	 * ARMS DOWN symbol 7 - Is a WALK symbol 8 - Is a TURN symbol 9 - Is a ZERO
	 * symbol 10 - Is a INC symbol 11 - Is a DEC symbol 12 - Is a WALL TEST
	 * symbol 13 - Is a CTR TEST symbol
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(Color.black);
		for (int r = 0; r < MAX_ROWS; r++) {
			for (int c = 0; c < MAX_COLS; c++) {
				// Draw the figure in the grid at location (r,c)
				int xLoc = c * GRID_HEIGHT;
				int yLoc = r * GRID_WIDTH;
				int x[] = { xLoc + 25, xLoc + 50, xLoc + 75, xLoc + 50 };
				int y[] = { yLoc + 50, yLoc + 25, yLoc + 50, yLoc + 75 };
				if (symbols[r][c] != null) {
					Graphics2D g2 = (Graphics2D) g;
					if (symbols[r][c].isSelected())
						g2.setStroke(new BasicStroke(3));
					else
						g2.setStroke(new BasicStroke(1));
					switch (symbols[r][c].getType()) {
					case START:
						g2.drawOval(xLoc + OVAL_X_OFFSET, yLoc + OVAL_Y_OFFSET, OVAL_WIDTH,
								OVAL_HEIGHT);
						g.drawString("Start", xLoc + 37, yLoc + 55);
						break;
					case STOP:
						g2.drawOval(xLoc + OVAL_X_OFFSET, yLoc + OVAL_Y_OFFSET, OVAL_WIDTH,
								OVAL_HEIGHT);
						g.drawString("Stop", xLoc + 38, yLoc + 55);
						break;
					case STAND:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Stand Up", xLoc + 22, yLoc + 55);
						break;
					case SIT:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Sit Down", xLoc + 22, yLoc + 55);
						break;
					case ARMS_UP:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Arms Up", xLoc + 22, yLoc + 55);
						break;
					case ARMS_DOWN:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Arms Down", xLoc + 18, yLoc + 55);
						break;
					case WALK:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Walk", xLoc + 34, yLoc + 55);
						break;
					case TURN:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Turn", xLoc + 34, yLoc + 55);
						break;
					case ZERO:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Zero", xLoc + 34, yLoc + 55);
						break;
					case INC:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Inc", xLoc + 40, yLoc + 55);
						break;
					case DEC:
						g2.drawRect(xLoc + RECT_X_OFFSET, yLoc + RECT_Y_OFFSET, RECT_WIDTH,
								RECT_HEIGHT);
						g.drawString("Dec", xLoc + 40, yLoc + 55);
						break;
					case WALL_TEST:
						g2.drawPolygon(x, y, 4);
						g.drawString("Wall?", xLoc + 35, yLoc + 55);
						g.drawString("Yes", xLoc + 70, yLoc + 55);
						g.drawString("No", xLoc + 42, yLoc + 82);
						break;
					case CTR_TEST:
						g2.drawPolygon(x, y, 4);
						g.drawString("Ctr?", xLoc + 39, yLoc + 55);
						g.drawString("Yes", xLoc + 70, yLoc + 55);
						g.drawString("No", xLoc + 42, yLoc + 82);
						break;
					}
					// Draw the arrow from this symbol (if there is one)
					ArrayList<FlowChartSymbol> goingTo = symbols[r][c].getTo();
					if (goingTo != null) {
						// Draw all arrows out of symbols[r][c];
						ArrayList<FlowChartSymbol> drawTo = symbols[r][c].getTo();
						for (int k = 0; k < drawTo.size(); k++) {
							if (drawTo.get(k) != null) {
								Coord coords[] = getCoord(symbols[r][c], drawTo.get(k), k);
								int x1 = coords[0].getX();
								int y1 = coords[0].getY();
								int x2 = coords[1].getX();
								int y2 = coords[1].getY();
								Debug.println("Draw line (" + x1 + "," + y1 + ") to (" + x2 + ","
										+ y2 + ")");
								g2.setStroke(new BasicStroke(1));
								g.drawLine(x1, y1, x2, y2);
								int[] xp = { 0, 0, x2 };
								int[] yp = { 0, 0, y2 };
								createArrowPoints(x1, y1, x2, y2, xp, yp);
								g.fillPolygon(xp, yp, 3);
							}
						}
					}
				}
			}
		}
	}

	// Remove symbol from grid
	private void removeSymbol(FlowChartSymbol fcs) {
		// Remove symbol from grid
		symbols[fcs.getRow()][fcs.getCol()] = null;
		// Remove links to this symbol
		fcs.delete();
		numSymbols--;
		setChanged();
	}

	/**
	 * Convenience method that calls setChanged(true);
	 */
	public void setChanged() {
		setChanged(true);
	}

	/**
	 * Sets the state of this FlowChartPanel as either changed or not.
	 * 
	 * @param changed
	 *            The value to set the changed status to.
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
		Debug.println("Setting chancged to " + changed);
	}
}
