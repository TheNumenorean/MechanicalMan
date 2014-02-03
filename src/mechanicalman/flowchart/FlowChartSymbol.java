package mechanicalman.flowchart;

import java.util.ArrayList;

import mechanicalman.Debug;

public class FlowChartSymbol {

	/**
	 * Represents different Commands
	 * 
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
		 * 
		 * @return A String representation of a command
		 */
		public String getCommand() {
			return com;
		}
	}

	private final ArrayList<FlowChartSymbol> from;
	private final ArrayList<FlowChartSymbol> to;
	private int rowLocation;
	private int colLocation;
	private boolean selected;
	private final Symbol type;

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
	protected boolean setFrom(FlowChartSymbol fromSymbol) {
		// Make sure there is a from symbol and
		// it is not itself
		if (fromSymbol != null && fromSymbol != this) {
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

		if (toSymbol == null || toSymbol == this || type.equals(Symbol.STOP))
			return false;

		if (getTo().size() > 0) {

			if (type.equals(Symbol.WALL_TEST) || type.equals(Symbol.CTR_TEST)) {

				for (int i = 0; i < getTo().size(); i++) {

					if (getTo().get(i) == null && toSymbol.setFrom(this)) {
						to.set(i, toSymbol);
						return true;
					}

				}

				if (getTo().size() == 1 && toSymbol.setFrom(this)) {
					to.add(toSymbol);
					return true;
				}

			}

			return false;

		}

		if (toSymbol.setFrom(this)) {
			to.add(toSymbol);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return type.name();
	}

	public void removeTo(FlowChartSymbol to) {

		int i = this.to.indexOf(to);

		if (i >= 0)
			this.to.set(i, null);
	}

	public void removeFrom(FlowChartSymbol from) {
		this.from.remove(from);
	}
}