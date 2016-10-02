package main;

import java.util.HashSet;
import java.util.List;

/**
 * This abstract class gives some structure that actual searches will implement.
 */
abstract class Algorithm
{
	protected Grid grid;
	protected HashSet<Path> closedStates;
	
	protected abstract List<Path> computeSolution();
}