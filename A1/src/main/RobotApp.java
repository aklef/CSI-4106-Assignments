package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.Robot.Orientation;

public class RobotApp
{
	private final static int order = 3;
	private static Robot robot;
	private static Grid grid;
	private static Random rng;
	
	
	public enum SearchType
	{
		DFS, BFS, Astar
	};
	
	//************************************** MAIN *****************************************//
	
	public static void main(String[] args)
	{
		System.out.print("Generating random states...");
		// Roll that rng
		rng = new Random();
		// TEMP
		// Gen search type
		SearchType searchType = SearchType.BFS;
		
		List<Position> obstacles = generateObstacles();
		List<Position> dirt = generateDirt(obstacles);
		
		Position startPos = generateStartPos(obstacles, dirt);
		Orientation startDir = randomEnum(Orientation.class);
		System.out.print(" Done.\n");
		
		robot = new Robot(startPos, startDir);
		
		grid = generateGrid(order, dirt, obstacles, robot);
		System.out.format("Robot is at %s facing %s\n", robot.getPosition(), robot.getOrientation());
		
		
		long startTime = System.currentTimeMillis();
		List<Path> solution = search(searchType);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		
		printSolution(solution, elapsedTime);
		
		System.exit(0);
	}
	
	//************************************** LOGIC ****************************************//
	
	private static List<Position> generateObstacles()
	{
		int amtObs = 0;
		int maxObs = rng.nextInt(order) + order;
		List<Position> obstacles = new LinkedList<Position>();
		
		for (int row = 0; row < order; row++)
		{
			for (int column = 0; column < order; column++)
			{
				if (rng.nextInt(order * order) < (order) && amtObs != maxObs)
				{
					obstacles.add(new Position(row, column));
					amtObs++;
				}
			}
		}
		return obstacles;
	}
	
	private static List<Position> generateDirt(List<Position> obstacles)
	{
		int amtDirt = 0;
		// the values below are critical, DO NOT CHANGE.
		int maxDirt = rng.nextInt(order-1) + order / 2;
		List<Position> dirt = new LinkedList<Position>();
		
		for (int row = 0; row < order; row++)
		{
			for (int column = 0; column < order; column++)
			{
				if (rng.nextInt(order * order) < (order) && amtDirt != maxDirt)
				{
					Position newDirtPos = new Position(row, column);
					
					if (!obstacles.contains(newDirtPos))
					{
						dirt.add(newDirtPos);
						amtDirt++;
					}
				}
			}
		}
		return dirt;
	}
	
	private static Position generateStartPos(List<Position> obstacles,
			List<Position> dirt)
	{
		boolean obstructed = true;
		int rndRow = rng.nextInt(order), rndcol = rng.nextInt(order);
		Position newStartPos = new Position(rndRow, rndcol);
		
		while (obstructed)
		{
			// Out of bounds
			if (rndRow < 0 || rndRow >= order || rndcol < 0 || rndcol >= order)
			{
				throw new RuntimeException(
						"\nAndréas broke the start position generation and generated "
								+ newStartPos);
			}
			// Invalid position
			// Re-generate
			else if (obstacles.contains(newStartPos))
			{
				rndRow = rng.nextInt(order);
				rndcol = rng.nextInt(order);
				newStartPos = new Position(rndRow, rndcol);
			}
			// valid
			else
			{
				obstructed = false;
			}
			
		}
		return newStartPos;
	}
	
	public static Grid generateGrid(int gridSize, List<Position> obstacles,
			List<Position> dirt, Robot robot)
	{
		Grid newGrid = new Grid(gridSize);
		try
		{
    		for (Position o : obstacles)
    		{
    			if (o.row < 0 || o.row >= gridSize || o.column < 0 || o.column >= gridSize) 
    			{
    				throw new OutOfBoundsException("One of your obstacles is out of bounds");
    			}
    			
    				newGrid.getCell(o.row, o.column).setObstructed(true);
			}
    		for (Position d : dirt)
    		{
    			if (d.row < 0 || d.row >= gridSize || d.column < 0 || d.column >= gridSize)
    			{
    				throw new OutOfBoundsException("One of your dirt piles is out of bounds");
    			}
    			newGrid.getCell(d.row, d.column).setDirty(true);
    		}
		}
		catch (OutOfBoundsException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Grid generation failed :(");
		}
		
		newGrid.setDirt(dirt);
		newGrid.setRobot(robot);
		return newGrid;
	}
	
	/**
	 * @param type 1=DFS, 2=BFS, 3=A
	 */
	private static List<Path> search(SearchType searchType)
	{
		Algorithm search = null;
		
		switch (searchType)
		{
			case BFS:
				search = new BFS(grid);
				break;
			case DFS:
				break;
			case Astar:
				break;
		}
		
		System.out.format("%s %s...\n", "Search running. Using", searchType);
//		System.out.println(visualize());
		
		List<Path> solution = null;
		if(search != null)
		{
			solution = search.computeSolution();
		}
		
		return solution;
	}
	
	//********************************* HELPER MEHTODS **************************************//
	
	public static String visualize()
	{
		int columns = grid.getWidth();
		int rows = grid.getHeight();
		Position roboPos = robot.getPosition();
		String out = "╔";
		for (int col = 0; col < columns; col++)
		{
			out += "══";
		}
		out += "╗" + System.lineSeparator();
		for (int row = 0; row < rows; row++)
		{
			out += "║";
			for (int column = 0; column < columns; column++)
			{
				if (roboPos.row == row && roboPos.column == column)
				{
					switch (robot.getOrientation()) {
						case NORTH:
							out += "↑↑";
							break;
						case EAST:
							out += "→→";
							break;
						case SOUTH:
							out += "↓↓";
							break;
						case WEST:
							out += "←←";
							break;
					}
				}
				else
				{
					try
					{
						out += grid.getCell(row, column);
					}
					catch (OutOfBoundsException e)
					{
						e.printStackTrace();
					}
				}
				
			}
			out += "║" + System.lineSeparator();
		}
		out += "╚";
		for (int j = 0; j < columns; j++)
		{
			out += "══";
		}
		return out += "╝";
	}
	
	public static void printSolution(List<Path> solution, long elapsedTime)
	{
		if (solution == null)
		{
			throw new NullPointerException("Solution cannot be null.");
		}
		
		Position pos;
		Robot robot;
		int cost = solution.get(solution.size()-1).cost;
		int depth = 0;
		System.out.println("pos(row, col), Dir, action");
		System.out.println(visualize());
		
		for (Path node : solution)
		{
			robot = node.roboClone;
			pos = robot.getPosition();
			System.out.format("%s, %s, %s\n", pos, robot.getOrientation(), depth == 0? "start": node.action);
			depth++;
		}
		
		System.out.format("total cost: %s\n", cost);
		System.out.format("Depth: %s\n", depth);
		System.out.format("Time : %s ms", elapsedTime);
	}
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz)
	{
		int x = rng.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}
}
