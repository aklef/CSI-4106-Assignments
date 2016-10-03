package main;

import java.util.List;

/**
 * This abstract class gives some structure that actual searches will implement.
 */
abstract class Algorithm
{
	protected Grid grid;
	/**
	 * Represents a configuration (or set of configurations) of the world.
	 */
	protected List<Path> closedStates;
	
	protected abstract List<Path> computeSolution();
}