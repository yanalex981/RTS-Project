import java.awt.Point;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class Camera 
{
	Vector3f position = new Vector3f(0, 0, 0);
	Vector3f size = new Vector3f(0, 0, 0);
	public Camera()
	{
		
	}
	public void changeView(Vector3f position, Vector3f rotation)
	{
		Vector3f view = new Vector3f(0, 0, 0);
		
		view.x = (float) Math.sin(Math.toRadians(rotation.x));
		view.y = (float) Math.sin(Math.toRadians(rotation.y));
		view.z = (float) Math.cos(Math.toRadians(rotation.z));
		
		GLU.gluLookAt(position.x, position.y, position.z, view.x, view.y, view.z, 0, 1, 0);
	}
}
