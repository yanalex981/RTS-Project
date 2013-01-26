/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Defines a line.
 */

import org.lwjgl.util.vector.Vector3f;

public class Line
{
	Vector3f point;
	Vector3f vector;
	
	public Line(Vector3f point, Vector3f vector)
	{
		this.point = point;
		this.vector = vector;
	}
}
