public class Robot {
	//robot variables
	public Position robotPosition;
	public Direction robotOrientation;
	
	public Robot(Position position, Direction orientation){
		
	}
	
	public Direction getRobotOrientation() {
		return robotOrientation;
	}
	
	public enum Direction {
		NORTH, WEST, SOUTH, EAST
	};
	
	public void robotTurnLeft(){
		switch(robotOrientation){
		case NORTH:
			robotOrientation = Direction.WEST;
			break;
		case WEST:
			robotOrientation = Direction.SOUTH;
			break;
		case SOUTH:
			robotOrientation = Direction.EAST;
			break;
		case EAST:
			robotOrientation = Direction.NORTH;
			break;
		}
	}
	
	public void robotTurnRight(){
		switch(robotOrientation){
		case NORTH:
			robotOrientation = Direction.EAST;
			break;
		case WEST:
			robotOrientation = Direction.NORTH;
			break;
		case SOUTH:
			robotOrientation = Direction.WEST;
			break;
		case EAST:
			robotOrientation = Direction.SOUTH;
			break;
		}
	}
	
	public Boolean robotMove(){
		
		//TODO: Check if moving out of bounds - return false
		
		//TODO: Check if there are obstacles - return false
		
		//TODO: Move - return true.
		
		return false;
	}
	
	public Boolean robotClean(){
		//TODO: Clean at the current position - return true if it was dirty before, return false if it was already clean
		return false;
	}
}
