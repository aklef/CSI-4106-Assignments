package main;

public class Path {
	public Path parent;
	public Robot.Action action;
	public Robot robotClone;
	public Boolean closed;
	public Integer cost;
	

	public Path(Path parentState, Robot robot, Robot.Action action, Boolean closed, Integer cost) {
		this.parent = parentState;
		this.action = action;
		this.robotClone = new Robot(robot);
		this.closed = closed;
		this.cost = cost;
				
	}
}
