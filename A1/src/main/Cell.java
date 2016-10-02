package main;
public class Cell
{
	private boolean visited;
	private boolean closed;
	private boolean isDirty;
	private boolean isObstructed;
	
	public Cell()
	{
		visited = false;
		closed = false;
		isDirty = false;
		isObstructed = false;
	}
	
	public Cell(Cell existingCell) {
		visited = existingCell.visited;
		closed = existingCell.closed;
		isDirty = existingCell.isDirty;
		isObstructed = existingCell.isObstructed;
	}
	
	public Cell(boolean isVisited,
				boolean isClosed,
				boolean isDirty,
				boolean isObstructed)
	{
		this.visited = isVisited;
		this.closed = isClosed;
		this.isDirty = isDirty;
		this.isObstructed = isObstructed;
	}
	
	public boolean getVisited()
	{
		return visited;
	}
	
	public void setVisited(boolean visited)
	{
		this.visited = visited;
	}
	
	public boolean getClosed()
	{
		return closed;
	}
	
	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}
	
	public boolean isDirty()
	{
		return this.isDirty;
	}
	
	public void setDirty(boolean isDirty)
	{
		this.isDirty = isDirty;
	}
	
	public boolean isObstructed()
	{
		return this.isObstructed;
	}
	
	public void setObstructed(boolean isObstructed)
	{
		this.isObstructed = isObstructed;
	}
	
	/*
	 * Returns a string representation of this cell. 
	 * Hopefully something ascii that looks alright.
	 */
	@Override
	public String toString()
	{
		String cell = "";
		
		if (isDirty)
		{
			cell = "¤¤";
		}
		else if (isObstructed)
		{
			cell = "██";
		}
		else
		{
			cell = "░░";
		}
		return cell;
	}
}
