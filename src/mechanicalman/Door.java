package mechanicalman;


public class Door
{
	private Location loc;
	private BoundedGrid env;
	private boolean open;
	
	public Door()
	{
		// Put door at random location
		open = true;
	}
	
	public Door(BoundedGrid theEnv, Location theLoc)
	{
		env = theEnv;
		loc = theLoc;
		open = true;
	}

	public Location location()
	{
		return loc;
	}
	
	public Direction facing()
	{
		Debug.println("loc.getX() = " + loc.getX());
		Debug.println("loc.getY() = " + loc.getY());
		Debug.println("nv.getHeight() = " + env.getHeight());
		Debug.println("nv.getWidth() = " + env.getWidth());
		
		if (loc.getX() == 0)
			return Direction.NORTH;
		else if (loc.getX() == env.getHeight() - 1)
			return Direction.SOUTH;
		else if (loc.getY() == 0)
			return Direction.WEST;
		else if (loc.getY() == env.getWidth() - 1)
			return Direction.EAST;
		return null;
	}
	
	public String toString()
	{
		if (open)
			return "Door is open at location " + loc + " and facing " + facing();
		return "Door is closed at location " + loc;
	}
}