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
		SUCK, MOVE, LEFT, RIGHT;
		
		/**
		 * Returns the cost of this Action.
		 * @return 10 energy units.
		 */
		public int suck()
		{
			return 10;
		}
		/**
		 * Returns the cost of this Action.
		 * @return 50 energy units.
		 */
		public int forwards()
		{
			return 50;
		}
		/**
		 * Returns the cost of this Action.
		 * @return 20 energy units.
		 */
		public int turnLeft()
		{
			return 20;
		}
		/**
		 * Returns the cost of this Action.
		 * @return 20 energy units.
		 */
		public int turnRight()
		{
			return this.turnLeft();
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
	
	public Robot(Robot existingRobot)
	{
		this.position = new Position(existingRobot.position);
		this.robotOrientation = existingRobot.robotOrientation;
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
	public Position getPosition()
	{
		return position;
	}

	public void setPosition(Position position)
	{
		this.position = position;
	}
	
	public Position getCellInFrontOfRobot()
	{
		Position robotPos = new Position(position);
		
		switch (this.getOrientation()) {
			case EAST:
				robotPos.column += 1;
				break;
			case SOUTH:
				robotPos.row += 1;
				break;
			case WEST:
				robotPos.column -= 1;
				break;
			case NORTH:
				robotPos.row -= 1;
				break;
		}
		return robotPos;
	}
	
	public Position getCellLeftOfRobot()
	{
		Position robotPos = new Position(position);
		
		switch (this.getOrientation()) {
			case EAST:
				robotPos.row -= 1;
				break;
			case SOUTH:
				robotPos.column += 1;
				break;
			case WEST:
				robotPos.row += 1;
				break;
			case NORTH:
				robotPos.column -= 1;
				break;
		}
		return robotPos;
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
