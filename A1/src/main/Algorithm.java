package main;

import java.util.List;

public interface Algorithm {
		
	public List<State> nextSuccessors(State currentState);
	
	public State getFinalState();
	
	public State getInitialState();
}