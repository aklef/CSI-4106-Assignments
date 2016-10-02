package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.collections4.map.MultiKeyMap;

public class BFS implements Algorithm {

	private Grid grid;
	//private MultiKeyMap stateMap = new MultiKeyMap(); // posx, posy, robotorientation, 
	private Queue<Path> openStates;
	private List<Path> closedStates;
	
	public BFS(Grid grid)
	{
		this.grid = grid;
		openStates = new LinkedList<Path>();
		closedStates = new ArrayList<Path>();
	}

	@Override
	public Path computeSolution() {
		
		Robot robot = grid.getRobot();
		Position position = robot.getPosition();
		Cell startCell = grid.getCell(position);
		
		Path firstNode = new Path(null, robot, null, 0, grid.getInitialDirtyCellList());
		Path finalNode = null;
		
		openStates.add(firstNode);
		
		while(!openStates.isEmpty())
		{
			Path node = openStates.poll();
			
			closedStates.add(node);
			
			for(Robot.Action action : Robot.Action.values()){
				Robot tempRobot = new Robot(robot);
				Robot.Direction currentDirection = tempRobot.getOrientation();
				
				Path newPath = null;
				
				switch(action) {
				case TURNLEFT:
					Robot leftBot = new Robot(tempRobot);
					leftBot.turnLeft();
					newPath = new Path(node, leftBot, action, node.cost + action.turnLeft(), node.remainingDirtyCells);
					break;
					
				case TURNRIGHT:
					Robot rightBot = new Robot(tempRobot);
					rightBot.turnRight();
					newPath = new Path(node, rightBot, action, node.cost + action.turnRight(), node.remainingDirtyCells);
					break;
					
				case FORWARDS:
					
					
					break;
				case SUCK:
					break;
				}
				
				if(!openStates.contains(newPath) && !closedStates.contains(newPath)){
					if(newPath.remainingDirtyCells.isEmpty()){
						finalNode = newPath;
					} else {
						openStates.add(newPath);
					}
				}
			}
			
		}		
		
		//return null if no solution was found
		return finalNode;
	}
	
	public Path getNextNode(){
		
		return null;
	}
}
