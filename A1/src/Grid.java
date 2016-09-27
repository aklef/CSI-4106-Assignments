public class Grid
{
	/**
	 * The grid is accessed in [row][column] style. 1st dimension represents the
	 * width, 2nd dimension represents the height
	 */
	private Cell	cells[][];
	private Integer height;
	private Integer width;
	
	public Grid(Integer size)
	{
		this(size, size);
	}
	
	private Grid(Integer width, Integer height)
	{
		if (width < 1 || height < 1)
			throw new RuntimeException(
					"Cannot create a grid with a dimension less than 1");
		
		this.setHeight(height);
		this.setWidth(width);
		
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
}
