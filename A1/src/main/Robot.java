package main;
public class Robot
{
	// Useful enums
	public enum Direction
	{
		NORTH, WEST, SOUTH, EAST
	};

	/**
	 * Returns an associated action cost.
	 * @return The cost of this action
	 */
	public enum Action {
		SUCK, FORWARDS, TURNLEFT, TURNRIGHT;
		
		public int suck()
		{
			return 10;
		}
		public int forwards()
		{
			return 50;
		}
		public int turnLeft()
		{
			return this.turnRight();
		}
		public int turnRight()
		{
			return 20;
		}
	};
	
	// robot variables
	private Position  position;
	private Direction robotOrientation;
	
	public Robot(Position position, Direction orientation)
	{
		this.position = position;
		this.robotOrientation = orientation;
	}
	
	public Robot(Robot existingRobot) {
		position = new Position(existingRobot.position.row, existingRobot.position.column);
		robotOrientation = existingRobot.robotOrientation;
		
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
	
	public Position getCellRightOfRobot()
	{
		Position robotPos = new Position(position);
		
		switch (this.getOrientation()) {
			case EAST:
					robotPos.row += 1;
				break;
			case SOUTH:
					robotPos.column -= 1;
				break;
			case WEST:
					robotPos.row -= 1;
				break;
			case NORTH:
					robotPos.column += 1;
				break;
		}
		return robotPos;
	}
}
