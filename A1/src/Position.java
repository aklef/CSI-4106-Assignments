public class Position
{
	public final Integer x;
	public final Integer y;
	
	public Position( Integer xy )
	{
		this(xy, xy);
	}
	
	public Position(Integer x, Integer y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString()
	{
		return "P(" + this.x + ", " + this.y + ")";
	}
	
	@Override
	public boolean equals(Object p) {
        boolean equal = false;
        
        if (p instanceof Position)
        {
        	Position pos = (Position) p;
            equal = ((this.x == pos.x) && (this.y == pos.y));
        }
        return equal;
	}
}