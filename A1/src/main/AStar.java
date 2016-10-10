package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.Robot.Action;

/**
 * Our implementation of the A* Search.
 */
public class AStar extends Algorithm
{
	protected LinkedList<Path> openStates;
	protected List<Position> dirts;
	/** 
	 * This stores the values of g(n) + h(n) for a given Path.
	 */
	Map<Path, Integer> costs_so_far;
	
	public AStar(Grid grid)
	{
		this.grid = grid;
		this.dirts = grid.getDirt();
		this.openStates = new LinkedList<Path>();
		this.closedStates = new LinkedList<Path>();
		this.nodesWhichSucked = new ArrayList<Path>();
		this.costs_so_far = new LinkedHashMap<Path, Integer>();
	}
	
	/**
	 * Calculates an estimate of the cost to go from one point to another.
	 * 
	 * @param source The node to start from
	 * @param destination The node we want to estimate the cost to get to.
	 * @return The manhattanDistance(n) cost of such a traversal.
	 */
	private int manhattanDistance(Position p1, Position p2)
	{
		return Math.abs(p1.row - p2.row) + Math.abs(p1.column - p2.column);
	}
	
	/**
	 * Our heuristic function. Estimates the cost to a goal state from our next Path node.
	 * 
	 * @param goalPositions The list of goals to reach. MUST BE VALID/REACHABLE POSITIONS.
	 * @param nextPath The proposed positon to move to.
	 * @return a cost being the estimate of the cost to the goal.
	 */
	private int heuristic(List<Position> goalPositions, Path nextPath)
	{
		// Start with cost being infinite
		Integer cost = Integer.MAX_VALUE;
		
		// Get estimated number of "steps" to get from nextPath to all goalPositions
		ArrayList<Integer> costs = new ArrayList<Integer>();
		
		for(Position gP : goalPositions)
		{
			// AVOID ALREADY CLEANED DIRTS
			if(nextPath.getCellsAlreadyCleaned().contains(gP))
			{
				continue;
			}
			// If dirt hasn't been cleaned
			// add the distance from robot to dirt as cost
			costs.add(manhattanDistance(nextPath.roboClone.getPosition(), gP) * Action.cost(Action.MOVE));
		}
		
		for (Integer i : costs)
		{
			// Find and save the lowest cost among the results
			if (i < cost)
				cost = i;
		}
		
		// add the cost to suck dirt and return
		cost +=  Action.cost(Action.SUCK);
		return cost;
	}
	
	@Override
	protected void computeSuccessors(Path current)
	{
		Robot tempBot;
		Path next;
		
		for (Action action : Action.values())
		{
			tempBot = new Robot(current.roboClone);
			next = null;
			
			switch (action)
			{
				case LEFT:
				case RIGHT:
					tempBot.turn(action);
					next = new Path(current, tempBot, action,
							(current.cost + Action.cost(action)),
							current.getCellsAlreadyCleaned());
					break;
					
				case MOVE:
					Position newPosition = tempBot.getCellInFrontOfRobot();
					try
					{
						Cell cellInFront = this.grid.getCell(newPosition);
						if (cellInFront.isObstructed())
						{
							continue; // skip invalid move
						}
					}
					catch (OutOfBoundsException e)
					{
						continue;
					}
					
					tempBot.setPosition(newPosition);
					next = new Path(current, tempBot, action, 
							(current.cost + Action.cost(action)),
							current.getCellsAlreadyCleaned());
					break;
					
				case SUCK:
					Position cleanBotPosition = tempBot.getPosition();
					Cell cell = null;
					try
					{
						cell = this.grid.getCell(cleanBotPosition);
					}
					catch (OutOfBoundsException e)
					{
						continue;
					}
					
					if (!cell.isDirty() || current.getCellsAlreadyCleaned().contains(cell))
					{
						continue; // can't suck if there's no dirt
					}
					else
					{
						next = new Path(current, tempBot, action,
								(current.cost + Action.cost(action)),
								current.getCellsAlreadyCleaned());
						next.addCleanedCell(cell);
						nodesWhichSucked.add(next);
					}
					break;
			}
			
			// AT THIS POINT WE HAVE A VALID 'NEXT'
			
			// check if we've already explored this Node
			if (closedStates.contains(next))
			{
				continue; // skip it
			}
			// and if the node is explored already
			if (openStates.contains(next))
			{
				continue; // skip it
			}
			// otherwise
			else
			{
				openStates.add(next); // open it
			}
			
			costs_so_far.put(next, (next.cost + heuristic(this.dirts, next)));
		}
	}
	
	@Override
	protected List<Path> computeSolution()
	{
		// First node always has a cost of zero.
		Path firstNode = new Path(this.grid.getRobot(), 0);
		Path finalNode = null;
		
		this.costs_so_far.put(firstNode, heuristic(this.grid.getDirt(), firstNode));
		this.openStates.add(firstNode);
		
		while (!this.openStates.isEmpty() && finalNode == null)
		{
			Path current = null;
			
			int maxCost = Integer.MAX_VALUE;
			for (Path openPath : openStates)
			{
				// path has non-infinity cost
				if (costs_so_far.containsKey(openPath))
				{
					int cost = costs_so_far.get(openPath);
					if (cost < maxCost)
					{
						current = openPath;
						maxCost = cost;
					}
				}
				// else the path is of infinity cost
			}
			
			//  GOAL CONDITION
			if (this.dirts.contains(current.roboClone.getPosition())
					&& current.action == Action.SUCK
					&& current.getCellsAlreadyCleaned().size() == this.dirts.size())
			{
				finalNode = current;
				break; // Make sure to exit!
			}
			
			openStates.remove(current);
			closedStates.add(current);
			
			// SUCCESSOR FUNCTION
			this.computeSuccessors(current);
		}
		
		LinkedList<Path> solution = new LinkedList<Path>();
		
		// Handle the case where there is some unreachable dirt
		if(finalNode == null)
		{
			if (nodesWhichSucked.size() > 0)
			{
				Collections.sort(nodesWhichSucked);
				
				int highestCleanCount = nodesWhichSucked.get(0).getCellsAlreadyCleaned().size();
				
				List<Path> maxCleanedPaths = new ArrayList<Path>();
				
				for (Path sucker : nodesWhichSucked)
				{
					if (sucker.getCellsAlreadyCleaned().size() == highestCleanCount)
						maxCleanedPaths.add(sucker);
				}
				
				Collections.sort(maxCleanedPaths, Path.Comparators.COST);
				
				// Get the least expensive path
				finalNode = maxCleanedPaths.get(0);				
				
			}
			else
			{
				finalNode = firstNode;
			}
		}
		
		while (finalNode != null)
		{
			solution.addFirst(finalNode); // always insert first = reverse the order
			finalNode = finalNode.parent; 
		}
		
		return solution;
	}
}
