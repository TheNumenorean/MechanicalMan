package mechanicalman.flowchart;

public final class MathFormulas
{
	// compute slope given two points
	public static double slope(double x1, double y1, double x2, double y2) throws UndefinedSlopeException
	{
		if (x1 != x2)
			return (y2 -y1) / (x2 - x1);
		throw new UndefinedSlopeException("Slope is undefined");
	}
	
	// compute slope given two Coord
	public static double slope(Coord p1, Coord p2) throws UndefinedSlopeException
	{
		if (p1.getX() != p2.getX())
			return ((double)p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
		throw new UndefinedSlopeException("Slope is undefined");
	}
	
	// Find first x from a,b,c efficents
	public static double quadratic1(double a, double b, double c)
	{
		return (-b + Math.sqrt(b*b - 4*a*c)) / (2 * a);
	}

	// Find second x from a,b,c co-efficents
	public static double quadratic2(double a, double b, double c)
	{
		return (-b - Math.sqrt(b*b - 4*a*c)) / (2 * a);
	}
	
	// Return discriminant
	public static double discriminant(double a, double b, double c)
	{
		return b*b - 4*a*c;
	}
	
	// Find x given y, slope and y-intercept
	// slope is not undefined
	public static double computeX(double y, double slope, double yInt)
	{
		return (y - yInt) / slope;
	}
	
	// Find y given x, slope and y-intercept
	// Slope is not undefined
	public static double computeY(double x, double slope, double yInt)
	{
		return slope * x + yInt;
	}
	
	// Find y-intercept given slope and point on line
	// slope is not undefined
	public static double yIntercept(double slope, double x, double y)
	{
		return y - (slope * x);
	}
	
	// Find the distance between two points
	public static double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	// Find the a, b, c coefficents
	public static double[] findABC(double dist, double x, double y, double m, double yInt)
	{
		double a, b, c;
		
		a = 1 + (m * m);
		b = (-2 * x) + (2 * m * (yInt - y));
		c = (x*x) + ((yInt-y)*(yInt-y)) - (dist*dist);
		double[] abc = {a, b, c};
		return abc;
	}
	
	// Given two points find the points on the line distDifference pixles 
	// from the second point in either direction
	public static double[] findPoints(double x1, double y1, 
	                       double x2, double y2, double distDifference)
	{
		double slope = 0;
		try {
			slope = MathFormulas.slope(x1, y1, x2, y2);
		} catch (UndefinedSlopeException e)
		{
			double[] points = {x2, y1 + distDifference, x2, y2 - distDifference};
			return points;
		}
		double yInt = MathFormulas.yIntercept(slope, x2, y2);
		double distance = MathFormulas.distance(x1, y1, x2, y2);
		double abc[] = MathFormulas.findABC(distance - distDifference, x1, y1, slope, yInt);
		double x3a = MathFormulas.quadratic1(abc[0], abc[1], abc[2]);
		double x3b = MathFormulas.quadratic2(abc[0], abc[1], abc[2]);
		double y3a = MathFormulas.computeY(x3a, slope, yInt);
		double y3b = MathFormulas.computeY(x3b, slope, yInt);
		double[] points = { x3a, y3a, x3b, y3b};
		return points;
	}
	
	public static double[] findPoints(double x, double y, double slope, double distanceAway)
	{
		double yInt = MathFormulas.yIntercept(slope, x, y);
		double abc[] = MathFormulas.findABC(distanceAway, x, y, slope, yInt);
		double x3a = MathFormulas.quadratic1(abc[0], abc[1], abc[2]);
		double x3b = MathFormulas.quadratic2(abc[0], abc[1], abc[2]);
		double y3a = MathFormulas.computeY(x3a, slope, yInt);
		double y3b = MathFormulas.computeY(x3b, slope, yInt);
		double[] points = { x3a, y3a, x3b, y3b};
		return points;
	}
}