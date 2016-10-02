package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import main.Robot.Action;

/**
 * Our implementation of the Breadth-First Search.
 */
public class BFS extends Algorithm
{
	// private MultiKeyMap stateMap = new MultiKeyMap(); // posx, posy,
	// robotorientation,
	
	protected Queue<Path> openStates;

	public BFS(Grid grid)
	{
		this.grid = grid;
		this.openStates = new LinkedList<Path>();
		this.closedStates = new LinkedList<Path>();
	}
	
	@Override
	protected List<Path> computeSolution()
	{
		Robot robot = this.grid.getRobot();
		Path firstNode = new Path(robot, 0, grid.getDirt());
		Path finalNode = null;
		
		this.openStates.add(firstNode);
		
		while (!this.openStates.isEmpty() && finalNode == null)
		{
			Path node = this.openStates.poll();
			this.closedStates.add(node);
			Robot tempBot;
			Path newPath;
			
			for (Action action : Action.values())
			{
				tempBot = new Robot(node.roboClone);
				newPath = null;
				
				switch (action)
				{
					case LEFT: case RIGHT:
						tempBot.turn(action);
						newPath = new Path(node, tempBot, action, node.cost
								+ Action.cost(action), node.remainingDirtyCells);
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
						newPath = new Path(node, tempBot, action, node.cost + Action.cost(action), node.remainingDirtyCells);
						break;
					
					case SUCK:
						// WE ARE NOT ACTUALLY IMPACTING THE GRID DURING A
						// SEARCH // leftBot.robotClean();

						Position cleanBotPosition = tempBot.getPosition();
						
						if (!node.remainingDirtyCells.contains(cleanBotPosition))
						{
							continue;
						}
						
						LinkedList<Position> newDirtyList = new LinkedList<Position>(node.remainingDirtyCells);
						
						if (!newDirtyList.remove(cleanBotPosition))
						{
							throw new RuntimeException("Tried to create a path in BFS.computeSolution() which had one less dirty tiles, but the tile to be removed was not in the dirty list");
						}
						
						newPath = new Path(node, tempBot, action, node.cost
								+ Action.cost(action), newDirtyList);
						break;
				}
				
				if(!openStates.contains(newPath)
						|| !closedStates.contains(newPath)	)
				{
//					if(!( firstNode.roboClone.getPosition().equals(newPath.roboClone.getPosition()) //FIRST NODE SPECIAL EQUALITY CHECK, CANNOT CHECK ACTION
//							&& firstNode.roboClone.getOrientation().equals(newPath.roboClone.getOrientation())  //FIRST NODE SPECIAL EQUALITY CHECK, CANNOT CHECK ACTION
//							&& firstNode.remainingDirtyCells.equals(newPath.remainingDirtyCells)) ) //FIRST NODE SPECIAL EQUALITY CHECK, CANNOT CHECK ACTION
//					{
						if(newPath.remainingDirtyCells.isEmpty() )
						{
							finalNode = newPath;
							
						}
						else
						{
							openStates.add(newPath);
						}
//					}
				}
			}
		}
		
		LinkedList<Path> finalPathList = new LinkedList<Path>();
		Path finalPath = finalNode;
		while (finalPath != null)
		{
			finalPathList.addFirst(finalPath);
			finalPath = finalPath.parent;
		}
		
		// return null if no solution was found
		return finalPathList;
	}
}
