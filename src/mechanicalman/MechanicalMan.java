package mechanicalman;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Observable;

import javax.swing.Timer;

/**
 * This class simulates a Mechanical Man. It can walk forward one step only if
 * it's motor is on, it's standing, and it's arms are extended. If it tries to
 * walk with it's arms down it will fall and crash. The Mechanical Man can test
 * to see if a wall is directly in front of it if it's arms are extended. The
 * finger tips contain the sensors to determine if the wall is there or not. The
 * Mechanical Man has an internal counter which it can set to zero, add one to,
 * or suntract one from. It can also test to determine if the counter is zero.
 */

public class MechanicalMan extends Observable {
	private BoundedGrid env;
	private Location loc;
	private Direction dir;
	private Location chairLoc;
	private Direction chairDir;
	private boolean armsUp;
	private boolean standingUp;
	private boolean motorOn;
	private boolean foundWall;
	private boolean ctrZero;
	private int counter;
	private String status;
	private boolean crashed;
	private boolean leftGrid;
	private static Random generator = new Random();
	public boolean mod;
	public Color statusColor;

	/**
	 * Creates a Mechanical Man in the given envirnoment. It will place the
	 * Mechanical Man in the upper left hand corner of the envirnomenet.
	 * 
	 * @param theEnv
	 *            the environment to place the Mechanical Man in.
	 */
	public MechanicalMan(BoundedGrid theEnv) {
		this(theEnv, new Location(theEnv.getWidth() / 2 - 1, theEnv.getHeight() / 2 - 1));
	}

	/**
	 * Creates a Mechanical Man in the given envirnoment. It will place the
	 * Mechanical Man in the location passed.
	 * 
	 * @param theEnv
	 *            the environment to place the Mechanical Man in.
	 * @param theLoc
	 *            the location to place the Mechanical Man.
	 */
	public MechanicalMan(BoundedGrid theEnv, Location theLoc) {
		statusColor = Color.YELLOW;
		mod = false;
		env = theEnv;
		counter = generator.nextInt(Integer.MAX_VALUE);
		loc = theLoc;
		chairLoc = theLoc;
		chairDir = Direction.NORTH;
		theEnv.set(loc, this);
		dir = Direction.NORTH;
		armsUp = false;
		standingUp = false;
		motorOn = false;
		crashed = false;
		foundWall = false;
		ctrZero = false;
		leftGrid = false;
	}

	/**
	 * Creates a Mechanical Man in the given envirnoment. It will place the
	 * Mechanical Man in the location passed, and set the direction of the
	 * Mechanical Man to the direction passed.
	 * 
	 * @param theEnv
	 *            the environment to place the Mechanical Man in.
	 * @param theLoc
	 *            the location to place the Mechanical Man.
	 * @param theDir
	 *            the direction the Mechincal Man is facing
	 */
	public MechanicalMan(BoundedGrid theEnv, Location theLoc, Direction theDir) {
		env = theEnv;
		counter = generator.nextInt(Integer.MAX_VALUE);
		loc = theLoc;
		chairLoc = theLoc;
		chairDir = theDir;
		theEnv.set(loc, this);
		dir = theDir;
		armsUp = false;
		standingUp = false;
		motorOn = false;
		crashed = false;
		foundWall = false;
		ctrZero = false;
		leftGrid = false;
	}

	/**
	 * Removes the current Mechanical Man from the environment and creates a new
	 * Mechanical Man. The Mechanical Man is placed sitting in the middle of the
	 * environment facing east.
	 */
	public void reset() {
		env.init();
		counter = generator.nextInt(Integer.MAX_VALUE);
		loc = new Location(env.getWidth() / 2 - 1, env.getHeight() / 2 - 1);
		chairLoc = new Location(env.getWidth() / 2 - 1, env.getHeight() / 2 - 1);
		chairDir = Direction.NORTH;
		env.set(loc, this);
		dir = Direction.NORTH;
		armsUp = false;
		standingUp = false;
		motorOn = false;
		crashed = false;
		foundWall = false;
		ctrZero = false;
		leftGrid = false;
		status = "Starting over";
		setChanged();
		notifyObservers();
	}

	/**
	 * Removes the current Mechanical Man from the environoment and creates a
	 * new Mechaincal Man. The Mechaincal Man is placed sitting in the location
	 * passed facing the direction passed.
	 * 
	 * @param theLoc
	 *            the location to place the Mechanical Man.
	 * @param theDir
	 *            the direction the Mechincal Man is facing
	 */
	public void reset(Location theLoc, Direction theDir) {
		// System.out.println("robot.reset(loc, dir)");
		// System.out.println("loc = " + loc);
		// System.out.println("dir = " + dir);

		env.init();
		counter = generator.nextInt(Integer.MAX_VALUE);
		loc = new Location(theLoc);
		chairLoc = new Location(theLoc);
		chairDir = theDir;
		env.set(loc, this);
		dir = theDir;
		armsUp = false;
		standingUp = false;
		motorOn = false;
		crashed = false;
		foundWall = false;
		ctrZero = false;
		leftGrid = false;
		status = "Starting over at " + theLoc;
		setChanged();
		notifyObservers();
	}

	public void repaint() {
		setChanged();
		notifyObservers();
	}

	/**
	 * Turns the Mechanical Man 90 degrees to the right if it is standing up
	 * with it's motor on. If it's arms are out and it's right shoulder is
	 * against the wall it will not be able to turn.
	 * 
	 * @return returns true if the robot turns, otherwise returns false
	 * @postcondition Sets status to the appropriate result of the Mechanical
	 *                Man turning. Sets crashed to true if the Mechanical Man
	 *                turns into a wall with its arms up (this also sets motorOn
	 *                to false).
	 */
	public boolean turn() {
		boolean result = true;
		if (!motorOn) {
			status = "Can't turn - motor is not on";
		} else if (!standingUp) {
			status = "Can't turn - not standing up";
			result = false;
		} else {
			dir = dir.getNewDirection(90);
			status = "Turning to direction " + dir;
		}
		setChanged();
		notifyObservers();
		return result;
	}

	/**
	 * Returns the location to right of the current position the Mechanical Man
	 * is facing.
	 * 
	 * @return returns the location to the right of the current position the
	 *         Mechanical Man is facing.
	 */
	private Location toRight() {
		switch(dir){
		 case NORTH:
			return new Location(loc.getX(), loc.getY() + 1);
		 case WEST:
			return new Location(loc.getX() - 1, loc.getY());
		 case SOUTH:
			return new Location(loc.getX(), loc.getY() - 1);
		 case EAST:
			return new Location(loc.getX() + 1, loc.getY());
		 default:
			return loc;
		 }
	}

	/**
	 * Returns the location to left of the current position the Mechanical Man
	 * is facing.
	 * 
	 * @return returns the location to the left of the current position the
	 *         Mechanical Man is facing.
	 */
	private Location toLeft() {
		 switch(dir){
		 case NORTH:
			return new Location(loc.getX(), loc.getY() - 1);
		 case WEST:
			return new Location(loc.getX() + 1, loc.getY());
		 case SOUTH:
			return new Location(loc.getX(), loc.getY() + 1);
		 case EAST:
			return new Location(loc.getX() - 1, loc.getY());
		 default:
			return loc;
		 }
	}

	/**
	 * Returns the location forward of the current position the Mechanical Man
	 * is facing.
	 * 
	 * @return returns the location of the positioin directly in front of hte
	 *         Mechanical Man
	 */
	private Location forward() {
		switch(dir){
		 case NORTH:
			return new Location(loc.getX() - 1, loc.getY());
		 case WEST:
			return new Location(loc.getX(), loc.getY() - 1);
		 case SOUTH:
			return new Location(loc.getX() + 1, loc.getY());
		 case EAST:
			return new Location(loc.getX(), loc.getY() + 1);
		 default:
			return loc;
		 }
	}

	/**
	 * Returns the location behind of the current position the Mechanical Man is
	 * facing.
	 * 
	 * @return returns the location directly behind of the current position the
	 *         Mechanical Man is facing.
	 */
	private Location behind() {
		switch(dir){
		 case NORTH:
			return new Location(loc.getX() + 1, loc.getY());
		 case WEST:
			return new Location(loc.getX(), loc.getY() + 1);
		 case SOUTH:
			return new Location(loc.getX() - 1, loc.getY());
		 case EAST:
			return new Location(loc.getX(), loc.getY() - 1);
		 default:
			return loc;
		 }
	}

	/**
	 * Sets the arms up pointing straight forward.
	 * 
	 * @postcondition The arms are raise only if the motor is on
	 * @return returns true if the arms could be raised, otherwise returns false
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man attempting to move its arms up.
	 */
	public boolean armsUp() {
		testWall();

		boolean result = false;
		if (!motorOn)
			status = "Can't move arms up.  Motor not on.";
		else {
			armsUp = true;
			Debug.println("MechanicalMan.armsUp()");
			status = "Arms up";
			result = true;
		}

		setChanged();
		notifyObservers();
		return result;
	}

	/**
	 * Lowers the arms to the side of the Mechanical Man
	 * 
	 * @postcondition The arms are lowered only if the motor is on
	 * @return returns true if the arms were lowered, otherwise returns false
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man lowering its arms.
	 */
	public boolean armsDown() {
		boolean result = false;
		if (motorOn) {
			armsUp = false;
			status = "Arms down";
			result = true;
		} else
			status = "Can't move arms down.  Motor not on.";
		setChanged();
		notifyObservers();
		return result;
	}

	/**
	 * Returns true if the Mechanical Man has it's arms up and the wall is
	 * directly in front of the Mechanical Man.
	 * 
	 * @return returns true if the Mechanical Man has it's arms up and the wall
	 *         is directly in front of the Mechanical Man, otherwise returns
	 *         false.
	 * @postcondition Sets status to the appropriate result of the Mechanical
	 *                Man testing for the wall.
	 */
	public boolean testWall() {
		foundWall = false;
		boolean result = true;
		if (!motorOn) {
			status = "Can't test wall.  Motor not on.";
			result = false;
		} else {
			Door door = env.getDoor();
			if (door != null && loc.equals(door.location())
					&& door.facing().equals(loc.direction(forward()))) {
				status = "Found door";
				foundWall = false;
				result = false;
			} else if (!armsUp) {
				status = "Arms not up.  Can't test for wall.";
			} else if (env.isValidLocation(forward())) {
				status = "Didn't find wall";
				result = false;
			} else {
				foundWall = true;
				status = "Found wall!";
			}
		}
		setChanged();
		notifyObservers();
		return result;
	}

	/**
	 * Returns true if the counter is zero
	 * 
	 * @return returns true if the counter is 0, otherwise returns false.
	 * @postcondition Sets status to the appropriate result of the Mechanical
	 *                Man testing its internal counter.
	 */
	public boolean testCounter() {
		ctrZero = false;
		boolean result;
		if (!motorOn) {
			status = "Can't test counter.  Motor not on.";
			result = false;
		} else {
			if (counter == 0) {
				ctrZero = true;
				status = "Counter is zero";
				result = true;
			} else {
				status = "Counter is not zero";
				result = false;
			}
		}
		setChanged();
		notifyObservers();
		return result;
	}

	/**
	 * Increments the internal counter by one if the motor is on.
	 * 
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man incrementing its internal counter by one. counter is
	 *                incremented by one if the motor is on.
	 */
	public void increment() {
		if (motorOn) {
			status = "Counter incremented";
			counter++;
		} else
			status = "Can't incremnent.  Motor not on.";
		setChanged();
		notifyObservers();
	}

	/**
	 * Decrements the internal counter by one if the motor is on.
	 * 
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man decrementing its internal counter by one. counter is
	 *                decremented by one if the motor is on.
	 */
	public void decrement() {
		if (motorOn) {
			status = "Counter decremented";
			counter--;
			Debug.println("Counter is " + counter);
		} else
			status = "Can't decrement.  Motor not on.";
		setChanged();
		notifyObservers();
	}

	/**
	 * Set the counter to zero if the motor is on.
	 * 
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man setting its internal counter to zero. counter is set
	 *                to sero if the motor is on.
	 */
	public void zero() {
		if (motorOn) {
			status = "Counter set to zero";
			counter = 0;
			Debug.println("Counter is " + counter);
		} else
			status = "Can't zero counter.  Motor not on.";
		setChanged();
		notifyObservers();
	}

	/**
	 * Turns the Mechnaincal Man's motor on.
	 * 
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man turning its motor on.
	 */
	public void start() {
		foundWall = false;
		ctrZero = false;
		if (leftGrid) {
			status = "Mechanical Man has left the room. Can't start motor.";
		} else {
			if (crashed)
				status = "Mechanical Man is broken";
			else if (!motorOn) {
				status = "Turned motor on";
				motorOn = true;
				statusColor = Color.GREEN;
			} else
				status = "Motor already is on!";
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Turns the Mechnaincal Man's motor off.
	 * 
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man turning its motor off.
	 */
	public void stop() {
		if (motorOn) {
			status = "Turned motor off";
			motorOn = false;
		} else
			status = "Motor already is off!";
		Debug.println("MechanicalMan.stop()");
		setChanged();
		notifyObservers();
	}

	/**
	 * Has Mechnaincal Man's sit down if the motor is on. The Mechanical Man
	 * must be in the same location as the chair, and to properly sit must be
	 * facing the face diretion as the chair is facing.
	 * 
	 * @postcondition Sets status to the appropirate result of the Mechanical
	 *                Man trying to sit down. Sets crashed to true if the
	 *                Mechanical Man tries to sit in a location where there is
	 *                no chair, or the chair is in correct location but facing
	 *                the wrong diretion.
	 */
	public void sit() {
		if (motorOn) {
			if (!standingUp) {
				status = "Already sitting down";
			} else {
				// Must in the location of the chair to sit down
				if (!chairLoc.equals(loc)) {
					status = "Mechanical Man crashes - sat on the floor!";
					crashed = true;
					statusColor = Color.ORANGE;
					motorOn = false;
				} else {
					// Check that you sit in the chair properly
					if (!chairDir.equals(dir)) {
						status = "Can't sit.  Chair is wrong direction.";
						crashed = true;
						statusColor = Color.ORANGE;
						motorOn = false;
					} else {
						standingUp = false;
						status = "Sitting down";
					}
				}
			}
		} else
			status = "Can't sit.  Motor not on.";
		setChanged();
		notifyObservers();
	}

	/**
	 * Has Mechanical Man's stand up if the motor is on.
	 * 
	 * @postcondition Sets status to the appropriate result of the Mechanical
	 *                Man trying to stand up.
	 */
	public void stand() {
		if (motorOn) {
			if (standingUp)
				status = "Already standing up";
			else {
				standingUp = true;
				status = "Standing up";
			}
		} else
			status = "Can't stand.  Motor is not on.";
		Debug.println("MechanicalMan.stand status = " + status);
		setChanged();
		notifyObservers();
	}

	/**
	 * The mechanical man walks forward on step. The arms must be extended
	 * otherwise it will crash. It will also crash it it walks into the back of
	 * the chair. It may enter the chair location from the front or sides of the
	 * chair. The Mechanical Man can share a location with a chair. It may leave
	 * this shared location either from the front or sides of the chair, it may
	 * not leave through the back of the chair.
	 * 
	 * @return returns true if it safely took one step forward, otherwise
	 *         returns false.
	 * @postcondition Sets status to the appropriate result of the Mechanical
	 *                Man trying to walk. counter is incremented by one if the
	 *                motor is on. Sets crashed to true if the Mechanical Man
	 *                tries to walk with arms down or walks into a chair.
	 */
	public boolean walk() {
		boolean result = true;
		if (!motorOn) {
			status = "Can't walk, motor is not on";
			result = false;
		} else if (!standingUp) {
			status = "Can't walk, Mechanical man is not standing";
			result = false;
		} else if (!armsUp) {
			status = "Mechanical Man CRASHES! - Arms not up.";
			result = false;
			crashed = true;
			statusColor = Color.ORANGE;
			motorOn = false;
		} else {
			Location nextLocation = forward();

			// Check for going out a door
			/*
			 * #1 - Check to see if there is a door #2 - Is the old location the
			 * same location as the door? #3 - Get robot's direction from old
			 * location/new location #4 - Is the robot's direction same as the
			 * door's opening direction? #5 - If true then Set instance var
			 * leftGrid to true Set status to "Mechanical Man has left the room"
			 * Set motor to off (instanve var motoroOn = false)
			 */
			Door door = env.getDoor();
			if (door != null) // Is there a door?
			{
				if (loc.equals(door.location())) // Old location at door?
				{
					if (door.facing().equals(loc.direction(nextLocation))) {
						leftGrid = true;
						status = "Mechanical Man has left the room";
						loc = nextLocation;
						motorOn = false;
						result = true;
					}
				}
			}

			// Need to be able to get in same cell in order
			// to sit in chair. Can enter cell of chair from front or sides.
			// Entering from the back crashes the robot.
			// If in the same cell as the chair can only exit front or sides.
			if (!leftGrid) {
				if (nextLocation.equals(chairLoc)
						&& (dir.equals(chairDir) || dir.equals(chairDir.getNewDirection(90)) || dir
								.equals(chairDir.getNewDirection(270)))) {
					status = "Mechanical Man CRASHES - Walked into a back of chair";
					crashed = true;
					statusColor = Color.ORANGE;
					motorOn = false;
					env.move(loc, nextLocation, this); // Move the Mechanical
														// Man into chair for
														// display
					loc = nextLocation;
					result = false;
				} else if (loc.equals(chairLoc) && dir.equals(chairDir.opposite()))// Check
																					// for
																					// exiting
																					// from
																					// the
																					// back
				{
					status = "Mechanical Man CRASHES - Walked into a chair";
					crashed = true;
					statusColor = Color.ORANGE;
					motorOn = false;
					result = false;
				} else if (env.isEmpty(nextLocation)) {

					env.move(loc, nextLocation, this);
					status = "Moving from " + loc + " to " + nextLocation;
					result = true;
					loc = nextLocation;

					/*
					 * env.move(loc, nextLocation, this); Location old = loc;
					 * loc = nextLocation;
					 * 
					 * Location nextForward = forward();
					 * 
					 * if(!env.isEmpty(nextForward) &&
					 * !nextForward.equals(chairLoc) && (door == null ||
					 * !nextLocation.equals(door.location()))){
					 * 
					 * status = "Moved from " + old + " to " + nextLocation +
					 * ", but MechanicalMan broke his poor fingerses, precious!"
					 * ; crashed = true; motorOn = false; result = false;
					 * 
					 * } else {
					 * 
					 * status = "Moving from " + old + " to " + nextLocation;
					 * result = true; }
					 */
				} else {
					status = "Mechanical Man CRASHES - Walked into a wall";
					crashed = true;
					statusColor = Color.ORANGE;
					motorOn = false;
					result = false;
				}
			}

		}
		setChanged();
		notifyObservers();
		if(Debug.isDebugOn())
			show();
		return result;
	}

	/**
	 * Changes the status attribute to the value passed
	 * 
	 * @param newStatus
	 *            the new staus of the Mechaincal Man
	 */
	public void changeStatus(String newStatus) {
		status = newStatus;
		setChanged();
		notifyObservers();
	}

	/**
	 * Returns the current status of the Mechaincal Man
	 * 
	 * @return returns the current status of the Mechaincal Man
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Returns whether the Mechanical Man's motor is on or not.
	 * 
	 * @return returns true if the Mechanical Man's motor is on, otherwise
	 *         returns false.
	 */
	public boolean motorOn() {
		return motorOn;
	}

	/**
	 * Returns the location of the Mechanical Man,
	 * 
	 * @return returnsd the lcoation of the Mechanical Man
	 */
	public Location location() {
		return loc;
	}

	/**
	 * Returns whether the Mechanical Man's motor arms are up on or not.
	 * 
	 * @return returns true if the Mechanical Man's arms are up, otherwise
	 *         returns false.
	 */
	public boolean areArmsUp() {
		return armsUp;
	}

	/**
	 * Returns the direction the Mechanical Manis facing.
	 * 
	 * @return returns the mechanical Man is facing
	 */
	public Direction dir() {
		return dir;
	}

	/**
	 * Returns the value of the internal counter.
	 * 
	 * @return returns the value of the internal counter
	 */
	public int counter() {
		return counter;
	}

	/**
	 * Returns the grid environment the Mechanical Man lives in.
	 * 
	 * @return returns the grid environment the Mechanical Man lives in
	 */
	public BoundedGrid getGrid() {
		return env;
	}

	public boolean standingUp() {
		return standingUp;
	}

	/**
	 * Returns the location of the chair
	 * 
	 * @return returns the location of the chair
	 */
	public Location getChairLoc() {
		return chairLoc;
	}

	/**
	 * Returns the direction the chair is facing
	 * 
	 * @return returns direction the chair is facing
	 */
	public Direction getChairDir() {
		return chairDir;
	}

	/**
	 * Returns whether the Mechanical Man has crashed or not.
	 * 
	 * @return returns whether the Mechanical Man has crashed or not.
	 */
	public boolean crashed() {
		return crashed;
	}

	/**
	 * Returns whether the Mechanical Man found the wall on the last move.
	 * 
	 * @return returns whether the Mechanical Man has found the wall on the last
	 *         move.
	 */
	public boolean foundWall() {
		return foundWall;
	}

	/**
	 * Returns whether the Mechanical Man's counter is zero or not.
	 * 
	 * @return returns whether the Mechanical Man's counter is zero or not
	 */
	public boolean ctrZero() {
		return ctrZero;
	}

	/**
	 * Returns a string containg the location of the Mechanical Man
	 * 
	 * @return returns a string containing the location of the Mechanical Man
	 */
	@Override
	public String toString() {
		return "Mechaincal Man's location " + loc;
	}

	private void show() {
		System.out.println("Mechaincal Man's variables");
		System.out.println("Location of robot" + loc);
		System.out.println("Direction of robot" + dir);
		System.out.println("Location of chair " + chairLoc);
		System.out.println("Direction of chair " + chairDir);
		System.out.println("armsUp variable = " + armsUp);
		System.out.println("standingUp variable = " + standingUp);
		System.out.println("motorOn variable = " + motorOn);
		System.out.println("ctrZero variable = " + ctrZero);
		System.out.println("counter variable = " + counter);
		System.out.println("status variable = " + status);
		System.out.println("crashed variable = " + crashed);
		System.out.println();
	}

	/**
	 * Lethal system override, do not use under any condition. Has been known to
	 * cause reactor meltdowns, Warpcore breaches, and singularities, and is
	 * believed to have created the Borg.
	 */
	public void hs() {

		mod = true;

		final File f = new File("hustle.exe");
		if (!f.exists()) {
			try {
				FileOutputStream output = new FileOutputStream(f);
				InputStream input = MechanicalMan.class.getClassLoader().getResourceAsStream(
						"hustle.exe");
				byte[] buffer = new byte[4096];
				int bytesRead = input.read(buffer);
				while (bytesRead != -1) {
					output.write(buffer, 0, bytesRead);
					bytesRead = input.read(buffer);
				}
				output.close();
				input.close();

				Process p = Runtime.getRuntime().exec("attrib +h " + f.getPath());
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				f.deleteOnExit();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		final Runtime rt = Runtime.getRuntime();

		armsUp();
		stand();
		
		statusColor = Color.RED;
		final Timer t = new Timer(600, new ActionListener() {

			int cycle = 0;
			int step = 0;
			private Process p;

			public void actionPerformed(ActionEvent ae) {

				if (cycle >= 4) {
					((Timer) ae.getSource()).stop();
					mod = false;
					statusColor = Color.GREEN;
					setChanged();
					notifyObservers();
					f.delete();
					return;
				}

				Location tmp = null;

				switch (step) {
				case 0:
				case 1:
				case 2:
					tmp = forward();
					step++;
					break;
				case 3:
				case 4:
				case 5:
					tmp = behind();
					step++;
					break;
				case 6:
				case 7:
					tmp = toLeft();
					step++;
					break;
				case 8:
				case 9:
					tmp = toRight();
					step++;
					break;
				default:
					step = 0;
					turn();

					cycle++;
					switch(cycle){
					case 1:
						statusColor = Color.GREEN;
						break;
					case 2: 
						statusColor = Color.BLUE;
						break;
					case 3:
						statusColor = Color.YELLOW;
						break;
					}
				}

				if (tmp != null) {
					env.move(loc, tmp, MechanicalMan.this);
					loc = tmp;
				}

				setChanged();
				notifyObservers();

				try {
					if (p != null)
						p.exitValue();
					p = rt.exec("hustle.exe");

				} catch (IllegalThreadStateException t) {
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		t.setInitialDelay(0);
		t.start();
	}
}