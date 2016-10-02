package main;

public class Position
{
	public int row;
	public int column;
	
	public Position( int xy )
	{
		this(xy, xy);
	}
	
	public Position(int row, int column)
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
	    else if (!Position.class.isAssignableFrom(obj.getClass()))
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