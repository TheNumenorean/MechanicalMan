package mechanicalman.flowchart.program;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import mechanicalman.MechanicalMan;

/**
 * Used to run Programs on MechanicalMen
 * @author Francesco Macagno
 *
 */
public class ProgramRunner {

	private MechanicalMan mm;
	private boolean finished;
	private ArrayList<String> commands;
	private Thread runThread;
	private int interval;
	public static final int INTERVAL_DEFAULT = 250;

	/**
	 * Creates a ProgramRunner with the given list of commands and MechanicalMan to act them upon.
	 * @param commands Commands to run
	 * @param mm Actor
	 */
	public ProgramRunner(ArrayList<String> commands, MechanicalMan mm) {
		this.mm = mm;
		finished = false;
		this.commands = commands;
		this.interval = INTERVAL_DEFAULT;

		new Thread() {

			@Override
			public void run() {

				try {
					runProgram();
				} catch (MMProgramRuntimeError e) {
					e.printStackTrace();
				}
				finished = true;
			}

		}.start();
	}

	/**
	 * Starts the runner running commands with the given interval.
	 * @param runnable A Runnable to run when finished.
	 */
	public void start(final Runnable runnable) {

		if (interval < 0)
			throw new InvalidParameterException("Interval cannot be 0!");
		
		stop();

		runThread = new Thread() {

			@Override
			public void run() {

				while (!finished()) {
					step();

					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						runThread = null;
						return;
					}
				}
				
				runThread = null;
				if(runnable != null)
					runnable.run();
			}

		};
		
		runThread.start();

	}
	
	/**
	 * Stops the runner running.
	 */
	public void stop(){
		if(runThread != null)
			runThread.interrupt();
	}
	
	/**
	 * Sets the interval at which the program will step
	 * @param interval Delay between steps
	 */
	public void setInterval(int interval){
		this.interval = interval;
	}

	/**
	 * Steps the program, moving it one command forward.
	 */
	public void step() {
		synchronized (this) {
			this.notify();
		}
	}
	
	/**
	 * Actually runs the program. Waits before each step for notify() to be called.
	 * @throws MMProgramRuntimeError If there is an error running the program.
	 */
	private void runProgram() throws MMProgramRuntimeError {

		for (int y = 0; y < commands.size(); y++) {
			
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String[] com = commands.get(y).split(" ");

			if (runStandardCommand(com[0])) {

				if (com.length > 1) {

					try {
						y = Integer.parseInt(com[1]) - 1;
					} catch (NumberFormatException e) {
						throw new MMProgramRuntimeError("Invalid goto number at line " + y);
					}
				}

			} else if (com[0].equals("stop")) {
				mm.stop();
				return;
			} else {

				boolean result = false;

				if (com[0].equals("cntTest")) {
					result = mm.testCounter();
				} else if (com[0].equals("wallTest")) {
					result = mm.testWall();
				} else
					throw new MMProgramRuntimeError("Unknown Command at line " + y);

				String[] resSplit = com[1].split(":");

				if (resSplit.length < 2)
					throw new MMProgramRuntimeError("Invalid if-result at line " + y);

				try {
					if (result)
						y = Integer.parseInt(resSplit[0]) - 1;
					else
						y = Integer.parseInt(resSplit[1]) - 1;
				} catch (NumberFormatException e) {
					throw new MMProgramRuntimeError("Invalid if-result number at line " + y);
				}

			}

			System.out.println("Running command " + com[0]);

		}
	}

	/**
	 * Has the program finished running?
	 * @return True if it has, false otherwise.
	 */
	public boolean finished() {
		return finished;
	}

	private boolean runStandardCommand(String com) {
		if (com.equals("start")) {
			mm.start();
		} else if (com.equals("armsUp")) {
			mm.armsUp();
		} else if (com.equals("armsDown")) {
			mm.armsDown();
		} else if (com.equals("stand")) {
			mm.stand();
		} else if (com.equals("sit")) {
			mm.sit();
		} else if (com.equals("walk")) {
			mm.walk();
		} else if (com.equals("turn")) {
			mm.turn();
		} else if (com.equals("cntReset")) {
			mm.zero();
		} else if (com.equals("cntDec")) {
			mm.decrement();
		} else if (com.equals("cntInc")) {
			mm.increment();
		} else
			return false;
		return true;
	}

}
