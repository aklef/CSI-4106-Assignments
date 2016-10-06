package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import main.Robot.Action;

public class DFS extends Algorithm {

	protected Stack<Path> openStates;

	public DFS(Grid grid)
	{
		this.grid = grid;
		this.openStates = new Stack<Path>();
		this.closedStates = new LinkedList<Path>();
	}

	@Override
	protected List<Path> computeSolution() {

		Robot robot = this.grid.getRobot();
		Path firstNode = new Path(robot, 0);
		Path finalNode = null;
		List<Path> nodesWhichSucked = new ArrayList<Path>();
		
		this.openStates.push(firstNode);
		
		while (!this.openStates.isEmpty() && finalNode == null)
		{
			Path node = this.openStates.pop();
			this.closedStates.add(node);
			Robot tempBot;
			Path newPath;
			
			
			
			for (int i = Action.values().length - 1; i >= 0 ; i--)
			{
				Action action = (Action.values())[i];
				tempBot = new Robot(node.roboClone);
				newPath = null;
				
				switch (action)
				{
					case LEFT: case RIGHT:
						tempBot.turn(action);
						newPath = new Path(node, tempBot, action, node.cost
								+ Action.cost(action), node.getCellsAlreadyCleaned());
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
						newPath = new Path(node, tempBot, action, node.cost 
								+ Action.cost(action), node.getCellsAlreadyCleaned());
						break;
					
					case SUCK:
						// WE ARE NOT ACTUALLY IMPACTING THE GRID DURING A
						// SEARCH
						
						Position cleanBotPosition = tempBot.getPosition();
						
						// if
						// (!node.remainingDirtyCells.contains(cleanBotPosition))
						// {
						// continue;
						// }
						//
						// LinkedList<Position> newDirtyList = new
						// LinkedList<Position>(node.remainingDirtyCells);
						//
						// if (!newDirtyList.remove(cleanBotPosition))
						// {
						// throw new
						// RuntimeException("Tried to create a path in BFS.computeSolution() which had one less dirty tiles, but the tile to be removed was not in the dirty list");
						// }
						Cell cell = null;
						try
						{
							cell = grid.getCell(cleanBotPosition);
						}
						catch (OutOfBoundsException e)
						{
							continue;
						}
						
						if (!cell.isDirty() || node.getCellsAlreadyCleaned().contains(cell))
						{
							continue;
						}
						else
						{
							newPath = new Path(node, tempBot, action, node.cost
									+ Action.cost(action), node.getCellsAlreadyCleaned());
							nodesWhichSucked.add(newPath);
							newPath.addCleanedCell(cell);
						}
						break;
				}
				
				if (!(openStates.contains(newPath) || closedStates.contains(newPath)))
				{
					openStates.push(newPath);
				}
			}
		}
		
		Collections.sort(nodesWhichSucked, new Comparator<Path>()
		{
			public int compare(Path p1, Path p2)
			{
				return Integer.compare(p2.getCellsAlreadyCleaned().size(), p1.getCellsAlreadyCleaned().size());
			}
		});
		
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
}
