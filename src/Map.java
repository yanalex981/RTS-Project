import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	
	private int collisionChecks;

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

	/**
	 * Checks a position and radius and sees if it collides with a position on
	 * the Map
	 * 
	 * @param position
	 *            The poisiton on the map to check
	 * @param unitRadius
	 *            The radius to check for
	 * @return boolean Whether or not the position given is empty
	 */
	public boolean checkCollision(Unit unit, double newX, double newY, int unitRadius) {
		collisionChecks++;
		
		for (Unit u : getUnitsInRadius(unit, unitRadius)) {
			double distBetween = (u.getCollisionRadius() + unitRadius) / 10;
			distBetween *= distBetween;

			if (u.getCurrentPosition().distanceSq(newX, newY) < distBetween)
				return false;
		}

		return true;
	}

	/**
	 * Returns the height of the map
	 * 
	 * @return The height of the map
	 */
	public int getHeight() {
		return nodes.length;
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

	public ArrayList<Unit> getUnits() {
		ArrayList<Unit> units = new ArrayList<Unit>(0);

		for (int y = 0; y < nodes.length; y++)
			for (int x = 0; x < nodes[y].length; x++)
				for (Unit u : nodes[y][x].getUnits())
					units.add(u);

		return units;
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

	public ArrayList<Node> findPath(Node startNode, Node endNode, int unitRadius) {
		return pathfinder.findPath(startNode, endNode, unitRadius);
	}

	public ArrayList<Node> findPath(int startX, int startY, int endX, int endY,
			int unitRadius) {
		return pathfinder.findPath(getNode(startX, startY),
				getNode(endX, endY), unitRadius);
	}

	public ArrayList<Node> findPath(Point start, Point end, int unitRadius) {
		return findPath(start.x, start.y, end.x, end.y, unitRadius);
	}

	/**
	 * Moves a unit based on its angle and movement speed
	 * 
	 * @param u
	 *            The Unit to move
	 */
	public void moveUnit(Unit u) {
		u.setTheta(Math.atan2(u.getYDest() - u.getY(), u.getXDest() - u.getX()));
		double deltaX = Math.cos(u.getTheta()) * u.getSpeed() / 10.0;
		double deltaY = Math.sin(u.getTheta()) * u.getSpeed() / 10.0;

		if (checkCollision(u, u.getX() + deltaX, u.getY() + deltaY,
				u.getCollisionRadius()))
			u.move(deltaX, deltaY);

		setUnitGridPosition(u, deltaX, deltaY);
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

	public void setValue(int x, int y, boolean value) {
		if (value)
			setUnblocked(x, y);
		else
			setBlocked(x, y);
	}

	public boolean getValue(int x, int y) {
		if (getMaxUnitRadius(x, y) == 0)
			return false;
		return true;
	}

	public int getMaxUnitRadius(int x, int y) {
		return nodes[y][x].getMaxUnitRadius();
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

	public Unit[] getUnitsInRadius(Unit u, int radius) {
		
		
		Set<Unit> units = getUnitsInRadius(getNode((int) u.getX(), (int) u.getY()), radius);
		units.remove(u);
		return units.toArray(new Unit[0]);
	}

	public Set<Unit> getUnitsInRadius(Node n, int radius) {
		Set<Unit> units = new HashSet<Unit>(Arrays.asList(n.getUnits()));

		if (radius > 0)
			for (Neighbour ne : n.getNeighbours())
				units.addAll(getUnitsInRadius(ne.getNode(),
						radius - ne.getCost()));
		return units;
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
	public void setUnitGridPosition(Unit u, double deltaX, double deltaY) {
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
	private void setUnitRadius(Node n, int radius) {
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

	/**
	 * Updates the units on this map
	 */
	public void update() {
		collisionChecks = 0;
		long time = System.nanoTime();
		for (int y = 0; y < nodes.length; y++)
			for (int x = 0; x < nodes[y].length; x++)
				for (Unit u : nodes[y][x].getUnits())
					moveUnit(u);
		double newTime = (System.nanoTime() - time) / 1000000000.0;
		System.out.println(collisionChecks / newTime);
	}
}
