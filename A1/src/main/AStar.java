package main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;

import main.Robot.Action;

public class AStar extends Algorithm
{
	/**
	 * List of open Nodes.
	 */
	protected PriorityQueue<Path> frontier;
	protected Path firstNode;
	protected List<Position> dirt;
	
	public AStar(Grid grid, List<Position> dirt)
	{
		this.grid = grid;
		this.dirt = dirt;
		this.frontier = new PriorityQueue<Path>();
		this.closedStates = new LinkedList<Path>();
	}
	/**
	 * Calculates an estimate of the cost to go from one position to another.
	 * @param source The node to start from
	 * @param destination The node we want to estimat ethe cost to get to.
	 * @return The h(n) cost of such a traversal.
	 */
	private int h(Position current, Position goal)
	{
		int manhattan = Math.abs(current.row-goal.row) + Math.abs(current.column-goal.column);
		return manhattan;
	}
	
	private int heuristic(Path current)
	{
//		Position start = startPath.roboClone.getPosition();
//		Position goal = goalPath.roboClone.getPosition();
//		
//		int cost = h(start, goal);
//		
//		return cost;
		return 0;
	}
	
	private int heuristic(Path goalPath, Path startPath)
	{
		Position start = startPath.roboClone.getPosition();
		Position goal = goalPath.roboClone.getPosition();
		
		int cost = h(start, goal);
		
		return cost;
	}
	
	private int heuristic(List<Position> goalPositions, Path nextPath)
	{
//		Position start = startPath.roboClone.getPosition();
//		Position goal = goalPath.roboClone.getPosition();
//		
		Integer cost = Integer.MAX_VALUE;
		
		//Get estimated number of "steps" to get from nextPath to all goalPositions		
		ArrayList<Integer> costs = new ArrayList<Integer>();
		
		for(Position i : goalPositions){
			costs.add(h(nextPath.roboClone.getPosition(), i));
		}
		
		//Find the lowest cost among the results
		for(Integer i : costs) {
			if(i < cost) {
				cost = i;
			}
		}
		
		//Convert the resulting steps into a "plausible" set of actions (n moves + 1 suck) and return it
		return (cost * Action.cost(Action.MOVE)) + Action.cost(Action.SUCK);
	}
	
	@Override
	protected List<Path> computeSolution()
	{
		LinkedList<Path> solution = new LinkedList<Path>();
		
		Robot firstRobot = this.grid.getRobot();
//		Path finalNode = null;
		firstNode = new Path(firstRobot, 0);
		Map<Path, Integer> cost_so_far = new LinkedHashMap<Path, Integer>();
		
		this.frontier.add(firstNode);
		
		while (!this.frontier.isEmpty())
		{
			Path current = this.frontier.poll();
			this.closedStates.add(current);
			Robot tempBot;
			Path next;
			
			for (Action action : Action.values())
			{
				tempBot = new Robot(current.roboClone);
				next = null;
				int  new_cost;
				
				switch (action)
				{
					case LEFT: case RIGHT:
						new_cost = cost_so_far.get(current) + Action.cost(action);
						tempBot.turn(action);
						next = new Path(current, tempBot, action, new_cost, current.getCellsAlreadyCleaned());
						if (cost_so_far.containsKey(next) || new_cost < cost_so_far.get(next))
						{
							cost_so_far.replace(next, new_cost);
//					    	priority = new_cost + heuristic(goal, next)
							int priority = new_cost + heuristic(next);
//					    	frontier.put(next, priority)
//					    	came_from[next] = current
						}
						break;
					case MOVE:
						break;
					case SUCK:
						break;
				}
			}
			
			if (!closedStates.contains(current) && frontier.contains(current))
			{
				frontier.add(current);
			}
		}
		return solution;
	}
}
