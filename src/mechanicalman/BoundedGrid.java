package mechanicalman;


/**
 *	This creates boundred grid for the Mechanical Man (or any object) to
 *	move around in.  Creates one possible door in the grid.
 */
 public class BoundedGrid
{
	private Object[][] theGrid;
	private Door door;
	
	/**
	 *	Returns the grid width X height
	 *	@param width the width of the grid to create
	 *	@param height the height of the grid to create
	 *	@postcondition a grid is created width X height
	 */
	public BoundedGrid(int width, int height)
	{
		theGrid = new Object[width][height];
		init();
	}
	
	/**
	 *	Sets every location in the grid to null to indiacte
	 *	all locations within the grid are empty
	 */
	public void init()
	{
		door = null;
		for (int r = 0; r < theGrid.length; r++)
			for (int c = 0; c < theGrid[r].length; c++)
				theGrid[r][c] = null;
	}
	
	/**
	 *	Trys to place an object into the grid
	 *	@param loc the location to attempt to place the object
	 *	@param o the object to place into the grid
	 *	@return returns true loc is empty and o is placed into the grid,
	 *			otherwise returns false
	 */
	public boolean set(Location loc, Object o)
	{
		if (isEmpty(loc))
		{
			theGrid[loc.getX()][loc.getY()] = o;
			return true;
		}
		return false;
	}
	
	/**
	 *	Moves an object from oldLoc to newLoc if newLoc is valid and empty
	 *	@param oldLoc the current location of object to move
	 *	@param newLoc the location to move the object to
	 *	@param o the object to move
	 *	@return returns true if the object was moved otherwise returns false
	 *	@postcondition moves the object o to newLoc if possible
	 */
	public boolean move(Location oldLoc, Location newLoc, Object o)
	{
		if (isValidLocation(newLoc) && isEmpty(newLoc))
		{
			theGrid[oldLoc.getX()][oldLoc.getY()] = null;
			theGrid[newLoc.getX()][newLoc.getY()] = o;
			return true;
		}
		return false;
	}
	
	/**
	 *	Returns true if loc is at an edge of the grid
	 *	@param loc the location to check to see if it is at edge
	 *	@return returns true if loc is on edge of grid otherwise returns false
	 */
	public boolean atEdge(Location loc)
	{
		int x = loc.getX();
		int y = loc.getY();
		return ((x == 0) || (y == 0) || (x == theGrid.length - 1) || (y == theGrid[x].length - 1));
	}
	
	/**
	 *	Returns true if loc is valid location within the grid
	 *	@param loc location to check to see if it's in the grid
	 *	@return returns true if loc is in the grid otherwise returns false
	 */
	public boolean isValidLocation(Location loc)
	{
		int x = loc.getX();
		int y = loc.getY();
		return (x >= 0 && x < theGrid.length && y >= 0 && y < theGrid[x].length);
	}
	
	/**
	 *	Determines if a lcoation is within the grid and empty
	 *	@param loc location to check within grid to be empty
	 *	@return returns true is loc is within the grid and is empty,
	 *			otherwise returns false
	 */
	public boolean isEmpty(Location loc)
	{
		return (isValidLocation(loc) && theGrid[loc.getX()][loc.getY()] == null);
	}
	
	/**
	 *	Returns the width of the the grid
	 *	@return returns the width of the grid
	 */
	public int getWidth()
	{
		return theGrid.length;
	}
	
	/**
	 *	Returns the height of the the grid
	 *	@return returns the height of the grid
	 */
	public int getHeight()
	{
		return theGrid[0].length;
	}
	
	/**
	 *	Removes the door from the grid
	 */
	public void removeDoor()
	{
		door = null;
	}
	
	/**
	 *	Adds (or moves) a door to a location
	 *	@param loc the location of the door
	 *	@return returns true if loc is on the edge of the grid,
	 *			and the location is empty, then places door at location loc,
	 *			otherwise returns false
	 *
	 */
	public boolean addDoor(Location loc)
	{
		if (!atEdge(loc))
			return false;
		if (theGrid[loc.getX()][loc.getY()] != null)	// Something already there!
			return false;
		door = new Door(this, loc);
		return true;
	}
	
	/**
	 *	Returns true if loc is the location of the door in the grid
	 *	@param loc lcoation to check for door
	 *	@return returns true if loc is the location of the door in the grid,
	 *			otherwise returns false
	 */
	public boolean atDoor(Location loc)
	{
		return (door != null && loc != null && loc.equals(door.location()));
	}
	
	/**
	 *	Returns the doof for this grid
	 *	@return returns the door for this grid
	 */
	public Door getDoor()
	{
		return door;
	}
	
	/**
	 *	Returns true if this grid has a door
	 *	@return returns true if the grid has a door, otherwise returns false
	 */
	public boolean hasDoor()
	{
		return (door != null);
	}
	
	/**
	 *	Returns a String containiing matrix with:
	 *			"d" for a door in the grid
	 *			"-" for an empty location in the grid
	 *			"+" for an object in the grid
	 *	@return returns a string matirx displaying the grid
	 */
	public String toString()
	{
		String s = "";
		for (int r = 0; r < theGrid.length; r++)
		{
			for (int c = 0; c < theGrid[r].length; c++)
				if (door.location().equals(new Location(r,c)))
					s += "d";
				else if (theGrid[r][c] == null)
					s += "-";
				else
					s += "+";
			s += "\n";
		}
		return s;
	}
	
}