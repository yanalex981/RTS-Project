/**
 * This class finds the shortest path from one node on the map to another,
 * taking into account obstacles on the map and the unit's radius.
 * 
 * @author Bobby Meagher
 * @since December 10th, 2012
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Pathfinder {

	/** The nodes to find the path on */
	private static Node[][] nodes;

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
	private void addSorted(ArrayList<Node> array, Node toAdd) {
		if (array.size() == 0) {
			array.add(toAdd);
			return;
		}

		float cost = toAdd.getF();
		for (int count = 0; count < array.size(); count++) {
			if (cost < array.get(count).getF()) {
				array.add(count, toAdd);
				break;
			}
		}

		array.add(toAdd);
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
	public ArrayList<Point2D.Float> findPath(float x0, float y0, float x1,
			float y1, float unitRadius) {
		boolean[][] hasChecked = new boolean[nodes.length][nodes[0].length];

		// Used to find the closest path to the goal if no path exists
		Node startNode = nodes[(int) y0][(int) x0];
		Node endNode = nodes[(int) y1][(int) x1];
		Node closestNode = startNode;

		startNode.setHeuristic(endNode.getX(), endNode.getY(), null, 0);
		startNode.setG(0);

		ArrayList<Node> open = new ArrayList<Node>();
		open.add(startNode);

		while (open.size() > 0) {
			Node temp = open.remove(0);
			hasChecked[temp.getY()][temp.getX()] = true;

			if (temp == endNode) {
				ArrayList<Node> nodePath = reconstructPath(temp);
				ArrayList<Point2D.Float> path = new ArrayList<Point2D.Float>(0);
				smoothPath(nodePath, unitRadius);

				for (Node n : nodePath)
					path.add(new Point2D.Float(n.getXCoord(), n.getZCoord()));
				path.add(new Point2D.Float(x1, y1));

				if (path.size() > 2)
					if (raytrace(path.get(path.size() - 3).x,
							path.get(path.size() - 3).y, x1, y1, unitRadius))
						path.remove(path.size() - 2);
				path.remove(0);

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

		ArrayList<Node> nodePath = reconstructPath(closestNode);
		ArrayList<Point2D.Float> path = new ArrayList<Point2D.Float>(0);
		smoothPath(nodePath, unitRadius);

		for (Node n : nodePath)
			path.add(new Point2D.Float(n.getXCoord(), n.getZCoord()));
		path.remove(0);

		return path;
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
	 * @param path
	 *            The precalculated path of Nodes
	 * @param unitRadius
	 *            The radius of the unit on the path
	 */
	private void smoothPath(ArrayList<Node> path, double unitRadius) {
		if (path.size() <= 2)
			return;

		for (int i = 0; i < path.size() - 2; i++) {
			if (raytrace(path.get(i), path.get(i + 2), unitRadius)) {
				path.remove(i + 1);
				i--;
			}
		}
	}

	/**
	 * Raytraces between 2 Nodes on the map to check for visibility from
	 * http://playtechs.blogspot.ca/2007/03/raytracing-on-grid.html
	 * 
	 * @param n1
	 *            The Node to raytrace from
	 * @param n2
	 *            The Node to raytrace to
	 * @param unitRadius
	 *            The radius of the unit to check for visibility
	 * @return Whether or not Node 2 is visible from Node 1
	 */
	private boolean raytrace(double x0, double y0, double x1, double y1,
			float unitRadius) {
		double dx = Math.abs(x1 - x0);
		double dy = Math.abs(y1 - y0);

		int x = (int) x0;
		int y = (int) y0;

		int n = 1;
		int x_inc, y_inc;
		double error;

		if (dx == 0) {
			x_inc = 0;
			error = Double.POSITIVE_INFINITY;
		} else if (x1 > x0) {
			x_inc = 1;
			n += (int) x1 - x;
			error = ((int) x0 + 1 - x0) * dy;
		} else {
			x_inc = -1;
			n += x - (int) x1;
			error = (x0 - (int) x0) * dy;
		}

		if (dy == 0) {
			y_inc = 0;
			error = Double.NEGATIVE_INFINITY;
		} else if (y1 > y0) {
			y_inc = 1;
			n += (int) y1 - y;
			error -= ((int) y0 + 1 - y0) * dx;
		} else {
			y_inc = -1;
			n += y - (int) (y1);
			error -= (y0 - (int) y0) * dx;
		}

		for (; n > 0; --n) {
			if (unitRadius > nodes[y][x].getMaxUnitRadius())
				return false;

			if (error > 0) {
				y += y_inc;
				error -= dx;
			} else {
				x += x_inc;
				error += dy;
			}
		}

		return true;
	}

	private boolean raytrace(Node n1, Node n2, double unitRadius) {
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
			if (unitRadius > nodes[y][x].getMaxUnitRadius())
				return false;

			if (error > 0) {
				x += x_inc;
				error -= dy;
			} else {
				y += y_inc;
				error += dx;
			}
		}

		return true;
	}
}