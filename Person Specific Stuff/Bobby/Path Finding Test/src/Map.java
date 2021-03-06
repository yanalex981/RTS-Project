/**
 * This class contains the data for one map in game, including the data used for
 * pathfinding and AI.
 * 
 * @author Bobby Meagher
 * @since December 10th, 2012
 */

public class Map {	
	/**
	 * The distance, in game units, to get from a node to a neighbouring
	 * adjacent Node
	 */
	public static final int ADJACENT_COST = 10;

	/**
	 * The distance, in game units, to get from a node to a neighbouring
	 * diagonal Node
	 */
	public static final int DIAGONAL_COST = 14;

	/**
	 * The maximum max unit radius that a node can have
	 */
	public static final int MAX_RADIUS_COST = 50;

	/** The nodes in this map */
	private Node[][] nodes;

	/**
	 * Instantiates a new Map.
	 * 
	 * @param width
	 *            The width of the map
	 * @param height
	 *            The height of the map
	 */
	public Map(int width, int height) {
		nodes = new Node[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				nodes[y][x] = new Node(x, y);
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				nodes[y][x].setNeighbours(nodes);
		}
	}

	/**
	 * Returns the Nodes that make up this map.
	 * 
	 * @return The Nodes that make up this map.
	 */
	public Node[][] getNodes() {
		return nodes;
	}
	
	public Node getNode(int x, int y) {
		return nodes[y][x];
	}

	/**
	 * Sets a node on this Map as unwalkable
	 * 
	 * @param x
	 *            The x-coordinate to block
	 * @param y
	 *            The y-coordinate to block
	 */
	public void setBlocked(int x, int y) {
		if (nodes[y][x].getMaxUnitRadius() == 0)
			return;

		setUnitRadius(nodes[y][x], 0);
	}

	/**
	 * Sets a node on this Map as walkable
	 * 
	 * @param x
	 *            The x-coordinate to block
	 * @param y
	 *            The y-coordinate to block
	 */
	public void setUnblocked(int x, int y) {
		if (nodes[y][x].getMaxUnitRadius() != 0)
			return;

		setUnblockedUnitRadius(nodes[y][x]);
	}

	/**
	 * Reverses the maxUnitRadius when unblocking a Node
	 * 
	 * @param n
	 *            The Node to reset the maxUnitRadius of
	 */
	private void setUnblockedUnitRadius(Node n) {
		int previousUnitRadius = n.getMaxUnitRadius();
		n.setMaxUnitRadius(MAX_RADIUS_COST);

		for (Neighbour neighbour : n.getNeighbours()) {
			if (neighbour.getNode().getMaxUnitRadius() == previousUnitRadius
					+ neighbour.getCost())
				setUnblockedUnitRadius(neighbour.getNode());
			else if (neighbour.getNode().getMaxUnitRadius() < previousUnitRadius
					+ neighbour.getCost())
				setUnitRadius(n, neighbour.getNode().getMaxUnitRadius()
						+ neighbour.getCost());
		}
	}

	/**
	 * Sets this Node to the radius specified, and updates the maxUnitRadius of
	 * all the Nodes neighbours
	 * 
	 * @param n
	 *            The node to set the radius of
	 * @param radius
	 *            The maxUnitRadius to set the Node to
	 */
	private void setUnitRadius(Node n, int radius) {
		if (n.getMaxUnitRadius() <= radius)
			return;

		n.setMaxUnitRadius(radius);

		for (Neighbour neighbour : n.getNeighbours())
			setUnitRadius(neighbour.getNode(), radius + neighbour.getCost());
	}
	
	public int getHeight() {
		return nodes.length;
	}
	
	public int getWidth() {
		return nodes[0].length;
	}
}
