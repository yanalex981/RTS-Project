import java.awt.geom.Point2D;

import org.lwjgl.util.vector.Vector3f;


public class Marine extends Unit
{

	public Marine(Point2D.Float position, float speed, float collisionRadius, int sightRadius, Player owner, boolean active)
	{
		super(position, speed, collisionRadius, sightRadius, "Marine", owner, active);

		this.buildTime = 1500;
		this.hp = 100;
	}

}
