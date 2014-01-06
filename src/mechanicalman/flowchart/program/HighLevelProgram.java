package mechanicalman.flowchart.program;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import mechanicalman.MechanicalMan;

/**
 * Program that uses more advanced concepts of programming.
 * @author Francesco Macagno
 *
 */
public class HighLevelProgram {

	private ArrayList<String> commands;

	private ProgramRunner pr;

	private String programString;

	public HighLevelProgram(InputStream stream) throws IOException {
		this(readFromStream(stream));
	}

	public HighLevelProgram(String program) {
		
		programString = program;

		commands = new ArrayList<String>();

		String[] data = program.split("\n");

		for (String tmp : data)
			if (!tmp.isEmpty())
				addCommand(tmp.trim());
	}

	private void addCommand(String command) {
		commands.add(command);
	}

	public void run(final MechanicalMan robot) {

		if (pr != null && pr.finished())
			pr = null;

		if (pr == null) {

			pr = new ProgramRunner(robot);

			new Thread() {

				public void run() {

					while (!pr.finished()) {
						pr.step();

						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}

			}.start();
		}

	}

	private class ProgramRunner {

		private MechanicalMan mm;
		private boolean finished;

		public ProgramRunner(MechanicalMan mm) {
			this.mm = mm;
			finished = false;

			new Thread() {

				public void run() {

					runSegment(0);
					finished = true;
				}

			}.start();
		}

		public void step() {
			synchronized (this) {
				this.notify();
			}
		}

		/**
		 * Runs from the starting point. WIll run until it hits a break, at which point it will exit, or no commands are left. if it hit and endwhile it will start again from the start, and keep doing this until break is called.
		 * @param start Where to start in the program
		 * @return Where it ends
		 */
		private int runSegment(int start) {
			
			int end = 0;
			
			int whiles = 0;
			for (int y = start; y < commands.size(); y++) {
				
				String com = commands.get(y);
				
				if(com.equals("while"))
					whiles++;
				else if(com.equals("endWhile")){
					
					if(whiles != 0)
						whiles--;
					else {
						end = y;
					}
					
				}
				
			}

			for (int y = start; y < commands.size(); y++) {

				String com = commands.get(y);

				if (runStandardCommand(com)) {
					// All good
				} else if (com.equals("while")) {
					y = runSegment(y + 1);
				} else if (com.equals("endWhile")) {
					y = start - 1;
				} else if (com.equals("break")) {
					return end;
				} else {
					
					System.out.println("Starting if...");

					boolean lastRes = true, first = true;
					for (String s : com.split(" ")) {

						System.out.println(first + " " + lastRes + " " + s);
						
						if (first) {
							first = false;

							if (s.equals("cntTest")) {
								lastRes = mm.testCounter();
							} else if (s.equals("wallTest")) {
								lastRes = mm.testWall();
							} else 
								break;
							
						} else {

							if (s.startsWith("!")) {

								if (lastRes)
									break;

								s = s.substring(1);

							} else if (!lastRes)
								break;

							if (s.equals("cntTest")) {
								lastRes = mm.testCounter();
							} else if (s.equals("wallTest")) {
								lastRes = mm.testWall();
							} else if (s.equals("break")) {
								return end;
							} else {
								runStandardCommand(com);
								break;
							}
						}

					}

				}

				System.out.println("Running command " + com);

				try {
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			
			return end;
		}

		public boolean finished() {
			return finished;
		}

		private boolean runStandardCommand(String com) {
			if (com.equals("start")) {
				mm.start();
			} else if (com.equals("stop")) {
				mm.stop();
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

	private static String readFromStream(InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		String program = "";
		int tmp;
		while ((tmp = br.read()) != -1)
			program = program + (char) tmp;

		br.close();

		return program;
	}
	
	public void save(String file) throws IOException{
		
		FileWriter writer = new FileWriter(file);
		
		writer.write(programString);
		
		writer.close();
		
	}
}