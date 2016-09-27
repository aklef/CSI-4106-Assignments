public class Robot
{
	
	public enum Direction
	{
		NORTH, WEST, SOUTH, EAST
	};
	
	// robot variables
	private Position  position;
	private Direction robotOrientation;
	
	public Robot(Position position, Direction orientation)
	{
		this.position = position;
		this.robotOrientation = orientation;
	}
	
	public Direction getOrientation()
	{
		return robotOrientation;
	}
	
	public void turnLeft()
	{
		switch (robotOrientation) {
			case NORTH:
				robotOrientation = Direction.WEST;
				break;
			case WEST:
				robotOrientation = Direction.SOUTH;
				break;
			case SOUTH:
				robotOrientation = Direction.EAST;
				break;
			case EAST:
				robotOrientation = Direction.NORTH;
				break;
		}
		// TODO return a cost
	}
	
	public void turnRight()
	{
		switch (robotOrientation) {
			case NORTH:
				robotOrientation = Direction.EAST;
				break;
			case WEST:
				robotOrientation = Direction.NORTH;
				break;
			case SOUTH:
				robotOrientation = Direction.WEST;
				break;
			case EAST:
				robotOrientation = Direction.SOUTH;
				break;
		}
		// TODO return a cost
	}
	
	public Boolean forwards()
	{
		
		// TODO: Check if moving out of bounds - return false
		
		// TODO: Check if there are obstacles - return false
		
		// TODO: Move - return true.
		// TODO return a cost

		return false;
	}
	
	public Boolean robotClean()
	{
		// TODO: Clean at the current position - return true if it was dirty
		// before, return false if it was already clean
		// TODO return a cost
		
		return false;
	}

	public Position getPosition()
	{
		return position;
	}

	public void setPosition(Position position)
	{
		this.position = position;
	}
}
