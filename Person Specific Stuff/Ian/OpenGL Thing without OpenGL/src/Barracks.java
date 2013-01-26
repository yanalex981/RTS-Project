import java.awt.geom.Point2D;

import org.lwjgl.util.vector.Vector3f;


public class Barracks extends Building
{

	public Barracks(Point2D.Float position, Player owner, boolean active)
	{
		super(position, "Barracks", owner, active);
	}
}
