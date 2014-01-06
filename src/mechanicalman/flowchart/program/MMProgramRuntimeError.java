package mechanicalman.flowchart.program;

public class MMProgramRuntimeError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8708652631962170231L;

	public MMProgramRuntimeError() {
		super("Unknown error running program!");
	}

	public MMProgramRuntimeError(String arg0) {
		super(arg0);
	}

	public MMProgramRuntimeError(Throwable arg0) {
		super(arg0);
	}

	public MMProgramRuntimeError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MMProgramRuntimeError(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
