package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import main.Robot.Action;

/**
 * Our implementation of the Breadth-First Search.
 */
public class BFS extends Algorithm
{
	protected Queue<Path> openStates;
	
	public BFS(Grid grid)
	{
		this.grid = grid;
		this.openStates = new LinkedList<Path>();
		this.closedStates = new LinkedList<Path>();
		this.nodesWhichSucked = new ArrayList<Path>();
	}
	
	@Override
	protected List<Path> computeSolution()
	{
		Robot firstRobot = this.grid.getRobot();
		Path firstNode = new Path(firstRobot, 0);
		Path finalNode = null;
		
		this.openStates.add(firstNode);
		
		while (!this.openStates.isEmpty() && finalNode == null)
		{
			Path node = this.openStates.poll();
			this.closedStates.add(node);
			
			
		}
		
		Collections.sort(nodesWhichSucked, Path.Comparators.CellsAlreadyCleaned);
		
		Path finalPath = null;
		if (nodesWhichSucked.isEmpty())
		{
			finalPath = firstNode;
		}
		else
		{
			finalPath = nodesWhichSucked.get(0);
		}
		
		LinkedList<Path> solution = new LinkedList<Path>();
		while (finalPath != null)
		{
			solution.addFirst(finalPath);
			finalPath = finalPath.parent;
		}
		
		return solution;
	}

	@Override
	protected void computeSuccessors(Path current)
	{
		Robot tempBot;
		Path newPath;
		
		for (Action action : Action.values())
		{
			tempBot = new Robot(current.roboClone);
			newPath = null;
			
			switch (action)
			{
				case LEFT: case RIGHT:
					tempBot.turn(action);
					newPath = new Path(current, tempBot, action, current.cost
							+ Action.cost(action), current.getCellsAlreadyCleaned());
					break;
				
				case MOVE:
					Position newPosition = tempBot.getCellInFrontOfRobot();
					Cell cellInFront;
					try
					{
						cellInFront = grid.getCell(newPosition);
						if (cellInFront.isObstructed())
							continue;
					}
					catch (OutOfBoundsException e)
					{
						continue;
					}
					
					tempBot.setPosition(newPosition);
					newPath = new Path(current, tempBot, action, current.cost 
							+ Action.cost(action), current.getCellsAlreadyCleaned());
					break;
				
				case SUCK:
					// WE ARE NOT ACTUALLY IMPACTING THE GRID DURING A SEARCH
					Position cleanBotPosition = tempBot.getPosition();
					Cell cell = null;
					try
					{
						cell = grid.getCell(cleanBotPosition);
					}
					catch (OutOfBoundsException e)
					{
						continue;
					}
					
					if (!cell.isDirty() || current.getCellsAlreadyCleaned().contains(cell))
					{
						continue;
					}
					else
					{
						newPath = new Path(current, tempBot, action, current.cost
								+ Action.cost(action), current.getCellsAlreadyCleaned());
						nodesWhichSucked.add(newPath);
						newPath.addCleanedCell(cell);
					}
					break;
			}
			
			if (!(openStates.contains(newPath) || closedStates.contains(newPath)))
			{
				this.openStates.add(newPath);
			}
		}
	}
}
