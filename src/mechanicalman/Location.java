package mechanicalman;

import java.util.StringTokenizer;


public class Location
{
	private int xLoc;
	private int yLoc;
	
	public Location()
	{
		xLoc = 0;
		yLoc = 0;
	}
	
	// Copy constructor
	public Location(Location loc)
	{
		xLoc = loc.xLoc;
		yLoc = loc.yLoc;
	}
	
	public Location(String s) throws Exception
	{
		// Parse (x,y)
		StringTokenizer st = new StringTokenizer(s," (),");
		if (st.countTokens() != 2)
			throw new Exception("Need two coordinates");
		String xToken = st.nextToken();
		int x = Integer.parseInt(xToken);
		String yToken = st.nextToken();
		int y = Integer.parseInt(yToken);
		xLoc = x;
		yLoc = y;
	}
	
	public Location(int x, int y)
	{
		xLoc = x;
		yLoc = y;
	}
	
	public int getX()
	{
		return xLoc;
	}
	
	public int getY()
	{
		return yLoc;
	}
	
	public void setX(int x)
	{
		xLoc = x;
	}
	
	public void setY(int y)
	{
		yLoc = y;
	}
	
	/**
	 *	returns only due north, south, east or west ( or -1)
	 */
	public Direction direction(Location to)
	{
		if (xLoc == to.xLoc && yLoc > to.yLoc)
			return Direction.WEST;	// West
		if (xLoc == to.xLoc && yLoc < to.yLoc)
			return Direction.EAST;	// East
		if (yLoc == to.yLoc && xLoc > to.xLoc)
			return Direction.NORTH;	// North
		if (yLoc == to.yLoc && xLoc < to.xLoc)
			return Direction.SOUTH;	// South
		return null;	//	No Direction
	}
	
	public String toString()
	{
		return "(" + xLoc + ", " + yLoc + ")";
	}
	
	public boolean equals(Object obj)
	{
		Location other = (Location) obj;
		return ((xLoc == other.xLoc) && (yLoc == other.yLoc));
	}
}