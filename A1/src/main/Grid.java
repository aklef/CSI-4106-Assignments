package main;

public class Grid
{
	/**
	 * The grid is accessed in [row][column] style. 1st dimension represents the
	 * height (row), 2nd dimension represents the width (column)
	 */
	private Cell	cells[][];
	private Robot   robot;
	private Integer width;
	private Integer height;
	private int	 maxRow;
	private int	 maxCol;
	
	public Grid(Integer size)
	{
		this(size, size);
	}
	
	private Grid(Integer rows, Integer columns)
	{
		if (rows < 1 || columns < 1)
			throw new RuntimeException(
					"Cannot create a grid with a dimension less than 1");

		this.setHeight(rows);
		this.setWidth(columns);
		this.maxRow = getWidth() - 1;
		this.maxCol = getHeight() - 1;
		
		this.cells = new Cell[rows][columns];
		
		for (int row = 0; row < rows; row++)
		{
			for (int column = 0; column < columns; column++)
			{
				this.cells[row][column] = new Cell();
			}
		}
	}
	
	public Cell getCell(Position pos)
	{
		return this.getCell(pos.row, pos.column);
	}
	
	public Cell getCell(Integer row, Integer column)
	{
		if (row < 0 || row >= getWidth() || column < 0 || column >= height) { throw new RuntimeException(
				"Cannot get a cell from out of bounds!"); }
		
		return cells[row][column];
	}
	
	public Position getPosition(Cell cell)
	{
		Position pos = null;
		
		// Iterate over the rows
		for (int row = 0; row < cells.length; row++)
		{
			// Iterate over the columns
			for (int column = 0; column < cells[row].length; column++)
			{
				if (cell.equals(cells[row][column]))
				{
					pos = new Position(row, column);
				}
			}
		}
		return pos;
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
		int row = robotPos.row, column = robotPos.column;
		
		switch (robot.getOrientation()) {
			case EAST:
				if (column != maxCol)
				{
					cellInFrontOfRobot = getCell(row, column + 1);
				}
				break;
			case SOUTH:
				if (row != maxRow)
				{
					cellInFrontOfRobot = getCell(row + 1, column);
				}
				break;
			case WEST:
				if (column != 0)
				{
					cellInFrontOfRobot = getCell(row, column - 1);
				}
				break;
			case NORTH:
				if (row != 0)
				{
					cellInFrontOfRobot = getCell(row - 1, column);
				}
				break;
		}
		return cellInFrontOfRobot;
	}
	
	public Cell getCellLeftOfRobot()
	{
		Cell cellLeftOfRobot = null;
		Position robotPos = robot.getPosition();
		int row = robotPos.row, column = robotPos.column;
		
		switch (robot.getOrientation()) {
			case EAST:
				if (row != 0)
				{
					cellLeftOfRobot = getCell(row - 1, column);
				}
				break;
			case SOUTH:
				if (column != maxCol)
				{
					cellLeftOfRobot = getCell(row, column + 1);
				}
				break;
			case WEST:
				if (row != maxRow)
				{
					cellLeftOfRobot = getCell(row + 1, column);
				}
				break;
			case NORTH:
				if (column != 0)
				{
					cellLeftOfRobot = getCell(row, column - 1);
				}
				break;
		}
		return cellLeftOfRobot;
	}
	
	public Cell getCellRightOfRobot()
	{
		Cell cellRightOfRobot = null;
		Position robotPos = robot.getPosition();
		int row = robotPos.row, column = robotPos.column;
		
		switch (robot.getOrientation()) {
			case EAST:
				if (row != maxRow)
				{
					cellRightOfRobot = getCell(row + 1, column);
				}
				break;
			case SOUTH:
				if (column != 0)
				{
					cellRightOfRobot = getCell(row, column - 1);
				}
				break;
			case WEST:
				if (row != 0)
				{
					cellRightOfRobot = getCell(row - 1, column);
				}
				break;
			case NORTH:
				if (column != maxCol)
				{
					cellRightOfRobot = getCell(row, column + 1);
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
