package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import main.Robot.Action;
import main.Robot.Direction;
/**
 * Our implementation of the Breadth-First Search.
 */
@SuppressWarnings("unused")
public class BFS extends Algorithm
{
	//private MultiKeyMap stateMap = new MultiKeyMap(); // posx, posy, robotorientation,
	
	public BFS(Grid grid)
	{
		this.grid = grid;
		this.openStates = new LinkedList<Path>();
		this.closedStates = new ArrayList<Path>();
	}

	@Override
	protected List<Path> computeSolution()
	{
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
			
			for(Action action : Action.values()){
				Robot tempRobot = new Robot(robot);
				Direction currentDirection = tempRobot.getOrientation();
				
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
					Robot forwardBot = new Robot(tempRobot);
					Position newPosition = forwardBot.getCellInFrontOfRobot();
					Cell cellInFront;
					
					try{
						cellInFront = grid.getCell(newPosition);
					} catch (RuntimeException e) {
						continue;
					}
					
					if(cellInFront.isObstructed()) {
						continue;
					}
					
					newPath = new Path(node, forwardBot, action, node.cost + action.forwards(), node.remainingDirtyCells);
					break;
					
				case SUCK:
					Robot cleanBot = new Robot(tempRobot);
					//WE ARE NOT ACTUALLY IMPACTING THE GRID DURING A SEARCH // leftBot.robotClean();
					
					
					if(!node.remainingDirtyCells.contains(cleanBot.getPosition())) {
						continue;
					}
										
					LinkedList<Position> newDirtyList = new LinkedList<Position>(node.remainingDirtyCells);
					
					Position cleanBotPosition = cleanBot.getPosition();
					if(!newDirtyList.remove(cleanBotPosition)){
						throw new RuntimeException("Tried to create a path in BFS.computeSolution() which had one less dirty tiles, but the tile to be removed was not in the dirty list");
					}
					
					newPath = new Path(node, cleanBot, action, node.cost + action.suck(), newDirtyList);
					break;
				}
				
				if(!openStates.contains(newPath) || !closedStates.contains(newPath)){
					if(newPath.remainingDirtyCells.isEmpty()){
						finalNode = newPath;
					} else {
						openStates.add(newPath);
					}
				}
			}
			
		}		
		
		LinkedList<Path> finalPathList = new LinkedList<Path>();
		Path finalPath = finalNode;
		while(finalPath != null) {
			finalPathList.addFirst(finalPath);
			finalPath = finalPath.parent;
		}
		
		
		//return null if no solution was found
		return finalPathList;
	}
}
