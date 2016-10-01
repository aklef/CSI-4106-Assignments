import java.util.List;

public class State {
	public State parent;
	public Robot robotClone;

	public State(State parentState, Robot robot, List<Cell> closed, List<Cell> visited) {

		parent = parentState;
		robotClone = new Robot(robot);


	}
}
