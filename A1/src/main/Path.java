package main;

import java.util.LinkedList;
import java.util.List;

import main.Robot.Action;

/**
 * The path represents a Node in our Search.
 * It had 
 */
public class Path
{
	/**
	 * The parent Node of this Path.
	 */
	public Path		   	  parent;
	/**
	 * What action was done at this Node of the Path.
	 */
	public Action   action;
	/**
	 * What action was done at this Node of the Path.
	 */
	public Robot		  roboClone;
	
	public Integer		  cost;
	public List<Position> remainingDirtyCells;
	
	public Path(Path parentState,
				Robot robot,
				Action action,
				Integer cost,
				List<Position> remainingDirtyCells)
	{
		this.parent = parentState;
		this.action = action;
		this.roboClone = new Robot(robot);
		this.cost = cost;
		this.remainingDirtyCells = new LinkedList<Position>(remainingDirtyCells);
		
	}
	/**
	 * Compares one path with another only
	 * regarding robot action, robot
	 * position, and dirty cell list
	 */
	@Override
	public boolean equals(Object object)
	{
		boolean isEqual = false;
		
		if (object != null && object instanceof Path)
		{
			Path that = ((Path) object);
			
			if (this.action.equals(that.action)
					&& this.roboClone.getPosition().equals(
							that.roboClone.getPosition())
					&& this.remainingDirtyCells
							.equals(that.remainingDirtyCells))
			{
				
			}
		}
		
		return isEqual;
	}
}
