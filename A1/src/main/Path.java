package main;

import java.util.LinkedList;
import java.util.List;

public class Path {
	public Path parent;
	public Robot.Action action;
	public Robot robotClone;
	public Integer cost;
	public List<Position> remainingDirtyCells;
	

	public Path(Path parentState, Robot robot, Robot.Action action, Integer cost, List<Position> remainingDirtyCells) {
		this.parent = parentState;
		this.action = action;
		this.robotClone = new Robot(robot);
		this.cost = cost;
		this.remainingDirtyCells = new LinkedList<Position>(remainingDirtyCells);
				
	}
	
	@Override
    public boolean equals(Object object) // Compares one path with another only regarding robot action, robot position, and dirty cell list
    {
        boolean isEqual = false;

        if (object != null && object instanceof Path)
        {
        	Path that = ((Path) object);
        	
        	if(		this.action.equals(that.action) &&
        			this.robotClone.getPosition().equals(that.robotClone.getPosition()) &&
        			this.remainingDirtyCells.equals(that.remainingDirtyCells)
        			) {
        		
        		
        		
        	}
        }

        return isEqual;
    }
}
