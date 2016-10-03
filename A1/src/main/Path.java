package main;

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
	public Path		parent;
	/**
	 * What action was done at this Node of the Path.
	 */
	public Action   action;
	/**
	 * This state's instance of our robot.
	 */
	public Robot	roboClone;
	/**
	 * The cummulative cost of the Actions taken to reach this state.
	 */
	public int		cost;
	public int totalCleaned;
	
	/**
	 * The normal State contructor.
	 * @param parentState This state's parent state.
	 * @param robot This state's instance of our robot.
	 * @param action The Action taken here.
	 * @param cost The cost of reaching this state.
	 */
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
	public Path(Robot robot, int cleaned)
	{
		this(null, robot, null, 0, cleaned);
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
