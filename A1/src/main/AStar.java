package main;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import main.Robot.Action;

public class AStar extends Algorithm
{
	/**
	 * List of open Nodes.
	 */
	// protected PriorityQueue<Path> frontier;
	protected Path firstNode;
	protected List<Position> dirt;
	
	public AStar(Grid grid)
	{
		this.grid = grid;
		this.dirt = grid.getDirt();
		//this.frontier = new PriorityQueue<Path>();
		this.closedStates = new LinkedList<Path>();
	}
	
	/**
	 * Calculates an estimate of the cost to go from one position to another.
	 * 
	 * @param source The node to start from
	 * @param destination The node we want to estimate the cost to get to.
	 * @return The manhattanDistance(n) cost of such a traversal.
	 */
	private int manhattanDistance(Position p1, Position p2)
	{
		return Math.abs(p1.row - p2.row) + Math.abs(p1.column - p2.column);
	}
	
	private int heuristic(List<Position> goalPositions, Path nextPath)
	{
		// Position start = startPath.roboClone.getPosition();
		// Position goal = goalPath.roboClone.getPosition();
		//
		Integer cost = Integer.MAX_VALUE;
		
		// Get estimated number of "steps" to get from nextPath to all goalPositions
		ArrayList<Integer> costs = new ArrayList<Integer>();
		
		
		//BE SURE TO AVOID ALREADY CLEANED GOAL NODES IN THE HEURISTIC
		for(Position i : goalPositions){
			if(nextPath.getCellsAlreadyCleaned().contains(i)){
				continue;
			}
			costs.add(manhattanDistance(nextPath.roboClone.getPosition(), i));
		}
		
		// Find the lowest cost among the results
		for (Integer i : costs)
		{
			if (i < cost)
				cost = i;
		}
		
		// Convert the resulting steps into a "plausible" cost estimation and return it
		return (cost * Action.cost(Action.MOVE)) + Action.cost(Action.SUCK);
	}
	
	@Override
	protected List<Path> computeSolution()
	{
		Robot firstRobot = this.grid.getRobot();
		Path finalNode = null;
		firstNode = new Path(firstRobot, 0);
		// LinkedHashMap<Path, Integer> realCost = new LinkedHashMap<Path, Integer>(); // g(n)
		LinkedHashMap<Path, Integer> totalCost = new LinkedHashMap<Path, Integer>(); // g(n) + h(n)
		List<Path> openSet = new LinkedList<Path>();
		List<Path> closedSet = new LinkedList<Path>();
		
		// realCost.put(firstNode, 0);
		totalCost.put(firstNode, heuristic(grid.getDirt(), firstNode));
		
		openSet.add(firstNode);
		
		while (!openSet.isEmpty() && finalNode == null)
		{
			Path current = null;
			int pathCost = Integer.MAX_VALUE;
			
			for (Path openPath : openSet)
			{
				// if exists, this is like having a path with is non-infinity cost
				if (totalCost.containsKey(openPath))
				{
					int tempCost = totalCost.get(openPath);
					if (tempCost < pathCost)
					{
						current = openPath;
						pathCost = tempCost;
					}
				}
				// else the path is of infinity cost
			}
			
			if (dirt.contains(current.roboClone.getPosition())
					&& current.action == Action.SUCK
					&& current.getCellsAlreadyCleaned().size() == dirt.size())
			{
				finalNode = current;
				break;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
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
						// new_cost = cost_so_far.get(current) +
						// Action.cost(action);
						tempBot.turn(action);
						next = new Path(current, tempBot, action,
								(current.cost + Action.cost(action)),
								current.getCellsAlreadyCleaned());
						
						// if (cost_so_far.containsKey(next) || new_cost <
						// cost_so_far.get(next))
						// {
						// cost_so_far.replace(next, new_cost);
						// // priority = new_cost + heuristic(goal, next)
						// int priority = new_cost + heuristic(next);
						// // frontier.put(next, priority)
						// // came_from[next] = current
						// }
						break;
						
					case MOVE:
						Position newPosition = tempBot.getCellInFrontOfRobot();
						Cell cellInFront;
						try
						{
							cellInFront = grid.getCell(newPosition);
							if (cellInFront.isObstructed())
							{
								continue;
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
							next = new Path(current, tempBot, action,
									current.cost + Action.cost(action),
									current.getCellsAlreadyCleaned());
							// nodesWhichSucked.add(next);
							next.addCleanedCell(cell);
						}
						break;
				}
				
				if (closedSet.contains(next))
				{
					continue;
				}
				
				int tentativeRealCost = current.cost + Action.cost(action);
				
				if (!openSet.contains(next))
				{
					openSet.add(next);
				}
				else if (tentativeRealCost >= next.cost)
				{
					continue;
				}
				
				next.cost = tentativeRealCost;
				totalCost.put(next, tentativeRealCost + heuristic(dirt, next));
			}
		}
		
		LinkedList<Path> solution = new LinkedList<Path>();
		while (finalNode != null)
		{
			solution.addFirst(finalNode);
			finalNode = finalNode.parent;
		}
		
		return solution;
	}
}
