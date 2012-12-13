/**
 * This class finds the shortest path from one node on the map to another,
 * taking into account obstacles on the map and the unit's radius.
 * 
 * @author Bobby Meagher
 * @since December 10th, 2012
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class Pathfinder {

	/** The nodes to find the path on */
	private static Node[][] nodes;

	public static void main(String[] args) {
		final int MAPSIZE = 176;
		final int CYCLES = 1000;

		Pathfinder pathfinder = new Pathfinder(new Map(MAPSIZE, MAPSIZE));

		Node startNode = nodes[0][0];
		Node endNode = nodes[MAPSIZE - 1][MAPSIZE - 1];

		long time = System.nanoTime();
		for (int i = 0; i < CYCLES; i++)
			pathfinder.findPath(startNode, endNode, 0);
		System.out.println(CYCLES / ((System.nanoTime() - time) / 1000000000D));

		for (Node node : pathfinder.findPath(startNode, endNode, 0))
			System.out.println(node.getX() + " " + node.getY());
	}

	/**
	 * Initializes the pathfinding class
	 * 
	 * @param m
	 *            The map to pathfind on
	 */
	public Pathfinder(Map m) {
		nodes = m.getNodes();
	}

	/**
	 * Adds a Node into a sorted LinkedList, based on the node's f value.
	 * 
	 * @param array
	 * @param toAdd
	 */
	private void addSorted(LinkedList<Node> array, Node toAdd) {
		if (array.size() == 0) {
			array.addFirst(toAdd);
			return;
		}

		int cost = toAdd.getF();
		int count;
		for (count = 0; count < array.size(); count++)
			if (cost >= array.get(count).getF())
				break;
		array.add(count, toAdd);
	}

	/**
	 * Finds the shortest path from one Node on the map to another, taking into
	 * account unit radius. If no path is available, returns the path that gets
	 * the unit closest to the target destination. Uses the A* pathfinding
	 * algorithm
	 * 
	 * @param startNode
	 *            The beginning Node on the path
	 * @param endNode
	 *            The destination Node of the path
	 * @param unitRadius
	 *            The radius of the unit to pathfind for
	 * @return The ArrayList of waypoint Nodes to get from the startNode to the
	 *         endNode
	 */
	public ArrayList<Node> findPath(Node startNode, Node endNode, int unitRadius) {
		boolean[][] hasChecked = new boolean[nodes.length][nodes[0].length];

		// Used to find the closest path to the goal if no path exists
		Node closestNode = startNode;

		startNode.setParent(null);

		LinkedList<Node> open = new LinkedList<Node>();
		open.addLast(startNode);

		while (open.size() > 0) {
			Node temp = open.removeLast();

			if (temp == endNode) {
				ArrayList<Node> path = reconstructPath(temp);
				smoothPath(path, unitRadius)
				return path;
			}

			if (temp.getH() < closestNode.getH())
				closestNode = temp;

			for (Neighbour neighbour : temp.getNeighbours()) {
				if (hasChecked[neighbour.getNode().getY()][neighbour.getNode()
						.getX()])
					continue;

				hasChecked[neighbour.getNode().getY()][neighbour.getNode()
						.getX()] = true;

				if (neighbour.getNode().getMaxUnitRadius() >= unitRadius) {
					neighbour.setHeuristic(endNode, temp);
					addSorted(open, neighbour.getNode());
				}
			}
		}

		return reconstructPath(closestNode);
	}

	/**
	 * Reconstructs the path from an end Node to the beginning Node of the path
	 * 
	 * @param endNode
	 *            The final Node on the path
	 * @return The ArrayList of Nodes that make up the path from the beginning
	 *         Node to the goal
	 */
	private ArrayList<Node> reconstructPath(Node endNode) {
		ArrayList<Node> path = new ArrayList<Node>(0);

		while (endNode != null) {
			path.add(0, endNode);
			endNode = endNode.getParent();
		}

		return path;
	}
	
	/**
	 * Smooths a precalculated path based on unit radius
	 * 
	 * @param path The precalculated path of Nodes
	 * @param unitRadius The radius of the unit on the path
	 */
	private void smoothPath(ArrayList<Node> path, int unitRadius) {
		if(path.size() <= 2)
			return;
		
		for(int i = 2; i < path.size(); path++)
			if(raytrace(path.get(i - 2), path.get(i), unitRadius))
				path.remove(i - 1);
	}
	
	/**
	 * Raytraces between 2 Nodes on the map to check for visibility
	 * from http://playtechs.blogspot.ca/2007/03/raytracing-on-grid.html
	 * 
	 * @param n1 The Node to raytrace from
	 * @param n2 The Node to raytrace to
	 * @param unitRadius The radius of the unit to check for visibility
	 * @return Whether or not Node 2 is visible from Node 1
	 */
	private boolean raytrace(Node n1, Node n2, int unitRadius) {
		int dx = Math.abs(n2.getX() - n1.getX());
		int dy = Math.abs(n2.getY() - n1.getY());
		int x = n1.getX();
		int y = n1.getY();
		int n = 1 + dx + dy;
		int x_inc = (n2.getX() > n1.getX()) ? 1 : -1;
		int y_inc = (n2.getY() > n1.getY()) ? 1 : -1;
		int error = dx - dy;
		dx *= 2;
		dy *= 2;
		
		for (; n > 0; --n) {
			if(unitRadius > nodes[y][x].getMaxUnitRadius())
				return false;
			
			if (error > 0) {
				x += x_inc;
				error -= dy;
			}
			else {
				y += y_inc;
				error += dx;
			}
		}
		
		return true;
	}
}
