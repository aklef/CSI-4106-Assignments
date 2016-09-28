

public class Grid
{
	/**
	 * The grid is accessed in [row][column] style. 1st dimension represents the
	 * width (x), 2nd dimension represents the height (y)
	 */
	private Cell	cells[][];
	private Robot 	robot;
	private Integer width;
	private Integer height;
	private int maxX;
	private int maxY;
	
	public Grid(Integer size)
	{
		this(size, size);
	}
	
	private Grid(Integer width, Integer height)
	{
		if (width < 1 || height < 1)
			throw new RuntimeException(
					"Cannot create a grid with a dimension less than 1");

		this.setWidth(width);
		this.setHeight(height);
		this.maxX = getWidth()-1;
		this.maxY = getHeight()-1;
		
		this.cells = new Cell[width][height];
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				cells[i][j] = new Cell();
			}
		}
	}

	public Cell getCell(Position pos)
	{
		return this.getCell(pos.x, pos.y);
	}
	
	public Cell getCell(Integer row, Integer column)
	{
		if (row < 0 || row >= getWidth() || column < 0 || column >= height) { throw new RuntimeException(
				"Cannot get a cell from out of bounds!"); }
		
		return cells[row][column];
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public Integer getHeight()
	{
		return height;
	}
	
	private void setHeight(Integer height)
	{
		this.height = height;
	}
	
	public Cell getCellInFrontOfRobot()
	{
		Cell cellInFrontOfRobot = null;
		Position robotPos = robot.getPosition();
		int x = robotPos.x, y = robotPos.y;
		
		switch (robot.getOrientation()) {
			case EAST:
				if (x != maxX)
				{
					cellInFrontOfRobot = cells[x+1][y];
				}
				break;
			case SOUTH:
				if (y != maxY)
				{
					cellInFrontOfRobot = cells[x][y+1];
				}
				break;
			case WEST:
				if (x != 0)
				{
					cellInFrontOfRobot = cells[x-1][y];
				}
				break;
			case NORTH:
				if (y != 0)
				{
					cellInFrontOfRobot = cells[x][y-1];
				}
				break;
		}
		return cellInFrontOfRobot;
	}
		
	public Cell getCellLeftOfRobot()
	{
		Cell cellLeftOfRobot = null;
		Position robotPos = robot.getPosition();
		int x = robotPos.x, y = robotPos.y;
		
		switch (robot.getOrientation()) {
			case EAST:
				if (y != 0)
				{
					cellLeftOfRobot = cells[x][y-1];
				}
				break;
			case SOUTH:
				if (x != maxX)
				{
					cellLeftOfRobot = cells[x+1][y];
				}
				break;
			case WEST:
				if (y != maxY)
				{
					cellLeftOfRobot = cells[x][y+1];
				}
				break;
			case NORTH:
				if (x != 0)
				{
					cellLeftOfRobot = cells[x-1][y];
				}
				break;
		}
		return cellLeftOfRobot;
	}
	
	public Cell getCellRightOfRobot()
	{
		Cell cellRightOfRobot = null;
		Position robotPos = robot.getPosition();
		int x = robotPos.x, y = robotPos.y;
		
		switch (robot.getOrientation()) {
			case EAST:
				if (y != maxY)
				{
					cellRightOfRobot = cells[x][y+1];
				}
				break;
			case SOUTH:
				if (x != 0)
				{
					cellRightOfRobot = cells[x-1][y];
				}
				break;
			case WEST:
				if (y != 0)
				{
					cellRightOfRobot = cells[x][y-1];
				}
				break;
			case NORTH:
				if (x != maxX)
				{
					cellRightOfRobot = cells[x+1][y];
				}
				break;
		}
		return cellRightOfRobot;
	}
	
	public void setRobot(Robot robot)
	{
		this.robot = robot;
	}
}
