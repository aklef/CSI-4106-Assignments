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
	/**
	 * @return The computed list of Path nodes that result in the least
	 *         expensive traversal of all piles of dirt. Return an empty list of
	 *         Path nodes if no solution was found
	 */
	protected abstract List<Path> computeSolution();
}