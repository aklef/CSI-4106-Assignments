package main;

import java.util.List;
import java.util.Queue;

/**
 * This abstract class gives some structure that actual searches will implement.
 */
abstract class Algorithm
{
	protected Grid grid;
	protected Queue<Path> openStates;
	protected List<Path> closedStates;
	
	protected abstract List<Path> computeSolution();
}