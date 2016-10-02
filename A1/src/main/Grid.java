package main;

import java.util.LinkedList;
import java.util.List;

public class Grid
{
	/**
	 * The grid is accessed in [row][column] style. 1st dimension represents the
	 * height (row), 2nd dimension represents the width (column)
	 */
	private Cell	cells[][];
	private Robot   robot;
	private int width;
	private int height;
	private List<Position> dirt;
	
	public Grid(int size)
	{
		this(size, size);
	}
	
	private Grid(int rows, int columns)
	{
		if (rows < 1 || columns < 1)
			throw new RuntimeException("Cannot create a grid with a dimension less than 1");

		this.setHeight(rows);
		this.setWidth(columns);
		
		this.cells = new Cell[rows][columns];
		
		for (int row = 0; row < rows; row++)
		{
			for (int column = 0; column < columns; column++)
			{
				this.cells[row][column] = new Cell();
			}
		}
		
		dirt = new LinkedList<Position>();
	}
	
	public Cell getCell(Position pos) throws OutOfBoundsException
	{
		return this.getCell(pos.row, pos.column);
	}
	
	public Cell getCell(int row, int column) throws OutOfBoundsException
	{
		if (row < 0 || row >= getWidth() || column < 0 || column >= height)
		{
			throw new OutOfBoundsException("Cannot get a cell from out of bounds!");
		}
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
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	private void setHeight(int height)
	{
		this.height = height;
	}
	
	public void setRobot(Robot robot)
	{
		this.robot = robot;
	}

	public Robot getRobot()
	{
		return robot;
	}

	public List<Position> getDirt()
	{
		return dirt;
	}

	public void setDirt(List<Position> dirt)
	{
		this.dirt = dirt;
	}
}
