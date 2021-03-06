/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Defines a face with four verticies.
 */

import org.lwjgl.util.vector.Vector4f;

public class QuadFace 
{
	public Vector4f vertex = new Vector4f();
	public Vector4f normal = new Vector4f();
	
	public QuadFace(Vector4f vertex, Vector4f normal)
	{
		this.vertex = vertex;
		this.normal = normal;
	}
}
