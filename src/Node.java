* This class is one Node contained in the game map. Each Node is one grid square
 * (10 game units by 10 game units), and contains information used for pathfinding.
 * 
 * @author Bobby Meagher
 * @since December 10th, 2012
 */

import java.util.ArrayList;

public class Node {
	/** The cost to travel from this Node to the beginning Node */
	private int g;

	/** The estimated cost from this Node to the goal Node */
	private int h;

	/** The maximum unit radius that can travel on this Node, in game units */
	private int maxUnitRadius;

	/** The Nodes that border this Node on the map grid */
	private Neighbour[] neighbours;

	/** The Node that comes before this Node on the current pathfinding cycle */
	private Node parent;

	/**
	 * The list of units on this Node
	 */
	private ArrayList<Unit> unitsOnGrid;

	/** The x-coordinate of this Node on the map grid */
	private int x;

	/** The y-coordinate of this Node on the map grid */
	private int y;

	/**
	 * Initializes the Node
	 * 
	 * @param x
	 *            The x-coordinate of the Node
	 * @param y
	 *            The y-coordinate of the Node
	 */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		maxUnitRadius = Map.MAX_RADIUS_COST;
		unitsOnGrid = new ArrayList<Unit>(0);
	}

	/**
	 * Adds a unit to this Node
	 * 
	 * @param u
	 *            The unit to add to this Node
	 */
	public void addUnit(Unit u) {
		unitsOnGrid.add(u);
	}

	/**
	 * Returns the total estimated cost of the path through this Node
	 * 
	 * @return The total estimated cost of the path through this Node
	 */
	public int getF() {
		return g + h;
	}

	/**
	 * Returns the distance from the beginning Node to this Node in the current
	 * pathfinding cycle
	 * 
	 * @return The distance from the beginning Node to this Node
	 */
	public int getG() {
		return g;
	}

	/**
	 * Returns the squared estimated distance from the end Node to this Node in
	 * the current pathfinding cycle
	 * 
	 * @return The distance from the end Node to this Node
	 */
	public int getH() {
		return h;
	}

	/**
	 * Returns the maximum radius of a unit that can travel on this Node
	 * 
	 * @return The maximum radius of a unit that can travel on this Node
	 */
	public int getMaxUnitRadius() {
		return maxUnitRadius;
	}

	/**
	 * Returns the Nodes bordering this Node
	 * 
	 * @return The neighbouring nodes of this Node
	 */
	public Neighbour[] getNeighbours() {
		return neighbours;
	}

	/**
	 * Returns the Node that is the parent of this Node
	 * 
	 * @return The parent of the Node
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Returns the units inside this Node
	 * 
	 * @return The units inside this Node
	 */
	public Unit[] getUnits() {
		return unitsOnGrid.toArray(new Unit[0]);
	}

	/**
	 * Returns the x-coordinate of this Node
	 * 
	 * @return The x-coordinate of the Node
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y-coordinate of this Node
	 * 
	 * @return The y-coordinate of the Node
	 */
	public int getY() {
		return y;
	}

	/**
	 * Removes a Unit from this Node
	 * 
	 * @param u
	 *            The unit to remove from this Node
	 */
	public void removeUnit(Unit u) {
		unitsOnGrid.remove(u);
	}
	
	public void setG(int g) {
		this.g = g;
	}

	/**
	 * Sets this Node up to be used for pathfinding by setting its h, g and
	 * parent values
	 * 
	 * @param endX
	 *            The x-coordinate of the goal Node
	 * @param endY
	 *            The y-coordinate of the goal Node
	 * @param parent
	 *            The parent to this Node
	 * @param cost
	 *            The cost to get from the parent Node to this Node
	 */
	public void setHeuristic(int endX, int endY, Node parent, int cost) {
		int deltaX = Math.abs(endX - x);
		int deltaY = Math.abs(endY - y);
		this.h = (int) (Math.sqrt(deltaX * deltaX + deltaY * deltaY) * 10);
		/*if(deltaY > deltaX)
			this.h = deltaX * Map.DIAGONAL_COST + (deltaY - deltaX) * Map.ADJACENT_COST;
		else
			this.h = deltaY * Map.DIAGONAL_COST + (deltaX - deltaY) * Map.ADJACENT_COST;*/
		
		this.parent = parent;
		
		if(parent != null) {
			int newG = parent.getG() + cost;

			if(newG > g)
				g = newG;
		}
	}

	/**
	 * Sets the maximum radius of units that can travel across this Node
	 * 
	 * @param maxUnitRadius
	 *            The maximum radius of units that can travel across this Node
	 */
	public void setMaxUnitRadius(int maxUnitRadius) {
		this.maxUnitRadius = maxUnitRadius;
	}

	/**
	 * Sets the neighbours to this Node given an array of Nodes
	 * 
	 * @param nodes
	 *            The nodes to get neighbours from
	 */
	public void setNeighbours(Node[][] nodes) {
		ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>(0);

		if (x > 0) {
			neighbours.add(new Neighbour(nodes[y][x - 1], Map.ADJACENT_COST));

			if (y > 0)
				neighbours.add(new Neighbour(nodes[y - 1][x - 1],
						Map.DIAGONAL_COST));
			if (y + 1 < nodes.length)
				neighbours.add(new Neighbour(nodes[y + 1][x - 1],
						Map.DIAGONAL_COST));
		}

		if (x + 1 < nodes[0].length) {
			neighbours.add(new Neighbour(nodes[y][x + 1], Map.ADJACENT_COST));

			if (y > 0)
				neighbours.add(new Neighbour(nodes[y - 1][x + 1],
						Map.DIAGONAL_COST));
			if (y + 1 < nodes.length)
				neighbours.add(new Neighbour(nodes[y + 1][x + 1],
						Map.DIAGONAL_COST));
		}

		if (y > 0)
			neighbours.add(new Neighbour(nodes[y - 1][x], Map.ADJACENT_COST));

		if (y + 1 < nodes.length)
			neighbours.add(new Neighbour(nodes[y + 1][x], Map.ADJACENT_COST));

		this.neighbours = neighbours.toArray(new Neighbour[0]);
	}

	/**
	 * Sets the parent of this Node
	 * 
	 * @param parent
	 *            The parent of the Node
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}
}
