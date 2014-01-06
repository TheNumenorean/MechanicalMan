package mechanicalman;

public interface Grid
{
	boolean atEdge(Location loc);
	boolean isValidLocation(Location loc);
	boolean isEmpty(Location loc);
	boolean set(Location loc, Object o);
	boolean move(Location oldLoc, Location newLoc, Object o);
	int getWidth();
	int getHeight();
	void init();
	boolean addDoor(Location loc);
	boolean hasDoor();
}
