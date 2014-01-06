package mechanicalman;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Controls commands from buttons.
 * @author Greg Volger, Francesco Macagno
 *
 */
public class CommandPanelController implements ActionListener
{
	private GridPanel gridView;
	private DataPanel dataView;
	private StatusPanel statusView;
  	private MechanicalMan model;
	private int unnecessaryOns;

	public CommandPanelController(MechanicalMan robot)
	{
		Debug.println("*************** CommandPanelController.constructor() - Beginning **************");
		model = robot;
		gridView = new GridPanel();
		dataView = new DataPanel();
		statusView = new StatusPanel();
		model.addObserver(gridView);
	    model.addObserver(dataView);
	    model.addObserver(statusView);
	    gridView.update(model, null);
	    dataView.update(model, null);
	    statusView.update(model, null);
	    unnecessaryOns = 0;
		Debug.println("*************** CommandPanelController.constructor() - Ending **************");
	}
	
	/**
	 * Gets the DataPanel
	 * @return a DataPanel
	 */
	public DataPanel getDataPanelView(){
		return dataView;
	}

	/**
	 * Gets the StatuPanel
	 * @return a StatusPanel
	 */
	public StatusPanel getStatusPanelView(){
		return statusView;
	}

	/**
	 * Gets the GridPanel
	 * @return A GridPanel
	 */
	public GridPanel getGridPanelView(){
		return gridView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		Debug.println("CommandPanelController.actionPerformed() STARTING - cmd = " + cmd);
		
		if(cmd.equals("Start")){
			if(model.motorOn())
				unnecessaryOns++;
			
			if(unnecessaryOns > 5){
				unnecessaryOns = 0;
				model.hs();
			}
		} else 
			unnecessaryOns = 0;
		
		if (cmd.equals("Start"))
			model.start();
		else if (cmd.equals("Stop"))
			model.stop();
		else if (cmd.equals("Stand Up"))
			model.stand();
		else if (cmd.equals("Sit Down"))
			model.sit();
		else if (cmd.equals("Arms Up"))
			model.armsUp();
		else if (cmd.equals("Arms Down"))
			model.armsDown();
		else if (cmd.equals("Walk"))
			model.walk();
		else if (cmd.equals("Turn"))
			model.turn();
		else if (cmd.equals("Inc"))
			model.increment();
		else if (cmd.equals("Dec"))
			model.decrement();
		else if (cmd.equals("Zero"))
			model.zero();
		else if (cmd.equals("Wall Test"))
			model.testWall();
		else if (cmd.equals("Ctr Test"))
			model.testCounter();
		else 
			model.changeStatus("Error (No match) - actionPerformed of CommandPanelController");
		
		
		
		Debug.println("CommandPanelController.actionPerformed() LEAVING - cmd = " + cmd);	// DEBUG
	}
	
}