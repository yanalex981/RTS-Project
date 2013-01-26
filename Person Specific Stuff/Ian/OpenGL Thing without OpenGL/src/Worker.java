import java.awt.geom.Point2D;

import org.lwjgl.util.vector.Vector3f;

public class Worker extends Unit
{
	int buildTime;
	Building buildObject;
	boolean building;

	public Worker(Point2D.Float position, float speed, float collisionRadius, int sightRadius, Player owner, boolean active)
	{
		super(position, speed, collisionRadius, sightRadius, "Worker", owner, active);

		building = false;
		this.hp = 50;
	}

	public void build(Building buildObject)
	{
		this.buildObject = buildObject;
		building = true;
		buildTime = 1000;
	}

	public void updateBuild(int delta)
	{
		buildTime -= delta;
		if (buildTime <= 0 && building)
		{
			buildObject.setActive(true);
			building = false;
		}
	}
}
