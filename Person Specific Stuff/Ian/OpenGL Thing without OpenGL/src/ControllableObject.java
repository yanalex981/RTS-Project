import java.awt.geom.Point2D;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ControllableObject
{
	public static final int WAITING = 0;

	public int ID;

	protected boolean active;

	protected int animationFrame;

	protected int buildTime;

	protected Vector3f colour = new Vector3f(1, 1, 1);

	/**
	 * max health, current health, damage negated each time this unit is attacked
	 */
	protected int maxHp, hp, armour;

	protected String objName;

	protected Player owner;

	/** The current position of the Unit */
	protected Point2D.Float position = new Point2D.Float(0, 0);

	protected long prevTime;

	/** The sight radius of the Unit, in game units */
	protected int sightRadius;

	protected int state;

	protected Vector4f velocity = new Vector4f(0, 0, 0, 1);

	public ControllableObject(Point2D.Float position, String objName, Player owner, boolean active)
	{
		this.animationFrame = 0;
		this.state = WAITING;
		this.objName = objName;
		this.active = active;
		this.position = position;
		this.owner = owner;
	}
	
	public Point2D.Float getPosition()
	{
		return position;
	}

	public int getBuildTime()
	{
		return buildTime;
	}

	public String getName()
	{
		return this.objName;
	}
	
	public void setActive(boolean status)
	{
		active = status;
	}

	public void setName(String objName)
	{
		this.objName = objName;
	}

	public void setPosition(float x, float y)
	{
		position.x = x;
		position.y = y;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public void takeDamage(int damage)
	{
		hp -= damage - armour;

		if (hp < 0)
			hp = 0;
	}
}
