import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class RobotApp
{
	private final static Integer order = 6;
	private static Robot robot;
	private static Grid grid;
	private static Random rng;
	private enum Search
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
		Integer searchType = rng.nextInt(3)+1;
		
		List<Position> obstacles = generateObstacles();
		List<Position> dirt = generateDirt(obstacles);
		
		Position startPos = generateStartPos(obstacles, dirt);
		Robot.Direction startDir = randomEnum(Robot.Direction.class);
		
		System.out.print(" Done.\n");
		
		robot = new Robot(startPos, startDir);
		
		boolean special = false;
		while(!special)
		{
			grid = generateGrid(order, dirt, obstacles, robot);
			
		}
		
		List<Position> solution = search(searchType, grid);
		
		printSolution(solution);
	}

	/************************************** LOGIC ****************************************/

	private static List<Position> generateObstacles()
	{
		int amtObs = 0;
		int maxObs = rng.nextInt(order)+order;
		List<Position> obstacles = new LinkedList<Position>();
		
		for (int i = 0; i < order; i++)
		{
			for (int j = 0; j < order; j++)
			{
				if (rng.nextInt(order*order) < (order) && amtObs != maxObs)
				{
					obstacles.add(new Position(i, j));
					amtObs++;
				}
			}
		}
		return obstacles;
	}

	private static List<Position> generateDirt( List<Position> obstacles )
	{
		int amtDirt = 0;
		int maxDirt = rng.nextInt(order) + order/2;
		List<Position> dirt = new LinkedList<Position>();
		
		for (int i = 0; i < order; i++)
		{
			for (int j = 0; j < order; j++)
			{
				if (rng.nextInt(order*order) < (order) && amtDirt != maxDirt)
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
	
	private static Position generateStartPos( List<Position> obstacles, List<Position> dirt )
	{
		boolean obstructed = true;
		int rndx = rng.nextInt(order) + 1, rndy = rng.nextInt(order) + 1;
		Position newStartPos = new Position(rndx, rndy);
		
		while (obstructed)
		{	
			// Out of bounds
			if (rndx < 0 || rndx > order || rndy < 0 || rndy > order)
			{
				throw new RuntimeException("Andréas broke the start position generation and generated " + newStartPos);
			}
			// Invalid position
			else if (!obstacles.contains(newStartPos) && !dirt.contains(newStartPos) )
			{
				rndx = rng.nextInt(order) + 1;
				rndy = rng.nextInt(order) + 1;
				newStartPos = new Position(rndx, rndy);
			}
			// valid
			else 
			{
				obstructed = false;
			}
			
		}
		return newStartPos;
	}
	
	public static Grid generateGrid(
			Integer gridSize,
			List<Position> obstacles,
			List<Position> dirt,
			Robot robot)
	{
		Grid newGrid = new Grid(gridSize);
		
		for (Position o : obstacles)
		{
			if (o.x < 0 || o.x >= gridSize || o.y < 0 || o.y >= gridSize)
			{
				throw new RuntimeException(
					"One of your obstacles is out of bounds");
			}
			newGrid.getCell(o.x, o.y).setObstructed();
		}
		
		for (Position d : dirt)
		{
			if (d.x < 0 || d.x >= gridSize || d.y < 0 || d.y >= gridSize) 
			{
				throw new RuntimeException(
					"One of your dirt piles is out of bounds"); 
			}
			newGrid.getCell(d.x, d.y).setDirty();
		}
		
		newGrid.setRobot(robot);
		
		return newGrid;
	}
	
    /**
     * @param searchType  1=DFS, 2=BFS, 3=A
     * @return *
     */
	private static List<Position> search (Integer searchType, Grid grid)
	{
		Search search = null;
		switch (searchType) {
			case 1:
				search = Search.DFS;
				break;
			case 2:
				search = Search.BFS;
				break;
			case 3:
				search = Search.Astar;
				break;
			default:
				System.out.println("ERROR! Search value received was " + searchType);
				break;
		}
		
		System.out.format("%s %s...\n", "Search running. Using", search);
		
		List<Position> solution = null;
		boolean done = false;
		
		while (!done)
		{
			System.out.println(visualize());
			break;
		}
		
		// TODO Implement searches
		return solution;
	}
	
	/********************************* HELPER MEHTODS *************************************/
	
	public static String visualize()
	{
		int width = RobotApp.grid.getWidth();
		int height = RobotApp.grid.getHeight();
		String vis = "╔";
		for (int j = 0; j < width; j++)
		{
			vis += "══";
		}
		vis += "╗" + System.lineSeparator();
		for (int i = 0; i < height; i++)
		{
			vis += "║";
			for (int j = 0; j < width; j++)
			{
				if (robot.getPosition().x == j && robot.getPosition().y == i)
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
					vis += RobotApp.grid.getCell(i,j);
				}
				
			}
			vis += "║" + System.lineSeparator();
		}
		vis += "╚";
		for (int j = 0; j < width; j++)
		{
			vis += "══";
		}
		return vis += "╝";
	}
	
	public static void printSolution(List<Position> solution)
	{
		if (solution == null)
			return;
		
		for (Position pos : solution) {
			System.out.println(grid.getCell(pos).toString());
		}
	}
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = rng.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
