/**
 * This class is used in pathfinding by the Node class. Each Node object
 * contains an array of Neighbours that contain both the specific neighbouring
 * Node and the distance between the two Nodes.
 * 
 * @author Bobby Meagher
 * @since December 10th, 2012
 */

public class Neighbour {
	/** The distance from the Node containing this class to the neighbour Node */
	private float cost;

	/** The neighbour Node */
	private Node neighbour;

	/**
	 * Instantiates a new Neighbour object.
	 * 
	 * @param n
	 *            The neighbour node.
	 * @param cost
	 *            The cost from the Node containing this object to the neighbour
	 *            Node.
	 */
	public Neighbour(Node n, float cost) {
		this.neighbour = n;
		this.cost = cost;
	}

	/**
	 * Returns the cost from the Node containing this class to the neighbour
	 * Node.
	 * 
	 * @return The cost from the Node containing this class to the neighbour
	 *         Node.
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * Returns the neighbour Node.
	 * 
	 * @return The neighbour Node.
	 */
	public Node getNode() {
		return neighbour;
	}

	/**
	 * Sets the heuristic of the neighbour Node
	 * 
	 * @param endNode
	 *            The detination Node in the path
	 * @param parent
	 *            The parent to set the neighbour Node to
	 */
	public void setHeuristic(Node endNode, Node parent) {
		neighbour.setHeuristic(endNode.getX(), endNode.getY(), parent, cost);
	}
}