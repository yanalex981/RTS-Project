/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Defines a face with three verticies.
 */

import org.lwjgl.util.vector.Vector3f;

public class TriangleFace
{
	public Vector3f vertex = new Vector3f();
	public Vector3f normal = new Vector3f();
	
	public TriangleFace(Vector3f vertex, Vector3f normal)
	{
		this.vertex = vertex;
		this.normal = normal;
	}
}
