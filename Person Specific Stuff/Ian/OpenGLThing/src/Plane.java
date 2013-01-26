/**
 * @author Ian Roukema	
 * @since November 10 2012
 * Description: Defines a plane.
 */

import org.lwjgl.util.vector.Vector3f;

public class Plane
{
	private Vector3f normal;
	private float d;
	private Vector3f point;
	
	public Plane(Vector3f normal, float d, Vector3f point)
	{
		this.normal = normal;
		this.d = d;
		this.point = point;
	}
	
	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}
	
	public void setD(float d)
	{
		this.d = d;
	}
	
	public void setPoint(Vector3f point)
	{
		this.point = point;
	}
	
	public Vector3f getNormal()
	{
		return normal;
	}
	
	public Vector3f getPoint()
	{
		return point;
	}
	
	public float getD()
	{
		return d;
	}
}
