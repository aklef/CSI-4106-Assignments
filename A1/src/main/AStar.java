package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	protected List<Path> computeSolution()
	{
		// First node always has a cost of zero.
		this.firstNode = new Path(this.grid.getRobot(), 0);
		Path finalNode = null;
		
		/* 
		 * This stores the values of g(n) + h(n) for a given Path.
		 */
		Map<Path, Integer> costs_so_far = new LinkedHashMap<Path, Integer>();
		List<Path> openSet = new LinkedList<Path>();
		List<Path> closedSet = new LinkedList<Path>();
		
		//maintain list of paths which lead to cleaning. this way the "next best" solution is usable when there is a blocked dirt
		List<Path> nodesWhichSucked = new ArrayList<Path>();
		
		// realCost.put(firstNode, 0);
		costs_so_far.put(firstNode, heuristic(grid.getDirt(), firstNode));
		openSet.add(firstNode);
		
		while (!openSet.isEmpty() && finalNode == null)
		{
			Path current = null;
			int pathCost = Integer.MAX_VALUE;
			
			for (Path openPath : openSet)
			{
				// path has non-infinity cost
				if (costs_so_far.containsKey(openPath))
				{
					int cost = costs_so_far.get(openPath);
					if (cost < pathCost)
					{
						current = openPath;
						pathCost = cost;
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
						try
						{
							Cell cellInFront = grid.getCell(newPosition);
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
							nodesWhichSucked.add(next);
							next.addCleanedCell(cell);
						}
						break;
				}
				// check if we've already explored this Node
				if (closedSet.contains(next))
				{
					continue; // skip it
				}
				// Make a tentative total cost for this new Node
				int tentativeRealCost = current.cost + Action.cost(action);
				if (!openSet.contains(next))
				{
					openSet.add(next);
				}
				else
				{
					continue;
				}
				
				next.cost = tentativeRealCost;
				costs_so_far.put(next, next.cost + heuristic(dirt, next));
			}
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
