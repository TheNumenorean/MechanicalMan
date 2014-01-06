package mechanicalman.flowchart.program;

import mechanicalman.flowchart.FlowChartSymbol.Symbol;

/**
 * Used to represent a command in a program. Acts like a link in a linkedlist
 * @author Francesco Macagno
 *
 */
public class Command implements Comparable<Command>{
	
	private Symbol type;
	private int position;
	private Command result;

	/**
	 * Creates a new command with the given type.
	 * @param type Type to set the command to
	 */
	public Command(Symbol type){
		this(type, -1, null);
	}
	
	/**
	 * Creates a new command with the given type, the given position, and the given result.
	 * @param type Type of Command
	 * @param comPos Position the command is at
	 * @param result Next command after this command
	 */
	public Command(Symbol type, int comPos, Command result) {
		this.type = type;
		this.position = comPos;
		this.result = result;
	}
	
	/**
	 * Gets the position of the command in the overall program.
	 * @return The position this 
	 */
	public int getPosition(){
		return position;
	}
	
	/**
	 * Sets the position that this Command is at
	 * @param position The position
	 */
	public void setPosition(int position){
		this.position = position;
	}

	/**
	 * Gets the type of command
	 * @return a Type
	 */
	public Symbol getType(){
		return type;
	}
	
	@Override
	public String toString(){
		return type.getCommand() + " " + (result != null ? result.getPosition() : "");
	}

	/**
	 * Sets the command to run after this command is run
	 * @return the resulting command
	 */
	public Command getResult() {
		return result;
	}

	/**
	 * @param result the command to do next
	 */
	public void setResult(Command result) {
		this.result = result;
	}

	@Override
	public int compareTo(Command com) {
		return getPosition() - ((Command)com).getPosition();
	}

}
