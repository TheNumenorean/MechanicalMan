package mechanicalman.flowchart.program;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import mechanicalman.flowchart.FlowChart;
import mechanicalman.flowchart.FlowChartSymbol;
import mechanicalman.flowchart.FlowChartSymbol.Symbol;

/**
 * Used for compiling and decompiling MM Programs.
 * 
 * @author Francesco Macagno
 *
 */
public class Compiler {
	

	/**
	 * Compiles the given symbols into a functional MM program.
	 * @param symbols FlowChartSymbols to interpret
	 * @return  Program that contains the data to run the program from the symbols
	 * @throws CompileError If there is an issue with the given FlowChartSymbols
	 */
	public static Program compile(FlowChartSymbol[][] symbols) throws CompileError {
		
		TreeMap<Command, FlowChartSymbol> commands = new TreeMap<Command, FlowChartSymbol>();
		
		// Find START
		FlowChartSymbol current = findStart(symbols);
		if (current == null)
			throw new CompileError("No start!");
		
		getCommandTree(current, commands);
		
		String program = "";
		
		for(Command c : commands.keySet()){
			program = program + c + "\n"; 
		}
		
		return new Program(program);
	}
	
	/**
	 * Constructs command mapping based on the FlowChartSymbols, filling the given TreeMap
	 * @param sym Root symbol to construct the map from
	 * @param commands TreeMap to fill with the commands and symbols
	 * @return The command associated with the first symbol.
	 * @throws CompileError If there is a problem with the symbol tree
	 */
	private static Command getCommandTree(FlowChartSymbol sym, TreeMap<Command, FlowChartSymbol> commands) throws CompileError{
		
		for( Entry<Command, FlowChartSymbol> e : commands.entrySet())
			if(e.getValue().equals(sym))
				return e.getKey();
		
		if (sym.getTo().size() == 0 && !sym.getType().equals(Symbol.STOP))
			throw new CompileError("Missing arrow from " + sym);
		
		Command com;
		
		if(sym.getType().equals(Symbol.CTR_TEST) || sym.getType().equals(Symbol.WALL_TEST)){
			com = new IfCommand(sym.getType(), commands.size(), null, null);
			
			commands.put(com, sym);
			
			if(sym.getTo().size() == 2){
				((IfCommand)com).setResult(getCommandTree(sym.getTo().get(1), commands), false);
				((IfCommand)com).setResult(getCommandTree(sym.getTo().get(0), commands), true);
			} else {
				throw new CompileError("Missing arrows from if " + sym);
			}
			
		} else {
			com = new Command(sym.getType(), commands.size(), null);
			
			commands.put(com, sym);
			
			if(!sym.getType().equals(Symbol.STOP))
				com.setResult(getCommandTree(sym.getTo().get(0), commands));
		}
		
		
		return com;
	}
	
	private static FlowChartSymbol findStart(FlowChartSymbol[][] symbols)
	{
		for (int r = 0; r < symbols.length; r++)
			for (int c = 0; c < symbols[r].length; c++)
				if (symbols[r][c] != null && symbols[r][c].getType().equals(Symbol.START))
					return symbols[r][c];
		
		return null;
	}

	/**
	 * Reads the contents oa file that contains an MM program and puts it into the given 
	 * flowchart as a graphical program.
	 * 
	 * This will clear all symbols currently in the flowchart.
	 * 
	 * @param file File to read
	 * @param flowChart FlowChart to add the program to
	 * @throws IOException If there is a file error.
	 * @throws CompileError If there is a problem reading the program.
	 */
	public static void decompile(File file, FlowChart flowChart) throws IOException, CompileError {
		
		flowChart.clear();
		
		BufferedReader br = new BufferedReader(new FileReader(file));

		String program = "";
		int tmp;
		while ((tmp = br.read()) != -1)
			program = program + (char) tmp;

		br.close();
		
		TreeMap<Command, String> commandTargets = new TreeMap<Command, String>();
		
		for(String com : program.split("\n")){
			com = com.trim();
			
			String[] spl = com.split(" ");
			
			com = spl[0];
			
			Command command = null;
			for(Symbol symbol : Symbol.values())
				if(symbol.getCommand().equals(com)){
					if(symbol.equals(Symbol.WALL_TEST) || symbol.equals(Symbol.CTR_TEST))
						command = new IfCommand(symbol, commandTargets.size(), null, null);
					else
						command = new Command(symbol, commandTargets.size(), null);
				}
			
			if(command == null)
				throw new CompileError("Unknown command " + command);
			
			String target = null;
			if(spl.length > 1)
				target = spl[1];
			
			commandTargets.put(command, target);
			
		}
		
		TreeMap<Command, FlowChartSymbol> commandMappings = new TreeMap<Command, FlowChartSymbol>();
		
		for(Command c : commandTargets.keySet()){
			FlowChartSymbol fcs = new FlowChartSymbol(c.getType());
			commandMappings.put(c, fcs);
		}
		
		for(Command c : commandTargets.keySet()){
			String target = commandTargets.get(c);
			if(c instanceof IfCommand){
				
				String[] tars = target.split(":");
				
				if(tars.length < 2)
					throw new CompileError("Not enough args for if statement at " + c.getPosition());
				
				int tr, fl;
				
				try {
					tr = Integer.parseInt(tars[0]);
					fl = Integer.parseInt(tars[1]);
				} catch (NumberFormatException e){
					throw new CompileError("Invalid goto in if statement at " + c.getPosition());
				}
				
				FlowChartSymbol fcs = commandMappings.get(c);
				fcs.setTo((FlowChartSymbol) commandMappings.values().toArray()[tr]);
				fcs.setTo((FlowChartSymbol) commandMappings.values().toArray()[fl]);
				
				
			} else if(!c.getType().equals(Symbol.STOP)) {
				
				int tar = c.getPosition() + 1;
				
				if(target != null){
				
					try {
						tar = Integer.parseInt(target);
					} catch (NumberFormatException e){
						throw new CompileError("Invalid goto in statement at " + c.getPosition());
					}
					
				}
				
				FlowChartSymbol fcs = commandMappings.get(c);
				fcs.setTo((FlowChartSymbol) commandMappings.values().toArray()[tar]);
				
			}
		}
		
		for(FlowChartSymbol fcs : commandMappings.values())
			flowChart.getFlowChartPanel().addSymbol(fcs);
		
	}
	
}