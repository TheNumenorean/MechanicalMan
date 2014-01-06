package mechanicalman.flowchart.program;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import mechanicalman.MechanicalMan;

/**
 * Represents a MechanicalMan program
 * @author Francesco Macagno
 *
 */
public class Program {

	private ArrayList<String> commands;
	private String programString;

	/**
	 * Reads the data from the input stream to create a new program
	 * @param stream Stream to read
	 * @throws IOException If there is an IO error
	 */
	public Program(InputStream stream) throws IOException {
		this(readFromStream(stream));
	}

	/**
	 * Creates a new Program with the given program data
	 * @param program The string version of a program
	 */
	public Program(String program) {

		programString = program;

		commands = new ArrayList<String>();

		String[] data = program.split("\n");

		for (String tmp : data)
			if (!tmp.isEmpty())
				commands.add(tmp.trim());
	}
	
	/**
	 * Gets a ProgramRunner for this program
	 * @param mm MechanicalMan to create the runner with
	 * @return A ProgramRunner
	 */
	@SuppressWarnings("unchecked")
	public ProgramRunner getProgramRunner(MechanicalMan mm){
		return new ProgramRunner((ArrayList<String>) commands.clone(), mm);
	}

	/**
	 * Reads all the data from a Stream and returns it as a string
	 * @param stream Stream to read
	 * @return A potentially empty string
	 * @throws IOException If there is a read error
	 */
	private static String readFromStream(InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		String program = "";
		int tmp;
		while ((tmp = br.read()) != -1)
			program = program + (char) tmp;

		br.close();

		return program;
	}

	/**
	 * Saves this program to the given file
	 * @param file File to save to
	 * @throws IOException If there is a write problem
	 */
	public void save(String file) throws IOException {

		FileWriter writer = new FileWriter(file);

		writer.write(programString);

		writer.close();

	}
}