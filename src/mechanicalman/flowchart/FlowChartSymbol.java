package mechanicalman.flowchart;

import java.util.ArrayList;

import mechanicalman.Debug;

public class FlowChartSymbol {

	/**
	 * Represents different Commands
	 * @author Francesco Macagno
	 *
	 */
	public enum Symbol {
		START("start"), STOP("stop"), STAND("stand"), SIT("sit"), ARMS_UP("armsUp"), ARMS_DOWN(
				"armsDown"), WALK("walk"), TURN("turn"), ZERO("cntReset"), INC("cntInc"), DEC(
				"cntDec"), WALL_TEST("wallTest"), CTR_TEST("cntTest");

		private String com;

		private Symbol(String com) {
			this.com = com;
		}

		/**
		 * Gets the actuall command to be used in programs
		 * @return A String representation of a command
		 */
		public String getCommand() {
			return com;
		}
	}

	private ArrayList<FlowChartSymbol> from;
	private ArrayList<FlowChartSymbol> to;
	private int rowLocation;
	private int colLocation;
	private boolean selected;
	private Symbol type;

	private static final int EMPTY = -1;

	public FlowChartSymbol() {
		this(null);
	}

	public FlowChartSymbol(Symbol type) {
		this(type, null);
	}

	public FlowChartSymbol(Symbol type, FlowChartSymbol comingFrom) {
		this(type, comingFrom, null);
	}

	public FlowChartSymbol(Symbol type, FlowChartSymbol comingFrom, FlowChartSymbol goingTo) {
		this.type = type;
		from = new ArrayList<FlowChartSymbol>();
		if (comingFrom != null)
			from.add(comingFrom);
		to = new ArrayList<FlowChartSymbol>();
		if (goingTo != null)
			to.add(goingTo);
		rowLocation = EMPTY;
		colLocation = EMPTY;
		selected = false;
	}

	public void delete() {
		// Find all pointers to this symbol and remove their to fields
		for (int k = 0; k < from.size(); k++) {
			FlowChartSymbol s = from.get(k);
			s.removeTo(this);
		}

		// Find all symbols this symbol points to and remove their from fields
		for (int k = 0; k < to.size(); k++) {
			FlowChartSymbol s = to.get(k);
			s.removeFrom(this);
		}
	}

	public int getCol() {
		return colLocation;
	}

	public ArrayList<FlowChartSymbol> getFrom() {
		return from;
	}

	public int getRow() {
		return rowLocation;
	}

	public ArrayList<FlowChartSymbol> getTo() {
		return to;
	}

	public Symbol getType() {
		return type;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setCoordinates(int row, int col) {
		rowLocation = row;
		colLocation = col;
	}

	/**
	 * Another Flow Chart symbol is pointing to this Flow Chart symbol so we
	 * need to update this Flow Chart symbol to know it's being pointed to. Make
	 * sure the other Flow Chart symbol is really pointing to this Flow Chart
	 * symbol. Make sure the other Flow Chart symbol is not this Flow Chart
	 * symbol. Set this Flow Chart symbol to be pointed by fromSymbol
	 */
	public boolean setFrom(FlowChartSymbol fromSymbol) {
		// Make sure there is a from symbol and
		// it is not itself
		// and the from symbol points to this symbol
		if (fromSymbol != null && fromSymbol != this && fromSymbol.getTo().contains(this)) {
			from.add(fromSymbol);
			Debug.println("setFrom = true");
			return true;
		}
		Debug.println("setFrom = false");
		return false;
	}

	public void setSelected(boolean select) {
		selected = select;
	}

	/**
	 * Point this Flow Chart symbol to another Flow Chart symbol. This will also
	 * change the existing to field if it points to a FlowChart symbol already.
	 * Change the to field of this Flow Chart symbol and change the from field
	 * of the Flow Chart symbol we want to point to and change the old symbol we
	 * pointed to from's field to null (if there is one);
	 */
	public boolean setTo(FlowChartSymbol toSymbol) {
		// System.out.println("to.size() = " + to.size()); // debug
		// Make sure there is a symbol to point to and we don't point to
		// ourselves
		if (validSetTo(toSymbol)) // Currently not pointing to a symbol
		{
			// System.out.println("Adding a link"); // debug
			to.add(toSymbol);
			if (toSymbol.setFrom(this)) {
				return true;
			}
			to.remove(toSymbol);
			return false;
		} else if (validChangeSetTo(toSymbol)) // Currently pointing to a symbol
												// - Change it
		{
			if (!type.equals(Symbol.WALL_TEST) && !type.equals(Symbol.CTR_TEST)) {
				FlowChartSymbol currentSymbol = to.remove(0);
				to.add(toSymbol);
				if (toSymbol.setFrom(this)) {
					return true;
				}
				to.remove(0);
				to.add(currentSymbol);
				return false;
			} else // Determine the Decision pointer to change
			{
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return type.name();
	}

	private boolean validChangeSetTo(FlowChartSymbol toSymbol) {
		return (toSymbol != null && // There is a toSymbol to point to
				this != toSymbol && // Can't point to self
		// Points to 1 or 2 symbols already
		getTo().size() >= 1);
	}

	private boolean validSetTo(FlowChartSymbol toSymbol) {
		return (toSymbol != null && // There is a toSymbol to point to
				this != toSymbol && // Can't point to self
				!type.equals(Symbol.STOP) && // Can't point out of Stop symbol
		// Nothing currently point to or
		// it's a decision and only 1 currently pointing to
		(getTo().size() == 0 || (getTo().size() == 1)
				&& (type.equals(Symbol.WALL_TEST) || type.equals(Symbol.CTR_TEST))));
	}

	public void removeTo(FlowChartSymbol to) {
		this.to.remove(to);
	}

	public void removeFrom(FlowChartSymbol from) {
		this.from.remove(from);
	}
}