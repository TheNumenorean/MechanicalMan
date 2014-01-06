package mechanicalman.flowchart.program;

import java.security.InvalidParameterException;

import mechanicalman.flowchart.FlowChartSymbol.Symbol;

/**
 * Represents a command that does different things depending on the result.
 * @author Francesco Macagno
 *
 */
public class IfCommand extends Command {

	private Command trueResult, falseResult;

	/**
	 * Creates an IfCommand with the given type.
	 * @param type The type, which must be associated with an if.
	 */
	public IfCommand(Symbol type) {
		this(type, -1, null, null);
		if(!type.name().endsWith("TEST"))
			throw new InvalidParameterException("Type must be some sort of if!");	
		
	}
	
	public IfCommand(Symbol type, int position, Command trueResult, Command falseResult) {
		super(type, position, null);
		
		this.trueResult = trueResult;
		this.falseResult = falseResult;
	}

	/**
	 * Sets the resulting command for if this if statement returns true or false.
	 * @param c Command to run when this is finished
	 * @param ifTrue If the command should be run if the if returns true or false
	 */
	public void setResult(Command c, boolean ifTrue){
		if(ifTrue)
			trueResult = c;
		else
			falseResult = c;
	}

	/**
	 * Gets the resulting command of this statement, either the true result or the false result depending on the passed parameter.
	 * @param result Whether to return the false-result of the true-result
	 * @return A command or null if a result hasn't been set
	 */
	public Command getResult(boolean result) {
		return result ? trueResult : falseResult;	
	}
	
	@Override
	public String toString(){
		return this.getType().getCommand() + " " + (trueResult != null ? trueResult.getPosition() : "") + ":" + (falseResult != null ? falseResult.getPosition() : "");
	}

}
