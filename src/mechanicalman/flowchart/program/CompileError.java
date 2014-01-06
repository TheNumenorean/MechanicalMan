package mechanicalman.flowchart.program;

/**
 * 
 * @author Francesco Macagno
 *
 */
public class CompileError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2024626184647153086L;

	public CompileError() {
		super("There was an unknown error compiling!");
	}

	public CompileError(String arg0) {
		super(arg0);
	}

	public CompileError(Throwable arg0) {
		super(arg0);
	}

	public CompileError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CompileError(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
