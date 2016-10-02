package main;

public class Position
{
	public Integer row;
	public Integer column;
	
	public Position( Integer xy )
	{
		this(xy, xy);
	}
	
	public Position(Integer row, Integer column)
	{
		this.row = row;
		this.column = column;
	}
	
	public Position(Position position)
	{
		this.row = position.row;
		this.column = position.column;
	}
	
	@Override
	public String toString()
	{
		return "pos(" + this.row + ", " + this.column + ")";
	}
	
	@Override
	public boolean equals(Object obj)
	{
	    if (obj == null) {
	        return false;
	    }
	    if (!Position.class.isAssignableFrom(obj.getClass()))
	    {
	        return false;
	    }
	    final Position pos = (Position) obj;
	    if (!((this.row == pos.row) && (this.column == pos.column)))
	    {
	        return false;
	    }
	    return true;
	}
}