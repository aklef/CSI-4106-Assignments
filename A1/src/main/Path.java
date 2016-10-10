package main;

import java.util.Comparator;
import java.util.HashSet;

import main.Robot.Action;

/**
 * 
 * The path represents a Node in our Search.
 * It is quite possible for two different nodes to contain the same state, if that state is generated via two different sequences
of actions.
 */
public class Path implements Comparable<Path>
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
	 * The robot as it was at this point in the Path.
	 */
	public Robot	roboClone;
	
	public int		cost;
	
	private HashSet<Cell> cellsAlreadyCleaned = new HashSet<Cell>();
	
	/**
	 * The normal State contructor.
	 * @param parentState This state's parent state.
	 * @param robot This state's instance of our robot.
	 * @param action The Action taken here.
	 * @param cost The cost of reaching this state.
	 * @param currentSet The set of already cleaned cells.
	 */
	public Path(Path parentState,
				Robot robot,
				Action action,
				int cost,
				HashSet<Cell> currentSet)
	{
		this.parent = parentState;
		this.action = action;
		this.roboClone = robot;
		this.cost = cost;
		if(currentSet == null)
		{
			cellsAlreadyCleaned = new HashSet<Cell>();
		}
		else
		{
			this.cellsAlreadyCleaned = (HashSet<Cell>) currentSet.clone();			
		}
	}
	/**
	 * This is intended to be used to create the initial Path state.
	 */
	public Path(Robot robot, int cost)
	{
		this(null, robot, null, cost, null);
	}
	/**
	 * Compares one path with another only
	 * regarding robot action, robot
	 * position, and dirty cell list
	 */
	@Override
    public boolean equals(Object obj)
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
    	
    	if( 
   			this.action == otherPath.action &&
    		this.roboClone.getPosition().equals(otherPath.roboClone.getPosition()) &&
			this.roboClone.getOrientation().name().equals(otherPath.roboClone.getOrientation().name()) &&
			this.cellsAlreadyCleaned.equals(otherPath.cellsAlreadyCleaned)			
			)
    	{
    		equal = true;
    	}
        return equal;
    }
	
	/**
	 * Compares one path with another only regarding robot action, robot position, and dirty cell list
	 * @param obj
	 * @return
	 */
	public boolean equalsStartNode(Object obj)
    {
	    if (obj == null) {
	        return false;
	    }
	    else if (!Path.class.isAssignableFrom(obj.getClass()))
	    {
	        return false;
	    }
		
    	Path otherPath = (Path) obj;
    	if( this.roboClone.getPosition().equals(otherPath.roboClone.getPosition()) &&
			this.roboClone.getOrientation() == otherPath.roboClone.getOrientation() &&
			this.cellsAlreadyCleaned.equals(otherPath.cellsAlreadyCleaned))
    	{
    		return true;
    	}
        return false;
    }
	public HashSet<Cell> getCellsAlreadyCleaned()
	{
		return cellsAlreadyCleaned;
	}
	public void addCleanedCell(Cell cellAlreadyCleaned)
	{
		this.cellsAlreadyCleaned.add(cellAlreadyCleaned);
	}
	
	/**
	 * Sets the Natural Ordering of path to be based on their cost.
	 */
	@Override
    public int compareTo(Path p)
	{
        return Comparators.COST.compare(this, p);
    }

    public static class Comparators
    {
        public static Comparator<Path> CellsAlreadyCleaned = new Comparator<Path>()
        {
        	@Override
        	public int compare(Path p1, Path p2)
        	{
        		return Integer.compare(p2.getCellsAlreadyCleaned().size(), p1.getCellsAlreadyCleaned().size());
        	}
        };
        public static Comparator<Path> COST = new Comparator<Path>()
        {
            @Override
            public int compare(Path p1, Path p2)
			{
				return Integer.compare(p1.cost, p2.cost);
			}
        };
    }
}
