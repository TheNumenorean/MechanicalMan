package mechanicalman.flowchart;

import mechanicalman.Debug;

public class Link {
	private Coord startingPoint;
	private Coord endingPoint;
	private double slope;
	private boolean undefinedSlope;
	private FlowChartSymbol from, to;
	private static final int DELTA = 5;

	public Link(Coord p1, Coord p2, FlowChartSymbol from, FlowChartSymbol to) {
		startingPoint = p1;
		endingPoint = p2;
		undefinedSlope = false;
		try {
			slope = MathFormulas.slope(startingPoint, endingPoint);
		} catch (UndefinedSlopeException e) {
			undefinedSlope = true;
		}
		this.from = from;
		this.to = to;
	}

	public Link(int x1, int y1, int x2, int y2, FlowChartSymbol from, FlowChartSymbol to) {
		this(new Coord(x1, y1), new Coord(x2, y2), from, to);
	}

	// Determines whether point passed lies on this line segment
	// Delta factor is use.
	public boolean onLine(int x, int y) {
		Debug.println("Checking Point " + x + ":" + y);
		Debug.println("Starting Point " + startingPoint.getX() + ", " + startingPoint.getY());
		Debug.println("Ending Point " + endingPoint.getX() + ", " + endingPoint.getY());
		Debug.println("UndefinedSlope = " + undefinedSlope);
		Debug.println("Slope = " + slope);
		if (x < Math.min(startingPoint.getX(), endingPoint.getX()) - DELTA)
			return false;
		if (x > Math.max(startingPoint.getX(), endingPoint.getX()) + DELTA)
			return false;
		if (y < Math.min(startingPoint.getY(), endingPoint.getY()) - DELTA)
			return false;
		if (y > Math.max(startingPoint.getY(), endingPoint.getY()) + DELTA)
			return false;
		
		if (!undefinedSlope) {
			double yInt = MathFormulas
					.yIntercept(slope, startingPoint.getX(), startingPoint.getY());
			double value = slope * x + yInt;
			Debug.println("yInt = " + yInt);
			Debug.println("Checking point " + x + ", " + y);
			Debug.println("value = " + value + " checking aginst y = " + y);
			double low = value - DELTA;
			double high = value + DELTA;
			Debug.println("low = " + low + " high = " + high);
			return (value >= low && value <= high);
		} else {
			return true;
		}
	}

	public boolean onLine(Coord coord) {
		return onLine(coord.getX(), coord.getY());
	}

	public boolean equals(Object other) {
		Link temp = (Link) other;
		return (startingPoint.equals(temp.startingPoint) && endingPoint.equals(temp.endingPoint));
	}

	public String toString() {
		return "from " + from + " to " + to + " at coordinates " + startingPoint
				+ " going to coordinate " + endingPoint;
	}

	/**
	 * @return the from
	 */
	public FlowChartSymbol getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public FlowChartSymbol getTo() {
		return to;
	}
}