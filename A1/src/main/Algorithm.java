package main;

import java.util.List;

/**
 * This abstract class gives some structure that actual searches will implement.
 */
abstract class Algorithm
{
	protected Grid grid;
	/**
	 * List of Paths which have been explored.
	 */
	protected List<Path> closedStates;
	/**
	 * List of paths which lead to cleaning, this way the "next best" solution is usable when there is a blocked dirts
	 */
	protected List<Path> nodesWhichSucked;
	
	/**
	 * @return The computed list of Path nodes that result in the least
	 *         expensive traversal of all piles of dirt. Return an empty list of
	 *         Path nodes if no solution was found
	 */
	protected abstract List<Path> computeSolution();
	
	/**
	 * @return The computed succesor Path nodes that is in the least
	 *         expensive traversal of all piles of dirt. Return an empty list of
	 *         Path nodes if no solution was found
	 */
	protected abstract void computeSuccessors(Path current);
}