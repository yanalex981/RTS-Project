/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description:
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

public class Unit extends ControllableObject
{
	/** State variables */
	private final int MOVE = 1, ATTACK = 2, ATTACKMOVE = 3, FOLLOW = 4;

	/** The time in millisconds until the next attack */
	private int attackCooldown;

	/** The radius that this units can attack other units within */
	private int attackRadius;

	/** The delay between attacks, in milliseconds */
	private int attackSpeed;

	/** The collision radius of the Unit, in game units */
	private float collisionRadius;

	/** The damage that this unit does each attack */
	private int damage;

	/** The list of coordinates for the Unit to follow */
	private ArrayList<Point2D.Float> path;

	/** The speed of the Unit, in game units/millisecond */
	private float speed;

	/** The ControllableObject that this unit is currently targeting */
	private ControllableObject targetUnit;

	public Unit(Vector3f position, float speed, float collisionRadius, int sightRadius, Model model, Animation setUpAnimation, String objName, Player owner, boolean active)
	{
		super(position, model, setUpAnimation, objName, owner, active);

		path = new ArrayList<Point2D.Float>(0);
		this.speed = speed;
		this.collisionRadius = collisionRadius;
		this.sightRadius = sightRadius;
		this.rotation = new Vector3f(0, 0, 0);
		this.owner = owner;
		this.path = new ArrayList<Point2D.Float>();

		buildTime = 1000;
	}

	/**
	 * Adds the destination waypoints to this Unit's path
	 * 
	 * @param path The destination waypoints for the Unit to follow
	 */
	public void addPath(ArrayList<Point2D.Float> path)
	{
		this.path.addAll(path);
		state = ATTACK;
	}

	public boolean checkGridCollision(Map map, float newX, float newY)
	{
		for (Node n : map.getNodesInRadius(newX, newY, collisionRadius))
		{
			if (!n.isWalkable())
				return false;
		}

		return true;
	}

	/**
	 * Checks a position and radius and sees if it collides with a position on the Map
	 * 
	 * @param position The position on the map to check
	 * @param unitRadius The radius to check for
	 * @return boolean Whether or not the position given is empty
	 */
	public boolean checkUnitCollision(Map map, float newX, float newY)
	{
		if (map.getUnitsInRadius(newX, newY, collisionRadius, Map.MAX_RADIUS).length > 1)
			return false;
		return true;
	}

	/**
	 * Returns the collision radius of the unit in game units
	 * 
	 * @return The collision radius of the unit
	 */
	public float getCollisionRadius()
	{
		return collisionRadius;
	}

	/**
	 * Returns the current position of the unit as a Point2D.Double
	 * 
	 * @return The current position of the unit
	 */
	public Vector3f getCurrentPosition()
	{
		return position;
	}

	public ArrayList<Point2D.Float> getPath()
	{
		return path;
	}

	public int getSightRadius()
	{
		return sightRadius;
	}

	/**
	 * Returns the speed of the Unit, in game units / tick
	 * 
	 * @return The speed of the Unit
	 */
	public float getSpeed()
	{
		return speed;
	}

	/**
	 * Returns the target position of the Unit
	 * 
	 * @return The Point2D.Double that the Unit is traveling towards
	 */
	public Vector3f getTargetPosition()
	{
		if (path.size() > 0)
			return new Vector3f(path.get(0).x, 0, path.get(0).y);
		return null;
	}

	/**
	 * Returns the unit's angle
	 * 
	 * @return The unit's angle
	 */
	public double getTheta()
	{
		return rotation.y;
	}

	/**
	 * Returns the x-coordinate of the Unit
	 * 
	 * @return The x-coordinate of the Unit
	 */
	public float getX()
	{
		return position.x;
	}

	/**
	 * Returns the y-coordinate of the Unit
	 * 
	 * @return The y-coordinate of the Unit
	 */
	public float getZ()
	{
		return position.z;
	}

	public void killUnit(Map map)
	{
		map.nodes[(int) position.z][(int) position.x].removeUnit(this);
		owner.removeUnit(this);
	}

	/**
	 * Moves this Unit by a specified x and y amount
	 * 
	 * @param x The movement of the Unit on the x-axis
	 * @param y The movement of the Unit on the y-axis
	 */
	public void move(Map map, long delta, Vector3f targetPosition)
	{
		float travelDistance = getSpeed() * delta;
		rotation.y = (float) Math.atan2(targetPosition.z - getZ(), targetPosition.x - getX());

		if (Resource.magnitude2DSq(targetPosition, position) < (travelDistance * travelDistance))
		{
			setPosition(map, targetPosition.x - getX(), targetPosition.z - getZ());
			nextPosition();
		}
		else
		{
			float deltaX = (float) Math.cos(getTheta()) * travelDistance;
			float deltaY = (float) Math.sin(getTheta()) * travelDistance;
			setPosition(map, deltaX, deltaY);

			if (Resource.magnitude2DSq(position, targetPosition) == 0)
				nextPosition();
		}
	}

	/**
	 * Sets this Units target position to the next coordinate on the path, if one exists
	 */
	public void nextPosition()
	{
		path.remove(0);
	}

	public void seekUnit(Map map)
	{
		Unit[] unitsInRadius = map.getUnitsInRadius(this, attackRadius);

		for (int i = 0; i < unitsInRadius.length; i++)
		{
			if (unitsInRadius[i].owner != this.owner)
			{
				setTargetUnit(unitsInRadius[i]);
				return;
			}
		}
	}

	public void seperateFlock(Map map)
	{
		float averageSeperateX = 0;
		float averageSeperateY = 0;

		Unit[] units = map.getUnitsInRadius(this, collisionRadius + Map.SEPERATION_STRENGTH);

		if (units.length == 0)
			return;

		for (Unit unit : units)
		{
			averageSeperateX -= unit.getX() - getX();
			averageSeperateY -= unit.getZ() - getZ();
		}

		averageSeperateX /= units.length;
		averageSeperateY /= units.length;

		float theta = ((float) Math.atan2(averageSeperateY, averageSeperateX));
		float deltaX = (float) Math.cos(theta) * Map.SEPERATION_MAGNITUDE;
		float deltaY = (float) Math.sin(theta) * Map.SEPERATION_MAGNITUDE;
		setPosition(map, deltaX, deltaY);
	}

	/**
	 * Sets the destination waypoints as this Unit's path
	 * 
	 * @param path The destination waypoints for the Unit to follow
	 */
	public void setPath(ArrayList<Point2D.Float> path)
	{
		this.path = path;
		state = MOVE;
	}

	public void setPosition(Map map, float deltaX, float deltaY)
	{
		if (checkUnitCollision(map, getX() + deltaX, getZ() + deltaY) && checkGridCollision(map, getX() + deltaX, getZ() + deltaY))
		{
			position.x += deltaX;
			position.z += deltaY;
			map.setUnitGridPosition(this, deltaX, deltaY);
		}
	}

	public void setTargetUnit(Unit targetUnit)
	{
		this.targetUnit = targetUnit;
		state = ATTACK;
	}

	public void update(Map map, long delta)
	{
		if (hp == 0)
			killUnit(map);

		switch (state)
		{
		case WAITING:
			seekUnit(map);
			break;
		case MOVE:
			if (getTargetPosition() != null)
				move(map, delta, getTargetPosition());

			if (getTargetPosition() == null)
				state = WAITING;
			break;
		case ATTACK:
			if (Resource.magnitude2DSq(targetUnit.position, position) > attackRadius * attackRadius)
			{
				move(map, delta, targetUnit.position);
			}
			else
			{
				if (attackCooldown <= 0)
				{
					targetUnit.takeDamage(damage);
					attackCooldown = attackSpeed;
				}
				else
				{
					attackCooldown -= delta;
				}
			}

			if (targetUnit.hp <= 0)
			{
				state = WAITING;
				targetUnit = null;
			}

			break;
		}
	}
}