package mechanicalman;

/**
 * This class gets tracks of a direction. North is 0 degrees West is 270 degrees
 * South is 180 degrees East is 90 degrees
 * 
 * @author Francesco Macagno
 */
public enum Direction {
	NORTH(0), SOUTH(180), EAST(90), WEST(270);

	private int dir;

	private Direction(int dir) {
		this.dir = dir;
	}

	/**
	 * Gets the direction of this Direction in degrees
	 * 
	 * @return An integer
	 */
	public int getDegrees() {
		return dir;
	}

	/**
	 * Gets a Direction depending on a degree value
	 * 
	 * @param i
	 *            Degree to interpret. Must be a cardinal direction
	 * @return A Direction, or null if i is not cardinal
	 */
	public static Direction getDirection(int i) {
		i = i % 360;
		switch (i) {
		case 0:
			return Direction.NORTH;
		case 90:
			return Direction.EAST;
		case 180:
			return Direction.SOUTH;
		case 270:
			return Direction.WEST;
		default:
			return null;
		}
	}

	/**
	 * Gets a new Direction in relation to this direction. Adds the given value
	 * to the current degrees and reinterprets. Does NOT change this direction.
	 * 
	 * @param degrees
	 *            A number between -360 and 360
	 * @return A new Direction
	 */
	public Direction getNewDirection(int degrees) {
		return Direction.getDirection(this.dir + degrees + 360);
	}

	/**
	 * Gets the direction for oposite this Direction. Does NOT change this
	 * direction!
	 * 
	 * @return A new Direction
	 */
	public Direction opposite() {
		return Direction.getDirection(dir + 180);
	}

	@Override
	public String toString() {
		return name().substring(0, 1) + name().substring(1).toLowerCase();
	}

	/**
	 * Gets the value of this direction in radians
	 * 
	 * @return A radian value
	 */
	public double getRadians() {
		return Math.toRadians(getDegrees());
	}

	public static Direction getDirection(String input) {
		return Direction.valueOf(input.toUpperCase());
	}
}