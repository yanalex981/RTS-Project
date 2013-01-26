import java.awt.geom.Point2D;

import org.lwjgl.util.vector.Vector3f;

public class CommandCenter extends Building
{

	public CommandCenter(Point2D.Float position, Player owner, boolean active)
	{
		super(position, "Command Center", owner, active);
	}
}
