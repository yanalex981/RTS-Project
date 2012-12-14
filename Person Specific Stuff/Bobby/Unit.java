/**
 * Represents one Unit in the game
 * 
 * @author Bobby Meagher
 * @since December 14th, 2012
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Unit {
	/** The collision radius of the Unit, in game units */
	private int collisionRadius;

	/** The list of coordinates for the Unit to follow */
	private ArrayList<Point2D.Double> path;

	/** The current position of the Unit */
	private Point2D.Double position;

	/** The speed of the Unit, in game units / tick */
	private int speed;

	/** The angle of the Unit, in degrees */
	private double theta;

	/**
	 * Initializes the Unit
	 * 
	 * @param x
	 *            The starting x-coordinate of the Unit
	 * @param y
	 *            The starting y-coordinate of the Unit
	 * @param speed
	 *            The speed of the Unit
	 * @param collisionRadius
	 *            The collision radius of the Unit
	 */
	public Unit(int x, int y, int speed, int collisionRadius) {
		this.position = new Point2D.Double(x, y);
		path = new ArrayList<Point2D.Double>(0);
		this.speed = speed;
		this.collisionRadius = collisionRadius;
		this.theta = 0;
	}

	/**
	 * Adds the destination waypoints to this Unit's path
	 * 
	 * @param path
	 *            The destination waypoints for the Unit to follow
	 */
	public void addDestination(ArrayList<Point2D.Double> path) {
		this.path.addAll(path);
	}

	/**
	 * Draws the unit on the screen
	 */
	public void draw() {
	}

	/**
	 * Returns the unit's angle in degrees
	 * 
	 * @return The unit's angle in degrees
	 */
	public double getAngleDegrees() {
		return theta;
	}

	/**
	 * Returns the unit's angle in radians
	 * 
	 * @return The unit's angle in radians
	 */
	public double getAngleRadians() {
		return Math.toRadians(theta);
	}

	/**
	 * Returns the collision radius of the unit in game units
	 * 
	 * @return The collision radius of the unit
	 */
	public int getCollisionRadius() {
		return collisionRadius;
	}

	/**
	 * Returns the current position of the unit as a Point2D.Double
	 * 
	 * @return The current position of the unit
	 */
	public Point2D.Double getCurrentPosition() {
		return position;
	}

	/**
	 * Returns the speed of the Unit, in game units / tick
	 * 
	 * @return The speed of the Unit
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Returns the target position of the Unit
	 * 
	 * @return The Point2D.Double that the Unit is traveling towards
	 */
	public Point2D.Double getTargetPosition() {
		if (path.size() > 0)
			return path.get(0);
		return null;
	}

	/**
	 * Returns the x-coordinate of the Unit
	 * 
	 * @return The x-coordinate of the Unit
	 */
	public double getX() {
		return position.x;
	}

	/**
	 * Returns the y-coordinate of the Unit
	 * 
	 * @return The y-coordinate of the Unit
	 */
	public double getY() {
		return position.y;
	}

	/**
	 * Moves this Unit by a specified x and y amount
	 * 
	 * @param x
	 *            The movement of the Unit on the x-axis
	 * @param y
	 *            The movement of the Unit on the y-axis
	 */
	public void move(double x, double y) {
		position.x += x;
		position.y += y;
	}

	/**
	 * Sets the destination waypoints as this Unit's path
	 * 
	 * @param path
	 *            The destination waypoints for the Unit to follow
	 */
	public void setDestination(ArrayList<Point2D.Double> path) {
		this.path = path;
	}

	/**
	 * Sets this Units target position to the next coordinate on the path, if
	 * one exists
	 */
	public void setTargetPosition() {
		path.remove(0);

		if (path.size() > 0)
			this.theta = -1
					* Math.atan((path.get(0).x - position.x)
							/ (path.get(0).y - position.y));
	}

	/**
	 * Sets the angle of the Unit
	 * 
	 * @param theta
	 *            The angle to set the Unit to, in degrees
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
}
