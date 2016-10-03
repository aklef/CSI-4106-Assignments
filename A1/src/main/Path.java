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
	
	public int		  cost;
	
	public int totalCleaned;
	
	public Path(Path parentState,
				Robot robot,
				Action action,
				int cost,
				int cleaned)
	{
		this.parent = parentState;
		this.action = action;
		this.roboClone = robot;
		this.cost = cost;
		this.totalCleaned = cleaned;
		
		
		
	}
	/**
	 * This is intended to be used to create the initial Path state.
	 */
	public Path(Robot robot, int cost, int cleaned)
	{
		this(null, robot, null, cost, cleaned);
	}
	/**
	 * Compares one path with another only
	 * regarding robot action, robot
	 * position, and dirty cell list
	 */
	@Override
    public boolean equals(Object obj) // Compares one path with another only regarding robot action, robot position, and dirty cell list
    {
	    if (obj == null) {
	        return false;
	    }
	    else if (!Path.class.isAssignableFrom(obj.getClass()))
	    {
	        return false;
	    }
		
        boolean equal = false;
    	Path otherPath = (Path) obj;
    	
    	if( this.roboClone.getPosition().equals(otherPath.roboClone.getPosition()) &&
			this.roboClone.getOrientation().name().equals(otherPath.roboClone.getOrientation().name())
			)
    	{
    		equal = true;
    	}
        return equal;
    }
}
