import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.util.vector.Vector3f;

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
	public static final float ADJACENT_COST = 0.8F;
	public static final float ALIGNMENT_THRESHOLD = 0.5F;
	public static final float COHESION_MAGNITUDE = 0.05F;
	public static final float COHESION_STRENGTH = 0.5F;
	/**
	 * The distance, in game units, to get from a node to a neighbouring
	 * diagonal Node
	 */
	public static final float DIAGONAL_COST = 1.4F;
	public static final float MAX_RADIUS = 0F;

	/**
	 * The maximum max unit radius that a node can have
	 */
	public static final float MAX_RADIUS_COST = 5.0F;

	public static final float SEPERATION_MAGNITUDE = 0.05F;

	public static final float SEPERATION_STRENGTH = 0.05F;

	public static final float STATIONARY_THRESHOLD = 0.07F;

	/** The nodes in this map */
	Node[][] nodes;

	Pathfinder pathfinder;

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

		pathfinder = new Pathfinder(this);
	}

	/**
	 * Sets the position of a new unit on the map
	 * 
	 * @param u
	 */
	public void addUnit(Unit u) {
		nodes[(int) u.getY()][(int) u.getX()].addUnit(u);
	}

	private void checkUnitPos(float x, float y, ArrayList<Unit> units,
			float radius) {
		for (int i = 0; i < units.size(); i++) {
			double distBetween = units.get(i).getCollisionRadius() + radius;
			distBetween *= distBetween;

			if (Resource.magnitude2DSq(units.get(i).getCurrentPosition(),
					new Point2D.Float(x, y)) > distBetween) {
				units.remove(i);
				i--;
			}
		}
	}

	public ArrayList<Point2D.Float> findPath(float startX, float startY,
			float endX, float endY, float unitRadius) {
		return pathfinder.findPath(startX, startY, endX, endY, unitRadius);
	}

	/**
	 * Returns the height of the map
	 * 
	 * @return The height of the map
	 */
	public int getHeight() {
		return nodes.length;
	}

	public float getMaxUnitRadius(int x, int y) {
		return nodes[y][x].getMaxUnitRadius();
	}

	/**
	 * Returns the Node at a specific location of the map
	 * 
	 * @param x
	 *            The x-coordinate of the Node to return
	 * @param y
	 *            The y-coordinate of the Node to return
	 * @return The Node at (x, y)
	 */
	public Node getNode(int x, int y) {
		return nodes[y][x];
	}

	/**
	 * Returns the Nodes that make up this map.
	 * 
	 * @return The Nodes that make up this map.
	 */
	public Node[][] getNodes() {
		return nodes;
	}

	public Node[] getNodesInRadius(float x, float y, float radius) {
		Set<Node> nodes = getNodesInRadius(getNode((int) x, (int) y), radius);

		for (Node n : nodes.toArray(new Node[0])) {
			double actDist = Point2D.distanceSq(x, y, n.getX(), n.getY());
			actDist *= actDist;

			double theta = Math.atan(Math.abs(x - n.getXCoord())
					/ Math.abs(y - n.getZCoord()));

			double maxDist = (ADJACENT_COST / 2) / Math.cos(theta);
			maxDist += radius;
			maxDist *= maxDist;

			if (actDist > maxDist)
				nodes.remove(n);
		}

		return nodes.toArray(new Node[0]);
	}

	private Set<Node> getNodesInRadius(Node n, float radius) {
		Set<Node> nodes = new HashSet<Node>(0);
		nodes.add(n);

		if (radius > 0)
			for (Neighbour ne : n.getNeighbours())
				nodes.addAll(getNodesInRadius(ne.getNode(),
						radius - ne.getCost()));
		return nodes;
	}

	public ArrayList<Unit> getUnits() {
		ArrayList<Unit> units = new ArrayList<Unit>(0);

		for (int y = 0; y < nodes.length; y++)
			for (int x = 0; x < nodes[y].length; x++)
				for (Unit u : nodes[y][x].getUnits())
					units.add(u);

		return units;
	}

	/**
	 * Allows you to get all Units in a specified radius while explicitly
	 * stating the position and radius
	 * 
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @param radius
	 *            The breadth of the search
	 * @param bufferRadius
	 *            The units to search outside of the radius
	 * @return The Units within the specified radius
	 */
	public Unit[] getUnitsInRadius(float x, float y, float radius,
			float bufferRadius) {
		ArrayList<Unit> units = new ArrayList<Unit>(0);
		units.addAll(getUnitsInRadius(getNode((int) x, (int) y), radius
				+ bufferRadius));
		checkUnitPos(x, y, units, radius);

		return units.toArray(new Unit[0]);
	}

	private Set<Unit> getUnitsInRadius(Node n, float radius) {
		Set<Unit> units = new HashSet<Unit>(Arrays.asList(n.getUnits()));

		if (radius > 0)
			for (Neighbour ne : n.getNeighbours())
				units.addAll(getUnitsInRadius(ne.getNode(),
						radius - ne.getCost()));
		return units;
	}

	public Unit[] getUnitsInRadius(Unit u, float radius) {
		ArrayList<Unit> units = new ArrayList<Unit>(0);
		units.addAll(getUnitsInRadius(getNode((int) u.getX(), (int) u.getY()),
				radius + MAX_RADIUS));
		units.remove(u);
		checkUnitPos(u.getX(), u.getY(), units, radius);

		return units.toArray(new Unit[0]);
	}

	public boolean getValue(int x, int y) {
		if (getMaxUnitRadius(x, y) == 0)
			return false;
		return true;
	}

	/**
	 * Returns the width of the map
	 * 
	 * @return The width of the map
	 */
	public int getWidth() {
		if (getHeight() > 0)
			return nodes[0].length;
		return 0;
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
		float previousUnitRadius = n.getMaxUnitRadius();
		n.setMaxUnitRadius(MAX_RADIUS_COST);

		for (Neighbour neighbour : n.getNeighbours()) {
			if (neighbour.getNode().getMaxUnitRadius() >= previousUnitRadius
					+ neighbour.getCost())
				setUnblockedUnitRadius(neighbour.getNode());
			else if (previousUnitRadius == 0
					&& neighbour.getNode().getMaxUnitRadius() >= previousUnitRadius
							+ neighbour.getCost() / 2)
				setUnblockedUnitRadius(neighbour.getNode());
			else if (neighbour.getNode().getMaxUnitRadius() < previousUnitRadius
					+ neighbour.getCost()) {
				if (neighbour.getNode().getMaxUnitRadius() == 0)
					setUnitRadius(n, neighbour.getNode().getMaxUnitRadius()
							+ neighbour.getCost() / 2);
				else
					setUnitRadius(n, neighbour.getNode().getMaxUnitRadius()
							+ neighbour.getCost());
			}
		}
	}

	/**
	 * Updates the position of a Unit on the map
	 * 
	 * @param u
	 *            The unit to update the position of
	 * @param deltaX
	 *            The distance moved along the x-axis
	 * @param deltaY
	 *            The distance moved along the y-axis
	 */
	public void setUnitGridPosition(Unit u, float deltaX, float deltaY) {
		if ((int) (u.getX() - deltaX) == (int) u.getX()
				&& (int) (u.getY() - deltaY) == (int) u.getY())
			return;

		nodes[(int) u.getY()][(int) u.getX()].addUnit(u);
		nodes[(int) (u.getY() - deltaY)][(int) (u.getX() - deltaX)]
				.removeUnit(u);
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
	private void setUnitRadius(Node n, float radius) {
		if (n.getMaxUnitRadius() < radius)
			return;

		n.setMaxUnitRadius(radius);

		if (radius == 0) {
			for (Neighbour neighbour : n.getNeighbours())
				setUnitRadius(neighbour.getNode(), radius + neighbour.getCost()
						/ 2);
		} else {
			for (Neighbour neighbour : n.getNeighbours())
				setUnitRadius(neighbour.getNode(), radius + neighbour.getCost());
		}
	}

	public void setValue(int x, int y, boolean value) {
		if (value)
			setUnblocked(x, y);
		else
			setBlocked(x, y);
	}

	/**
	 * Updates the units on this map
	 */
	public void update(long delta) {
		ArrayList<Unit> units = new ArrayList<Unit>(0);

		for (int y = 0; y < nodes.length; y++)
			for (int x = 0; x < nodes[y].length; x++)
				for (Unit u : nodes[y][x].getUnits())
					units.add(u);

		for (Unit u : units)
			u.update(this, delta);

		for (Unit u : units)
			u.seperateFlock(this);
	}
}