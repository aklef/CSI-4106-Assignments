package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.Robot.Direction;
import main.Search.SearchType;

@SuppressWarnings("unused")

public class RobotApp
{
	private final static Integer order = 6;
	private static Robot robot;
	private static Grid grid;
	private static Random rng;
	
	
	public enum SearchType
	{
		DFS, BFS, Astar
	};
	
	/************************************** MAIN *****************************************/
	
	public static void main(String[] args)
	{
		System.out.print("Generating random states...");
		// Roll that rng
		rng = new Random();
		// TEMP
		// Gen search type
		Integer searchType = rng.nextInt(3) + 1;
		
		List<Position> obstacles = generateObstacles();
		List<Position> dirt = generateDirt(obstacles);
		
		Position startPos = generateStartPos(obstacles, dirt);
		Direction startDir = randomEnum(Direction.class);
		System.out.print(" Done.\n");
		
		robot = new Robot(startPos, startDir);
		
		grid = generateGrid(order, dirt, obstacles, robot);
		
		System.out.format("Robot is at %s\n", robot.getPosition());
		
		List<Position> solution = search(searchType, grid);
		
		printSolution(solution);
	}
	
	/************************************** LOGIC ****************************************/
	
	private static List<Position> generateObstacles()
	{
		int amtObs = 0;
		int maxObs = rng.nextInt(order) + order;
		List<Position> obstacles = new LinkedList<Position>();
		
		for (int i = 0; i < order; i++)
		{
			for (int j = 0; j < order; j++)
			{
				if (rng.nextInt(order * order) < (order) && amtObs != maxObs)
				{
					obstacles.add(new Position(i, j));
					amtObs++;
				}
			}
		}
		return obstacles;
	}
	
	private static List<Position> generateDirt(List<Position> obstacles)
	{
		int amtDirt = 0;
		int maxDirt = rng.nextInt(order) + order / 2;
		List<Position> dirt = new LinkedList<Position>();
		
		for (int i = 0; i < order; i++)
		{
			for (int j = 0; j < order; j++)
			{
				if (rng.nextInt(order * order) < (order) && amtDirt != maxDirt)
				{
					Position newDirtPos = new Position(i, j);
					
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
		int rndRow = rng.nextInt(order) + 1, rndcol = rng.nextInt(order) + 1;
		Position newStartPos = new Position(rndRow, rndcol);
		
		while (obstructed)
		{
			// Out of bounds
			if (rndRow < 0 || rndRow > order || rndcol < 0 || rndcol > order)
			{
				throw new RuntimeException(
						"Andréas broke the start position generation and generated "
								+ newStartPos);
			}
			// Invalid position
			else if (!obstacles.contains(newStartPos)
					&& !dirt.contains(newStartPos))
			{
				rndRow = rng.nextInt(order) + 1;
				rndcol = rng.nextInt(order) + 1;
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
	
	public static Grid generateGrid(Integer gridSize, List<Position> obstacles,
			List<Position> dirt, Robot robot)
	{
		Grid newGrid = new Grid(gridSize);
		
		for (Position o : obstacles)
		{
			if (o.row < 0 || o.row >= gridSize || o.column < 0
					|| o.column >= gridSize) { throw new RuntimeException(
					"One of your obstacles is out of bounds"); }
			newGrid.getCell(o.row, o.column).setObstructed();
		}
		
		for (Position d : dirt)
		{
			if (d.row < 0 || d.row >= gridSize || d.column < 0
					|| d.column >= gridSize) { throw new RuntimeException(
					"One of your dirt piles is out of bounds"); }
			newGrid.getCell(d.row, d.column).setDirty();
			
			//This will be used by each algorithm's statemap. This behavior of setting cells (dirty and obstructed) is Grid behavior and should be in the Grid class.
			newGrid.addInitialDirtyCell(new Position(d.row, d.column));
		}
		
		newGrid.setRobot(robot);
		
		return newGrid;
	}
	
	/**
	 * @param type
	 *            1=DFS, 2=BFS, 3=A
	 * @return *
	 */
	private static List<Path> search(SearchType searchType)
	{
		switch (searchType) {
			case BFS:
				break;
			case DFS:
				break;
			case Astar:
				break;
		}
		
		System.out.format("%s %s...\n", "SearchType running. Using", searchType);
		System.out.println(visualize());
		
		
		List<Path> solution = null;
			 
		//TODO implement searches
		return solution;
	}
	
	/********************************* HELPER MEHTODS *************************************/
	
	public static String visualize()
	{
		int columns = grid.getWidth();
		int rows = grid.getHeight();
		Position roboPos = robot.getPosition();
		String vis = "╔";
		for (int j = 0; j < columns; j++)
		{
			vis += "══";
		}
		vis += "╗" + System.lineSeparator();
		for (int row = 0; row < rows; row++)
		{
			vis += "║";
			for (int column = 0; column < columns; column++)
			{
				if (roboPos.row == row && roboPos.column == column)
				{
					switch (robot.getOrientation()) {
						case NORTH:
							vis += "↑↑";
							break;
						case EAST:
							vis += "→→";
							break;
						case SOUTH:
							vis += "↓↓";
							break;
						case WEST:
							vis += "←←";
							break;
					}
				}
				else
				{
					vis += grid.getCell(row, column);
				}
				
			}
			vis += "║" + System.lineSeparator();
		}
		vis += "╚";
		for (int j = 0; j < columns; j++)
		{
			vis += "══";
		}
		return vis += "╝";
	}
	
	public static void printSolution(List<Position> solution)
	{
		if (solution == null)
			return;
		
		for (Position pos : solution)
		{
			System.out.println(grid.getCell(pos).toString());
		}
	}
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz)
	{
		int x = rng.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}
}
