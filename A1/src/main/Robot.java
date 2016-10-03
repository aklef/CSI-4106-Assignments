package main;

public class Robot
{
	// Useful enums
	public enum Orientation
	{
		NORTH, EAST, SOUTH, WEST;

		public static Orientation valueOf(int n)
		{
			for (Orientation dir : Orientation.values())
			{
				if (dir.ordinal() == n)
					return dir;
			}
			return null;
		}
		
		@Override
	    public String toString()
		{
			String s = this.name();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
	    }
	};

	/**
	 * Returns an associated action cost.
	 * @return The cost of this action
	 */
	public enum Action {
		SUCK, MOVE, LEFT, RIGHT;
		/**
		 * Returns the cost of this Action.
		 */
		public static int cost(Action action)
		{
			switch (action)
			{
				case LEFT: case RIGHT:
					return 20;
				case SUCK:
					return 10;
				case MOVE:
					return 50;
				default:
					return 0;
			}
		}
		

		@Override
	    public String toString()
		{
			return this.name().toLowerCase();
	    }
	};
	
	// robot variables
	private Position  position;
	private Orientation robotOrientation;
	
	public Robot(Position position, Orientation orientation)
	{
		this.position = position;
		this.robotOrientation = orientation;
	}
	
	public Robot(Robot robot)
	{
		if (robot == null)
			throw new NullPointerException();
		
		this.position = new Position(robot.position);
		this.robotOrientation = robot.robotOrientation;
	}
	
	public Orientation getOrientation()
	{
		return robotOrientation;
	}
	
	/**
	 * Changes this Robot's Orientation.
	 */
	public void turn(Action dir)
	{
		switch (dir)
		{
			case LEFT:
				this.robotOrientation = Orientation.valueOf((robotOrientation.ordinal() + 3) % 4);
				break;
			case RIGHT:
				this.robotOrientation = Orientation.valueOf((robotOrientation.ordinal() + 1) % 4);
				break;
			
			default:
				throw new RuntimeException("Invalid turn Action");
		}
		return;
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
