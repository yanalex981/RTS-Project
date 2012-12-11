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
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++)
				nodes[y][x] = new Node(x, y);
		}
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++)
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
}
