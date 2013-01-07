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
	
	/**
	 * The sight radius of the Unit, in game units */
	private int sightRadius;

	/** The list of coordinates for the Unit to follow */
	private ArrayList<Node> path;

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
	public Unit(double x, double y, int speed, int collisionRadius, int sightRadius) {
		this.position = new Point2D.Double(x, y);
		path = new ArrayList<Node>(0);
		this.speed = speed;
		this.collisionRadius = collisionRadius;
		this.sightRadius = sightRadius;
		this.theta = 0;
	}

	/**
	 * Adds the destination waypoints to this Unit's path
	 * 
	 * @param path
	 *            The destination waypoints for the Unit to follow
	 */
	public void addDestination(ArrayList<Node> path) {
		this.path.addAll(path);
	}

	/**
	 * Draws the unit on the screen
	 */
	public void draw() {
	}

	/**
	 * Returns the unit's angle
	 * 
	 * @return The unit's angle
	 */
	public double getTheta() {
		return theta;
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
	
	public ArrayList<Node> getPath() { 
		return path;
	}
	
	public int getSightRadius() {
		return sightRadius;
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
			return new Point2D.Double(path.get(0).getX(), path.get(0).getY());
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
	
	public double getXDest() {
		if(path.size() > 0)
			return path.get(0).getX();
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
	
	public double getYDest() {
		if(path.size() > 0)
			return path.get(0).getY();
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
	public void setDestination(ArrayList<Node> path) {
		this.path = path;
	}

	/**
	 * Sets this Units target position to the next coordinate on the path, if
	 * one exists
	 */
	public void nextPosition() {
		path.remove(0);
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
