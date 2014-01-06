package mechanicalman.flowchart;
public class Coord
{
	private int x;
	private int y;
	
	public Coord()
	{
		x = 0;
		y = 0;
	}
	
	public Coord(int newX, int newY)
	{
		x = newX;
		y = newY;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public Coord getCoord()
	{
		return this;
	}
	
	public void setX(int newX)
	{
		x = newX;
	}
	
	public void setY(int newY)
	{
		y = newY;
	}
	
	public void setCoord(Coord newCoord)
	{
		x = newCoord.getX();
		y = newCoord.getY();
	}
	
	public boolean equals(Object other)
	{
		Coord o = (Coord) other;
		return (x == o.getX()) && (y == o.getY());
	}
	
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
}