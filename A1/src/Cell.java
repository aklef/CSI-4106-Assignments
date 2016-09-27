public class Cell
{
	private Boolean visited;
	private Boolean closed;
	private Boolean isDirty;
	private Boolean isObstructed;
	
	public Cell()
	{
		visited = false;
		closed = false;
		isDirty = false;
		isObstructed = false;
	}
	
	public Cell(Boolean isVisited,
				Boolean isClosed,
				Boolean isDirty,
				Boolean isObstructed)
	{
		this.visited = isVisited;
		this.closed = isClosed;
		this.isDirty = isDirty;
		this.isObstructed = isObstructed;
	}
	
	public Boolean getVisited()
	{
		return visited;
	}
	
	public void setVisited(Boolean visited)
	{
		this.visited = visited;
	}
	
	public Boolean getClosed()
	{
		return closed;
	}
	
	public void setClosed(Boolean closed)
	{
		this.closed = closed;
	}
	
	public Boolean isDirty()
	{
		return this.isDirty;
	}
	
	public void setDirty()
	{
		this.isDirty = true;
	}
	
	public Boolean isObstructed()
	{
		return this.isObstructed;
	}
	
	public void setObstructed()
	{
		this.isObstructed = true;
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
			cell = "▒▒";
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
