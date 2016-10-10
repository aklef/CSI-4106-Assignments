package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.Robot.Orientation;

public class RobotApp
{
	private final static int order = 4;
	private static Robot robot;
	private static Grid grid;
	private static Random rng;
	
	
	public enum SearchType
	{
		DFS, BFS, Astar;
		
		/**
		 * Get an enum value based on its ordinal value.
		 * @param n the ordinal of the enum vlaue desired
		 * @return The correspoinding enum value
		 */
		public static SearchType valueOf(int n)
		{
			for (SearchType srch : SearchType.values())
			{
				if (srch.ordinal() == n)
					return srch;
			}
			return null;
		}
	};
	
	//************************************** MAIN *****************************************//
	
	public static void main(String[] args)
	{
		System.out.print("Generating random states...");
		// Roll that rng
		rng = new Random();
		// TEMP
		// Gen search type
//		SearchType searchType =  randomEnum(SearchType.class);
		SearchType searchType = SearchType.Astar;
		
		List<Position> obstacles = generateObstacles();
		List<Position> dirt = generateDirt(obstacles);
		
		Position startPos = generateStartPos(obstacles, dirt);
		Orientation startDir = randomEnum(Orientation.class);
		System.out.print(" Done.\n");
		
		robot = new Robot(startPos, startDir);
		
		grid = generateGrid(order, obstacles, dirt, robot);
		System.out.format("Robot is at %s facing %s\n", robot.getPosition(), robot.getOrientation());
		
		//SPECIAL TEST CASE 1 - PLEASE DELETE
		
//				Position teststartPos = new Position(1,0);
				
//				grid = null;
//				grid = new Grid(2);
//				Cell testcell = null;
//				Cell testcell2 = null;
//				Cell testcell3 = null;
//				try {
//					testcell = grid.getCell(new Position(1,1));
//					testcell2 = grid.getCell(new Position(0,1));
//					testcell3 = grid.getCell(new Position(0,0));
//				} catch (OutOfBoundsException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return;
//				}
//				testcell.setDirty(true);
//				testcell2.setObstructed(true);
//				testcell3.setObstructed(true);
//				robot = new Robot(teststartPos, Orientation.WEST);
//				grid.setRobot(robot);
//				grid.setDirt(new LinkedList<Position>(Arrays.asList(new Position(1,1))));
				
				//SPECIAL TEST CASE - PLEASE DELETE
		
		long startTime = System.currentTimeMillis();
		List<Path> solution = search(searchType, startTime);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		
		printSolution(solution, elapsedTime);
		
		System.exit(0);
	}
	
	//************************************** LOGIC ****************************************//
	
	private static List<Position> generateObstacles()
	{
		int amtObs = 0;
		int maxObs = rng.nextInt(order-1) + order / 2;
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
		int maxDirt = rng.nextInt(order) + order;
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
				if (dirt.contains(newStartPos))
				{
					dirt.remove(newStartPos);
				}
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
	
	@SuppressWarnings("unused")
	private static List<Path> search(Integer searchType, Long startTime)
	{
		return search(SearchType.valueOf(searchType), startTime);
	}
	
	/**
	 * @param type 1=DFS, 2=BFS, 3=A
	 */
	private static List<Path> search(SearchType searchType, Long startTime)
	{
		Algorithm search = null;
		
		switch (searchType)
		{
			case BFS:
				search = new BFS(grid);
				break;
			case DFS:
				search = new DFS(grid);
				break;
			case Astar:
				search = new AStar(grid);
				break;
		}

		System.out.println(visualize());
		System.out.format("%s %s... ", "Search using", searchType);
		
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
			throw new NullPointerException("Solution cannot be null!");

		System.out.print("Done.\n");
		Position pos;
		Robot robot;
		int cost = 0;
		int depth = 0;
		
		if (solution.isEmpty())
		{
			throw new NullPointerException("Solution empty. BAD");
		}
		else
		{
			cost = solution.get(solution.size()-1).cost;
			System.out.println("pos(row, col), Dir, action");
			for (Path node : solution)
			{
				robot = node.roboClone;
				pos = robot.getPosition();
				System.out.format("%s, %s, %s\n", pos, robot.getOrientation(), depth == 0? "start": node.action);
				depth++;
			}
		}
		
		System.out.format("total cost: %s\n", cost);
		System.out.format("Depth: %s\n", depth);
		String alt = "";
		double time = elapsedTime/1000.0;
		if (time >= 1.1)
		{
			alt = String.format(" = %.2f s", time);
		}
		System.out.format("Time : %s ms%s", elapsedTime, alt);
	}
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz)
	{
		int x = rng.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}
}
